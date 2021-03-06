package mx.helper.tools.communication;

public final class SystemMessage {
    //https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    static boolean stacktraceEnabled = false;

    public static void stacktraceOn(){
        stacktraceEnabled = true;
    }

    public static void stacktraceOff(){
        stacktraceEnabled = false;
    }

    public static void warningMessage(String message){
        System.out.println("[-"+ANSI_YELLOW+"WARNING"+ANSI_RESET+"-] "+message);
    }

    public static void errorMessage(String message){
        String outMessage = ("["+ANSI_RED+"ERROR"+ANSI_RESET+"] "+message);
        Logger.log(message);
        System.out.println(outMessage);
    }
    public static void exceptionMessage(Exception e){
        Logger.log(e);
        if(stacktraceEnabled){
            warningMessage("It is recommended to disable stacktrace for live environments.");
            System.out.println("[" + ANSI_RED + "STACKTRACE" + ANSI_RESET + "] "+e.getClass().toString()+": "+e.getMessage());
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            for(StackTraceElement element: stackTraceElements){
                System.out.println("[" + ANSI_RED + "STACKTRACE" + ANSI_RESET + "] \t\t"+ANSI_RED +element.toString()+ ANSI_RESET);
            }
            System.out.println("\n");
        } else {
            errorMessage(e.getClass().getName());
        }
    }
    public static void infoMessage(String message){
        System.out.println("["+ANSI_CYAN+"INFO"+ANSI_RESET+"] "+message);
    }

    public static void finishedTaskMessage(String message){
        System.out.println("["+ANSI_GREEN+"DONE"+ANSI_RESET+"] " + message);
    }
    public static void finishedTaskMessage(){
        finishedTaskMessage("");
    }
    public static void connectionMessage(String message){
        System.out.println("["+ANSI_PURPLE+"CONNECTION"+ANSI_RESET+"] "+message);
    }
}
