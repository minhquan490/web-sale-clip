package com.system.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.system.spring.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.username = :username")
	public User getUserFromUsername(@Param("username") String username);

	@Query("SELECT u FROM User u INNER JOIN FETCH u.credit WHERE u.username = :username")
	public User getUserInformation(@Param("username") String username);

	@Query("SELECt u FROM User u INNER JOIN FETCH u.clips WHERE u.id = :id")
	public User getUserClip(@Param("id") long id);

	@Query("SELECT u FROM User u INNER JOIN FETCH u.clipsHaveBeenPurchased WHERE u.id = :id")
	public User getUserHasClipsPurchased(@Param("id") long id);
}
