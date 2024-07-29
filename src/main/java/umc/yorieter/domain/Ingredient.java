package umc.yorieter.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.yorieter.domain.mapping.Recipe_Ingredient;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column
    private Integer calorie;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private List<Recipe_Ingredient> recipeIngredientListList = new ArrayList<>();


    public Ingredient(Long id, String ingredientName, Integer calorie) {
        this.id = id;
        this.name = ingredientName;
        this.calorie = calorie;

    }
}
