package umc.yorieter.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.payload.ApiResponse;
import umc.yorieter.service.ImageUploadService.RecipeImageService;
import umc.yorieter.web.dto.request.RecipeImageRequestDto;
import umc.yorieter.web.dto.response.RecipeImageResponseDto.RecipeImageDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/test")
public class RecipeImageTestController {

    private final RecipeImageService recipeImageService;

    @PostMapping("")
    public ApiResponse<RecipeImageDto> uploadImage(
            @RequestPart RecipeImageRequestDto request,
            @RequestPart MultipartFile file) {

        return ApiResponse.onSuccess(recipeImageService.uploadImage(request, file));
    }
}
