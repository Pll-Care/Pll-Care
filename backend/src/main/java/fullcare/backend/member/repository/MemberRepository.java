package fullcare.backend.member.repository;

import fullcare.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    @Query("select m from Member m where m.id = :id")
    Optional<Member> findById(@Param("id") Long id);

    Optional<Member> findByoAuth2Id(String oAuth2Id);
}
