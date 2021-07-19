package hu.stepintomeetups.commands;

import com.vdurmont.emoji.EmojiParser;
import hu.stepintomeetups.skills.action.AvailableCommandsAction;
import lombok.RequiredArgsConstructor;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.CertainMessageEvent;

import javax.enterprise.context.ApplicationScoped;

/**
 * Command which is handling available command listing.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class ListCommandsCommand extends GuruBotCommand {

    private final AvailableCommandsAction availableCommandsAction;

    @Override
    public String getCommand() {
        return "commands";
    }

    @Override
    public void handleMessage(CertainMessageEvent event) {
        Message message = event.getMessage();
        message.getAuthor().asUser().ifPresent(availableCommandsAction::sendAllCommandsToUser);
        message.addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
    }
}
