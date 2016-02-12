package com.ya.yaevent.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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
import com.ya.yaevent.domain.Event;
import com.ya.yaevent.domain.User;
import com.ya.yaevent.domain.event.EventComment;
import com.ya.yaevent.domain.event.EventFactory;
import com.ya.yaevent.domain.event.EventTimeline;
import com.ya.yaevent.domain.event.Participation;
import com.ya.yaevent.domain.event.ParticipationSummary;
import com.ya.yaevent.exception.EntityAuthorizationException;
import com.ya.yaevent.exception.EntityNotFoundException;
import com.ya.yaevent.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Event.
 */
@RestController
@RequestMapping("/api")
public class EventResource extends YaResource {

	private final Logger log = LoggerFactory.getLogger(EventResource.class);

	/**
	 * POST /events -> Create a new event.
	 */
	@RequestMapping(value = "/events", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Event> createEvent(@RequestBody Event event) throws URISyntaxException {
		log.debug("REST request to save Event : {}", event);
		if (event.getId() != null) {
			return ResponseEntity.badRequest()
					.headers(
							HeaderUtil.createFailureAlert("event", "idexists", "A new event cannot already have an ID"))
					.body(null);
		}
		Event result = getEventService().save(event);
		return ResponseEntity.created(new URI("/api/events/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("event", result.getId().toString())).body(result);
	}

	/**
	 * PUT /events -> Updates an existing event.
	 */
	@RequestMapping(value = "/events", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Event> updateEvent(@RequestBody Event event) throws URISyntaxException {
		log.debug("REST request to update Event : {}", event);
		if (event.getId() == null) {
			return createEvent(event);
		}
		Event result = getEventService().save(event);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("event", event.getId().toString()))
				.body(result);
	}

	/**
	 * GET /events -> get all the events.
	 */
	@RequestMapping(value = "/events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Event> getAllEvents() {
		log.debug("REST request to get all Events");
		return getEventService().findAll();
	}

	/**
	 * GET /events/:id -> get the "id" event.
	 */
	@RequestMapping(value = "/events/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Event> getEvent(@PathVariable String id) {
		log.debug("REST request to get Event : {}", id);
		Event event = getEventService().findOne(id);
		return Optional.ofNullable(event).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping("/events")
	public List<Event> events() {
		log.debug("EventAPIController.events()");
		return getEventService().findAll();
	}

	@RequestMapping("/events/timeline")
	public EventTimeline getEventTimeline() {
		log.debug("EventAPIController.getEventTimeline()");
		return getEventService().findEventTimeline();
	}

	@RequestMapping("/groups/{groupId}/events/timeline")
	public EventTimeline getEventTimelineByGroup(@PathVariable String groupId) {
		log.debug("EventAPIController.getEventTimelineByGroup(groupId) groupId=" + groupId);
		return getEventService().findEventTimeline(groupId);
	}

	@RequestMapping("/groups/{groupId}/events/new")
	public Event createNewEvent(@PathVariable String groupId) {
		log.debug("EventAPIController.createNewEvent() gor groupId" + groupId);
		return EventFactory.createEvent(getGroupService().findOne(groupId), getUser());
	}

	@RequestMapping(value = "/events/{eventId}", method = RequestMethod.PUT)
	public Event update(@PathVariable String eventId, @RequestBody Event event) {
		log.debug("EventAPIController.save()");

		Event original = getEventService().findOne(eventId);

		if (original == null)
			throw new EntityNotFoundException(Event.class.getSimpleName(), eventId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Event.class.getSimpleName(), eventId);

		event.setUser(getUser().getIdentifier());
		event.setId(eventId);

		return getEventService().save(event);
	}

	@RequestMapping(value = "/events", method = RequestMethod.POST)
	public Event create(@RequestBody Event event) {
		log.debug("EventAPIController.save()");

		event.setUser(getUser().getIdentifier());
		event.setId(null);

		return getEventService().save(event);
	}

	@RequestMapping("/events/{eventId}/sponsors")
	public List<User> sponsors(@PathVariable String eventId) {
		log.debug("EventAPIController.sponsors eventId =" + eventId);
		return getEventService().findSponsors(eventId);
	}

	@RequestMapping("/users/{username}/events")
	public List<Event> getUserEvents(@PathVariable String username) {
		log.debug("EventAPIController.eventByOwner username =" + username);
		return getEventService().findUserEvents(username, -1, -1);
	}

	@RequestMapping("/groups/{groupId}/events/last")
	public List<Event> lastGroupEvents(@PathVariable String groupId) {
		log.debug("EventAPIController.lastGroupEvents groupId =" + groupId);
		return getEventService().findGroupEvents(groupId, 0, 5);
	}

	@RequestMapping("/groups/{groupId}/events")
	public List<Event> groupEvents(@PathVariable String groupId) {
		log.debug("EventAPIController.groupEvents groupId =" + groupId);
		return getEventService().findGroupEvents(groupId, -1, -1);
	}

	@RequestMapping("/groups/{groupId}/events/size")
	public int eventSizeByGroup(@PathVariable String groupId) {
		log.debug("EventAPIController.eventSizeByGroup groupId =" + groupId);
		return getEventService().countByGroup(groupId);
	}

	@RequestMapping("/events/{eventId}")
	public Event event(@PathVariable String eventId) {
		log.debug("EventAPIController.event eventId =" + eventId);
		return getEventService().findOne(eventId);
	}

	/**
	 * DELETE /events/:id -> delete the "id" event.
	 */
	@RequestMapping(value = "/events/{eventId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteEvent(@PathVariable String eventId) {
		log.debug("EventAPIController.deleteEvent eventId =" + eventId);

		Event original = getEventService().findOne(eventId);

		if (original == null)
			throw new EntityNotFoundException(Event.class.getSimpleName(), eventId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Event.class.getSimpleName(), eventId);

		getEventService().delete(eventId);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("event", eventId.toString())).build();

	}

	@RequestMapping(value = "/events/{eventId}/participations", method = RequestMethod.POST)
	public Participation participate(@PathVariable String eventId, @RequestBody Participation participation) {
		log.debug("EventAPIController.participate");

		Event event = getEventService().findOne(eventId);

		if (event == null)
			throw new EntityNotFoundException(Event.class.getSimpleName(), eventId);

		if (event.accept(participation)) {
			participation.setEventName(event.getName());
			participation.setEventId(event.getId());
			participation = getEventService().save(participation);
		}

		return participation;
	}

	@RequestMapping("/events/{eventId}/participations/generate")
	public void generateParticipation(@PathVariable String eventId) {
		log.debug("EventAPIController.generateParticipation() eventId = " + eventId);
		Event event = getEventService().findOne(eventId);
		if (event != null) {
			getEventService().deleteEventParticipations(eventId);

			List<User> users = getUserService().findAll();
			if (users != null) {
				for (User user : users) {
					log.debug("User username=|" + user.getUsername() + "|");
					if (user.getUsername() != null && !user.getUsername().equals("")) {
						Participation participation = new Participation(event, user);
						int i = ThreadLocalRandom.current().nextInt(1, 6);
						if (i == 1 || i == 2) {
							participation.setStatus(Participation.STATUS_IN);
						} else if (i == 2 || i == 3 || i == 4) {
							participation.setStatus(Participation.STATUS_OUT);
						} else {
							participation.setStatus(Participation.STATUS_RSVP);
						}
						getEventService().save(participation);
					} else {
						getUserService().delete(user.getId());
					}
				}
			}
		}
	}

	@RequestMapping("/events/{eventId}/participations")
	public List<Participation> participations(@PathVariable String eventId) {
		log.debug("EventAPIController.participations()" + eventId);
		return getEventService().findParticipations(eventId, 0, -1);
	}

	@RequestMapping("/events/{eventId}/participations/summary")
	public ParticipationSummary participationSummary(@PathVariable String eventId) {
		log.debug("EventAPIController.participationSummary()" + eventId);
		return getEventService().findParticipationSummary(eventId);
	}

	@RequestMapping("/events/{eventId}/participations/size")
	public int countParticipations(@PathVariable String eventId) {
		log.debug("EventAPIController.participations()" + eventId);
		return getEventService().countParticipations(eventId);
	}

	@RequestMapping("/events/{eventId}/participations/last")
	public List<Participation> lastParticipations(@PathVariable String eventId) {
		log.debug("EventAPIController.participations()" + eventId);
		return getEventService().findParticipations(eventId, 0, 5);
	}

	@RequestMapping("/users/{username}/participations")
	public List<Participation> userParticipations(@PathVariable String username) {
		log.debug("EventAPIController.userParticipations()" + username);
		return getEventService().findUserParticipations(username);
	}

	@RequestMapping("/users/{username}/events/{eventId}/participation")
	public Participation userParticipation(@PathVariable String username, @PathVariable String eventId) {
		log.debug("EventAPIController.userParticipation()" + eventId);

		Participation participation = getEventService().findOneParticipation(eventId, username);
		if (participation == null) {
			participation = new Participation();
			participation.setEventId(eventId);
			participation.setStatus(Participation.STATUS_RSVP);
			participation.setUsername(username);
		}

		return participation;
	}

	@RequestMapping("/events/{eventId}/comments")
	public List<EventComment> comments(@PathVariable String eventId) {
		log.debug("EventAPIController.comments for eventId" + eventId);

		return getEventService().findComments(eventId, 0, 0);

	}

	@RequestMapping("/events/{eventId}/comments/size")
	public int commentSize(@PathVariable String eventId) {
		log.debug("EventAPIController.commentSize for eventId" + eventId);

		return getEventService().countComments(eventId);
	}

	@RequestMapping(value = "/events/{eventId}/comments/content", method = RequestMethod.POST)
	public EventComment post(@PathVariable String eventId, @PathVariable String content) {
		log.debug("EventAPIController.post");
		EventComment comment = null;

		if (content != null && !"".equals(content)) {
			comment = new EventComment();
			comment.setEventId(eventId);

			comment.setUsername(getUserName());
			comment.setCommentDate(new Date());
			comment.setContent(content);

			comment = getEventService().saveComments(comment);
		}

		return comment;
	}

}
