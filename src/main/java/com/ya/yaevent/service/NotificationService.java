package com.ya.yaevent.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ya.yaevent.domain.notification.Notification;

@Service
public interface NotificationService {

	public Notification findOne(String notificationId);

	public Notification save(Notification notification);

	public void delete(String notificationId);

	public void deleteNotifications(List<Notification> notifications);

	public List<Notification> findUserNotifications(String username, int offset, int length);

	public void publishNotification(Object o);

	public List<Notification> findGroupNotifications(String groupId, String username);

	public List<Notification> findEventNotifications(String eventId, String username);

	public void acknowledgeEventNotifications(String eventId, String username);

	public void acknowledgeGroupNotifications(String groupId, String username);

	public void acknowledgeNotification(String notificationId);

	public void acknowledgeNotifications(String username);

	public List<Notification> findAll();

}
