package hu.stepintomeetups.listeners;

import hu.stepintomeetups.action.WelcomeUserAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import javax.enterprise.context.ApplicationScoped;

@Slf4j
@ApplicationScoped
@DiscordMessageListener
@RequiredArgsConstructor
public class PrivateMessageListener implements MessageCreateListener {

    private final WelcomeUserAction welcomeUserAction;

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessage().isPrivateMessage() && event.getMessageContent().startsWith("/help")) {
            welcomeUserAction.welcomeUser(event);
        }
    }
}
