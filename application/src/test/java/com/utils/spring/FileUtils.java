package com.utils.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class FileUtils {

    public static File createFile(String fileName, byte[] bytes) {

        File file = new File(fileName);

        try {

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {

            log.error(e.getMessage(), e.getStackTrace());
        }

        return file;
    }

    public static MultipartFile createMultipartFile(String fileName, byte[] bytes) {

       return new MultipartFile() {
           @Override
           public String getName() {
               return fileName;
           }

           @Override
           public String getOriginalFilename() {
               return fileName;
           }

           @Override
           public String getContentType() {
               return null;
           }

           @Override
           public boolean isEmpty() {
               return false;
           }

           @Override
           public long getSize() {
               return 0;
           }

           @Override
           public byte[] getBytes() throws IOException {
               return bytes;
           }

           @Override
           public InputStream getInputStream() throws IOException {
               return null;
           }

           @Override
           public void transferTo(File dest) throws IOException, IllegalStateException {

           }
       };
    }
}
