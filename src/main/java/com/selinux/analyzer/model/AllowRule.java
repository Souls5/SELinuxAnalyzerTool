package com.selinux.analyzer.model;

import java.util.List;
import java.util.Set;

/**
 * 允许规则，表示 SELinux 策略中的“allow”类型规则
 */
public class AllowRule extends Rule {
    public AllowRule(List<String> sourceTypes, List<String> targetTypes, List<String> classes, List<String> permissions) {
        super(sourceTypes, targetTypes, classes, permissions);
    }

    @Override
    public String toString() {
        String src = formatSet(sourceTypes);
        String tgt = formatSet(targetTypes);
        String cls = formatSet(classes);
        String perm = permissions.size() > 1 ? "{ " + String.join(" ", permissions) + " }" : permissions.iterator().next();
        return "allow " + src + " " + tgt + " : " + cls + " " + perm + ";";
    }

    // 格式化集合为带有大括号的字符串（例如 { read write }）
    private String formatSet(Set<String> set) {
        if (set.size() > 1) {
            return "{ " + String.join(" ", set) + " }";
        } else if (set.isEmpty()) {
            return "{}";
        } else {
            return set.iterator().next();
        }
    }
}