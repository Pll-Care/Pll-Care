package fullcare.backend.post.service;

import fullcare.backend.apply.domain.Apply;
import fullcare.backend.apply.repository.ApplyRepository;
import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.GlobalErrorCode;
import fullcare.backend.global.errorcode.PostErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.DuplicateProjectMemberException;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.global.exceptionhandling.exception.InvalidAccessException;
import fullcare.backend.likes.domain.Likes;
import fullcare.backend.likes.repository.LikesRepository;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.domain.RecruitInfo;
import fullcare.backend.post.domain.Recruitment;
import fullcare.backend.post.dto.request.PostCreateRequest;
import fullcare.backend.post.dto.request.PostUpdateRequest;
import fullcare.backend.post.dto.response.MyPostResponse;
import fullcare.backend.post.dto.response.PostDetailResponse;
import fullcare.backend.post.dto.response.PostListResponse;
import fullcare.backend.post.repository.PostRepository;
import fullcare.backend.post.repository.RecruitmentRepository;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.domain.ProjectMemberPositionType;
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
    private final S3Service s3Service;

    @Transactional
    public Post createPost(ProjectMember projectMember, PostCreateRequest request) {
        Post newPost = Post.createNewPost()
                .project(projectMember.getProject())
                .author(projectMember.getMember())
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
                .currentAmount(0)
                .totalAmount(r.getTotalCnt())
                .build()).toList();

        newPost.updateRecruitments(recruitments);
        return postRepository.save(newPost);
    }


    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.POST_NOT_FOUND));

        List<RecruitInfo> recruitInfo = request.getRecruitInfo();
        List<Recruitment> recruitments = recruitInfo.stream().map(r -> Recruitment.createNewRecruitment()
                .post(post)
                .recruitPosition(r.getPosition())
                .currentAmount(r.getCurrentCnt())
                .totalAmount(r.getTotalCnt())
                .build()).toList();

        post.updateAll(request.getTitle(), request.getDescription(),
                request.getRecruitStartDate(), request.getRecruitEndDate(),
                request.getReference(), request.getContact(), request.getRegion(), TechStackUtil.listToString(request.getTechStack()), recruitments);
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.POST_NOT_FOUND));
    }

    public Post findPostWithRecruitments(Long postId) {
        return postRepository.findPostWithRecruitmentsById(postId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.POST_NOT_FOUND));

    }

    public PostDetailResponse findPostDetailResponse(Long memberId, Long postId) {
        PostDetailResponse postDetailResponse = postRepository.findPostDto(memberId, postId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.POST_NOT_FOUND));

        List<TechStackDto> techStackList = TechStackUtil.stringToList(postDetailResponse.getTechStack()).stream()
                .map(t -> new TechStackDto(t.getValue(), s3Service.find(t.getValue(), t.getContentType()))).collect(Collectors.toList());
        postDetailResponse.setTechStackList(techStackList);

        List<Recruitment> recruitments = recruitmentRepository.findByPostId(postDetailResponse.getPostId());
        postDetailResponse.setRecruitInfoList(recruitments.stream().map(r -> new RecruitInfo(r)).toList());

        return postDetailResponse;
    }

    public CustomPageImpl<PostListResponse> findPostList(Long memberId, Pageable pageable) {
        Page<PostListResponse> result = postRepository.findList(memberId, pageable);
        List<PostListResponse> content = result.getContent();

        content.forEach(postListResponse ->
                postListResponse.setTechStackList(TechStackUtil.stringToList(postListResponse.getTechStack()).stream()
                        .map(t -> new TechStackDto(t.getValue(), s3Service.find(t.getValue(), t.getContentType())))
                        .collect(Collectors.toList())));


        List<Long> postIds = content.stream().map(p -> p.getPostId()).collect(Collectors.toList());
        List<Recruitment> recruitments = recruitmentRepository.findByPostIds(postIds);
        Map<Long, List<Recruitment>> recruitMap = recruitments.stream().collect(Collectors.groupingBy(r -> r.getPost().getId()));

        content.forEach(postListResponse -> postListResponse.setRecruitInfoList(recruitMap.get(postListResponse.getPostId()).stream().map(r -> new RecruitInfo(r)).toList()));

        return new CustomPageImpl<>(content, pageable, result.getTotalElements());
    }


    @Transactional
    public void likePost(Post post, Member member) {
        Optional<Likes> findLikes = likesRepository.findByPostAndMember(post, member);

        if (!(findLikes.isPresent())) {
            Likes newLikes = Likes.createNewLikes()
                    .member(member)
                    .post(post)
                    .build();

            likesRepository.save(newLikes);
        } else {
            Likes likes = findLikes.get();
            likesRepository.delete(likes);
        }

    }

    @Transactional
    public void applyProjectByPost(Post post, Member member, ProjectMemberPositionType position) {
        Optional<Apply> findApply = applyRepository.findByPostAndMember(post, member);

        if (!(findApply.isPresent())) {

            Recruitment findRecruitment = post.getRecruitments().stream()
                    .filter(r -> r.getRecruitPosition() == position).findAny()
                    .orElseThrow(() -> new EntityNotFoundException(GlobalErrorCode.INVALID_ACCESS));// todo 모집중인 포지션이 아닙니다.

            if (findRecruitment.getCurrentAmount() < findRecruitment.getTotalAmount()) {
                Apply newApply = Apply.createNewApply()
                        .member(member)
                        .post(post)
                        .position(position)
                        .build();

                applyRepository.save(newApply);

            } else {
                throw new InvalidAccessException(GlobalErrorCode.INVALID_ACCESS); // todo 이미 모집이 완료된 포지션입니다. (총 모집인원을 넘어갈 수 없음)
            }

        } else {
            throw new DuplicateProjectMemberException(ProjectErrorCode.DUPLICATE_PROJECT_MEMBER); // todo 이미 프로젝트에 지원한 사용자입니다.
        }
    }

    @Transactional
    public void cancelApply(Post post, Member member) {
        Apply findApply = applyRepository.findByPostAndMember(post, member).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.APPLY_NOT_FOUND));// todo 프로젝트 지원 정보가 없습니다.
        applyRepository.delete(findApply);
    }

    public CustomPageImpl<MyPostResponse> findMyPost(Long memberId, State state, Pageable pageable) {
        Page<Post> posts = postRepository.findPageByMemberId(memberId, state, pageable);
        List<MyPostResponse> content = posts.stream().map(p -> MyPostResponse.builder()
                .postId(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .state(p.getState())
                .build()
        ).collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, posts.getTotalElements());
    }

    public CustomPageImpl<MyPostResponse> findMyLikePost(Long memberId, Pageable pageable) {
        Page<Post> posts = postRepository.findLikePageByMemberId(memberId, pageable);
        List<MyPostResponse> content = posts.stream().map(p -> MyPostResponse.builder()
                .postId(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .state(p.getState())
                .build()
        ).collect(Collectors.toList());

        return new CustomPageImpl<>(content, pageable, posts.getTotalElements());
    }
}
