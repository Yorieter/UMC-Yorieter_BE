package umc.yorieter.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import umc.yorieter.domain.Comment;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.Recipe;
import umc.yorieter.domain.mapping.RecipeLike;
import umc.yorieter.web.dto.response.MemberResponseDTO;
import umc.yorieter.web.dto.response.RecipeResponseDTO;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class MemberConverter {

    public MemberResponseDTO.MemberDetailDto toDetailDto(Member member, String profileUrl) {
        return MemberResponseDTO.MemberDetailDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .description(member.getDescription())
                .profileUrl(profileUrl)
                .build();
    }



    public static MemberResponseDTO.RecipePreViewListDTO recipePreViewListDTO(Page<Recipe> memberRecipeList) {
        List<MemberResponseDTO.RecipePreViewDTO> recipePreViewDTOList = memberRecipeList.stream()
                .map(MemberConverter::recipePreViewDTO).collect(Collectors.toList());

        return MemberResponseDTO.RecipePreViewListDTO.builder()
                .isLast(memberRecipeList.isLast())
                .isFirst(memberRecipeList.isFirst())
                .totalPage(memberRecipeList.getTotalPages())
                .totalElements(memberRecipeList.getTotalElements())
                .listSize(recipePreViewDTOList.size())
                .recipeList(recipePreViewDTOList)
                .build();
    }

    public static MemberResponseDTO.RecipePreViewDTO recipePreViewDTO(Recipe recipe) {
        return MemberResponseDTO.RecipePreViewDTO.builder()
                .title(recipe.getTitle())
                .recipeId(recipe.getId())
                .imageUrl(recipe.getRecipeImage()!=null ? recipe.getRecipeImage().getUrl():null)
                .createdAt(recipe.getCreatedAt().toLocalDate())
                .build();
    }

    public static MemberResponseDTO.CommentPreViewListDTO commentPreViewListDTO(Page<Comment> commentList){
        List<RecipeResponseDTO.CommentPreViewDTO> commentPreViewDTOList = commentList.stream()
                .map(RecipeConverter::commentPreViewDTO).collect(Collectors.toList());

        return MemberResponseDTO.CommentPreViewListDTO.builder()
                .isLast(commentList.isLast())
                .isFirst(commentList.isFirst())
                .totalPage(commentList.getTotalPages())
                .totalElements(commentList.getTotalElements())
                .listSize(commentPreViewDTOList.size())
                .commentList(commentPreViewDTOList)
                .build();
    }

    public static MemberResponseDTO.CommentPreViewDTO commentPreViewDTO(Comment comment){
        return MemberResponseDTO.CommentPreViewDTO.builder()
                .username(comment.getMember().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt().toLocalDate())
                .build();
    }


    public static MemberResponseDTO.RecipeLikePreViewListDTO recipeLikePreViewListDTO(Page<RecipeLike> recipeLikeList){
        List<MemberResponseDTO.RecipeLikePreViewDTO> recipeLikePreViewDTOList = recipeLikeList.stream()
                .map(MemberConverter::recipeLikePreViewDTO).collect(Collectors.toList());

        return MemberResponseDTO.RecipeLikePreViewListDTO.builder()
                .isLast(recipeLikeList.isLast())
                .isFirst(recipeLikeList.isFirst())
                .totalPage(recipeLikeList.getTotalPages())
                .totalElements(recipeLikeList.getTotalElements())
                .listSize(recipeLikePreViewDTOList.size())
                .recipeLikeList(recipeLikePreViewDTOList)
                .build();
    }


    public static MemberResponseDTO.RecipeLikePreViewDTO recipeLikePreViewDTO(RecipeLike recipeLike){
        return MemberResponseDTO.RecipeLikePreViewDTO.builder()
                .title(recipeLike.getRecipe().getTitle())
                .recipeId(recipeLike.getRecipe().getId())
                .createdAt(recipeLike.getRecipe().getCreatedAt().toLocalDate())
                .build();
    }


}
