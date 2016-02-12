package com.ya.yaevent.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.yaevent.domain.Group;

/**
 * Spring Data MongoDB repository for the Group entity.
 */
public interface GroupRepository extends MongoRepository<Group, String> {

	public int countByUsername(String username);

	public Page<Group> findByUsername(String username, Pageable pageable);

	public List<Group> findByUsername(String username);

}
