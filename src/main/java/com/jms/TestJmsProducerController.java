package com.jms;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestJmsProducerController {
	
    @Autowired
	private JmsTemplate jmsTemplate;
	
	private static final Logger LOG = LoggerFactory.getLogger(TestJmsProducerController.class);
	
	@GetMapping("async/produce/{number}")
	@Async
	public ResponseEntity<Void> sendJmsMessageAsync(@PathVariable Integer number) {
		return sendMessage(number);
	}
	
	@GetMapping("produce/{number}")
	public ResponseEntity<Void> sendJMSMessage(@PathVariable Integer number) {
		return sendMessage(number);
	}
	
	private ResponseEntity<Void> sendMessage(Integer number) {
		for (int i = 0; i< number; i++) {
			try {
				sendJMSMessage("Message number = " + i);
			}catch(JMSException e) {
				LOG.error("error sending message to queue", e);
			}
		}
		return ResponseEntity.accepted()
				.build();
	}

	private void sendJMSMessage(String jsonObj) throws JMSException {
		jmsTemplate.send("test-queue", session -> session.createTextMessage(jsonObj));
	}
}
