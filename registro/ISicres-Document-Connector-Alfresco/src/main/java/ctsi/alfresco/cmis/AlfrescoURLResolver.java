package ctsi.alfresco.cmis;

public class AlfrescoURLResolver {
	
	/*
	CMIS 1.0
	    For Alfresco 3.x : http://[host]:[port]/alfresco/service/cmis
	    For Alfresco 4.0.x, Alfresco 4.1.x and Alfresco 4.2.a-c: http://[host]:[port]/alfresco/cmisatom
	    For Alfresco 4.2.d-f and Alfresco 5.0: http://[host]:[port]/alfresco/api/-default-/public/cmis/versions/1.0/atom
	CMIS 1.1
	    For Alfresco 4.2.d-f and Alfresco 5.0: http://[host]:[port]/alfresco/api/-default-/public/cmis/versions/1.1/atom	
	 */
	public static String getCmisURL(String alfrescoVersion) {
		if (alfrescoVersion.startsWith("3")) {
			return "/alfresco/service/cmis";
		} else if (alfrescoVersion.startsWith("4.0") || 
				   alfrescoVersion.startsWith("4.1") ||
				   alfrescoVersion.toLowerCase().equals("4.2.a") || 
				   alfrescoVersion.toLowerCase().equals("4.2.b") || 
				   alfrescoVersion.toLowerCase().equals("4.2.c")) {
			return "/alfresco/cmisatom";
	    } else if (alfrescoVersion.startsWith("4.2") || 
	    		   alfrescoVersion.startsWith("5")) {
			return "/alfresco/api/-default-/public/cmis/versions/1.1/atom";
		} else {
			return null;
		}
	}
}
