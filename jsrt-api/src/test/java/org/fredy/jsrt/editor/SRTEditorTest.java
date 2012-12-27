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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import org.fredy.jsrt.api.SRT;
import org.fredy.jsrt.api.SRTInfo;
import org.fredy.jsrt.api.SRTTimeFormat;
import org.fredy.jsrt.util.StringUtils;
import org.junit.Test;

/**
 * @author fredy
 */
public class SRTEditorTest {

    @Test
    public void testSetTime() throws Exception {
        SRT oldSRT = new SRT(1, SRTTimeFormat.parse("00:00:24,600"),
            SRTTimeFormat.parse("00:00:26,600"), "Hello World");
        
        SRT newSRT = SRTEditor.setTime(oldSRT, SRTTimeFormat.Type.HOUR, 13);
        assertEquals(oldSRT.number, newSRT.number);
        assertEquals("13:00:24,600", SRTTimeFormat.format(newSRT.startTime));
        assertEquals("13:00:26,600", SRTTimeFormat.format(newSRT.endTime));
        assertEquals(oldSRT.text.get(0), newSRT.text.get(0));
        
        newSRT = SRTEditor.setTime(oldSRT, SRTTimeFormat.Type.MINUTE, 2);
        assertEquals(oldSRT.number, newSRT.number);
        assertEquals("00:02:24,600", SRTTimeFormat.format(newSRT.startTime));
        assertEquals("00:02:26,600", SRTTimeFormat.format(newSRT.endTime));
        assertEquals(oldSRT.text.get(0), newSRT.text.get(0));
        
        newSRT = SRTEditor.setTime(oldSRT, SRTTimeFormat.Type.SECOND, 2);
        assertEquals(oldSRT.number, newSRT.number);
        assertEquals("00:00:26,600", SRTTimeFormat.format(newSRT.startTime));
        assertEquals("00:00:28,600", SRTTimeFormat.format(newSRT.endTime));
        assertEquals(oldSRT.text.get(0), newSRT.text.get(0));
        
        newSRT = SRTEditor.setTime(oldSRT, SRTTimeFormat.Type.MILLISECOND, -200);
        assertEquals(oldSRT.number, newSRT.number);
        assertEquals("00:00:24,400", SRTTimeFormat.format(newSRT.startTime));
        assertEquals("00:00:26,400", SRTTimeFormat.format(newSRT.endTime));
        assertEquals(oldSRT.text.get(0), newSRT.text.get(0));
    }
    
    @Test
    public void testUpdateTime() throws Exception {
        SRTInfo srtInfo = new SRTInfo();
        srtInfo.add(new SRT(1, SRTTimeFormat.parse("00:00:24,600"),
            SRTTimeFormat.parse("00:00:26,600"), "Foo", "Bar"));
        srtInfo.add(new SRT(2, SRTTimeFormat.parse("00:11:24,600"),
            SRTTimeFormat.parse("00:12:26,600"), "Bye", "World"));
        
        SRTEditor.updateTime(srtInfo, 2, SRTTimeFormat.Type.MINUTE, -2);
        SRT srt = srtInfo.get(2);
        assertEquals(2L, srt.number);
        assertEquals("00:09:24,600", SRTTimeFormat.format(srt.startTime));
        assertEquals("00:10:26,600", SRTTimeFormat.format(srt.endTime));
        assertEquals("Bye", srt.text.get(0));
        assertEquals("World", srt.text.get(1));
    }
    
    @Test(expected = SRTEditorException.class)
    public void testUpdateTimeInvalidSubtitleNumber() throws Exception {
        SRTInfo srtInfo = new SRTInfo();
        srtInfo.add(new SRT(1, SRTTimeFormat.parse("00:00:24,600"),
            SRTTimeFormat.parse("00:00:26,600"), "Foo", "Bar"));
        srtInfo.add(new SRT(2, SRTTimeFormat.parse("00:11:24,600"),
            SRTTimeFormat.parse("00:12:26,600"), "Bye", "World"));
        
        SRTEditor.updateTime(srtInfo, 100, SRTTimeFormat.Type.MINUTE, -2);
    }
    
    @Test
    public void testUpdateTimes() throws Exception {
        SRTInfo srtInfo = new SRTInfo();
        srtInfo.add(new SRT(1, SRTTimeFormat.parse("00:00:24,600"),
            SRTTimeFormat.parse("00:00:26,600"), "Foo", "Bar"));
        srtInfo.add(new SRT(2, SRTTimeFormat.parse("00:11:24,600"),
            SRTTimeFormat.parse("00:12:26,600"), "Bye", "World"));
        
        SRTEditor.updateTimes(srtInfo, SRTTimeFormat.Type.MINUTE, -2);
        
        SRT srt = srtInfo.get(1);
        assertEquals(1L, srt.number);
        assertEquals("23:58:24,600", SRTTimeFormat.format(srt.startTime));
        assertEquals("23:58:26,600", SRTTimeFormat.format(srt.endTime));
        assertEquals("Foo", srt.text.get(0));
        assertEquals("Bar", srt.text.get(1));
        
        srt = srtInfo.get(2);
        assertEquals(2L, srt.number);
        assertEquals("00:09:24,600", SRTTimeFormat.format(srt.startTime));
        assertEquals("00:10:26,600", SRTTimeFormat.format(srt.endTime));
        assertEquals("Bye", srt.text.get(0));
        assertEquals("World", srt.text.get(1));
    }
    
    @Test
    public void testBreakText() {
        Date d = new Date();
        SRT oldSRT = new SRT(1, d, d, "0123456789 0123456789 0123456789 0123456789");
        SRT newSRT = SRTEditor.breakText(oldSRT, 21);
        assertEquals(oldSRT.number, newSRT.number);
        assertEquals(SRTTimeFormat.format(oldSRT.startTime), SRTTimeFormat.format(newSRT.startTime));
        assertEquals(SRTTimeFormat.format(oldSRT.startTime), SRTTimeFormat.format(newSRT.startTime));
        assertEquals(2, newSRT.text.size());
        assertEquals("0123456789 0123456789", newSRT.text.get(0));
        assertEquals("0123456789 0123456789", newSRT.text.get(1));
        
        oldSRT = new SRT(1, d, d, "0123456789 0123456789 0123456789 0123456789 0123456789");
        newSRT = SRTEditor.breakText(oldSRT, 21);
        assertEquals(oldSRT.number, newSRT.number);
        assertEquals(SRTTimeFormat.format(oldSRT.startTime), SRTTimeFormat.format(newSRT.startTime));
        assertEquals(SRTTimeFormat.format(oldSRT.startTime), SRTTimeFormat.format(newSRT.startTime));
        assertEquals(3, newSRT.text.size());
        assertEquals("0123456789 0123456789", newSRT.text.get(0));
        assertEquals("0123456789 0123456789", newSRT.text.get(1));
        assertEquals("0123456789", newSRT.text.get(2));
        
        oldSRT = new SRT(1, d, d, "0123456789");
        newSRT = SRTEditor.breakText(oldSRT, 5);
        assertEquals(oldSRT.number, newSRT.number);
        assertEquals(SRTTimeFormat.format(oldSRT.startTime), SRTTimeFormat.format(newSRT.startTime));
        assertEquals(SRTTimeFormat.format(oldSRT.startTime), SRTTimeFormat.format(newSRT.startTime));
        assertEquals(1, newSRT.text.size());
        assertEquals("0123456789", newSRT.text.get(0));
    }
    
    @Test
    public void testUpdateText() throws Exception {
        SRTInfo srtInfo = new SRTInfo();
        srtInfo.add(new SRT(1, SRTTimeFormat.parse("00:00:24,600"),
            SRTTimeFormat.parse("00:00:26,600"),
            "Hello!!! This is a very long string.", "Ain't it cool??? :)"));
        srtInfo.add(new SRT(2, SRTTimeFormat.parse("00:11:24,600"),
            SRTTimeFormat.parse("00:12:26,600"),
            "Hello!!! There is really nothing to see here.", "Foo Bar.", "Bye World."));
        
        SRTEditor.updateText(srtInfo, 2, 15);
        
        SRT srt = srtInfo.get(2);
        assertEquals(2L, srt.number);
        assertEquals("00:11:24,600", SRTTimeFormat.format(srt.startTime));
        assertEquals("00:12:26,600", SRTTimeFormat.format(srt.endTime));
        assertEquals(5, srt.text.size());
        assertEquals("Hello!!! There", srt.text.get(0));
        assertEquals("is really", srt.text.get(1));
        assertEquals("nothing to see", srt.text.get(2));
        assertEquals("here. Foo Bar.", srt.text.get(3));
        assertEquals("Bye World.", srt.text.get(4));
    }
    
    @Test(expected = SRTEditorException.class)
    public void testUpdateTextInvalidSubtitleNumber() throws Exception {
        SRTInfo srtInfo = new SRTInfo();
        srtInfo.add(new SRT(1, SRTTimeFormat.parse("00:00:24,600"),
            SRTTimeFormat.parse("00:00:26,600"),
            "Hello!!! This is a very long string.", "Ain't it cool??? :)"));
        srtInfo.add(new SRT(2, SRTTimeFormat.parse("00:11:24,600"),
            SRTTimeFormat.parse("00:12:26,600"),
            "Hello!!! There is really nothing to see here.", "Foo Bar.", "Bye World."));
        
        SRTEditor.updateText(srtInfo, 100, 15);
    }
    
    @Test
    public void testUpdateTexts() throws Exception {
        SRTInfo srtInfo = new SRTInfo();
        srtInfo.add(new SRT(1, SRTTimeFormat.parse("00:00:24,600"),
            SRTTimeFormat.parse("00:00:26,600"),
            "Hello!!! This is a very long string.", "Ain't it cool??? :)"));
        srtInfo.add(new SRT(2, SRTTimeFormat.parse("00:11:24,600"),
            SRTTimeFormat.parse("00:12:26,600"),
            "Hello!!! There is really nothing to see here.", "Foo Bar.", "Bye World."));
        
        SRTEditor.updateTexts(srtInfo, 15);
        
        SRT srt = srtInfo.get(1);
        assertEquals(1L, srt.number);
        assertEquals("00:00:24,600", SRTTimeFormat.format(srt.startTime));
        assertEquals("00:00:26,600", SRTTimeFormat.format(srt.endTime));
        assertEquals(4, srt.text.size());
        assertEquals("Hello!!! This", srt.text.get(0));
        assertEquals("is a very long", srt.text.get(1));
        assertEquals("string. Ain't", srt.text.get(2));
        assertEquals("it cool??? :)", srt.text.get(3));
        
        srt = srtInfo.get(2);
        assertEquals(2L, srt.number);
        assertEquals("00:11:24,600", SRTTimeFormat.format(srt.startTime));
        assertEquals("00:12:26,600", SRTTimeFormat.format(srt.endTime));
        assertEquals(5, srt.text.size());
        assertEquals("Hello!!! There", srt.text.get(0));
        assertEquals("is really", srt.text.get(1));
        assertEquals("nothing to see", srt.text.get(2));
        assertEquals("here. Foo Bar.", srt.text.get(3));
        assertEquals("Bye World.", srt.text.get(4));
    }
    
    @Test
    public void testAppendSubtitle() {
        SRTInfo srtInfo = new SRTInfo();
        SRTEditor.appendSubtitle(srtInfo, "00:00:24,600", "00:00:26,600", Arrays.asList("Foo"));
        SRTEditor.appendSubtitle(srtInfo, "00:11:24,600", "00:12:26,600", Arrays.asList("Bar"));
        
        SRT srt = srtInfo.get(1);
        assertEquals(1L, srt.number);
        assertEquals("00:00:24,600", SRTTimeFormat.format(srt.startTime));
        assertEquals("00:00:26,600", SRTTimeFormat.format(srt.endTime));
        assertEquals(1, srt.text.size());
        assertEquals("Foo", srt.text.get(0));
        
        srt = srtInfo.get(2);
        assertEquals(2L, srt.number);
        assertEquals("00:11:24,600", SRTTimeFormat.format(srt.startTime));
        assertEquals("00:12:26,600", SRTTimeFormat.format(srt.endTime));
        assertEquals(1, srt.text.size());
    }
    
    @Test
    public void testRemoveSubtitle() throws Exception {
        SRTInfo srtInfo = new SRTInfo();
        for (int i = 1; i <= 5; i++) {
            srtInfo.add(new SRT(i, SRTTimeFormat.parse("00:00:24,600"),
                SRTTimeFormat.parse("00:00:26,600"), "Foo" + i));
        }
        SRTEditor.removeSubtitle(srtInfo, 3);
        
        assertEquals(4, srtInfo.size());
        SRT srt = srtInfo.get(1);
        assertEquals("Foo1", StringUtils.join(srt.text, ""));
        
        srt = srtInfo.get(2);
        assertEquals("Foo2", StringUtils.join(srt.text, ""));
        
        srt = srtInfo.get(3);
        assertEquals("Foo4", StringUtils.join(srt.text, ""));
        
        srt = srtInfo.get(4);
        assertEquals("Foo5", StringUtils.join(srt.text, ""));
    }
    
    @Test(expected = SRTEditorException.class)
    public void testRemoveSubtitleInvalidSubtitleNumber() throws Exception {
        SRTInfo srtInfo = new SRTInfo();
        for (int i = 1; i <= 5; i++) {
            srtInfo.add(new SRT(i, SRTTimeFormat.parse("00:00:24,600"),
                SRTTimeFormat.parse("00:00:26,600"), "Foo" + i));
        }
        SRTEditor.removeSubtitle(srtInfo, 100);
    }
    
    @Test
    public void testInsertSubtitle() throws Exception {
        SRTInfo srtInfo = new SRTInfo();
        for (int i = 1; i <= 4; i++) {
            srtInfo.add(new SRT(i, SRTTimeFormat.parse("00:00:24,600"),
                SRTTimeFormat.parse("00:00:26,600"), "Foo" + i));
        }
        SRTEditor.insertSubtitle(srtInfo, 3, "00:00:24,600", "00:00:26,600",
            Arrays.asList("Foo100"));
        
        assertEquals(5, srtInfo.size());
        SRT srt = srtInfo.get(1);
        assertEquals("Foo1", StringUtils.join(srt.text, ""));
        
        srt = srtInfo.get(2);
        assertEquals("Foo2", StringUtils.join(srt.text, ""));
        
        srt = srtInfo.get(3);
        assertEquals("Foo100", StringUtils.join(srt.text, ""));
        
        srt = srtInfo.get(4);
        assertEquals("Foo3", StringUtils.join(srt.text, ""));
        
        srt = srtInfo.get(5);
        assertEquals("Foo4", StringUtils.join(srt.text, ""));
    }
    
    @Test(expected = SRTEditorException.class)
    public void testInsertSubtitleInvalidSubtitleNumber() throws Exception {
        SRTInfo srtInfo = new SRTInfo();
        for (int i = 1; i <= 4; i++) {
            srtInfo.add(new SRT(i, SRTTimeFormat.parse("00:00:24,600"),
                SRTTimeFormat.parse("00:00:26,600"), "Foo" + i));
        }
        SRTEditor.insertSubtitle(srtInfo, 100, "00:00:24,600", "00:00:26,600",
            Arrays.asList("Foo100"));
    }
    
    @Test
    public void testPrepend() throws Exception {
        SRTInfo srtInfo = new SRTInfo();
        for (int i = 1; i <= 3; i++) {
            srtInfo.add(new SRT(i, SRTTimeFormat.parse("00:00:24,600"),
                SRTTimeFormat.parse("00:00:26,600"), "Foo" + i));
        }
        SRTEditor.prependSubtitle(srtInfo, "00:00:24,600", "00:00:26,600",
            Arrays.asList("Foo100"));
        
        assertEquals(4, srtInfo.size());
        SRT srt = srtInfo.get(1);
        assertEquals("Foo100", StringUtils.join(srt.text, ""));
        
        srt = srtInfo.get(2);
        assertEquals("Foo1", StringUtils.join(srt.text, ""));
        
        srt = srtInfo.get(3);
        assertEquals("Foo2", StringUtils.join(srt.text, ""));
        
        srt = srtInfo.get(4);
        assertEquals("Foo3", StringUtils.join(srt.text, ""));
    }
}
