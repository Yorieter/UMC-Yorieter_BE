package umc.yorieter.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import umc.yorieter.domain.common.BaseEntity;
import umc.yorieter.domain.mapping.RecipeLike;
import umc.yorieter.domain.mapping.Recipe_Ingredient;
import umc.yorieter.web.dto.request.RecipeRequestDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Recipe extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column
    private Integer calories;

    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private RecipeImage recipeImage;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeLike> recipeLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Recipe_Ingredient> recipeIngredientList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 레시피이미지 올리기
    public void updateRecipeImageUrl(String url) {
        if (this.recipeImage == null) {
            this.recipeImage = RecipeImage.builder()
                    .url(url)
                    .recipe(this)
                    .build();
        } else {
            this.recipeImage.updateRecipeImageUrl(url);
        }
    }

    // 레시피 수정
    public Recipe update(RecipeRequestDTO.UpdateRecipeDTO updateRecipeDTO) {
        if (updateRecipeDTO.getTitle() != null) this.title = updateRecipeDTO.getTitle();
        if (updateRecipeDTO.getDescription() != null) this.description = updateRecipeDTO.getDescription();
        if (updateRecipeDTO.getCalories() != null) this.calories = updateRecipeDTO.getCalories();
        if (updateRecipeDTO.getIngredientNames() != null) this.calories = updateRecipeDTO.getCalories();

        return this;
    }






}
