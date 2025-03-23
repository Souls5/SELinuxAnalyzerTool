package com.selinux.analyzer;

import com.selinux.analyzer.parser.PolicyParser;
import com.selinux.analyzer.checker.ConflictChecker;
import com.selinux.analyzer.model.Conflict;
import com.selinux.analyzer.model.Rule;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;

/**
 * JavaFX 图形界面应用程序，用于显示 SELinux 策略的冲突分析结果
 */
public class SelinuxAnalyzerApp extends Application {
    private TextArea outputArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SELinux 策略分析工具");
        // UI 组件：一个按钮用于打开文件，文本框用于显示结果
        Button loadButton = new Button("打开策略文件");
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);

        // 文件选择器事件
        loadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择 SELinux 策略文件");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("SELinux 策略文件 (*.te)", "*.te")
            );
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                analyzePolicy(file);
            }
        });

        // 布局设置
        VBox root = new VBox(10, loadButton, outputArea);
        root.setPrefSize(600, 400);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** 解析并分析选定的策略文件，然后显示结果 */
    private void analyzePolicy(File file) {
        try {
            List<Rule> rules = PolicyParser.parse(file);
            List<Conflict> conflicts = ConflictChecker.findConflicts(rules);
            if (conflicts.isEmpty()) {
                outputArea.setText("策略文件 " + file.getName() + " 中未发现冲突。");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("策略文件 " + file.getName() + " 中的冲突：\n");
                for (Conflict conflict : conflicts) {
                    sb.append(conflict.toString()).append("\n");
                }
                outputArea.setText(sb.toString());
            }
        } catch (Exception e) {
            outputArea.setText("错误: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}