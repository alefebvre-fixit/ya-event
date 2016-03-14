package com.ya.yaevent.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ya.yaevent.domain.Authority;
import com.ya.yaevent.domain.User;
import com.ya.yaevent.domain.user.Following;
import com.ya.yaevent.domain.user.UserIdentifier;
import com.ya.yaevent.repository.AuthorityRepository;
import com.ya.yaevent.repository.FollowingRepository;
import com.ya.yaevent.repository.UserRepository;
import com.ya.yaevent.security.SecurityUtils;
import com.ya.yaevent.service.util.RandomUtil;
import com.ya.yaevent.util.YaUtil;
import com.ya.yaevent.web.rest.dto.ManagedUserDTO;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private UserRepository userRepository;

	@Inject
	private AuthorityRepository authorityRepository;

	@Inject
	private FollowingRepository followingRepository;

	public Optional<User> activateRegistration(String key) {
		log.debug("Activating user for activation key {}", key);
		userRepository.findOneByActivationKey(key).map(user -> {
			// activate given user for the registration key.
			user.setActivated(true);
			user.setActivationKey(null);
			userRepository.save(user);
			log.debug("Activated user: {}", user);
			return user;
		});
		return Optional.empty();
	}

	public Optional<User> completePasswordReset(String newPassword, String key) {
		log.debug("Reset user password for reset key {}", key);

		return userRepository.findOneByResetKey(key).filter(user -> {
			ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
			return user.getResetDate().isAfter(oneDayAgo);
		}).map(user -> {
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setResetKey(null);
			user.setResetDate(null);
			userRepository.save(user);
			return user;
		});
	}

	public Optional<User> requestPasswordReset(String mail) {
		return userRepository.findOneByEmail(mail).filter(User::getActivated).map(user -> {
			user.setResetKey(RandomUtil.generateResetKey());
			user.setResetDate(ZonedDateTime.now());
			userRepository.save(user);
			return user;
		});
	}

	public User createUserInformation(String username, String password, String firstName, String lastName, String email,
			String langKey) {

		User newUser = new User();
		Authority authority = authorityRepository.findOne("ROLE_USER");
		Set<Authority> authorities = new HashSet<>();
		String encryptedPassword = passwordEncoder.encode(password);
		newUser.setUsername(username);
		// new user gets initially a generated password
		newUser.setPassword(encryptedPassword);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(email);
		newUser.setLangKey(langKey);
		// new user is not active
		newUser.setActivated(false);
		// new user gets registration key
		newUser.setActivationKey(RandomUtil.generateActivationKey());
		authorities.add(authority);
		newUser.setAuthorities(authorities);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	public User createUser(ManagedUserDTO managedUserDTO) {
		User user = new User();
		user.setUsername(managedUserDTO.getUsername());
		user.setFirstName(managedUserDTO.getFirstName());
		user.setLastName(managedUserDTO.getLastName());
		user.setEmail(managedUserDTO.getEmail());
		if (managedUserDTO.getLangKey() == null) {
			user.setLangKey("en"); // default language is English
		} else {
			user.setLangKey(managedUserDTO.getLangKey());
		}
		if (managedUserDTO.getAuthorities() != null) {
			Set<Authority> authorities = new HashSet<>();
			managedUserDTO.getAuthorities().stream()
					.forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
			user.setAuthorities(authorities);
		}
		String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
		user.setPassword(encryptedPassword);
		user.setResetKey(RandomUtil.generateResetKey());
		user.setResetDate(ZonedDateTime.now());
		user.setActivated(true);
		userRepository.save(user);
		log.debug("Created Information for User: {}", user);
		return user;
	}

	public void updateUserInformation(String firstName, String lastName, String email, String city, String country,
			String website, String biography, String langKey) {
		userRepository.findOneByUsername(SecurityUtils.getCurrentUser().getUsername()).ifPresent(u -> {
			u.setFirstName(firstName);
			u.setLastName(lastName);
			u.setEmail(email);
			u.setCity(city);
			u.setCountry(country);
			u.setWebsite(website);
			u.setBiography(biography);
			u.setLangKey(langKey);
			userRepository.save(u);
			log.debug("Changed Information for User: {}", u);
		});
	}

	public void deleteUserInformation(String username) {
		userRepository.findOneByUsername(username).ifPresent(u -> {
			userRepository.delete(u);
			log.debug("Deleted User: {}", u);
		});
	}

	public void changePassword(String password) {
		userRepository.findOneByUsername(SecurityUtils.getCurrentUser().getUsername()).ifPresent(u -> {
			String encryptedPassword = passwordEncoder.encode(password);
			u.setPassword(encryptedPassword);
			userRepository.save(u);
			log.debug("Changed password for User: {}", u);
		});
	}

	public Optional<User> getUserWithAuthoritiesByUsername(String username) {
		return userRepository.findOneByUsername(username).map(u -> {
			u.getAuthorities().size();
			return u;
		});
	}

	public User getUserWithAuthorities(String id) {
		User user = userRepository.findOne(id);
		user.getAuthorities().size(); // eagerly load the association
		return user;
	}

	public User getUserWithAuthorities() {
		User user = userRepository.findOneByUsername(SecurityUtils.getCurrentUser().getUsername()).get();
		user.getAuthorities().size(); // eagerly load the association
		return user;
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p/>
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 * </p>
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		ZonedDateTime now = ZonedDateTime.now();
		List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
		for (User user : users) {
			log.debug("Deleting not activated user {}", user.getUsername());
			userRepository.delete(user);
		}
	}

	public List<User> findByIdentifiers(List<UserIdentifier> identifiers) {

		List<String> usernames = new ArrayList<String>();

		if (YaUtil.isNotEmpty(identifiers)) {
			for (UserIdentifier id : identifiers) {
				usernames.add(id.getUsername());
			}
		}

		return find(usernames);
	}

	public List<User> find(List<String> usernames) {
		List<User> result = null;

		log.debug("MongoUserService.find(List<String> usernames) usernames=" + usernames);

		if (YaUtil.isNotEmpty(usernames)) {
			result = userRepository.findByUsernameIn(usernames);
		}

		if (result == null) {
			result = new ArrayList<User>();
		}

		return result;
	}

	public User findOne(String username) {
		log.debug("MongoUserService.load(String userName) username=" + username);
		final User result;

		result = userRepository.findOneByUsername(SecurityUtils.getCurrentUser().getUsername()).get();

		return result;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public void delete(String id) {
		userRepository.delete(id);
	}

	public int countFollowers(String username) {
		log.debug("MongoUserService.countFollowers(String username) username=" + username);
		return followingRepository.countByFollowee(username);
	}

	public void follow(String follower, String followee) {
		log.debug("MongoUserService.follow(String follower, String followee) follower=" + follower + " follower= "
				+ follower);

		List<Following> following = followingRepository.findByFolloweeAndFollower(followee, follower);
		if (YaUtil.isEmpty(following)) {
			followingRepository.save(Following.create(followee, follower));
		}
	}

	public void unFollow(String follower, String followee) {
		log.debug("MongoUserService.unFollow(String follower, String followee) follower=" + follower + " follower= "
				+ follower);
		List<Following> following = followingRepository.findByFolloweeAndFollower(followee, follower);
		if (YaUtil.isNotEmpty(following)) {
			followingRepository.delete(following);
		}

	}

	public List<User> getFollowers(String username) {

		log.debug("MongoUserService.getFollowers(String username) username=" + username);

		List<User> result = null;

		List<String> names = findFollowerNames(username);

		if (YaUtil.isNotEmpty(names)) {
			result = userRepository.findByUsernameIn(names);
		}

		if (result == null) {
			result = new ArrayList<User>();
		}

		log.debug("MongoUserService.getFollowers(String username) result=" + result.size());

		return result;
	}

	public List<String> findFollowerNames(String username) {
		log.debug("MongoUserService.getFollowerNames(String username) username=" + username);

		List<String> result = new ArrayList<String>();

		List<Following> following = followingRepository.findByFollowee(username);
		if (YaUtil.isNotEmpty(following)) {
			for (Following f : following) {
				result.add(f.getFollower());
			}
		}

		return result;
	}

	public List<String> findFollowingNames(String username) {
		log.debug("MongoUserService.getFollowingNames(String username) username=" + username);

		List<String> result = new ArrayList<String>();

		List<Following> following = followingRepository.findByFollower(username);
		if (following != null && following.size() > 0) {
			for (Following f : following) {
				result.add(f.getFollowee());
			}
		}

		return result;
	}

	public List<User> findFollowing(String username) {
		List<User> result = null;

		log.debug("MongoUserService.getFollowing(String username) username=" + username);

		List<String> names = findFollowingNames(username);

		if (YaUtil.isNotEmpty(names)) {
			result = userRepository.findByUsernameIn(names);
		}

		if (result == null) {
			result = new ArrayList<User>();
		}

		return result;
	}

	public int countFollowing(String username) {
		log.debug("MongoUserService.countFollowing(String username) username=" + username);
		return followingRepository.countByFollower(username);
	}

}
