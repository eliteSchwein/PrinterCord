package de.eliteschw31n;


import de.eliteschw31n.events.CommandManager;
import de.eliteschw31n.utils.MainConfiguration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    private static Properties properties = new Properties();
    private MainConfiguration mainConfiguration;
    private JDA discordBot;
    private CommandManager commandManager;
    private SystemInfo systemInfo;
    private long[] cpuPrevTicks = new long[CentralProcessor.TickType.values().length];

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
        System.out.println("Check Database...");
        System.out.println("Start Bot...");
        try {
            discordBot = JDABuilder.createDefault(mainConfiguration.getDiscordToken()).build();
            System.out.println("Invite URL: https://discordapp.com/api/oauth2/authorize?client_id=" + discordBot.getSelfUser().getId() + "&permissions=8&scope=bot");
        } catch (LoginException e) {
            e.printStackTrace();
            return;
        }
        commandManager = new CommandManager(this);
        System.out.println("Load Instances...");
        System.out.println("Load OSHI...");
        systemInfo = new SystemInfo();
        Timer timer = new Timer(1000, e -> {
            HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
            CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
            System.out.println("CPU Load " + getCpuLoad());
            cpuPrevTicks = centralProcessor.getSystemCpuLoadTicks();
        });
        System.out.println("Startup Done!");
    }

    public static Properties getProperties() {
        return properties;
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
        double load = systemInfo.getHardware().getProcessor().getSystemCpuLoadBetweenTicks(cpuPrevTicks);
        return load;
    }

    public static void main(String[] args) {
        try {
            properties.load(Main.class.getResourceAsStream("/project.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Main();
    }
}
