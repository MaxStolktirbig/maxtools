package mx.helper.tools.communication;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    //the standard directory to log to is the working directory
    public static String logDir = System.getProperty("user.dir");

    public static void log(String error){
        File file = setSettings("error");
        writeToLog(file, error);
    }
    public static void log(Exception e){
        File file = setSettings("exception");
        writeToLog(file, e.toString() );
    }

    private static File setSettings(String type){
        File root = new File(logDir);
        File file = new File(root, "logs/"+type);
        file.mkdirs();
        return checkErrorLog(type, file);
    }


    private static File checkErrorLog(String type, File file) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        File[] children = file.listFiles();
        String filename = "/"+ dtf.format(now) + "-" + type + "-log" + ".log";
        if (children != null) {
            for (File child : children) {
                if (child.getName().equals(filename) && child.canWrite()) {
                    return file;
                }
            }
        }
        String path = file.getPath();
        return new File(path + filename);
    }



    private static void writeToLog(File file, String appendText){
        try {
            file.createNewFile();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String time = now.format(dtf);

            appendText = "["+time+"]\n"+appendText+"\n\n\n";
            FileOutputStream os = new FileOutputStream(file, true);
            os.write(appendText.getBytes(), 0, appendText.length());
            os.close();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Something went wrong trying to write to the log file");
        }
    }



}
