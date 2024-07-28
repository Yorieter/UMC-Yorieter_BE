package umc.yorieter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByRecipe(Recipe recipe, PageRequest pageRequest);
    Page<Comment> findAllByMember(Member member, PageRequest pageRequest);
}
