package fullcare.backend.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FailureResponse {

    private String errorCode;
    private String description;

}
