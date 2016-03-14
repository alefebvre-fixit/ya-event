package com.ya.yaevent.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.yaevent.domain.user.Profile;
import com.ya.yaevent.domain.user.UserIdentifier;

/**
 * A user.
 */
@Document(collection = "jhi_user")
public class User extends AbstractAuditingEntity implements Serializable {

	@Id
	private String id;

	@JsonIgnore
	@NotNull
	@Size(min = 60, max = 60)
	private String password;

	@Size(max = 50)
	@Field("first_name")
	private String firstName;

	@Size(max = 50)
	@Field("last_name")
	private String lastName;
	
	@NotNull
	@Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
	@Size(min = 1, max = 50)
	@Field("username")
	private String username;

	@Email
	@Size(max = 100)
	private String email;
	
	@Size(max = 50)
	@Field("city")
	private String city;
	
	@Size(max = 50)
	@Field("country")
	private String country;
	
	@Size(max = 50)
	@Field("website")
	private String website;
	
	@Size(max = 50)
	@Field("biography")
	private String biography;
	
	private boolean activated = false;

	@Size(min = 2, max = 5)
	@Field("lang_key")
	private String langKey;

	@Size(max = 20)
	@Field("activation_key")
	@JsonIgnore
	private String activationKey;

	@Size(max = 20)
	@Field("reset_key")
	private String resetKey;

	@Field("reset_date")
	private ZonedDateTime resetDate = null;

	@JsonIgnore
	private Set<Authority> authorities = new HashSet<>();
	
	@Field("facebook_id")
	private String facebookId;
	
	@Field("gravatar_id")
	private String gravatarId;
	
	
	private Profile profile = new Profile();

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public String getResetKey() {
		return resetKey;
	}

	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}

	public ZonedDateTime getResetDate() {
		return resetDate;
	}

	public void setResetDate(ZonedDateTime resetDate) {
		this.resetDate = resetDate;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getGravatarId() {
		return gravatarId;
	}

	public void setGravatarId(String gravatarId) {
		this.gravatarId = gravatarId;
	}

	public UserIdentifier getIdentifier() {
		UserIdentifier result = new UserIdentifier();

		result.setFacebookId(facebookId);
		result.setGravatarId(gravatarId);
		result.setUsername(username);

		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		User user = (User) o;

		if (!username.equals(user.username)) {
			return false;
		}

		return true;
	}
	
	


	@Override
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public String toString() {
		return "User{" + "username='" + username + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
				+ '\'' + ", email='" + email + '\'' + ", activated='" + activated + '\'' + ", langKey='" + langKey
				+ '\'' + ", activationKey='" + activationKey + '\'' + "}";
	}
}
