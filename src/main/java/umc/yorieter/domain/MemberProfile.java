package umc.yorieter.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    //프로필 사진 수정
    public void updateProfileUrl(String url) {
        this.url = url;
    }
}
