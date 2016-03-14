package com.ya.yaevent.web.rest.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.ya.yaevent.domain.Authority;
import com.ya.yaevent.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

	public static final int PASSWORD_MIN_LENGTH = 5;
	public static final int PASSWORD_MAX_LENGTH = 100;

	@Pattern(regexp = "^[a-z0-9]*$")
	@NotNull
	@Size(min = 1, max = 50)
	private String username;

	@NotNull
	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
	private String password;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 100)
	private String email;

	@Size(max = 50)
	private String city;

	@Size(max = 50)
	private String country;

	@Size(max = 50)
	private String website;

	@Size(max = 50)
	private String biography;

	private boolean activated = false;

	@Size(min = 2, max = 5)
	private String langKey;

	private Set<String> authorities;

	public UserDTO() {
	}

	public UserDTO(User user) {
		this(user.getUsername(), null, user.getFirstName(), user.getLastName(), user.getEmail(), user.getCity(),
				user.getCountry(), user.getWebsite(), user.getBiography(), user.getActivated(), user.getLangKey(),
				user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
	}

	public UserDTO(String username, String password, String firstName, String lastName, String email, String city,
			String country, String website, String biography, boolean activated, String langKey,
			Set<String> authorities) {

		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.city = city;
		this.country = country;
		this.website = website;
		this.biography = biography;
		this.activated = activated;
		this.langKey = langKey;
		this.authorities = authorities;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public boolean isActivated() {
		return activated;
	}

	public String getLangKey() {
		return langKey;
	}

	public Set<String> getAuthorities() {
		return authorities;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getWebsite() {
		return website;
	}

	public String getBiography() {
		return biography;
	}

	@Override
	public String toString() {
		return "UserDTO{" + "username='" + username + '\'' + ", password='" + password + '\'' + ", firstName='"
				+ firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", activated="
				+ activated + ", langKey='" + langKey + '\'' + ", authorities=" + authorities + "}";
	}
}
