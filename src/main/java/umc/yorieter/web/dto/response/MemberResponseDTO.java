package umc.yorieter.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.yorieter.domain.Member;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberResponseDTO {

    private Long id;
    private String username;


    public MemberResponseDTO(Member entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDetailDto {

        Long id;
        String nickname;
        String description;
        String profileUrl;
    }


    //멤버가 작성한 게시물
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipePreViewListDTO{
        List<MemberResponseDTO.RecipePreViewDTO> recipeList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipePreViewDTO{
        String title;
        Long recipeId;
        LocalDate createdAt;
    }

    //멤버가 단 댓글 리스트
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentPreViewListDTO{
        List<RecipeResponseDTO.CommentPreViewDTO> commentList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentPreViewDTO{
        String username;
        String content;
        LocalDate createdAt;
    }

    //멤버가 좋아요한 레시피 목록
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeLikePreViewListDTO{
        List<RecipeLikePreViewDTO> recipeLikeList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeLikePreViewDTO{
        String title;
        Long recipeId;
        LocalDate createdAt;
    }
}
