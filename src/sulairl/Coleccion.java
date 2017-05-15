package sulairl;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


/**
 * @author José Antonio Medina García
 */

public class Coleccion {

	private String nombre;
	private String rutaColeccion;
	private int etapa;
	private File stopWords;
	private String idioma;
	private Map<Integer, ArchivosColeccion> archivoOriginal;
	private Map<Integer, ArchivosColeccion> archivoTokenizado;
	private Map<Integer, ArchivosColeccion> archivoStopWord;
	private Map<Integer, ArchivosColeccion> archivoStemizado;
	private ArrayList<ArchivosColeccion> docOriginales;

	/**
	 * 
	 * Constructor por parámetros de la clase Coleccion con archivo stopWord.
	 * @param unNombre String con el nombre de la colección.
	 * @param unaRutaColeccion String con la ruta donde se ubicará la colección.
	 * @param unaEtapa Entero que representa la etapa en la que se encuentra el procesamiento de la colección.<p> 0 = preprocesamiento<p> 1 = creación del índice<p> 2 = consultas<p>
	 * @param rutaStopWord String con la ruta de la ubicación del archivo de palabras vacías.
	 * @param unIdioma Idioma en el que se encuentra procesada la colección.<p> 'es' = Español<p> 'en' = Inglés<p>
	 * @param unosDocOriginales Array que contiene los documentos originales de la colección.
	 */
	
	public Coleccion(String unNombre, String unaRutaColeccion, int unaEtapa, String rutaStopWord, String unIdioma, ArrayList<ArchivosColeccion> unosDocOriginales) throws IOException{
		nombre = unNombre;
		rutaColeccion = unaRutaColeccion;
		etapa = unaEtapa;
		idioma = unIdioma;
		
		archivoTokenizado = new HashMap<>();
		archivoOriginal = new HashMap<>();
		archivoStopWord = new HashMap<>();
		archivoStemizado = new HashMap<>();
		docOriginales = unosDocOriginales;
		
		crearDirectorios(unaRutaColeccion);
		
		// Copiamos el archivo de las palabras vacías al directorio de trabajo.
		stopWords = new File(rutaColeccion + "archivos/archivoStopWords/" + new File(rutaStopWord).getName());
		Files.copy(new File(rutaStopWord).getAbsoluteFile(), stopWords);
	}
	
	/**
	 * 
	 * Constructor por parámetros de la clase Coleccion sin archivo stopWord.
	 * @param unNombre String con el nombre de la colección.
	 * @param unaRutaColeccion String con la ruta donde se ubicará la colección.
	 * @param unaEtapa Entero que representa la etapa en la que se encuentra el procesamiento de la colección.<p> 0 = preprocesamiento<p> 1 = creación del índice<p> 2 = consultas<p>
	 * @param unIdioma Idioma en el que se encuentra procesada la colección.<p> 'es' = Español<p> 'en' = Inglés<p>
	 * @param unosDocOriginales Array que contiene los documentos originales de la colección.
	 */
	
	public Coleccion(String unNombre, String unaRutaColeccion, int unaEtapa, String unIdioma, ArrayList<ArchivosColeccion> unosDocOriginales){
		nombre = unNombre;
		rutaColeccion = unaRutaColeccion;
		etapa = unaEtapa;
		stopWords = null;
		idioma = unIdioma;
		
		archivoTokenizado = new HashMap<>();
		archivoOriginal = new HashMap<>();
		archivoStopWord = new HashMap<>();
		archivoStemizado = new HashMap<>();
		docOriginales = unosDocOriginales;
		
		crearDirectorios(unaRutaColeccion);
	}
	
	/**
	 * Crea una colección con los siguientes parámetros.
	 * @param unNombre String con el nombre de la colección
	 * @param unaEtapa Entero con la etapa actual de la colección
	 * @param unIdioma String con el idioma con el que se crea la colección.<p> "es" = Español<p> "en" = Inglés<p>
	 * @param unosDocOriginales Array con los archivos originales de la colección.
	 * @param unStopword String con el nombre del archivo de StopWord
	 */
	
	public Coleccion(String unNombre, int unaEtapa, String unIdioma, ArrayList<ArchivosColeccion> unosDocOriginales, String unStopword){
		nombre = unNombre;
		rutaColeccion = "./colecciones/" + unNombre+ "/";
		etapa = unaEtapa;
		
		if (unStopword.equals("null")){
			stopWords = null;
		}else{
			stopWords = new File("./colecciones/"+nombre+"/archivos/archivoStopWords/"+unStopword);
		}
		idioma = unIdioma;
		
		archivoTokenizado = new HashMap<>();
		archivoOriginal = new HashMap<>();
		archivoStopWord = new HashMap<>();
		archivoStemizado = new HashMap<>();
		docOriginales = unosDocOriginales;
		
		addFiles(new File("./colecciones/"+nombre+"/archivos/tokenizados"), archivoTokenizado);
		addFiles(new File("./colecciones/"+nombre+"/archivos/originales"), archivoOriginal);
		addFiles(new File("./colecciones/"+nombre+"/archivos/palabrasVacias"), archivoStopWord);
		addFiles(new File("./colecciones/"+nombre+"/archivos/stemizados"), archivoStemizado);
		
	}
		
	/**
	 * Añade los archivos de un determinado directorio en una estructura Map donde el primer campo es un identificador y el segundo la ruta del fichero
	 * @param file Carpeta dónde se encuentra el archivo
	 * @param archivos Map donde serán almacenados los archivos de la carpeta
	 * @return true si los archivos han podido ser cargados correctamente, false en caso contrario
	 */
	
	private boolean addFiles(File file, Map<Integer, ArchivosColeccion> archivos){
        if(!file.exists()){	// Si no existe el directorio
            return false;
        }else{	// Si existe
			File[] ficheros = file.listFiles();
			for (File f: ficheros){
				ArchivosColeccion archivoOri = new ArchivosColeccion(archivos.size(), f.getPath());
				archivos.put(archivos.size(), archivoOri);
			}
        }
		return true;
	}
	
	/**
	 * Realiza el procesamiento de los ficheros realizando la eliminación de las palabras vacías.
	 * @param archivo Archivo del tipo ArchivoColeccion que va a ser procesado.
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException 
	 */
	public void IndexarStopWords(ArchivosColeccion archivo) throws IOException, SAXException, TikaException{
		synchronized (archivoOriginal){
			archivoOriginal.put(archivo.getNumArchivo(), archivo);
		}
		tokenizar(archivoOriginal.get(archivo.getNumArchivo()));
		quitarStopWord(archivoTokenizado.get(archivo.getNumArchivo()));
		lemmatizacionStopWords(archivoStopWord.get(archivo.getNumArchivo()));
	}
	
	/**
	 * Realiza el procesamiento de los ficheros sin eliminar las palabras vacías.
	 * @param archivo Archivo del tipo ArchivoColeccion que va a ser procesado.
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException 
	 */	
	public void IndexarNoStopWords(ArchivosColeccion archivo) throws IOException, SAXException, TikaException{
		synchronized (archivoOriginal){
			archivoOriginal.put(archivo.getNumArchivo(), archivo);
		}
		tokenizar(archivoOriginal.get(archivo.getNumArchivo()));
		lemmatizacionNoStopWords(archivoTokenizado.get(archivo.getNumArchivo()));
	}
	
	/**
	 * Establece el nombre de la colección.
	 * @param unNombre Nombre de la colección.
	 */
	
	public void setNombre(String unNombre){
		nombre = unNombre;
	}

	/**
	 * Establece cual es la ruta donde se almacena la colección.
	 * @param unaRutaColeccion Ruta de los archivos de la colección.
	 */
	
	public void setRutaColeccion(String unaRutaColeccion){
		rutaColeccion = unaRutaColeccion;
	}

	/**
	 * Establece la etapa de preprocesamiento en la que se encuentra el índice.<p>
	 * 0 = Procesamiento documental<p>
	 * 1 = Creación del índice.<p>
	 * 2 = Consultas.<p>
	 * @param unaEtapa Etapa en la que se encuentra el preprocesamiento del índice.
	 */
	
	public void setEtapa(int unaEtapa){
		etapa = unaEtapa;
	}

	/**
	 * Establece el fichero de StopWord
	 * @param unStopWord Es un archivo con las palabras vacías para el idioma de la colección.
	 */
	
	public void setStopWord(File unStopWord){
		stopWords = unStopWord;
	}
	
	/**
	 * Asigna un array con documentos originales a la colección.
	 * @param unosArchivosOriginales Array del tipo ArchivosColeccion con los documentos originales de la colección.
	 */
	
	public void setDocOriginales(ArrayList<ArchivosColeccion> unosArchivosOriginales){
		docOriginales = unosArchivosOriginales;
	}
	
	/**
	 * Establece el idioma en el que se procesa o será procesada la colección.
	 * @param unIdioma es = Español <p> en = Inglés <p>
	 */
	
	public void setIdioma(String unIdioma){
		idioma = unIdioma;
	}
	
	/**
	 * Devuelve la ruta donde se encuentra ubicada la colección.
	 * @return Un string con la ruta de la colección.
	 */
	
	public String getRutaColeccion(){
		return rutaColeccion;
	}

	/**
	 * Devuelve el nombre de la colección.
	 * @return Un string con el nombre de la colección.
	 */
	
	public String getNombre(){
		return nombre;
	}

	/**
	 * Devuelve la etapa de preprocesamiento en la que se encuentra el índice.
	 * @return 0 = Procesamiento documental. <p>
	 * 1 = Creación del índice. <p>
	 * 2 = Consultas. <p>
	 */
	
	public int getEtapa(){
		return etapa;
	}
	
	/**
	 * Revuelve el idioma de la coleccion. Español o inglés.
	 * @return Un String con el idioma de la coleccion.<p> es = Español<p> en = Inglés.<p>
	 */
	
	public String getIdioma(){
		return idioma;
	}
	
	/**
	 * Devuelve los documentos originales de la colección en formato txt.<p>
	 * Estos archivos son el resultado de extraer todo el texto original del archivo de la colección.
	 * @return Array con todos los documentos de la colección.
	 */
	public Map<Integer, ArchivosColeccion> getArchivoOriginal(){
		return archivoOriginal;
	}
	
	/**
	 * Devuelve los documentos tokenizados de la colección en formato txt.<p>
	 * Estos archivos son el resultado de eliminar puntos, símbolos, pasar los tokens a minúscula... de los archivos originales.
	 * @return Array con todos los documentos de la colección.
	 */
	public Map<Integer, ArchivosColeccion> getArchivoTokenizados(){
		return archivoTokenizado;
	}
	
	/**
	 * Devuelve los documentos sin palabras vacías de la colección en formato txt.<p>
	 * Estos archivos son el resultado de eliminar las palabras vacías a los archivos tokenizados.
	 * @return Array con todos los documentos de la colección.
	 */
	public Map<Integer, ArchivosColeccion> getArchivoStopWords(){
		return archivoStopWord;
	}
	
	/**
	 * Devuelve los documentos stemizados de la colección en formato txt.<p>
	 * Estos archivos son el resultado de eliminar los afijos de los archivos tokenizados de etapas anteriores, ya sean con o sin palabras vacías.
	 * @return Array con todos los documentos de la colección.
	 */
	public Map<Integer, ArchivosColeccion> getArchivoStemizados(){
		return archivoStemizado;
	}
	
	/**
	 * Devuelve un array con los documentos originales de la colección.
	 * @return Array de ArchivosColeccion con los documentos originales de la colección
	 */
	
	public ArrayList<ArchivosColeccion> getDocOriginales(){
		return docOriginales;
	}
	
	
	/**
	 * Devuelve el fichero de StopWord
	 */
	
	public File getStopWord(){
		return stopWords;
	}
	
	/**
	 * Crea la jerarquía de directorios para almacenar los archivos procesados y el índice.
	 * @param rutaColeccion ruta donde se crearán los archivos de la coleccion. Será de la forma ./colecciones/usuario/nombreColeccion/...
	 */
	
	public void crearDirectorios(String rutaColeccion){
		new File(rutaColeccion).mkdirs();
		new File(rutaColeccion + "archivos/originales").mkdirs();
		new File(rutaColeccion + "archivos/tokenizados").mkdirs();
		new File(rutaColeccion + "archivos/palabrasVacias").mkdirs();
		new File(rutaColeccion + "archivos/stemizados").mkdirs();
		new File(rutaColeccion + "archivos/archivoStopWords").mkdirs();
		new File(rutaColeccion + "indice").mkdirs();
	}
	
	/**
	 * Imprime el contenido actual de la colección.
	 * Imprime todos los datos de la coleccion.
	 */
	
	public void imprimirDatos(){
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Nombre de la colección: " + nombre);
		System.out.println("Ruta de la colección: " + rutaColeccion);
		System.out.println("Etapa actual de la creación del índice: " + ((etapa == 0) ? "Procesamiento documental" : (etapa==1) ? "Indexación" : "Consulta"));
		if (stopWords!= null){
			System.out.println("Archivo de palabras vacías: " + stopWords.getPath());
		}else{
			System.out.println("Archivo de palabras vacías: No hay.");
		}
		System.out.println("Lista de ficheros: (" + docOriginales.size() + " Elementos):");
		for (int i = 0; i<docOriginales.size(); i++){
			ArchivosColeccion a = docOriginales.get(i);
			a.imprimirArchivos();
		}
		System.out.println("Lista de ficheros originales (" + archivoOriginal.size() + " Elementos):");
		for (int i = 0; i<archivoOriginal.size(); i++){
			ArchivosColeccion a = archivoOriginal.get(i);
			a.imprimirArchivos();
		}
		System.out.println("Lista de ficheros tokenizados (" + archivoTokenizado.size() + " Elementos):");
		for (int i = 0; i<archivoTokenizado.size(); i++){
			ArchivosColeccion a = archivoTokenizado.get(i);
			a.imprimirArchivos();
		}
		System.out.println("Lista de ficheros StopWords (" + archivoStopWord.size() + " Elementos):");
		for (int i = 0; i<archivoStopWord.size(); i++){
			ArchivosColeccion a = archivoStopWord.get(i);
			a.imprimirArchivos();
		}
		System.out.println("Lista de ficheros Stemizados (" + archivoStemizado.size() + " Elementos):");
		for (int i = 0; i<archivoStemizado.size(); i++){
			ArchivosColeccion a = archivoStemizado.get(i);
			a.imprimirArchivos();
		}
		System.out.println("------------------------------------------------------------------------");
	}
	
	/**
	 * Tokeniza los documentos de texto que están almacenados en el array de archivos originales.
	 * @param file Archivo del tipo ArchivoColección que será tokenizado y guardado en la carpeta de archivos tokenizados.
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException 
	 */
	
	private void tokenizar(ArchivosColeccion file) throws IOException, SAXException, TikaException{
		Analyzer analyzer = new StandardAnalyzer();
        //Extraemos información y lo parseamos
        Metadata metadata; //Metadatos
        ContentHandler ch; //ContentHandler
        ParseContext parseContext; //Para modificar el comportamiento de ContentHandler
		Parser parser = new AutoDetectParser(); //Detectamos el tipo de archivo
        FileInputStream input;
		String textoTokenizado = "";
		File archivoTok;
		File archivoOri; // Archivo original que se guardará para el proceso de análisis léxico
		File ficheroOri = new File(file.getRutaFichero());	// Archivo original en la carpeta original.
		parseContext = new ParseContext();
		metadata = new Metadata();
		ch = new BodyContentHandler(-1);
		input  = new FileInputStream(ficheroOri.getPath());
		//Parseamos el stream
		parser.parse(input, ch, metadata, parseContext);
		String textoOriginal = ch.toString();
		textoOriginal = textoOriginal.replaceAll("[\u4E00-\u9FA5]|´","").replaceAll("\\s*(\\p{Alpha}*)(\\p{Digit}+)(\\p{Alpha}*)\\s*", "$1 $2 $3").replaceAll("[\\[\\]|,.!?;:/\\)\\(\\-−=]", " $0 ").replaceAll("(\\r|\\n|\\r\\n|\\t|\\s| )+", " ").replaceAll("^\\s+","").replaceAll("( )+", " ").replace("-|^\\p{ASCII}", " ").replaceAll("( )+|\\%\\p{Alnum}+\\%?", " ");
		TokenStream ts = analyzer.tokenStream(null, new StringReader(textoOriginal));
		try {
			ts.reset();
			while (ts.incrementToken()){
				textoTokenizado = textoTokenizado + ts.getAttribute(CharTermAttribute.class) + " ";
			}
			ts.end();
		}finally {
			ts.close();
		}
		archivoOri = new File (rutaColeccion + "archivos/originales/" + FilenameUtils.removeExtension(ficheroOri.getName()) + ".txt");
		ArchivosColeccion ar = new ArchivosColeccion(file.getNumArchivo(), archivoOri.getPath());
		archivoOriginal.replace(file.getNumArchivo(), ar);
		Files.write(textoOriginal, archivoOri, Charset.forName("UTF-8"));
		textoTokenizado = textoTokenizado.replaceAll("\\p{Digit}","").replaceAll("( )+", " ").replaceAll("^\\w\\p{Digit}+|[^\\w]\\p{Digit}+|\\p{Punct}", " ").replaceAll("[,.!?;:\\-]", "$0 ").replaceAll("(\\r|\\n|\\r\\n|\\t|\\s)+", " ").replaceAll("^\\s+","").replace("-", " ");
		textoTokenizado = StringUtils.stripAccents(textoTokenizado);
		// Almacenamos el archivo tokenizado.
		archivoTok = new File(rutaColeccion + "archivos/tokenizados/" + FilenameUtils.removeExtension(ficheroOri.getName()) + ".txt");
		Files.write(textoTokenizado, archivoTok, Charset.forName("UTF-8"));
		ArchivosColeccion archivo = new ArchivosColeccion(file.getNumArchivo(), archivoTok.getPath());
		synchronized (archivoTokenizado){
			archivoTokenizado.put(archivo.getNumArchivo(), archivo);
		}
	}
	
	/**
	 * Quita las palabras vacías de los documentos almacenados en el array de archivoTokenizado 
	 * @param file ArchivoColeccion que del que se quitarán las palabras vacías que se haya cargado a la colección.
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException 
	 */
	
	private void quitarStopWord(ArchivosColeccion file) throws IOException, SAXException, TikaException{
		Analyzer analyzer = new StandardAnalyzer(new FileReader(stopWords));
        //Extraemos información y lo parseamos
        Metadata metadata = new Metadata(); //Metadatos
        ContentHandler ch; //ContentHandler
        ParseContext parseContext = new ParseContext(); //Para modificar el comportamiento  de ContentHandler
        AutoDetectParser parser = new AutoDetectParser(); //Detectamos el tipo de archivo
        InputStream input;
		String textoStopWords = "";
		File archivoStopWords;
		File ficheroTokenizado = new File (file.getRutaFichero());
		ch = new BodyContentHandler(-1);
		input  = new FileInputStream(file.getRutaFichero());
		//Parseamos el stream
		parser.parse(input, ch, metadata, parseContext);
		TokenStream ts = analyzer.tokenStream(null, new StringReader(ch.toString()));
		try {
			ts.reset();
			while (ts.incrementToken()){
				textoStopWords = textoStopWords + ts.getAttribute(CharTermAttribute.class) + " ";
			}
			ts.end();
		}finally {
			ts.close();
		}
		archivoStopWords = new File(rutaColeccion + "archivos/palabrasVacias/" + FilenameUtils.removeExtension(ficheroTokenizado.getName()) + ".txt");
		Files.write(textoStopWords, archivoStopWords, Charset.forName("UTF-8"));
		ArchivosColeccion archivo = new ArchivosColeccion(file.getNumArchivo(), archivoStopWords.getPath());
		textoStopWords = "";
		synchronized(archivoStopWord){
			archivoStopWord.put(archivo.getNumArchivo(), archivo);
		}
	}

	/**
	 * Quita los afijos de los archivos tokenizados y que no contienen palabras vacías.
	 * @param file ArchivoColeccion al que se le han quitado las palabras vacías y que será tratado para el proceso de lematización.
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException 
	 */
	
	private void lemmatizacionStopWords(ArchivosColeccion file) throws IOException, SAXException, TikaException{
		
        //Extraemos información y lo parseamos
        Metadata metadata = new Metadata(); //Metadatos
        ContentHandler ch; //ContentHandler
        ParseContext parseContext = new ParseContext(); //Para modificar el comportamiento  de ContentHandler
        AutoDetectParser parser = new AutoDetectParser(); //Detectamos el tipo de archivo
        InputStream input = null;
		String textoStemizado = "";
		File archivoStem;
		File archivoSW = new File (file.getRutaFichero());
		//Leemos los documentos
		ch = new BodyContentHandler(-1);
		input  = new FileInputStream(archivoSW);
		//Parseamos el stream
		parser.parse(input, ch, metadata, parseContext);
		Analyzer analyzer = null;
		if (idioma.equals("en")){
			analyzer = new EnglishAnalyzer(null);
		}else{
			analyzer = new SpanishAnalyzer(null);
		}
		TokenStream ts = analyzer.tokenStream(null, new StringReader(ch.toString()));
		try {
			ts.reset();
			while (ts.incrementToken()){
				textoStemizado = textoStemizado + ts.getAttribute(CharTermAttribute.class) + " ";
			}
			ts.end();
		}finally {
			ts.close();
		}
		archivoStem = new File(rutaColeccion + "archivos/stemizados/" + FilenameUtils.removeExtension(archivoSW.getName()) + ".txt");
		Files.write(textoStemizado, archivoStem, Charset.forName("UTF-8"));
		ArchivosColeccion archivo = new ArchivosColeccion(file.getNumArchivo(), archivoStem.getPath());
		synchronized(archivoStemizado){
			archivoStemizado.put(archivo.getNumArchivo(), archivo);
		}
		textoStemizado = "";
	}

		/**
	 * Quita los afijos de los archivos tokenizados y que contienen palabras vacías.
	 * @param file ArchivoColeccion con palabras  vacías que será lematizado.
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException 
	 */
	
	private void lemmatizacionNoStopWords(ArchivosColeccion file) throws IOException, SAXException, TikaException{
        //Extraemos información y lo parseamos
        Metadata metadata = new Metadata(); //Metadatos
        ContentHandler ch; //ContentHandler
        ParseContext parseContext = new ParseContext(); //Para modificar el comportamiento  de ContentHandler
        AutoDetectParser parser = new AutoDetectParser(); //Detectamos el tipo de archivo
        InputStream input;
		String textoStemizado = "";
		File archivoStem;
		File archivoSW = new File (file.getRutaFichero());
		
		//Leemos los documentos
		ch = new BodyContentHandler(-1);
		input  = new FileInputStream(archivoSW);
		//Parseamos el stream
		parser.parse(input, ch, metadata, parseContext);
		
		Analyzer analyzer = null;
		if (idioma.equals("en")){
			analyzer = new EnglishAnalyzer(null);
		}else{
			analyzer = new SpanishAnalyzer(null);
		}
		TokenStream ts = analyzer.tokenStream(null, new StringReader(ch.toString()));
		try {
			ts.reset();
			while (ts.incrementToken()){
				textoStemizado = textoStemizado + ts.getAttribute(CharTermAttribute.class) + " ";
			}
			ts.end();
		}finally {
			ts.close();
		}
		archivoStem = new File(rutaColeccion + "archivos/stemizados/" + FilenameUtils.removeExtension(archivoSW.getName()) + ".txt");
		Files.write(textoStemizado, archivoStem, Charset.forName("UTF-8"));
		ArchivosColeccion archivo = new ArchivosColeccion(file.getNumArchivo(), archivoStem.getPath());
		synchronized(archivoStemizado){
			archivoStemizado.put(archivo.getNumArchivo(), archivo);
		}
		textoStemizado = "";
	}

	/**
	 * Elimina toda la información de una colección
	 * @throws Throwable 
	 */
	
	public void liberarMemoria() throws Throwable {
		nombre = null;
		rutaColeccion = null;
		etapa = 0;
		idioma = null;
		stopWords = null;
		archivoTokenizado.clear();
		archivoOriginal.clear();
		archivoStopWord.clear();
		archivoStemizado.clear();
		docOriginales.clear();
		this.finalize();
	}
}