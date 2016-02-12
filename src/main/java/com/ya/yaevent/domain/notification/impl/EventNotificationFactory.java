package com.ya.yaevent.domain.notification.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ya.yaevent.domain.Event;
import com.ya.yaevent.domain.notification.Notification;
import com.ya.yaevent.domain.notification.NotificationFactory;

public class EventNotificationFactory extends NotificationFactory {

	private final Logger log = LoggerFactory.getLogger(EventNotificationFactory.class);

	@Override
	public Notification createNotification(Object object) {
		if (object instanceof Event) {
			Event event = (Event) object;
			Notification notification = new Notification();

			notification.setType(Notification.TYPE_EVENT);
			notification.setActor(event.getUsername());
			notification.setGroupId(event.getGroupId());
			notification.setEventName(event.getName());
			notification.setGroupName(event.getGroupName());
			notification.setEventId(event.getId());
			notification.setNotificationDate(new Date());

			notification.setStatus(event.getStatus());

			return notification;
		} else {
			log.debug(
					"Object " + object.getClass().getName() + " is not an instance of " + Event.class.getSimpleName());
			return null;
		}
	}

}
