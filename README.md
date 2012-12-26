jsrt
====

A small SRT parser in Java.
SRT is a subtitle file format (http://en.wikipedia.org/wiki/.srt#SubRip_text_file_format).

How to build
------------
jsrt uses Gradle build system (http://www.gradle.org/) and requires Java 7 to build.

### Building jsrt JAR ###
    gradle
The JAR will be created in build/libs/ directory

Examples
--------
__in.srt__

    1
    00:00:20,000 --> 00:00:24,400
    Hello World
    Bye World
    
    2
    00:00:24,600 --> 00:00:27,800
    Foo Bar
    Bar Foo
    

__out.srt__

    1
    16:17:55,370 --> 16:17:55,370
    Hello
    World
    
    2
    16:17:55,370 --> 16:17:55,370
    Bye
    World
    

__Main.java__

    import java.io.File;
    import java.util.Date;
    
    import org.fredy.jsrt.SRT;
    import org.fredy.jsrt.SRTInfo;
    import org.fredy.jsrt.SRTReader;
    import org.fredy.jsrt.SRTTimeFormat;
    import org.fredy.jsrt.SRTWriter;
    
    public class Main {
        private static void testRead() {
            SRTInfo info = SRTReader.read(new File("in.srt"));
            for (SRT s : info) {
                System.out.println("Number: " + s.number);
                System.out.println("Start time: " + SRTTimeFormat.format(s.startTime));
                System.out.println("End time: " + SRTTimeFormat.format(s.endTime));
                System.out.println("Texts:");
                for (String str : s.subtitles) {
                    System.out.println("    " + str);
                }
                System.out.println();
            }
        }
        
        private static void testWrite() {
            SRTInfo info = new SRTInfo();
            Date d = new Date();
            info.add(new SRT(1, d, d, "Hello", "World"));
            info.add(new SRT(2, d, d, "Bye", "World"));
            
            SRTWriter.write(new File("out.srt"), info);
        }
        
        public static void main(String[] args) {
            testRead();
            testWrite();
        }
    }

