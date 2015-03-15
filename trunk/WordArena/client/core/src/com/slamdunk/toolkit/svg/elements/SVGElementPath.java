package com.slamdunk.toolkit.svg.elements;

import com.slamdunk.toolkit.svg.SVGPath;

public class SVGElementPath extends SVGElement{
	
	public SVGPath[] path;

	public SVGElementPath(SVGElement parent) {
		this(parent, null);
	}
	
	public SVGElementPath(SVGElement parent, SVGPath[] path) {
		super(parent, "path");
		this.path	   = path;
	}
	
	@Override
	public SVGElement deepCopy(){
		SVGElementPath element   = (SVGElementPath) super.deepCopy(new SVGElementPath(getParent()));
		
		SVGPath[] pathCopy 		 = new SVGPath[path.length];
		System.arraycopy(path, 0, pathCopy, 0, path.length);
		
		element.path 		= pathCopy;
		
		return element;
	}
}
