package net.cukrus.demo.playground.domain;

import java.time.Instant;

public record KidDto(Long ticket, String name, Integer age) {

    public static KidDto fromEntity(Kid entity) {
        return new KidDto(entity.getTicket(), entity.getName(), entity.getAge());
    }

    public static Kid toEntity(KidDto dto, PlaySite playSite, Instant createdAt) {
        return new Kid(dto.ticket(), playSite, dto.name(), dto.age(), createdAt);
    }

    public Kid toEntity(PlaySite playSite, Instant createdAt) {
        return toEntity(this, playSite, createdAt);
    }
}
