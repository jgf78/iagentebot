package com.julian.iagentebot.bot;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import com.julian.iagentebot.client.IAgenteClient;
import com.julian.iagentebot.conf.TelegramProperties;

@Component
public class IAgenteTelegramBot implements SpringLongPollingBot {

    private final TelegramProperties properties;
    private final IAgenteClient iaAgenteClient;
    private final TelegramClient telegramClient;

    public IAgenteTelegramBot(TelegramProperties properties,
                              IAgenteClient iaAgenteClient,
                              TelegramClient telegramClient) {
        this.properties = properties;
        this.iaAgenteClient = iaAgenteClient;
        this.telegramClient = telegramClient;
    }

    @Override
    public String getBotToken() {
        return properties.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this::handleUpdates;
    }

    private void handleUpdates(List<Update> updates) {

        for (Update update : updates) {

            if (!update.hasMessage() || !update.getMessage().hasText()) {
                continue;
            }

            Long chatId = update.getMessage().getChatId();
            String userId = update.getMessage().getFrom().getId().toString();
            String text = update.getMessage().getText();

            AtomicBoolean typing =
                    new AtomicBoolean(true);

            startTyping(chatId, typing);

            String response;

            try {

                response =
                        iaAgenteClient.chat(
                                userId,
                                text);

            } catch (Exception e) {

                response =
                        "Ha ocurrido un error procesando tu mensaje.";

            } finally {

                typing.set(false);
            }

            try {
                telegramClient.execute(
                        SendMessage.builder()
                                .chatId(chatId.toString())
                                .text(response)
                                .build()
                );
            } catch (Exception ignored) {}
        }
    }
    
    private CompletableFuture<Void> startTyping(
            Long chatId,
            AtomicBoolean running) {

        return CompletableFuture.runAsync(() -> {

            while (running.get()) {

                try {

                    telegramClient.execute(
                            SendChatAction.builder()
                                    .chatId(chatId.toString())
                                    .action("typing")
                                    .build()
                    );

                    Thread.sleep(4000);

                } catch (Exception e) {
                    break;
                }
            }
        });
    }
}
