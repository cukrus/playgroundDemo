package net.cukrus.demo.playground.repo;

import net.cukrus.demo.playground.domain.Kid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KidRepo extends JpaRepository<Kid, Long> {
}
