package hu.stepintomeetups.listeners;

import hu.stepintomeetups.action.WelcomeUserAction;
import lombok.RequiredArgsConstructor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@DiscordMessageListener
@RequiredArgsConstructor
public class WelcomeUserListener implements ServerMemberJoinListener, MessageCreateListener {

    private final WelcomeUserAction welcomeUserAction;

    @Override
    public void onServerMemberJoin(ServerMemberJoinEvent event) {
        welcomeUserAction.welcomeUser(event.getUser());
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

    }
}
