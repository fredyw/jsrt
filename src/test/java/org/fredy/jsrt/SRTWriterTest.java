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
package org.fredy.jsrt;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.Test;

/**
 * @author fredy
 */
public class SRTWriterTest {
    @AfterClass
    public static void cleanUp() {
        new File("src/test/resources/test1.srt").delete();
    }
    
    @Test
    public void testWrite() throws Exception {
        SRTInfo inInfo = new SRTInfo();
        inInfo.add(new SRT(2, SRTTimeFormat.parse("00:00:24,600"),
            SRTTimeFormat.parse("00:00:27,800"), "Foo Bar", "Bar Foo"));
        inInfo.add(new SRT(1, SRTTimeFormat.parse("00:00:20,000"),
            SRTTimeFormat.parse("00:00:24,400"), "Hello World", "Bye World"));
            
        File srtFile = new File("src/test/resources/test1.srt");
        SRTWriter.write(srtFile, inInfo);
        
        SRTInfo outInfo = SRTReader.read(srtFile);
        assertEquals(inInfo.size(), outInfo.size());
        Iterator<SRT> inIter = inInfo.iterator();
        Iterator<SRT> outIter = outInfo.iterator();
        
        SRT inSRT = inIter.next();
        SRT outSRT = outIter.next();
        assertEquals(inSRT.number, outSRT.number);
        assertEquals(inSRT.startTime, outSRT.startTime);
        assertEquals(inSRT.endTime, outSRT.endTime);
        assertEquals(inSRT.subtitles.get(0), outSRT.subtitles.get(0));
        assertEquals(inSRT.subtitles.get(1), outSRT.subtitles.get(1));
        
        inSRT = inIter.next();
        outSRT = outIter.next();
        assertEquals(inSRT.number, outSRT.number);
        assertEquals(inSRT.startTime, outSRT.startTime);
        assertEquals(inSRT.endTime, outSRT.endTime);
        assertEquals(inSRT.subtitles.get(0), outSRT.subtitles.get(0));
        assertEquals(inSRT.subtitles.get(1), outSRT.subtitles.get(1));
    }
}
