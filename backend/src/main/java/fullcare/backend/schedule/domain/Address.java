package fullcare.backend.schedule.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Embeddable
public class Address {
    private String city;
    private String street;


}
