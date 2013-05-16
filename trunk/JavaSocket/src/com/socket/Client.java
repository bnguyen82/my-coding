package com.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class Client implements Runnable {
	private int port;
	private Socket socket = null;
	public Client(int port) {
		this.port = port;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void run() {
		try{
			socket = new Socket("localhost", port);
			OutputStream out = socket.getOutputStream();
			out.write("some data".getBytes());
			out.flush();
			out.close();
	
//			socket.close();
		}
		catch(IOException e){
			
		}
	}

}
