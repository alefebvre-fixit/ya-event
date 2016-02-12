package com.ya.yaevent.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.ya.yaevent.domain.event.Participation;
import com.ya.yaevent.domain.user.UserIdentifier;
import com.ya.yaevent.util.YaUtil;

/**
 * A Event.
 */

@Document(collection = "event")
public class Event implements Serializable {

	@Id
	private String id;

	@Field("creation_date")
	private ZonedDateTime creationDate;

	@Field("modification_date")
	private ZonedDateTime modificationDate;

	@Field("version")
	private Double version;

	@Field("status")
	private String status;

	@Field("event_size")
	private Integer eventSize;

	@Field("type")
	private String type;

	@Field("name")
	private String name;

	@Field("description")
	private String description;

	@Field("location")
	private String location;

	@Field("user_name")
	private String username;

	@Field("group_name")
	private String groupName;

	@Field("group_id")
	private String groupId;

	@Field("date")
	private Date date;

	private UserIdentifier user;

	private List<UserIdentifier> sponsors = new ArrayList<UserIdentifier>();

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserIdentifier getUser() {
		return user;
	}

	public void setUser(UserIdentifier user) {
		this.user = user;
	}

	public ZonedDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(ZonedDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public ZonedDateTime getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(ZonedDateTime modificationDate) {
		this.modificationDate = modificationDate;
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getEventSize() {
		return eventSize;
	}

	public void setEventSize(Integer eventSize) {
		this.eventSize = eventSize;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<UserIdentifier> getSponsors() {
		return sponsors;
	}

	public void setSponsors(List<UserIdentifier> sponsors) {
		this.sponsors = sponsors;
	}

	public boolean canUpdate(String actor) {

		if (actor != null) {
			if (actor.equals(getUsername())) {
				return true;
			}

			if (isSponsor(actor)) {
				return true;
			}

		}

		return false;
	}
	
	public boolean isSponsor(UserIdentifier identifier) {
		if (identifier != null) {
			return isSponsor(identifier.getUsername());
		}
		return false;
	}

	public boolean isSponsor(String username) {
		if (YaUtil.isNotEmpty(sponsors)) {
			for (UserIdentifier userIdentifier : sponsors) {
				if (userIdentifier.getUsername().equals(username)) {
					return true;
				}
			}
		}
		return false;
	}

	public void removeFromSponsors(String username) {
		if (YaUtil.isNotEmpty(sponsors)) {
			for (UserIdentifier userIdentifier : sponsors) {
				if (userIdentifier.getUsername().equals(username)) {
					sponsors.remove(userIdentifier);
				}
			}
		}
	}
	
	public boolean accept(Participation participation) {
		return true;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Event event = (Event) o;
		return Objects.equals(id, event.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Event{" + "id=" + id + ", creationDate='" + creationDate + "'" + ", modificationDate='"
				+ modificationDate + "'" + ", version='" + version + "'" + ", status='" + status + "'" + ", eventSize='"
				+ eventSize + "'" + ", type='" + type + "'" + ", name='" + name + "'" + ", description='" + description
				+ "'" + ", location='" + location + "'" + '}';
	}
}
