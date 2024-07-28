package umc.yorieter.service.MemberService;
import org.springframework.data.domain.Page;
import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.domain.mapping.RecipeLike;

import java.util.Optional;

public interface MemberQueryService {

    Optional<Member> findMember(Long id);

    Page<Comment> getCommentListByMemberId(Long memberId, Integer page);

    Page<Recipe> getRecipeListByMemberId(Long memberId, Integer page);

    Page<RecipeLike> getRecipeLikeListByMemberId(Long memberId, Integer page);
}