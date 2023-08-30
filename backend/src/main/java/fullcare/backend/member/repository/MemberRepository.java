package fullcare.backend.member.repository;

import fullcare.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByoAuth2Id(String oAuth2Id);

}
