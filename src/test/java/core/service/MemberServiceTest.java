package core.service;

import core.dto.MemberDTO;
import core.entity.Member;
import core.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void createMember_ShouldSaveMemberWithProfile() {
        // Given
        MemberDTO dto = MemberDTO.builder()
                .fullName("Иван Иванов")
                .email("ivan@example.com")
                .phone("+77771234567")
                .faculty("Информатика")
                .groupName("CS-23")
                .course(2)
                .build();

        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Member result = memberService.create(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo("Иван Иванов");
        assertThat(result.getEmail()).isEqualTo("ivan@example.com");
        assertThat(result.getProfile()).isNotNull();
        assertThat(result.getProfile().getFaculty()).isEqualTo("Информатика");
    }
}