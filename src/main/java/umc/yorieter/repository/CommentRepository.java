package umc.yorieter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.yorieter.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
