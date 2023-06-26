package fullcare.backend.post.service;

import fullcare.backend.global.State;
import fullcare.backend.likes.domain.Likes;
import fullcare.backend.likes.repository.LikesRepository;
import fullcare.backend.member.domain.Member;
import fullcare.backend.member.repository.MemberRepository;
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
import jakarta.persistence.EntityNotFoundException;
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
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    public Post createPost(PostCreateRequest request, Long memberId) {
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트 정보가 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("해당 사용자 정보가 없습니다."));


        Post newPost = Post.createNewPost()
                .project(project)
                .author(member)
                .title(request.getTitle())
                .description(request.getDescription())
                .contact(request.getContact())
                .reference(request.getReference())
                .region(request.getRegion())
                .techStack(request.getTechStack())
                .state(State.TBD)
                .build();

        List<RecruitInfo> recruitInfo = request.getRecruitInfo();
        List<Recruitment> recruitments = recruitInfo.stream().map(r -> Recruitment.createNewRecruitment()
                .post(newPost)
                .recruitPosition(r.getPosition())
                .amount(r.getCnt())
                .build()).toList();

        newPost.updateRecruit(recruitments);
        return postRepository.save(newPost);
    }


    @Transactional
    public void updatePost(Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("해당 모집글이 존재하지 않습니다."));

        List<RecruitInfo> recruitInfo = request.getRecruitInfo();
        List<Recruitment> recruitments = recruitInfo.stream().map(r -> Recruitment.createNewRecruitment()
                .post(post)
                .recruitPosition(r.getPosition())
                .amount(r.getCnt())
                .build()).toList();

        post.updateAll(request.getTitle(), request.getReference(),
                request.getContact(), request.getDescription(),
                request.getRegion(), recruitments);
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("해당 모집글이 존재하지 않습니다."));
    }

    public PostDetailResponse findPostDetailResponse(Long postId) {
        Post findPost = findPost(postId);
        PostDetailResponse postDetailResponse = PostDetailResponse.entityToDto(findPost);

        List<Recruitment> recruitments = recruitmentRepository.findByPostId(findPost.getId());
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


        //            throw new InvalidAccessException("이미 좋아요 처리한 모집글입니다.");

    }

    public CustomPageImpl<MyPostResponse> findMyPost(Long memberId, Pageable pageable){
        Page<Post> posts = postRepository.findPageByMemberId(memberId, pageable);
        List<MyPostResponse> content = posts.stream().map(p -> MyPostResponse.builder()
                .postId(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .state(p.getState())
                .build()
        ).collect(Collectors.toList());
        return new CustomPageImpl<>(content, pageable, posts.getTotalElements());
    }

    public CustomPageImpl<MyPostResponse> findMyLikePost(Long memberId, Pageable pageable){
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
