package io.github.yienruuuuu.bean.entity;

import io.github.yienruuuuu.bean.enums.FileType;
import io.github.yienruuuuu.bean.enums.RarityType;
import io.github.yienruuuuu.bean.enums.SendTextType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "resource", schema = "tg_draw_bot")
public class Resource extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false, length = 16)
    private FileType fileType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rarity_type", nullable = false, length = 16)
    private RarityType rarityType;

    @Size(max = 128)
    @Column(name = "file_id_main_bot", length = 128)
    private String fileIdMainBot;

    @Size(max = 128)
    @Column(name = "file_id_manage_bot", length = 128)
    private String fileIdManageBot;

    @Size(max = 255)
    @Column(name = "tags")
    private String tags;

    @Size(max = 52)
    @Column(name = "unique_id")
    private String uniqueId;

    @NotNull
    @Column(name = "is_in_used")
    private boolean inUsed;

    @Column(name = "send_text_type")
    @Enumerated(EnumType.STRING)
    private SendTextType sendTextType;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "resource_text",
            schema = "tg_draw_bot",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "text_id")
    )
    private List<Text> texts;
}