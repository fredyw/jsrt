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
package org.fredy.jsrt.api;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class is responsible for writing an SRT file.
 * 
 * @author fredy
 */
public class SRTWriter {
    public SRTWriter() {
    }
    
    /**
     * Writes an SRT file from an SRT object.
     * 
     * @param srtFile the SRT file
     * @param srtInfo the SRTInfo object
     * @throws SRTWriterException thrown while writing an SRT file
     */
    public static void write(File srtFile, SRTInfo srtInfo) throws SRTWriterException {
        try (PrintWriter pw = new PrintWriter(srtFile)) {
            for (SRT srt : srtInfo) {
                pw.println(srt.number);
                pw.println(
                    SRTTimeFormat.format(srt.startTime) +
                    SRTTimeFormat.TIME_DELIMITER +
                    SRTTimeFormat.format(srt.endTime));
                for (String text : srt.text) {
                    pw.println(text);
                }
                // Add an empty line at the end
                pw.println();
            }
        } catch (IOException e) {
            throw new SRTWriterException(e);
        }
    }
}
