package com.ya.yaevent.service;

import java.util.List;

import com.ya.yaevent.domain.survey.Survey;

public interface SurveyService {

	public List<Survey> getAll();

	public Survey save(Survey survey);

	public void delete(String id);

	public Survey getSurvey(String id);

}
