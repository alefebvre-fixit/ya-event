package com.ya.yaevent.service.impl.subscription;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ya.yaevent.domain.notification.Notification;
import com.ya.yaevent.service.SubscriptionService;

@Service
public class FollowersSubscriptionService extends BaseSubscriptionService implements SubscriptionService {

	@Override
	public Set<String> getSubscribers(Notification notification) {

		Set<String> result = new HashSet<String>();

		if (Notification.TYPE_FOLLOWERS.equals(notification.getType())) {

		}
		// TODO To be implemented

		return result;
	}

}
