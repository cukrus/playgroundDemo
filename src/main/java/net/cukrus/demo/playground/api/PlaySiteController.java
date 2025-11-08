package net.cukrus.demo.playground.api;

import net.cukrus.demo.playground.domain.KidDto;
import net.cukrus.demo.playground.domain.PlaySite;
import net.cukrus.demo.playground.domain.PlaySiteDto;
import net.cukrus.demo.playground.domain.VisitorCount;
import net.cukrus.demo.playground.service.PlaySiteService;
import net.cukrus.demo.playground.service.VisitorService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/playSite")
public class PlaySiteController {
    private static final int HALF_BIL = 500000000;

    private final PlaySiteService playSiteService;
    private final VisitorService visitorService;

    @Autowired
    public PlaySiteController(PlaySiteService playSiteService, VisitorService visitorService) {
        this.playSiteService = playSiteService;
        this.visitorService = visitorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaySiteDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(playSiteService.getById(id).orElseThrow(() -> new ObjectNotFoundException(id, PlaySite.class.getSimpleName())));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public List<PlaySiteDto> getAll() {
        return playSiteService.getAll();
    }

    @PostMapping
    public PlaySiteDto create(@RequestBody PlaySiteDto body) {
        return playSiteService.create(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaySiteDto> updateById(@PathVariable Long id, @RequestBody PlaySiteDto body) {
        try {
            return ResponseEntity.ok(playSiteService.updateById(id, body));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        playSiteService.deleteById(id);
    }

    @PostMapping("/{playSiteId}/kid")
    public ResponseEntity<Void> addKid(@PathVariable Long playSiteId, @RequestBody KidDto body, @RequestParam(required = false) Boolean queueable) {
        Instant now = Instant.now();
        if (queueable == null) {
            queueable = now.getNano() < HALF_BIL;
        }
        try {
            playSiteService.addKid(playSiteId, body, queueable, now);
            return ResponseEntity.ok().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @DeleteMapping("/{playSiteId}/kid/{ticket}")
    public ResponseEntity<Void> removeKid(@PathVariable Long playSiteId, @PathVariable Long ticket) {
        try {
            playSiteService.removeKid(playSiteId, ticket);
            return ResponseEntity.ok().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/visitors")
    public List<VisitorCount> getAllVisitorCounts() {
        Instant startOfDay = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = startOfDay.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.NANOS);
        return visitorService.getVisitorCounts(startOfDay, endOfDay);
    }
}
