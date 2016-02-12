package com.ya.yaevent.domain.group;

import java.time.ZonedDateTime;
import java.util.Date;

import com.ya.yaevent.domain.Group;
import com.ya.yaevent.domain.User;

public class GroupFactory {

	public static final Group createGroup(User user) {
		Group result = new Group();

		result.setCreationDate(ZonedDateTime.now());
		result.setModificationDate(result.getCreationDate());
		result.setUser(user.getIdentifier());
		result.setCity(user.getProfile().getCity());
		result.setCountry(user.getProfile().getCountry());

		return result;
	}

}
