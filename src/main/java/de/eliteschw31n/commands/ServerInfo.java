package de.eliteschw31n.commands;

import com.vdurmont.emoji.EmojiManager;
import de.eliteschw31n.Main;
import de.eliteschw31n.utils.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import oshi.SystemInfo;
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
        pages.add(getCPU(getMain().getSystemInfo()));
        pages.add(getRAM(getMain().getSystemInfo()));
        for (String disk : getDisks(getMain().getSystemInfo())) {
            pages.add(disk);
        }
        pages.add(getOS(getMain().getSystemInfo()));
        pages.add(getNetwork());
        pages.add(getJava());
        pages.add(getPrinterCord());
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
        translateTemplate(page, embedBuilder);
        embedBuilder.setThumbnail(getMain().getMainConfiguration().getServerInfoEmbed());
        embedBuilder.setAuthor("Serverinfo " + site + "/" + maxsite);
        return embedBuilder;
    }

    private String getCPU(SystemInfo systemInfo) {
        StringBuilder cpuBuilder = new StringBuilder();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor centralProcessor = hardware.getProcessor();
        Sensors sensors = hardware.getSensors();
        double cpuCurrentFreq = (double) centralProcessor.getCurrentFreq()[0] / 1000000.0D;
        cpuBuilder.append("TITLE:CPU ");
        cpuBuilder.append("FIELD:Name:" + centralProcessor.getProcessorIdentifier().getName().replace(" ", "_") + " ");
        cpuBuilder.append("FIELD:Cores:" + centralProcessor.getLogicalProcessorCount() + "/" + centralProcessor.getPhysicalProcessorCount() + " ");
        cpuBuilder.append("FIELD:Freq:" + round(cpuCurrentFreq, 2) + "MHz ");
        cpuBuilder.append("FIELD:Arch:" + centralProcessor.getProcessorIdentifier().getMicroarchitecture().replace(" ", "_") + " ");
        cpuBuilder.append("FIELD:Usage:" + round(getMain().getCpuLoad() * 100D, 2) + "% ");
        cpuBuilder.append("FIELD:Temp:" + round(sensors.getCpuTemperature(), 2) + "°C ");
        cpuBuilder.append("FIELD:Voltage:" + round(sensors.getCpuVoltage(), 4) + "V");
        return cpuBuilder.toString();
    }

    private String getRAM(SystemInfo systemInfo) {
        StringBuilder ramBuilder = new StringBuilder();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        GlobalMemory randomAccessMemory = hardware.getMemory();
        double totalRam = randomAccessMemory.getTotal() / (double) (1024 * 1024 * 1024);
        double freeRam = randomAccessMemory.getAvailable() / (double) (1024 * 1024 * 1024);
        ramBuilder.append("TITLE:RAM ");
        ramBuilder.append("FIELD:Usage:" + round((totalRam - freeRam), 2) + "/" + round(totalRam, 2) + "GB" + " ");
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
        return ramBuilder.toString();
    }

    private List<String> getDisks(SystemInfo systemInfo) {
        List<String> diskPages = new ArrayList<>();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        List<HWDiskStore> disks = hardware.getDiskStores();
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
                    diskBuilder.append("FIELD:Partition_" + (iPart + 1) + "_Type:" +
                            "" + partition.getType().replace(" ", "_").replace(":", ";") + " " +
                            "FIELD:Partition_" + (iPart + 1) + "_Size:" + round(partitionSize, 2) + "GB" + " ");
                    if (partition.getMountPoint().replace(" ", "_").replace(":", ";").length() != 0) {
                        diskBuilder.append("FIELD:Partition_" + (iPart + 1) + "_Mount:" +
                                "" + partition.getMountPoint().replace(" ", "_").replace(":", ";") + " ");
                    } else {
                        diskBuilder.append("FIELD:Partition_" + (iPart + 1) + "_Mount:" + "N/A" + " ");
                    }
                }
            }
            diskPages.add(diskBuilder.toString());
        }
        return diskPages;
    }

    private String getOS(SystemInfo systemInfo) {
        StringBuilder osBuilder = new StringBuilder();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        osBuilder.append("TITLE:OS ");
        osBuilder.append("FIELD:Family:" + operatingSystem.getFamily() + " ");
        osBuilder.append("FIELD:Manufacturer:" + operatingSystem.getManufacturer() + " ");
        osBuilder.append("FIELD:Build_Number:" + operatingSystem.getVersionInfo().getBuildNumber() + " ");
        osBuilder.append("FIELD:Code_Name:" + operatingSystem.getVersionInfo().getCodeName() + " ");
        osBuilder.append("FIELD:Version:" + operatingSystem.getVersionInfo().getVersion());
        return osBuilder.toString();
    }

    private String getNetwork() {
        StringBuilder networkBuilder = new StringBuilder();
        networkBuilder.append("TITLE:Network ");
        networkBuilder.append("FIELD:Google_Ping:" + getGooglePing() + " ");
        networkBuilder.append("FIELD:Websocket_Ping:" + getMain().getDiscordBot().getGatewayPing() + "ms" + " ");
        networkBuilder.append("FIELD:Rest_Ping:" + getMain().getDiscordBot().getRestPing().complete() + "ms");
        return networkBuilder.toString();
    }

    private String getJava() {
        StringBuilder javaBuilder = new StringBuilder();
        javaBuilder.append("TITLE:Java ");
        javaBuilder.append("FIELD:Version:" + System.getProperty("java.version") + " ");
        javaBuilder.append("FIELD:Arch:" + System.getProperty("os.arch"));
        return javaBuilder.toString();
    }

    private String getPrinterCord() {
        StringBuilder printerCordBuilder = new StringBuilder();
        double totalRam = Runtime.getRuntime().totalMemory() / (double) (1024 * 1024);
        double freeRam = Runtime.getRuntime().freeMemory() / (double) (1024 * 1024);
        printerCordBuilder.append("TITLE:PrinterCord ");
        printerCordBuilder.append("FIELD:Commands:" + getMain().getCommandManager().getAvailableCommands().size() + " ");
        printerCordBuilder.append("FIELD:Version:" + Main.getProperties().getProperty("version") + " ");
        printerCordBuilder.append("FIELD:JDA_Version:" + JDAInfo.VERSION + " ");
        printerCordBuilder.append("FIELD:Ram_Usage:" + round((totalRam - freeRam), 2) + "/" + round(totalRam, 2) + "MB");
        return printerCordBuilder.toString();
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private String getGooglePing() {
        String result;
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


    private void translateTemplate(String input, EmbedBuilder embedInput) {
        StringBuilder descriptionBuilder = new StringBuilder();
        for (String inputPart : input.split(" ")) {
            if (inputPart.startsWith("TITLE")) {
                embedInput.setTitle(inputPart.replaceFirst("TITLE:", "").replace("_", " "));
            } else if (inputPart.startsWith("FIELD")) {
                embedInput.addField(inputPart.split(":")[1].replace("_", " ").replace(";", ":"),
                        inputPart.split(":")[2].replace("_", " ").replace(";", ":"), true);
            } else {
                descriptionBuilder.append(inputPart + " ");
            }
        }
    }
}
