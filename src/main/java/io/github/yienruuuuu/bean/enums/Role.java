package io.github.yienruuuuu.bean.enums;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public enum Role {
    MANAGER(999),
    VIP(2),
    NORMAL(1);

    private final int level;

    Role(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean hasPermission(Role requiredRole) {
        return this.level >= requiredRole.level;
    }
}
