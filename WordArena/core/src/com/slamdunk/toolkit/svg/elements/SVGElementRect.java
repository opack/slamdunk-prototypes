package com.slamdunk.toolkit.svg.elements;

public class SVGElementRect extends SVGElement{

	public float x; 
	public float y; 
	public float width; 
	public float height; 
	public float rx; 
	public float ry;
	
	public SVGElementRect(SVGElement parent) {
		this(parent, 0,0,0,0,0,0);
	}
	
	public SVGElementRect(SVGElement parent, float x, float y, float width, float height, float rx, float ry) {
		super(parent, "rect");
		this.x 		= x;
		this.y 		= y;
		this.width  = width;
		this.height = height;
		this.rx 	= rx;
		this.ry 	= ry;
	}
	
	@Override
	public void addAttribute(String attribute, float value){
		if ( "x".equals(attribute) ){
			x  = value;
		}else if ( "y".equals(attribute) ){
			y = value;
		}else if ( "width".equals(attribute) ){
			width = value;
		}else if ( "height".equals(attribute) ){
			height = value;
		}else if ( "rx".equals(attribute) ){
			rx = value;
		}else if ( "ry".equals(attribute) ){
			ry = value;
		}
	}
	
	@Override
	public SVGElement deepCopy(){
		return super.deepCopy(new SVGElementRect(getParent(), x, y, width, height, rx, ry));
	}
}
