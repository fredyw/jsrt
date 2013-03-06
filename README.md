jsrt
====

A small SRT parser and editor in Java.
SRT is a subtitle file format (http://en.wikipedia.org/wiki/.srt#SubRip_text_file_format).

How to build
------------
jsrt uses Gradle build system (http://www.gradle.org/) and requires Java 7 and JavaFX 2 to build.

### Building jsrt JAR ###
    gradle
The JAR will be created in dist directory.

### Running jsrt-gui ###
java -jar dist/jsrt-gui-<version>.jar

Examples
--------
__in.srt__

    1
    00:00:20,000 --> 00:00:24,400
    Hello World. This is a long text.
    Bye World. This is a short text.
    
    2
    00:00:24,600 --> 00:00:27,800
    Foo Bar
    Bar Foo

__out1.srt__

    1
    16:17:55,370 --> 16:17:55,370
    Hello
    World
    
    2
    16:17:55,370 --> 16:17:55,370
    Bye
    World
    
__out2.srt__

    1
    00:00:05,000 --> 00:00:07,000
    Test
    
    2
    00:00:20,100 --> 00:00:24,500
    Hello
    World.
    This is a
    long text.
    Bye World.
    This is a
    short
    text.
    
    3
    00:00:24,600 --> 00:00:27,800
    Foo Bar
    Bar Foo
    
    4
    00:01:05,000 --> 00:01:07,000
    Test

__Main.java__

    package examples;
    
    import java.io.File;
    import java.util.Arrays;
    import java.util.Date;
    
    import org.fredy.jsrt.api.SRT;
    import org.fredy.jsrt.api.SRTInfo;
    import org.fredy.jsrt.api.SRTReader;
    import org.fredy.jsrt.api.SRTTimeFormat;
    import org.fredy.jsrt.api.SRTWriter;
    import org.fredy.jsrt.editor.SRTEditor;
    
    public class Main {
        private static void print(SRTInfo info) {
            for (SRT s : info) {
                System.out.println("Number: " + s.number);
                System.out.println("Start time: " + SRTTimeFormat.format(s.startTime));
                System.out.println("End time: " + SRTTimeFormat.format(s.endTime));
                System.out.println("Texts:");
                for (String line : s.text) {
                    System.out.println("    " + line);
                }
                System.out.println();
            }
        }
        
        private static void testRead() {
            SRTInfo info = SRTReader.read(new File("in.srt"));
            print(info);
        }
        
        private static void testWrite() {
            SRTInfo info = new SRTInfo();
            Date d = new Date();
            info.add(new SRT(1, d, d, "Hello", "World"));
            info.add(new SRT(2, d, d, "Bye", "World"));
            
            File f = new File("out1.srt");
            f.deleteOnExit();
            SRTWriter.write(f, info);
        }
        
        private static void testEdit() {
            SRTInfo info = SRTReader.read(new File("in.srt"));
            SRTEditor.updateText(info, 1, 10);
            SRTEditor.updateTime(info, 1, SRTTimeFormat.Type.MILLISECOND, 100);
            SRTEditor.prependSubtitle(info, "00:00:05,000", "00:00:07,000",
                Arrays.asList("Test"));
            SRTEditor.appendSubtitle(info, "00:01:05,000", "00:01:07,000",
                Arrays.asList("Test"));
            
            print(info);
            
            // Write it back
            File f = new File("out2.srt");
            f.deleteOnExit();
            SRTWriter.write(f, info);
        }
        
        public static void main(String[] args) {
            testRead();
            testWrite();
            testEdit();
        }
    }
