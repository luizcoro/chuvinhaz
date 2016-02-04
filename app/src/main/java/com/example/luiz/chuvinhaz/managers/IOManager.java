package com.example.luiz.chuvinhaz.managers;

import com.example.luiz.chuvinhaz.utils.Constants;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by luiz on 6/26/15.
 */
public class IOManager {

    private static LinkedList<String> queue = new LinkedList<>();


    public static File[] getOrderedFiles(File dir)
    {
        File[] files = dir.listFiles();
        Arrays.sort(files);

        return files;
    }

    public static String getNextUserFilePath(File dir)
    {
        if(!dir.exists()) {
            dir.mkdirs();
            return dir.getName() + "/0001";
        }

        File [] files = dir.listFiles();
        if(files == null || files.length == 0)
            return dir.getName() + "/0001";

        Arrays.sort(files);

        int v = Integer.parseInt(files[files.length - 1].getName());

        return dir.getName() + "/" + String.format("%04d", (v+1));
    }

    public static void writeQueueToFile(File file, boolean append)
    {

        if(queue.isEmpty())
            return;

        StringBuilder builder = new StringBuilder();

        while(!queue.isEmpty())
            builder.append(queue.remove());

        try {
            FileUtils.writeStringToFile(file, builder.toString(), Charset.forName("UTF8"), append);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeStringToFile(File file, String text, boolean append )
    {
        queue.add(text);

        if(queue.size() == Constants.MAX_BUFFER_QUEUE) {
            StringBuilder builder = new StringBuilder();

            while(!queue.isEmpty())
                builder.append(queue.remove());

            try {
                FileUtils.writeStringToFile(file, builder.toString(), Charset.forName("UTF8"), append);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
