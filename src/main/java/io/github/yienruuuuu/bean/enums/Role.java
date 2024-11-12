package io.github.yienruuuuu.bean.enums;

import lombok.Getter;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Getter
public enum Role {
    MANAGER(999),
    VIP(2),
    NORMAL(1);

    private final int level;

    Role(int level) {
        this.level = level;
    }

    public boolean hasPermission(Role requiredRole) {
        return this.level >= requiredRole.level;
    }
}
