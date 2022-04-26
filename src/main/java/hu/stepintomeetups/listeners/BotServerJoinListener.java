package hu.stepintomeetups.listeners;

import lombok.extern.slf4j.Slf4j;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.server.ServerJoinListener;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@Slf4j
@ApplicationScoped
@DiscordMessageListener
public class BotServerJoinListener implements ServerJoinListener {

    @Override
    public void onServerJoin(ServerJoinEvent serverJoinEvent) {
        SlashCommand.with("i-know", "Hozzádrendel egy programnyelvhez tartozó szerepkört és a csatornáit", List.of())
                .createForServer(serverJoinEvent.getServer())
                .thenAccept(slashCommand -> log.info("Command [{}] registered", slashCommand))
                .exceptionally(throwable -> {
                    log.error("Shit happened", throwable);
                    return null;
                });

        serverJoinEvent.getServer().addSlashCommandCreateListener(event -> {
            log.info("Event coming in:[{}]", event);
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            slashCommandInteraction.respondLater();
            slashCommandInteraction.createFollowupMessageBuilder()
                    .setContent("Pong")
                    .send();
        });
    }
}
