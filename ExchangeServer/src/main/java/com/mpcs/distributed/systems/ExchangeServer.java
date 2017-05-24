package com.mpcs.distributed.systems;

import resourcesupport.*;
import java.io.IOException;
import java.net.ServerSocket;
import superpeer.SuperPeer;

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
import com.mpcs.distributed.systems.services.ClientConnection;

@SpringBootApplication
public class ExchangeServer extends SpringBootServletInitializer {
	public static ExchangeTimer exchangeTimer = new ExchangeTimer();
	public static SuperPeer superPeer = null;
	public static ClientReplier clientReplier = new ClientReplier();
	public static SenderToSuper senderToSuper;
	public static Exchange[] neighborPeers = null;
    public static Exchange exchange = null;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ExchangeServer.class);
	}
	
	@Bean
	public CommandLineRunner initBeans(ApplicationContext ctx, UserRepository userRepository) {
		return (args) -> {
            AppContext.setApplicationContext(ctx);
			userRepository.save(new User("gopalp", exchange.toString()));
		};
	}

	public static void main(String[] args) {
		String exchangeServerName = null;
        try {
        	exchangeServerName = args[0];
        	exchange = Exchange.valueOf(exchangeServerName);
        	neighborPeers = exchange.neighbors();
        	senderToSuper = new SenderToSuper(exchange);
        } catch (IllegalArgumentException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: specified invalid exchange name. See resourcesupport.Exchange for list of valid names.");
            System.exit(1);
        }
        SpringApplication.run(ExchangeServer.class, args);
		startSystem(exchangeServerName);
	}
	
	private static void startSystem(String exchangeServerName){

		
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(exchange.portNum());
	    	while(!serverSocket.isClosed()){
				try {
		    		ClientConnection peerServer = new ClientConnection(serverSocket);
			        peerServer.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	        System.out.println("Closing " + exchangeServerName + " exchange server!");
		    serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}

}
