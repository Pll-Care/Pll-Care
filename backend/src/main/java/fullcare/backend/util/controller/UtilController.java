package fullcare.backend.util.controller;

import fullcare.backend.security.jwt.JwtTokenService;
import fullcare.backend.security.jwt.exception.CustomJwtException;
import fullcare.backend.security.jwt.exception.JwtErrorCode;
import fullcare.backend.util.TechStackUtil;
import fullcare.backend.util.dto.ReissueTokenResponse;
import fullcare.backend.util.dto.TechStackResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Tag(name = "유틸", description = "유틸 관련 API")
@RequestMapping("/api/auth/util")
@RestController
public class UtilController {

    private final TechStackUtil techStackUtil;
    private final JwtTokenService jwtTokenService;

    @Operation(method = "get", summary = "기술스택 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기술스택 검색 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "기술스택 검색 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping(value = "/techstack")
    public ResponseEntity<TechStackResponse> findTechStack(@RequestParam("tech") String tech) {

        TechStackResponse response = techStackUtil.findTechStack(tech);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "토큰 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "기술스택 검색 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping(value = "/reissuetoken")
    public ResponseEntity<ReissueTokenResponse> reissueToken(@RequestHeader("Authorization_refresh") String refreshToken) {
        if (!StringUtils.hasText(refreshToken) || !refreshToken.startsWith("Bearer ")) {
            throw new CustomJwtException(JwtErrorCode.TOKEN_NOT_FOUND);
        }

        String extractedRefreshToken = refreshToken.substring(7);

        try {
            jwtTokenService.validateJwtToken(extractedRefreshToken);
            String[] reIssueTokens = jwtTokenService.reIssueTokens(extractedRefreshToken);
            ReissueTokenResponse reissueTokenResponse = new ReissueTokenResponse(reIssueTokens);
            return new ResponseEntity(reissueTokenResponse, HttpStatus.OK);
            
        } catch (CustomJwtException e) {
            if (e.getErrorCode().equals(JwtErrorCode.EXPIRED_TOKEN)) {
                throw new CustomJwtException(JwtErrorCode.EXPIRED_REFRESH_TOKEN);
            } else {
                throw e;
            }
        }
    }
}
