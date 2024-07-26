package umc.yorieter.service.CommentService;

import umc.yorieter.web.dto.request.CommentRequestDTO;
import umc.yorieter.web.dto.response.CommentResponseDTO;

public interface CommentService {
    CommentResponseDTO createComment (CommentRequestDTO commentRequestDTO);
}
