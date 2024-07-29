package umc.yorieter.web.dto.request;

import lombok.*;

import java.util.List;


public class RecipeRequestDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateRecipeDTO {

        private String title;
        private String description;
        private Integer calories;
        private List<String> ingredientNames; // 식재료 리스트 추가

    }


    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateRecipeDTO{

        private String title;
        private String description;
        private Integer calories;
        private List<String> ingredientNames; // 식재료 리스트 추가

    }
}
