package com.slamdunk.toolkit.svg;

public class SVGPath {
	public char command;
	public float[] coordinates;

	public SVGPath(char attribute, float[] patch) {
		this.command = attribute;
		this.coordinates	   = patch;
	}

}
