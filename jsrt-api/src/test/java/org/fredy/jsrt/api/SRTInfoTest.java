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

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Iterator;

import org.fredy.jsrt.api.SRT;
import org.fredy.jsrt.api.SRTInfo;
import org.fredy.jsrt.util.StringUtils;
import org.junit.Test;

/**
 * @author fredy
 */
public class SRTInfoTest {

   @Test
   public void testAdd() {
       SRTInfo srtInfo = new SRTInfo();
       Date d = new Date();
       srtInfo.add(new SRT(2, d, d, "Hello", "World"));
       srtInfo.add(new SRT(1, d, d, "Foo", "Bar"));
       srtInfo.add(new SRT(2, d, d, "Bye", "World"));
       
       assertEquals(2, srtInfo.size());
       Iterator<SRT> iter = srtInfo.iterator();
       SRT srt = iter.next();
       assertEquals(1, srt.number);
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.startTime));
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.endTime));
       assertEquals("Foo Bar", StringUtils.join(srt.text, " "));
       
       srt = iter.next();
       assertEquals(2, srt.number);
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.startTime));
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.endTime));
       assertEquals("Bye World", StringUtils.join(srt.text, " "));
       
       assertFalse(iter.hasNext());
   }
   
   @Test
   public void testRemove1() {
       SRTInfo srtInfo = new SRTInfo();
       Date d = new Date();
       srtInfo.add(new SRT(3, d, d, "Hello", "World"));
       srtInfo.add(new SRT(1, d, d, "Foo", "Bar"));
       srtInfo.add(new SRT(2, d, d, "Bye", "World"));
       
       srtInfo.remove(3);
       
       assertEquals(2, srtInfo.size());
       Iterator<SRT> iter = srtInfo.iterator();
       SRT srt = iter.next();
       assertEquals(1, srt.number);
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.startTime));
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.endTime));
       assertEquals("Foo Bar", StringUtils.join(srt.text, " "));
       
       srt = iter.next();
       assertEquals(2, srt.number);
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.startTime));
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.endTime));
       assertEquals("Bye World", StringUtils.join(srt.text, " "));
       
       assertFalse(iter.hasNext());
   }
   
   @Test
   public void testRemove2() {
       SRTInfo srtInfo = new SRTInfo();
       Date d = new Date();
       SRT toBeDeleted = new SRT(3, d, d, "Hello", "World");
       srtInfo.add(toBeDeleted);
       srtInfo.add(new SRT(1, d, d, "Foo", "Bar"));
       srtInfo.add(new SRT(2, d, d, "Bye", "World"));
       
       srtInfo.remove(toBeDeleted);
       
       assertEquals(2, srtInfo.size());
       Iterator<SRT> iter = srtInfo.iterator();
       SRT srt = iter.next();
       assertEquals(1, srt.number);
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.startTime));
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.endTime));
       assertEquals("Foo Bar", StringUtils.join(srt.text, " "));
       
       srt = iter.next();
       assertEquals(2, srt.number);
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.startTime));
       assertEquals(SRTTimeFormat.format(d), SRTTimeFormat.format(srt.endTime));
       assertEquals("Bye World", StringUtils.join(srt.text, " "));
       
       assertFalse(iter.hasNext());
   }
   
   @Test
   public void testGet() {
       SRTInfo srtInfo = new SRTInfo();
       Date d = new Date();
       SRT srt1 = new SRT(1, d, d, "Foo", "Bar");
       srtInfo.add(srt1);
       SRT srt2 = new SRT(2, d, d, "Hello", "World");
       srtInfo.add(srt2);
       
       assertEquals(srt1, srtInfo.get(1));
       assertEquals(srt1, srtInfo.get(srt1));
   }
   
   @Test
   public void testContains() {
       SRTInfo srtInfo = new SRTInfo();
       Date d = new Date();
       SRT srt1 = new SRT(1, d, d, "Foo", "Bar");
       srtInfo.add(srt1);
       SRT srt2 = new SRT(2, d, d, "Hello", "World");
       srtInfo.add(srt2);
       
       assertTrue(srtInfo.contains(1));
       assertTrue(srtInfo.contains(srt1));
   }
}
