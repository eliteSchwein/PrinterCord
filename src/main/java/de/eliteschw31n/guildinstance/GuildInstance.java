package de.eliteschw31n.guildinstance;

import de.eliteschw31n.guildinstance.util.AdminRole;
import de.eliteschw31n.printercordinstance.PrinterCordInstance;
import de.eliteschw31n.utils.CustomCommand;
import de.eliteschw31n.utils.SetupStep;

import java.util.List;

public class GuildInstance {
    private List<PrinterCordInstance> printerCordInstances;
    private List<AdminRole> adminRoles;
    private List<CustomCommand> customCommands;
    private boolean percentJobPing;
    private int jobDelay;
    private int instanceLimitPerUser;
    private long broadcastChannel;
    private long adminCommandChannel;
    private long generalCommandChannel;
    private boolean setupProcess;
    private int setupStep;
    private SetupStep setupPage;

    public GuildInstance(final List<PrinterCordInstance> printerCordInstances, final boolean percentJobPing,
                         final int jobDelay, final List<AdminRole> adminRoles, final List<CustomCommand> customCommands,
                         final int instanceLimitPerUser, final long broadcastChannel, final long adminCommandChannel,
                         final long generalCommandChannel) {
        this.printerCordInstances = printerCordInstances;
        this.percentJobPing = percentJobPing;
        this.jobDelay = jobDelay;
        this.adminRoles = adminRoles;
        this.customCommands = customCommands;
        this.instanceLimitPerUser = instanceLimitPerUser;
        this.broadcastChannel = broadcastChannel;
        this.adminCommandChannel = adminCommandChannel;
        this.generalCommandChannel = generalCommandChannel;
    }

    public List<PrinterCordInstance> getPrinterCordInstances() {
        return printerCordInstances;
    }

    public void setPrinterCordInstances(List<PrinterCordInstance> printerCordInstances) {
        this.printerCordInstances = printerCordInstances;
    }

    public boolean isPercentJobPing() {
        return percentJobPing;
    }

    public void setPercentJobPing(boolean percentJobPing) {
        this.percentJobPing = percentJobPing;
    }

    public int getJobDelay() {
        return jobDelay;
    }

    public void setJobDelay(int jobDelay) {
        this.jobDelay = jobDelay;
    }

    public List<AdminRole> getAdminRoles() {
        return adminRoles;
    }

    public void setAdminRoles(List<AdminRole> adminRoles) {
        this.adminRoles = adminRoles;
    }

    public List<CustomCommand> getCustomCommands() {
        return customCommands;
    }

    public void setCustomCommands(List<CustomCommand> customCommands) {
        this.customCommands = customCommands;
    }

    public int getInstanceLimitPerUser() {
        return instanceLimitPerUser;
    }

    public void setInstanceLimitPerUser(int instanceLimitPerUser) {
        this.instanceLimitPerUser = instanceLimitPerUser;
    }

    public long getBroadcastChannel() {
        return broadcastChannel;
    }

    public void setBroadcastChannel(long broadcastChannel) {
        this.broadcastChannel = broadcastChannel;
    }

    public long getAdminCommandChannel() {
        return adminCommandChannel;
    }

    public void setAdminCommandChannel(long adminCommandChannel) {
        this.adminCommandChannel = adminCommandChannel;
    }

    public long getGeneralCommandChannel() {
        return generalCommandChannel;
    }

    public void setGeneralCommandChannel(long generalCommandChannel) {
        this.generalCommandChannel = generalCommandChannel;
    }

    public boolean isSetupProcess() {
        return setupProcess;
    }

    public void setSetupProcess(boolean setupProcess) {
        this.setupProcess = setupProcess;
    }

    public SetupStep getSetupPage() {
        return setupPage;
    }

    public void setSetupPage(SetupStep setupPage) {
        this.setupPage = setupPage;
    }

    public int getSetupStep() {
        return setupStep;
    }

    public void setSetupStep(int setupStep) {
        this.setupStep = setupStep;
    }
}
