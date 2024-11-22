package io.github.yienruuuuu.bean.enums;

import lombok.Getter;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Getter
public enum RoleType {
    MANAGER(999),
    VIP(2),
    NORMAL(1),
    BLOCKED(0);

    private final int level;

    RoleType(int level) {
        this.level = level;
    }

    public boolean hasPermission(RoleType requiredRoleType) {
        return this.level >= requiredRoleType.level;
    }
}
