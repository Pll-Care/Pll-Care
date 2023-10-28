package fullcare.backend.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import fullcare.backend.global.errorcode.AwsErrorCode;
import fullcare.backend.global.exceptionhandling.exception.NotFoundFileException;
import fullcare.backend.global.exceptionhandling.exception.UploadException;
import fullcare.backend.s3.dto.UploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String find(String fileName, String contentType){
        return amazonS3Client.getResourceUrl(bucket,  "techstack/" + fileName + "." + contentType);
    }
    public UploadResponse upload(MultipartFile file, String dir){
        UUID uuid = UUID.randomUUID();
        try {

            String fileName = file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, dir + "/" +uuid, file.getInputStream(), metadata);
            String resourceUrl = amazonS3Client.getResourceUrl(bucket, dir + "/" +uuid);
            return new UploadResponse(resourceUrl);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new UploadException(AwsErrorCode.UPLOAD_FAIL);
        }
    }

    public void delete(String deleteUrl) {
        if (deleteUrl != null && !deleteUrl.isEmpty()) {
            try {
                URL url = new URL(deleteUrl);
                String keyName = url.getPath().substring(1);
                System.out.println("keyName = " + keyName);
                boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, keyName);
                if (isObjectExist) {
                    amazonS3Client.deleteObject(bucket, keyName);
                } else {
                    throw new RuntimeException("파일을 찾지 못했습니다.");
                }
            } catch (Exception e) {
                throw new NotFoundFileException(AwsErrorCode.NOT_FOUND_FILE);
            }
        }
    }
}