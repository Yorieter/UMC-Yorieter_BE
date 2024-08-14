package umc.yorieter.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;

public class SearchRequestDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchRecipeDTO {
        private Integer maxCalorie;
        private Integer minCalorie;
        private List<String> ingredientNames;
    }


    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TitleSearchDTO {
        private String title;
    }
}
