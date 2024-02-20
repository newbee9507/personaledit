package com.seb_main_034.SERVER.auth.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @NotBlank
    private String token;

    private OffsetDateTime createAt;

    public RefreToken(String token) {
        this.token = token;
    }

    @PrePersist
    public void setCreateAt() {
        this.createAt = OffsetDateTime.now();
    }
}
