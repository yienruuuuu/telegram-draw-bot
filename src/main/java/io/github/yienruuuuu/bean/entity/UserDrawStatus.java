package io.github.yienruuuuu.bean.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_draw_status", schema = "tg_draw_bot")
public class UserDrawStatus extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_pool_id", nullable = false)
    private CardPool cardPool;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "total_draw_count", nullable = false)
    private Integer totalDrawCount;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "ur_draw_count", nullable = false)
    private Integer urDrawCount;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "ssr_draw_count", nullable = false)
    private Integer ssrDrawCount;

    @Column(name = "last_draw_time")
    private Instant lastDrawTime;

    @Column(name = "dynamic_drop_rate", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Double> dynamicDropRate;
}