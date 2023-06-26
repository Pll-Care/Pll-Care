package fullcare.backend.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
//import fullcare.backend.newS3.S3UploaderService;
import com.amazonaws.services.s3.model.PutObjectResult;
import fullcare.backend.global.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Tag(name = "AWS S3 업로드", description = "AWS S3 업로드 관련 API")
@RestController
@RequestMapping(value = "/api/auth/upload", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UploadController {
    private final FileUploadService fileUploadService;
//    private final S3UploaderService s3UploaderService;
    private final AmazonS3 amazonS3;

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
//    @PostMapping("/1")
//    public ResponseEntity<FileDetail> post(
//            @RequestPart("file") MultipartFile multipartFile) {
//        return ResponseEntity.ok(fileUploadService.save(multipartFile));
//    }

//    @PostMapping("/image-upload")
//    public String imageUpload(@RequestPart("file") MultipartFile multipartFile) throws IOException {
//        return s3UploaderService.upload(multipartFile, "1-source", "image");
//    }

    // * 새로운 프로젝트 생성
    @Operation(method = "post", summary = "이미지 업로드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이미지 업로드 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "이미지 업로드 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/image")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("dir")String dir) {
        try {
            String fileName = file.getOriginalFilename();

            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, dir+"/" + fileName, file.getInputStream(), metadata);

            URL url1 = amazonS3Client.getUrl(bucket, dir+"/" + fileName);
            String resourceUrl = amazonS3Client.getResourceUrl(bucket, dir+"/" + fileName);
            URL url = amazonS3.getUrl(bucket, dir+"/" + fileName);
            System.out.println("resourceUrl = " + resourceUrl);
            System.out.println("url.getHost() = " + url.getHost());
            System.out.println("url.getPath() = " + url.getPath());
            System.out.println("url.getProtocol() = " + url.getProtocol());
            System.out.println("url.getFile() = " + url.getFile());
            System.out.println("url.getPort() = " + url.getPort());
            return ResponseEntity.ok(resourceUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}