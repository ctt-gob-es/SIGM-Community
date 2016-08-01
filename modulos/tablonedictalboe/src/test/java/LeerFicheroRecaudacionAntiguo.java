import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class LeerFicheroRecaudacionAntiguo {

	public static void main(String[] args) {
		try {
			BufferedReader readbuffer = new BufferedReader(new FileReader ("C:\\Teresa\\borrar\\bop.wp5"));			
			String strRead = "";
			System.out.println ("Entrando") ;
			while (!((strRead = readbuffer.readLine()) == null)) {
				if(strRead.equals(" ")){
					System.out.println ("Nuevo párrafo");
				}
				else{
					StringBuffer stbParrado = new StringBuffer(strRead);
				} 
			} 
			System.out.println ("Cerrando") ;
			readbuffer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
