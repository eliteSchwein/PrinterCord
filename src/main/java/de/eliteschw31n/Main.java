package de.eliteschw31n;


import de.eliteschw31n.utils.MainConfiguration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    public static Properties properties = new Properties();
    public MainConfiguration mainConfiguration;
    public JDA discordBot;

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
                mainConfigurationFile.createNewFile();
                FileWriter writer = new FileWriter(mainConfigurationFile.getAbsoluteFile());
                yaml.dump(data, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        yaml = new Yaml();
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("/mainConfig.yml");
        Map<String, Object> yamlData = yaml.load(inputStream);
        mainConfiguration = new MainConfiguration(((String) yamlData.get("discordToken")));
        System.out.println("Check Database...");
        System.out.println("Start Bot...");
        try {
            discordBot = JDABuilder.createDefault(mainConfiguration.getDiscordToken()).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        System.out.println("Load Instances...");
        System.out.println("Startup Done!");
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
