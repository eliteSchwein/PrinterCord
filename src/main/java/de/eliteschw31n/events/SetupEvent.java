package de.eliteschw31n.events;

import de.eliteschw31n.utils.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SetupEvent extends Event {
    public SetupEvent() {
        super("Setup");
    }

    @Override
    public void execute() {
        getMain().getDiscordBot().addEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
    }
}
