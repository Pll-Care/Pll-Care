package fullcare.backend.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import fullcare.backend.s3.dto.UploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public UploadResponse upload(MultipartFile file, String dir){
        try {

            String fileName = file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, dir + "/" + fileName, file.getInputStream(), metadata);
            String resourceUrl = amazonS3Client.getResourceUrl(bucket, dir + "/" + fileName);
            return new UploadResponse(resourceUrl);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("업로드를 실패했습니다.");
        }
    }

    public void delete(String deleteUrl){
        try {
            URL url = new URL(deleteUrl);
            String keyName = url.getPath().substring(1);
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, keyName);
            if (isObjectExist) {
                amazonS3Client.deleteObject(bucket, keyName);
            } else {
                throw new RuntimeException("파일을 찾지 못했습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제를 실패했습니다.");
        }
    }
}