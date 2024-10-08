package umc.yorieter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDate;


public class RecipeResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailRecipeDTO {

        private Long memberId;
        private Long recipeId;
        private String title;
        private String description;
        private Integer calories;
        private String imageUrl;
        private List<String> ingredientNames; // 식재료 리스트 추가

        @JsonInclude(JsonInclude.Include.ALWAYS) // 항상 포함되도록 설정
        private boolean isLiked; // 좋아요 여부 추가

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime updatedAt;
    }


    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AllRecipeListDto {
        List<DetailRecipeDTO> recipeList;
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
        Long commentId;
        String imageUrl;
        LocalDate createdAt;
    }
}
