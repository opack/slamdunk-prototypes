package com.slamdunk.toolkit.svg.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.slamdunk.toolkit.svg.SVGStyle;
import com.slamdunk.toolkit.svg.SVGTransform;

public class SVGElement{
	
	private String 	 		name;
	private String 	 		id;
	private String 	 		hRef;
	private SVGStyle[] 		style; 
	private SVGTransform[]  transforms; 
	private StringBuilder   styleInline;
	private SVGElement		parent;
	private Map<String, String>	extraAttributes;
	
	protected ArrayList<SVGElement> children;
	
	public SVGElement(SVGElement parent, String name){
		this.parent = parent;
		this.name = name;
	}
	
	public SVGElement getParent() {
		return parent;
	}

	public void setParent(SVGElement parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String gethRef() {
		return hRef;
	}

	public void sethRef(String hRef) {
		if ( hRef != null ){
		   if ( hRef.startsWith("#") ){	
			   this.hRef = hRef.substring(1);//#link
		   }else{
			   this.hRef = hRef;
		   }
		}
	}

	public SVGStyle[] getStyle() {
		return style;
	}

	public void setStyle(SVGStyle[] style) {
		this.style = style;
	}

	public SVGTransform[] getTransforms() {
		return transforms;
	}

	public void setTransforms(SVGTransform[] transforms) {
		this.transforms = transforms;
	}

	public ArrayList<SVGElement> getChildren() {
		return children;
	}
	
	public SVGElement getChildById(String id) {
		if (id != null && children != null) {
			for (SVGElement child : children) {
				if (child.getId().equals(id)) {
					return child;
				}
			}
		}
		return null;
	}
	
	public void addStyleInLine(String key, String value){
		if ( styleInline == null ){
			styleInline = new StringBuilder();
		}
		styleInline.append(key).append(":").append(value);
	}
	
	public void setStyleInline(StringBuilder styleInline){
		this.styleInline=styleInline;
	}
	
	public String getStyleInLine(){
		return styleInline != null ? styleInline.toString() : null;
	}
	
	public boolean hasExtraAttributes() {
		return extraAttributes != null && !extraAttributes.isEmpty();
	}
	
	public String getExtraAttribute(String key) {
		if (extraAttributes == null) {
			return null;
		}
		return extraAttributes.get(key);
	}

	public void addChild(SVGElement element) {
		if ( children == null ){
			children = new ArrayList<SVGElement>();
		}
		children.add(element);
	}
	
	public void addAttribute(String attribute, float value){
		
	}
	
	/**
	 * Appelée lorsque des attributs hors SVG sont trouvés.
	 * Le comportement par défaut consiste à les placer dans
	 * la table extraAttributes.
	 * @param attribute
	 * @param value
	 */
	public void addExtraAttribute(String attribute, String value){
		if (extraAttributes == null) {
			extraAttributes = new HashMap<String, String>();
		}
		extraAttributes.put(attribute, value);
	}
	
	@Override
	public boolean equals(Object value){
		return name.equals(value);
	}
	
	public SVGElement deepCopy(){
		return deepCopy(new SVGElement(parent, name));
	}
	
	public SVGElement deepCopy(SVGElement element){
		element.setId(id);
		element.sethRef(hRef);
		element.setStyleInline(styleInline);
		
		if ( style != null ){
			SVGStyle[] styleCopy = new SVGStyle[style.length];
			System.arraycopy(style, 0, styleCopy, 0, style.length);
			element.setStyle(styleCopy);
		}
		
		if ( transforms != null ){
			SVGTransform[] transformCopy = new SVGTransform[transforms.length];
			System.arraycopy(transforms, 0, transformCopy, 0, transforms.length);
			element.setTransforms(transformCopy);
		}
		
		if ( children != null ){
			int length = children.size();
			for ( int x=0; x<length; x++){
				element.addChild(children.get(x).deepCopy());
			}
		}
		
		return element;
	}
}
