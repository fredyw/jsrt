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
package org.fredy.jsrt.util;

import static org.junit.Assert.*;

import org.fredy.jsrt.api.VersionException;
import org.junit.Test;

/**
 * @author fredy
 */
public class VersionUtilsTest {

    @Test
    public void testCompare() {
        // major versions
        assertEquals(0, VersionUtils.compare("0.1.0", "0.1.0"));
        assertEquals(-1, VersionUtils.compare("1.0.0", "2.0.0"));
        assertEquals(1, VersionUtils.compare("2.0.0", "1.0.0"));
        // minor versions
        assertEquals(-1, VersionUtils.compare("1.0.0", "1.1.0"));
        assertEquals(1, VersionUtils.compare("1.1.0", "1.0.0"));
        // subminor versions
        assertEquals(-1, VersionUtils.compare("1.0.0", "1.0.1"));
        assertEquals(1, VersionUtils.compare("1.0.1", "1.0.0"));
    }
    
    @Test(expected = VersionException.class)
    public void testCompareInvalidVersion() {
        VersionUtils.compare("0.1", "0.2");
    }
    
    @org.junit.Ignore
    @Test
    public void testGetLatestVersion() {
        System.out.println(VersionUtils.getLatestVersion());
    }
}
