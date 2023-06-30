package com.example.exampleplugin;

import com.example.exampleplugin.model.TestEntity;
import com.example.exampleplugin.model.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.bdavies.babblebot.api.command.Command;
import net.bdavies.babblebot.api.command.CommandExample;
import net.bdavies.babblebot.api.command.CommandParam;
import net.bdavies.babblebot.api.command.ICommandContext;
import net.bdavies.babblebot.api.obj.message.discord.DiscordMessage;
import net.bdavies.babblebot.api.obj.message.discord.embed.EmbedMessage;
import net.bdavies.babblebot.api.plugins.Plugin;

/**
 * Edit me
 *
 * @author me@bdavies (Ben Davies)
 * @since 1.0.0
 */
@Plugin
@RequiredArgsConstructor
@Slf4j
public class ExamplePlugin {
    private final TestRepository repository;

    @Command(description = "Ping the bot!")
    public String ping(DiscordMessage message, ICommandContext context) {
        return "pong!";
    }

    @Command(aliases = "addtest", description = "Add a test name to the system")
    @CommandParam(value = "name", canBeEmpty = false, optional = false, exampleValue = "john")
    @CommandExample("${commandName} -name=John")
    public EmbedMessage addTest(DiscordMessage message, ICommandContext context) {
        val e = repository.saveAndFlush(TestEntity.builder()
                .name(context.getParameter("name"))
                .build());

        return EmbedMessage.builder()
                .title("Saved")
                .description(e.toString())
                .build();
    }

    @Command(aliases = "listtest", description = "list all the test names")
    public EmbedMessage listTest(DiscordMessage message, ICommandContext context) {
        val tests = repository.findAll();
        val em = EmbedMessage.builder()
                .title("All Tests")
                .build();
        tests.forEach(t -> em.addField(t.getName(), String.valueOf(t.getId()), false));

        return em;
    }
}

