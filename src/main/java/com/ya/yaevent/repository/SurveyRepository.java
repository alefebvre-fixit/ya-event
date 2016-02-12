package com.ya.yaevent.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.yaevent.domain.survey.Survey;

public interface SurveyRepository extends MongoRepository<Survey, String> {

}