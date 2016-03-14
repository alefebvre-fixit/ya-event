package com.ya.yaevent.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ya.yaevent.domain.User;

/**
 * Spring Data MongoDB repository for the User entity.
 */
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByUsername(String username);

    Optional<User> findOneById(String userId);
    
    List<User> findByUsernameIn(List<String> usernames);
	
	List<User> findByEmail(String email);

    @Override
    void delete(User t);

}
