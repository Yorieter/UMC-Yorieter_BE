package umc.yorieter.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class IngredientRequestDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IngredientAndQuantityDTO {
        private String name;
        private Integer quantity;
    }
}