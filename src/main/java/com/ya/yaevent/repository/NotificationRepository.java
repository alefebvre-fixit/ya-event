package com.ya.yaevent.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.yaevent.domain.notification.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {

	public Page<Notification> findByUsernameOrderByNotificationDateDesc(String username, Pageable pageable);

	public List<Notification> findByUsernameOrderByNotificationDateDesc(String username);

	public List<Notification> findByUsernameAndGroupId(String username, String groupId);

	public List<Notification> findByUsernameAndEventId(String username, String eventId);

	public List<Notification> findByUsername(String username);

}