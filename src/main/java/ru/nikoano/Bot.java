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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bot implements LongPollingSingleThreadUpdateConsumer, InitializingBean {
	private final TelegramBotsLongPollingApplication telegramBotsLongPollingApplication;
	@Value("${bot.token}")
	private String botToken;

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
		log.info("viaBot {}", viaBot);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		telegramBotsLongPollingApplication.registerBot(botToken, () -> TelegramUrl.DEFAULT_URL, new DefaultGetUpdatesGenerator(), this);
	}
}
