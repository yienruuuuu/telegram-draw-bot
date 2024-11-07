package io.github.yienruuuuu.use_case.port.in.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Eric.Lee
 * Date: 2024/11/6
 */
@Getter
@Setter
@AllArgsConstructor
public class Task {
    private Long id;
    private String title;
    private boolean completed;
}
