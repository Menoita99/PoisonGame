package game.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IOManager {
	
	private static IOManager INSTANCE = null;
	private static String ROOT  = "src/main/resources/save";
	
	private List<File> files = new ArrayList<>();

	
	
	

	public IOManager() {
		loadSaves();
	}

	
	
	

	/**
	 * Loads 
	 */
	private void loadSaves() {
		// TODO Auto-generated method stub
		
	}

	
	//save game , load game
	

	/**
	 * @return the iNSTANCE
	 */
	public static IOManager getINSTANCE() {
		if(INSTANCE == null) INSTANCE = new IOManager();
		return INSTANCE;
	}
}
