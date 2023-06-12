package fullcare.backend.post.service;

import fullcare.backend.global.State;
import fullcare.backend.likes.domain.Likes;
import fullcare.backend.likes.repository.LikesRepository;
import fullcare.backend.member.domain.Member;
import fullcare.backend.post.domain.Post;
import fullcare.backend.post.domain.Recruitment;
import fullcare.backend.post.dto.request.PostCreateRequest;
import fullcare.backend.post.dto.request.PostUpdateRequest;
import fullcare.backend.post.dto.request.RecruitInfo;
import fullcare.backend.post.dto.response.PostDetailResponse;
import fullcare.backend.post.dto.response.PostListResponse;
import fullcare.backend.post.repository.PostRepository;
import fullcare.backend.post.repository.RecruitmentRepository;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.repository.ProjectMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final LikesRepository likesRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    public Post createPost(PostCreateRequest request, Long memberId) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(request.getProjectId(), memberId).orElseThrow(() -> new EntityNotFoundException("해당 프로젝트 멤버 정보가 없습니다."));

        Post newPost = Post.createNewPost()
                .projectMember(projectMember)
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
        PostDetailResponse postDetailResponse = postRepository.findPostDetailDtoById(postId).orElseThrow(() -> new EntityNotFoundException("해당 모집글이 존재하지 않습니다."));

        List<Recruitment> recruitments = recruitmentRepository.findByPostId(postDetailResponse.getPostId());
        postDetailResponse.setRecruitInfoList(recruitments.stream().map(r -> new RecruitInfo(r)).toList());

        return postDetailResponse;
    }

    public Page<PostListResponse> findPostList(Long memberId, Pageable pageable) {
        Page<PostListResponse> postListResponsePage = postRepository.findList(memberId, pageable);

        List<PostListResponse> content = postListResponsePage.getContent();

        List<Long> postIds = content.stream().map(p -> p.getPostId()).collect(Collectors.toList());
        List<Recruitment> recruitments = recruitmentRepository.findByPostIds(postIds);

        Map<Long, List<Recruitment>> collect = recruitments.stream().collect(Collectors.groupingBy(r -> r.getPost().getId()));
        content.stream().forEach(l -> l.setRecruitInfoList(collect.get(l.getPostId()).stream().map(r -> new RecruitInfo(r)).toList()));

        return postListResponsePage;
    }


    @Transactional
    public void likePost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("해당 모집글이 존재하지 않습니다."));

        if (!(likesRepository.existsByPostIdAndMemberId(postId, member.getId()))) {
            Likes newLikes = Likes.createNewLikes()
                    .member(member)
                    .post(post)
                    .build();

            likesRepository.save(newLikes);
        } else {
            Likes likes = likesRepository.findByPostIdAndMemberId(postId, member.getId()).get();
            likesRepository.delete(likes);
        }


        //            throw new InvalidAccessException("이미 좋아요 처리한 모집글입니다.");

    }
}
