package umc.yorieter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findAllByMember(Member member, PageRequest pageRequest);
}
