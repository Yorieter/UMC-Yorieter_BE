package umc.yorieter.service.RecipeService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.domain.mapping.RecipeLike;
import umc.yorieter.payload.exception.GeneralException;
import umc.yorieter.payload.status.ErrorStatus;
import umc.yorieter.repository.MemberRepository;
import umc.yorieter.repository.RecipeLikeRepository;
import umc.yorieter.repository.RecipeRepository;
import umc.yorieter.web.dto.request.RecipeRequestDTO;
import umc.yorieter.web.dto.response.RecipeResponseDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;
    private final RecipeLikeRepository recipeLikeRepository;

    // 레시피 작성
    @Override
    @Transactional
    public Long createRecipe(Long memberId, RecipeRequestDTO.CreateRecipeDTO createRecipeDTO) {
        // 회원 있나 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_EXIST_ERROR));

        // DTO에서 데이터 가져와 Recipe 객제 생성
        Recipe recipe = Recipe.builder()
                .member(member)
                .title(createRecipeDTO.getTitle())
                .description(createRecipeDTO.getDescription())
                .calories(createRecipeDTO.getCalories())
                .build();

        // 레시피 저장
        return recipeRepository.save(recipe).getId(); // 생성한 ID값 반환

    }

    
    // 레시피 (상세)조회
    @Override
    public RecipeResponseDTO.DetailRecipeDTO getRecipe(Long recipeId) {
        System.out.println("Fetching recipe with ID: " + recipeId);
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_EXIST_ERROR));


        return RecipeResponseDTO.DetailRecipeDTO.builder()
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .calories(recipe.getCalories())
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
