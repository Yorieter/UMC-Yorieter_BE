package umc.yorieter.service.RecipeService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import umc.yorieter.repository.RecipeRepository;

@Service
@RequiredArgsConstructor
@Component
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;

    // 레시피 작성



}
