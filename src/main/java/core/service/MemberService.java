package core.service;

import core.dto.MemberDTO;
import core.entity.Member;
import core.entity.MemberProfile;
import core.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member create(MemberDTO dto) {
        Member member = Member.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();

        Member savedMember = memberRepository.save(member);

        MemberProfile profile = MemberProfile.builder()
                .member(savedMember)
                .faculty(dto.getFaculty())
                .groupName(dto.getGroupName())
                .course(dto.getCourse())
                .build();

        savedMember.setProfile(profile);

        log.info("Создан новый участник: {}", dto.getFullName());
        return memberRepository.save(savedMember);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public List<Member> findAllWithFilter(String fullName, String email, String faculty, String groupName) {
        if (fullName != null && !fullName.isBlank()) {
            return memberRepository.findByFullNameContainingIgnoreCase(fullName);
        }
        if (email != null && !email.isBlank()) {
            return memberRepository.findByEmailContainingIgnoreCase(email);
        }
        if (faculty != null && !faculty.isBlank()) {
            return memberRepository.findByProfileFaculty(faculty);
        }
        if (groupName != null && !groupName.isBlank()) {
            return memberRepository.findByProfileGroupName(groupName);
        }
        return findAll();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Участник не найден с id: " + id));
    }

    @Transactional
    public Member update(Long id, MemberDTO dto) {
        Member existing = findById(id);
        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());

        if (existing.getProfile() != null) {
            existing.getProfile().setFaculty(dto.getFaculty());
            existing.getProfile().setGroupName(dto.getGroupName());
            existing.getProfile().setCourse(dto.getCourse());
        }

        log.info("Обновлён участник: {}", dto.getFullName());
        return memberRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new RuntimeException("Участник не найден с id: " + id);
        }
        memberRepository.deleteById(id);
        log.info("Удалён участник с id: {}", id);
    }
}