package umc.yorieter.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.yorieter.config.security.util.SecurityUtil;
import umc.yorieter.converter.MemberConverter;
import umc.yorieter.domain.Member;
import umc.yorieter.domain.MemberProfile;
import umc.yorieter.payload.exception.GeneralException;
import umc.yorieter.payload.status.ErrorStatus;
import umc.yorieter.repository.MemberRepository;
import umc.yorieter.service.ImageUploadService.ImageUploadService;
import umc.yorieter.web.dto.request.MemberRequestDto;
import umc.yorieter.web.dto.response.MemberResponseDTO;

import java.util.Optional;


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
        Long loginMemberId = SecurityUtil.getCurrentMemberId();

        // 현재 로그인된 사용자와 수정하려는 사용자가 같은지 확인
        if (!loginMemberId.equals(memberId)) {
            throw new GeneralException(ErrorStatus.NO_EDIT_DELETE_PERMISSION);
        }

        // 멤버 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_EXIST_ERROR));

        // memberUpdateDto가 있는 경우
        if (memberUpdateDto != null) {
            String newNickname = memberUpdateDto.getNickname();
            if (newNickname != null && !newNickname.equals(member.getNickname())) {
                // 닉네임 중복 체크
                Optional<Member> existingMember = memberRepository.findByNickname(newNickname);
                if (existingMember.isPresent() && !existingMember.get().getId().equals(memberId)) {
                    throw new GeneralException(ErrorStatus.USERNAME_ALREADY_EXISTS, "중복된 닉네임입니다.");
                }
                member.update(memberUpdateDto);
            } else if (newNickname != null) {
                member.update(memberUpdateDto);
            }
        }

        // 이미지 업로드 처리
        String profileUrl = null;
        if (image != null && !image.isEmpty()) {
            profileUrl = imageUploadService.uploadImage(image);
        }

        if (profileUrl != null) {
            member.updateProfileUrl(profileUrl);
        }

        memberRepository.save(member);
        return memberConverter.toDetailDto(member, member.getProfile() != null ? member.getProfile().getUrl() : null);
    }
}