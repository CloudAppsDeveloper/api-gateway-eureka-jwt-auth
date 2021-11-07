package com.assglobal.esms.api.gateway.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.assglobal.esms.api.gateway.model.LoginUser;
@Repository
public interface UsersRepository extends CrudRepository<LoginUser, Long>{

	LoginUser findByUsername(String userId);

}
