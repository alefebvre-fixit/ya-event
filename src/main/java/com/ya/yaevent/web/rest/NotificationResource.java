package com.ya.yaevent.web.rest;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ya.yaevent.domain.notification.Notification;

/**
 * REST controller for managing Group.
 */
@RestController
@RequestMapping("/api")
public class NotificationResource extends YaResource {

	private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

	@RequestMapping("/notifications")
	public List<Notification> notifications(Principal principal) {

		log.debug("NotificationAPIController.notifications");
		log.debug("Principal principal " + principal.getName());
		return getNotificationService().findUserNotifications(getUserName(), 0, 0);
	}

	@RequestMapping(value = "/groups/{groupId}/notifications/acknowledge", method = RequestMethod.POST)
	public void acknowledgeGroupNotifications(@PathVariable String groupId) {
		log.debug("NotificationAPIController.acknowledgeGroupNotifications groupId" + groupId);

		getNotificationService().acknowledgeGroupNotifications(groupId, getUserName());
	}

	@RequestMapping(value = "/events/{eventId}/notifications/acknowledge", method = RequestMethod.POST)
	public void acknowledgeEventNotifications(@PathVariable String eventId) {
		log.debug("NotificationAPIController.acknowledgeEventNotifications eventId" + eventId);

		getNotificationService().acknowledgeEventNotifications(eventId, getUserName());

	}

	@RequestMapping(value = "/notifications/{notificationId}/acknowledge", method = RequestMethod.POST)
	public void acknowledgeNotification(@PathVariable String notificationId) {
		log.debug("NotificationAPIController.acknowledgeNotification notificationId" + notificationId);

		getNotificationService().acknowledgeNotification(notificationId);
	}

	@RequestMapping(value = "/notifications/acknowledge", method = RequestMethod.POST)
	public void acknowledgeNotifications() {
		log.debug("NotificationAPIController.acknowledgeNotifications");

		getNotificationService().acknowledgeNotifications(getUserName());
	}

	@RequestMapping(value = "/notifications", method = RequestMethod.DELETE)
	public void deleteNotifications() {
		log.debug("NotificationAPIController.deleteNotifications");

		getNotificationService().acknowledgeNotifications(getUserName());
	}

	@RequestMapping(value = "/notifications/{notificationId}", method = RequestMethod.DELETE)
	public void deleteNotification(@PathVariable String notificationId) {
		log.debug("NotificationAPIController.deleteNotification notificationId=" + notificationId);

		getNotificationService().acknowledgeNotification(notificationId);
	}

}
