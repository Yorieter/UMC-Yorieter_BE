package umc.yorieter.service.RecipeService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.config.security.util.SecurityUtil;
import umc.yorieter.domain.Ingredient;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.domain.mapping.RecipeLike;
import umc.yorieter.domain.mapping.Recipe_Ingredient;
import umc.yorieter.payload.exception.GeneralException;
import umc.yorieter.payload.status.ErrorStatus;
import umc.yorieter.repository.*;
import umc.yorieter.service.ImageUploadService.ImageUploadService;
import umc.yorieter.web.dto.request.RecipeRequestDTO;
import umc.yorieter.web.dto.response.RecipeResponseDTO;

import java.util.ArrayList;
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
    private final IngredientRepository ingredientRepository;

    // 레시피 & 식재료 함께 조회 (JPQL 메소드)
    public Recipe getRecipeWithIngredients(Long recipeId) {
        return recipeRepository.findRecipeWithIngredients(recipeId);
    }


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
                .recipeIngredientList(new ArrayList<>()) // 식재료 초기화 추가
                .build();


        // 이미지 URL 설정
        if (imageUrl != null) {
            recipe.updateRecipeImageUrl(imageUrl);  // 이미지 URL 업데이트
        }

        // 식재료 추가
        List<Recipe_Ingredient> recipeIngredientList = new ArrayList<>();
        for (String ingredientName : createRecipeDTO.getIngredientNames()) {
            // 식재료 찾고 없으면 새로 생성
            Ingredient ingredient = ingredientRepository.findByName(ingredientName)
                    .orElseGet(() -> ingredientRepository.save(new Ingredient(null, ingredientName, null))); // 이름만 새로 저장 ..

            // Recipe_Ingredient 객체 생성
            Recipe_Ingredient recipeIngredient = Recipe_Ingredient.builder()
                    .recipe(recipe)
                    .ingredient(ingredient)
                    .build();

            // recipeIngredientList에 추가
            recipeIngredientList.add(recipeIngredient);
        }

        // Recipe에 식재료 리스트 추가
        recipe.getRecipeIngredientList().addAll(recipeIngredientList);


        // 레시피 저장
        Recipe savedRecipe = recipeRepository.save(recipe);


        return RecipeResponseDTO.DetailRecipeDTO.builder()
                .recipeId(savedRecipe.getId())
                .memberId(savedRecipe.getMember().getId())
                .title(savedRecipe.getTitle())
                .description(savedRecipe.getDescription())
                .calories(savedRecipe.getCalories())
                .imageUrl(savedRecipe.getRecipeImage() != null ? savedRecipe.getRecipeImage().getUrl() : null)
                .ingredientNames(createRecipeDTO.getIngredientNames()) // 식재료 리스트 추가
                .build();
    }



    // 레시피 전체 조회 (생성시간순 정렬) 추후 좋아요순으로 변경 필요
    @Override
    public RecipeResponseDTO.AllRecipeListDto getAllRecipes() {
        // 레시피 & 식재료 함께 조회
        List<Recipe> recipes = recipeRepository.findAllWithIngredients();
        List<RecipeResponseDTO.DetailRecipeDTO> detailRecipeDTOs = recipes.stream()
                .map(recipe -> {
                    // 식재료 리스트 가져오기
                    List<String> ingredientNames = recipe.getRecipeIngredientList().stream()
                            .map(recipeIngredient -> recipeIngredient.getIngredient().getName()) // 각 재료의 이름을 추출
                            .collect(Collectors.toList());

                    // DetailRecipeDTO 생성
                    return RecipeResponseDTO.DetailRecipeDTO.builder()
                            .recipeId(recipe.getId())
                            .memberId(recipe.getMember().getId())
                            .title(recipe.getTitle())
                            .description(recipe.getDescription())
                            .calories(recipe.getCalories())
                            .imageUrl(recipe.getRecipeImage().getUrl())
                            .ingredientNames(ingredientNames) // 식재료 리스트 추가
                            .createdAt(recipe.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return RecipeResponseDTO.AllRecipeListDto.builder()
                .recipeList(detailRecipeDTOs)
                .build();
    }

    // 레시피 (상세)조회
    @Override
    public RecipeResponseDTO.DetailRecipeDTO getRecipe(Long recipeId) {
        // 레시피 & 식재료 함께 조회
        Recipe recipe = getRecipeWithIngredients(recipeId);

        if (recipe == null) {
            throw new GeneralException(ErrorStatus.RECIPE_NOT_EXIST_ERROR);
        }

        // 식재료 리스트 가져오기
        List<String> ingredientNames = recipe.getRecipeIngredientList().stream()
                .map(recipeIngredient -> recipeIngredient.getIngredient().getName())
                .toList();


        return RecipeResponseDTO.DetailRecipeDTO.builder()
                .recipeId(recipe.getId())
                .memberId(recipe.getMember().getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .calories(recipe.getCalories())
                .imageUrl(recipe.getRecipeImage().getUrl())
                .ingredientNames(ingredientNames) // 식재료 리스트 추가
                .createdAt(recipe.getCreatedAt())
                .build();
    }




    // 레시피 수정
    @Override
    public RecipeResponseDTO.DetailRecipeDTO updateRecipe(Long recipeId, RecipeRequestDTO.UpdateRecipeDTO updateRecipeDTO, MultipartFile image) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        // 레시피 & 식재료 함께 조회
        Recipe recipe = getRecipeWithIngredients(recipeId);

        if (recipe == null) {
            throw new GeneralException(ErrorStatus.RECIPE_NOT_EXIST_ERROR);
        }

        // 작성자 본인만 수정 가능하도록
        if (!recipe.getMember().getId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.NO_EDIT_DELETE_PERMISSION);
        }

        // updateRecipeDto 있는 경우
        if (updateRecipeDTO != null) {
            recipe.update(updateRecipeDTO);
        }

        // 이미지 있는 경우
        if (image != null && !image.isEmpty()) {
            String imageUrl = imageUploadService.uploadImage(image);
            recipe.updateRecipeImageUrl(imageUrl);
        }

        recipeRepository.save(recipe);

        // 식재료 리스트 가져오기
        List<String> ingredientNames = recipe.getRecipeIngredientList().stream()
                .map(recipeIngredient -> recipeIngredient.getIngredient().getName())
                .toList();

        return RecipeResponseDTO.DetailRecipeDTO.builder()
                .recipeId(recipe.getId())
                .memberId(recipe.getMember().getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .calories(recipe.getCalories())
                .imageUrl(recipe.getRecipeImage().getUrl())
                .ingredientNames(ingredientNames)  // 식재료 리스트 추가
                .build();
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
    public void addLike(Long recipeId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_EXIST_ERROR));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_EXIST_ERROR));

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
    public void deleteLike(Long recipeId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_EXIST_ERROR));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_EXIST_ERROR));

        RecipeLike recipeLike = recipeLikeRepository.findByMemberAndRecipe(member, recipe)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPELIKE_NOT_EXIST_ERROR));

        recipeLikeRepository.delete(recipeLike);

    }
}
