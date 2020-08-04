package com.grizzi.microservices.users.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

/**
 * User POJO
 * 
 * @author giuseppe
 *
 */
@Entity(name = "USERS")
public class User {

	@Id
	@GeneratedValue
	private Integer id;
	
	@NotEmpty(message = "Invalid first name")
	@ApiModelProperty(example = "John", value = "First name", required = true)
	@ApiParam(example = "John", value = "First name", required = true)
	private String firstName;

	@NotEmpty(message = "Invalid last name")
	@ApiModelProperty(example = "Smith", value = "Last name", required = true)
	@ApiParam(example = "Smith", value = "Last name", required = true)
	private String lastName;

	@Pattern(regexp = "^(\\w+\\S+)$", message = "Nickname cannot contain blanks")
	@NotEmpty(message = "Invalid nickame")
	@ApiModelProperty(example = "myCoolNickName", value = "Nickname, unique among users. Cannot contain blanks", required = true)
	@ApiParam(example = "myCoolNickName", value = "Nickname, unique among users. Cannot contain blanks", required = true)
	private String nickname;

	@JsonProperty(access = Access.WRITE_ONLY)
	@NotEmpty(message = "Invalid password")
    @Column(length = 60)
	@ApiModelProperty(example = "Pa$$w0Rd!", value = "Password", required = true)
	@ApiParam(example = "Pa$$w0Rd!", value = "Password", required = true)
	private String password;

	@Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email")
	@NotEmpty(message = "Invalid email")
	@ApiModelProperty(example = "john.smith@email.com", value = "Valid email address, unique among users", required = true)
	@ApiParam(example = "john.smith@email.com", value = "Valid email address, unique among users", required = true)
	private String email;

	@NotEmpty(message = "Invalid country")
	@ApiModelProperty(example = "UK", value = "Country", required = true)
	@ApiParam(example = "UK", value = "Country", required = true)
	private String country;

	@ApiModelProperty(example = "1", value = "Id", dataType = "java.lang.Integer", readOnly = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setId(String id) {
		if (id != null) {
			this.id = Integer.valueOf(id);
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.trim();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.trim();
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country.trim();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", nickname=" + nickname
				+ ", email=" + email + ", country=" + country + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
}
