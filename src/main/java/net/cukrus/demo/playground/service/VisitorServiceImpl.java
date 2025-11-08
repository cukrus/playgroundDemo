package net.cukrus.demo.playground.service;

import net.cukrus.demo.playground.domain.Visitor;
import net.cukrus.demo.playground.domain.VisitorCount;
import net.cukrus.demo.playground.repo.VisitorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class VisitorServiceImpl implements VisitorService {
    private final VisitorRepo visitorRepo;

    @Autowired
    public VisitorServiceImpl(VisitorRepo visitorRepo) {
        this.visitorRepo = visitorRepo;
    }

    @Override
    public void recordVisit(Long playSite, Long ticket, Instant visitAt) {
        visitorRepo.save(new Visitor(null, playSite, ticket, visitAt));
    }

    @Override
    public List<VisitorCount> getVisitorCounts(Instant startOfDay, Instant endOfDay) {
        return visitorRepo.countVisitorsByPlaySiteBetween(startOfDay, endOfDay);
    }
}
