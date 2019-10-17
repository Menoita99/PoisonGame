package game.controls;

import java.util.Random;

public class LevelData {

	private static LevelData INSTANCE;

	private final String[] LEVEL1 = new String[] {
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000001111111110000000000000000000000000000",
			"000000000000300000000000001111110000000000030000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000300000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000011110000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000001010000000000000000011110020111000000000000000000",
			"000000000000000011111100000000000000001111111100000000000000000000",
			"000000000000000000000000000000000000000011111000000000000000000000",
			"000000000000411110000000000000011110000000000000001111000000000000",
			"000000000001110000000000000011110000000000000000111110200010000000",
			"111111111111100000001111100000000000001000111111111111110000000000",
			"000000000000000000111100000000000000000000000000000000000100041111",
			"000000000000000000000000000000000000000000000000000000000111111100"
	};

	private final String[] LEVEL2 = new String[] {
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000001111111",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000011110000000000",
			"000000000300011000000011100000000000000000000000000000000000000000",
			"000000000000001111111110000000000000000011111000000000000000000000",
			"000000000000000000000000000000000000001111111100000000000000000000",
			"000000000000000000000000000000000000000011111000000000000000000000",
			"000000000000011110000000000000011110000000000000001111000000000000",
			"000000000000000000000000000001111111110000000000000000000000000000",
			"000000000000000000000000001111110000000000000000000000000000000000",
			"111111111111100000001111100000000000001000111111111111110000000000",
			"000000000000000000000000000000000000000000000000000000000000000000",
			"000000000000000000000000000000000000000000000000000000000000000000"
	};

	public static LevelData getInstance() {
		if(INSTANCE == null)INSTANCE = new LevelData();
		return INSTANCE;
	}

	public String[] getLevel(int i) {
		switch (i) {
		case 1:
			return LEVEL1;
		case 2:
			return LEVEL2;
		case 3:
			return levelGenerator(20, 250);
		default:
			throw new IllegalArgumentException("Unexpected value: " + i);
		}
	}
	
	
	
	//generator very simple don't have in count map coherence
	private String[] levelGenerator(int rows, int columns) {
		String[] level = new String[rows];
		for (int i = 0; i < rows; i++) {
			String col = "";
			
			for (int j = 0; j < columns; j++) {
				col+= new Random().nextDouble()/i<0.007 ? "1" : "0";
				if(col.charAt(j) == '0' &&  new Random().nextDouble()<0.001)
					col = new StringBuilder(col).replace(j, j, "2").toString();
					
			}
			level[i]=col;
		}
		
		return level;
	}
}