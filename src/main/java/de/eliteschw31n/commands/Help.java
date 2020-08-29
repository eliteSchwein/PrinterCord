package de.eliteschw31n.commands;

import com.vdurmont.emoji.EmojiManager;
import de.eliteschw31n.utils.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class Help extends Command {
    public Help() {
        super("help", "shows this Help", "shows the Command List FIELD:command:get_Command_description", false, true, "Help ");
    }

    @Override
    public void execute(String[] args, String prefix, Message message) {
        if (args.length >= 1) {
            for (Command command : getMain().getCommandManager().getAvailableCommands()) {
                if (command.getCommand().equals(args[0])) {
                    if (command.isPrivateCommand() && !message.isFromGuild()) {
                        //if (command.isAdminCommand() && isPrivateCommand() && plugin.dcbot.checkadminormaster(message.getAuthor(), null)) {
                        //    message.getChannel().sendMessage(command.getExtendedDescription(prefix, message).build()).queue();
                        //    return;
                        //} else if (!command.isAdminCommand() && isPrivateCommand()) {
                        //    message.getChannel().sendMessage(command.getExtendedDescription(prefix, message).build()).queue();
                        //    return;
                        //}
                        message.getChannel().sendMessage(command.getExtendedDescription(prefix, message).build()).queue();
                    } else {
                        //if (command.isAdminCommand() && isPrivateCommand() && plugin.dcbot.checkadminormaster(message.getAuthor(), message.getGuild())) {
                        //    message.getChannel().sendMessage(command.getExtendedDescription(prefix, message).build()).queue();
                        //    return;
                        //} else if (!command.isAdminCommand() && isPrivateCommand()) {
                        //   message.getChannel().sendMessage(command.getExtendedDescription(prefix, message).build()).queue();
                        //   return;
                        //}
                        message.getChannel().sendMessage(command.getExtendedDescription(prefix, message).build()).queue();
                    }
                    return;
                }
            }
        }
        Guild guild = null;
        if (message.isFromGuild()) {
            guild = message.getGuild();
        }
        message.getChannel().sendMessage(getSite(2, false, message.getAuthor(), guild).build()).queue();
    }

    @Override
    public void executeReact(MessageReaction.ReactionEmote reactionEmote, User reactSender, Message message) {
        if (reactionEmote.isEmote()) {
            return;
        }
        Guild guild = null;
        if (message.isFromGuild()) {
            guild = message.getGuild();
        }
        if (reactionEmote.getEmoji().equalsIgnoreCase(EmojiManager.getForAlias("arrow_backward").getUnicode())) {
            int Site = Integer.parseInt(message.getEmbeds().get(0).getAuthor().getName().replace("Help ", "").replace("General ", "").replace("Admin ", "").split("/")[0]);
            message.editMessage(getSite(Site, false, reactSender, guild).build()).queue();
        }
        if (reactionEmote.getEmoji().equalsIgnoreCase(EmojiManager.getForAlias("arrow_forward").getUnicode())) {
            int Site = Integer.parseInt(message.getEmbeds().get(0).getAuthor().getName().replace("Help ", "").replace("General ", "").replace("Admin ", "").split("/")[0]);
            message.editMessage(getSite(Site, true, reactSender, guild).build()).queue();
        }
    }

    @Override
    public void executeAddReact(Message message) {
        message.addReaction(EmojiManager.getForAlias("arrow_backward").getUnicode()).queue();
        message.addReaction(EmojiManager.getForAlias("arrow_forward").getUnicode()).queue();
    }

    private EmbedBuilder getSite(int site, boolean next, User user, Guild guild) {
        EmbedBuilder embedBuilder = getEmbed(user);
        ArrayList<Command> adminCommands = new ArrayList<>();
        ArrayList<Command> generalCommands = new ArrayList<>();
        for (Command command : getMain().getCommandManager().getAvailableCommands()) {
            if (command.isAdminCommand()) {
                if (command.isPrivateCommand() && guild == null) {
                    adminCommands.add(command);
                } else if (!command.isPrivateCommand() && guild != null) {
                    adminCommands.add(command);
                }
            } else if (command.isPrivateCommand() && guild == null) {
                generalCommands.add(command);
            } else if (guild != null) {
                generalCommands.add(command);
            }
        }
        int maxsite = (int) Math.ceil((double) (generalCommands.size()) / 4.0) + (int) Math.ceil((double) (adminCommands.size()) / 4.0);
        if (site == 1 && !next) {
            site = maxsite;
        } else if (site == maxsite && next) {
            site = 1;
        } else if (next) {
            site = site + 1;
        } else {
            site = site - 1;
        }
        boolean admin = false;
        int backupSite = site;
        if (site > Math.ceil(generalCommands.size() / 4.0)) {
            admin = true;
            site = site - (int) Math.ceil(generalCommands.size() / 4.0);
        }
        String prefix = "";
        StringBuilder description = new StringBuilder();
        for (int i = ((site - 1) * 3) + (site) - 1; i <= 3 + ((site - 1) * 3) + (site) - 1; i++) {
            if (admin) {
                if (i < adminCommands.size()) {
                    description.append("**").append(prefix).append(adminCommands.get(i).getCommand()).append("**\n*").append(adminCommands.get(i).getDescription()).append("*\n\n");
                }
            } else {
                if (i < generalCommands.size()) {
                    description.append("**").append(prefix).append(generalCommands.get(i).getCommand()).append("**\n*").append(generalCommands.get(i).getDescription()).append("*\n\n");
                }
            }
        }
        embedBuilder.setDescription(description.toString());
        if (admin) {
            embedBuilder.setAuthor("Help Admin " + backupSite + "/" + maxsite);
            embedBuilder.setThumbnail(getMain().getMainConfiguration().getHelpAdminEmbed());
        } else {
            embedBuilder.setAuthor("Help General " + backupSite + "/" + maxsite);
            embedBuilder.setThumbnail(getMain().getMainConfiguration().getHelpEmbed());
        }
        return embedBuilder;
    }
}
