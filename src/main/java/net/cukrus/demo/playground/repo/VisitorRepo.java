package net.cukrus.demo.playground.repo;

import net.cukrus.demo.playground.domain.Visitor;
import net.cukrus.demo.playground.domain.VisitorCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface VisitorRepo extends JpaRepository<Visitor, Long> {
    @Query("""
        SELECT new net.cukrus.demo.playground.domain.VisitorCount(v.playSite, COUNT(v))
        FROM Visitor v
        WHERE v.createdAt BETWEEN :start AND :end
        GROUP BY v.playSite
        ORDER BY v.playSite
        """)
    List<VisitorCount> countVisitorsByPlaySiteBetween(@Param("start") Instant start, @Param("end") Instant end);
}
