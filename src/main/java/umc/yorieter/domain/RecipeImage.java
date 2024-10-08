package umc.yorieter.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecipeImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public void updateRecipeImageUrl(String url) {
        this.url = url;
    }
}
