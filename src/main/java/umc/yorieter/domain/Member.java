package umc.yorieter.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.yorieter.domain.common.BaseEntity;
import umc.yorieter.domain.enums.Term;
import umc.yorieter.domain.mapping.RecipeLike;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String nickname;

    @Enumerated(EnumType.ORDINAL)
    private Term term1;

    @Enumerated(EnumType.ORDINAL)
    private Term term2;

    @Enumerated(EnumType.ORDINAL)
    private Term term3;

    @Column(nullable = false, length = 15)
    private String username;

    @Column(nullable = false, length = 20)
    private String password;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    private MemberProfile profile;

    @Column(length = 20)
    private String description;

    @Column(length = 20)
    private String provider;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Recipe> recipeList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<RecipeLike> recipeLikeList = new ArrayList<>();
}