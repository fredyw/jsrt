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

import java.util.ResourceBundle;

/**
 * This enum contains list of keys in the jsrt.properties.
 * 
 * @author fredy
 */
public enum ResourceBundleKeys {
    VERSION("version"),
    TITLE("title"),
    
    LABEL_SUBTITLE("label.subtitle"),
    LABEL_CREATED_BY("label.createdby"),
    LABEL_NUMBER("label.number"),
    LABEL_START_TIME("label.starttime"),
    LABEL_END_TIME("label.endtime"),
    LABEL_TEXT("label.text"),
    LABEL_HOUR("label.hour"),
    LABEL_MINUTE("label.minute"),
    LABEL_SECOND("label.second"),
    LABEL_MILLISECOND("label.millisecond"),
    LABEL_UPDATE_ALL_TIMES("label.updatealltimes"),
    
    BUTTON_OPEN("button.open"),
    BUTTON_EDIT("button.edit"),
    BUTTON_INSERT("button.insert"),
    BUTTON_REMOVE("button.remove"),
    BUTTON_MOVE_UP("button.moveup"),
    BUTTON_MOVE_DOWN("button.movedown"),
    BUTTON_CHECK_FOR_UPDATE("button.checkforupdate"),
    BUTTON_UPDATE("button.update"),
    BUTTON_UPDATE_ALL("button.updateall"),
    
    TABLECOLUMN_NUMBER("tablecolumn.number"),
    TABLECOLUMN_START_TIME("tablecolumn.starttime"),
    TABLECOLUMN_END_TIME("tablecolumn.endtime"),
    TABLECOLUMN_TEXT("tablecolumn.text"),
    
    DIALOG_ERROR_TITLE("dialog.error.title"),
    DIALOG_INSERT_TITLE("dialog.insert.title"),
    DIALOG_EDIT_TITLE("dialog.edit.title"),
    DIALOG_CHECK_FOR_UPDATE_TITLE("dialog.checkforupdate.title"),
    
    DIALOG_CHECK_FOR_UPDATE_MESSAGE("dialog.checkforupdate.message"),
    DIALOG_LATEST_VERSION_MESSAGE("dialog.latestversion.message"),
    DIALOG_NEW_VERSION_MESSAGE("dialog.newversion.message"),
    
    FILECHOOSEREXT_FILTER("filechooserext.filter");
    
    private String key;

    private ResourceBundleKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the property value of a given resource bundle key.
     * 
     * @param rb the ResourceBundle
     * @param args the args
     * @return the property value
     */
    public String getValue(ResourceBundle rb, Object... args) {
        if (args.length != 0) {
            return String.format(rb.getString(this.toString()), args);
        }
        return rb.getString(this.toString());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return key;
    }
}
