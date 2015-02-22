package com.slamdunk.toolkit.svg;

public class SVGTransform {
	
	public String name;
	public float[] values;
	
	public SVGTransform(){}
	
	public SVGTransform(String name, float[] values){
		this.name   = name;
		this.values = values;
	}
	
}
