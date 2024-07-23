package umc.yorieter.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.yorieter.service.RecipeService.RecipeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;




}
