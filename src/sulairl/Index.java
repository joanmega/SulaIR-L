package sulairl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * @author José Antonio Medina García
 */

public class Index {
	
    private final Coleccion col;
    private int numTerms;
    IndexWriter iwriter;
    IndexReader ireader;
	IndexSearcher searcherVec;
	IndexSearcher searcherBool;
	IndexSearcher searcherProb;
    Directory directory;
    DirectoryReader directoryReader;
	Query queryVec;
	Query queryBool;
	Query queryProb;
	String consVec;
	String consBool;
	String consProb;
	String consBoolLucene;
	long timeVec;
	long timeBool;
	long timeProb;
	
	/**
	 * Constructor por defecto de un tipo Index
	 * @throws IOException 
	 */

    public Index() throws IOException{
		col = Configuracion.col;
		numTerms = 0;
		queryVec = null;
		queryBool = null;
		searcherVec = null;
		searcherBool = null;
		searcherProb = null;
		consVec = "";
		consBool = "";
		consProb = "";
		consBoolLucene = "";
		timeVec = 0;
		timeBool = 0;
		timeProb = 0;
    }

	/**
	 * Inicializa la variable IndexReader que nos permitirá leer información sobre el índice.
	 * @throws IOException 
	 */
	
    public void createIndexReader() throws IOException{
		Path p = Paths.get(col.getRutaColeccion() + "indice/");  // Indicamos la carpeta del índice
        directory = FSDirectory.open(p); // Almacenamos el índice en el directorio
		ireader = DirectoryReader.open(directory);
    }
	
	/**
	 * Inicializa la variable IndexReader que nos permitirá leer información sobre el índice.
	 * @param nombreColeccion nombre de la colección de la cual se va a leer los datos.
	 * @return 
	 */
	
	public boolean createIndexReader(String nombreColeccion){
		try {
			Path p = Paths.get("./colecciones/" + nombreColeccion + "/indice/");  // Indicamos la carpeta del índice
			directory = FSDirectory.open(p); // Almacenamos el índice en el directorio
			ireader = DirectoryReader.open(directory);
			return true;
		} catch (IOException ex) {
			return false;
		}
    }
	
	/**
	 * Inicializa una variable IndexWriter que nos permitirá crear el indice.
	 * @throws IOException 
	 */
	
    public void createIndexWriter() throws IOException{
		Path p = Paths.get(col.getRutaColeccion() + "indice/");  // Indicamos la carpeta del índice
        directory = FSDirectory.open(p); // Almacenamos el índice en el directorio
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        iwriter = new IndexWriter(directory, config);
		iwriter.deleteAll();
    }
	
	/**
	 * Devuleve el directorio en el que se encuentra el índice.
	 * @return 
	 */

    public String getDirectory(){
		return col.getRutaColeccion() + "indice/";
    }
	
	/**
	 * Devuelve el número de documentos que componen la colección.
	 * @return Un entero con el número de documentos.
	 * @throws IOException 
	 */

    public int getNumDocs() throws IOException{
		return ireader.numDocs();
    }
	
	/**
	 * Devuelve la versión del índice.
	 * @return Un long con el número de la versión
	 * @throws IOException 
	 */

    public long getVersion() throws IOException{
		directoryReader = DirectoryReader.open(directory);
		return directoryReader.getVersion();
    }
	
	/**
	 * Devuelve el tiempo empleado en recuperar los documentos por la consulta vectorial.
	 * @return Un long con el tiempo en microsegundos.
	 */
	
	public long getTimeVec(){
		return timeVec;
	}
	
	/**
	 * Devuelve el tiempo empleado en recuperar los documentos por la consulta booleana.
	 * @return Un long con el tiempo en microsegundos.
	 */
	
	public long getTimeBool(){
		return timeBool;
	}
	
	/**
	 * Devuelve el tiempo empleado en recuperar los documentos por la consulta probabilística.
	 * @return Un long con el tiempo en microsegundos.
	 */
	
	public long getTimeProb(){
		return timeProb;
	}
	
	/**
	 * Establece el tiempo empleado en recuperar los documentos la consulta vectorial.
	 * @param unTimeVec Un objeto del tipo long con el tiempo en microsegundos 
	 */
	
	public void setTimeVec(long unTimeVec){
		timeVec  = unTimeVec;
	}
	
	/**
	 * Establece el tiempo empleado en recuperar los documentos la consulta probabilística.
	 * @param unTimeProb Un objeto del tipo long con el tiempo en microsegundos 
	 */
	
	public void setTimeProb(long unTimeProb){
		timeProb  = unTimeProb;
	}
	
	/**
	 * Establece el tiempo empleado en recuperar los documentos la consulta probabilística.
	 * @param unTimeBool Un objeto del tipo long con el tiempo en microsegundos 
	 */
	
	public void setTimeBool(long unTimeBool){
		timeBool  = unTimeBool;
	}

	/**
	 * Cierra la variable IndexReader del índice.
	 * @throws IOException 
	 */
	
    public void cerrarIndexReader() throws IOException{
        ireader.close();
    }
	
	/**
	 * Cierra la variable IndexWriter del índice.
	 * @throws IOException 
	 */

    public void cerrarIndexWriter() throws IOException{
        iwriter.close();
    }
	
	/**
	 * Cierra la variable IndexReader, IndexWriter, el directory y el diretoryReader.
	 * @throws IOException 
	 */
	
	public void cerrarIndice() throws IOException{
		if (ireader != null){
			ireader.close();
		}
		if (iwriter != null){
			iwriter.close();
		}
		directory.close();
		directoryReader.close();
	}

	/**
	 * Añade un ArchivoColeccion al índice. Antes debe de estar inicializada la variable IndexWriter
	 * @param archivo ArchivoColeccion que será introducido en el índice.
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException 
	 */
	
    public void createIndex(ArchivosColeccion archivo) throws IOException, SAXException, TikaException{
        Document doc;
        Metadata metadata = new Metadata();

        ParseContext parseContext = new ParseContext();
        AutoDetectParser parser = new AutoDetectParser();
        InputStream input;

        ContentHandler ch;
        FieldType ft = new FieldType(TextField.TYPE_STORED);
        ft.setStoreTermVectors(true);
        ft.setStoreTermVectorOffsets(true);
        ft.setStoreTermVectorPayloads(true);
        ft.setStoreTermVectorPositions(true);
        ft.setStored(true);
        ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		ch = new BodyContentHandler(-1);
		input  = new FileInputStream(archivo.getRutaFichero());
		parser.parse(input, ch, metadata, parseContext);
		doc = new Document(); //Creamos un documento
		doc.add(new StringField("rutaFicheroOriginal", archivo.getRutaFichero(), Field.Store.YES));
		doc.add(new StringField("ficheroStemming", archivo.getRutaFichero(), Field.Store.YES));
		doc.add(new StringField("nombreFichero", new File(archivo.getRutaFichero()).getName() , Field.Store.YES));
		doc.add(new Field("text", ch.toString(), ft));
		iwriter.addDocument(doc);
    }

	/*
	
    public void getInfoTerminos () throws IOException{
		String infoTermino = "";
		createIndexReader();
		TFIDFSimilarity tfidfsimilarity = new ClassicSimilarity();
		Terms terminos = MultiFields.getTerms(ireader, "text");
		TermsEnum termino = terminos.iterator();
		numTerms = 0;
		BytesRef rf = termino.next();
		Termino ter;
		while (rf!=null){
			ter = new Termino(ireader.numDocs());
			Term t = new Term("text", rf.utf8ToString());	
			long freqTerm = ireader.totalTermFreq(t);		// Ocurrencias	
			String term = rf.utf8ToString();				// Término
			BigDecimal bd = new BigDecimal(Float.toString(tfidfsimilarity.idf(ireader.docFreq(t), ireader.numDocs())));
			bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
			float idf = bd.floatValue();		// Idf
			int freq = ireader.docFreq(t);		// Documentos en los que aparece el término
			ter.setRank(numTerms);
			ter.setOcurrencias(freqTerm);
			ter.setTerm(term);
			ter.setIdf(idf);
			ter.setFreq(freq);
			infoTermino = numTerms +", " + term + ", " + idf + ", ";
			PostingsEnum docEnum = MultiFields.getTermDocsEnum(ireader, "text", rf);
			int docActual = docEnum.nextDoc();
			for (int i = 0; i< ireader.numDocs(); i++){
				if (docActual == i){
					if (i == ireader.numDocs()-1){
						infoTermino = infoTermino + docEnum.freq();
						docActual = docEnum.nextDoc();
					}else{
						infoTermino = infoTermino + docEnum.freq() + ", ";
						docActual = docEnum.nextDoc();
					}
				}else{
					if (i == ireader.numDocs()-1){
						infoTermino = infoTermino + "0";
						docActual = docEnum.nextDoc();
					}else{
						infoTermino = infoTermino + "0, ";
						docActual = docEnum.nextDoc();
					}
				}
			}
			numTerms++;
			rf = termino.next();
		}
    }*/
	
	/**
	 * Devuelve las posiciones en la colección de un término.
	 * @param termino String con el término del cual se quiere obtener las posiciones en la colección.
	 * @return Un array de enteros donde:<p>
	 * Posición 0: Identificador del documento.
	 * Posición 1: Posición del término en el documento.
	 * Posición 3: Posición donde empieza el término (start offset).
	 * Posición 4: Posición donde termina el término (end offset).
	 * @throws IOException 
	 */

    public ArrayList<int[]> getPosiciones(String termino) throws IOException{
        createIndexReader();
        int[] posiciones;
        ArrayList<int[]> listaPosiciones = new ArrayList<>();
        Term t = new Term("text", termino);
        PostingsEnum docEnum = MultiFields.getTermDocsEnum(ireader, "text", t.bytes());
        int docId = docEnum.nextDoc();
        while (docId != PostingsEnum.NO_MORE_DOCS) {
            TermsEnum it = ireader.getTermVector(docId, "text").iterator();
            it.seekExact(t.bytes());
            PostingsEnum postingsInDoc = it.postings(null, PostingsEnum.OFFSETS);
            postingsInDoc.nextDoc();
            int totalFreq = postingsInDoc.freq();
            int pos;
            for (int i = 0; i < totalFreq; i++) {
                pos = postingsInDoc.nextPosition();
                posiciones = new int[4];
                posiciones[0] = docEnum.docID();        // Identificador del documento
                posiciones[1] = pos;                    // Posición del término en el documento
                posiciones[2] = postingsInDoc.startOffset();    // Posición en la que empieza el término
                posiciones[3] = postingsInDoc.endOffset();      // Posición en la que termina el término
                listaPosiciones.add(posiciones);
//              System.out.println("Termino: " + it.term().utf8ToString() + " position: " + pos + " StartOffset: " + postingsInDoc.startOffset() + " EndOffset: " +  postingsInDoc.endOffset());
            }
            docId=docEnum.nextDoc();
        }
        return listaPosiciones;
    }
	
	/**
	 * Devuelve la ruta del documento stemming que está almacenado en el índice.
	 * @param docID Identificador del documento del cual queremos obtener la ruta del documento stemming.
	 * @return
	 * @throws IOException 
	 */

    public String getFicheroStemming(int docID) throws IOException{
        createIndexReader();
        Document doc = ireader.document(docID);
        IndexableField i = doc.getField("ficheroStemming");
        return i.stringValue();
    }
	
	/**
	 * Devuelve el nombre del archivo almacenado en el índice.
	 * @param docID Identificador del documento del que queremos obtener el nombre.
	 * @return
	 * @throws IOException 
	 */

    public String getNombreFichero (int docID) throws IOException{
        createIndexReader();
        Document doc = ireader.document(docID);
        IndexableField i = doc.getField("nombreFichero");
        return i.stringValue();
    }
	
	/**
	 * Devuelve el identificador del documento indicado por parámetro.
	 * @param nombreFichero Nombre del archivo del cual queremos obtener el identificador.
	 * @return
	 * @throws IOException 
	 */

    public int getIdFichero (String nombreFichero) throws IOException{
        createIndexReader();
        Term t = new Term("nombreFichero", nombreFichero);
        PostingsEnum docEnum = MultiFields.getTermDocsEnum(ireader, "nombreFichero", t.bytes());
        docEnum.nextDoc();
        return docEnum.docID();
    }
	
	/**
	 * Devuelve la tranformación de la consulta vectorial para obtener los resultados.
	 * @return String con la consulta utilizada para obtener los resultados. Esta consulta ha sufrido los mismos cambios que los documentos en su procesamiento.
	 */
	
	public String getConsVec(){
		return consVec;
	}
	
	/**
	 * Devuelve la tranformación de la consulta booleana para obtener los resultados.
	 * @return String con la consulta utilizada para obtener los resultados. Esta consulta ha sufrido los mismos cambios que los documentos en su procesamiento excepto los operadores lógicos que no sufren ninguna modificación.
	 */
	
	public String getConsBool() {
		return consBool;
	}
	
	/**
	 * Devuelve la tranformación de la consulta probabilística para obtener los resultados.
	 * @return String con la consulta utilizada para obtener los resultados. Esta consulta ha sufrido los mismos cambios que los documentos en su procesamiento.
	 */
	
	public String getConsProb() {
		return consProb;
	}
	
	/**
	 * Devuelve la tranformación de la consulta booleana transformada por lucene para obtener los resultados.
	 * @return String con la consulta utilizada para obtener los resultados.
	 */
	
	public String getConsBoolLucene() {
		return consBoolLucene;
	}
	
	/**
	 * Establece la consulta vectorial que se ha hecho para obtener los resultados.
	 * @param unaConsVec String con la consulta modificada de la misma manera que los documentos en su procesamiento.
	 */
	
	public void setConsVec(String unaConsVec){
		consVec = unaConsVec;
	}
	
	/**
	 * Establece la consulta probabilística que se ha hecho para obtener los resultados.
	 * @param unaConsProb String con la consulta modificada de la misma manera que los documentos en su procesamiento.
	 */
	
	public void setConsProb(String unaConsProb){
		consProb = unaConsProb;
	}
	
	/**
	 * Establece la consulta probabilística que se ha hecho para obtener los resultados.
	 * @param setConsBoolLucene String con la consulta modificada por Lucene para realizar la búsqueda.
	 */
	
	public void setConsBoolLucene(String unaConsBoolLucene) {
		consBoolLucene = unaConsBoolLucene;
	}
	
	/**
	 * Devuelve el término más repetido en la colección.
	 * @return TermStat del término que más se repite en la colección.
	 * @throws IOException 
	 */
	
    public TermStats maxTermFreq() throws IOException{
		TermStats[] ts = null;
		createIndexReader();
		try {
			ts = HighFreqTerms.getHighFreqTerms(ireader, 1, "text", new HighFreqTerms.TotalTermFreqComparator()); 
		} catch (IOException ex) {
			Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception ex) {
			Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ts[0];
	}
	
	/**
	 * Devuelve el término menos repetido en la colección.
	 * @return TermStat del término que menos se repite en la colección.
	 * @throws IOException 
	 */

    public TermStats minTermFreq() throws IOException{
		createIndexReader();
		TermStats[] ts = null;
		try {
			ts = HighFreqTerms.getHighFreqTerms(ireader, (int)ireader.getSumTotalTermFreq("text"), "text", new HighFreqTerms.TotalTermFreqComparator()); 
		} catch (IOException ex) {
			Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception ex) {
			Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ts[ts.length-1];
	}
	
	/**
	 * Función que ejecuta la búsqueda de documentos en la colección mediante una consulta vectorial.
	 * @param q Consulta que hará sobre el índice. Será transformada de la misma manera que los documentos de la colección en su procesamiento.
	 * @param stopWords Archivo de palabras vacías de la colección para procesar la consulta. Si no hay ninguno entonces no se quitarán.
	 * @param numDocumentos Número de documentos máximo a devolver por la consulta.
	 * @return Array con los documentos devueltos por la consulta.
	 * @throws IOException
	 * @throws ParseException 
	 */
	
	public ScoreDoc[] queryVect(String q, File stopWords, int numDocumentos) throws IOException, ParseException{
		createIndexReader();
		Analyzer analyzer = new StandardAnalyzer();
		consVec = q;
		if (stopWords != null){
			analyzer = new StandardAnalyzer(new FileReader(stopWords));
		}
		consVec = procesarStringConsulta(consVec, analyzer);
		if (col.getIdioma().equals("en")){
			analyzer = new EnglishAnalyzer(null);
		}else{
			analyzer = new SpanishAnalyzer(null);
		}
		consVec = procesarStringConsulta(consVec, analyzer);
		searcherVec = new IndexSearcher(ireader);
		searcherVec.setSimilarity(new ClassicSimilarity());
		long startTime = System.nanoTime();
		queryVec = new QueryParser("text", new StandardAnalyzer()).parse(consVec);
		long endTime = System.nanoTime();
		setTimeVec((endTime - startTime)/1000);
		TopDocs hits = searcherVec.search(queryVec, numDocumentos);
		return hits.scoreDocs;
	}
	
	/**
	 * Devuelve la expliacación por la que se han devuelto los documentos en la consulta vectorial.
	 * @param doc Número del documento del cual se quiere obtener la información de su relevancia.
	 * @return String con los detalles de la relevancia del documento.
	 */
	
	public String getExplanationVec (int doc){
		String explanation = "";
		try {
			createIndexReader();
			explanation = searcherVec.explain(queryVec, doc).toString();
			return explanation;
		} catch (IOException ex) {
			Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
		}
		return explanation;
	}
	
	/**
	 * Función que tranforma una consulta de igual manera que se han hecho con los documentos en el proceso de procesamiento.
	 * @param consulta Consulta a transformar.
	 * @param analyzer Analyzer a utilizar en el proceso.
	 * @return String con la consulta transformada.
	 */
	
	public String procesarStringConsulta(String consulta, Analyzer analyzer) {
		String temp = "";
		TokenStream ts = analyzer.tokenStream(null, consulta);
		try {
			try {
				ts.reset();
				while (ts.incrementToken()){
					temp = temp + ts.getAttribute(CharTermAttribute.class) + " ";
				}
				ts.end();
			}finally {
				ts.close();
			}
		} catch (IOException ex) {
			Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
		}
		return temp;
	}
	
	/**
	 * Función que ejecuta la búsqueda de documentos en la colección mediante una consulta booleana
	 * @param q Consulta que hará sobre el índice. Esta consulta deberá estar escrita de forma correcta con los operadores lógicos. 
	 * @param numDocumentos Número máximo de documentos a recuperar por la consulta.
	 * @return Documentos que cumplen con la consulta.
	 * @throws IOException Si la consulta no está escrita de forma correcta.
	 */
	
	public ScoreDoc[] queryBool(String q, int numDocumentos) throws IOException{
		try {
			createIndexReader();
			Analyzer analyzer = null;
			consBool = q;
			searcherBool = new IndexSearcher(ireader);
			if (col.getIdioma().equals("en")){
				analyzer = new EnglishAnalyzer(null);
			}else{
				analyzer = new SpanishAnalyzer(null);
			}
			consBool = procesarStringConsultaBool(consBool, analyzer);
			consBool = consBool.replace("\\s+", " ");
			queryBool = new QueryParser("text", new StandardAnalyzer()).parse(consBool);
			long startTime = System.nanoTime();
			BooleanQuery booleanQuery = new BooleanQuery.Builder()
				.add(queryBool, BooleanClause.Occur.MUST)
				.build();
			setConsBoolLucene(booleanQuery.toString());
			long endTime = System.nanoTime();
			setTimeBool((endTime - startTime)/1000);
			TopDocs hits = searcherBool.search(booleanQuery, numDocumentos);
			return hits.scoreDocs;
		} catch (ParseException ex) {
//			Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
	
	/**
	 * Procesa los términos de la consulta de igual forma que los documentos en su procesamiento.
	 * @param query Consulta a procesar.
	 * @param analyzer Analyzer a utilizar.
	 * @return String con la consulta procesada.
	 * @throws IOException 
	 */
	
	private String procesarStringConsultaBool(String query, Analyzer analyzer) throws IOException{
		query = query.replaceAll("[\\(\\)]", " $0 ");		
		String temp = "", palabra = "";
		while (!query.equals("")){
			palabra = query.substring(0, query.indexOf(" "));
			if(palabra.equals("AND") || palabra.equals("OR") || palabra.equals("NOT") || palabra.equals(")") || palabra.equals("(")){
				temp = temp + palabra + " ";
			}else{
				TokenStream ts = analyzer.tokenStream(null, palabra);
				ts.reset();
				ts.incrementToken();
				temp = temp + ts.getAttribute(CharTermAttribute.class) + " ";
				ts.close();
			}
			query = (String) query.subSequence(query.indexOf(" ")+1, query.length());
			palabra = "";
		}
		
		return temp;
	}
	
	/**
	 * Función que ejecuta la búsqueda de documentos en la colección mediante una consulta probabilística
	 * @param q Consulta que hará sobre el índice. Será transformada de la misma manera que los documentos de la colección en su procesamiento.
	 * @param stopWords Archivo de palabras vacías a utilizar en el procesamiento de la consulta.
	 * @param numDocumentos Número de documentos máximos a recuperar por la consulta
	 * @param normalizacion Índice de normalización de la consulta.
	 * @param saturacion Índice de saturación de la consulta.
	 * @return Array con los documentos devueltos por la consulta.
	 * @throws IOException
	 * @throws ParseException 
	 */
	
	public ScoreDoc[] queryProb(String q, File stopWords, int numDocumentos, float normalizacion, float saturacion) throws IOException, ParseException{
		createIndexReader();
		Analyzer analyzer = new StandardAnalyzer();
		consProb = q;
		if (stopWords != null){
			analyzer = new StandardAnalyzer(new FileReader(stopWords));
		}
		consProb = procesarStringConsulta(consProb, analyzer);
		if (col.getIdioma().equals("en")){
			analyzer = new EnglishAnalyzer(null);
		}else{
			analyzer = new SpanishAnalyzer(null);
		}
		consProb = procesarStringConsulta(consProb, analyzer);
		searcherProb = new IndexSearcher(ireader);
		searcherProb.setSimilarity(new BM25Similarity(saturacion, normalizacion));
		long startTime = System.nanoTime();
		queryProb = new QueryParser("text", new StandardAnalyzer()).parse(consProb);
		long endTime = System.nanoTime();
		setTimeProb((endTime - startTime)/1000);
		TopDocs hits = searcherProb.search(queryProb, numDocumentos);
		return hits.scoreDocs;
	}
	
	/**
	 * Devuelve la expliacación por la que se han devuelto los documentos en la consulta probabilística.
	 * @param doc Número del documento del cual se quiere obtener la información de su relevancia.
	 * @return String con los detalles de la relevancia del documento.
	 */
	
	public String getExplanationProb (int doc){
		String explanation = "";
		try {
			createIndexReader();
			explanation = searcherProb.explain(queryProb, doc).toString();
			return explanation;
		} catch (IOException ex) {
			Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
		}
		return explanation;
	}
	
	
	
/*	
    public void obtenerInfo() throws IOException, InvalidTokenOffsetsException, Exception {

			
		String consulta = "(mancha AND lugar)";

		consulta = consulta.replaceAll("[\\(\\)]", " $0 ");
		
		Analyzer analyzer = new SpanishAnalyzer(null);
		
		String temp = "", palabra = "";
		while (!consulta.equals("")){
			palabra = consulta.substring(0, consulta.indexOf(" "));
			System.out.println("palabra: " + palabra);
			if(palabra.equals("AND") || palabra.equals("OR") || palabra.equals("NOT") || palabra.equals(")") || palabra.equals("(")){
				temp = temp + palabra + " ";
			}else{
				TokenStream ts = analyzer.tokenStream(null, palabra);
				ts.reset();
				ts.incrementToken();
				temp = temp + ts.getAttribute(CharTermAttribute.class) + " ";
				ts.close();
			}
			consulta = (String) consulta.subSequence(consulta.indexOf(" ")+1, consulta.length());
			System.out.println("Consulta: " + consulta);
			System.out.println("Temp: " + temp);
			palabra = "";
			System.out.println("---------------------------------");
			
		}

		consulta = temp;
		System.out.println("Temp: " + temp);
		System.out.println("");
		
		System.out.println("consulta: " + consulta);
		
		Pattern pattern = Pattern.compile("^(\\(*\\s*)*(NOT{0,1}\\s*)?(\\(*\\s*)*\\p{Alnum}+\\s*(\\s*(AND|OR)\\s+(NOT{0,1}\\s+)?\\p{Alnum}+\\s*\\)*)*$");
		Matcher m = pattern.matcher(temp.trim());
		if (m.matches()) {
			System.out.println("Valid!");
		} else {
			System.out.println("Invalid.");
		}
		Path p = Paths.get("./colecciones/parXML/indice/");  // Indicamos la carpeta del índice
        directory = FSDirectory.open(p); // Almacenamos el índice en el directorio
        IndexReader reader = DirectoryReader.open(directory);

		IndexSearcher searcher = new IndexSearcher(reader);
		
		TFIDFSimilarity similarity = new ClassicSimilarity();
		
		System.out.println("Consulta: " + consulta);
		
		searcher.setSimilarity(similarity);
		Query query = null;
		try{
			query = new QueryParser("text", new StandardAnalyzer()).parse(consulta);
			System.out.println("Query: " + query.toString());

			BooleanQuery booleanQuery = new BooleanQuery.Builder()
				.add(query, BooleanClause.Occur.MUST)
				.build();

			System.out.println("BooleanQuery " + booleanQuery.toString());

			TopDocs hits = searcher.search(booleanQuery, 10);

			ScoreDoc[] docs = hits.scoreDocs;
			
			String textoFichero = "En un lugar de la mancha de cuyo nombre no quiero acordarme vivía el amigo don quijote de la mancha";
		
			Scorer scorer = new QueryScorer(query);
			
			Highlighter highlighter = new Highlighter(scorer);
			highlighter.setTextFragmenter(new SimpleSpanFragmenter((QueryScorer) scorer, Integer.MAX_VALUE));
			highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
			TokenStream tokenStream = new SimpleAnalyzer().tokenStream(null, new StringReader(textoFichero));
			
		}catch (ParseException e){
			System.err.println("Error en la consulta");
		}
		
/*
		System.out.println("Búsqueda Probabilística");
		
		consulta = "lugar manch";
		BM25Similarity similarity2 = new BM25Similarity();
		
		searcher.setSimilarity(similarity2);
		
		try{
		query = new QueryParser("text", new StandardAnalyzer()).parse(consulta);
		TopDocs hits = searcher.search(query, 10);
/*
			ScoreDoc[] docs = hits.scoreDocs;
			for (ScoreDoc documento: docs){                
				BigDecimal bd = new BigDecimal(Float.toString(documento.score));
				bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
				System.out.println("Doc. " + documento.doc + " Score: " + bd);
//				Explanation explanation = searcher.explain(query, documento.doc);
//				System.out.println("Explanation: " + explanation.toString());
			}
			
		}catch(ParseException e){
			
		}
*/		
/*				
		TopDocs hits = searcher.search(query, reader.numDocs(), Sort.RELEVANCE, true, true);
		System.out.println("Total Hits: " + hits.totalHits);
		ScoreDoc[] docs = hits.scoreDocs;
		for (ScoreDoc documento: docs){                
			BigDecimal bd = new BigDecimal(Float.toString(documento.score));
            bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
			System.out.println("Doc. " + documento.doc + " Score: " + bd);
			Explanation explanation = searcher.explain(query, documento.doc);
			System.out.println("Explanation: " + explanation.toString());
		}
		
		*/

/*

		String text = "En un lugar de la mancha de cuyo nombre no quiero acordarme";
//		query = new TermQuery(new Term(null, "cuyo"));
		Scorer scorer = new QueryScorer(query);
		
		Highlighter highlighter = new Highlighter(scorer);
		highlighter.setTextFragmenter(new SimpleSpanFragmenter((QueryScorer) scorer, Integer.MAX_VALUE));
		highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
		
		
		TokenStream tokenStream = new SimpleAnalyzer().tokenStream(null, new StringReader(text));
		System.out.println(highlighter.getBestFragment(tokenStream, text));
		
        TopDocs hits = searcher.search(query, reader.maxDoc());
        System.out.println("Numero documentos recuperados: " + hits.totalHits);
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
        for (int i = 0; i < reader.maxDoc(); i++) {
            int id = hits.scoreDocs[i].doc;
            Document doc = searcher.doc(id);
            String text = doc.get("text");
            TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), id, "text", analyzer);
            TextFragment[] frag = null;
			try {
				frag = highlighter.getBestTextFragments(tokenStream, text, false, 4);
			} catch (InvalidTokenOffsetsException ex) {
				Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
			}
            for (int j = 0; j < frag.length; j++) {
                if ((frag[j] != null) && (frag[j].getScore() > 0)) {
                    System.out.println((frag[j].toString()));
                }
            }
			
			System.out.println();
            //Term vector
            text = doc.get("text");
            tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), hits.scoreDocs[i].doc, "text", analyzer);
            frag = highlighter.getBestTextFragments(tokenStream, text, false, 4);
            for (int j = 0; j < frag.length; j++) {
                if ((frag[j] != null) && (frag[j].getScore() > 0)) {
                    System.out.println((frag[j].toString()));
                }
            }
        }
		
		
		*/
		
/*		
        try {
            Path p = Paths.get("./colecciones/miniquijote/indice/");  // Indicamos la carpeta del índice
            directory = FSDirectory.open(p); // Almacenamos el índice en el directorio
            IndexReader reader = DirectoryReader.open(directory);
            TFIDFSimilarity tfidfsimilarity = new ClassicSimilarity();
            Term t = new Term("text", "quijot");
            System.out.println("Número de documentos: " + reader.numDocs());
            System.out.println("Número de documentos que aparece \"quijot\" en la colección: " + reader.docFreq(t));
            System.out.println("Número Docs. con contenido en el Field \"text\": " + reader.getDocCount("text"));
            System.out.println("Suma de las frecuencias de los términos en \"txt\": " + reader.getSumDocFreq("text"));
            System.out.println("Términos totales en el field \"text\": " + reader.getSumTotalTermFreq("text"));
            System.out.println("Suma de veces que aparece el término en todos los documentos: " + reader.totalTermFreq(t));
            System.out.println("Version: " + getVersion());	

            directoryReader = DirectoryReader.open(directory);
/*
            System.out.println("ID DOCUMENTO " + getIdFichero("cap1.txt"));

            TermStats[] ts = HighFreqTerms.getHighFreqTerms(reader, (int)reader.getSumTotalTermFreq("text"), "text", new HighFreqTerms.TotalTermFreqComparator());
            
            for (TermStats t1 : ts) {
                System.out.println("Termino: " + t1.termtext.utf8ToString() + " Apariciones: " + t1.totalTermFreq + " Num. Docs: " + t1.docFreq);
            }
			

*/
/*
            // DOCUMENT

            // Tomamos cada uno de los documentos donde aparece un término y sacamos su frecuencia en cada documento.
    			
            Document doc = null;
            PostingsEnum docEnum = MultiFields.getTermDocsEnum(reader, "text", t.bytes());

            while ((docEnum.nextDoc()) != PostingsEnum.NO_MORE_DOCS) {
                System.out.println("DocID: " + docEnum.docID() + " Freq: " + docEnum.freq() + "  idf: " + tfidfsimilarity.idf(reader.docFreq(t), reader.numDocs()));
                doc = reader.document(docEnum.docID());
                IndexableField i = doc.getField("nombreFichero");
                System.out.println("Ruta stemming " + i.stringValue() + " " );

            }
    			
  /*  					
            docEnum =  MultiFields.getTermDocsEnum(reader, "nombreFichero", t.bytes());

            int docId = docEnum.nextDoc();
            while (docId != PostingsEnum.NO_MORE_DOCS) {
                TermsEnum it = reader.getTermVector(docId, "text").iterator();
                it.seekExact(t.bytes());
                PostingsEnum postingsInDoc = it.postings(null, PostingsEnum.OFFSETS);

                postingsInDoc.nextDoc();

                int totalFreq = postingsInDoc.freq();

                for (int i = 0; i < totalFreq; i++) {
                        int pos = postingsInDoc.nextPosition();
                        System.out.println("Termino: " + it.term().utf8ToString() + " position: " + pos + " StartOffset: " + postingsInDoc.startOffset() + " EndOffset: " +  postingsInDoc.endOffset());
                }
                docId=docEnum.nextDoc();
            }
    */

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*		
            for (int i = 0; i<reader.numDocs(); i++){
                System.out.println("Documento: " + i);
                Terms terms = reader.getTermVector(i, "text");
                TermsEnum it = terms.iterator();
                BytesRef bytesRef = it.next();
                while(bytesRef != null){
                    System.out.println("Término: " + bytesRef.utf8ToString() + " ord: " + "\t\tdocFreq: " + it.docFreq() + "\t\ttotalTermFreq: " + it.totalTermFreq() + "\tIdf: " + idf(reader.docFreq(new Term("text", bytesRef)), reader.numDocs()));
                    bytesRef = it.next();
                }
            }
*/  
/*   
        reader.close();	
        } catch (IOException ex) {
                Logger.getLogger(SulaIRL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
*/
}
