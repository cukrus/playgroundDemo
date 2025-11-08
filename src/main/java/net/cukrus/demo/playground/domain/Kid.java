package net.cukrus.demo.playground.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Kid {
    @Id
    private Long ticket;
    @ManyToOne
    private PlaySite playSite;
    private String name;
    private Integer age;
    private Instant createdAt;
}
