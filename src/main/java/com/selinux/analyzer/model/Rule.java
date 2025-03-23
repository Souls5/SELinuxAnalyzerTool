package com.selinux.analyzer.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 规则基类，表示一个通用的 SELinux 策略规则
 */
public abstract class Rule {
    protected Set<String> sourceTypes;
    protected Set<String> targetTypes;
    protected Set<String> classes;
    protected Set<String> permissions;

    public Rule(List<String> sourceTypes, List<String> targetTypes, List<String> classes, List<String> permissions) {
        // 使用 LinkedHashSet 维护插入顺序
        this.sourceTypes = new LinkedHashSet<>(sourceTypes);
        this.targetTypes = new LinkedHashSet<>(targetTypes);
        this.classes = new LinkedHashSet<>(classes);
        this.permissions = new LinkedHashSet<>(permissions);
    }

    public Set<String> getSourceTypes() {
        return Collections.unmodifiableSet(sourceTypes);
    }

    public Set<String> getTargetTypes() {
        return Collections.unmodifiableSet(targetTypes);
    }

    public Set<String> getClasses() {
        return Collections.unmodifiableSet(classes);
    }

    public Set<String> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    @Override
    public abstract String toString();
}
