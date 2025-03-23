package com.selinux.analyzer;

import javafx.application.Application;

/**
 * 主入口类，根据输入参数决定运行命令行模式（CLI）或图形界面模式（GUI）
 */
public class SelinuxAnalyzerMain {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("--cli")) {
            // CLI 模式，要求传入策略文件路径
            if (args.length < 2) {
                System.err.println("用法: --cli <策略文件>");
                System.exit(1);
            }
            String policyFile = args[1];
            SelinuxAnalyzerCLI.run(policyFile);
        } else if (args.length > 0 && args[0].equalsIgnoreCase("--gui")) {
            // 启动 GUI 模式（JavaFX）
            Application.launch(SelinuxAnalyzerApp.class, args);
        } else if (args.length > 0) {
            // 如果传入其他参数，默认为 CLI 模式
            String policyFile = args[0];
            SelinuxAnalyzerCLI.run(policyFile);
        } else {
            // 默认运行 GUI 模式
            Application.launch(SelinuxAnalyzerApp.class);
        }
    }
}