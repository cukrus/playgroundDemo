package net.cukrus.demo.playground.service;

import net.cukrus.demo.playground.domain.KidDto;
import net.cukrus.demo.playground.domain.PlaySiteDto;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PlaySiteService {
    Optional<PlaySiteDto> getById(Long id);
    List<PlaySiteDto> getAll();
    PlaySiteDto create(PlaySiteDto dto);
    PlaySiteDto updateById(Long id, PlaySiteDto dto);
    void deleteById(Long id);

    void addKid(Long playSiteId, KidDto dto, boolean queueable, Instant creatingAt);
    void removeKid(Long playSiteId, Long ticket);
}
