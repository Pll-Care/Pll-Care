package fullcare.backend.main.controller;

import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.main.dto.CloseDeadlinePostResponse;
import fullcare.backend.main.dto.MostLikedPostResponse;
import fullcare.backend.main.dto.UpToDatePostResponse;
import fullcare.backend.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "메인 페이지", description = "메인 페이지 관련 API")
@RequestMapping("/api/main")
@RestController
public class MainController {

    private final PostService postService;

    @Operation(method = "get", summary = "실시간 인기 모집글 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "모집글 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/mostliked")
    public ResponseEntity<List<MostLikedPostResponse>> mostLiked() {
        List<MostLikedPostResponse> responses = postService.findMostLikedPost();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "마감임박 모집글 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "모집글 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/closedeadline")
    public ResponseEntity<List<CloseDeadlinePostResponse>> closeDeadline() {
        List<CloseDeadlinePostResponse> responses = postService.findCloseDeadlinePost();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "최신 모집글 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집글 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "모집글 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/uptodate")
    public ResponseEntity<List<UpToDatePostResponse>> upToDate() {
        List<UpToDatePostResponse> responses = postService.findUpToDateProject();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

}
