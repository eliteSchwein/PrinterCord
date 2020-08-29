package de.eliteschw31n.commands;

import de.eliteschw31n.utils.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public class Invite extends Command {
    public Invite() {
        super("invite", "get Bot Invite Link", "I will sent you a Invite Link for me!", false, true, "Bot Invite Command");
    }

    @Override
    public void execute(String[] args, String prefix, Message message) {
        message.getChannel().sendMessage("https://discordapp.com/api/oauth2/authorize?client_id=" + getMain().getDiscordBot().getSelfUser().getId() + "&permissions=8&scope=bot").queue();
    }

    @Override
    public void executeReact(MessageReaction.ReactionEmote reactionEmote, User reactSender, Message message) {

    }

    @Override
    public void executeAddReact(Message message) {

    }
}
