package com.adriangl.amide.network;

import java.util.ArrayList;

import com.adriangl.amide.gameelements.GameElement;

public class ResponseHandler implements Runnable{
	
	ArrayList<GameElement> gameElements;
	String data;
	
	public ResponseHandler (ArrayList<GameElement> gameElements, String data){
		this.data= data;
		this.gameElements = gameElements;
	}

	@Override
	public void run() {
	
	}

}
