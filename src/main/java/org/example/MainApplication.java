package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.model.Paper;
import org.example.model.PaperAction;

import java.util.List;

public class MainApplication extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {
        PaperAction paperAction = new PaperAction();
        List<Paper> papers = paperAction.queryAll();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 550);
        scene.getRoot().setStyle("-fx-font-family: 'Noto Sans CJK SC';");
        stage.setTitle("论文搜索系统");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}