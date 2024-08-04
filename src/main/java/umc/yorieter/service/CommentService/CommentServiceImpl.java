package umc.yorieter.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.yorieter.config.security.util.SecurityUtil;
import umc.yorieter.converter.CommentConverter;
import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.payload.exception.GeneralException;
import umc.yorieter.payload.status.ErrorStatus;
import umc.yorieter.repository.CommentRepository;
import umc.yorieter.repository.MemberRepository;
import umc.yorieter.repository.RecipeRepository;
import umc.yorieter.web.dto.request.CommentRequestDTO;
import umc.yorieter.web.dto.response.CommentResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final RecipeRepository  recipeRepository;
    private final MemberRepository memberRepository;
    private final CommentConverter commentConverter;

    @Override
    @Transactional
    public CommentResponseDTO createComment(Long recipeId, CommentRequestDTO commentRequestDTO) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_EXIST_ERROR));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));

        // 부모 댓글이 없는 경우 처리
        Comment comment = commentConverter.toCommentEntity(commentRequestDTO, recipe, member, null);
        Comment savedComment = commentRepository.save(comment);

        return commentConverter.toCommentResponseDTO(savedComment);
    }

    @Override
    public List<CommentResponseDTO> getCommentsByRecipeId(Long recipeId) {
        // 레시피 확인
        recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));

        //댓글 조회
        List<Comment> comments = commentRepository.findByRecipeId(recipeId);

        return comments.stream()
                .map(CommentResponseDTO::fromComment)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));

        //본인만 댓글 삭제할 수 있도록
        if (!comment.getMember().getId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.NO_EDIT_DELETE_PERMISSION, "본인만 댓글을 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }

    //대댓글 구현
    @Override
    @Transactional
    public CommentResponseDTO createReply(Long parentCommentId, Long recipeId, CommentRequestDTO commentRequestDTO) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid parent comment ID: " + parentCommentId));

        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_EXIST_ERROR));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe ID: " + recipeId));

        Comment reply = commentConverter.toCommentEntity(commentRequestDTO, recipe, member, parentComment);
        Comment savedComment = commentRepository.save(reply);

        return commentConverter.toCommentResponseDTO(savedComment);
    }

    @Override
    public Long getCommentOwnerId(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
        return comment.getMember().getId();
    }
}