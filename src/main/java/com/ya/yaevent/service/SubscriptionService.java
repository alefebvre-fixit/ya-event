package com.ya.yaevent.service;

import java.util.Set;

import com.ya.yaevent.domain.notification.Notification;

public interface SubscriptionService {

	public abstract Set<String> getSubscribers(Notification notification);

}
