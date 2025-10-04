package ru.nikoano;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bot implements LongPollingSingleThreadUpdateConsumer, InitializingBean {
	private final TelegramBotsLongPollingApplication telegramBotsLongPollingApplication;
	private final TelegramClient telegramClient;
	@Value("${bot.token}")
	private String botToken;

	@Value("${ban.bots}")
	private String[] banBots;
	private Set<String> banBotsSet;

	@Override
	public void consume(Update update) {
		if (!update.hasMessage()) {
			return;
		}

		Message message = update.getMessage();
		if (!message.hasViaBot()) {
			return;
		}

		User viaBot = message.getViaBot();
		if (banBotsSet.contains(viaBot.getUserName())) {
			try {
				telegramClient.execute(new DeleteMessage(
						String.valueOf(message.getChatId()),
						message.getMessageId()
				));
				log.info("deleted msg with {} from {} in chat {}", viaBot.getUserName(), userToString(message.getFrom()), message.getChatId());
			} catch (TelegramApiException e) {
				log.error("delete msg error", e);
			}
		}
	}

	private String userToString(User user) {
		return "User(id=%s, firstName=%s, lastName=%s, userName=%s)"
				.formatted(user.getId(), user.getFirstName(), user.getLastName(), user.getUserName());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		banBotsSet = new HashSet<>(Arrays.asList(banBots));
		telegramBotsLongPollingApplication.registerBot(botToken, () -> TelegramUrl.DEFAULT_URL, new DefaultGetUpdatesGenerator(), this);
	}
}
