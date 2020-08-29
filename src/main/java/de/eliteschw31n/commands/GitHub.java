package de.eliteschw31n.commands;

import de.eliteschw31n.utils.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public class GitHub extends Command {
    public GitHub() {
        super("github", "get GitHub URL", "I am OpenSource", false, true, "Bot Invite Command");
    }

    @Override
    public void execute(String[] args, String prefix, Message message) {
        message.getChannel().sendMessage("https://github.com/eliteSchwein/PrinterCord").queue();
    }

    @Override
    public void executeReact(MessageReaction.ReactionEmote reactionEmote, User reactSender, Message message) {

    }

    @Override
    public void executeAddReact(Message message) {

    }
}
