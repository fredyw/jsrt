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

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class stores collections of SRT objects.
 * 
 * @author fredy <fredy.wijaya@amd.com>
 */
public class SRTInfo implements Iterable<SRT> {
    private final Set<SRT> info = new TreeSet<>();
    
    /**
     * Adds SRT object into SRTInfo object. If SRT object already exists, the old
     * SRT object will be replaced with the new SRT object.
     * 
     * @param srt the SRT object to be added
     */
    public void add(SRT srt) {
        remove(srt);
        info.add(srt);
    }
    
    /**
     * Returns the iterator of SRTInfo object.
     * 
     * @return the iterator of SRTInfo object
     */
    public Iterator<SRT> iterator() {
        return info.iterator();
    }
    
    /**
     * Gets the number of SRT objects stored in SRTInfo object.
     * 
     * @return the number of SRT objects stored in SRTInfo object
     */
    public int size() {
        return info.size();
    }
    
    /**
     * Removes the SRT object from SRTInfo.
     * 
     * @param srt the SRT object to be removed from SRTInfo
     */
    public void remove(SRT srt) {
        // Set.remove() will check if the object is present in the Set, so
        // there is no need to do another check if the object is present in
        // the set
        info.remove(srt);
    }
    
    /**
     * Removes the SRT object with subtitle number from SRTInfo.
     * 
     * @param number the subtitle number to be removed from SRTInfo
     */
    public void remove(long number) {
        // Create a dummy SRT object since the comparison is by number only.
        info.remove(new SRT(number, null, null, new String[]{}));
    }
}