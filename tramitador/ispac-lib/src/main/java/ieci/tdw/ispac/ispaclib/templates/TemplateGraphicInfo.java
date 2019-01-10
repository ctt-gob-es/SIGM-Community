package ieci.tdw.ispac.ispaclib.templates;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class TemplateGraphicInfo {

	private String url;
	private String graphicUrl;
	private boolean asLink;
	private int width;
	private int height;
	private boolean centered;
	
	public TemplateGraphicInfo(String url, boolean asLink, int width, int height) {
		setUrl(url);		
		setAsLink(asLink);
		this.width = width;
		this.height = height;
		this.centered = false;
	}	
	
	public TemplateGraphicInfo() {
		url = null;
		graphicUrl = null;
		asLink = false;
		width = 0;
		height = 0;
		centered = false;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
		this.graphicUrl = "file://" + url;
	}
	public String getGraphicUrl() {
		return graphicUrl;
	}
	public void setGraphicUrl(String graphicUrl) {
		this.graphicUrl = graphicUrl;
	}
	public boolean isAsLink() {
		return asLink;
	}
	public void setAsLink(boolean asLink) {
		this.asLink = asLink;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isCentered() {
		return centered;
	}

	public void setCentered(boolean centered) {
		this.centered = centered;
	}	
}
