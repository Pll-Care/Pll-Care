package fullcare.backend.member;

import fullcare.backend.member.domain.Member;
import fullcare.backend.member.dto.MemberIdResponse;
import fullcare.backend.member.dto.MemberImageResponse;
import fullcare.backend.profile.dto.response.ProfileImageResponse;
import fullcare.backend.profile.service.ProfileService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "멤버", description = "멤버 관련 API")
@RequestMapping("/api/auth/member")
@RestController
public class MemberController {
    private final ProfileService profileService;

    @Operation(method = "get", summary = "프로필 이미지 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 이미지 조회 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "프로필 이미지 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/image")
    public ResponseEntity<ProfileImageResponse> imageProfile(@CurrentLoginMember Member member) {
        ProfileImageResponse response = profileService.findProfileImage(member.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "아이디만 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디만 조회 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "아이디만 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/onlyid")
    public ResponseEntity<MemberIdResponse> getId(@CurrentLoginMember Member member) {
        return new ResponseEntity<>(new MemberIdResponse(member.getId()), HttpStatus.OK);
    }

    @Operation(method = "get", summary = "이미지만 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지만 조회 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "이미지만 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping("/onlyimage")
    public ResponseEntity<MemberImageResponse> getImageUrl(@CurrentLoginMember Member member) {
        return new ResponseEntity<>(new MemberImageResponse(member.getImageUrl()), HttpStatus.OK);
    }
}