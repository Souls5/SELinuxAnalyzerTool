package com.selinux.analyzer.checker;

import com.selinux.analyzer.model.AllowRule;
import com.selinux.analyzer.model.NeverAllowRule;
import com.selinux.analyzer.model.Rule;
import com.selinux.analyzer.model.Conflict;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 冲突检测器，用于检测 SELinux 策略文件中的冲突
 */
public class ConflictChecker {

    /**
     * 查找给定规则列表中的冲突
     * @param rules 规则列表
     * @return 包含冲突的 Conflict 对象列表
     */
    public static List<Conflict> findConflicts(List<Rule> rules) {
        List<Conflict> conflicts = new ArrayList<>();
        // 将规则分为 AllowRule 和 NeverAllowRule
        List<AllowRule> allowRules = new ArrayList<>();
        List<NeverAllowRule> neverRules = new ArrayList<>();
        for (Rule r : rules) {
            if (r instanceof AllowRule) {
                allowRules.add((AllowRule) r);
            } else if (r instanceof NeverAllowRule) {
                neverRules.add((NeverAllowRule) r);
            }
        }
        // 比较每个 allow 规则与 neverallow 规则之间是否有冲突
        for (AllowRule allow : allowRules) {
            for (NeverAllowRule never : neverRules) {
                // 如果源类型、目标类型和类名重叠，检查权限是否重叠
                if (!disjoint(allow.getSourceTypes(), never.getSourceTypes()) &&
                        !disjoint(allow.getTargetTypes(), never.getTargetTypes()) &&
                        !disjoint(allow.getClasses(), never.getClasses())) {
                    // 查找重叠的权限
                    Set<String> commonPerms = new HashSet<>(allow.getPermissions());
                    commonPerms.retainAll(never.getPermissions());
                    if (!commonPerms.isEmpty()) {
                        conflicts.add(new Conflict(allow, never, commonPerms));
                    }
                }
            }
        }
        return conflicts;
    }

    // 判断两个集合是否有交集
    private static boolean disjoint(Set<String> set1, Set<String> set2) {
        for (String s : set1) {
            if (set2.contains(s)) {
                return false;
            }
        }
        return true;
    }
}
