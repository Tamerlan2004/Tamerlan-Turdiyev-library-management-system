package core.repository;

import core.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByFullNameContainingIgnoreCase(String fullName);

    List<Member> findByEmailContainingIgnoreCase(String email);

    List<Member> findByProfileFaculty(String faculty);

    List<Member> findByProfileGroupName(String groupName);
}