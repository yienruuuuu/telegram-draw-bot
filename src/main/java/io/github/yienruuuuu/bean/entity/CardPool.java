package io.github.yienruuuuu.bean.entity;

import io.github.yienruuuuu.bean.enums.CardPoolType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "card_pool", schema = "tg_draw_bot")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardPool extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "start_at")
    private Instant startAt;

    @Column(name = "end_at")
    private Instant endAt;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_open", nullable = false)
    private boolean isOpen = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pool_type", nullable = false)
    private CardPoolType cardPoolType;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "card_pool_text",
            schema = "tg_draw_bot",
            joinColumns = @JoinColumn(name = "card_pool_id"),
            inverseJoinColumns = @JoinColumn(name = "text_id")
    )
    private List<Text> texts;

    @OneToMany(mappedBy = "cardPool", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Card> cards;
}