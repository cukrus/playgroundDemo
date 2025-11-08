package net.cukrus.demo.playground.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PlaySite {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(mappedBy = "playSite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attraction> attractions;
    @OneToMany(mappedBy = "playSite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Kid> kids;
    private int capacity;
}
