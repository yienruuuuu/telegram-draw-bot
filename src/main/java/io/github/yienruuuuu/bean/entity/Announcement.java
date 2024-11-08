package io.github.yienruuuuu.bean.entity;

import io.github.yienruuuuu.bean.enums.AnnouncementType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "announcement", schema = "tg_draw_bot")
public class Announcement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private AnnouncementType type;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "announcement_text",
            schema = "tg_draw_bot",
            joinColumns = @JoinColumn(name = "announcement_id"),
            inverseJoinColumns = @JoinColumn(name = "text_id")
    )
    private List<Text> texts;  // 關聯的 Text 列表

}