package com.adriangl.amide.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.adriangl.amide.gameelements.Asteroid;
import com.adriangl.amide.gameelements.GameElement;
import com.adriangl.amide.gameelements.GameElementList;

public class Client implements Runnable{
	
	GameElementList gameElements;
	
	public static final int CLIENT_PORT = 2348;
	public static final int SERVER_PORT = 1234;
	
	public Client (GameElementList gameElements){
		this.gameElements = gameElements;
	}

	@Override
	public void run() {
		try {
			DatagramSocket socket = new DatagramSocket(CLIENT_PORT, InetAddress
			                .getByName("localhost"));
			while(true){
			// Encode the asteroids to send
			ByteArrayOutputStream baos = new ByteArrayOutputStream(280);
            for (int i = 0; i<gameElements.size(); i++) {
            	GameElement element = gameElements.get(i);
            	if (element instanceof Asteroid){
            		byte[] encoded = ((Asteroid)element).encode();
            		baos.write(encoded);
            	}
            }
            
            byte[] rawData = baos.toByteArray();
            
            System.out.println(rawData.length);
            
            DatagramPacket datagram = new DatagramPacket(rawData,
                    rawData.length, InetAddress.getByName("localhost"),
                    SERVER_PORT);
            
            socket.send(datagram);
            Thread.sleep(50);
			}
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
