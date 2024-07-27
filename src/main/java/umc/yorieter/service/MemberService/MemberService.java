package umc.yorieter.service.MemberService;

import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.web.dto.request.MemberRequestDto.MemberUpdateDto;
import umc.yorieter.web.dto.response.MemberResponseDTO;

public interface MemberService {

    MemberResponseDTO.MemberDetailDto getMemberDetail(Long memberId);

    MemberResponseDTO.MemberDetailDto updateMember(Long memberId, MultipartFile image, MemberUpdateDto memberUpdateDto);
}
