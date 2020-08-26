package de.eliteschw31n;


import java.io.IOException;
import java.util.Properties;

public class Main {
    public static Properties properties = new Properties();

    public static void main(String[] args) {
        try {
            properties.load(Main.class.getResourceAsStream("/project.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("PrinterCord " + properties.getProperty("version") + " starting...");
        System.out.println("Check Local Files...");
        System.out.println("Check Database...");
        System.out.println("Load Instances...");
        System.out.println("Startup Done!");
    }
}
