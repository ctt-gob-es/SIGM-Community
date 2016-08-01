package test;

import java.util.Scanner; 
import java.io.File; 
import java.io.FileNotFoundException;

/**
 *  Esta clase ilustra cómo explorar la información textual almacenada en un fichero
 *  de datos
 */

public class PruebaE {

    /*
     * Atributos o campos de cada objeto con el siguiente significado:
     *     [numLineas] : número de líneas del texto asociado al objeto
     *     [numCaracteres] : número de caracteres del texto asociado al objeto
     *     [frecuencias] : tabla de frecuencias de cada una de las letras en el texto
     *                     asociado al objeto
     */
    private int numLineas = 0, numCaracteres = 0;
    private int[] frecuencias = new int['Z'-'A'+1];

    /**
     *  Crea un objeto de la clase Texto, analiza el fichero de texto de nombre [nombreTexto]
     *  y, para ese fichero, calcula los valores de los atributos [numLineas], [numCaracteres]
     *  y [frecuencias] y les asigna el valor que les corresponde
     */
    public PruebaE (String nombreTexto) {
        try {
            Scanner f = new Scanner(new File(nombreTexto));
            while (f.hasNextLine()) {
                String linea = f.nextLine().toUpperCase();
                numLineas++;
                numCaracteres += linea.length();
                for (int i=0; i<linea.length(); i++)
                    if ((linea.charAt(i)>='A') && (linea.charAt(i)<='Z'))
                        frecuencias[linea.charAt(i)-'A']++;
            }
        }
        catch (FileNotFoundException e) {
            System.out.printf("El fichero %s no ha podido ser abierto%n", nombreTexto);
        }
    }

    /**
     * Pre: ---
     * Post: Devuelve el número de líneas del fichero asociado al objeto
     */
    public int lineas () {
        return numLineas;
    }

    /**
     * Pre: ---
     * Post: Devuelve el número de caracteres que almacena el fichero asociado al objeto
     */
    public int caracteres () {
        return numCaracteres;
    }

    /**
     * Pre: letra>='A' y letra<='Z'
     * Post: Devuelve el número de veces que está almacenada en el fichero asociado al objeto
     *       la letra [letra], sin hacer distinción entre mayúsculas y minúsculas
     */
    public int frecuencia (char letra) {
        return frecuencias[letra-'A'];
    }
    
    /**
     * Pre: --
     * Post: Presenta por pantalla información sobre el número de líneas, el número
     *       de caracteres y el número de veces que figura cada letra en el fichero
     *       "datos\alumnos.txt"
     */
    public static void main(String[] args) {
        String nombre = "C:\\Teresa\\Cursos\\Curso INAP\\Curso Firma\\semana 2\\prueba.txt";
        PruebaE texto = new PruebaE(nombre);
        System.out.printf("El fichero %s almacena un texto%n" +
                          "con %d caracteres escritos en %d líneas%n%n",
                          nombre, texto.caracteres(), texto.lineas());
        for (char letra='A'; letra<='Z'; letra++)
            System.out.printf("%c - %4d%n", letra, texto.frecuencia(letra));
    }

}
