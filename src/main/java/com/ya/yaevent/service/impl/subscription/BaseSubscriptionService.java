package com.ya.yaevent.service.impl.subscription;

import javax.inject.Inject;

import com.ya.yaevent.service.EventService;
import com.ya.yaevent.service.GroupService;

public abstract class BaseSubscriptionService {

	@Inject
	private EventService eventService;

	protected EventService getEventService() {
		return eventService;
	}

	@Inject
	private GroupService groupService;

	protected GroupService getGroupService() {
		return groupService;
	}

}
