package umc.yorieter.service.ImageUploadService;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

    String uploadImage(MultipartFile file);
}
