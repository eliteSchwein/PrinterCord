package de.eliteschw31n.events;

import de.eliteschw31n.utils.Event;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

public class BotAddEvent extends Event {
    public BotAddEvent() {
        super("Bot Add");
    }

    @Override
    public void execute() {
        getMain().getDiscordBot().addEventListener(this);
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        MessageChannel channel = event.getGuild().getDefaultChannel();
        channel.sendMessage("Hey \uD83D\uDC4B \n" +
                "\n" +
                "Thanks for adding me!\n" +
                "To start the setup use !setup and follow the steps").queue();
    }
}
