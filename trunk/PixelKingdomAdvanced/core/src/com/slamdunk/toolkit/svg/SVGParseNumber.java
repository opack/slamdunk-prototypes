package com.slamdunk.toolkit.svg;

public class SVGParseNumber {

	private static final String REGEX = "[\\s,]+"; 
	
	public static float[] parseNumbers(String val) {
		if ( val != null ){
			String[] parts   = val.split(REGEX);
			float[] numbers = new float[parts.length];
			
			for(int i = parts.length - 1; i >= 0; i--) {
				numbers[i] = Float.parseFloat(parts[i]);
			}
			
			return numbers;
		}
		return null;
	}

	public static int[] parseInts(String val) {
		if ( val != null ){
			String[] parts 	= val.split(REGEX);
			int[] numbers 	= new int[parts.length];
			
			for(int i = parts.length - 1; i >= 0; i--) {
				numbers[i] = Integer.parseInt(parts[i]);
			}
			
			return numbers;
		}
		return null;
	}

}
