package com.ya.yaevent.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.yaevent.domain.Favorite;

public interface FavoriteRepository extends MongoRepository<Favorite, String> {
	
	public int countByUsernameAndGroupId(String username, String groupId);
	
	public List<Favorite> findByUsernameAndGroupId(String username, String groupId);

	public List<Favorite> findByUsername(String username);
	
	public List<Favorite> findByGroupId(String groupId);
	
	public int countByGroupId(String groupId);
	
	public int countByUsername(String username);


	
}