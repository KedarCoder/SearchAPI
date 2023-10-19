package com.concerto.crud.application.init;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class WebMVCConfiguration {
	
	private static MessageSource messageSource;
	
	@Bean
	public MessageSource messageSource(){
		ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
		resourceBundleMessageSource.setBasenames("i18n/message_en");
		setMessageSource(resourceBundleMessageSource);
		return resourceBundleMessageSource;
	}

	public static MessageSource getMessageSource() {
		return messageSource;
	}

	public static void setMessageSource(MessageSource messageSource) {
		WebMVCConfiguration.messageSource = messageSource;
	}

}
