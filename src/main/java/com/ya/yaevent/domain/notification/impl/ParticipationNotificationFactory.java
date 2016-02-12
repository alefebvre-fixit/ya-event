package com.ya.yaevent.domain.notification.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ya.yaevent.domain.event.Participation;
import com.ya.yaevent.domain.notification.Notification;
import com.ya.yaevent.domain.notification.NotificationFactory;

public class ParticipationNotificationFactory extends NotificationFactory {

	private final Logger log = LoggerFactory.getLogger(ParticipationNotificationFactory.class);

	@Override
	public Notification createNotification(Object object) {
		if (object instanceof Participation) {
			Participation participation = (Participation) object;
			Notification notification = new Notification();

			notification.setType(Notification.TYPE_PARTICIPATION);
			notification.setActor(participation.getUsername());
			notification.setEventId(participation.getEventId());
			notification.setEventName(participation.getEventName());
			notification.setNotificationDate(new Date());
			notification.setStatus(participation.getStatus());

			return notification;
		} else {
			log.debug("Object " + object.getClass().getName() + " is not an instance of "
					+ Participation.class.getSimpleName());
			return null;
		}
	}

}
