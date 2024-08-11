package spring.db1.repository;

import org.junit.jupiter.api.Test;
import spring.db1.domain.Member;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest {

    MemberRepository memberRepository = new MemberRepository();

    @Test
    void crud() throws SQLException {
        Member member = new Member("member1", 10000);
        memberRepository.save(member);
    }
}