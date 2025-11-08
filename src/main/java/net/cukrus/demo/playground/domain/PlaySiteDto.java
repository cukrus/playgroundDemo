package net.cukrus.demo.playground.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

public record PlaySiteDto(
        @JsonProperty(access = READ_ONLY) Long id,
        List<AttractionDto> attractions,
        @JsonProperty(access = READ_ONLY) List<KidDto> players,
        @JsonProperty(access = READ_ONLY) List<KidDto> queue,
        @JsonProperty(access = READ_ONLY) Integer capacity,
        @JsonProperty(access = READ_ONLY) double utilization) {

    public static PlaySiteDto fromEntity(PlaySite entity) {
        List<AttractionDto> attractions = Optional.ofNullable(entity.getAttractions()).orElse(new ArrayList<>()).stream().map(AttractionDto::fromEntity).toList();
        int capacity = entity.getCapacity();
        List<KidDto> kids = Optional.ofNullable(entity.getKids()).orElse(new ArrayList<>()).stream().sorted(Comparator.comparing(Kid::getCreatedAt)).map(KidDto::fromEntity).toList();
        boolean full = kids.size() >= capacity;
        double usage = full ? 100.00 : (double) kids.size() / capacity * 100.00;
        List<KidDto> players = new ArrayList<>(full ? kids.subList(0, capacity) : kids);
        List<KidDto> queue = full ? new ArrayList<>(kids.subList(capacity, kids.size())) : new ArrayList<>();
        return new PlaySiteDto(entity.getId(), attractions, players, queue, capacity, usage);
    }

    public static PlaySite toEntity(PlaySiteDto dto, PlaySite playSiteOverride) {
        AtomicInteger capacity = new AtomicInteger();
        PlaySite result = new PlaySite(dto.id(), null, null, 0);
        List<Attraction> attractions = new ArrayList<>();
        dto.attractions.forEach(attraction -> {
            capacity.addAndGet(attraction.capacity());
            attractions.add(AttractionDto.toEntity(attraction, playSiteOverride != null ? playSiteOverride : result));
        });
        result.setCapacity(capacity.get());
        result.setAttractions(attractions);
        return result;
    }

    public PlaySite toEntity() {
        return toEntity(this, null);
    }
}
