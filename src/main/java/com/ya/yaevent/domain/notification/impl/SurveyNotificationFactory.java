package com.ya.yaevent.domain.notification.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ya.yaevent.domain.notification.Notification;
import com.ya.yaevent.domain.notification.NotificationFactory;
import com.ya.yaevent.domain.survey.Survey;

public class SurveyNotificationFactory extends NotificationFactory {

	private final Logger log = LoggerFactory.getLogger(SurveyNotificationFactory.class);
	
	@Override
	public Notification createNotification(Object object) {
		if (object instanceof Survey) {
			Survey survey = (Survey) object;
			Notification notification = new Notification();

			notification.setType(Notification.TYPE_EVENT);
			notification.setActor(survey.getUsername());
			notification.setEventId(survey.getId());
			notification.setNotificationDate(new Date());

			notification.setStatus(survey.getStatus());

			return notification;
		} else {
			log.debug("Object " + object.getClass().getName()
					+ " is not an instance of " + Survey.class.getSimpleName());
			return null;
		}
	}

}
