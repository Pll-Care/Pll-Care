package fullcare.backend.s3;

import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.s3.dto.UploadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "AWS S3 업로드", description = "AWS S3 업로드 관련 API")
@RestController
@RequestMapping(value = "/api/auth/upload", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UploadController {
    private final UploadService uploadService;

    //* 이미지 관련 api
    @Operation(method = "post", summary = "이미지 업로드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이미지 업로드 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "이미지 업로드 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/image")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("dir") String dir) {
        UploadResponse response = uploadService.upload(file, dir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "delete", summary = "이미지 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이미지 삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "이미지 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })

    @DeleteMapping("/image")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("url") String url) {
        uploadService.delete(url);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}