package com.ya.yaevent.service;

import java.util.List;

import com.ya.yaevent.domain.Event;
import com.ya.yaevent.domain.User;
import com.ya.yaevent.domain.event.EventComment;
import com.ya.yaevent.domain.event.EventTimeline;
import com.ya.yaevent.domain.event.Participation;
import com.ya.yaevent.domain.event.ParticipationSummary;

/**
 * Service Interface for managing Event.
 */
public interface EventService {

	/**
	 * Save a event.
	 * 
	 * @return the persisted entity
	 */
	public Event save(Event event);

	/**
	 * get all the events.
	 * 
	 * @return the list of entities
	 */
	public List<Event> findAll();

	/**
	 * get the "id" event.
	 * 
	 * @return the entity
	 */
	public Event findOne(String id);

	/**
	 * delete the "id" event.
	 */
	public void delete(String id);

	public EventTimeline findEventTimeline();

	public EventTimeline findEventTimeline(String groupId);

	public int countByOwner(String username);

	public List<Event> findUserEvents(String username, int offset, int length);

	public List<Event> findGroupEvents(String groupId, int offset, int length);

	public int countByGroup(String groupId);

	public String findEventOwner(String eventId);

	public List<Participation> findParticipations(String eventId, int offset, int length);

	public ParticipationSummary findParticipationSummary(String eventId);

	public int countParticipations(String eventId);

	public Participation save(Participation participation);

	public Participation findOneParticipation(String participationId);

	public Participation findOneParticipation(String eventId, String username);

	public void deleteParticipation(String id);

	public void deleteEventParticipations(String eventId);

	public List<Participation> findUserParticipations(String username);

	public List<Participation> findParticipations(String eventId);

	public List<Participation> findAllParticipations();

	public EventComment findOneComment(String commentId);

	public EventComment saveComments(EventComment comment);

	public void deleteComment(String commentId);

	public List<EventComment> findComments(String eventId, int offset, int length);

	public int countComments(String eventId);

	public List<User> findSponsors(String groupId);

}
