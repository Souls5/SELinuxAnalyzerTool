# SELinuxAnalyzerTool

基于 Java + ANTLR + JavaFX 的 SELinux 策略冲突分析工具，支持命令行（CLI）与图形界面（GUI）双模式运行，适用于简化语法的 SELinux 策略测试与分析。

---

## 🧾 项目简介

本项目旨在帮助用户对简化版 SELinux 策略文件进行解析、冲突检测并输出检测结果。核心功能包括：

- 策略语法解析（基于 ANTLR）
- 冲突检测（如 `allow` 与 `neverallow` 的权限冲突）
- 命令行模式快速测试
- 图形界面可视化交互（JavaFX）

---

## 📁 项目结构

```
SELinuxAnalyzerTool/
├── pom.xml                       # Maven 项目配置文件
├── README.md                    # 项目说明文档
├── src/
│   ├── main/
│   │   ├── java/com/selinux/analyzer/
│   │   │   ├── SelinuxAnalyzerMain.java   # 主入口类，根据参数判断 CLI / GUI
│   │   │   ├── SelinuxAnalyzerCLI.java    # 命令行模式入口
│   │   │   ├── SelinuxAnalyzerApp.java    # JavaFX GUI 主程序
│   │   │   ├── parser/PolicyParser.java   # ANTLR 解析封装类
│   │   │   ├── checker/ConflictChecker.java # 冲突检测逻辑
│   │   │   └── model/Rule.java 等模型类
│   │   ├── antlr/SELinuxPolicy.g4         # 策略语法定义文件
│   │   └── resources/                     # GUI 用的资源文件（如 FXML）
│   └── test/
│       ├── java/com/selinux/analyzer/PolicyParserTest.java
│       └── resources/policies/           # 测试策略文件（含冲突 / 无冲突 / 语法错误）
```

---

## 🚀 快速开始

### ✅ 构建项目

```bash
# 编译 ANTLR 语法 + 编译项目 + 运行测试 + 构建可运行 JAR
mvn clean package
```

### 📦 运行命令行版本（CLI）

```bash
java -jar target/SELinuxAnalyzerTool-1.0.0.jar --cli src/test/resources/policies/example_conflict.te
```

### 🖥️ 运行图形界面版本（GUI）

```bash
java -jar target/SELinuxAnalyzerTool-1.0.0.jar --gui
```

> 如果你使用 JDK 8，请提前配置 JavaFX 的运行参数；推荐使用 JDK 11+，无需额外配置。

---

## 📄 示例策略（简化语法）

```
allow ping_t net_raw_t : raw_socket { read write };
neverallow ping_t net_raw_t : raw_socket write;
```

运行结果将检测出权限冲突。

---

## 📅 最后更新

2025-03-23  
作者：[Souls5](https://github.com/Souls5)
