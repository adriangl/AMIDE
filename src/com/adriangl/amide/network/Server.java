package com.adriangl.amide.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

import testlwjgl.ObjModel;
import testlwjgl.Texture;

import com.adriangl.amide.constants.Constants;
import com.adriangl.amide.gameelements.AssetsProvider;
import com.adriangl.amide.gameelements.Asteroid;
import com.adriangl.amide.gameelements.GameElement;
import com.adriangl.amide.gameelements.GameElementList;

public class Server implements Runnable{
	
	private GameElementList gameElements;
	
	private HashMap<SocketAddress, Integer> clientMap;
	
    DatagramPacket data = new DatagramPacket(new byte[Constants.bufferSize], Constants.bufferSize);
    String elementData;
    
    Texture texture;
    ObjModel model;
    
    private static final int CLIENT_UPDATE = 1;
    private static final int CLIENT_BYE = 2;
	
	public Server (GameElementList gameElements){
		this.gameElements = gameElements;
		this.clientMap = new HashMap<SocketAddress, Integer>();

		for (int i = 0; i<gameElements.size(); i++){
			GameElement e = gameElements.get(i);
			if (e instanceof Asteroid){
				texture = ((Asteroid)e).getTexture();
				model = ((Asteroid)e).getModel();
				break;
			}
		}
	}

	@Override
	public void run() {
		try
        {
            DatagramSocket socket = new DatagramSocket(Constants.SERVER_PORT, InetAddress
                            .getByName(Constants.SERVER_HOST));
            
            while (true)
            {
                // Data receiving
                socket.receive(data);
                byte[] rawData = data.getData();
                
                // check if the client is already registered on the server
                // if it exist on the hashmap, we remove the entries from the
                // hashmap and from the game element's hashmap
                SocketAddress addr = data.getSocketAddress();
                int clientID = 0;
                
                if (!clientMap.containsKey(addr)){
                	clientID = (int)(10000*Math.random());
                	clientMap.put(addr, clientID);
                }
                else{
                	clientID = clientMap.get(addr);
                }
                
                // We will now process new entries                
    			ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
    			DataInputStream in = new DataInputStream(bais);
    			
    			synchronized(gameElements){
    				// Remove selected asteroids from big list
                    removeElementsFromList(clientID);
                    switch((int)in.readFloat()){
        			case CLIENT_UPDATE:
        					readAvailableData(in,clientID);
        					updateCollisions();
        					sendAsteroids(socket,addr, clientID);
        				break;
        			case CLIENT_BYE:
        				break;
        			default:
        				break;
        			}
        			in.close();
        			bais.close();  
    			}
                //(new Thread(new ResponseHandler(gameElements, elementData))).start();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
	}

	private void updateCollisions() {
		for (int i = 0; i<gameElements.size();i++){
			GameElement element = gameElements.get(i);
			for (int j = 0; j<gameElements.size();j++){
				GameElement other = gameElements.get(j);
				if (!element.equals(other) && element.collides(other)){
					element.collide(other);
				}
			}
		}
	}

	private void removeElementsFromList(int clientID) {
		synchronized(gameElements){
			ArrayList<GameElement> clientList = getClientList(clientID);
			gameElements.removeAll(clientList);
			clientList.clear();
		}
	}

	private ArrayList<GameElement> getClientList(int clientID) {
		ArrayList<GameElement> clientList = new ArrayList<GameElement>();
		for(int i = 0; i<gameElements.size();i++){
			GameElement e = gameElements.get(i);
			if (clientID == e.getClientID()){
				clientList.add(e);
			}
		}
		return clientList;
	}

	private void sendAsteroids(DatagramSocket socket, SocketAddress addr, int clientID) 
			throws IOException {
		// Encode the asteroids to send
		ByteArrayOutputStream baos = new ByteArrayOutputStream(Constants.bufferSize);
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeFloat(CLIENT_UPDATE);
		ArrayList<GameElement> clientList = getClientList(clientID);
        for (int i = 0; i<clientList.size(); i++) {
        	GameElement element = clientList.get(i);
        	if (element instanceof Asteroid){
        		byte[] encoded = ((Asteroid)element).encode();
        		baos.write(encoded);
        	}
        }
        
        byte[] rawData = baos.toByteArray();
        
        // Send asteroids
        DatagramPacket datagram = new DatagramPacket(rawData,
                rawData.length, addr);
        
        socket.send(datagram);
        baos.close();
        dos.close();		
	}

	private void readAvailableData(DataInputStream in, int clientID) {
		int floatRead = 0;
		
		int ID = 0;
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
					ID = (int)elementData;
					// client has no ID
					if (ID == 0){
						ID = clientID;
					}
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
							AssetsProvider.asteroidModel, x,y,size,angle,speedX,
							speedY,rotateSpeed);
					a.setClientID(ID);
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

}
