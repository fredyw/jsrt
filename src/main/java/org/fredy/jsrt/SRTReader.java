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

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is responsible for reading an SRT file.
 * 
 * @author fredy
 */
public class SRTReader {
    public SRTReader() {
    }
    
    /**
     * Reads an SRT file and transforming it into SRT object.
     * 
     * @param srtFile SRT file
     * @return the SRTInfo object
     * @throws InvalidSRTException thrown when the SRT file is invalid
     * @throws SRTReaderException thrown while reading SRT file
     */
    public static SRTInfo read(File srtFile) throws InvalidSRTException, SRTReaderException {
        if (!srtFile.exists()) {
            throw new SRTReaderException(srtFile.getAbsolutePath() + " does not exist");
        }
        if (!srtFile.isFile()) {
            throw new SRTReaderException(srtFile.getAbsolutePath() + " is not a regular file");
        }
        
        SRTInfo srtInfo = new SRTInfo();
        try (BufferedReader br = new BufferedReader(new FileReader(srtFile))) {
            while (true) {
                srtInfo.add(parse(br));
            }
        } catch (EOFException e) {
            // Do nothing
        } catch (IOException e) {
            throw new SRTReaderException(e);
        }
        
        return srtInfo;
    }
    
    private static SRT parse(BufferedReader br) throws IOException, EOFException {
        String nString = br.readLine();
        if (nString == null) {
            throw new EOFException();
        }
        
        long subtitleNumber = -1;
        try {
            subtitleNumber = Long.parseLong(nString);
        } catch (NumberFormatException e) {
            throw new InvalidSRTException(
                nString + " has an invalid subtitle number");
        }
        
        String tString = br.readLine();
        if (tString == null) {
            throw new InvalidSRTException(
                "Start time and end time information is not present");
        }
        String[] times = tString.split(SRTTimeFormat.TIME_DELIMITER);
        if (times.length != 2) {
            throw new InvalidSRTException(
                tString + " needs to be seperated with " + SRTTimeFormat.TIME_DELIMITER);
        }
        Date startTime = null;
        try {
            startTime = SRTTimeFormat.parse(times[0]);
        } catch (ParseException e) {
            throw new InvalidSRTException(
                times[0] + " has an invalid time format");
        }
        
        Date endTime = null;
        try {
            endTime = SRTTimeFormat.parse(times[1]);
        } catch (ParseException e) {
            throw new InvalidSRTException(
                times[1] + " has an invalid time format");
        }
        
        List<String> subtitleLines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                break;
            }
            subtitleLines.add(line);
        }
        
        if (subtitleLines.size() == 0) {
            throw new InvalidSRTException("Missing subtitle text information");
        }
        
        return new SRT(subtitleNumber, startTime, endTime, subtitleLines);
    }
}
