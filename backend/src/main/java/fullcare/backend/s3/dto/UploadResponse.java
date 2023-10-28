package fullcare.backend.s3.dto;

import lombok.Data;

@Data
public class UploadResponse {

    String imageUrl;
    public UploadResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
