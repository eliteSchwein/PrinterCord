package de.eliteschw31n.utils;

public class MainConfiguration {
    private final String discordToken;
    private final String generalEmbed;
    private final String helpEmbed;
    private final String helpAdminEmbed;

    public MainConfiguration(final String discordToken, final String generalEmbed, final String helpEmbed, final String helpAdminEmbed) {
        this.discordToken = discordToken;
        this.generalEmbed = generalEmbed;
        this.helpEmbed = helpEmbed;
        this.helpAdminEmbed = helpAdminEmbed;
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
}
