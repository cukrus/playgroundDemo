package net.cukrus.demo.playground.service;

import net.cukrus.demo.playground.domain.VisitorCount;

import java.time.Instant;
import java.util.List;

public interface VisitorService {
    void recordVisit(Long playSite, Long ticket, Instant visitAt);
    List<VisitorCount> getVisitorCounts(Instant startOfDay, Instant endOfDay);
}
