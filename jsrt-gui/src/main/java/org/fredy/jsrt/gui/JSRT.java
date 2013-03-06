/* 
 * Copyright 2013 Fredy Wijaya
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.fredy.jsrt.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.scene.control.ListSpinner;
import name.antonsmirnov.javafx.dialog.Dialog;

import org.fredy.jsrt.api.SRT;
import org.fredy.jsrt.api.SRTInfo;
import org.fredy.jsrt.api.SRTReader;
import org.fredy.jsrt.api.SRTTimeFormat;
import org.fredy.jsrt.api.SRTTimeFormat.SRTTime;
import org.fredy.jsrt.api.SRTWriter;
import org.fredy.jsrt.editor.SRTEditor;
import org.fredy.jsrt.util.StringUtils;
import org.fredy.jsrt.util.VersionUtils;

/**
 * A GUI client code.
 * 
 * @author fredy
 */
public class JSRT extends Application {
    private ResourceBundle rb;
    private ObservableList<SRTWrapper> srtInfoData;
    private SRTInfo srtInfo;
    private File srtFile;
    // all the controls
    private Stage primaryStage;
    private Label filePathLabel;
    private Button openButton;
    private Button editButton;
    private Button insertButton;
    private Button removeButton;
//    private Button upButton;
//    private Button downButton;
    private Button checkForUpdateButton;
    private FileChooser fileChooser;
    private TableView<SRTWrapper> tableView;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("jsrt", locale);
        
        primaryStage.setTitle(ResourceBundleKeys.TITLE.getValue(rb,
            ResourceBundleKeys.VERSION.getValue(rb)));
        
        BorderPane root = new BorderPane();
        root.setTop(createTopPane());
        root.setRight(createRightPane());
        root.setCenter(createCenterPane());
        root.setBottom(createBottomPane());
        
        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private Node createBottomPane() {
        Label createdByLabel = LabelBuilder.create()
            .text(ResourceBundleKeys.LABEL_CREATED_BY.getValue(rb))
            .build();
        
        checkForUpdateButton = ButtonBuilder.create()
            .text(ResourceBundleKeys.BUTTON_CHECK_FOR_UPDATE.getValue(rb))
            .build();
        checkForUpdateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String message = "";
                try {
                    String latestVersion = VersionUtils.getLatestVersion();
                    String currentVersion = ResourceBundleKeys.VERSION.getValue(rb);
                    int comparison = VersionUtils.compare(currentVersion, latestVersion);
                    if (comparison == 0) {
                        message = ResourceBundleKeys.DIALOG_LATEST_VERSION_MESSAGE.getValue(rb,
                            latestVersion);
                    } else if(comparison == -1) {
                        message = ResourceBundleKeys.DIALOG_NEW_VERSION_MESSAGE.getValue(rb,
                            latestVersion);
                    } else {
                        // this shouldn't actually happen
                        message = ResourceBundleKeys.DIALOG_LATEST_VERSION_MESSAGE.getValue(rb);
                    }
                    Dialog.showInfo(ResourceBundleKeys.DIALOG_CHECK_FOR_UPDATE_TITLE.getValue(rb),
                        message, primaryStage);
                } catch (Exception e) {
                    Dialog.showThrowable(
                        ResourceBundleKeys.DIALOG_ERROR_TITLE.getValue(rb),
                        e.getMessage(), e, primaryStage);
                }
            }
        });
        
        HBox hbox = HBoxBuilder.create()
            .spacing(5)
            .padding(new Insets(10, 10, 10, 10))
            .children(createdByLabel, checkForUpdateButton)
            .build();
        
        VBox vbox = VBoxBuilder.create()
            .spacing(5)
            .padding(new Insets(10, 10, 10, 10))
            .children(hbox)
            .build();
        
        return vbox;
    }
    
    private Node createTopPane() {
        filePathLabel = new Label("");
        
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
            new FileChooser.ExtensionFilter(
                ResourceBundleKeys.FILECHOOSEREXT_FILTER.getValue(rb), "*.srt");
        fileChooser.getExtensionFilters().add(extFilter);
        
        Label subtitleLabel = LabelBuilder.create()
            .text(ResourceBundleKeys.LABEL_SUBTITLE.getValue(rb))
            .build();
        
        openButton = ButtonBuilder.create()
            .text(ResourceBundleKeys.BUTTON_OPEN.getValue(rb))
            .build();
        openButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evt) {
                try {
                    srtFile = fileChooser.showOpenDialog(primaryStage);
                    if (srtFile == null) {
                        return;
                    }
                    srtInfo = SRTReader.read(srtFile);
                    // Always make sure to clear the srtInfoData
                    srtInfoData.clear();
                    for (SRT srt : srtInfo) {
                        srtInfoData.add(new SRTWrapper(srt));
                    }
                    filePathLabel.setText("  [" + srtFile.getAbsolutePath() + "]"); 
                } catch (Exception e) {
                    Dialog.showThrowable(
                        ResourceBundleKeys.DIALOG_ERROR_TITLE.getValue(rb),
                        e.getMessage(), e, primaryStage);
                }
            }
        });
        
        HBox hbox = HBoxBuilder.create()
            .spacing(5)
            .padding(new Insets(10, 10, 10, 10))
            .children(subtitleLabel, openButton, filePathLabel)
            .build();
        
        VBox vbox = VBoxBuilder.create()
            .spacing(5)
            .padding(new Insets(10, 10, 10, 10))
            .children(hbox)
            .build();
        
        return vbox;
    }
    
    private void showInsertDialog(SRT prevSRT) {
        try {
            // It makes more sense to fill in the start time and end time from
            // the previous SRT record
            SRT srt = new SRT(prevSRT.number + 1, prevSRT.startTime, prevSRT.endTime, "");
            InsertDialog editDialog = new InsertDialog(rb, srt);
            editDialog.show();
        } catch (Exception e) {
            Dialog.showThrowable(
                ResourceBundleKeys.DIALOG_ERROR_TITLE.getValue(rb),
                e.getMessage(), e, primaryStage);
        }
    }
    
    private void showEditDialog() {
        try {
            SRT srt = tableView.getSelectionModel().getSelectedItem().srt;
            EditDialog editDialog = new EditDialog(rb, srt);
            editDialog.show();
        } catch (Exception e) {
            Dialog.showThrowable(
                ResourceBundleKeys.DIALOG_ERROR_TITLE.getValue(rb),
                e.getMessage(), e, primaryStage);
        }
    }
    
    private Node createRightPane() {
        editButton = ButtonBuilder.create()
            .maxWidth(Double.MAX_VALUE)
            .text(ResourceBundleKeys.BUTTON_EDIT.getValue(rb))
            .disable(true)
            .build();
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showEditDialog();
            }
        });
        
        insertButton = ButtonBuilder.create()
            .maxWidth(Double.MAX_VALUE)
            .text(ResourceBundleKeys.BUTTON_INSERT.getValue(rb))
            .disable(true)
            .build();
        insertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SRTWrapper sw = tableView.getSelectionModel().getSelectedItem();
                    if (sw == null) {
                        return;
                    }
                    showInsertDialog(sw.srt);
                } catch (Exception e) {
                    Dialog.showThrowable(
                        ResourceBundleKeys.DIALOG_ERROR_TITLE.getValue(rb),
                        e.getMessage(), e, primaryStage);
                }
            }
        });
        
        removeButton = ButtonBuilder.create()
            .maxWidth(Double.MAX_VALUE)
            .text(ResourceBundleKeys.BUTTON_REMOVE.getValue(rb))
            .disable(true)
            .build();
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SRTWrapper sw = tableView.getSelectionModel().getSelectedItem();
                    if (sw == null) {
                        return;
                    }
                    SRTEditor.removeSubtitle(srtInfo, sw.srt.number);
                    SRTWriter.write(srtFile, srtInfo);
                    srtInfoData.remove(sw);
                    if (srtInfoData.size() == 0) {
                        editButton.setDisable(true);
                        removeButton.setDisable(true);
                    }
                } catch (Exception e) {
                    Dialog.showThrowable(
                        ResourceBundleKeys.DIALOG_ERROR_TITLE.getValue(rb),
                        e.getMessage(), e, primaryStage);
                }
            }
        });
        
//        upButton = ButtonBuilder.create()
//            .maxWidth(Double.MAX_VALUE)
//            .text(ResourceBundleKeys.BUTTON_MOVE_UP.getValue(rb))
//            .build();
//        upButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                try {
//                    // TODO Auto-generated method stub
//                } catch (Exception e) {
//                    Dialog.showThrowable(
//                        ResourceBundleKeys.DIALOG_ERROR_TITLE.getValue(rb),
//                        e.getMessage(), e, primaryStage);
//                }
//            }
//        });
//        
//        downButton = ButtonBuilder.create()
//            .maxWidth(Double.MAX_VALUE)
//            .text(ResourceBundleKeys.BUTTON_MOVE_DOWN.getValue(rb))
//            .build();
//        downButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                try {
//                    // TODO Auto-generated method stub
//                } catch (Exception e) {
//                    Dialog.showThrowable(
//                        ResourceBundleKeys.DIALOG_ERROR_TITLE.getValue(rb),
//                        e.getMessage(), e, primaryStage);
//                }
//            }
//        });
        
        VBox vbox = VBoxBuilder.create()
            .spacing(5)
            .padding(new Insets(10, 10, 10, 10))
            .children(editButton, insertButton, removeButton)
            .build();
        
        VBox.setMargin(editButton, new Insets(0, 0, 5, 0));
        VBox.setMargin(insertButton, new Insets(0, 0, 5, 0));
        VBox.setMargin(removeButton, new Insets(0, 0, 5, 0));
//        VBox.setMargin(upButton, new Insets(0, 0, 5, 0));
//        VBox.setMargin(downButton, new Insets(0, 0, 5, 0));
        
        return vbox;
    }
    
    @SuppressWarnings("unchecked")
    private Node createCenterPane() {
        srtInfoData = FXCollections.observableArrayList();
        
        tableView = new TableView<>();
        tableView.setItems(srtInfoData);
        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                if (srtInfoData.size() == 0) {
                    return;
                }
                SRTWrapper sw = tableView.getSelectionModel().getSelectedItem();
                if (sw != null) {
                    editButton.setDisable(false);
                    insertButton.setDisable(false);
                    removeButton.setDisable(false);
                    
                    if (evt.getButton().equals(MouseButton.PRIMARY)){
                        if (evt.getClickCount() == 2) {
                            showEditDialog();
                        }
                    }
                }
            }
        });
        
        TableColumn<SRTWrapper, String> numberTableColumn = new TableColumn<>(
            ResourceBundleKeys.TABLECOLUMN_NUMBER.getValue(rb));
        numberTableColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        numberTableColumn.setCellValueFactory(
            new Callback<CellDataFeatures<SRTWrapper, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<SRTWrapper, String> p) {
                return new ReadOnlyStringWrapper(Integer.toString(p.getValue().srt.number));
            }
        });
        
        TableColumn<SRTWrapper, String> startTimeTableColumn = new TableColumn<>(
            ResourceBundleKeys.TABLECOLUMN_START_TIME.getValue(rb));
        startTimeTableColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        startTimeTableColumn.setCellValueFactory(
            new Callback<CellDataFeatures<SRTWrapper, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<SRTWrapper, String> p) {
                return new ReadOnlyStringWrapper(SRTTimeFormat.format(p.getValue().srt.startTime));
            }
        });
        
        TableColumn<SRTWrapper, String> endTimeTableColumn = new TableColumn<>(
            ResourceBundleKeys.TABLECOLUMN_END_TIME.getValue(rb));
        endTimeTableColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        endTimeTableColumn.setCellValueFactory(
            new Callback<CellDataFeatures<SRTWrapper, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<SRTWrapper, String> p) {
                return new ReadOnlyStringWrapper(SRTTimeFormat.format(p.getValue().srt.endTime));
            }
        });
        
        TableColumn<SRTWrapper, String> textTableColumn = new TableColumn<>(
            ResourceBundleKeys.TABLECOLUMN_TEXT.getValue(rb));
        textTableColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        textTableColumn.setCellValueFactory(
            new Callback<CellDataFeatures<SRTWrapper, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<SRTWrapper, String> p) {
                return new ReadOnlyStringWrapper(StringUtils.join(p.getValue().srt.text,
                    System.getProperty("line.separator")));
            }
        });
        
        tableView.getColumns().addAll(numberTableColumn, startTimeTableColumn,
            endTimeTableColumn, textTableColumn);
        
        VBox vbox = VBoxBuilder.create()
            .spacing(5)
            .padding(new Insets(10, 10, 10, 10))
            .children(tableView)
            .build();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        
        return vbox;
    }
    
    private abstract class BaseDialog extends Stage {
        protected ResourceBundle rb;
        protected SRT srt;
        // controls
        private ListSpinner<Integer> startTimeHourListSpinner;
        private ListSpinner<Integer> startTimeMinListSpinner;
        private ListSpinner<Integer> startTimeSecListSpinner;
        private ListSpinner<Integer> startTimeMilliSecListSpinner;
        private ListSpinner<Integer> endTimeHourListSpinner;
        private ListSpinner<Integer> endTimeMinListSpinner;
        private ListSpinner<Integer> endTimeSecListSpinner ;
        private ListSpinner<Integer> endTimeMilliSecListSpinner;
        private TextArea textTextArea;
        
        public BaseDialog(ResourceBundle rb, SRT srt) {
            this.rb = rb;
            this.srt = srt;
            
            setTitle(getDialogTitle());
            BorderPane root = new BorderPane();
            root.setCenter(createCenterPane());
            root.setBottom(createBottomPane());
            Scene scene = new Scene(root, 800, 400);
            setScene(scene);
        }
        
        protected abstract String getDialogTitle();
        
        protected abstract String getButtonText();
        
        protected abstract void execute(SRT newSRT);
        
        private ObservableList<Integer> getHourList() {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i <= 23; i++) {
                list.add(i);
            }
            return FXCollections.observableList(list);
        }
        
        private ObservableList<Integer> getMinList() {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i <= 60; i++) {
                list.add(i);
            }
            return FXCollections.observableList(list);
        }
        
        private ObservableList<Integer> getSecList() {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i <= 60; i++) {
                list.add(i);
            }
            return FXCollections.observableList(list);
        }
        
        private ObservableList<Integer> getMilliSecList() {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i <= 999; i++) {
                list.add(i);
            }
            return FXCollections.observableList(list);
        }
        
        private Node createCenterPane() {
            GridPane gridPane = GridPaneBuilder.create()
                .hgap(10)
                .vgap(10)
                .padding(new Insets(10, 10, 10, 10))
                .style("-fx-border-style: solid;"
                    + "-fx-border-width: 1;"
                    + "-fx-border-color: black")
                .build();
            
            // number
            Label numberLabel = new Label(ResourceBundleKeys.LABEL_NUMBER.getValue(rb));
            TextField numberTextField = new TextField(Integer.toString(srt.number));
            numberTextField.setEditable(false);
            gridPane.add(numberLabel, 0, 0);
            gridPane.add(numberTextField, 1, 0);
            
            // start time
            Label startTimeLabel = new Label(ResourceBundleKeys.LABEL_START_TIME.getValue(rb));
            SRTTime startTimeSRTTime = SRTTimeFormat.toSRTTime(srt.startTime);
            
            Label startTimeHourLabel = new Label(ResourceBundleKeys.LABEL_HOUR.getValue(rb));
            startTimeHourListSpinner = new ListSpinner<>(getHourList())
                .withValue(startTimeSRTTime.hour);
            
            Label startTimeMinLabel = new Label(ResourceBundleKeys.LABEL_MINUTE.getValue(rb));
            startTimeMinListSpinner = new ListSpinner<>(getMinList())
                .withValue(startTimeSRTTime.minute);
                
            Label startTimeSecLabel = new Label(ResourceBundleKeys.LABEL_SECOND.getValue(rb));
            startTimeSecListSpinner = new ListSpinner<>(getSecList())
                .withValue(startTimeSRTTime.second);
                
            Label startTimeilliSecLabel = new Label(ResourceBundleKeys.LABEL_MILLISECOND.getValue(rb));
            startTimeMilliSecListSpinner = new ListSpinner<>(getMilliSecList())
                .withValue(startTimeSRTTime.millisecond);
                
            HBox startTimeHBox = HBoxBuilder.create()
                .spacing(5)
                .padding(new Insets(10, 10, 10, 10))
                .children(
                    startTimeHourLabel, startTimeHourListSpinner,
                    startTimeMinLabel, startTimeMinListSpinner,
                    startTimeSecLabel, startTimeSecListSpinner,
                    startTimeilliSecLabel, startTimeMilliSecListSpinner)
                .build();
            gridPane.add(startTimeLabel, 0, 1);
            gridPane.add(startTimeHBox, 1, 1);
            
            // end time
            Label endTimeLabel = new Label(ResourceBundleKeys.LABEL_END_TIME.getValue(rb));
            SRTTime endSRTTime = SRTTimeFormat.toSRTTime(srt.endTime);
            
            Label endTimeHourLabel = new Label(ResourceBundleKeys.LABEL_HOUR.getValue(rb));
            endTimeHourListSpinner = new ListSpinner<>(getHourList())
                .withValue(endSRTTime.hour);
                
            Label endTimeMinLabel = new Label(ResourceBundleKeys.LABEL_MINUTE.getValue(rb));
            endTimeMinListSpinner = new ListSpinner<>(getMinList())
                .withValue(endSRTTime.minute);
            
            Label endTimeSecLabel = new Label(ResourceBundleKeys.LABEL_SECOND.getValue(rb));
            endTimeSecListSpinner = new ListSpinner<>(getSecList())
                .withValue(endSRTTime.second);
                
            Label endTImeMilliSecLabel = new Label(ResourceBundleKeys.LABEL_MILLISECOND.getValue(rb));
            endTimeMilliSecListSpinner = new ListSpinner<>(getMilliSecList())
                .withValue(endSRTTime.millisecond);
                
            HBox endTimeHBox = HBoxBuilder.create()
                .spacing(5)
                .padding(new Insets(10, 10, 10, 10))
                .children(
                    endTimeHourLabel, endTimeHourListSpinner,
                    endTimeMinLabel, endTimeMinListSpinner,
                    endTimeSecLabel, endTimeSecListSpinner,
                    endTImeMilliSecLabel, endTimeMilliSecListSpinner)
                .build();
            gridPane.add(endTimeLabel, 0, 2);
            gridPane.add(endTimeHBox, 1, 2);
            
            // text
            Label textLabel = new Label(ResourceBundleKeys.LABEL_TEXT.getValue(rb));
            textTextArea = new TextArea(StringUtils.join(srt.text,
                System.getProperty("line.separator")));
            gridPane.add(textLabel, 0, 3);
            gridPane.add(textTextArea, 1, 3);
            
            GridPane.setVgrow(textTextArea, Priority.ALWAYS);
            GridPane.setHgrow(textTextArea, Priority.ALWAYS);
            
            HBox hbox = HBoxBuilder.create()
                .spacing(5)
                .padding(new Insets(10, 10, 10, 10))
                .children(gridPane)
                .build();
            HBox.setHgrow(gridPane, Priority.ALWAYS);
            
            VBox vbox = VBoxBuilder.create()
                .spacing(5)
                .padding(new Insets(10, 10, 10, 10))
                .children(hbox)
                .build();
            VBox.setVgrow(hbox, Priority.ALWAYS);
            
            return vbox;
        }
        
        private Node createBottomPane() {
            Button btn = new Button(getButtonText());
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent evt) {
                    try {
                        SRTTime startSRTTime = new SRTTime(
                            startTimeHourListSpinner.getValue(),
                            startTimeMinListSpinner.getValue(),
                            startTimeSecListSpinner.getValue(),
                            startTimeMilliSecListSpinner.getValue());
                        SRTTime endSRTTime = new SRTTime(
                            endTimeHourListSpinner.getValue(),
                            endTimeMinListSpinner.getValue(),
                            endTimeSecListSpinner.getValue(),
                            endTimeMilliSecListSpinner.getValue());

                        SRT newSRT = new SRT(srt.number,
                            SRTTimeFormat.fromSRTTime(startSRTTime),
                            SRTTimeFormat.fromSRTTime(endSRTTime),
                            textTextArea.getText());
                        execute(newSRT);
                        
                        SRTWriter.write(srtFile, srtInfo);
                        srtInfoData.clear();
                        for (SRT s : srtInfo) {
                            srtInfoData.add(new SRTWrapper(s));
                        }
                        close();
                    } catch (Exception e) {
                        Dialog.showThrowable(
                            ResourceBundleKeys.DIALOG_ERROR_TITLE.getValue(rb),
                            e.getMessage(), e, BaseDialog.this);
                    }
                }
            });
            
            VBox vbox = VBoxBuilder.create()
                .spacing(5)
                .padding(new Insets(0, 20, 20, 20))
                .children(btn)
                .build();

            return vbox;
        }
    }
    
    private class EditDialog extends BaseDialog {
        public EditDialog(ResourceBundle rb, SRT srt) {
            super(rb, srt);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String getDialogTitle() {
            return ResourceBundleKeys.DIALOG_EDIT_TITLE.getValue(rb);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String getButtonText() {
            return ResourceBundleKeys.BUTTON_UPDATE.getValue(rb);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void execute(SRT newSRT) {
            SRTEditor.updateSubtitle(srtInfo, newSRT);
        }
    }
    
    private class InsertDialog extends BaseDialog {
        public InsertDialog(ResourceBundle rb, SRT srt) {
            super(rb, srt);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String getDialogTitle() {
            return ResourceBundleKeys.DIALOG_INSERT_TITLE.getValue(rb);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String getButtonText() {
            return ResourceBundleKeys.BUTTON_INSERT.getValue(rb);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void execute(SRT newSRT) {
            SRTEditor.insertSubtitle(srtInfo, newSRT);
        }
    }
    
    /*
     * This class is required because SRT's equals() and hashCode() only
     * compare the subtitle number, but the tableViewer has a weird thing
     * that calling srtInfoData.clear() and adding all those elements
     * again doesn't seem to update the table.
     */
    private static class SRTWrapper {
        public final SRT srt;
        
        public SRTWrapper(SRT srt) {
            this.srt = srt;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((srt == null) ? 0 : srt.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            SRTWrapper other = (SRTWrapper) obj;
            if (srt == null) {
                if (other.srt != null) {
                    return false;
                }
            } else if (srt.number != other.srt.number ||
                !srt.startTime.equals(other.srt.startTime) ||
                !srt.endTime.equals(other.srt.endTime) ||
                !srt.text.equals(other.srt.text)) {
                return false;
            }
            return true;
        }
    }
}
