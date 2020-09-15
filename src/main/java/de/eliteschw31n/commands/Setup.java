package de.eliteschw31n.commands;

import de.eliteschw31n.utils.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public class Setup extends Command {
    public Setup() {
        super("setup", "start setup", "Start the Setup, follow the Steps\n\nTo restart the setup add the Argument FIELD:reset:restart_setup", true, false, "Setup ");
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
