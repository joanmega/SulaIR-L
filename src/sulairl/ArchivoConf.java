package sulairl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author José Antonio Medina García
 */
public class ArchivoConf {
	private String carpetaOriginal;
	private int numDocumentos;
	private String stopWords;
	private int etapa;
	private String carpetaColec;
	private String idioma;

	/**
	 * Constructor por defecto del archivo de configuración
	 */
	
	public ArchivoConf(){
		carpetaOriginal = "";
		numDocumentos = 0;
		stopWords = "";
		etapa = 0;
		carpetaColec = "";
		idioma = "";
	}

	/**
	 * Crea una instacia con la información que guardará el archivo de configuración.
	 * @param unaCarpetaOri Dirección donde se encuentran los archivos originales de la colección.
	 * @param unNumDocumentos Número de documentos que componen la colección.
	 * @param unStopWord 1 si hay archivo stopword, 0 en caso contrario.
	 * @param unaEtapa Indica la etapa de progreso en la que se encuentra la colección. 0 = preprocesamiento de documentos, 1 = Creación del índice, 2 = Consultas.
	 * @param unaCarpetaColec Indica la carpeta actual de la colección.
	 * @param unIdioma Indica el idioma en la que se ha procesado la colección.
	 */
	
	
	public ArchivoConf(String unaCarpetaOri, int unNumDocumentos, String unStopWord, int unaEtapa, String unaCarpetaColec, String unIdioma){
		carpetaOriginal = unaCarpetaOri;
		numDocumentos = unNumDocumentos;
		stopWords = unStopWord;
		etapa = unaEtapa;
		carpetaColec = unaCarpetaColec;
		idioma = unIdioma;
	}
	
	/**
	 * Devuelve la carpeta que contiene los archivos originales de la colección.
	 * @return String con el path de la carpeta original de los documentos.
	 */
	
	public String getCarpetaOriginal(){
		return carpetaOriginal;
	}
	
	/**
	 * Devuelve el número de documentos que componen la colección.
	 * @return Entero que indica el número de documentos de la colección.
	 */
	
	public int getNumDocumentos(){
		return numDocumentos;
	}
	
	/**
	 * Devuelve el nombre del archivo Stopword, si se ha utilizado.
	 * @return String con el nombre del archivo StopWord.
	 */
	
	public String getStopWord(){
		return stopWords;
	}
	
	/**
	 * Devuelve la última etapa a la que se ha llegado en el proceso
	 * @return 0 = preprocesamiento de documentos, 1 = Creación del índice, 2 = Consultas
	 */
	
	public int getEtapa(){
		return etapa;
	}
	
	/**
	 * Devuelve la carpeta donde se encuentra la colección.
	 * @return String con la ruta de la carpeta original
	 */
	
	public String getCarpetaColec(){
		return carpetaColec;
	}
	
	/**
	 * Devuelve el idioma en el que fue realizado el procesamiento de la colección
	 * @return String cuyo valor es: es = español, en = inglés.
	 */
	
	public String getIdioma(){
		return idioma;
	}
	
	/**
	 * Establece la carpeta original donde se encuentra la colección.
	 * @param unaCarpetaOri Dirección donde se encuentran los archivos originales de la colección
	 */
	
	public void setCarpetaOriginal(String unaCarpetaOri){
		carpetaOriginal = unaCarpetaOri;
	}
	
	/**
	 * Establece el número de documentos que componen la colección.
	 * @param unNumDocumentos Número de documentos que componen la colección.
	 */
	
	public void setNumDocumentos(int unNumDocumentos){
		numDocumentos = unNumDocumentos;
	}
	
	/**
	 * Establece el nombre del archivo StopWord
	 * @param unStopWord nombre del archivo StopWord.
	 */
	
	public void setStopWord(String unStopWord){
		stopWords = unStopWord;
	}
	
	/**
	 * Establece la etapa actual del procesamiento de la colección.
	 * @param unaEtapa Indica la etapa de progreso en la que se encuentra la colección. 0 = preprocesamiento de documentos, 1 = Creación del índice, 2 = Consultas.
	 */
	
	public void setEtapa(int unaEtapa){
		etapa = unaEtapa;
	}
	
	/**
	 * Establece la carpeta actual de la colección
	 * @param unaCarpetaColec Indica la carpeta actual de la colección.
	 */
	
	public void setCarpetaColec(String unaCarpetaColec){
		carpetaColec = unaCarpetaColec;
	}
	
	/**
	 * Establece el idioma en el que se procesa la colección
	 * @param unIdioma Indica el idioma en la que se ha procesado la colección.
	 */
	
	public void setIdioma(String unIdioma){
		idioma = unIdioma;
	}
	
	/**
	 * Guarda la información del archivo de configuración en un archivo físico en la carpeta de la colección.
	 * @param nombreArchivo String con el nombre del archivo de configuración. Normalmente será igual que el nombre de la colección.
	 * @return true si el guardado ha sido correcto, false en caso contrario.
	 */
	
	public boolean guardarConfiguración(String nombreArchivo){
		try {
			List<String> lineas = new ArrayList<>();
			lineas.add(getCarpetaOriginal());	// Directorio original.
			lineas.add("" + getNumDocumentos());	// Número de documentos.
			lineas.add(getStopWord());
			lineas.add("" + getEtapa());		// Etapa del proceso
			lineas.add(getCarpetaColec());	// Carpeta de la colección
			lineas.add(getIdioma());
			File f = new File("./colecciones/" + nombreArchivo + "/" + nombreArchivo + ".conf");
			FileUtils.writeLines(f, lineas);
			return true;
		} catch (IOException ex) {
			Logger.getLogger(ArchivoConf.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}
	
	/**
	 * Lee la información de un archivo de configuración y lo almacena en un objeto de este tipo de dato
	 * @param nombreArchivo nombre del archivo de configuración que normalmente será igual al nombre de la colección.
	 * @return true si la carga ha sido correcta, false en caso contrario.
	 */
	
	public boolean leerConfiguración(String nombreArchivo){
		try {
			List<String> lineas = new ArrayList<>();
			lineas = FileUtils.readLines(new File("./colecciones/" + nombreArchivo + "/" + nombreArchivo + ".conf"), "UTF-8");
			setCarpetaOriginal(lineas.get(0));	// Directorio original.
			setNumDocumentos(Integer.parseInt(lineas.get(1)));	// Número de documentos.
			setStopWord(lineas.get(2));
			setEtapa(Integer.parseInt(lineas.get(3)));		// Etapa del proceso
			setCarpetaColec(lineas.get(4));	// Carpeta de la colección
			setIdioma(lineas.get(5));
			return true;
		} catch (IOException ex) {
			Logger.getLogger(ArchivoConf.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}
	
	/**
	 * Imprime por pantalla la información de un archivo de configuración.
	 */
	
	public void imprimirArchivoConf(){
		System.out.println("Ruta original: " + getCarpetaOriginal());
		System.out.println("Número de documentos: " + getNumDocumentos());
		System.out.println("Archivo StopWords: " + getStopWord());
		System.out.println("Etapa actual: " + getEtapa());
		System.out.println("Carpeta de la colección: " + getCarpetaColec());
		System.out.println("Idioma de la colección: " + getIdioma());
	}
}
