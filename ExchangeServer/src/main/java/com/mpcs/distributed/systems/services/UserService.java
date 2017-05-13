package com.mpcs.distributed.systems.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mpcs.distributed.systems.model.User;
import com.mpcs.distributed.systems.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
    private UserRepository userRepo;
	
	public boolean isUserValid(String userId, String exchangeName){

	    //UserRepository userRepo;// = new UserRepository();
	    
		List<User> users = userRepo.findByUserNameAndExchangeName(userId, exchangeName);
		
		if(users.isEmpty()){
			return false;
		}else{
			return true;
		}
	}

}
