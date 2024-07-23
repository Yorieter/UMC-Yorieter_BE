package umc.yorieter.web.dto.request;

import lombok.*;

import java.util.List;

public class RecipeRequestDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateRecipeDTO {

        private Long memberId;
        private String title;
        private String description;
        private Integer calories;
        // private String imageUrl; <- RecipeImage도 사용 (이거 일단 킵)
        // private List recipeIngredientList; <- 식재료 넣는건데, 잘 모르겠다


    }


    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateRecipeDTO{

        private String title;
        private String content;

    }
}
