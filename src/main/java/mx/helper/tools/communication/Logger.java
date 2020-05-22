package mx.helper.tools.communication;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static boolean on = true;
    //the standard directory to log to is the working directory
    public static String logDir = System.getProperty("user.dir");

    public static void on(){
        on = true;
    }

    public static void off(){
        if(!SystemMessage.stacktraceEnabled){
            SystemMessage.warningMessage("Logging is off; enabeling stacktrace");
            SystemMessage.stacktraceOn();
        }
        on = false;
    }

    public static void log(String error) {
        if (on) {
            File file = setSettings("error");
            writeToLog(file, error);
        }
    }
    public static void log(Exception e) {
        if (on) {
            File file = setSettings("exception");
            String exceptionstring = e.getClass().toString() + " at line: "+e.getStackTrace()[0].getLineNumber();
            for (StackTraceElement element : e.getStackTrace()) {
                exceptionstring += "\n\t\t\t" + element.toString();
            }
            writeToLog(file, exceptionstring);
        }
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
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String time = now.format(dtf);

            boolean newFile = file.createNewFile();
            appendText = "\n\n\n["+time+"]\n"+appendText+"\n\n\n";
            for(int i = 0; i < 50; i++){
                if(newFile){
                   appendText = "="+appendText;
                }
                appendText = appendText+"=";
            }
            FileOutputStream os = new FileOutputStream(file, true);
            os.write(appendText.getBytes(), 0, appendText.length());
            os.close();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Something went wrong trying to write to the log file");
        }
    }

}
