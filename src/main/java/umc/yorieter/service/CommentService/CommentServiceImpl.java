package umc.yorieter.service.CommentService;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final RecipeRepository  recipeRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO requestDTO) {
        // 데이터베이스에서 Recipe와 Member를 조회
        Recipe recipe = recipeRepository.findById(requestDTO.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // Comment 객체를 생성
        Comment comment = Comment.builder()
                .content(requestDTO.getContent())
                .recipe(recipe)
                .member(member)
                .build();

        // 댓글을 저장
        Comment savedComment = commentRepository.save(comment);

        // Comment 객체를 CommentResponseDTO로 변환하여 반환
        return CommentResponseDTO.fromComment(savedComment);
    }
//
//    @Override
//    @Transactional
//    public RecipeCommentsResponseDTO getCommentsByRecipeId(Long recipeId) {
//        // 레시피를 조회
//        Recipe recipe = recipeRepository.findById(recipeId)
//                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + recipeId));
//
//        // 레시피에 대한 모든 댓글을 조회
//        List<Comment> comments = commentRepository.findByRecipeId(recipeId);
//
//        // Recipe와 댓글을 DTO로 변환
//        List<CommentResponseDTO> commentResponseDTOs = comments.stream()
//                .map(commentConverter::toCommentResponseDTO)
//                .collect(Collectors.toList());
//
//        return RecipeCommentsResponseDTO.builder()
//                .recipeId(recipe.getId())
//                .comments(commentResponseDTOs)
//                .createdAt(recipe.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .updatedAt(recipe.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .build();
//    }
}