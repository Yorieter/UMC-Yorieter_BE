package umc.yorieter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.domain.mapping.RecipeLike;

import java.util.List;
import java.util.Optional;

public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long> {

    // 레시피 좋아요
    Optional<RecipeLike> findByMemberAndRecipe(Member member, Recipe recipe);

    // 레시피 삭제 시, RecipeLike 데이터 먼저 찾아서 삭제하기 위한 메서드
    List<RecipeLike> findAllByRecipeId(Long recipeId);

    Page<RecipeLike> findAllByMember(Member member, PageRequest pageRequest);
}
