package fullcare.backend.post.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;

@Getter
@Embeddable
public class PostContent {


    @Lob
    @Column(name = "tech_stack", nullable = false)
    private String techStack;
}
