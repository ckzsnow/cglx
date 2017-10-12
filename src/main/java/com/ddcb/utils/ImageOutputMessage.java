package com.ddcb.utils;

public class ImageOutputMessage extends OutputMessage {  
	  
  	private static final long serialVersionUID = 1L;
	  
    private String MsgType = "image";
    
    private Image Image;  
        
	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}

	@Override  
    public String getMsgType() {  
        return MsgType;  
    } 
}  