package umc.yorieter.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class RecipeResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailRecipeDTO {

        private Long memberId;
        private String title;
        private String description;
        private Integer calories;
        // private String imageUrl; <- RecipeImage도 사용 (이거 S3필요하니까 킵)
        // private List recipeIngredientList; <- 식재료 넣는건데, 킵
        // private List recipeLikeList; <- 이것도 킵
        // private List commentList; <- 이것도 킵
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentPreViewListDTO{
        List<CommentPreViewDTO> commentList;
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
}
