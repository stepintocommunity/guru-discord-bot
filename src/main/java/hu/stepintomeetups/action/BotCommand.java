package hu.stepintomeetups.action;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BotCommand {

    private String command;
    private String description;

}
