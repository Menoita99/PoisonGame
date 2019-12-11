package game.io;

import java.io.FileWriter;
import java.io.IOException;

public class IOManager {
	
	private static IOManager INSTANCE = null;
	private static String ROOT  = "src/main/resources/save/";
	
	
	

	public IOManager() {
		loadSlots();
	}

	
	
	

	/**
	 * Loads 
	 */
	//save game , load game
	private void loadSlots() {
	
		
	}

	
	
	/**
	 * Creates an slot file if it exits overwrites it 
	 * @param slot slot number
	 * @throws IOException
	 */
	public void startNewGame(int slot) throws IOException {
		FileWriter writer = new FileWriter(ROOT+"slot"+slot+".txt");
		writer.write("");  
		writer.close();
	}
	
	
	
	
	

	/**
	 * @return the iNSTANCE
	 */
	public static IOManager getINSTANCE() {
		if(INSTANCE == null) INSTANCE = new IOManager();
		return INSTANCE;
	}
	
	public static void main(String[] args) {
		try {
			IOManager.getINSTANCE().startNewGame(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
