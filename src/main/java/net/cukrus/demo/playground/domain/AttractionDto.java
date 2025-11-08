package net.cukrus.demo.playground.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record AttractionDto(@JsonIgnore Long id, String name, Integer capacity) {

    public static AttractionDto fromEntity(Attraction entity) {
        return new AttractionDto(entity.getId(), entity.getName(), entity.getCapacity());
    }

    public static Attraction toEntity(AttractionDto dto, PlaySite playSite) {
        return new Attraction(dto.id(), playSite, dto.name(), dto.capacity());
    }

    public Attraction toEntity(PlaySite playSite) {
        return toEntity(this, playSite);
    }
}
