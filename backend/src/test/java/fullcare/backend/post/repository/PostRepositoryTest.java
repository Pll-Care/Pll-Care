package fullcare.backend.post.repository;

import fullcare.backend.post.domain.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;


    @Test
    public void test() {

        List<Post> list = postRepository.findList(1L);

        for (Post post : list) {
            System.out.println("post = " + post);
        }
    }
}