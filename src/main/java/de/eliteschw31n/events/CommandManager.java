package de.eliteschw31n.events;

import de.eliteschw31n.Main;
import de.eliteschw31n.utils.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CommandManager extends ListenerAdapter {
    private final Main printerCord;
    private final Set<Command> commands;

    public CommandManager(final Main printerCord) {
        this.printerCord = printerCord;
        this.commands = new HashSet<>();
        final Set<Class<? extends Command>> classes = new Reflections("de.eliteschw31n.commands")
                .getSubTypesOf(Command.class);
        for (Class<? extends Command> cmdClass : classes) {
            try {
                final Command command = cmdClass.getDeclaredConstructor().newInstance();
                command.setInstance(printerCord);
                if (commands.add(command)) {
                    System.out.println("Registered " + command.getCommand() + " Command");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        printerCord.getDiscordBot().addEventListener(this);
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            if (event.isFromGuild()) {
                if (!event.getGuild().getSelfMember().hasPermission(event.getGuild().getGuildChannelById(event.getChannel().getId()), Permission.MESSAGE_WRITE) && !event.getGuild().getSelfMember().hasPermission(event.getGuild().getGuildChannelById(event.getChannel().getId()), Permission.MESSAGE_ADD_REACTION)) {
                    return;
                }
            }
            String keyWord = "";
            if (event.getMessage().getEmbeds().size() != 0) {
                MessageEmbed.AuthorInfo author = event.getMessage().getEmbeds().get(0).getAuthor();
                if (author != null) {
                    keyWord = author.getName();
                } else {
                    String title = event.getMessage().getEmbeds().get(0).getTitle();
                    if (title != null) {
                        keyWord = title;
                    }
                }
            } else {
                keyWord = event.getMessage().getContentRaw();
            }
            if (keyWord == null) {
                return;
            }
            if (keyWord.length() == 0) {
                return;
            }
            for (Command command : this.commands) {
                if (keyWord.contains(command.getReactKeyWord())) {
                    command.executeAddReact(event.getMessage());
                    return;
                }
            }
            return;
        }
        final String content = event.getMessage().getContentRaw();
        final String commandString = content.split("\n")[0];
        String prefix = "!";
        if (!commandString.startsWith(prefix)) {
            return;
        }
        final String[] arguments = content.split(" ");
        final String input = arguments[0].replaceFirst(prefix, "");
        event.getChannel().sendTyping().queue();
        boolean commandFound = false;
        for (Command command : this.commands) {
            if ((command.getCommand()).equalsIgnoreCase(input)) {
                commandFound = true;
                if (event.getChannelType() == ChannelType.PRIVATE && !command.isPrivateCommand()) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " The Command is disabled for dm\n```" + prefix + command.getCommand() + "```").queue();
                    return;
                }
                //if (command.isAdminCommand() && !plugin.dcbot.checkadminormaster(event.getAuthor(), event.getGuild())) {
                //    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You are not allowed to execute the command\n```" + prefix + command.getCommand() + "```").queue();
                //    return;
                //}
                if (arguments.length >= 2) {
                    if (arguments[1].equalsIgnoreCase("help")) {
                        event.getChannel().sendMessage(command.getExtendedDescription(prefix, event.getMessage()).build()).queue();
                        return;
                    }
                }
                command.execute(Arrays.copyOfRange(arguments, 1, arguments.length), prefix, event.getMessage());
            }
        }
        if (!commandFound) {
            if (commandString.contains("<@") || commandString.contains("@everyone") || commandString.contains("@here")) {
                event.getChannel().sendMessage("You cant Tag anyone via me......").queue();
                return;
            }
            event.getChannel().sendMessage(event.getMessage().getAuthor().getAsMention() + "\nCommand not found:\n```" + commandString + "```please use " + prefix + "help for the command list.").queue();
        }
    }

    @Override
    public void onMessageReactionAdd(final MessageReactionAddEvent event) {
        Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        if (!message.getAuthor().isBot()) {
            return;
        }
        if (event.getUser() == null) {
            return;
        }
        if (event.getUser().isBot()) {
            return;
        }
        if (message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            String keyword = "";
            if (message.getEmbeds().size() != 0) {
                MessageEmbed.AuthorInfo author = message.getEmbeds().get(0).getAuthor();
                if (author != null) {
                    keyword = author.getName();
                } else {
                    String title = message.getEmbeds().get(0).getTitle();
                    if (title != null) {
                        keyword = title;
                    }
                }
            } else {
                keyword = message.getContentRaw();
            }
            if (keyword == null) {
                return;
            }
            if (keyword.length() == 0) {
                return;
            }
            if (keyword.contains("Command not found")) {
                return;
            }
            for (Command command : this.commands) {
                if (keyword.contains(command.getReactKeyWord())) {
                    if (command.isAdminCommand()) {
                        Guild guild = null;
                        if (event.isFromGuild()) {
                            guild = event.getGuild();
                        }
                    }
                    command.executeReact(event.getReactionEmote(), event.getUser(), message);
                    return;
                }
            }
        }
    }


    @Override
    public void onMessageReactionRemove(final MessageReactionRemoveEvent event) {
        Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        if (!message.getAuthor().isBot()) {
            return;
        }
        if (event.getUser() == null) {
            return;
        }
        if (event.getUser().isBot()) {
            return;
        }
        if (message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            String keyWord = "";
            if (message.getEmbeds().size() != 0) {
                MessageEmbed.AuthorInfo author = message.getEmbeds().get(0).getAuthor();
                if (author != null) {
                    keyWord = author.getName();
                } else {
                    String title = message.getEmbeds().get(0).getTitle();
                    if (title != null) {
                        keyWord = title;
                    }
                }
            } else {
                keyWord = message.getContentRaw();
            }
            if (keyWord == null) {
                return;
            }
            if (keyWord.contains("Command not found")) {
                return;
            }
            if (keyWord.length() == 0) {
                return;
            }
            for (Command command : this.commands) {
                if (keyWord.contains(command.getReactKeyWord())) {
                    if (command.isAdminCommand()) {
                        Guild guild = null;
                        if (event.isFromGuild()) {
                            guild = event.getGuild();
                        }
                    }
                    command.executeReact(event.getReactionEmote(), event.getUser(), message);
                    return;
                }
            }
        }
    }

    public Set<Command> getAvailableCommands() {
        return Collections.unmodifiableSet(commands);
    }
}
