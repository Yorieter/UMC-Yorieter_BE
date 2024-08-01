package umc.yorieter.service.RecipeService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
import umc.yorieter.web.dto.response.IngredientResponseDTO;
import umc.yorieter.web.dto.response.RecipeResponseDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Component
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;
    private final RecipeLikeRepository recipeLikeRepository;
    private final ImageUploadService imageUploadService;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    RestTemplate restTemplate = new RestTemplate();

    @Value("${api.service.key}")
    private String serviceKey;

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

        // DTO에서 데이터 가져와 Recipe 객체 생성
        Recipe recipe = Recipe.builder()
                .member(member)
                .title(createRecipeDTO.getTitle())
                .description(createRecipeDTO.getDescription())
                .calories(0)
                .recipeIngredientList(new ArrayList<>())
                .build();

        // 이미지 URL 설정
        if (imageUrl != null) {
            recipe.updateRecipeImageUrl(imageUrl); // 이미지 URL 업데이트
        }

        // Recipe 저장
        Recipe savedRecipe = recipeRepository.save(recipe);

        // 식재료 추가 및 저장
        List<Recipe_Ingredient> recipeIngredientList = createRecipeDTO.getIngredientList().stream()
                .map(ingredientRequestDTO -> {
                    // searchIngredient 호출하여 IngredientDto 객체 반환
                    IngredientResponseDTO.IngredientDto ingredientDto = searchIngredient(ingredientRequestDTO.getName(), savedRecipe.getId());

                    // Ingredient 객체 검색 (중복 방지)
                    Ingredient ingredient = ingredientRepository.findByName(ingredientDto.getName())
                            .orElseThrow(() -> new GeneralException(ErrorStatus.INGREDIENT_NOT_FOUND, "Ingredient not found after search."));

                    // Recipe_Ingredient 객체 생성
                    Recipe_Ingredient recipeIngredient = Recipe_Ingredient.builder()
                            .recipe(savedRecipe)
                            .ingredient(ingredient)
                            .quantity(ingredientRequestDTO.getQuantity()) // 무게(양) 설정
                            .build();

                    // 중복 체크: 동일한 Recipe와 Ingredient의 조합이 이미 존재하는지 확인
                    boolean alreadyExists = recipeIngredientRepository.existsByRecipeAndIngredient(savedRecipe, ingredient);
                    if (!alreadyExists) {
                        // Recipe_Ingredient 저장
                        recipeIngredientRepository.save(recipeIngredient);
                    }

                    return recipeIngredient;
                })
                .collect(Collectors.toList());

        int totalCalories = calculateTotalCalories(recipeIngredientList);

        // 레시피 칼로리 업데이트
        savedRecipe.updateCalories(totalCalories);
        recipeRepository.save(savedRecipe);

        // 응답 DTO 작성 및 반환
        List<String> ingredientNames = recipeIngredientList.stream()
                .map(recipeIngredient -> recipeIngredient.getIngredient().getName())
                .collect(Collectors.toList());

        return RecipeResponseDTO.DetailRecipeDTO.builder()
                .recipeId(savedRecipe.getId())
                .memberId(savedRecipe.getMember().getId())
                .title(savedRecipe.getTitle())
                .description(savedRecipe.getDescription())
                .calories(totalCalories)
                .imageUrl(savedRecipe.getRecipeImage() != null ? savedRecipe.getRecipeImage().getUrl() : null)
                .ingredientNames(ingredientNames)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 총칼로리 계산
    private int calculateTotalCalories(List<Recipe_Ingredient> recipeIngredientList) {
        return recipeIngredientList.stream()
                .mapToInt(recipeIngredient -> {

                    // 100g 칼로리 -> 무게 비율로 변경
                    Integer caloriePer100g = recipeIngredient.getIngredient().getCalorie();
                    Integer quantity = recipeIngredient.getQuantity();

                    if (caloriePer100g == null || quantity == null) {
                        return 0;
                    }

                    log.info("kcal 100g: {}", caloriePer100g);
                    log.info("quantity: {}", quantity);
                    log.info("result: {}", (caloriePer100g * (quantity / 100.0)));

                    return (int) (caloriePer100g * (quantity / 100.0));
                })
                .sum();
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

    @Override
    public IngredientResponseDTO.IngredientDto searchIngredient(String name, Long recipeId) {
        String url = "https://apis.data.go.kr/1471000/FoodNtrCpntDbInfo/getFoodNtrCpntDbInq";

        try {
            // URL 인코딩
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
            String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8.toString());

            // 쿼리 파라미터 생성
            String query = String.format("serviceKey=%s&pageNo=1&numOfRows=5&type=json&FOOD_NM_KR=%s",
                    encodedServiceKey, encodedName);

            // URI 객체 생성
            URI uri = new URI(url + "?" + query);

            // 로그에 인코딩된 URI 출력
            log.info("Encoded URI: {}", uri);

            // HTTP GET 요청
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            // 응답을 JSON 형태로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // JSON 응답에서 필요한 데이터 추출
            JsonNode itemsNode = rootNode.path("body").path("items");

            if (itemsNode.isArray() && itemsNode.size() > 0) {
                JsonNode item = itemsNode.get(0);  // 첫 번째 아이템

                // AMT_NUM1 (칼로리) 데이터 추출
                String calorieStr = item.path("AMT_NUM1").asText();
                Integer calorie = null;

                if (calorieStr != null && !calorieStr.isEmpty()) {
                    try {
                        calorie = Integer.parseInt(calorieStr);
                    } catch (NumberFormatException e) {
                        calorie = 0; // 기본값 설정 또는 적절한 예외 처리
                    }
                }

                Optional<Ingredient> existingIngredientOpt = ingredientRepository.findByName(name);
                Ingredient ingredient;
                if (existingIngredientOpt.isPresent()) {
                    // 이미 존재하는 Ingredient 사용
                    ingredient = existingIngredientOpt.get();
                } else {
                    // 새로운 Ingredient 객체 생성 및 저장
                    ingredient = Ingredient.builder()
                            .name(name)
                            .calorie(calorie)
                            .build();
                    ingredient = ingredientRepository.save(ingredient);
                }

                // API에서 불러온 칼로리를 포함한 IngredientDto 반환
                return IngredientResponseDTO.IngredientDto.builder()
                        .name(ingredient.getName())
                        .calorie(ingredient.getCalorie())
                        .build();
            }
            throw new GeneralException(ErrorStatus.INGREDIENT_NOT_FOUND);

        } catch (IOException e) {
            throw new GeneralException(ErrorStatus.API_CALL_ERROR);
        } catch (URISyntaxException e) {
            throw new GeneralException(ErrorStatus.API_CALL_ERROR);
        }
    }
}