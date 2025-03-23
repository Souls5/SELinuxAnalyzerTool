package com.selinux.analyzer;

import com.selinux.analyzer.parser.PolicyParser;
import com.selinux.analyzer.parser.PolicyParseException;
import com.selinux.analyzer.checker.ConflictChecker;
import com.selinux.analyzer.model.Conflict;
import com.selinux.analyzer.model.Rule;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class PolicyParserTest {

    @Test
    public void testValidPolicyNoConflict() throws URISyntaxException, PolicyParseException {
        File policyFile = getResourceFile("policies/example_valid.te");
        List<Rule> rules = PolicyParser.parse(policyFile);
        // 对有效策略，期望没有冲突
        List<Conflict> conflicts = ConflictChecker.findConflicts(rules);
        Assert.assertTrue("有效策略中不应有冲突", conflicts.isEmpty());
    }

    @Test
    public void testConflictDetected() throws URISyntaxException, PolicyParseException {
        File policyFile = getResourceFile("policies/example_conflict.te");
        List<Rule> rules = PolicyParser.parse(policyFile);
        List<Conflict> conflicts = ConflictChecker.findConflicts(rules);
        // 对于冲突策略，应该检测到冲突
        Assert.assertFalse("应检测到冲突", conflicts.isEmpty());
    }

    @Test
    public void testSyntaxError() {
        File policyFile;
        try {
            policyFile = getResourceFile("policies/example_syntax_error.te");
        } catch (URISyntaxException e) {
            throw new RuntimeException("加载测试资源失败", e);
        }
        try {
            PolicyParser.parse(policyFile);
            Assert.fail("应抛出 PolicyParseException 异常");
        } catch (PolicyParseException e) {
            // 捕获到异常则测试通过
            Assert.assertTrue(e.getMessage().toLowerCase().contains("语法"));
        }
    }

    private File getResourceFile(String resourcePath) throws URISyntaxException {
        return new File(getClass().getClassLoader().getResource(resourcePath).toURI());
    }
}