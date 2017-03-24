package me.ele.hackathon.helper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

/***
 * Extracts Tar file
 * @author Aditya.YAGNIK
 *
 */
class TarExtractor{
    public void extract() throws IOException{
        String filePath = "file.tar";
        FileInputStream fis = new FileInputStream(new File(filePath));
        BufferedInputStream bis = new BufferedInputStream(fis);
        try {
            ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(bis);
            if (input instanceof TarArchiveInputStream){
                System.out.println("It is a tar input stream");
                TarArchiveInputStream tarInput = (TarArchiveInputStream)input;
                TarArchiveEntry entry = tarInput.getNextTarEntry();
                while (entry != null) {
                    String name = entry.getName();
                    System.out.println("Entry: " + name  );
                    entry = tarInput.getNextTarEntry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public static void main(String[] args) throws IOException{
        TarExtractor t = new TarExtractor();
        t.extract();
    }
}