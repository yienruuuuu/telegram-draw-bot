package io.github.yienruuuuu.bean.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "text", schema = "tg_draw_bot")
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @Lob
    @Column(name = "content")
    private String content;

    @Override
    public String toString() {
        String languageCode = (language != null && language.getLanguageCode() != null) ? language.getLanguageCode() : "unknown";
        return languageCode + ": " + content;
    }
}