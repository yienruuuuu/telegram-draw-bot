package io.github.yienruuuuu.bean.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "language", schema = "tg_draw_bot")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "language_code", nullable = false, length = 20)
    private String languageCode;

    @Size(max = 50)
    @NotNull
    @Column(name = "language_name", nullable = false, length = 50)
    private String languageName;
}