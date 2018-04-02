package net.siudek.sandbox.demo;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(topics = "myTopic1", partitions = 10)
public class DemoApplicationTests {

	@Autowired
	private KafkaTemplate<Object, Object> template;

	@Test
	@SneakyThrows
	public void testCalcOperation() {
		template.send(MessageBuilder
				.withPayload(new CalcOperation(1,2))
				.setHeader(KafkaHeaders.TOPIC, "myTopic1")
				.build());
		Thread.sleep(10_000);
	}

}
