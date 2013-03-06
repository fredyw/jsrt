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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.fredy.jsrt.api.VersionException;

/**
 * A utility class for version related stuff.
 * 
 * @author fredy
 */
public class VersionUtils {
    private static final String UPDATE_URL = "https://raw.github.com/fredyw/jsrt/master/VERSION.txt";
    
    private VersionUtils() {
    }
    
    /**
     * Gets the ID3Tidy latest version.
     * 
     * @return the ID3Tidy latest version
     */
    public static String getLatestVersion() {
        BufferedReader br = null;
        try {
            URL url = new URL(UPDATE_URL);
            URLConnection conn = url.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            return br.readLine();
        } catch (MalformedURLException e) {
            throw new VersionException(e);
        } catch (IOException e) {
            throw new VersionException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new VersionException(e);
                }
            }
        }
    }
    
    /**
     * Compares the two versions.
     * 
     * @param version1 the version 1
     * @param version2 the version 2
     * @return 0 if both versions are the same
     *         -1 if version1 is less than version2
     *         1 if version1 is greater than version2
     */
    public static int compare(String version1, String version2) {
        String[] ver1 = version1.split("\\.");
        String[] ver2 = version2.split("\\.");
        if (ver1.length != 3 && ver2.length != 3) {
            throw new VersionException(
                "Version must be in the format of <major>.<minor>.<subminor>");
        }
        // compare the major versions
        int majorVer1 = Integer.parseInt(ver1[0]);
        int majorVer2 = Integer.parseInt(ver2[0]);
        if (majorVer1 < majorVer2) {
            return -1;
        } else if (majorVer1 > majorVer2) {
            return 1;
        } else {
            // compare the minor versions
            int minorVer1 = Integer.parseInt(ver1[1]);
            int minorVer2 = Integer.parseInt(ver2[1]);
            if (minorVer1 < minorVer2) {
                return -1;
            } else if (minorVer1 > minorVer2) {
                return 1;
            } else {
                // compare subminor versions
                int subminorVer1 = Integer.parseInt(ver1[2]);
                int subminorVer2 = Integer.parseInt(ver2[2]);
                if (subminorVer1 < subminorVer2) {
                    return -1;
                } else if (subminorVer1 > subminorVer2) {
                    return 1;
                } else { 
                    return 0;
                }
            }
        }
    }
}
