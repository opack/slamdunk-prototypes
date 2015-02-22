package com.slamdunk.toolkit.svg.elements;


public class SVGElementPolygon extends SVGElement{
	
	public float paths[];

	public SVGElementPolygon(SVGElement parent) {
		this(parent, null);
	}
	
	public SVGElementPolygon(SVGElement parent, float paths[]) {
		super(parent, "polygon");
		this.paths = paths;
	}
	
	@Override
	public SVGElement deepCopy(){
		SVGElementPolygon element   = (SVGElementPolygon) super.deepCopy(new SVGElementPolygon(getParent()));
		
		float pathCopy[] 	 		= new float[paths.length];
		System.arraycopy(paths, 0, pathCopy, 0, paths.length);
		
		element.paths 				= pathCopy;
		
		return element;
	}
	
}
