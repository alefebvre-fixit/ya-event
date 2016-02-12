package com.ya.yaevent.domain.notification.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ya.yaevent.domain.Favorite;
import com.ya.yaevent.domain.notification.Notification;
import com.ya.yaevent.domain.notification.NotificationFactory;

public class FavoriteNotificationFactory extends NotificationFactory {

	private final Logger log = LoggerFactory.getLogger(FavoriteNotificationFactory.class);

	@Override
	public Notification createNotification(Object object) {
		if (object instanceof Favorite) {
			Favorite favorite = (Favorite) object;
			Notification notification = new Notification();

			notification.setType(Notification.TYPE_FAVORITE);
			notification.setActor(favorite.getUsername());
			notification.setGroupId(favorite.getGroupId());
			notification.setNotificationDate(new Date());

			return notification;
		} else {
			log.debug("Object " + object.getClass().getName() + " is not an instance of "
					+ Favorite.class.getSimpleName());
			return null;
		}
	}

}
