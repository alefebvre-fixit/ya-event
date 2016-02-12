package com.ya.yaevent.service.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ya.yaevent.domain.Favorite;
import com.ya.yaevent.domain.Group;
import com.ya.yaevent.domain.User;
import com.ya.yaevent.domain.user.UserIdentifier;
import com.ya.yaevent.repository.FavoriteRepository;
import com.ya.yaevent.repository.GroupRepository;
import com.ya.yaevent.service.GroupService;
import com.ya.yaevent.service.NotificationService;
import com.ya.yaevent.service.UserService;
import com.ya.yaevent.util.YaUtil;

/**
 * Service Implementation for managing Group.
 */
@Service
public class GroupServiceImpl implements GroupService{

    private final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);
    
    @Inject
    private GroupRepository groupRepository;
    
    @Inject
	FavoriteRepository favoriteRepository;

    @Inject
	private UserService userService;

    @Inject
	private NotificationService notificationService;    
    
    
	@Override
	public List<Group> findAll() {
        log.debug("Request to get all Groups");
		return groupRepository.findAll(new Sort(Sort.Direction.DESC,
				"creationDate"));
	}

	@Override
	public void delete(String id) {
        log.debug("Request to delete Group : {}", id);
		groupRepository.delete(id);
	}

	@Override
	public Group create(Group group) {
		log.debug("Request to create Group : {}", group);
		group.setCreationDate(ZonedDateTime.now());
		group.setModificationDate(ZonedDateTime.now());
		Group result = groupRepository.save(group);
		notificationService.publishNotification(group);
		return result;
	}

	@Override
    /**
     * Save a group.
     * @return the persisted entity
     */
	public Group save(Group group) {
		log.debug("Request to save Group : {}", group);
		group.setModificationDate(ZonedDateTime.now());
		Group result = groupRepository.save(group);
		notificationService.publishNotification(group);
		return result;
	}

	@Override
	public Group findOne(String id) {
		log.debug("Request to get Group : {}", id);
		return groupRepository.findOne(id);
	}

	@Override
	public int countByOwner(String username) {
		return groupRepository.countByUsername(username);
	}

	@Override
	public List<Group> findUserGroups(String username, int offset, int length) {
		List<Group> result = null;

		if (length > 0) {
			Page<Group> pages = groupRepository.findByUsername(username,
					new PageRequest(offset, length));
			result = pages.getContent();
		} else {
			result = groupRepository.findByUsername(username);
		}

		return result;
	}

	@Override
	public void follow(String username, String groupId) {
		Favorite favorite = new Favorite();
		favorite.setCreationDate(new Date());
		favorite.setUsername(username);
		favorite.setGroupId(groupId);

		int count = favoriteRepository.countByUsernameAndGroupId(username,
				groupId);

		if (count <= 0) {
			favoriteRepository.save(favorite);
		}
	}

	@Override
	public void unfollow(String username, String groupId) {
		List<Favorite> favorites = favoriteRepository.findByUsernameAndGroupId(
				username, groupId);

		if (favorites != null) {
			favoriteRepository.delete(favorites);
		}

	}

	@Override
	public List<String> findFollowingIds(String username) {
		List<String> result = new ArrayList<String>();

		List<Favorite> favorites = favoriteRepository.findByUsername(username);
		if (favorites != null) {
			for (Favorite favorite : favorites) {
				result.add(favorite.getGroupId());
			}
		}

		return result;
	}

	@Override
	public String findGroupOwner(String groupId) {
		// TODO Improve implementation by loading only the username

		String result = null;

		Group group = findOne(groupId);
		if (group != null) {
			result = group.getUsername();
		}

		return result;
	}

	@Override
	public int countFollowers(String groupId) {
		return favoriteRepository.countByGroupId(groupId);
	}

	@Override
	public List<String> findFollowerNames(String groupId) {
		// TODO Improve implementation by loading only the username

		List<String> result = new ArrayList<String>();

		List<Favorite> favorites = favoriteRepository.findByGroupId(groupId);
		if (favorites != null) {
			for (Favorite favorite : favorites) {
				if (favorite != null) {
					result.add(favorite.getUsername());
				}
			}
		}

		return result;
	}

	@Override
	public List<User> findFollowers(String groupId) {

		log.debug("MongoGroupService.groupFollowers(String groupId) groupId="
				+ groupId);
		List<User> result = new ArrayList<User>();

		List<Favorite> favorites = favoriteRepository.findByGroupId(groupId);

		if (favorites != null) {
			List<String> usernames = new ArrayList<String>();
			for (Favorite favorite : favorites) {
				usernames.add(favorite.getUsername());
			}
			result = userService.find(usernames);
		}

		if (result == null) {
			result = new ArrayList<User>();
		}

		return result;
	}

	@Override
	public int countFollowingSize(String username) {
		log.debug("MongoGroupService.groupFollowingSize(String username) username="
				+ username);
		return favoriteRepository.countByUsername(username);
	}

	@Override
	public List<Group> findFollowingGroups(String username) {
		List<Group> result = new ArrayList<Group>();

		List<String> ids = findFollowingIds(username);
		if (YaUtil.isNotEmpty(ids)) {
			Iterable<Group> groups = groupRepository.findAll(ids);
			for (Group group : groups) {
				result.add(group);
			}
		}

		return result;
	}

	@Override
	public List<User> findSponsors(String groupId) {

		log.debug("MongoGroupService.groupSponsors(String groupId) groupId="
				+ groupId);

		List<User> result = new ArrayList<User>();
		Group group = findOne(groupId);
		if (group != null) {
			result = userService.findByIdentifiers(group.getSponsors());
		}

		if (result == null) {
			result = new ArrayList<User>();
		}

		return result;
	}

	@Override
	public Group setSponsors(Group group, List<String> usernames) {

		group.setSponsors(new ArrayList<UserIdentifier>());

		List<User> sponsors = userService.find(usernames);
		if (YaUtil.isNotEmpty(sponsors)) {
			for (User User : sponsors) {
				group.getSponsors().add(User.getIdentifier());
			}
		}

		return save(group);
	}
    
    
    
    
}
