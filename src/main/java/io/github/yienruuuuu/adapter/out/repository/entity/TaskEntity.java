package io.github.yienruuuuu.adapter.out.repository.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Eric.Lee
 * Date: 2024/11/6
 */
@Entity
@Table(name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private boolean completed;
}
