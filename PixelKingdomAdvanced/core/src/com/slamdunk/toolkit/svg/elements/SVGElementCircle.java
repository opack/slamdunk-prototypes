package com.slamdunk.toolkit.svg.elements;

public class SVGElementCircle extends SVGElement{

	public float cx;
	public float cy;
	public float raio;
	
	public SVGElementCircle(SVGElement parent) {
		this(parent, 0,0,0);
	}
	
	public SVGElementCircle(SVGElement parent, float cx, float cy, float raio) {
		this(parent, "circle", cx, cy, raio);
	}
	
	public SVGElementCircle(SVGElement parent, String name, float cx, float cy, float raio) {
		super(parent, name);
		this.cx   = cx;
		this.cy   = cy;
		this.raio = raio;
	}
	
	@Override
	public void addAttribute(String attribute, float value){
		if ( "cx".equals(attribute) ){
			cx  = value;
		}else if ( "cy".equals(attribute) ){
			cy = value;
		}else if ( "cy".equals(attribute) ){
			raio = value;
		}
	}
	
	@Override
	public SVGElement deepCopy(){
		return super.deepCopy(new SVGElementCircle(getParent(), cx, cy, raio));
	}
}
