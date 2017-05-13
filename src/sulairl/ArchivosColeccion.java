package sulairl;


/**
 * @author José Antonio Medina García
 */
public class ArchivosColeccion {

	private int numArchivo;
	private String rutaFichero;

	/**
	 * Constructor por defecto de un archivo de la colección. El número de archivo se pone a 0 y su ruta a la cadena vacía.
	 */
	
	public ArchivosColeccion(){
		numArchivo = 0;
		rutaFichero = "";
	}

	/**
	 * Constructor por parámetro de un archivo de colección.
	 * @param unNumArchivo Entero con el número de archivo
	 * @param unaRutaFichero String con la ruta en la que se encuentra el archivo.
	 */
	public ArchivosColeccion(int unNumArchivo, String unaRutaFichero){
		numArchivo = unNumArchivo;
		rutaFichero = unaRutaFichero;
	}

	/**
	 * Devuleve el número de un archivo de la colección.
	 * @return Entero con el número del archivo.
	 */
	
	public int getNumArchivo(){
		return numArchivo;
	}

	/**
	 * Devuelve la ruta de un fichero de la colección.
	 * @return String con la ruta de un fichero de la colección
	 */
	
	public String getRutaFichero(){
		return rutaFichero;
	}

	/**
	 * Establece el número del archivo para un fichero de la colección
	 * @param unNumArchivo Entero con el valor del número de archivo
	 */
	public void setNumArchivo(int unNumArchivo){
		numArchivo = unNumArchivo;
	}

	/**
	 * Establece una ruta para el archivo de la colección
	 * @param unaRutaFichero String con la ruta del archivo
	 */
	public void setRutaFichero(String unaRutaFichero){
		rutaFichero = unaRutaFichero;
	}
	
	/**
	 * Imprime por consola la información del archivo.
	 */
	
	public void imprimirArchivos(){
		System.out.println("Numero: " + numArchivo + " Path: " + rutaFichero);
	}

}