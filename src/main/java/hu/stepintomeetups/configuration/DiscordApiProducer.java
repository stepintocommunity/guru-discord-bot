package hu.stepintomeetups.configuration;

import hu.stepintomeetups.listeners.DiscordMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.listener.GloballyAttachableListener;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * Producer class which is initiating a new {@link DiscordApi} instance, which can be used with injection in any CDI (ArC) managed class.
 */
@Slf4j
@ApplicationScoped
public class DiscordApiProducer {

    @Inject
    BotConfiguration botConfiguration;

    @Inject
    @DiscordMessageListener
    Instance<GloballyAttachableListener> listeners;

    private DiscordApi discordApi;

    @Produces
    public DiscordApi discordApi() {
        if (!botConfiguration.enabled()) {
            log.debug("Log is disabled, no DiscordApi instance will be made");
            return null;
        }
        if (discordApi == null) {
            initDiscordApi();
        }
        return discordApi;
    }

    private void initDiscordApi() {
        discordApi = new DiscordApiBuilder()
                .setToken(botConfiguration.token())
                .login()
                .join();
        listeners.forEach(discordApi::addListener);
        String botInvite = discordApi.createBotInvite(Permissions.fromBitmask(0).toBuilder()
                .setAllowed(
                        PermissionType.MANAGE_ROLES,
                        PermissionType.MANAGE_CHANNELS,
                        PermissionType.MANAGE_EMOJIS,
                        PermissionType.SEND_MESSAGES,
                        PermissionType.SEND_TTS_MESSAGES,
                        PermissionType.MANAGE_MESSAGES,
                        PermissionType.EMBED_LINKS,
                        PermissionType.ATTACH_FILE,
                        PermissionType.USE_EXTERNAL_EMOJIS,
                        PermissionType.ADD_REACTIONS
                )
                .build());
        log.info("Invite link for the bot:[{}]", botInvite);
    }

}
