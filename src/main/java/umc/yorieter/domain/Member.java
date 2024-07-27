package umc.yorieter.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.yorieter.domain.common.BaseEntity;
import umc.yorieter.domain.enums.Authority;
import umc.yorieter.domain.enums.Provider;
import umc.yorieter.domain.enums.Term;
import umc.yorieter.domain.mapping.RecipeLike;
import umc.yorieter.web.dto.request.MemberRequestDto;

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

    @Enumerated(EnumType.ORDINAL)
    private Term term1;

    @Enumerated(EnumType.ORDINAL)
    private Term term2;

    @Enumerated(EnumType.ORDINAL)
    private Term term3;

    @Column(nullable = false, length = 15)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 15)
    private String nickname;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    private MemberProfile profile;

    @Column(length = 20)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private Authority authority;  // 초기값: ROLE_USER

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Recipe> recipeList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<RecipeLike> recipeLikeList = new ArrayList<>();

    @Builder(builderClassName = "MemberJoinBuilder", builderMethodName = "MemberJoinBuilder")
    public Member(String nickname, Term term1, Term term2, Term term3, String username, String password, String description, Authority authority) {
        // 이 빌더는 사용자 회원가입때만 사용할 용도
        this.username = username;
        this.nickname = nickname;
        this.term1 = term1;
        this.term2 = term2;
        this.term3 = term3;
        this.password = password;
        this.description = description;
        this.authority = authority;
    }

    // 회원 정보 수정
    public Member update(MemberRequestDto.MemberUpdateDto memberUpdateDto) {
        if(memberUpdateDto.getNickname() != null ) this.nickname = memberUpdateDto.getNickname();
        if(memberUpdateDto.getDescription() != null) this.description = memberUpdateDto.getDescription();

        return this;
    }

    // 프로필사진 업로드
    public void updateProfileUrl(String profileUrl) {
        if (this.profile == null) {
            this.profile = MemberProfile.builder()
                    .member(this)
                    .url(profileUrl)
                    .build();
        } else {
            this.profile.updateProfileUrl(profileUrl);
        }
    }
}