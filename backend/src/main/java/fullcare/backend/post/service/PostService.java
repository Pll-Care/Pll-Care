package fullcare.backend.post.service;

import fullcare.backend.global.State;
import fullcare.backend.global.errorcode.PostErrorCode;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.EntityNotFoundException;
import fullcare.backend.likes.domain.Likes;
import fullcare.backend.likes.repository.LikesRepository;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.domain.Recruitment;
import fullcare.backend.post.dto.request.PostCreateRequest;
import fullcare.backend.post.dto.request.PostUpdateRequest;
import fullcare.backend.post.dto.request.RecruitInfo;
import fullcare.backend.post.dto.response.MyPostResponse;
import fullcare.backend.post.dto.response.PostDetailResponse;
import fullcare.backend.post.dto.response.PostListResponse;
import fullcare.backend.post.repository.PostRepository;
import fullcare.backend.post.repository.RecruitmentRepository;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.repository.ProjectRepository;
import fullcare.backend.util.CustomPageImpl;
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

    private final ProjectRepository projectRepository;
    private final LikesRepository likesRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    public Post createPost(PostCreateRequest request, Member member) {
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new EntityNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));

        Post newPost = Post.createNewPost()
                .project(project)
                .author(member)
                .title(request.getTitle())
                .description(request.getDescription())
                .recruitStartDate(request.getRecruitStartDate())
                .recruitEndDate(request.getRecruitEndDate())
                .contact(request.getContact())
                .reference(request.getReference())
                .region(request.getRegion())
                .techStack(request.getTechStack())
                .state(State.ONGOING)
                .build();

        List<RecruitInfo> recruitInfo = request.getRecruitInfo();
        List<Recruitment> recruitments = recruitInfo.stream().map(r -> Recruitment.createNewRecruitment()
                .post(newPost)
                .recruitPosition(r.getPosition())
                .currentAmount(r.getCurrentCnt())
                .totalAmount(r.getTotalCnt())
                .build()).toList();

        newPost.updateRecruit(recruitments);
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
                request.getReference(), request.getContact(), request.getRegion(), recruitments);
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.POST_NOT_FOUND));
    }

    public PostDetailResponse findPostDetailResponse(Long memberId, Long postId) {
        PostDetailResponse postDetailResponse = postRepository.findPostDto(memberId, postId).orElseThrow(() -> new EntityNotFoundException(PostErrorCode.POST_NOT_FOUND));

        List<Recruitment> recruitments = recruitmentRepository.findByPostId(postDetailResponse.getPostId());
        postDetailResponse.setRecruitInfoList(recruitments.stream().map(r -> new RecruitInfo(r)).toList());

        return postDetailResponse;
    }

    public CustomPageImpl<PostListResponse> findPostList(Long memberId, Pageable pageable) {
        Page<PostListResponse> result = postRepository.findList(memberId, pageable);
        List<PostListResponse> content = result.getContent();

        List<Long> postIds = content.stream().map(p -> p.getPostId()).collect(Collectors.toList());

        List<Recruitment> recruitments = recruitmentRepository.findByPostIds(postIds);
        Map<Long, List<Recruitment>> recruitMap = recruitments.stream().collect(Collectors.groupingBy(r -> r.getPost().getId()));
        content.forEach(p -> p.setRecruitInfoList(recruitMap.get(p.getPostId()).stream().map(r -> new RecruitInfo(r)).toList()));

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
