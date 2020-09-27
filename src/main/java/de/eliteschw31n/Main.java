package de.eliteschw31n;


import de.eliteschw31n.events.CommandManager;
import de.eliteschw31n.utils.DataBase;
import de.eliteschw31n.utils.Event;
import de.eliteschw31n.utils.MainConfiguration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.reflections.Reflections;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import javax.security.auth.login.LoginException;
import javax.swing.Timer;
import java.io.*;
import java.util.*;

public class Main {
    private static Properties properties = new Properties();
    private MainConfiguration mainConfiguration;
    private JDA discordBot;
    private Set<Event> events;
    private CommandManager commandManager;
    private SystemInfo systemInfo;
    private long[] cpuPrevTicks = new long[CentralProcessor.TickType.values().length];
    private double cpuLoad = 0;
    private DataBase dataBase;

    public Main() {
        System.out.println("PrinterCord " + properties.getProperty("version") + " starting...");
        System.out.println("Check Local Files...");
        final DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        File mainConfigurationFile = new File("mainConfig.yml");
        if (!mainConfigurationFile.exists()) {
            System.out.println("Main Configuration not found! Generating it now!");
            try {
                Map<String, Object> data = new HashMap<>();
                data.put("discordToken", "123456789");
                data.put("embedGeneral", "http://example.com");
                data.put("embedHelp", "http://example.com");
                data.put("embedAdminHelp", "http://example.com");
                data.put("embedServerInfo", "http://example.com");
                mainConfigurationFile.createNewFile();
                FileWriter writer = new FileWriter(mainConfigurationFile.getAbsoluteFile());
                yaml.dump(data, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        yaml = new Yaml();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(mainConfigurationFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, Object> yamlData = yaml.load(inputStream);
        mainConfiguration = new MainConfiguration(((String) yamlData.get("discordToken")), (String) yamlData.get("embedGeneral"), (String) yamlData.get("embedHelp"), (String) yamlData.get("embedAdminHelp"), (String) yamlData.get("embedServerInfo"));
        System.out.println("Connect Database...");
        dataBase = new DataBase();
        dataBase.setup();
        System.out.println("Start Bot...");
        try {
            discordBot = JDABuilder.createDefault(mainConfiguration.getDiscordToken()).build();
            System.out.println("Invite URL: https://discordapp.com/api/oauth2/authorize?client_id=" + discordBot.getSelfUser().getId() + "&permissions=8&scope=bot");
        } catch (LoginException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Load Events...");
        events = new HashSet<>();
        final Set<Class<? extends Event>> classes = new Reflections("de.eliteschw31n.events")
                .getSubTypesOf(Event.class);
        for (Class<? extends Event> eventClass : classes) {
            try {
                final Event event = eventClass.getDeclaredConstructor().newInstance();
                event.setInstance(this);
                event.execute();
                if (event.getEventName().equals("CommandManager")) {
                    commandManager = (CommandManager) event;
                }
                if (events.add(event)) {
                    System.out.println("Registered " + event.getEventName() + " Event");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        System.out.println("Load Instances...");
        System.out.println("Load OSHI...");
        systemInfo = new SystemInfo();
        Timer timer = new Timer(1000, e -> {
            HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
            CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
            cpuLoad = centralProcessor.getSystemCpuLoadBetweenTicks(cpuPrevTicks);
            cpuPrevTicks = centralProcessor.getSystemCpuLoadTicks();
        });
        timer.start();
        System.out.println("Startup Done!");
    }

    public static Properties getProperties() {
        return properties;
    }

    public static void main(String[] args) {
        try {
            properties.load(Main.class.getResourceAsStream("/project.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Main();
    }

    public JDA getDiscordBot() {
        return discordBot;
    }

    public MainConfiguration getMainConfiguration() {
        return mainConfiguration;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public double getCpuLoad() {
        return cpuLoad;
    }

    public Set<Event> getEvents() {
        return events;
    }
}
