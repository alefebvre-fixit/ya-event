package com.ya.yaevent.service.impl.subscription;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ya.yaevent.domain.notification.Notification;
import com.ya.yaevent.service.SubscriptionService;

@Service
public class GroupSubscriptionService extends BaseSubscriptionService implements SubscriptionService {

	private final Logger log = LoggerFactory.getLogger(GroupSubscriptionService.class);

	@Override
	public Set<String> getSubscribers(Notification notification) {

		Set<String> result = new HashSet<String>();

		if (Notification.TYPE_GROUP.equals(notification.getType())) {

			// Followers want to receive notification about project they follow
			List<String> followers = getGroupService().findFollowerNames(notification.getGroupId());
			if (followers != null && followers.size() > 0) {
				result.addAll(followers);
			}
			log.debug("MongoGroupSubscriptionService.getSubscribers(Notification notification) result size ="
					+ result.size());

			// TODO Continue implementation
		} else {
			log.debug(
					"MongoGroupSubscriptionService.getSubscribers(Notification notification) notification type is not supported ="
							+ notification.getType());
		}

		return result;
	}

}
