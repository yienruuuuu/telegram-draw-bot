package io.github.yienruuuuu.bean.entity;

import io.github.yienruuuuu.bean.enums.PointType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "point_log", schema = "tg_draw_bot")
public class PointLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 10)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "point_type", nullable = false, length = 10)
    private PointType pointType;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @NotNull
    @Column(name = "balance_before", nullable = false)
    private Integer balanceBefore;

    @NotNull
    @Column(name = "balance_after", nullable = false)
    private Integer balanceAfter;

    @Size(max = 255)
    @NotNull
    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "telegram_payment_charge_id")
    private String telegramPaymentChargeId;

    @Column(name = "provider_payment_charge_id")
    private String providerPaymentChargeId;
}