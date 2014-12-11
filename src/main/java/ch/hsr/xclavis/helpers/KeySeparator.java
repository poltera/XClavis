package ch.hsr.xclavis.helpers;

public class KeySeparator {
		
	public static String[] getSeparated(String string, int length) {
		//Specifically worth
		double blockscalc = (double) string.length() / (double) length;
		//Round up
		int blocksnumber = (int) StrictMath.ceil(blockscalc);
		
		String[] blocks = new String[blocksnumber];
		
		for (int i = 0; i < blocksnumber; i++) {
			int start = i * length;
			int end = i * length + length;
			
			if (end < string.length()) {
				blocks[i] = string.substring(start, end);
			} else {
				blocks[i] = string.substring(start);
			}
		}
		
		return blocks;
	}
	
	public static String putTogether(String[] blocks) {		
		String string = "";
		
		for (String block : blocks) {
			string += block;
		}
		
		return string;
	}
}

