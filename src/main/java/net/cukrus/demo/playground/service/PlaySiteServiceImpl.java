package net.cukrus.demo.playground.service;

import net.cukrus.demo.playground.domain.Kid;
import net.cukrus.demo.playground.domain.KidDto;
import net.cukrus.demo.playground.domain.PlaySite;
import net.cukrus.demo.playground.domain.PlaySiteDto;
import net.cukrus.demo.playground.repo.PlaySiteRepo;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PlaySiteServiceImpl implements PlaySiteService {
    private final PlaySiteRepo playSiteRepo;
    private final VisitorService visitorService;

    @Autowired
    public PlaySiteServiceImpl(PlaySiteRepo playSiteRepo, VisitorService visitorService) {
        this.playSiteRepo = playSiteRepo;
        this.visitorService = visitorService;
    }

    @Override
    public Optional<PlaySiteDto> getById(Long id) {
        return playSiteRepo.findById(id).map(PlaySiteDto::fromEntity);
    }

    @Override
    public List<PlaySiteDto> getAll() {
        return playSiteRepo.findAll().stream().map(PlaySiteDto::fromEntity).toList();
    }

    @Override
    public PlaySiteDto create(PlaySiteDto dto) {
        return PlaySiteDto.fromEntity(playSiteRepo.save(dto.toEntity()));
    }

    @Override
    @Transactional
    public PlaySiteDto updateById(Long id, PlaySiteDto dto) {
        PlaySite playSite = playSiteRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, PlaySite.class.getSimpleName()));
        PlaySite fromDto = PlaySiteDto.toEntity(dto, playSite);
        playSite.setCapacity(fromDto.getCapacity());
        playSite.getAttractions().clear();
        playSite.getAttractions().addAll(fromDto.getAttractions());
        return PlaySiteDto.fromEntity(playSiteRepo.save(playSite));
    }

    @Override
    public void deleteById(Long id) {
        playSiteRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void addKid(Long playSiteId, KidDto dto, boolean queueable, Instant creatingAt) {
        PlaySite playSite = playSiteRepo.findById(playSiteId).orElseThrow(() -> new ObjectNotFoundException(playSiteId, PlaySite.class.getSimpleName()));
        if (!queueable && playSite.getCapacity() <= playSite.getKids().size()) {
            throw new IllegalStateException("PlaySite FULL");
        }
        Kid kid = dto.toEntity(playSite, creatingAt);
        playSite.getKids().add(kid);
        visitorService.recordVisit(playSiteId, dto.ticket(), creatingAt);
    }

    @Override
    @Transactional
    public void removeKid(Long playSiteId, Long ticket) {
        PlaySite playSite = playSiteRepo.findById(playSiteId).orElseThrow(() -> new ObjectNotFoundException(playSiteId, PlaySite.class.getSimpleName()));
        var kidsIterator = playSite.getKids().iterator();
        boolean removed = false;
        while (kidsIterator.hasNext()) {
            if (kidsIterator.next().getTicket().equals(ticket)) {
                kidsIterator.remove();
                removed = true;
                break;
            }
        }
        if (!removed) {
            throw new ObjectNotFoundException(ticket, Kid.class.getSimpleName());
        }
    }
}
