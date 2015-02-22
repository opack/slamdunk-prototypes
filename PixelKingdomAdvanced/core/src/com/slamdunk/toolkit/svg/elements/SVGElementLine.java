package com.slamdunk.toolkit.svg.elements;

public class SVGElementLine extends SVGElement{

	public float x1; 
	public float y1; 
	public float x2; 
	public float y2;
	
	public SVGElementLine(SVGElement parent) {
		this(parent, 0, 0, 0, 0);
	}
	
	public SVGElementLine(SVGElement parent, float x1, float y1, float x2, float y2) {
		super(parent, "line");
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	@Override
	public void addAttribute(String attribute, float value){
		if ( "x1".equals(attribute) ){
			x1  = value;
		}else if ( "y1".equals(attribute) ){
			y1 = value;
		}else if ( "x2".equals(attribute) ){
			x2 = value;
		}else if ( "y2".equals(attribute) ){
			y2 = value;
		}
	}
	
	@Override
	public SVGElement deepCopy(){
		return super.deepCopy(new SVGElementLine(getParent(), x1, y1, x2, y2));
	}
	
}
