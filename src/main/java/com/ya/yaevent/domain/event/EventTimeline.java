package com.ya.yaevent.domain.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.ya.yaevent.domain.Event;
import com.ya.yaevent.util.YaUtil;

public class EventTimeline {

	private List<Event> today = new ArrayList<Event>();
	private List<Event> upcoming = new ArrayList<Event>();
	private List<Event> past = new ArrayList<Event>();

	private Date reference;
	
	public EventTimeline(){
		this(new Date());
	}
	
	public EventTimeline(Date reference){
		this.reference = reference;
	}
	
	public void add(List<Event> events){
		if (YaUtil.isNotEmpty(events)){
			for (Event event : events) {
				if (event.getDate() == null){
					upcoming.add(event);
				} else if (DateUtils.isSameDay(reference, event.getDate())){
					today.add(event);
				} else if (event.getDate().after(reference)){
					upcoming.add(event);
				} else {
					past.add(event);
				}
			} 
		}
	}
	
	public List<Event> getPast() {
		return past;
	}

	public void setPast(List<Event> past) {
		this.past = past;
	}

	public List<Event> getUpcoming() {
		return upcoming;
	}

	public void setUpcoming(List<Event> upcoming) {
		this.upcoming = upcoming;
	}

	public List<Event> getToday() {
		return today;
	}

	public void setToday(List<Event> today) {
		this.today = today;
	}
	
	public int getTodaySize(){
		return today.size();
	}
	
	public int getPastSize(){
		return past.size();
	}
	
	public int getUpcomingSize(){
		return upcoming.size();
	}
	
	public int getSize(){
		return getTodaySize() + getUpcomingSize() + getPastSize();
	}

}
