package com.ya.yaevent.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ya.yaevent.domain.Group;
import com.ya.yaevent.domain.User;
import com.ya.yaevent.domain.group.GroupFactory;
import com.ya.yaevent.exception.EntityAuthorizationException;
import com.ya.yaevent.service.GroupService;
import com.ya.yaevent.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Group.
 */
@RestController
@RequestMapping("/api")
public class GroupResource extends YaResource {

	private final Logger log = LoggerFactory.getLogger(GroupResource.class);

	/**
	 * POST /groups -> Create a new group.
	 */
	@RequestMapping(value = "/groups", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Group> createGroup(@RequestBody Group group) throws URISyntaxException {
		log.debug("REST request to save Group : {}", group);
		if (group.getId() != null) {
			return ResponseEntity.badRequest()
					.headers(
							HeaderUtil.createFailureAlert("group", "idexists", "A new group cannot already have an ID"))
					.body(null);
		}

		group.setUser(getUser().getIdentifier());
		Group result = getGroupService().save(group);
		return ResponseEntity.created(new URI("/api/groups/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("group", result.getId().toString())).body(result);
	}

	/**
	 * PUT /groups -> Updates an existing group.
	 */
	@RequestMapping(value = "/groups", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Group> updateGroup(@RequestBody Group group) throws URISyntaxException {
		log.debug("REST request to update Group : {}", group);
		if (group.getId() == null) {
			return createGroup(group);
		}
		
		Group result = getGroupService().save(group);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("group", group.getId().toString()))
				.body(result);
	}

	/**
	 * GET /groups -> get all the groups.
	 */
	@RequestMapping(value = "/groups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Group> getAllGroups() {
		log.debug("REST request to get all Groups");
		return getGroupService().findAll();
	}

	/**
	 * GET /groups/:id -> get the "id" group.
	 */
	@RequestMapping(value = "/groups/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Group> getGroup(@PathVariable String id) {
		log.debug("REST request to get Group : {}", id);
		Group group = getGroupService().findOne(id);
		return Optional.ofNullable(group).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping("/groups")
	@Timed
	public List<Group> groups() {
		log.debug("debug GroupController.groups()");
		return getGroupService().findAll();
	}

	@RequestMapping("/groups/{groupId}")
	@Timed
	public Group group(@PathVariable String groupId) {
		log.debug("GroupController.group groupId =" + groupId);
		return getGroupService().findOne(groupId);
	}

	@RequestMapping(value = "/groups/{groupId}/follow", method = RequestMethod.POST)
	@Timed
	public List<String> follow(@PathVariable String groupId) {
		log.debug("GroupController.follow groupId =" + groupId);

		getGroupService().follow(getUserName(), groupId);

		return getGroupService().findFollowingIds(getUserName());
	}

	@RequestMapping(value = "/groups/{groupId}/unfollow", method = RequestMethod.POST)
	@Timed
	public List<String> unfollow(@PathVariable String groupId) {
		log.debug("GroupController.unfollow groupId =" + groupId);

		getGroupService().unfollow(getUserName(), groupId);

		return getGroupService().findFollowingIds(getUserName());
	}

	@RequestMapping("/users/{username}/groups")
	@Timed
	public List<Group> getUserGroups(@PathVariable String username) {
		log.debug("GroupController.groupByOwner username =" + username);

		return getGroupService().findUserGroups(username, -1, -1);
	}

	@RequestMapping("/groups/{groupId}/followers/size")
	@Timed
	public int followerSize(@PathVariable String groupId) {
		log.debug("GroupController.followerSize groupId =" + groupId);

		return getGroupService().countFollowers(groupId);
	}

	@RequestMapping("/groups/{groupId}/followers")
	@Timed
	public List<User> followers(@PathVariable String groupId) {
		log.debug("GroupController.followers groupId =" + groupId);

		return getGroupService().findFollowers(groupId);
	}

	@RequestMapping("/groups/{groupId}/sponsors")
	@Timed
	public List<User> sponsors(@PathVariable String groupId) {
		log.debug("GroupController.sponsors groupId =" + groupId);
		return getGroupService().findSponsors(groupId);
	}

	@RequestMapping("/users/{username}/groups/following/id")
	@Timed
	public List<String> followingIds(@PathVariable String username) {
		log.debug("GroupController.followingIds username =" + username);
		return getGroupService().findFollowingIds(username);
	}

	@RequestMapping("/users/{username}/groups/following/size")
	@Timed
	public int followingSize(@PathVariable String username) {
		log.debug("GroupController.followingSize username =" + username);
		return getGroupService().countFollowingSize(username);
	}

	@RequestMapping("/users/{username}/groups/following")
	@Timed
	public List<Group> following(@PathVariable String username) {
		log.debug("GroupController.following username =" + username);
		return getGroupService().findFollowingGroups(username);
	}

	/**
	 * DELETE /groups/:groupId -> delete the "groupId" group.
	 */
	@RequestMapping(value = "/groups/{groupId}", method = RequestMethod.DELETE)
	@Timed
	public ResponseEntity<Void> deleteGroup(@PathVariable String groupId) {
		log.debug("REST request to delete Group : {}", groupId);

		Group original = getGroupService().findOne(groupId);

		if (original == null)
			throw new EntityNotFoundException(Group.class.getSimpleName() + groupId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Group.class.getSimpleName(), groupId);

		log.debug("GroupController.deleteGroup groupId =" + groupId);
		getGroupService().delete(groupId);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("group", groupId.toString())).build();
	}

	@RequestMapping(value = "/groups", method = RequestMethod.POST)
	public Group create(@RequestBody Group group) {
		log.debug("GroupController.create()");

		if (group != null) {
			group.setUser(getUser().getIdentifier());
			group = getGroupService().save(group);
		}

		return group;
	}

	@RequestMapping(value = "/groups/{groupId}", method = RequestMethod.PUT)
	public Group update(@PathVariable String groupId, @RequestBody Group group) {
		log.debug("GroupController.update()");

		Group original = getGroupService().findOne(groupId);

		if (original == null)
			throw new EntityNotFoundException(Group.class.getSimpleName() + groupId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Group.class.getSimpleName(), groupId);

		group.setId(groupId);
		group.setUser(getUser().getIdentifier());

		return getGroupService().save(group);

	}

	@RequestMapping(value = "/groups/{groupId}/sponsors", method = RequestMethod.PUT)
	public Group update(@PathVariable String groupId, @RequestBody List<String> sponsors) {
		log.debug("GroupController.update()");

		Group original = getGroupService().findOne(groupId);

		if (original == null)
			throw new EntityNotFoundException(Group.class.getSimpleName() + groupId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Group.class.getSimpleName(), groupId);

		return getGroupService().setSponsors(original, sponsors);

	}

}
