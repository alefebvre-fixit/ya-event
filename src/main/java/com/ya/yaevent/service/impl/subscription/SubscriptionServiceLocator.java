package com.ya.yaevent.service.impl.subscription;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ya.yaevent.domain.notification.Notification;
import com.ya.yaevent.service.SubscriptionService;

@Service
public class SubscriptionServiceLocator {

	private final Logger log = LoggerFactory.getLogger(ParticipationSubscriptionService.class);

	@Autowired
	private Map<String, SubscriptionService> myServiceRegistry;

	/**
	 * Service locator to find the right Domain service to interact with the
	 * requested data store
	 * 
	 * @param serviceID
	 * @return
	 */
	public SubscriptionService locateServiceFor(Notification notification) {
		SubscriptionService result = myServiceRegistry.get(getServiceId(notification));

		if (result != null) {
			log.debug("SubscriptionServiceFactory.getInstance(Object object) result="
					+ result.getClass().getSimpleName());
		} else {
			log.debug("SubscriptionServiceFactory.getInstance(Object object) result = notfound");
		}

		return result;

	}

	private String getServiceId(Notification notification) {

		return notification.getType().toLowerCase() + "SubscriptionService";

	}

}
