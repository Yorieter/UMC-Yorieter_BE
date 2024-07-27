package umc.yorieter.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.converter.MemberConverter;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.MemberProfile;
import umc.yorieter.payload.exception.GeneralException;
import umc.yorieter.payload.status.ErrorStatus;
import umc.yorieter.repository.MemberRepository;
import umc.yorieter.service.ImageUploadService.ImageUploadService;
import umc.yorieter.web.dto.request.MemberRequestDto;
import umc.yorieter.web.dto.response.MemberResponseDTO;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ImageUploadService imageUploadService;
    private final MemberConverter memberConverter;

    //회원정보 조회
    @Override
    public MemberResponseDTO.MemberDetailDto getMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_EXIST_ERROR));

        MemberProfile memberProfile = member.getProfile();
        String profileUrl = (memberProfile != null) ? memberProfile.getUrl() : null;

        return memberConverter.toDetailDto(member, profileUrl);
    }

    //회원정보 수정
    @Override
    public MemberResponseDTO.MemberDetailDto updateMember(Long memberId, MultipartFile image, MemberRequestDto.MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_EXIST_ERROR));

        if (memberRepository.findByNickname(memberUpdateDto.getNickname())
                .filter(existingMember -> !existingMember.getId().equals(memberId))
                .isPresent()) {
            throw new GeneralException(ErrorStatus.USERNAME_ALREADY_EXISTS, "중복된 닉네임입니다.");
        }

        // 이미지 업로드 처리
        String profileUrl = null;
        if (image != null && !image.isEmpty()) {
            profileUrl = imageUploadService.uploadImage(image);
        }
        member.update(memberUpdateDto);
        if (profileUrl != null) {
            member.updateProfileUrl(profileUrl);
        }

        memberRepository.save(member);
        return memberConverter.toDetailDto(member, profileUrl);
    }
}