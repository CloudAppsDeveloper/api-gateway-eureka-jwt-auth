package com.cloudapps.developer.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cloudapps.developer.model.LoginUser;
@Repository
public interface UsersRepository extends CrudRepository<LoginUser, Long>{

	LoginUser findByUsername(String userId);

}
