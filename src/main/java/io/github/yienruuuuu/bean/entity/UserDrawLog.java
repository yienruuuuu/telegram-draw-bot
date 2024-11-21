package io.github.yienruuuuu.bean.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_draw_log", schema = "tg_draw_bot")
public class UserDrawLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "card_pool_id", nullable = false)
    private CardPool cardPool;

    @NotNull
    @Column(name = "is_free", nullable = false)
    private Boolean isFree = false;

    @NotNull
    @Column(name = "points_used", nullable = false)
    private Integer pointsUsed;
}