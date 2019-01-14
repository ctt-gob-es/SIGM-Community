package es.sigem.dipcoruna.desktop.editlauncher.service.holder;



public class EditorInstanceContextHolder {
	private static EditorInstanceContext contextHolder = new EditorInstanceContext();	

	
	public static EditorInstanceContext getContext() {		
		return contextHolder;
	}
	
	public static void setContext(EditorInstanceContext context) {
		contextHolder = context;
	}		
}
