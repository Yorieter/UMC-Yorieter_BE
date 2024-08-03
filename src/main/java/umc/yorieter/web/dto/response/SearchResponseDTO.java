package umc.yorieter.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.yorieter.domain.Recipe;

import java.time.LocalDateTime;
import java.util.List;

public class SearchResponseDTO {

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
        private List<String> ingredientNames;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AllRecipeListDto {
        List<DetailRecipeDTO> recipeList;
    }
}
