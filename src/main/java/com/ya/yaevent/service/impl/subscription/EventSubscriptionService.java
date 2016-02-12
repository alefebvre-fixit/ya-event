package com.ya.yaevent.service.impl.subscription;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ya.yaevent.domain.notification.Notification;
import com.ya.yaevent.service.SubscriptionService;

@Service
public class EventSubscriptionService extends BaseSubscriptionService implements SubscriptionService {

	@Override
	public Set<String> getSubscribers(Notification notification) {

		Set<String> result = new HashSet<String>();

		if (Notification.TYPE_EVENT.equals(notification.getType())) {

			// Followers want to receive notification about group they follow
			List<String> followers = getGroupService().findFollowerNames(notification.getGroupId());
			if (followers != null && followers.size() > 0) {
				result.addAll(followers);
			}

			// TODO Continue implementation
		}

		return result;
	}

}
