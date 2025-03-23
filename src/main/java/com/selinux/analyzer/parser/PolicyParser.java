package com.selinux.analyzer.parser;

import com.selinux.analyzer.model.AllowRule;
import com.selinux.analyzer.model.NeverAllowRule;
import com.selinux.analyzer.model.Rule;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 使用 ANTLR 生成的词法分析器和语法解析器类
import com.selinux.analyzer.parser.SELinuxPolicyLexer;
import com.selinux.analyzer.parser.SELinuxPolicyParser;


public class PolicyParser {

    /**
     * 解析 SELinux 策略文件并返回规则列表
     * @param policyFile 策略文件路径
     * @return 规则列表
     * @throws PolicyParseException 如果解析出错
     */
    public static List<Rule> parse(File policyFile) throws PolicyParseException {
        try {
            // 创建词法分析器和语法分析器
            CharStream input = CharStreams.fromPath(policyFile.toPath());
            SELinuxPolicyLexer lexer = new SELinuxPolicyLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SELinuxPolicyParser parser = new SELinuxPolicyParser(tokens);
            // 设置错误监听器，遇到语法错误时抛出异常
            parser.removeErrorListeners();
            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) throws ParseCancellationException {
                    throw new ParseCancellationException("第 " + line + " 行，第 " + charPositionInLine + " 列 " + msg);
                }
            });
            // 解析策略文件
            SELinuxPolicyParser.PolicyContext policyCtx = parser.policy();
            // 遍历解析树，构建规则对象
            List<Rule> rules = new ArrayList<>();
            for (SELinuxPolicyParser.StatementContext stmtCtx : policyCtx.statement()) {
                if (stmtCtx.allowStatement() != null) {
                    // 构建 AllowRule 对象
                    rules.add(buildAllowRule(stmtCtx.allowStatement()));
                } else if (stmtCtx.neverallowStatement() != null) {
                    // 构建 NeverAllowRule 对象
                    rules.add(buildNeverAllowRule(stmtCtx.neverallowStatement()));
                }
            }
            return rules;
        } catch (IOException e) {
            throw new PolicyParseException("读取策略文件失败: " + e.getMessage());
        } catch (ParseCancellationException e) {
            // 捕获语法错误
            throw new PolicyParseException("策略文件语法错误: " + e.getMessage());
        }
    }

    // 构建 AllowRule 对象的帮助方法
    private static AllowRule buildAllowRule(SELinuxPolicyParser.AllowStatementContext ctx) {
        List<String> sources = extractIdList(ctx.src);
        List<String> targets = extractIdList(ctx.tgt);
        List<String> classes = extractIdList(ctx.cls);
        List<String> perms = extractIdList(ctx.perms);
        return new AllowRule(sources, targets, classes, perms);
    }

    // 构建 NeverAllowRule 对象的帮助方法
    private static NeverAllowRule buildNeverAllowRule(SELinuxPolicyParser.NeverallowStatementContext ctx) {
        List<String> sources = extractIdList(ctx.src);
        List<String> targets = extractIdList(ctx.tgt);
        List<String> classes = extractIdList(ctx.cls);
        List<String> perms = extractIdList(ctx.perms);
        return new NeverAllowRule(sources, targets, classes, perms);
    }

    // 从 Set 表达式解析出标识符列表
    private static List<String> extractIdList(SELinuxPolicyParser.SetExprContext ctx) {
        List<String> ids = new ArrayList<>();
        if (ctx.ID() != null) {
            // 单个标识符
            ids.add(ctx.ID().getText());
        } else if (ctx.idList() != null) {
            for (TerminalNode node : ctx.idList().ID()) {
                ids.add(node.getText());
            }
        }
        return ids;
    }
}