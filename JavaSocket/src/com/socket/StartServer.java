package com.socket;

public class StartServer {

	public static void main(String[] args) {
		MultiThreadedServer server = new MultiThreadedServer(9000);
		new Thread(server).start();
//		Client client = new Client(9000); 
//		new Thread(client).start();
//		try {
//		    Thread.sleep(60 * 1000);
//		} catch (InterruptedException e) {
//		    e.printStackTrace();
//		}
//		System.out.println("Stopping Server");
////		server.stop();
//		new Thread(client).start();
	}
}
