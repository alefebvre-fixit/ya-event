package com.ya.yaevent.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ya.yaevent.domain.survey.Survey;
import com.ya.yaevent.repository.SurveyRepository;
import com.ya.yaevent.service.NotificationService;
import com.ya.yaevent.service.SurveyService;

@Service
public class SurveyServiceImpl implements SurveyService {

	public static final String USER_NAME = "username";
	public static final String SURVEY_ID = "surveyId";
	public static final String EVENT_ID = "eventId";

	private final Logger log = LoggerFactory.getLogger(SurveyServiceImpl.class);

	@Inject
	private SurveyRepository surveyRepository;

	@Inject
	private NotificationService notificationService;

	@Override
	public void delete(String id) {
		log.debug("MongoEventService.delete(String id) id=" + id);
		surveyRepository.delete(id);
	}

	@Override
	public Survey save(Survey survey) {
		notificationService.publishNotification(survey);

		Survey result = surveyRepository.save(survey);
		return result;
	}

	@Override
	public Survey getSurvey(String id) {
		return surveyRepository.findOne(id);
	}

	@Override
	public List<Survey> getAll() {
		return surveyRepository.findAll();
	}

}
