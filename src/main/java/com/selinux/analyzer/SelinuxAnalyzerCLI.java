package com.selinux.analyzer;

import com.selinux.analyzer.parser.PolicyParser;
import com.selinux.analyzer.checker.ConflictChecker;
import com.selinux.analyzer.model.Conflict;
import com.selinux.analyzer.model.Rule;
import java.io.File;
import java.util.List;

/**
 * SELinux 策略分析工具的命令行界面（CLI）实现
 */
public class SelinuxAnalyzerCLI {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("用法: SelinuxAnalyzerCLI <策略文件>");
            System.exit(1);
        }
        run(args[0]);
    }

    public static void run(String policyFilePath) {
        try {
            File policyFile = new File(policyFilePath);
            if (!policyFile.exists()) {
                System.err.println("找不到策略文件: " + policyFilePath);
                return;
            }
            // 解析策略文件
            List<Rule> rules = PolicyParser.parse(policyFile);
            // 检查是否有冲突
            List<Conflict> conflicts = ConflictChecker.findConflicts(rules);
            if (conflicts.isEmpty()) {
                System.out.println("策略中未发现冲突。");
            } else {
                System.out.println("检测到以下冲突：");
                for (Conflict conflict : conflicts) {
                    System.out.println(conflict);
                }
            }
        } catch (Exception e) {
            // 处理解析错误或其他异常
            System.err.println("错误: " + e.getMessage());
        }
    }
}