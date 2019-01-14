package core.tools;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class BaseResourceBoundle extends ResourceBundle{
    
    private ResourceBundle parent;
    
    public BaseResourceBoundle(){        
    }

    protected Object handleGetObject(String key) {        
        return null;
    }

    public Enumeration getKeys() {
        return null;
    }

    public ResourceBundle getParent() {
        return parent;
    }

    public void setParent(ResourceBundle parent) {
        this.parent = parent;
    }
    
    public String getProperty(String msgID){
        return parent.getString(msgID);
    }

}
