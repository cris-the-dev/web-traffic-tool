package com.tiennln.webtraffictool.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The FileHelper is a class that ...
 *
 * @author Theon Tran Phan Truong Thinh
 * @version 1.0
 * @since 21/04/2024 10:59 AM
 */
public class FileHelper {
    private FileHelper() {
    }

    public static void writeToZipFile(String path, ZipOutputStream zipStream) throws IOException {
        var aFile = new File(path);
        var fis = new FileInputStream(aFile);
        var zipEntry = new ZipEntry(path);
        zipStream.putNextEntry(zipEntry);
        var bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipStream.write(bytes, 0, length);
        }
        zipStream.closeEntry();
        fis.close();
    }

    public static void createFile(String filename, String text) throws FileNotFoundException {
        try (var out = new PrintWriter(filename)) {
            out.println(text);
        }
    }
}
