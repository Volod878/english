package ru.volod878.english.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;

public class ParserMp3 {
    public static void main(String[] args) {
        try{
            BufferedInputStream in = new BufferedInputStream(new URL("https://wooordhunt.ru/data/sound/sow/us/consumer.mp3").openStream());
            FileOutputStream fos = new FileOutputStream("sound.mp3");
            BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
            byte[] data = new byte[1024];
            int x;
            while((x=in.read(data,0,1024))>=0){
                bout.write(data,0,x);
            }
            fos.flush();
            bout.flush();
            fos.close();
            bout.close();
            in.close();
        }
        catch (Exception ignored) {}
    }
}
