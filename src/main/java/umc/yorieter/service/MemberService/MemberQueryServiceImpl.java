package umc.yorieter.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.domain.mapping.RecipeLike;
import umc.yorieter.repository.CommentRepository;
import umc.yorieter.repository.MemberRepository;
import umc.yorieter.repository.RecipeRepository;
import umc.yorieter.repository.RecipeLikeRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService{

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeLikeRepository recipeLikeRepository;

    @Override
    public Optional<Member> findMember(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Page<Comment> getCommentListByMemberId(Long memberId, Integer page) {

        Member member = memberRepository.findById(memberId).get();

        Page<Comment> memberPage = commentRepository.findAllByMember(member,
                PageRequest.of(page, 10));
        return memberPage;
    }

    @Override
    public Page<Recipe> getRecipeListByMemberId(Long memberId, Integer page) {

        Member member = memberRepository.findById(memberId).get();

        Page<Recipe> recipePage = recipeRepository.findAllByMember(member,
                PageRequest.of(page, 10));
        return recipePage;
    }

    @Override
    public Page<RecipeLike> getRecipeLikeListByMemberId(Long memberId, Integer page){

        Member member = memberRepository.findById(memberId).get();

        Page<RecipeLike> recipeLikePage = recipeLikeRepository.findAllByMember(member,
                PageRequest.of(page, 10));
        return recipeLikePage;
    }
}
