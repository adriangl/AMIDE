package com.adriangl.amide.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.lwjgl.opengl.Display;

import com.adriangl.amide.gameelements.Asteroid;
import com.adriangl.amide.gameelements.GameElement;
import com.adriangl.amide.gameelements.GameElementList;

public class Client implements Runnable{
	
	GameElementList gameElements;
	
	public static final int CLIENT_PORT = (int)(10000*Math.random());
	public static final int SERVER_PORT = 1234;
	
	private boolean finished = false;
	
	public Client (GameElementList gameElements){
		this.gameElements = gameElements;
	}

	@Override
	public void run() {
		try {
			DatagramSocket socket = new DatagramSocket(CLIENT_PORT, InetAddress
			                .getByName("localhost"));
			while(!finished){
				if (Display.isCloseRequested()){
					finished = true;
				}
				else{
					// Encode the asteroids to send
					ByteArrayOutputStream baos = new ByteArrayOutputStream(284);
					DataOutputStream dos = new DataOutputStream(baos);
					dos.writeFloat(1);
		            for (int i = 0; i<gameElements.size(); i++) {
		            	GameElement element = gameElements.get(i);
		            	if (element instanceof Asteroid){
		            		byte[] encoded = ((Asteroid)element).encode();
		            		baos.write(encoded);
		            	}
		            }
		            
		            byte[] rawData = baos.toByteArray();
		            
		            DatagramPacket datagram = new DatagramPacket(rawData,
		                    rawData.length, InetAddress.getByName("localhost"),
		                    SERVER_PORT);
		            
		            socket.send(datagram);
		            baos.close();
		            dos.close();
		            // We send packets 20 times a second
		            Thread.sleep(50);
				}
			}
			
			// Send last message, when close is requested
			ByteArrayOutputStream baos = new ByteArrayOutputStream(284);
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeFloat(2);
			byte[] rawData = baos.toByteArray();
			DatagramPacket datagram = new DatagramPacket(rawData,
                    rawData.length, InetAddress.getByName("localhost"),
                    SERVER_PORT);
            
            socket.send(datagram);
            baos.close();
            dos.close();
            
            System.exit(0);
			
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
