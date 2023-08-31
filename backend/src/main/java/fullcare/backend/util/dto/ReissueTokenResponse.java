package fullcare.backend.util.dto;

import lombok.Getter;

@Getter
public class ReissueTokenResponse {

    private String accessToken;
    private String refreshToken;

    public ReissueTokenResponse(String[] reIssueTokens) {
        this.accessToken = reIssueTokens[0];
        this.refreshToken = reIssueTokens[0];
    }
}
