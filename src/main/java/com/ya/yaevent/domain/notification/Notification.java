package com.ya.yaevent.domain.notification;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ya.yaevent.domain.user.UserIdentifier;

@Document(collection = "notification")
public class Notification implements Cloneable {

	public static final String TYPE_FAVORITE = "Favorite";
	public static final String TYPE_FOLLOWERS = "Follow";
	public static final String TYPE_COMMENTS = "Comment";
	
	public static final String TYPE_GROUP = "Group";
	public static final String TYPE_EVENT = "Event";
	public static final String TYPE_PARTICIPATION = "Participation";

	private String id;
	private String username;
	private String actor;
	private String type;
	private String subtype;
	private String status;
	private Date notificationDate;
	private String title;
	private String description;
	private String eventId;
	private String groupId;
	private String eventName;
	private String groupName;
	
	private UserIdentifier user;
	private UserIdentifier actorUser;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public Date getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public UserIdentifier getUser() {
		return user;
	}

	public void setUser(UserIdentifier user) {
		this.user = user;
	}

	public UserIdentifier getActorUser() {
		return actorUser;
	}

	public void setActorUser(UserIdentifier actorUser) {
		this.actorUser = actorUser;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
