package umc.yorieter.web.dto;

import lombok.*;

public class RecipeRequestDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateRecipeDTO {

        private String title;
        private String content;

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
