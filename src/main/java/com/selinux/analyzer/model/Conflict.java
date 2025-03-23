package com.selinux.analyzer.model;

import java.util.Set;

/**
 * 冲突类，表示 SELinux 策略中一个 allow 规则和一个 neverallow 规则之间的冲突
 */
public class Conflict {
    private AllowRule allowRule;
    private NeverAllowRule neverAllowRule;
    private Set<String> conflictPermissions;

    public Conflict(AllowRule allowRule, NeverAllowRule neverAllowRule, Set<String> conflictPermissions) {
        this.allowRule = allowRule;
        this.neverAllowRule = neverAllowRule;
        this.conflictPermissions = conflictPermissions;
    }

    public AllowRule getAllowRule() {
        return allowRule;
    }

    public NeverAllowRule getNeverAllowRule() {
        return neverAllowRule;
    }

    public Set<String> getConflictPermissions() {
        return conflictPermissions;
    }

    @Override
    public String toString() {
        return "冲突: " + allowRule.toString() + " 与 "
                + neverAllowRule.toString() + " 冲突 [重叠权限: " + conflictPermissions + "]";
    }
}