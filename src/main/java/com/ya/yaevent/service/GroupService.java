package com.ya.yaevent.service;

import java.util.List;

import com.ya.yaevent.domain.Group;
import com.ya.yaevent.domain.User;

/**
 * Service Interface for managing Group.
 */
public interface GroupService {

	/**
	 * Save a group.
	 * 
	 * @return the persisted entity
	 */
	public Group save(Group group);

	/**
	 * get all the groups.
	 * 
	 * @return the list of entities
	 */
	public List<Group> findAll();

	/**
	 * get the "id" group.
	 * 
	 * @return the entity
	 */
	public Group findOne(String id);

	/**
	 * delete the "id" group.
	 */
	public void delete(String id);

	public Group create(Group group);

	public int countByOwner(String username);

	public List<Group> findUserGroups(String username, int offset, int length);

	public void follow(String username, String groupId);

	public void unfollow(String username, String groupId);

	public List<String> findFollowingIds(String username);

	public List<Group> findFollowingGroups(String username);

	public int countFollowingSize(String username);

	public List<User> findFollowers(String groupId);

	public List<User> findSponsors(String groupId);

	public Group setSponsors(Group group, List<String> usernames);

	public List<String> findFollowerNames(String groupId);

	public int countFollowers(String groupId);

	public String findGroupOwner(String groupId);

}
