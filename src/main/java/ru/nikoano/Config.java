package ru.nikoano;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class Config {

	@Value("${bot.token}")
	private String botToken;

	@Bean
	public TelegramBotsLongPollingApplication telegramBotsLongPollingApplication() {
		return new TelegramBotsLongPollingApplication();
	}

	@Bean
	public TelegramClient telegramClient() {
		return new OkHttpTelegramClient(botToken, TelegramUrl.DEFAULT_URL);
	}
}
