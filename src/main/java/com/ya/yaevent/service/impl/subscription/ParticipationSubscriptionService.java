package com.ya.yaevent.service.impl.subscription;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ya.yaevent.domain.notification.Notification;
import com.ya.yaevent.service.SubscriptionService;

@Service
public class ParticipationSubscriptionService extends BaseSubscriptionService implements SubscriptionService {

	private final Logger log = LoggerFactory.getLogger(ParticipationSubscriptionService.class);

	@Override
	public Set<String> getSubscribers(Notification notification) {

		log.debug("MongoParticipationSubscriptionService.getSubscribers(Notification notification");

		Set<String> result = new HashSet<String>();

		if (notification.getEventId() != null) {

			// Owner of the event want to receive notifications
			String owner = getEventService().findEventOwner(notification.getEventId());

			log.debug("MongoParticipationSubscriptionService.getSubscribers(Notification notification) owner=" + owner);

			if (owner != null) {
				result.add(owner);
			}

		}

		return result;
	}

}
