package com.ya.yaevent.domain.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ya.yaevent.domain.Event;
import com.ya.yaevent.domain.Favorite;
import com.ya.yaevent.domain.Group;
import com.ya.yaevent.domain.event.Participation;
import com.ya.yaevent.domain.notification.impl.EventNotificationFactory;
import com.ya.yaevent.domain.notification.impl.FavoriteNotificationFactory;
import com.ya.yaevent.domain.notification.impl.GroupNotificationFactory;
import com.ya.yaevent.domain.notification.impl.ParticipationNotificationFactory;
import com.ya.yaevent.domain.notification.impl.SurveyNotificationFactory;
import com.ya.yaevent.domain.survey.Survey;

public abstract class NotificationFactory {

	private static final Logger log = LoggerFactory.getLogger(SurveyNotificationFactory.class);

	public abstract Notification createNotification(Object object);

	// Fixit
	private final static NotificationFactory favoriteFactory = new FavoriteNotificationFactory();

	// Ya
	private final static NotificationFactory participationFactory = new ParticipationNotificationFactory();
	private final static NotificationFactory groupFactory = new GroupNotificationFactory();
	private final static NotificationFactory eventFactory = new EventNotificationFactory();
	private final static NotificationFactory surveyFactory = new SurveyNotificationFactory();

	public static NotificationFactory getInstance(Object object) {
		NotificationFactory result = null;

		if (object instanceof Group) {
			result = groupFactory;
		} else if (object instanceof Event) {
			result = eventFactory;
		} else if (object instanceof Survey) {
			result = surveyFactory;
		} else if (object instanceof Participation) {
			result = participationFactory;
		} else if (object instanceof Favorite) {
			result = favoriteFactory;
		}

		if (result != null) {
			log.debug("NotificationFactory.getInstance(Object object) result=" + result.getClass().getSimpleName());
		} else {
			log.debug("NotificationFactory.getInstance(Object object) result = notfound");
		}

		return result;
	}

}
