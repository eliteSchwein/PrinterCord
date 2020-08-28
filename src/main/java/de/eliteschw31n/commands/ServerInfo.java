package de.eliteschw31n.commands;

import de.eliteschw31n.utils.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public class ServerInfo extends Command {
    public ServerInfo() {
        super("Serverinfo", "Show the Informations of the Server", "Shows Hardware and Software Informations \nFIELD:software:show_software \nFIELD:hardware:show_hardware", false, true, "Serverinfo");
    }

    @Override
    public void execute(String[] args, String prefix, Message message) {

    }

    @Override
    public void executeReact(MessageReaction.ReactionEmote reactionEmote, User reactSender, Message message) {

    }

    @Override
    public void executeAddReact(Message message) {

    }
}
