package com.grizzi.microservices.users.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grizzi.microservices.users.models.User;

//@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	List<User> findByEmail(String email);

	List<User> findByNickname(String nickname);
}
