package com.socket;

public class StartClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = new Client(9000); 
		new Thread(client).start();
	}

}
