package hu.stepintomeetups.listeners;

import hu.stepintomeetups.action.RoleAssociationAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveListener;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;

@Slf4j
@ApplicationScoped
@DiscordMessageListener
@RequiredArgsConstructor
public class SkillCenterReactionListener implements ReactionAddListener, ReactionRemoveListener {

    private final RoleAssociationAction roleAssociationAction;
    private final DiscordApi discordApi;

    @Override
    public void onReactionAdd(ReactionAddEvent event) {
        discordApi.getMessageById(event.getMessageId(), event.getChannel())
                .thenCombine(discordApi.getUserById(event.getUserId()), (message, user) -> {
                    if (message.getAuthor().isBotUser() && !user.isBot()) {
                        roleAssociationAction.assignEmojiBasedRoleToUser(event.getEmoji(), user);
                    }
                    return CompletableFuture.completedFuture("Done");
                })
                .exceptionally(throwable -> {
                    log.warn("Error during processing reaction to message.");
                    return null;
                });

    }

    @Override
    public void onReactionRemove(ReactionRemoveEvent event) {
        discordApi.getMessageById(event.getMessageId(), event.getChannel())
                .thenCombine(discordApi.getUserById(event.getUserId()), (message, user) -> {
                    if (message.getAuthor().isBotUser() && !user.isBot()) {
                        roleAssociationAction.removeEmojiBasedRoleToUser(event.getEmoji(), user);
                    }
                    return CompletableFuture.completedFuture("Done");
                })
                .exceptionally(throwable -> {
                    log.warn("Error during processing reaction to message.");
                    return null;
                });
    }
}
