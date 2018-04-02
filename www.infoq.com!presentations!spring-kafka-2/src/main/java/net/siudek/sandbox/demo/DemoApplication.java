package net.siudek.sandbox.demo;

import lombok.Value;
import lombok.val;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.converter.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public MessageConverter messageConverter() {
		val typeMapper = new DefaultJackson2JavaTypeMapper();
		typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
		typeMapper.addTrustedPackages("set.siudek");

		val converter = new StringJsonMessageConverter();
		converter.setTypeMapper(typeMapper);
		return converter;
	}

	@Component
	@KafkaListener(id="group", topics = "myTopic1")
	static class MyListener {

		@KafkaHandler
		public void calcListener(CalcOperation item, Consumer<?, ?> a) {
			ListenerContainerIdleEvent
		}
	}

	@RestController()
	static class MyController {

	}
}

@Value
class CalcOperation {
	private int value1;
	private int value2;
}

