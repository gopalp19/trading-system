package com.mpcs.distributed.systems;


import java.io.IOException;
import java.net.ServerSocket;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;

@SpringBootApplication
public class ExchangeServer {

	public static void main(String[] args) {
		String exchangeServerName = args[0];

        SpringApplication.run(ExchangeServer.class, args);

        /*String url = "jdbc:mysql://exchange-server-db.cj3vzodbpmog.us-east-1.rds.amazonaws.com:3306/ExchangeServerDB";
        try {
			Connection con = DriverManager.getConnection(url, "master", "master123");
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
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
