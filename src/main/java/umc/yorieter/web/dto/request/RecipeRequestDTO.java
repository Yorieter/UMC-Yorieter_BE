package umc.yorieter.web.dto.request;

import lombok.*;


public class RecipeRequestDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateRecipeDTO {

        private String title;
        private String description;
        private Integer calories;
        // private List recipeIngredientList; <- 식재료 넣는건데, 킵

    }


    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateRecipeDTO{

        private String title;
        private String description;
        private Integer calories;
        // private List recipeIngredientList; <- 식재료 넣는건데, 잘 모르겠다

    }
}
