package com.selinux.analyzer.parser;

/**
 * 表示 SELinux 策略文件解析错误的异常类
 */
public class PolicyParseException extends Exception {
    public PolicyParseException(String message) {
        super(message);
    }
}