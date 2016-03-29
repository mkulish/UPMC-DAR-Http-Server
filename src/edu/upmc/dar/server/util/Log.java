package edu.upmc.dar.server.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static LogSeverity severity = LogSeverity.DEBUG;

    public static void debug(String message){
        log(LogSeverity.DEBUG, message);
    }
    public static void info(String message){
        log(LogSeverity.INFO, message);
    }
    public static void warn(String message){
        log(LogSeverity.WARN, message);
    }
    public static void err(String message){
        log(LogSeverity.ERROR, message);
    }
    public static void log(LogSeverity level, String message){
        if(level.ordinal() >= severity.ordinal()) {
            System.out.println("[" + df.format(new Date()) + "] [" + level + "]: " + message);
        }
    }
    public static void skipLine(){
        System.out.println();
    }

    public enum LogSeverity{
        DEBUG, INFO, WARN, ERROR
    }

    public static void setSeverity(LogSeverity severity) {
        Log.severity = severity;
    }
}
