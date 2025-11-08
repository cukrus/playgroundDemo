package net.cukrus.demo.playground.repo;

import net.cukrus.demo.playground.domain.PlaySite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaySiteRepo extends JpaRepository<PlaySite, Long> {
}
