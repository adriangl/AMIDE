package com.adriangl.amide.gameelements;

import java.util.ArrayList;
import java.util.Iterator;

public class GameElementList{
	
	private ArrayList<GameElement> elementList;
	
	public GameElementList(){
		elementList = new ArrayList<GameElement>();
	}
	
	public void add(GameElement e){
		synchronized(this){
			elementList.add(e);
		}
	}
	
	public void remove(GameElement e){
		synchronized(this){
			elementList.remove(e);
		}
	}

	public int size(){
		int size;
		synchronized(this){
			size = elementList.size();
		}
		return size;
	}
	
	public GameElement get(int idx){
		return elementList.get(idx);
	}

}
