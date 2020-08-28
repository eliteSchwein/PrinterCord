package de.eliteschw31n.utils;

import de.eliteschw31n.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public abstract class Command {

    private final String command;
    private final String extendedDescription;
    private final String description;
    private final boolean adminCommand;
    private final boolean privateCommand;
    private final String reactKeyWord;
    protected Main printerCord = null;

    public Command(final String command, final String description, final String extendedDescription, final boolean adminCommand, final boolean privateCommand, final String reactKeyWord) {
        this.command = command;
        this.extendedDescription = extendedDescription;
        this.description = description;
        this.adminCommand = adminCommand;
        this.privateCommand = privateCommand;
        this.reactKeyWord = reactKeyWord;
    }

    public abstract void execute(final String[] args, final String prefix, final Message message);

    public abstract void executeReact(final MessageReaction.ReactionEmote reactionEmote, final User reactSender, final Message message);

    public abstract void executeAddReact(final Message message);

    protected EmbedBuilder getEmbed(final User requester) {
        return new EmbedBuilder().setFooter("@" + requester.getName() + "#" + requester.getDiscriminator(),
                requester.getEffectiveAvatarUrl()).setColor(Color.getColor("#247083")).setThumbnail(printerCord.getMainConfiguration().getGeneralEmbed());
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

    public String getExtendedDescription() {
        return extendedDescription;
    }

    public String getReactKeyWord() {
        return reactKeyWord;
    }

    public boolean isAdminCommand() {
        return adminCommand;
    }

    public boolean isPrivateCommand() {
        return privateCommand;
    }

    public EmbedBuilder getExtendedDescription(String prefix, Message message) {
        EmbedBuilder embedBuilder = getEmbed(message.getAuthor());
        embedBuilder.setAuthor("Command Description");
        embedBuilder.setTitle(prefix + command);
        StringBuilder descriptionBuilder = new StringBuilder();
        String[] lines = extendedDescription.split(" ");
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
}
