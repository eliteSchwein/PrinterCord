package de.eliteschw31n.commands;

import com.vdurmont.emoji.EmojiManager;
import de.eliteschw31n.Main;
import de.eliteschw31n.utils.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServerInfo extends Command {
    public ServerInfo() {
        super("Serverinfo", "Show the Informations of the Server", "Shows Hardware and Software Informations", false, true, "Serverinfo");
    }

    @Override
    public void execute(String[] args, String prefix, Message message) {
        message.getChannel().sendMessage(getSite(2, false, message.getAuthor()).build()).queue();
    }

    @Override
    public void executeReact(MessageReaction.ReactionEmote reactionEmote, User reactSender, Message message) {
        if (reactionEmote.isEmote()) {
            return;
        }
        if (reactionEmote.getEmoji().equalsIgnoreCase(EmojiManager.getForAlias("arrow_backward").getUnicode())) {
            int Site = Integer.parseInt(message.getEmbeds().get(0).getAuthor().getName().replace("Serverinfo ", "").split("/")[0]);
            message.editMessage(getSite(Site, false, reactSender).build()).queue();
        }
        if (reactionEmote.getEmoji().equalsIgnoreCase(EmojiManager.getForAlias("arrow_forward").getUnicode())) {
            int Site = Integer.parseInt(message.getEmbeds().get(0).getAuthor().getName().replace("Serverinfo ", "").split("/")[0]);
            message.editMessage(getSite(Site, true, reactSender).build()).queue();
        }
    }

    @Override
    public void executeAddReact(Message message) {
        message.addReaction(EmojiManager.getForAlias("arrow_backward").getUnicode()).queue();
        message.addReaction(EmojiManager.getForAlias("arrow_forward").getUnicode()).queue();
    }

    private EmbedBuilder getSite(int site, boolean next, User user) {
        List<String> pages = new ArrayList<>();
        HardwareAbstractionLayer hardwareAbstractionLayer = getMain().getSystemInfo().getHardware();
        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
        Sensors sensors = hardwareAbstractionLayer.getSensors();
        GlobalMemory randomAccessMemory = hardwareAbstractionLayer.getMemory();
        OperatingSystem operatingSystem = getMain().getSystemInfo().getOperatingSystem();
        List<HWDiskStore> disks = hardwareAbstractionLayer.getDiskStores();
        double totalRam = randomAccessMemory.getTotal() / (double) (1024 * 1024 * 1024);
        double freeRam = randomAccessMemory.getAvailable() / (double) (1024 * 1024 * 1024);
        pages.add("TITLE:CPU " +
                "FIELD:Name:" + centralProcessor.getProcessorIdentifier().getName().replace(" ", "_") + " " +
                "FIELD:Cores:" + centralProcessor.getLogicalProcessorCount() + "/" + centralProcessor.getPhysicalProcessorCount() + " " +
                "FIELD:Arch:" + centralProcessor.getProcessorIdentifier().getMicroarchitecture().replace(" ", "_") + " " +
                "FIELD:Usage:" + round(centralProcessor.getSystemCpuLoadBetweenTicks(getMain().getCpuPrevTicks()), 2) + "% " +
                "FIELD:Temp:" + round(sensors.getCpuTemperature(), 2) + "°C " +
                "FIELD:Voltage:" + round(sensors.getCpuVoltage(), 4) + "V");
        StringBuilder ramBuilder = new StringBuilder();
        ramBuilder.append("TITLE:Ram" + " " +
                "FIELD:Usage:" + round((totalRam - freeRam), 2) + "/" + round(totalRam, 2) + "GB" + " ");
        if (!randomAccessMemory.getPhysicalMemory().isEmpty()) {
            for (int i = 0; i < randomAccessMemory.getPhysicalMemory().size(); i++) {
                PhysicalMemory ramStick = randomAccessMemory.getPhysicalMemory().get(i);
                double ramFreq = (double) ramStick.getClockSpeed() / 1000000.0D;
                double ramCapacity = (double) ramStick.getCapacity() / (double) (1024 * 1024 * 1024);
                ramBuilder.append("FIELD:Ram_" + (i + 1) + "_Slot:" + ramStick.getBankLabel().replace(" ", "_") + " " +
                        "FIELD:Ram_" + (i + 1) + "_Manufacturer:" + ramStick.getManufacturer().replace(" ", "_") + " " +
                        "FIELD:Ram_" + (i + 1) + "_Freq:" + round(ramFreq, 2) + "MHz" + " " +
                        "FIELD:Ram_" + (i + 1) + "_Size:" + round(ramCapacity, 2) + "GB" + " " +
                        "FIELD:Ram_" + (i + 1) + "_Type:" + ramStick.getMemoryType().replace(" ", "_") + " ");
            }
        }
        pages.add(ramBuilder.toString());
        for (int i = 0; i < disks.size(); i++) {
            double diskSize = disks.get(i).getSize() / (double) (1024 * 1024 * 1024);
            StringBuilder diskBuilder = new StringBuilder();
            diskBuilder.append("TITLE:Disk_" + (i + 1) + " " +
                    "FIELD:Name:" + disks.get(i).getModel().replace(" ", "_").replace(":", ";") + " " +
                    "FIELD:Size:" + round(diskSize, 2) + "GB" + " ");
            if (!disks.get(i).getPartitions().isEmpty()) {
                for (int iPart = 0; iPart < disks.get(i).getPartitions().size(); iPart++) {
                    HWPartition partition = disks.get(i).getPartitions().get(iPart);
                    double partitionSize = partition.getSize() / (double) (1024 * 1024 * 1024);
                    diskBuilder.append("FIELD:Partition_" + (iPart + 1) + "_Name:" + partition.getName().replace(" ", "_").replace(":", ";") + " " +
                            "FIELD:Partition_" + (iPart + 1) + "_Type:" + partition.getType().replace(" ", "_").replace(":", ";") + " " +
                            "FIELD:Partition_" + (iPart + 1) + "_Mount:" + partition.getMountPoint().replace(" ", "_").replace(":", ";") + " " +
                            "FIELD:Partition_" + (iPart + 1) + "_Size:" + round(partitionSize, 2) + "GB" + " ");
                }
            }
            pages.add(diskBuilder.toString());
        }
        pages.add("TITLE:OS " +
                "FIELD:Family:" + operatingSystem.getFamily() + " " +
                "FIELD:Manufacturer:" + operatingSystem.getManufacturer() + " " +
                "FIELD:Build_Number:" + operatingSystem.getVersionInfo().getBuildNumber() + " " +
                "FIELD:Code_Name:" + operatingSystem.getVersionInfo().getCodeName() + " " +
                "FIELD:Version:" + operatingSystem.getVersionInfo().getVersion());
        pages.add("TITLE:Network " +
                "FIELD:Google_Ping:" + getGooglePing() + " " +
                "FIELD:Websocket_Ping:" + getMain().getDiscordBot().getGatewayPing() + "ms" + " " +
                "FIELD:Rest_Ping:" + getMain().getDiscordBot().getRestPing().complete() + "ms");
        pages.add("TITLE:Java " +
                "FIELD:Version:" + System.getProperty("java.version") + " " +
                "FIELD:Arch:" + System.getProperty("os.arch"));

        double totalProgrammRam = Runtime.getRuntime().totalMemory() / (double) (1024 * 1024);
        double freeProgrammRam = Runtime.getRuntime().freeMemory() / (double) (1024 * 1024);
        pages.add("TITLE:PrinterCord " +
                "FIELD:Commands:" + getMain().getCommandManager().getAvailableCommands().size() + " " +
                "FIELD:Version:" + Main.getProperties().getProperty("version") + " " +
                "FIELD:JDA_Version:" + JDAInfo.VERSION + " " +
                "FIELD:Ram_Usage:" + round((totalProgrammRam - freeProgrammRam), 2) + "/" + round(totalProgrammRam, 2) + "MB");
        EmbedBuilder embedBuilder = getEmbed(user);
        int maxsite = pages.size();
        if (site == 1 && !next) {
            site = maxsite;
        } else if (site == maxsite && next) {
            site = 1;
        } else if (next) {
            site = site + 1;
        } else {
            site = site - 1;
        }
        String page = pages.get(site - 1);
        StringBuilder descriptionBuilder = new StringBuilder();
        for (String pagePart : page.split(" ")) {
            if (pagePart.startsWith("TITLE")) {
                embedBuilder.setTitle(pagePart.replaceFirst("TITLE:", "").replace("_", " "));
            } else if (pagePart.startsWith("FIELD")) {
                embedBuilder.addField(pagePart.split(":")[1].replace("_", " ").replace(";", ":"), pagePart.split(":")[2].replace("_", " ").replace(";", ":"), true);
            } else {
                descriptionBuilder.append(pagePart + " ");
            }
        }
        embedBuilder.setDescription(descriptionBuilder.toString());
        embedBuilder.setAuthor("Serverinfo " + site + "/" + maxsite);
        return embedBuilder;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private String getGooglePing() {
        String result = "N/A";
        InetAddress inetAddress = null;
        InetSocketAddress socketAddress = null;
        SocketChannel sc = null;
        long timeToRespond = -1;
        Date start, stop;
        try {
            inetAddress = InetAddress.getByName("google.com");
        } catch (UnknownHostException e) {
            System.out.println("Problem, unknown host:");
            e.printStackTrace();
        }

        try {
            socketAddress = new InetSocketAddress(inetAddress, 80);
        } catch (IllegalArgumentException e) {
            System.out.println("Problem, port may be invalid:");
            e.printStackTrace();
        }
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(true);
            start = new Date();
            if (sc.connect(socketAddress)) {
                stop = new Date();
                timeToRespond = (stop.getTime() - start.getTime());
            }
        } catch (IOException e) {
            System.out.println("Problem, connection could not be made:");
            e.printStackTrace();
        }

        try {
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = timeToRespond + "ms";
        return result;
    }
}
