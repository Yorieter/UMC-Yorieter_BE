package umc.yorieter.service.ImageUploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.converter.RecipeImageConverter;
import umc.yorieter.domain.RecipeImage;
import umc.yorieter.repository.RecipeImageRepository;
import umc.yorieter.web.dto.request.RecipeImageRequestDto;
import umc.yorieter.web.dto.response.RecipeImageResponseDto;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecipeImageService {
    private final RecipeImageRepository recipeImageRepository;
    private final ImageUploadService imageUploadService;
    private final RecipeImageConverter recipeImageConverter;

    public RecipeImageResponseDto.RecipeImageDto uploadImage(RecipeImageRequestDto requestDto, MultipartFile image) {
        String imageUrl = imageUploadService.uploadImage(image);
        RecipeImage recipeImage = recipeImageConverter.toRecipeImage(imageUrl, requestDto);
        RecipeImage savedRecipeImage = recipeImageRepository.save(recipeImage);
        log.info("service:");
        return recipeImageConverter.toRecipeImageDto(savedRecipeImage);
    }
}