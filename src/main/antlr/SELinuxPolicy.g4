grammar SELinuxPolicy;

@header {
package com.selinux.analyzer.parser;
}

// 顶层规则：策略由若干语句组成
policy: statement* EOF ;

// 语句可以是 allow 规则或 neverallow 规则
statement: allowStatement | neverallowStatement ;

// 定义一个 "allow" 语句
allowStatement: 'allow' src=setExpr tgt=setExpr ':' cls=setExpr perms=setExpr ';' ;

// 定义一个 "neverallow" 语句
neverallowStatement: 'neverallow' src=setExpr tgt=setExpr ':' cls=setExpr perms=setExpr ';' ;

// 集合表达式，表示一个标识符或一个用大括号括起来的标识符列表
setExpr: ID | '{' idList '}' ;

// 一个或多个标识符（用空格分隔）
idList: ID (ID)* ;

// 词法规则
ID      : [a-zA-Z_][a-zA-Z0-9_]* ;        // 类型、类、权限等标识符
COMMENT : '#' ~[\r\n]* -> skip ;          // 以 # 开头的行注释
WS      : [ \t\r\n]+ -> skip ;            // 跳过空格、制表符和换行符