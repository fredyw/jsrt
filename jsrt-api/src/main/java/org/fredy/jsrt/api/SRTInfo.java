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

import java.util.Iterator;
import java.util.TreeSet;

import org.fredy.jsrt.editor.SRTEditor;

/**
 * This class stores collections of SRT objects.
 * 
 * All the methods here perform low-level operations on SRTInfo object.
 * If you need to perform high-level operations for editing the SRTInfo,
 * use {@link SRTEditor} instead.
 * 
 * @author fredy
 */
public class SRTInfo implements Iterable<SRT>, Cloneable {
    private final TreeSet<SRT> info;
    
    /**
     * Creates a new instance of SRTInfo.
     */
    public SRTInfo() {
        info = new TreeSet<>();
    }
    
    /**
     * Creates a new instance of SRTInfo.
     * This constructor acts as a copy constructor.
     * 
     * @param srtInfo the SRTInfo object
     */
    public SRTInfo(SRTInfo srtInfo) {
        info = new TreeSet<>(srtInfo.info);
    }
    
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
     * {@inheritDoc}
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
    public void remove(int number) {
        info.remove(get(number));
        
    }
    
    /**
     * Gets the SRT object from a given number.
     * 
     * @param number the subtitle number
     * @return the SRT object
     */
    public SRT get(int number) {
        // Create a dummy SRT object since the comparison is by number only.
        return info.tailSet(new SRT(number, null, null, new String[]{})).first();
    }
    
    /**
     * Gets the SRT object.
     * 
     * @param srt the SRT object
     * @return the SRT object
     */
    public SRT get(SRT srt) {
        return info.tailSet(srt).first();
    }
    
    /**
     * Check if the subtitle number is in the SRTInfo object.
     * 
     * @param number the subtitle number
     * @return true if the subtitle number is in the SRTInfo; false otherwise
     */
    public boolean contains(int number) {
        return info.contains(new SRT(number, null, null, new String[]{}));
    }
    
    /**
     * Check if the SRT is in the SRTInfo object.
     * 
     * @param srt the SRT object
     * @return true if the subtitle number is in the SRTInfo; false otherwise
     */
    public boolean contains(SRT srt) {
        return info.contains(srt);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() {
        return new SRTInfo(this);
    }
}