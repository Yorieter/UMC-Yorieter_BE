package umc.yorieter.service.CommentService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.yorieter.converter.CommentConverter;
import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.repository.CommentRepository;
import umc.yorieter.web.dto.request.CommentRequestDTO;
import umc.yorieter.web.dto.response.CommentResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO, Recipe recipe, Member member) {
        Comment comment = Comment.builder()
                .content(commentRequestDTO.getContent())
                .recipe(recipe)
                .member(member)
                .build();

        // 저장
        Comment savedComment = commentRepository.save(comment);
        return CommentConverter.toResponseDTO(savedComment);
    }

//    @Transactional(readOnly = true)
//    public List<CommentResponseDTO> getCommentsByRecipe(Long recipeId) {
//        List<Comment> comments = commentRepository.findByRecipeId(recipeId);
//        return comments.stream()
//                .map(CommentConverter::toResponseDTO)
//                .collect(Collectors.toList());
//    }
}
