package com.ya.yaevent.web.rest;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ya.yaevent.domain.survey.Survey;
import com.ya.yaevent.exception.EntityAuthorizationException;

/**
 * REST controller for managing Group.
 */
@RestController
@RequestMapping("/api")
public class SurveyResource extends YaResource {

	private final Logger log = LoggerFactory.getLogger(SurveyResource.class);

	@RequestMapping(value = "/surveys/{surveyId}", method = RequestMethod.PUT)
	public Survey update(@PathVariable String surveyId, @RequestBody Survey survey) {
		log.debug("SurveyAPIController.update()");

		Survey original = getSurveyService().getSurvey(surveyId);

		if (original == null)
			throw new EntityNotFoundException(Survey.class.getSimpleName() + surveyId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Survey.class.getSimpleName(), surveyId);

		survey.setUsername(getUserName());
		survey.setId(surveyId);
		return getSurveyService().save(survey);

	}

	@RequestMapping(value = "/survey", method = RequestMethod.POST)
	public Survey create(@RequestBody Survey survey) {
		log.debug("SurveyAPIController.save()");

		survey.setUsername(getUserName());
		survey.setId(null);

		return getSurveyService().save(survey);

	}

	@RequestMapping("/surveys/{surveyId}")
	public Survey survey(@PathVariable String surveyId) {
		log.debug("SurveyAPIController.survey surveyId =" + surveyId);
		return getSurveyService().getSurvey(surveyId);
	}

	@RequestMapping(value = "/surveys/{surveyId}", method = RequestMethod.DELETE)
	public void deleteSurvey(String surveyId) {

		Survey original = getSurveyService().getSurvey(surveyId);

		if (original == null)
			throw new EntityNotFoundException(Survey.class.getSimpleName() + surveyId);

		if (!original.canUpdate(getUserName()))
			throw new EntityAuthorizationException(Survey.class.getSimpleName(), surveyId);

		getSurveyService().delete(surveyId);
	}

	@RequestMapping("/surveys")
	public List<Survey> surveys() {
		log.debug("SurveyAPIController.surveys()");

		return getSurveyService().getAll();
	}

}
