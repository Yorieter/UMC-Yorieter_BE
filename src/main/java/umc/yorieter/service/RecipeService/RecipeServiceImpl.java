package umc.yorieter.service.RecipeService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.config.security.util.SecurityUtil;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.domain.mapping.RecipeLike;
import umc.yorieter.payload.exception.GeneralException;
import umc.yorieter.payload.status.ErrorStatus;
import umc.yorieter.repository.MemberRepository;
import umc.yorieter.repository.RecipeLikeRepository;
import umc.yorieter.repository.RecipeRepository;
import umc.yorieter.service.ImageUploadService.ImageUploadService;
import umc.yorieter.web.dto.request.RecipeRequestDTO;
import umc.yorieter.web.dto.response.RecipeResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Component
@Slf4j
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;
    private final RecipeLikeRepository recipeLikeRepository;
    private final ImageUploadService imageUploadService;

    // 레시피 작성
    @Override
    public RecipeResponseDTO.DetailRecipeDTO createRecipe(RecipeRequestDTO.CreateRecipeDTO createRecipeDTO, MultipartFile image) {
        // 회원 확인
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_EXIST_ERROR));

        // 이미지 업로드 처리
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = imageUploadService.uploadImage(image);
        }

        // DTO에서 데이터 가져와 Recipe 객제 생성
        Recipe recipe = Recipe.builder()
                .member(member)
                .title(createRecipeDTO.getTitle())
                .description(createRecipeDTO.getDescription())
                .calories(createRecipeDTO.getCalories())
                .build();

        // 이미지 URL 설정
        if (imageUrl != null) {
            recipe.updateRecipeImageUrl(imageUrl);  // 이미지 URL 업데이트
        }

        // 레시피 저장
        Recipe savedRecipe = recipeRepository.save(recipe);

        return RecipeResponseDTO.DetailRecipeDTO.builder()
                .recipeId(savedRecipe.getId())
                .memberId(savedRecipe.getMember().getId())
                .title(savedRecipe.getTitle())
                .description(savedRecipe.getDescription())
                .calories(savedRecipe.getCalories())
                .imageUrl(savedRecipe.getRecipeImage() != null ? savedRecipe.getRecipeImage().getUrl() : null)
                .build();
    }

    // 레시피 전체 조회 (생성시간순 정렬) 추후 좋아요순으로 변경 필요
    @Override
    public RecipeResponseDTO.AllRecipeListDto getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        List<RecipeResponseDTO.DetailRecipeDTO> detailRecipeDTOs = recipes.stream()
                .map(recipe -> RecipeResponseDTO.DetailRecipeDTO.builder()
                        .recipeId(recipe.getId())
                        .memberId(recipe.getMember().getId())
                        .title(recipe.getTitle())
                        .description(recipe.getDescription())
                        .calories(recipe.getCalories())
                        .imageUrl(recipe.getRecipeImage().getUrl())
                        .build())
                .collect(Collectors.toList());

        return RecipeResponseDTO.AllRecipeListDto.builder()
                .recipeList(detailRecipeDTOs)
                .build();
    }

    // 레시피 (상세)조회
    @Override
    public RecipeResponseDTO.DetailRecipeDTO getRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_EXIST_ERROR));

        return RecipeResponseDTO.DetailRecipeDTO.builder()
                .recipeId(recipe.getId())
                .memberId(recipe.getMember().getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .calories(recipe.getCalories())
                .imageUrl(recipe.getRecipeImage().getUrl())
                .build();
    }

    // 레시피 수정 <- memberId 어디에 쓰일건지 확인
    @Override
    public void updateRecipe(Long memberId, Long recipeId, RecipeRequestDTO.UpdateRecipeDTO updateRecipeDTO) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_EXIST_ERROR));

        recipe.setTitle(updateRecipeDTO.getTitle());
        recipe.setDescription(updateRecipeDTO.getDescription());
        recipe.setCalories(updateRecipeDTO.getCalories());

        recipeRepository.save(recipe);
    }


    // 레시피 삭제
    @Override
    public void deleteRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_EXIST_ERROR));

        // 외래키 제약 조건 -> 레시피를 참조하는 ""좋아요 테이블 데이터를 먼저 삭제""
        List<RecipeLike> recipeLikes = recipeLikeRepository.findAllByRecipeId(recipeId);
        recipeLikeRepository.deleteAll(recipeLikes);

        recipeRepository.delete(recipe);
    }


    // 레시피 좋아요
    @Override
    public void addLike(Long memberId, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_EXIST_ERROR));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_EXIST_ERROR));

        // 내가 쓴 글 좋아요 못 누르게
        if (recipe.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.CANNOT_LIKE_OWN_RECIPE_ERROR);
        }
        // 이미 좋아요 눌러져 있는지 확인
        if (recipeLikeRepository.findByMemberAndRecipe(member, recipe).isPresent()) {
            throw new GeneralException(ErrorStatus.RECIPE_LIKE_ALREADY_ERROR);
        }

        RecipeLike recipeLike = RecipeLike.builder()
                .member(member)
                .recipe(recipe)
                .build();

        recipeLikeRepository.save(recipeLike);
    }


    // 레시피 좋아요 해제
    @Override
    public void deleteLike(Long memberId, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_EXIST_ERROR));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_EXIST_ERROR));

        RecipeLike recipeLike = recipeLikeRepository.findByMemberAndRecipe(member, recipe)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPELIKE_NOT_EXIST_ERROR));

        recipeLikeRepository.delete(recipeLike);

    }
}
