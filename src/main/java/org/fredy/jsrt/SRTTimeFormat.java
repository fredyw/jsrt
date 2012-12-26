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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains utility methods for SRT time format related stuff.
 * 
 * @author fredy
 */
public class SRTTimeFormat {
    public static final String TIME_DELIMITER = " --> ";
    public static final String TIME_FORMAT = "HH:mm:ss,SSS";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
    
    private SRTTimeFormat() {
    }
    
    /**
     * Formats the date into SRT time format.
     * @param date the date
     * @return the SRT time format
     */
    public static String format(Date date) {
        return sdf.format(date);
    }
    
    /**
     * Parses the SRT time format into date.
     * @param srtTime the SRT time format
     * @return the date
     * @throws ParseException
     */
    public static Date parse(String srtTime) throws ParseException {
        return sdf.parse(srtTime);
    }
}
