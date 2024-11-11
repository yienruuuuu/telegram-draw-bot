package io.github.yienruuuuu.bean.entity;

import io.github.yienruuuuu.bean.enums.BotType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bot", schema = "tg_draw_bot")
public class Bot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private BotType type;

    @Size(max = 512)
    @Column(name = "bot_token", length = 512)
    private String botToken;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "bot_telegram_user_name")
    private String botTelegramUserName;
}