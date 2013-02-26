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

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.fredy.jsrt.api.SRT;
import org.fredy.jsrt.api.SRTInfo;
import org.fredy.jsrt.api.SRTTimeFormat;
import org.fredy.jsrt.util.StringUtils;

/**
 * A GUI client code.
 * 
 * @author fredy
 */
public class JSRT extends Application {
    private ResourceBundle rb;
    private SRTInfo srtInfo = new SRTInfo();
    // all the controls
    private Button openButton;
    private Button addButton;
    private Button removeButton;
    private Button upButton;
    private Button downButton;
    private Button checkForUpdateButton;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
        
        HBox hbox = HBoxBuilder.create()
            .spacing(5)
            .padding(new Insets(10, 10, 10, 10))
            .children(createdByLabel, checkForUpdateButton)
            .build();
        
        return hbox;
    }
    
    private Node createTopPane() {
        Label subtitleLabel = LabelBuilder.create()
            .text(ResourceBundleKeys.LABEL_SUBTITLE.getValue(rb))
            .build();
        
        openButton = ButtonBuilder.create()
            .text(ResourceBundleKeys.BUTTON_OPEN.getValue(rb))
            .build();
        
        HBox hbox = HBoxBuilder.create()
            .spacing(5)
            .padding(new Insets(10, 10, 10, 10))
            .children(subtitleLabel, openButton)
            .build();
        
        return hbox;
    }
    
    private Node createRightPane() {
        addButton = ButtonBuilder.create()
            .maxWidth(Double.MAX_VALUE)
            .text(ResourceBundleKeys.BUTTON_INSERT.getValue(rb))
            .build();
        
        removeButton = ButtonBuilder.create()
            .maxWidth(Double.MAX_VALUE)
            .text(ResourceBundleKeys.BUTTON_REMOVE.getValue(rb))
            .build();
        
        upButton = ButtonBuilder.create()
            .maxWidth(Double.MAX_VALUE)
            .text(ResourceBundleKeys.BUTTON_MOVE_UP.getValue(rb))
            .build();
        
        downButton = ButtonBuilder.create()
            .maxWidth(Double.MAX_VALUE)
            .text(ResourceBundleKeys.BUTTON_MOVE_DOWN.getValue(rb))
            .build();
        
        VBox vbox = VBoxBuilder.create()
            .spacing(5)
            .padding(new Insets(10, 10, 10, 10))
            .children(addButton, removeButton, upButton, downButton)
            .build();
        
        VBox.setMargin(addButton, new Insets(0, 0, 5, 0));
        VBox.setMargin(removeButton, new Insets(0, 0, 5, 0));
        VBox.setMargin(upButton, new Insets(0, 0, 5, 0));
        VBox.setMargin(downButton, new Insets(0, 0, 5, 0));
        
        return vbox;
    }
    
    @SuppressWarnings("unchecked")
    private Node createCenterPane() {
        ObservableList<SRT> data = FXCollections.observableArrayList();
        for (SRT srt : srtInfo) {
            data.add(srt);
        }
        
        TableView<SRT> tableView = new TableView<>();
        tableView.setItems(data);
        TableColumn<SRT, String> numberTableColumn = new TableColumn<>(
            ResourceBundleKeys.TABLECOLUMN_NUMBER.getValue(rb));
        numberTableColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        numberTableColumn.setCellValueFactory(
            new Callback<CellDataFeatures<SRT, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<SRT, String> p) {
                return new ReadOnlyStringWrapper(Integer.toString(p.getValue().number));
            }
        });
        
        TableColumn<SRT, String> startTimeTableColumn = new TableColumn<>(
            ResourceBundleKeys.TABLECOLUMN_START_TIME.getValue(rb));
        startTimeTableColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        startTimeTableColumn.setCellValueFactory(
            new Callback<CellDataFeatures<SRT, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<SRT, String> p) {
                return new ReadOnlyStringWrapper(SRTTimeFormat.format(p.getValue().startTime));
            }
        });
        
        TableColumn<SRT, String> endTimeTableColumn = new TableColumn<>(
            ResourceBundleKeys.TABLECOLUMN_END_TIME.getValue(rb));
        endTimeTableColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        endTimeTableColumn.setCellValueFactory(
            new Callback<CellDataFeatures<SRT, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<SRT, String> p) {
                return new ReadOnlyStringWrapper(SRTTimeFormat.format(p.getValue().endTime));
            }
        });
        
        TableColumn<SRT, String> textTableColumn = new TableColumn<>(
            ResourceBundleKeys.TABLECOLUMN_TEXT.getValue(rb));
        textTableColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));
        textTableColumn.setCellValueFactory(
            new Callback<CellDataFeatures<SRT, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<SRT, String> p) {
                return new ReadOnlyStringWrapper(StringUtils.join(p.getValue().text,
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
}
