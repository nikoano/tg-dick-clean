package ru.nikoano;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Configuration
public class Config {
	@Bean
	public TelegramBotsLongPollingApplication telegramBotsLongPollingApplication() {
		return new TelegramBotsLongPollingApplication();
	}
}
