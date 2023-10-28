package fullcare.backend.profile.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    @Email
    private String email;
    @Email
    private String github;
    @URL
    private String websiteUrl;
}
