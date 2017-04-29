package com.mpcs.distributed.systems.repository;

import org.springframework.data.repository.CrudRepository;

import com.mpcs.distributed.systems.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
