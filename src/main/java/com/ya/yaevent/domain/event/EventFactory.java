package com.ya.yaevent.domain.event;

import java.time.ZonedDateTime;

import com.ya.yaevent.domain.Event;
import com.ya.yaevent.domain.Group;
import com.ya.yaevent.domain.User;

public class EventFactory {

	public static final Event createEvent(Group group, User user) {
		Event result = new Event();

		result.setUser(user.getIdentifier());
		result.setCreationDate(ZonedDateTime.now());
		result.setModificationDate(result.getCreationDate());
		result.setLocation(group.getLocation());
		// result.setCity(group.getCity());
		// result.setCountry(group.getCountry());
		result.setGroupId(group.getId());
		result.setGroupName(group.getName());
		result.setType(group.getType());

		result.setSponsors(group.getSponsors());

		if (!result.isSponsor(group.getUsername())) {
			result.isSponsor(group.getUsername());
		}
		if (result.isSponsor(user.getUsername())) {
			result.removeFromSponsors(user.getUsername());
		}

		return result;
	}

}
