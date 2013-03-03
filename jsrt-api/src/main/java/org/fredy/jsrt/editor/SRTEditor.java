/* 
 * Copyright 2012 Fredy Wijaya
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
package org.fredy.jsrt.editor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.fredy.jsrt.api.SRT;
import org.fredy.jsrt.api.SRTInfo;
import org.fredy.jsrt.api.SRTTimeFormat;
import org.fredy.jsrt.util.StringUtils;

/**
 * This class performs high-level operations related to editing SRT.
 * 
 * @author fredy
 */
public class SRTEditor {
    private SRTEditor() {
    }
    
    /**
     * Updates time of the SRT object in the SRTInfo object. This method will
     * update both start time and end time.
     * 
     * @param info the SRTInfo object
     * @param subtitleNumber the subtitle number
     * @param type the subtitle time format type
     * @param value the time value
     */
    public static void updateTime(SRTInfo info, int subtitleNumber,
        SRTTimeFormat.Type type, int value) {
        if (!info.contains(subtitleNumber)) {
            throw new SRTEditorException(subtitleNumber + " could not be found");
        }
        info.add(setTime(info.get(subtitleNumber), type, value));
    }
    
    /**
     * Updates all times of the SRT objects in the SRTInfo object. This method will
     * update both start time and end time.
     * 
     * @param info the SRTInfo object
     * @param type the SRT time format object
     * @param value the time value
     */
    public static void updateTimes(SRTInfo info, SRTTimeFormat.Type type, int value) {
        for (int i = 1; i <= info.size(); i++) {
            updateTime(info, info.get(i).number, type, value);
        }
    }
    
    /**
     * Creates a new SRT.
     * 
     * @param srt the SRT object
     * @param type the SRTTimeFormat
     * @param value the time value
     * @return the new SRT object
     */
    static SRT setTime(SRT srt, SRTTimeFormat.Type type, int value) {
        return new SRT(srt.number, newDate(srt.startTime, type, value),
            newDate(srt.endTime, type, value), srt.text);
    }
    
    private static Date newDate(Date oldDate, SRTTimeFormat.Type type, int value) {
        Calendar newCal = Calendar.getInstance();
        newCal.setTime(oldDate);
        switch (type) {
        case HOUR:
            newCal.add(Calendar.HOUR_OF_DAY, value);
            break;
        case MINUTE:
            newCal.add(Calendar.MINUTE, value);
            break;
        case SECOND:
            newCal.add(Calendar.SECOND, value);
            break;
        case MILLISECOND:
            newCal.add(Calendar.MILLISECOND, value);
            break;
        }
        return newCal.getTime();
    }
    
    /**
     * Updates a subtitle text according to the width given.
     * 
     * @param info the SRTInfo object
     * @param subtitleNumber the subtitle number
     * @param width the width (number of characters per subtitle line)
     */
    public static void updateText(SRTInfo info, int subtitleNumber, int width) {
        if (!info.contains(subtitleNumber)) {
            throw new SRTEditorException(subtitleNumber + " could not be found");
        }
        info.add(breakText(info.get(subtitleNumber), width));
    }
    
    /**
     * Updates all subtitle texts according to the width given.
     * 
     * @param info the SRTInfo object
     * @param width the width (number of characters per subtitle line)
     */
    public static void updateTexts(SRTInfo info, int width) {
        for (int i = 1; i <= info.size(); i++) {
            updateText(info, info.get(i).number, width);
        }
    }
    
    /**
     * Breaks the subtitle according to the width.
     * 
     * @param srt the SRT object
     * @param width the width (number of characters per subtitle line)
     * @return the new SRT
     */
    static SRT breakText(SRT srt, int width) {
        List<String> newTexts = new ArrayList<>();
        String subtitle = StringUtils.join(srt.text, " ");
        if (subtitle.length() <= width) {
            newTexts.addAll(srt.text);
        } else {
            int begin = 0;
            int end = width;
            while (end < subtitle.length()) {
                while (subtitle.charAt(end) != ' ' && end != 0) {
                    end--;
                }
                if (end == 0) {
                    break;
                }
                newTexts.add(subtitle.substring(begin, end));
                begin = end + 1;
                end = begin + width;
            }
            newTexts.add(subtitle.substring(begin, subtitle.length()));
        }
        return new SRT(srt.number, srt.startTime, srt.endTime, newTexts);
    }
    
    /**
     * Appends a new subtitle into SRTInfo.
     * 
     * @param info the SRTInfo object
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */
    public static void appendSubtitle(SRTInfo info, String startTime,
        String endTime, List<String> text) {
        try {
            SRT newSRT = new SRT(
                info.size() + 1,
                SRTTimeFormat.parse(startTime),
                SRTTimeFormat.parse(endTime),
                text);
            info.add(newSRT);
        } catch (ParseException e) {
            throw new SRTEditorException(e);
        }
    }
    
    /**
     * Prepends the subtitle into SRTInfo object.
     * 
     * This operation is very expensive since it needs to update all the subtitle
     * numbers.
     * 
     * @param info the SRTInfo object
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */
    public static void prependSubtitle(SRTInfo info, String startTime,
        String endTime, List<String> text) {
        insertSubtitle(info, 1, startTime, endTime, text);
    }
    
    /**
     * Inserts the subtitle into SRTInfo object. All the subsequent subtitle
     * numbers after the new subtitle that is going to be inserted will be
     * updated.
     * 
     * @param info the SRTInfo object
     * @param subtitleNumber the subtitle number
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */
    public static void insertSubtitle(SRTInfo info, int subtitleNumber,
        String startTime, String endTime, List<String> text) {
        if (!info.contains(subtitleNumber)) {
            throw new SRTEditorException(subtitleNumber + " could not be found");
        }
        
        for (int i = info.size(); i >= subtitleNumber; i--) {
            SRT tmp = info.get(i);
            info.add(new SRT(tmp.number+1, tmp.startTime, tmp.endTime, tmp.text));
        }
        
        try {
            info.add(new SRT(subtitleNumber, SRTTimeFormat.parse(startTime),
                SRTTimeFormat.parse(endTime), text));
        } catch (ParseException e) {
            throw new SRTEditorException(e);
        }
    }
    
    /**
     * Removes the subtitle from SRTInfo object. This method will update
     * all the subsequent subtitle numbers after the subtitle number that is
     * going to be removed.
     * 
     * @param info the SRTInfo object
     * @param subtitleNumber the subtitle number to be removed
     */
    public static void removeSubtitle(SRTInfo info, int subtitleNumber) {
        if (!info.contains(subtitleNumber)) {
            throw new SRTEditorException(subtitleNumber + " could not be found");
        }
        
        int originalSize = info.size();
        for (int i = subtitleNumber+1; i  <= originalSize; i++) {
            SRT tmp = info.get(i);
            info.add(new SRT(tmp.number-1, tmp.startTime, tmp.endTime, tmp.text));
        }
        // Remove the last element
        info.remove(info.size());
    }
    
    /**
     * Updates the subtitle from the SRTInfo object.
     * 
     * @param info the SRTInfo object
     * @param srt the SRT object
     */
    public static void updateSubtitle(SRTInfo info, SRT srt) {
        // TODO: to be implemented
    }
}
