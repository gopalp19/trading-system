package com.mpcs.distributed.systems.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mpcs.distributed.systems.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	List<User> findByUserNameAndExchangeName(String userName, String exchangeName);

}
