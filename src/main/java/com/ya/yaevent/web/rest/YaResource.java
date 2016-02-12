package com.ya.yaevent.web.rest;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.ya.yaevent.domain.User;
import com.ya.yaevent.service.EventService;
import com.ya.yaevent.service.GroupService;
import com.ya.yaevent.service.NotificationService;
import com.ya.yaevent.service.SurveyService;
import com.ya.yaevent.service.UserService;

public class YaResource {

	public static final String SESSION_ATTRIBUTE_USERNAME = "username";
	public static final String SESSION_ATTRIBUTE_ACCESS_TOKEN = "access_token";

	@Inject
	private GroupService groupService;

	@Inject
	private UserService userService;

	@Inject
	private EventService eventService;

	@Inject
	private SurveyService surveyService;

	@Inject
	private NotificationService notificationService;

	protected GroupService getGroupService() {
		return groupService;
	}

	protected NotificationService getNotificationService() {
		return notificationService;
	}

	protected UserService getUserService() {
		return userService;
	}

	protected EventService getEventService() {
		return eventService;
	}

	protected User getUser() {
		return getUserService().findOne(getUserName());
	}

	protected SurveyService getSurveyService() {
		return surveyService;
	}

	protected static String getUserName() {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getUsername();
	}

}
