package com.adriangl.amide.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.lwjgl.opengl.Display;

import com.adriangl.amide.constants.Constants;
import com.adriangl.amide.gameelements.Asteroid;
import com.adriangl.amide.gameelements.GameElement;
import com.adriangl.amide.gameelements.GameElementList;
import com.adriangl.amide.gameelements.AssetsProvider;

public class Client implements Runnable{
	
	GameElementList gameElements;
	
	public static final int CLIENT_PORT = (int)(10000*Math.random());
	
	private boolean finished = false;
	
	private static final float UPDATES_PER_SECOND = 20f;
	private static final int SLEEP_TIME = (int)(1000/UPDATES_PER_SECOND); //milliseconds
	
	private static final int CLIENT_UPDATE = 1;
	private static final int CLIENT_BYE = 2;
	
	public Client (GameElementList gameElements){
		this.gameElements = gameElements;
	}

	@Override
	public void run() {
		try {
			DatagramSocket socket = new DatagramSocket(CLIENT_PORT, InetAddress
			                .getByName(Constants.SERVER_HOST));
			while(!finished){
				if (Display.isCloseRequested()){
					finished = true;
				}
				else{
					synchronized(gameElements){
						sendAsteroids(socket);
						receiveAsteroids(socket);
					}
		            // We send packets 20 times a second
		            Thread.sleep(SLEEP_TIME);
				}
			}
			// Send last message when close is requested
			sendByeToServer(socket);
            socket.close();
            
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

	private void receiveAsteroids(DatagramSocket socket) throws IOException {
		DatagramPacket data = new DatagramPacket(new byte[Constants.bufferSize],
				Constants.bufferSize);
		socket.receive(data);
        byte[] rawData = data.getData();
        // We will now process new entries                
		ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
		DataInputStream in = new DataInputStream(bais);
		if ((int)in.readFloat() == CLIENT_UPDATE){
			readAvailableData(in);
		}
	}

	private void readAvailableData(DataInputStream in) {
		gameElements.clear();
		
		int floatRead = 0;
		
		int clientID = 0;
		float x = 0;
		float y = 0;
		float size = 0;
		float angle = 0;
		float speedX = 0;
		float speedY = 0;
		float rotateSpeed = 0;
		
		try {
			while (in.available() > 0) {
				float elementData = in.readFloat();
				
				switch (floatRead % Constants.asteroidFields){
				case 0:
					clientID = (int)elementData;
					break;
				case 1:
					x = elementData;
					break;
				case 2:
					y = elementData;
					break;
				case 3:
					size = elementData;
					break;
				case 4:
					angle = elementData;
					break;
				case 5:
					speedX = elementData;
					break;
				case 6:
					speedY = elementData;
					break;
				case 7:
					rotateSpeed = elementData;
					Asteroid a = new Asteroid(AssetsProvider.asteroidTexture, 
							AssetsProvider.asteroidModel,x,y,size,angle,speedX,
							speedY,rotateSpeed);
					a.setClientID(clientID);
					gameElements.add(a);
					break;
				default:
					break;
				}
				floatRead++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendByeToServer(DatagramSocket socket) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(Constants.bufferSize);
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeFloat(CLIENT_BYE);
		byte[] rawData = baos.toByteArray();
		DatagramPacket datagram = new DatagramPacket(rawData,
                rawData.length, InetAddress.getByName(Constants.SERVER_HOST),
                Constants.SERVER_PORT);
        
        socket.send(datagram);
        baos.close();
        dos.close();		
	}

	private void sendAsteroids(DatagramSocket socket) throws IOException {
		// Encode the asteroids to send
		ByteArrayOutputStream baos = new ByteArrayOutputStream(Constants.bufferSize);
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeFloat(CLIENT_UPDATE);
        for (int i = 0; i<gameElements.size(); i++) {
        	GameElement element = gameElements.get(i);
        	if (element instanceof Asteroid){
        		byte[] encoded = ((Asteroid)element).encode();
        		baos.write(encoded);
        	}
        }
        
        byte[] rawData = baos.toByteArray();
        
        // Send asteroids
        DatagramPacket datagram = new DatagramPacket(rawData,
                rawData.length, InetAddress.getByName(Constants.SERVER_HOST),
                Constants.SERVER_PORT);
        
        socket.send(datagram);
        baos.close();
        dos.close();		
	}

}
