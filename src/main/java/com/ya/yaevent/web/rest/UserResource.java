package com.ya.yaevent.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ya.yaevent.domain.Authority;
import com.ya.yaevent.domain.User;
import com.ya.yaevent.repository.AuthorityRepository;
import com.ya.yaevent.repository.UserRepository;
import com.ya.yaevent.security.AuthoritiesConstants;
import com.ya.yaevent.security.SecurityUtils;
import com.ya.yaevent.service.MailService;
import com.ya.yaevent.service.UserService;
import com.ya.yaevent.web.rest.dto.ManagedUserDTO;
import com.ya.yaevent.web.rest.util.HeaderUtil;
import com.ya.yaevent.web.rest.util.PaginationUtil;

/**
 * REST controller for managing users.
 *
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </p>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserService userService;

    
    private UserService getUserService(){
    	return userService;
    }
    
    /**
     * POST  /users -> Creates a new user.
     * <p>
     * Creates a new user if the username and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> createUser(@RequestBody ManagedUserDTO managedUserDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save User : {}", managedUserDTO);
        if (userRepository.findOneByUsername(managedUserDTO.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("user-management", "userexists", "username already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(managedUserDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use"))
                .body(null);
        } else {
            User newUser = userService.createUser(managedUserDTO);
            String baseUrl = request.getScheme() + // "http"
            "://" +                                // "://"
            request.getServerName() +              // "myhost"
            ":" +                                  // ":"
            request.getServerPort() +              // "80"
            request.getContextPath();              // "/myContextPath" or "" if deployed in root context
            mailService.sendCreationEmail(newUser, baseUrl);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getUsername()))
                .headers(HeaderUtil.createAlert( "A user is created with identifier " + newUser.getUsername(), newUser.getUsername()))
                .body(newUser);
        }
    }

    /**
     * PUT  /users -> Updates an existing User.
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<ManagedUserDTO> updateUser(@RequestBody ManagedUserDTO managedUserDTO) throws URISyntaxException {
        log.debug("REST request to update User : {}", managedUserDTO);
        Optional<User> existingUser = userRepository.findOneByEmail(managedUserDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserDTO.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "E-mail already in use")).body(null);
        }
        existingUser = userRepository.findOneByUsername(managedUserDTO.getUsername());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserDTO.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "userexists", "Username already in use")).body(null);
        }
        return userRepository
            .findOneById(managedUserDTO.getId())
            .map(user -> {
                user.setUsername(managedUserDTO.getUsername());
                user.setFirstName(managedUserDTO.getFirstName());
                user.setLastName(managedUserDTO.getLastName());
                user.setEmail(managedUserDTO.getEmail());
                user.setActivated(managedUserDTO.isActivated());
                user.setLangKey(managedUserDTO.getLangKey());
                Set<Authority> authorities = user.getAuthorities();
                authorities.clear();
                managedUserDTO.getAuthorities().stream().forEach(
                    authority -> authorities.add(authorityRepository.findOne(authority))
                );
                userRepository.save(user);
                return ResponseEntity.ok()
                    .headers(HeaderUtil.createAlert("A user is updated with identifier " + managedUserDTO.getUsername(), managedUserDTO.getUsername()))
                    .body(new ManagedUserDTO(userRepository
                        .findOne(managedUserDTO.getId())));
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

    }

    /**
     * GET  /users -> get all users.
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ManagedUserDTO>> getAllUsers(Pageable pageable)
        throws URISyntaxException {
        Page<User> page = userRepository.findAll(pageable);
        List<ManagedUserDTO> managedUserDTOs = page.getContent().stream()
            .map(user -> new ManagedUserDTO(user))
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(managedUserDTOs, headers, HttpStatus.OK);
    }

    /**
     * GET  /users/:username -> get the "username" user.
     */
    @RequestMapping(value = "/users/{username:[_'.@a-z0-9-]+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedUserDTO> getUser(@PathVariable String username) {
        log.debug("REST request to get User : {}", username);
        return userService.getUserWithAuthoritiesByUsername(username)
                .map(ManagedUserDTO::new)
                .map(managedUserDTO -> new ResponseEntity<>(managedUserDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    /**
     * DELETE  USER :username -> delete the "username" User.
     */
    @RequestMapping(value = "/users/{username}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        log.debug("REST request to delete User: {}", username);
        userService.deleteUserInformation(username);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A user is deleted with identifier " + username, username)).build();
    }
    
    
    
    
	@RequestMapping(value = "/users/{followee}/follow", method = RequestMethod.POST)
	public void follow(@PathVariable String followee) {
		log.debug("UserAPIController.follow(followee)" + followee);

		if (!getUserName().equals(followee)) {
			getUserService().follow(getUserName(), followee);
		}

	}

	@RequestMapping(value = "/users/{followee}/unfollow", method = RequestMethod.POST)
	public void unFollow(@PathVariable String followee) {
		log.debug("UserAPIController.unFollow(followee)" + followee);

		if (!getUserName().equals(followee)) {
			getUserService().unFollow(getUserName(), followee);
		}
	}

	@RequestMapping("/users/{username}/followers")
	public List<User> followers(@PathVariable String username) {
		log.debug("UserAPIController.followers(username)" + username);

		return getUserService().getFollowers(username);
	}

	@RequestMapping("/users/{username}/followers/size")
	public int followersSize(@PathVariable String username) {
		log.debug("UserAPIController.followerSize username =" + username);
		return getUserService().countFollowers(username);
	}

	@RequestMapping("/users/{username}/following")
	public List<User> following(@PathVariable String username) {
		log.debug("UserAPIController.following username =" + username);
		return getUserService().findFollowing(username);
	}

	@RequestMapping("/users/{username}/following/name")
	public List<String> followingNames(@PathVariable String username) {
		log.debug("UserAPIController.followingNames username =" + username);
		return getUserService().findFollowingNames(username);
	}

	@RequestMapping("/users/{username}/following/size")
	public int followingSize(@PathVariable String username) {
		log.debug("UserAPIController.followingSize username =" + username);
		return getUserService().countFollowing(username);
	}
    
	protected static String getUserName() {
		return SecurityUtils.getCurrentUsername();
	}
    
}
