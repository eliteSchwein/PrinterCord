package de.eliteschw31n.utils;

import de.eliteschw31n.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public abstract class CustomCommand {

    private final String command;
    private final String description;
    private final long guildId;
    private final boolean adminCommand;
    private final boolean isEmbedFeedback;
    private final String feedbackEmbedTitle;
    private final String feedbackEmbedColor;
    private final String feedbackEmbedIcon;
    protected Main printerCord = null;
    private String feedbackText;

    public CustomCommand(final String command, final String description, final boolean adminCommand, final long guildId, final String feedbackText, final boolean isEmbedFeedback, final String feedbackEmbedTitle, final String feedbackEmbedColor, final String feedbackEmbedIcon) {
        this.command = command;
        this.description = description;
        this.adminCommand = adminCommand;
        this.guildId = guildId;
        this.feedbackText = feedbackText;
        this.isEmbedFeedback = isEmbedFeedback;
        this.feedbackEmbedTitle = feedbackEmbedTitle;
        this.feedbackEmbedColor = feedbackEmbedColor;
        this.feedbackEmbedIcon = feedbackEmbedIcon;
    }

    public abstract void execute(final String[] args, final Message message);

    protected EmbedBuilder getEmbed(final User requester, final String feedbackText) {
        EmbedBuilder embedBuilder = new EmbedBuilder().setFooter("@" + requester.getName() + "#" + requester.getDiscriminator(),
                requester.getEffectiveAvatarUrl()).setColor(Color.getColor("#247083")).setThumbnail(printerCord.getMainConfiguration().getGeneralEmbed());
        if (feedbackEmbedTitle != null) {
            if (!feedbackEmbedTitle.equals("null")) {
                embedBuilder.setAuthor(feedbackEmbedTitle);
            }
        }
        if (feedbackEmbedIcon != null) {
            if (!feedbackEmbedIcon.equals("null")) {
                embedBuilder.setThumbnail(feedbackEmbedIcon);
            }
        }
        if (feedbackEmbedColor != null) {
            if (!feedbackEmbedColor.equals("null")) {
                embedBuilder.setColor(getColorFromHex(feedbackEmbedColor));
            }
        }
        embedBuilder.setDescription(feedbackText);
        return embedBuilder;
    }

    public void setInstance(final Main instance) {
        if (printerCord != null) {
            throw new IllegalStateException("Can only initialize once!");
        }
        printerCord = instance;
    }

    public Main getMain() {
        return printerCord;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getFeedbackEmbedTitle() {
        return feedbackEmbedTitle;
    }

    public String getFeedbackEmbedColor() {
        return feedbackEmbedColor;
    }

    public String getFeedbackEmbedIcon() {
        return feedbackEmbedIcon;
    }

    public boolean isAdminCommand() {
        return adminCommand;
    }

    public boolean isEmbedFeedback() {
        return isEmbedFeedback;
    }

    public long getGuildId() {
        return guildId;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public EmbedBuilder getDescription(String prefix, Message message) {
        EmbedBuilder embedBuilder = getEmbed(message.getAuthor(), feedbackText);
        embedBuilder.setAuthor("Command Description");
        embedBuilder.setTitle(prefix + command);
        StringBuilder descriptionBuilder = new StringBuilder();
        String[] lines = description.split(" ");
        for (String line : lines) {
            if (line.startsWith("FIELD")) {
                String[] fieldEntries = line.split(":");
                embedBuilder.addField(fieldEntries[1].replace("_", " "), fieldEntries[2].replace("_", " "), true);
            } else {
                descriptionBuilder.append(line).append(" ");
            }
        }
        embedBuilder.setDescription(descriptionBuilder.toString());
        return embedBuilder;
    }

    public Color getColorFromHex(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }
}
