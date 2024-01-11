package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.commons.text.StringEscapeUtils;
import org.example.model.Paper;
import org.example.model.PaperAction;

import java.io.*;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class Controller {

    // 选中项内容
    public TextField idTextField;
    public TextField titleTextField;
    public TextField authorTextField;
    public TextField sourceTextField;
    public TextField timeTextField;
    public TextField emailTextField;
    public TextField searchField;
    // 主窗口表格
    @FXML
    private TableView<Paper> tableView;
    // 对话框部件
    @FXML
    private Dialog<Object> aboutDialog;
    // 爬取选项
    @FXML
    private TextField keywordTextField;
    @FXML
    private TextField maxRecordsTextField;
    @FXML
    private ChoiceBox<String> siteChoiceBox;

    private static FileWriter getFileWriter(File file, List<Paper> papers) throws IOException {
        FileWriter csvWriter = new FileWriter(file);
        csvWriter.append("id,标题,作者,来源,时间,邮箱\n");
        for (Paper paper : papers) {
            csvWriter.append(StringEscapeUtils.escapeCsv(paper.getId().toString()));
            csvWriter.append(",");
            csvWriter.append(StringEscapeUtils.escapeCsv(paper.getTitle()));
            csvWriter.append(",");
            csvWriter.append(StringEscapeUtils.escapeCsv(paper.getAuthor()));
            csvWriter.append(",");
            csvWriter.append(StringEscapeUtils.escapeCsv(paper.getSource()));
            csvWriter.append(",");
            csvWriter.append(StringEscapeUtils.escapeCsv(paper.getTime()));
            csvWriter.append(",");
            csvWriter.append(StringEscapeUtils.escapeCsv(paper.getEmail()));
            csvWriter.append("\n");

        }
        return csvWriter;
    }

    public void initialize() throws Exception {
        idTextField.setEditable(false);
        keywordTextField.setText("计算机");
        maxRecordsTextField.setText("10");
        tableView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleRowSelection(newValue);
            }
        }));
    }

    @FXML
    public void showAboutDialog() {
        aboutDialog = new Dialog<>();
        aboutDialog.setTitle("关于");
        aboutDialog.setHeaderText("论文搜索系统");
        aboutDialog.getDialogPane().setStyle("-fx-font-family: 'Noto Sans CJK SC';");
        aboutDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        aboutDialog.getDialogPane().setContentText("Pilrymage");
    }

    public void crawlPaperAction() {
        try {
            int maxRecords = parseInt(maxRecordsTextField.getText());
            String keyword = keywordTextField.getText();
            if (Objects.equals(keyword, "")) {
                throw new Exception("搜索栏不可为空！");
            }
            String selectSite = siteChoiceBox.getValue();
            if (maxRecords > 0 && maxRecords < 10000) {
                Crawler crawler = new Crawler();
                crawler.crawlData(maxRecords, keyword, selectSite);
                refreshTableView();
            } else {
                throw new Exception("爬取数量错误！\n搜索范围：1~10000");
            }
        } catch (NumberFormatException e) {
            showWarningAlert(new Exception("爬取输入错误！请输入数字"));
        } catch (Exception e) {
            showWarningAlert(e);
        }
    }

    public void refreshTableView() throws Exception {
        PaperAction paperAction = new PaperAction();
        List<Paper> papers = paperAction.queryAll();

        ObservableList<Paper> data = FXCollections.observableArrayList(papers);
        tableView.setItems(data);
    }

    public void handleRowSelection(Paper selectedData) {
        idTextField.setText(selectedData.getId().toString());
        titleTextField.setText(selectedData.getTitle());
        authorTextField.setText(selectedData.getAuthor());
        sourceTextField.setText(selectedData.getSource());
        timeTextField.setText(selectedData.getTime());
        emailTextField.setText(selectedData.getEmail());
    }

    public void addTableView() throws Exception {
        ButtonType ans = showConfirmationAlert("确认添加？");
        if (ans == ButtonType.CANCEL) {
            return;
        }
        try {
            if (Objects.equals(idTextField.getText(), "")) {
                throw new Exception("添加失败！\n请勿输入空ID");
            }
            if (Objects.equals(titleTextField.getText(), "")) {
                throw new Exception("添加失败！\n请勿输入空标题");
            }
            if (Objects.equals(authorTextField.getText(), "")) {
                throw new Exception("添加失败！\n请勿输入空作者");
            }
        } catch (Exception e) {
            showWarningAlert(e);
        }
        String newTitle = titleTextField.getText();
        String newAuthor = authorTextField.getText();
        String newSource = sourceTextField.getText();
        String newTime = timeTextField.getText();
        String newEmail = emailTextField.getText();
        PaperAction paperAction = new PaperAction();
        Paper paper = new Paper();
        paper.setTitle(newTitle);
        paper.setAuthor(newAuthor);
        paper.setEmail(newEmail);
        paper.setSource(newSource);
        paper.setTime(newTime);
        paperAction.add(paper);
        refreshTableView();
    }

    public void deleteTableView() {
        ButtonType ans = showConfirmationAlert("确认删除？");
        if (ans == ButtonType.CANCEL) {
            return;
        }
        try {
            PaperAction paperAction = new PaperAction();
            paperAction.delete(parseInt(idTextField.getText()));
            refreshTableView();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showWarningAlert(new Exception("删除失败！\n请先选择一行数据"));
        } catch (Exception e) {
            e.printStackTrace();
            showWarningAlert(e);
        }
    }

    public void updateTableView() {
        ButtonType ans = showConfirmationAlert("确认修改？");
        if (ans == ButtonType.CANCEL) {
            return;
        }
        try {
            if (Objects.equals(idTextField.getText(), "")) {
                throw new Exception("添加失败！\n请勿输入空ID");
            }
            if (Objects.equals(titleTextField.getText(), "")) {
                throw new Exception("添加失败！\n请勿输入空标题");
            }
            if (Objects.equals(authorTextField.getText(), "")) {
                throw new Exception("添加失败！\n请勿输入空作者");
            }
        } catch (Exception e) {
            showWarningAlert(e);
        }
        PaperAction paperAction = new PaperAction();
        Paper paper = new Paper();
        paper.setId(parseInt(idTextField.getText()));
        paper.setTitle(titleTextField.getText());
        paper.setAuthor(authorTextField.getText());
        paper.setSource(sourceTextField.getText());
        paper.setTime(timeTextField.getText());
        paper.setEmail(emailTextField.getText());
        try {
            paperAction.update(paper);
            refreshTableView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchTableView() throws Exception {
        String keyword = searchField.getText();
        if (Objects.equals(keyword, "")) {
            throw new Exception("搜索栏不可为空！");
        }
        PaperAction paperAction = new PaperAction();
        List<Paper> papers = paperAction.queryByTitle(keyword);
        ObservableList<Paper> data = FXCollections.observableArrayList(papers);
        tableView.setItems(data);
    }

    private void showWarningAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.getDialogPane().setStyle("-fx-font-family: 'Noto Sans CJK SC';");
        ;
        alert.setTitle("非法输入");
        alert.setHeaderText(null);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private ButtonType showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setStyle("-fx-font-family: 'Noto Sans CJK SC';");
        ;
        alert.setTitle("确认操作");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("确认");
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");

        okButton.setOnAction(event -> {
            alert.setResult(ButtonType.OK);
            alert.close();
        });
        cancelButton.setOnAction(event -> {
            alert.setResult(ButtonType.CANCEL);
            alert.close();
        });

        alert.showAndWait();
        return null;
    }

    public void quitCrawler(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void exportCSV(ActionEvent actionEvent) throws Exception {
        PaperAction paperAction = new PaperAction();
        List<Paper> papers = paperAction.queryAll();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            FileWriter csvWriter = getFileWriter(file, papers);
            csvWriter.flush();
            csvWriter.close();
        }
    }

    public void importCSV(ActionEvent actionEvent) throws Exception {
        PaperAction paperAction = new PaperAction();

        String line = "";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            BufferedReader csvReader = new BufferedReader(new FileReader(file));
            line = csvReader.readLine();
            while ((line = csvReader.readLine()) != null) {
                String[] data = line.split(",");
                Paper paper = new Paper();
                paper.setId(parseInt(StringEscapeUtils.unescapeCsv(data[0])));
                paper.setTitle(StringEscapeUtils.unescapeCsv(data[1]));
                paper.setAuthor(StringEscapeUtils.unescapeCsv(data[2]));
                paper.setSource(StringEscapeUtils.unescapeCsv(data[3]));
                paper.setTime(StringEscapeUtils.unescapeCsv(data[4]));
                if (data.length == 6) {
                    paper.setEmail(StringEscapeUtils.unescapeCsv(data[5]));
                }
                paperAction.add(paper);
            }


            csvReader.close();
            refreshTableView();
        }
    }
}
