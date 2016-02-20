package edu.upmc.dar.server.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void info(String message){
        log("INFO", message);
    }
    public static void warn(String message){
        log("WARN", message);
    }
    public static void err(String message){
        log("ERROR", message);
    }
    public static void log(String level, String message){
        System.out.println("[" + df.format(new Date()) + "] " + level + " " + message);
    }
}
