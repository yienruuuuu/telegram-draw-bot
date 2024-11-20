package io.github.yienruuuuu.bean.entity;

import io.github.yienruuuuu.bean.enums.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "tg_draw_bot")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private RoleType role;

    @Size(max = 50)
    @NotNull
    @Column(name = "telegram_user_id", nullable = false, length = 50)
    private String telegramUserId;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @NotNull
    @Column(name = "is_block")
    private boolean isBlock;

    @NotNull
    @Column(name = "free_points")
    private Integer freePoints;

    @NotNull
    @Column(name = "purchased_points")
    private Integer purchasedPoints;

    @Column(name = "last_pick_rare_time")
    private Instant lastPickRareTime;

    @Column(name = "last_play_dice_time")
    private Instant lastPlayDiceTime;
}