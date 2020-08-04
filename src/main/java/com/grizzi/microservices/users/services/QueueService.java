package com.grizzi.microservices.users.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
	private static final Logger log = LoggerFactory.getLogger(QueueService.class);

	@Value("${queue.enabled}")
	private boolean queueEnabled;

	@Autowired
	private JmsTemplate jmsTemplate;

	public void send(String destination, String message) {
		try {
			if (queueEnabled) {
				jmsTemplate.convertAndSend(destination, message);
			} else {
				log.debug("Queue transfer is disabled. To enable it, set to true the value of the ACTIVE_QUEUE_ENABLED env variable");
			}
		} catch (Exception e) {
			log.error("Exception while sending a message", e);
		}
	}
}
