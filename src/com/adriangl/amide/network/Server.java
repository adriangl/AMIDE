package com.adriangl.amide.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

import testlwjgl.ObjModel;
import testlwjgl.Texture;

import com.adriangl.amide.gameelements.Asteroid;
import com.adriangl.amide.gameelements.GameElement;
import com.adriangl.amide.gameelements.GameElementList;

public class Server implements Runnable{
	
	private GameElementList gameElements;
	private static final int SERVER_PORT = 1234;
	
	private HashMap<SocketAddress, ArrayList<GameElement>> clientMap;
	
    DatagramPacket data = new DatagramPacket(new byte[284], 284);
    String elementData;
    
    Texture texture;
    ObjModel model;
    
    private static final int CLIENT_UPDATE = 1;
    private static final int CLIENT_BYE = 2;
	
	public Server (GameElementList gameElements){
		this.gameElements = gameElements;
		this.clientMap = new HashMap<SocketAddress, ArrayList<GameElement>>();

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
            DatagramSocket socket = new DatagramSocket(SERVER_PORT, InetAddress
                            .getByName("localhost"));
            
            while (true)
            {
                // Data receiving
                socket.receive(data);
                byte[] rawData = data.getData();
                
                // check if the client is already registered on the server
                // if it exist on the hashmap, we remove the entries from the
                // hashmap and from the game element's hashmap
                SocketAddress addr = data.getSocketAddress();
                ArrayList<GameElement> list = null;
                
                if (!clientMap.containsKey(addr)){
                	list = new ArrayList<GameElement>();
                	clientMap.put(addr, list);
                }
                else{
                	list = clientMap.get(addr);
                }
                
                // Remove selected asteroids from big list
                for (GameElement element: list){
                	gameElements.remove(element);
                }
                
                // We will now process new entries                
    			ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
    			DataInputStream in = new DataInputStream(bais);
    			
    			switch((int)in.readFloat()){
    			case 1:
    				readAvailableData(in,list);
    				break;
    			case 2:
    				break;
    			default:
    				break;
    			}
    			in.close();
    			bais.close();
                //(new Thread(new ResponseHandler(gameElements, elementData))).start();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
	}

	private void readAvailableData(DataInputStream in, ArrayList<GameElement> list) {
		// TODO Auto-generated method stub
		int floatRead = 0;

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
				
				switch (floatRead % 7){
				case 0:
					x = elementData;
					break;
				case 1:
					y = elementData;
					break;
				case 2:
					size = elementData;
					break;
				case 3:
					angle = elementData;
					break;
				case 4:
					speedX = elementData;
					break;
				case 5:
					speedY = elementData;
					break;
				case 6:
					rotateSpeed = elementData;
					Asteroid a = new Asteroid(texture, model, x,y,size,angle,speedX,speedY,rotateSpeed);
					gameElements.add(a);
					list.add(a);
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
