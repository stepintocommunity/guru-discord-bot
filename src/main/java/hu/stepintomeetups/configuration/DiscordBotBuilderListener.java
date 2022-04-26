package hu.stepintomeetups.configuration;

import io.quarkus.runtime.Startup;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.DiscordApi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


/**
 * Application scoped bean which is being initiated when the application starts.
 */
@Slf4j
@Startup
@ApplicationScoped
public class DiscordBotBuilderListener {

    @Inject
    DiscordApi discordApi;

    @PostConstruct
    public void init() {
        log.info("Starting application which triggers the bot initiation.");
    }

    @PreDestroy
    public void destroy() {
        if (discordApi != null) {
            discordApi.disconnect();
        }
    }

}
