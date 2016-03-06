package com.ya.yaevent.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.yaevent.domain.user.UserIdentifier;
import com.ya.yaevent.util.YaUtil;

/**
 * A Group.
 */

@Document(collection = "group")
public class Group implements Serializable {
	
	public static final String STATUS_NEW = "New";
	public static final String STATUS_PUBLISHED = "Published";
	public static final String STATUS_DRAFT = "Draft";

	
	public static final String TYPE_COFEE = "Coffee";
	
	@Id
	private String id;

	@Field("creation_date")
	private ZonedDateTime creationDate;

	@Field("modification_date")
	private ZonedDateTime modificationDate;

	@Field("version")
	private Double version = 0.;

	@Field("status")
	private String status = STATUS_NEW;

	@Field("event_size")
	private Integer eventSize = 0;

	@Field("type")
	private String type = TYPE_COFEE;

	@Field("name")
	private String name;

	@Field("description")
	private String description;

	@Field("location")
	private String location;

	@Field("username")
	private String username;

	@Field("user")
	private UserIdentifier user;

	@Field("sponsors")
	private List<UserIdentifier> sponsors = new ArrayList<UserIdentifier>();

	@Field("city")
	private String city;

	@Field("country")
	private String country;

	public UserIdentifier getUser() {
		return user;
	}

	public void setUser(UserIdentifier user) {
		this.user = user;
		this.username = user.getUsername();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public void incrementVersion() {
		this.version++;
	}

	@JsonIgnore
	public void incrementEventSize() {
		eventSize++;
	}

	@JsonIgnore
	public void decrementEventSize() {
		eventSize--;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Group group = (Group) o;
		return Objects.equals(id, group.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Group{" + "id=" + id + ", creationDate='" + creationDate + "'" + ", modificationDate='"
				+ modificationDate + "'" + ", version='" + version + "'" + ", status='" + status + "'" + ", eventSize='"
				+ eventSize + "'" + ", type='" + type + "'" + ", name='" + name + "'" + ", description='" + description
				+ "'" + ", location='" + location + "'" + '}';
	}
}
