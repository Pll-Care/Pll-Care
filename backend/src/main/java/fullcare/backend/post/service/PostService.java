package fullcare.backend.post.service;

import fullcare.backend.apply.domain.Apply;
import fullcare.backend.apply.repository.ApplyRepository;
import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.MemberErrorCode;
import fullcare.backend.global.errorcode.PostErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.*;
import fullcare.backend.likes.domain.Likes;
import fullcare.backend.likes.repository.LikesRepository;
import fullcare.backend.main.dto.CloseDeadlinePostResponse;
import fullcare.backend.main.dto.MostLikedPostResponse;
import fullcare.backend.main.dto.UpToDatePostResponse;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.dto.request.PostCreateRequest;
import fullcare.backend.post.dto.request.PostUpdateRequest;
import fullcare.backend.post.dto.response.MyPostResponse;
import fullcare.backend.post.dto.response.PostDetailResponse;
import fullcare.backend.post.dto.response.PostListResponse;
import fullcare.backend.post.repository.PostRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.ProjectApplyRequest;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import fullcare.backend.recruitment.domain.RecruitInfo;
import fullcare.backend.recruitment.domain.Recruitment;
import fullcare.backend.recruitment.repository.RecruitmentRepository;
import fullcare.backend.s3.S3Service;
import fullcare.backend.util.CustomPageImpl;
import fullcare.backend.util.TechStackUtil;
import fullcare.backend.util.dto.TechStackDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final ApplyRepository applyRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;

    private final ProjectService projectService;
    private final S3Service s3Service;

    @Transactional
    public Long createPost(Long memberId, PostCreateRequest request) {
        try {
            ProjectMember findProjectMember = projectService.isProjectAvailable(request.getProjectId(), memberId, false);
            Project findProject = findProjectMember.getProject();

            if (!(request.getRecruitStartDate().isAfter(findProject.getStartDate()) && request.getRecruitEndDate().isBefore(findProject.getEndDate()))) {
                throw new InvalidDateRangeException(PostErrorCode.INVALID_RECRUITMENT_DATE_RANGE); // * 모집기간이 프로젝트 일정에 포함되는 기간인지 검증 (프로젝트 시작 < 모집 시작 + 모집 종료 < 프로젝트 종료)
            }

            Post newPost = Post.createNewPost()
                    .project(findProjectMember.getProject())
                    .author(findProjectMember)
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .recruitStartDate(request.getRecruitStartDate())
                    .recruitEndDate(request.getRecruitEndDate())
                    .contact(request.getContact())
                    .reference(request.getReference())
                    .region(request.getRegion())
                    .techStack(TechStackUtil.listToString(request.getTechStack()))
                    .state(State.ONGOING)
                    .build();

            List<RecruitInfo> recruitInfo = request.getRecruitInfo();
            List<Recruitment> recruitments = recruitInfo.stream().map(r -> Recruitment.createNewRecruitment()
                    .post(newPost)
                    .recruitPosition(r.getPosition())
                    .currentAmount(r.getCurrentCnt())
                    .totalAmount(r.getTotalCnt())
                    .build()).collect(Collectors.toList());

            newPost.updateRecruitments(recruitments);
            return postRepository.save(newPost).getId();

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(PostErrorCode.INVALID_CREATE);

        }
    }


    @Transactional
    public void updatePost(Long memberId, Long postId, PostUpdateRequest request) {
        try {
            Post findPost = findPostWithRecruitmentsAndProject(postId);
            ProjectMember findProjectMember = projectService.isProjectAvailable(findPost.getProject().getId(), memberId, false);

            if (!findPost.getAuthor().getId().equals(findProjectMember.getId()) && !findProjectMember.isLeader()) {
                throw new UnauthorizedAccessException(PostErrorCode.UNAUTHORIZED_MODIFY);
            }

            if (request.getRecruitStartDate().isAfter(request.getRecruitEndDate())) {
                throw new InvalidDateRangeException(PostErrorCode.INVALID_DATE_RANGE); // todo 모집 날짜 자체에 대한 검증 (프로젝트 일정에 포함되는 기간인지 검증 X)
            }

            Project findProject = findPost.getProject();

            if (!(request.getRecruitStartDate().isAfter(findProject.getStartDate()) && request.getRecruitEndDate().isBefore(findProject.getEndDate()))) {
                throw new InvalidDateRangeException(PostErrorCode.INVALID_RECRUITMENT_DATE_RANGE); // * 모집기간이 프로젝트 일정에 포함되는 기간인지 검증 (프로젝트 시작 < 모집 시작 + 모집 종료 < 프로젝트 종료)
            }

            List<RecruitInfo> recruitInfo = request.getRecruitInfo();
            List<Recruitment> recruitments = recruitInfo.stream().map(r -> Recruitment.createNewRecruitment()
                    .post(findPost)
                    .recruitPosition(r.getPosition())
                    .currentAmount(r.getCurrentCnt())
                    .totalAmount(r.getTotalCnt())
                    .build()).collect(Collectors.toList());

            findPost.updateAll(request.getTitle(), request.getDescription(),
                    request.getRecruitStartDate(), request.getRecruitEndDate(),
                    request.getReference(), request.getContact(), request.getRegion(), TechStackUtil.listToString(request.getTechStack()),
                    recruitments);

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(PostErrorCode.INVALID_MODIFY);
        }


    }

    @Transactional
    public void deletePost(Long memberId, Long postId) {
        try {
            Post findPost = findPostWithProject(postId);
            ProjectMember findProjectMember = projectService.isProjectAvailable(findPost.getProject().getId(), memberId, false);

            if (!findPost.getAuthor().getId().equals(findProjectMember.getId()) && !findProjectMember.isLeader()) {
                throw new UnauthorizedAccessException(PostErrorCode.UNAUTHORIZED_DELETE);
            }

            postRepository.delete(findPost);

        } catch (CompletedProjectException completedProjectException) {
            throw new CompletedProjectException(PostErrorCode.INVALID_DELETE);
        }

    }

    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.POST_NOT_FOUND));
    }

    public Post findPostWithProject(Long postId) {
        return postRepository.findPostWithProjectById(postId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.POST_NOT_FOUND));
    }

    public Post findPostWithRecruitmentsAndProject(Long postId) {
        return postRepository.findPostWithRecruitmentsAndProjectById(postId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.POST_NOT_FOUND));

    }

    public PostDetailResponse findPostDetailResponse(Long memberId, Long postId) {
        PostDetailResponse findPostDetailResponse = postRepository.findPostDetailResponse(memberId, postId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.POST_NOT_FOUND));

        List<TechStackDto> techStackList = TechStackUtil.stringToList(findPostDetailResponse.getTechStack()).stream()
                .map(t -> new TechStackDto(t.getValue(), s3Service.find(t.getValue(), t.getContentType()))).collect(Collectors.toList());
        findPostDetailResponse.setTechStackList(techStackList);

        List<Recruitment> recruitments = recruitmentRepository.findByPostId(findPostDetailResponse.getPostId());
        findPostDetailResponse.setRecruitInfoList(recruitments.stream().map(RecruitInfo::new).collect(Collectors.toList()));

        // ! sql에 null값이 전달되었을 때 문제가 없는가?
        Optional<Apply> findApply = applyRepository.findByPostIdAndMemberId(findPostDetailResponse.getPostId(), memberId);

        if (findApply.isPresent()) {
            ProjectMemberPositionType applyPosition = findApply.get().getPosition();
            findPostDetailResponse.setAvailable(false);
            findPostDetailResponse.setApplyPosition(applyPosition);
        } else {
            findPostDetailResponse.setAvailable(true);
        }

        // ! 작성자는 false
        if (findPostDetailResponse.getAuthorId().equals(memberId)) {
            findPostDetailResponse.setAvailable(false);
        }

        // ! 완료된 프로젝트는 available -> false
        if (findPostDetailResponse.getProjectState().equals(State.COMPLETE)) {
            findPostDetailResponse.setAvailable(false);
        }

        return findPostDetailResponse;
    }

    public CustomPageImpl<PostListResponse> findPostList(Long memberId, Pageable pageable) {
        Page<PostListResponse> result = postRepository.findListWithPaging(memberId, pageable);
        List<PostListResponse> content = result.getContent();

        content.forEach(postListResponse ->
                postListResponse.setTechStackList(TechStackUtil.stringToList(postListResponse.getTechStack()).stream()
                        .map(t -> new TechStackDto(t.getValue(), s3Service.find(t.getValue(), t.getContentType())))
                        .collect(Collectors.toList())));


        List<Long> postIds = content.stream().map(PostListResponse::getPostId).collect(Collectors.toList());
        List<Recruitment> recruitments = recruitmentRepository.findByPostIds(postIds);
        Map<Long, List<Recruitment>> recruitMap = recruitments.stream().collect(Collectors.groupingBy(r -> r.getPost().getId()));

        content.forEach(postListResponse -> postListResponse.setRecruitInfoList(recruitMap.getOrDefault(postListResponse.getPostId(), new ArrayList<>()).stream().map(RecruitInfo::new).collect(Collectors.toList())));

        return new CustomPageImpl<>(content, pageable, result.getTotalElements());
    }


    @Transactional
    public void likePost(Long memberId, Long postId) {
        Optional<Likes> findLikes = likesRepository.findByPostIdAndMemberId(postId, memberId);

        if (findLikes.isEmpty()) {
            Post findPost = findPost(postId);
            Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));

            Likes newLikes = findMember.like(findPost);
            likesRepository.save(newLikes);
        } else {
            likesRepository.delete(findLikes.get());
        }
    }

    @Transactional
    public void applyProjectByPost(Long memberId, Long postId, ProjectApplyRequest request) {
        Post findPost = findPostWithRecruitmentsAndProject(postId);

        // ! V1
//            Project project = projectService.findProject(findPost.getProject().getId());
//            project.getProjectMembers().stream().filter(pm -> pm.getMember().getId() == memberId).findAny().orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));

        // ! V2
        Optional<ProjectMember> findProjectMember = projectMemberRepository.findByProjectIdAndMemberId(findPost.getProject().getId(), memberId);

        if (findProjectMember.isPresent()) {
            throw new DuplicateProjectMemberException(ProjectErrorCode.DUPLICATE_PROJECT_MEMBER); // todo 이미 프로젝트에 소속된 사용자입니다.
        } else {
            Optional<Apply> findApply = applyRepository.findByPostIdAndMemberId(findPost.getId(), memberId);

            if (findApply.isEmpty()) {
                Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));

                Recruitment findRecruitment = findPost.getRecruitments().stream()
                        .filter(r -> r.getRecruitPosition() == request.getPosition()).findAny()
                        .orElseThrow(() -> new InvalidApplyException(PostErrorCode.RECRUITMENT_NOT_FOUND));

                if (findRecruitment.getCurrentAmount() < findRecruitment.getTotalAmount()) {
                    Apply newApply = findMember.apply(findPost, request.getPosition());
                    applyRepository.save(newApply);

                } else {
                    throw new InvalidApplyException(PostErrorCode.RECRUITMENT_COMPLETED);
                }

            } else {
                throw new UnauthorizedAccessException(PostErrorCode.DUPLICATE_APPLY);
            }
        }
    }

    @Transactional
    public void cancelApply(Long memberId, Long postId) {
        Post findPost = findPostWithProject(postId);

        // ! V1
//            Project project = projectService.findProject(findPost.getProject().getId());
//            project.getProjectMembers().stream().filter(pm -> pm.getMember().getId() == memberId).findAny().orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND));

        // ! V2
        Optional<ProjectMember> findProjectMember = projectMemberRepository.findByProjectIdAndMemberId(findPost.getProject().getId(), memberId);

        if (findProjectMember.isPresent()) {
            throw new UnauthorizedAccessException(ProjectErrorCode.DUPLICATE_PROJECT_MEMBER);
        } else {
            Apply findApply = applyRepository.findByPostIdAndMemberId(findPost.getId(), memberId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.APPLY_NOT_FOUND));
            applyRepository.delete(findApply);
        }


    }

    public CustomPageImpl<MyPostResponse> findMyPost(Long memberId, State state, Pageable pageable) {
        Page<Post> posts = postRepository.findListWithPagingByMemberId(memberId, state, pageable);
        List<MyPostResponse> content = posts.stream().map(p -> MyPostResponse.builder()
                        .postId(p.getId())
                        .title(p.getTitle())
                        .description(p.getDescription())
                        .state(p.getState())
                        .build())
                .collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, posts.getTotalElements());
    }

    public CustomPageImpl<MyPostResponse> findMyLikePost(Long memberId, Pageable pageable) {
        Page<Post> posts = postRepository.findLikePageByMemberId(memberId, pageable);
        List<MyPostResponse> content = posts.stream().map(p -> MyPostResponse.builder()
                        .postId(p.getId())
                        .title(p.getTitle())
                        .description(p.getDescription())
                        .state(p.getState())
                        .build())
                .collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, posts.getTotalElements());
    }

    public List<MostLikedPostResponse> findMostLikedPost() {
        List<Post> mostLikedPostList = postRepository.findTop5ByRecruitEndDateAfterOrderByLikeCountDescRecruitEndDateAsc(LocalDate.now());

        List<MostLikedPostResponse> content = mostLikedPostList.stream().map(p -> MostLikedPostResponse.builder()
                        .postId(p.getId())
                        .projectId(p.getProject().getId())
                        .projectTitle(p.getProject().getTitle())
                        .projectImageUrl(p.getProject().getImageUrl())
                        .recruitEndDate(p.getRecruitEndDate())
                        .likeCount(p.getLikeCount())
                        .title(p.getTitle())
                        .description(p.getProject().getDescription())
                        .build())
                .collect(Collectors.toList());

        return content;
    }

    public List<CloseDeadlinePostResponse> findCloseDeadlinePost() {
        List<Post> closeDeadlinePostList = postRepository.findTop5ByRecruitEndDateAfterOrderByRecruitEndDateAscCreatedDateAsc(LocalDate.now());

        List<CloseDeadlinePostResponse> content = closeDeadlinePostList.stream().map(p -> CloseDeadlinePostResponse.builder()
                        .postId(p.getId())
                        .projectId(p.getProject().getId())
                        .projectTitle(p.getProject().getTitle())
                        .projectImageUrl(p.getProject().getImageUrl())
                        .recruitEndDate(p.getRecruitEndDate())
                        .title(p.getTitle())
                        .description(p.getProject().getDescription())
                        .build())
                .collect(Collectors.toList());

        return content;
    }

    public List<UpToDatePostResponse> findUpToDateProject() {
        List<Post> upToDatePostList = postRepository.findTop5ByCreatedDateBeforeOrderByCreatedDateDescRecruitEndDateAsc(LocalDateTime.now());

        List<UpToDatePostResponse> content = upToDatePostList.stream().map(p -> UpToDatePostResponse.builder()
                        .postId(p.getId())
                        .projectId(p.getProject().getId())
                        .projectTitle(p.getProject().getTitle())
                        .projectImageUrl(p.getProject().getImageUrl())
                        .recruitEndDate(p.getRecruitEndDate())
                        .title(p.getTitle())
                        .description(p.getProject().getDescription())
                        .build())
                .collect(Collectors.toList());

        return content;
    }
}
