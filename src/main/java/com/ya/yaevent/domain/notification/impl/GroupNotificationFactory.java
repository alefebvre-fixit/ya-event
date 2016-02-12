package com.ya.yaevent.domain.notification.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ya.yaevent.domain.Group;
import com.ya.yaevent.domain.notification.Notification;
import com.ya.yaevent.domain.notification.NotificationFactory;

public class GroupNotificationFactory extends NotificationFactory {

	private final Logger log = LoggerFactory.getLogger(GroupNotificationFactory.class);
	
	@Override
	public Notification createNotification(Object object) {
		if (object instanceof Group) {
			Group group = (Group) object;
			Notification notification = new Notification();

			notification.setType(Notification.TYPE_GROUP);
			notification.setActor(group.getUsername());
			notification.setGroupId(group.getId());
			notification.setGroupName(group.getName());
			notification.setNotificationDate(new Date());

			notification.setStatus(group.getStatus());

			return notification;
		} else {
			log.debug("Object " + object.getClass().getName()
					+ " is not an instance of " + Group.class.getSimpleName());
			return null;
		}
	}

}
