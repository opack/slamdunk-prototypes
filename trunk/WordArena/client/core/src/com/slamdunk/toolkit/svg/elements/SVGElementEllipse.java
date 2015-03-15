package com.slamdunk.toolkit.svg.elements;

public class SVGElementEllipse extends SVGElementCircle{


	public SVGElementEllipse(SVGElement parent) {
		this(parent, 0, 0, 0);
	}
	
	public SVGElementEllipse(SVGElement parent, float cx, float cy, float raio) {
		super(parent, "ellipse", cx, cy, raio);
	}
	
	
	@Override
	public SVGElement deepCopy(){
		return super.deepCopy(new SVGElementEllipse(getParent(), cx, cy, raio));
	}
}
