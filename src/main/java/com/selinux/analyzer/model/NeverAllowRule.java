package com.selinux.analyzer.model;

import java.util.List;
import java.util.Set;

/**
 * 不允许规则，表示 SELinux 策略中的“neverallow”类型规则
 */
public class NeverAllowRule extends Rule {
    public NeverAllowRule(List<String> sourceTypes, List<String> targetTypes, List<String> classes, List<String> permissions) {
        super(sourceTypes, targetTypes, classes, permissions);
    }

    @Override
    public String toString() {
        String src = formatSet(sourceTypes);
        String tgt = formatSet(targetTypes);
        String cls = formatSet(classes);
        String perm = permissions.size() > 1 ? "{ " + String.join(" ", permissions) + " }" : permissions.iterator().next();
        return "neverallow " + src + " " + tgt + " : " + cls + " " + perm + ";";
    }

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