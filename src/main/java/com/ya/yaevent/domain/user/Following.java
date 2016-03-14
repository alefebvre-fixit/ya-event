package com.ya.yaevent.domain.user;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Following")
public class Following {

	private String followee;
	private String follower;
	private Date creationDate;

	@Id
	public String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFollowee() {
		return followee;
	}

	public void setFollowee(String followee) {
		this.followee = followee;
	}

	public String getFollower() {
		return follower;
	}

	public void setFollower(String follower) {
		this.follower = follower;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "Following [followee=" + followee + ", follower=" + follower + ", creationDate=" + creationDate + ", id="
				+ id + "]";
	}

	public static final Following create(String followee, String follower) {
		Following result = new Following();
		result.setCreationDate(new Date());
		result.setFollowee(followee);
		result.setFollower(follower);
		return result;
	}

}
