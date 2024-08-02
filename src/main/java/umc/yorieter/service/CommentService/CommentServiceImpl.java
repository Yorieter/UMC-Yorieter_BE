package umc.yorieter.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.yorieter.converter.CommentConverter;
import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.domain.RecipeImage;
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
    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO) {
        Recipe recipe = recipeRepository.findById(commentRequestDTO.getRecipeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe ID: " + commentRequestDTO.getRecipeId()));
        Member member = memberRepository.findById(commentRequestDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + commentRequestDTO.getMemberId()));

        // 부모 댓글이 없는 경우 처리
        Comment comment = commentConverter.toCommentEntity(commentRequestDTO, recipe, member, null);
        Comment savedComment = commentRepository.save(comment);

        // 저장된 댓글을 DTO로 변환하여 반환합니다.
        return commentConverter.toCommentResponseDTO(savedComment);
    }

    @Override
    public List<CommentResponseDTO> getCommentsByRecipeId(Long recipeId) {
        // 레시피 확인
        recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));

        // 댓글을 조회
        List<Comment> comments = commentRepository.findByRecipeId(recipeId);

        // 댓글을 DTO로 변환
        return comments.stream()
                .map(CommentResponseDTO::fromComment)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));

        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public CommentResponseDTO createReply(Long parentCommentId, CommentRequestDTO commentRequestDTO) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid parent comment ID: " + parentCommentId));

        Member member = memberRepository.findById(commentRequestDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + commentRequestDTO.getMemberId()));

        Recipe recipe = recipeRepository.findById(commentRequestDTO.getRecipeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe ID: " + commentRequestDTO.getRecipeId()));

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