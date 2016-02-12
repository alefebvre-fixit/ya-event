package com.ya.yaevent.domain.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticipationSummary {
	
	private int totalSize = 0;

	private Map<String, List<Participation>> participationMap = new HashMap<String, List<Participation>>();
	
	
	public List<Participation> getIn() {
		return participationMap.get(Participation.STATUS_IN);
	}
	
	public List<Participation> getOut() {
		return participationMap.get(Participation.STATUS_OUT);
	}
	
	public List<Participation> getRsvp() {
		return participationMap.get(Participation.STATUS_RSVP);
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public ParticipationSummary(List<Participation> participations){
		if (participations != null){

			this.totalSize = participations.size();
			
			for (Participation participation : participations) {
				if (participationMap.containsKey(participation.getStatus())){
					participationMap.get(participation.getStatus()).add(participation);
				} else {
					List<Participation> values = new ArrayList<Participation>();
					values.add(participation);
					participationMap.put(participation.getStatus(), values);
				}
			}
			
		}
	}
	
}
