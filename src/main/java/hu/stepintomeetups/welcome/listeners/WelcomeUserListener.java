package hu.stepintomeetups.welcome.listeners;

import hu.stepintomeetups.welcome.action.WelcomeUserAction;
import hu.stepintomeetups.listeners.DiscordMessageListener;
import lombok.RequiredArgsConstructor;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

import javax.enterprise.context.ApplicationScoped;

/**
 * Listener which listens to member join events.
 */
@ApplicationScoped
@DiscordMessageListener
@RequiredArgsConstructor
public class WelcomeUserListener implements ServerMemberJoinListener {

    private final WelcomeUserAction welcomeUserAction;

    @Override
    public void onServerMemberJoin(ServerMemberJoinEvent event) {
        welcomeUserAction.welcomeUser(event.getUser());
    }

}
