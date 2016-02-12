package com.ya.yaevent.domain.event;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ya.yaevent.domain.Event;
import com.ya.yaevent.domain.User;
import com.ya.yaevent.domain.user.UserIdentifier;

@Document(collection = "participation")
public class Participation {

	public static final String STATUS_RSVP = "RSVP";
	public static final String STATUS_IN = "IN";
	public static final String STATUS_OUT = "OUT";

	@Id
	public String id;
	public double version = 0;
	private String eventId;
	private String eventName;
	private Date creationDate;
	private Date modificationDate;
	private String status;
	private String comment;
	private UserIdentifier user;
	
	@Deprecated
	private String username;


	public Participation() {
	}

	public Participation(Event event, User user) {
		this.eventId = event.getId();
		this.eventName = event.getName();
		this.creationDate = new Date();
		this.modificationDate = new Date();
		this.user = user.getIdentifier();
		this.status = STATUS_RSVP;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getVersion() {
		return version;
	}

	public void incrementVersion() {
		this.version++;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	@Deprecated
	public String getUsername() {
		return username;
	}

	@Deprecated
	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void reconcile(Participation original) {
		this.id = original.getId();
		this.version = original.getVersion();
		this.creationDate = original.getCreationDate();
		incrementVersion();
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public UserIdentifier getUser() {
		return user;
	}

	public void setUser(UserIdentifier user) {
		this.user = user;
	}

	
	
}
