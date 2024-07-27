package umc.yorieter.service.ImageUploadService;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.payload.exception.GeneralException;
import umc.yorieter.payload.status.ErrorStatus;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadImage(MultipartFile file) {

        if (file == null) {
            return null;
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String fileName = file.getOriginalFilename();
        try {
            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new GeneralException(ErrorStatus.INTERNAL_ERROR, e.getMessage());
        }

        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }
}
