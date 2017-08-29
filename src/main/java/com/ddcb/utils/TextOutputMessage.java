package com.ddcb.utils;

public class TextOutputMessage extends OutputMessage {  
	  
  	private static final long serialVersionUID = 1L;
	  
    private String MsgType = "text";  
      
    private String Content;  
  
    public TextOutputMessage() {}  

    public TextOutputMessage(String content) {  
        Content = content;  
    }  
    
    @Override  
    public String getMsgType() {  
        return MsgType;  
    }  
  
    public String getContent() {  
        return Content;  
    }  
  
    public void setContent(String content) {  
        Content = content;  
    }  
}  