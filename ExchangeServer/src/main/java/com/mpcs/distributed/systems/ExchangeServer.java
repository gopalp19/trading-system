package com.mpcs.distributed.systems;

import resourcesupport.*;
import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.mpcs.distributed.systems.application.AppContext;
import com.mpcs.distributed.systems.model.User;
import com.mpcs.distributed.systems.repositories.UserRepository;
import com.mpcs.distributed.systems.services.ClientConnectionPool;

@SpringBootApplication
public class ExchangeServer extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ExchangeServer.class);
	}
	
	@Bean
	public CommandLineRunner initBeans(ApplicationContext ctx, UserRepository userRepository) {
		return (args) -> {
            AppContext.setApplicationContext(ctx);
			userRepository.save(new User("gopalp", "NYSE"));
		};
	}

	public static void main(String[] args) {
		String exchangeServerName = args[0];

        SpringApplication.run(ExchangeServer.class, args);
        
		startSystem(exchangeServerName);

	}
	
	private static void startSystem(String exchangeServerName){

		
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(9090);

			ClientConnectionPool pool = new ClientConnectionPool(serverSocket);
		    pool.start();
		    
			pool.join();
			//Once pool is done, join will finish (before that, will be stuck at join)
		        
	        System.out.println("Closing " + exchangeServerName + " exchange server!");
		    serverSocket.close();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}

}
