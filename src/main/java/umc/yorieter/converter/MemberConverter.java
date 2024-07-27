package umc.yorieter.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.yorieter.domain.Member;
import umc.yorieter.web.dto.response.MemberResponseDTO;


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

}
