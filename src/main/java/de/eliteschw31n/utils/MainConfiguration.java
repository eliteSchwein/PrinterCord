package de.eliteschw31n.utils;

public class MainConfiguration {
    private final String discordToken;
    private final String generalEmbed;
    private final String helpEmbed;
    private final String helpAdminEmbed;
    private final String serverInfoEmbed;

    public MainConfiguration(final String discordToken, final String generalEmbed, final String helpEmbed, final String helpAdminEmbed, final String serverInfoEmbed) {
        this.discordToken = discordToken;
        this.generalEmbed = generalEmbed;
        this.helpEmbed = helpEmbed;
        this.helpAdminEmbed = helpAdminEmbed;
        this.serverInfoEmbed = serverInfoEmbed;
    }

    public String getDiscordToken() {
        return discordToken;
    }

    public String getGeneralEmbed() {
        return generalEmbed;
    }

    public String getHelpEmbed() {
        return helpEmbed;
    }

    public String getHelpAdminEmbed() {
        return helpAdminEmbed;
    }

    public String getServerInfoEmbed() {
        return serverInfoEmbed;
    }
}
