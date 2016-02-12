package com.ya.yaevent.service.impl;

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

import com.ya.yaevent.domain.Event;
import com.ya.yaevent.domain.Group;
import com.ya.yaevent.domain.User;
import com.ya.yaevent.domain.event.EventComment;
import com.ya.yaevent.domain.event.EventTimeline;
import com.ya.yaevent.domain.event.Participation;
import com.ya.yaevent.domain.event.ParticipationSummary;
import com.ya.yaevent.repository.EventCommentRepository;
import com.ya.yaevent.repository.EventRepository;
import com.ya.yaevent.repository.ParticipationRepository;
import com.ya.yaevent.service.EventService;
import com.ya.yaevent.service.GroupService;
import com.ya.yaevent.service.NotificationService;
import com.ya.yaevent.service.UserService;

/**
 * Service Implementation for managing Event.
 */
@Service
public class EventServiceImpl implements EventService {

	private final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

	@Inject
	private EventRepository eventRepository;

	@Inject
	private ParticipationRepository participationRepository;

	@Inject
	private NotificationService notificationService;

	@Inject
	private EventCommentRepository commentRepository;

	@Inject
	private GroupService groupService;

	@Inject
	private UserService userService;

	/**
	 * get one event by id.
	 * 
	 * @return the entity
	 */
	public Event findOne(String id) {
		log.debug("Request to get Event : {}", id);
		Event event = eventRepository.findOne(id);
		return event;
	}

	/**
	 * get all the events.
	 * 
	 * @return the list of entities
	 */
	public List<Event> findAll() {
		log.debug("Request to get all Events");
		return eventRepository.findAll(new Sort(Sort.Direction.DESC, "date"));
	}

	/**
	 * delete the event by id.
	 */
	public void delete(String id) {
		log.debug("Request to delete Event : {}", id);
		Event event = findOne(id);
		if (event != null && event.getGroupId() != null) {
			Group group = groupService.findOne(event.getGroupId());
			if (group != null) {
				group.decrementEventSize();
				groupService.save(group);
			}
		}
		eventRepository.delete(id);
	}

	/**
	 * Save a event.
	 * 
	 * @return the persisted entity
	 */
	public Event save(Event event) {
		log.debug("Request to save Event : {}", event);

		notificationService.publishNotification(event);

		if (event.getGroupId() != null && event.getId() == null) {
			Group group = groupService.findOne(event.getGroupId());
			if (group != null) {
				group.incrementEventSize();
				log.debug("MongoEventService.create(Event id) incrementEventSize=" + group.getEventSize());
				groupService.save(group);
			}
		}

		Event result = eventRepository.save(event);
		return result;
	}

	@Override
	public int countByOwner(String username) {
		return eventRepository.countByUsername(username);
	}

	@Override
	public List<Event> findUserEvents(String username, int offset, int length) {
		List<Event> result = null;

		if (length > 0) {
			Page<Event> pages = eventRepository.findByUsername(username, new PageRequest(offset, length));
			result = pages.getContent();
		} else {
			result = eventRepository.findByUsername(username);
		}

		return result;
	}

	@Override
	public List<Event> findGroupEvents(String groupId, int offset, int length) {
		List<Event> result = null;

		if (length > 0) {
			Page<Event> pages = eventRepository.findByGroupId(groupId, new PageRequest(offset, length));
			result = pages.getContent();
		} else {
			result = eventRepository.findByGroupId(groupId);
		}

		return result;
	}

	@Override
	public int countByGroup(String groupId) {
		return eventRepository.countByGroupId(groupId);
	}

	@Override
	public String findEventOwner(String eventId) {
		// TODO Improve implementation by loading only the username

		String result = null;

		Event event = findOne(eventId);
		if (event != null) {
			result = event.getUsername();
		}

		return result;
	}

	@Override
	public List<Participation> findParticipations(String eventId, int offset, int length) {
		List<Participation> result = null;
		log.debug("MongoEventService.getParticipations(String eventId = " + eventId + ", int offset = " + offset
				+ ", int length = " + length + " )");
		if (length > 0) {
			Page<Participation> pages = participationRepository.findByEventId(eventId, new PageRequest(offset, length));
			result = pages.getContent();
		} else {
			result = participationRepository.findByEventId(eventId);
		}

		return result;
	}

	@Override
	public Participation save(Participation participation) {

		Participation result = null;
		participation.incrementVersion();
		log.debug("MongoEventService.save(Participation participation)");
		if (participation.getId() == null) {

			log.debug("Looking for an existing participation with params eventId = " + participation.getEventId()
					+ " participation " + participation.getUsername());

			Participation existing = findOneParticipation(participation.getEventId(), participation.getUsername());
			if (existing != null) {
				log.debug("MongoEventService.save.reconcile with id=" + existing.id);
				participation.reconcile(existing);
				participation.setModificationDate(new Date());
				result = participationRepository.save(participation);
			} else {
				log.debug("Cannot find existing participation");

				participation.setCreationDate(new Date());
				participation.setModificationDate(participation.getCreationDate());
				result = participationRepository.save(participation);
			}
		} else {
			log.debug("MongoEventService.save.updateById(String id) id=" + participation.id);
			participation.setModificationDate(new Date());
			result = participationRepository.save(participation);
		}

		notificationService.publishNotification(participation);

		return result;
	}

	@Override
	public Participation findOneParticipation(String participationId) {
		return participationRepository.findOne(participationId);
	}

	@Override
	public Participation findOneParticipation(String eventId, String username) {
		Participation result = null;

		List<Participation> pages = participationRepository.findByEventIdAndUsername(eventId, username);

		if (pages != null && pages.size() > 0) {
			result = pages.get(0);
		}

		return result;
	}

	@Override
	public List<Participation> findUserParticipations(String username) {
		return participationRepository.findByUsername(username);
	}

	@Override
	public List<Participation> findParticipations(String eventId) {
		return participationRepository.findByEventId(eventId);
	}

	@Override
	public int countParticipations(String eventId) {
		return participationRepository.countByEventIdAndStatus(eventId, Participation.STATUS_IN);
	}

	@Override
	public EventComment findOneComment(String commentId) {
		return commentRepository.findOne(commentId);
	}

	@Override
	public EventComment saveComments(EventComment comment) {
		return commentRepository.save(comment);
	}

	@Override
	public void deleteComment(String commentId) {
		commentRepository.delete(commentId);
	}

	@Override
	public List<EventComment> findComments(String eventId, int offset, int length) {

		List<EventComment> result = null;
		log.debug("MongoEventService.getComments(String eventId = " + eventId + ", int offset = " + offset
				+ ", int length = " + length + " )");
		if (length > 0) {
			Page<EventComment> pages = commentRepository.findByEventIdOrderByCommentDateDesc(eventId,
					new PageRequest(offset, length));
			result = pages.getContent();
		} else {
			result = commentRepository.findByEventIdOrderByCommentDateDesc(eventId);
		}
		log.debug("MongoEventService.getCommentSize(String eventId = " + eventId + " ) found=" + result.size());

		return result;

	}

	@Override
	public int countComments(String eventId) {
		log.debug("MongoEventService.getCommentSize(String eventId = " + eventId + " )");
		return commentRepository.countByEventId(eventId);
	}

	@Override
	public void deleteParticipation(String id) {
		log.debug("MongoEventService.deleteParticipation(String id = " + id + " )");
		participationRepository.delete(id);
	}

	@Override
	public void deleteEventParticipations(String eventId) {
		log.debug("MongoEventService.deleteEventParticipations(String eventId = " + eventId + " )");
		participationRepository.deleteByEventId(eventId);
	}

	@Override
	public ParticipationSummary findParticipationSummary(String eventId) {
		log.debug("MongoEventService.getParticipationSummary(String eventId = " + eventId + " )");
		return new ParticipationSummary(findParticipations(eventId));
	}

	@Override
	public EventTimeline findEventTimeline() {

		EventTimeline result = new EventTimeline();
		result.add(findAll());

		return result;
	}

	@Override
	public EventTimeline findEventTimeline(String groupId) {

		EventTimeline result = new EventTimeline();
		result.add(findGroupEvents(groupId, 0, -1));

		return result;
	}

	@Override
	public List<User> findSponsors(String eventId) {

		log.debug("MongoEventService.eventSponsors(String eventId) eventId=" + eventId);

		List<User> result = new ArrayList<User>();
		Event event = findOne(eventId);
		if (event != null) {
			result = userService.findByIdentifiers(event.getSponsors());
		}

		if (result == null) {
			result = new ArrayList<User>();
		}

		return result;
	}

	@Override
	public List<Participation> findAllParticipations() {
		return participationRepository.findAll();
	}

}
