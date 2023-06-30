package fullcare.backend.member.repository;

import fullcare.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
    Optional<Member> findByoAuth2Id(String oAuth2Id);

    @Query("select m from Member m where m.id in :memberIds")
    List<Member> findByIds(@Param("memberIds") List<Long> memberIds);


}
