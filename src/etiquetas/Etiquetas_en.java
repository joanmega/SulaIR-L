package etiquetas;

import java.util.ListResourceBundle;

/**
 *
 * @author joanm
 */
public class Etiquetas_en extends ListResourceBundle{

	@Override
	protected Object[][] getContents() {
		return contenido;
	}

	private Object[][] contenido ={
		
		// Generales
		
			{"etiqueta_boton_aceptar", "Accept"},
			{"etiqueta_boton_salir", "Exit"},
			{"etiqueta_label_atencion", "Attention"},
			{"boton_continuar", "Next"},
		
		// Pagina Principal
		
			{"etiqueta_label_bienvenida", "Welcome to SulaIR-L"},
			{"etiqueta_boton_entrar", "Enter"},
			{"etiqueta_borde_idioma", "Language"},
			{"etiqueta_label_desc1", "Application for learning and teaching"},
			{"etiqueta_label_desc2", " Information Retrieval based on Lucene"},
		
		// Ventana de configuración
		
			{"titulo_ventana_configuracion", "Settings for the new SulaIR-L session"},
			{"etiqueta_label_configuracion", "GENERAL SETTINGS"},
			{"etiqueta_label_docColeccion", "Document collection directory"},
			{"etiqueta_label_nomColeccion", "Collection name"},
			{"etiqueta_checkbox_cbStopWords", "Use stopwords file"},
			{"etiqueta_borde_idiomaColeccion", "Collection's language"},
			{"etiqueta_radioButton_espanol", "Spanish"},
			{"etiqueta_radioButton_ingles", "English"},
			{"etiqueta_button_examinar", "Search..."},
			{"etiqueta_borde_nuevaColeccion", "New collection directory"},
			{"etiqueta_borde_existenteColeccion", "Load an existing collection"},
			{"etiqueta_label_colecciones","Collections"},
			{"etiqueta_label_etapaInicio","Start stage"},
			{"etiqueta_boton_borrar" , "Delete"},
			{"cbEtapa_Preprocesado" , "Documents's pre-processing"},
			{"cbEtapa_Indexacion" , "Indexing"},
			{"cbEtapa_Consultas" , "Queries"},
			{"titulo_ventana_dialog_coleccion", "Select the collection's folder"},
			{"titulo_ventana_dialog_stopWord", "Select the stopword file"},
			{"optionDialog_coleccion_no_seleccionada", "Collection no selected"},
                        {"optionDialog_stopWord_no_seleccionado", "Stopword no selected"},
			{"optionDialog_coleccion_no_cargada", "Make sure you have a collection loaded"},
			{"optionDialog_nombre_coleccion_vacio", "Write a name for collection's work folder"},
			{"optionDialog_stopword_no_cargado", "Make sure you have a stopword's file loaded"},
			{"optionDialog_coleccion_existente", "Exists a collection with this name. Overwrite?"},
			{"optionDialog_titulo_coleccion_existente", "Existing collection"},
			{"optionDialog_directorio_sin_documentos", "The folder no contains any file txt, html or xml"},
			{"etiqueta_optionDialog_creacionArchivoErronea", "ERROR: Try another name, make sure the folder is closed, or check the permissions."},
			{"etiqueta_optionDialog_cargaArchivoErronea", "ERROR: Can not save the configuration file. Make sure configuration file exist."},
			
			// Carga de colección
			
				{"optionDialog_borrar_coleccion_existente", "Are you sure you want to delete the collection "},
				{"optionDialog_titulo_borrar_coleccion", "Delete colection"},
				{"etiqueta_optionDialog_cargaColeccionErronea1", "ERROR: Can not load the stage "},
				{"etiqueta_optionDialog_cargaColeccionErronea2", "Only finished until the stage "},
				{"etiqueta_optionDialog_cargaColeccionErronea3", "Make sure the previous steps are completed."},
				{"etiqueta_optionDialog_numDocOriginalesErroneo","ERROR: Missing documents in original folder"},
				{"etiqueta_optionDialog_numDocProcesadosErroneo", "ERROR: Missing processed documents from the collection. Re-build the collection."},
				{"etiqueta_optionDialog_recIndiceErroneo", "ERROR: Unable to retrieve index. Re-build it"},
				{"etiqueta_optionDialog_cargaColeccion", "ERROR: Missing the collection original folder. Re-build the collection."},
				
			// Ventana ayuda configuración
				
				{"etiqueta_borde_panel_ayuda_configuracion","Help"},
				{"titulo_ventana_ayuda_configuracion","Help"},
				{"contenido_pane_ayuda_configuracion", "This window allows us to create a new collection or to load an existing collection."
				+ "<ul>"
					+ "<li>To create a new collection we must first select a folder in which the collection is located. To do this click on <b>Search</b> in the <b>Document collection directory</b>. "
					+ "Then we will name the collection.<br><br>"
					+ "The next step is to select whether or not to use an stopword's file. If we are going to use an stopword's file it should have txt extension. To load it we must click on "
					+ "<b>Search</b> where it says <b>Use stopwords file</b></li><br>"
					+ "<li>To load a collection we must have already created collections.<br><br>"
					+ "First we must <b>select the collection</b> we want to load and then select the <b>stage of the process</b> from which we want to load. Remember that we can only load until the stage we "
					+ "have completed in the creation of the information retrieval system.</li>"
				+ "</ul>"},
			
		// Ventana de procesamiento de documentos
		
			{"ventana_procesamiento_label7", "The lexical analysis of documents is running."},
			{"ventana_procesamiento_label8", "1. The documents are tokenized."},
			{"ventana_procesamiento_label9", "2. Stopwords are deleted (if applicable)."},
			{"ventana_procesamiento_label10", "3. The terms are stemmed."},
			{"titulo_ventana_preprocesamiento", "Preprocessing of documents."},
			
		// Dialog Cargando colección
			
			{"jdCargandoColeccion","Loading collection..."},
		
		// Ventana de indexación
		
			// General	
		
				{"etiqueta_label_archivo", "File: "},
				{"etiqueta_tab_preprocesamiento", "Preprocessing"},
				{"etiqueta_borde_leyenda_preprocesamiento", "Legend"},
				{"etiqueta_boton_siguiente_termino", "Next term"},
				{"etiqueta_boton_terminar_documento", "Finish the document"},
				{"etiqueta_boton_siguiente_doc", "Next document"},
				{"etiqueta_boton_siguente_paso","Next stage"},
				{"etiqueta_label_sin_cambios_preprocesamiento", "No change"},
				{"etiqueta_label_se_elimina_preprocesamiento", "It is removed"},
				{"etiqueta_label_se_modifica_preprocesamiento","It is modified"},
		
			// Preprocesado
		
				// Tokenización

					{"etiqueta_tab_tokenizacion_tokenizacion", "Tokenization"},
					{"etiqueta_borde_tokenizacion_archivo_original", "Original File"},
					{"etiqueta_borde_tokenizacion_archivo_tokenizado", "Tokenized File"},
					{"etiqueta_boton_iniciar_tokenizacion", "Start tokenization"},

				// Palabras vacías

					{"etiqueta_tab_tokenizacion_stopwords", "Stop words"},
					{"etiqueta_borde_palabrasVacias_conSW", "File with stop words"},
					{"etiqueta_borde_palabrasVacias_sinSW", "File without stop words"},
					{"etiqueta_boton_iniciar_SW", "Start stop words"},

				// Stemming

					{"etiqueta_tab_tokenizacion_stemming", "Stemming"},
					{"etiqueta_borde_stemming_conLemas", "No stemmized File"},
					{"etiqueta_borde_stemming_sinLemas", "Stemmized File"},
					{"etiqueta_boton_inicio_stemming", "Start stemming"},
					{"etiqueta_boton_crear_indice", "Create index"},
					{"etiqueta_label_stemming_se_lematiza", "It is stemmized"},
			
			// Ventana de creación del índice
		
				{"titulo_creacion_indice", "Processing index"},
				{"etiqueta_label19_creacion_indice","In this stage happen the creation of"},
				{"etiqueta_label20_creacion_indice","invert index"},
				{"etiqueta_label21_creacion_indice","This stage can be slow because of the amount of documents"},
				{"etiqueta_label22_creacion_indice","that can be in the collection"},
				{"label_estadoIndice_indexando", "Indexing: "},
				{"label_estadoIndice_rellenando", "Filling index tables"},

			// Indexación
				
				{"etiqueta_tab_indexacion", "Indexing"},
					
				// Indice Completo				

					{"etiqueta_tab_indice_indiceCompleto", "Index"},
					{"etiqueta_borde_indexacion_indiceCompleto", "Full index display"},
					{"etiqueta_borde_indexacion_informacionTermino", "Term information"},
					{"etiqueta_borde_indexacion_informacionIndice", "Index information"},
					{"etiqueta_borde_indexacion_informacionTerminoDocumento", "Term information in document"},
					{"etiqueta_label_indexacion_rutaIndice", "Index path:"},
					{"etiqueta_label_indexacion_numDocIndice", "Documents number:"},
					{"etiqueta_label_indexacion_numTerminosIndice", "Terms number:"},
					{"etiqueta_label_indexacion_versionIndice", "Index version:"},
					{"label_columna_termino", "Term"},
					{"label_columna_numDocumentos", "Num. Documents"},
					{"label_columna_numOcurrencias", "Freq. in collection"},
					{"label_button_indice_siguienteEtapa", "Next stage"},
	
				// Gráficas
				
					{"etiqueta_tab_indice_graficas", "Graphics"},
					{"etiqueta_borde_indexacion_graficas", "Graph"},
					{"etiqueta_borde_indexacion_graficasDocumentos", "Documents"},
					{"etiqueta_borde_indexacion_graficasTerminos", "Terms"},
					{"boton_indexacion_guardar_grafica", "Save graph"},

			// Consultas
					
				// General
			
					{"etiqueta_busqueda_buscar", "Retrieve"},
					{"etiqueta_label_busqueda_consulta", "Query"},
					{"etiqueta_borde_busqueda_visualizacionBusqueda", "Hits"},
					{"etiqueta_borde_busqueda_documentos", "Documents"},
					{"etiqueta_borde_busqueda_documento", "Document"},
					{"etiqueta_boton_busqueda_buscar", "Search"},
					{"etiqueta_label_busqueda_numDocumentos", "Number of documents to retrieve:"},
					{"etiqueta_label_busqueda_tiempoRec", "Search time:"},
					{"etiqueta_label_busqueda_documentosRec", "Retrieved documents:"},
					{"etiqueta_label_busqueda_consultaResultante", "Final query:"},
					{"etiqueta_tab_busqueda_explanation", "Details"},
					{"optionDialog_consulta_vacia","Write a query"},
					{"etiqueta_label_explanation", "Document explanation: "},
					{"etiqueta_optionDialog_consultaErronea", "Query is wrong: Use the logics operators AND, OR, NOT, ) OR (. "},
					{"optionDialog_ver_visor_pdf","Opening the pdf with the determinated program?"},
					{"optionDialog_titulo_abrirPDF", "Open pdf file"},
					
				// Vectorial
				
					{"etiqueta_tab_busqueda_vectorial", "Vectorial querying"},
				
				// Booleana
				
					{"etiqueta_tab_busqueda_booleana", "Boolean Querying"},
					{"etiqueta_tab_busqueda_booleana_lucene", "Query Lucene:"},
				
				// Probabilística
				
					{"etiqueta_tab_busqueda_probabilistica", "Probabilistic querying"},
					{"optionDialog_consulta_vacia_probabilistica","The query, saturation and normalization can not be empty"},
					{"optionDialog_consulta_probabilistica_parametros","Wrong values: Saturation: [0,∞), Normalization: [0,1]"},
					{"etiqueta_label_busqueda_probabilistica_saturacion", "Saturation:"},
					{"etiqueta_label_busqueda_probabilistica_normalizacion", "Normalization:"},
		
		// Ventana de ayuda
					
			// Contenidos
					
					{"etiqueta_borde_ayuda_contenidos","Contents"},
					{"etiqueta_borde_ayuda_descripcion", "Description"},
					{"lista_ayuda_contenido_0","Retrieval Information System"},
					{"lista_ayuda_contenido_1","Tokenization process"},
					{"lista_ayuda_contenido_2","Stop words process"},
					{"lista_ayuda_contenido_3","Stemming process"},
					{"lista_ayuda_contenido_4", "Indexing process"},
					{"lista_ayuda_contenido_5","Full index"},
					{"lista_ayuda_contenido_6", "Graphs"},
					{"lista_ayuda_contenido_7","Vectorial Querying"},
					{"lista_ayuda_contenido_8","Boolean Querying"},
					{"lista_ayuda_contenido_9","Probabilistic Querying"},
					{"lista_ayuda_contenido_10","Bibliography"},
			
			// Sistema de recuperación de información
					
				{"ayuda_cuadro_texto_contenido_0", "An <b>Information Retrieval System</b> is a system by which it receives a query from a user and must find, "
					+ "within a collection of documents, the location of the information required by the user.<br><br>"
					+ "Once the query is done, the results are retrieved as an ordered list of documents."},
			
			// Proceso de tokenización
			
				{"ayuda_cuadro_texto_contenido_1","In the <b>tokenization</b> process the text containing the terms that are indexed is recognized. Once the text is obtained it is proceeded to "
					+ "identify its terms and to eliminate all those that are not useful to us, such as punctuation marks, accents, hyphens, or transforming letters from uppercase to lowercase."
					+ "With this we allow that when entering a word that is accompanied with any of these symbols can be found in the index.<br><br>"
					+ "In the screen of tokenización we can load the first document of the collection by pressing in <b>Start tokenización</b>. The original document will be loaded in the top box and "
					+ "in the lower one we can see the result of the tokenization by clicking on the <b>Next term</b> button (term to term) or the <b>Finish the document</b> button (complete document). We can "
					+ "also click on <b>Next document</b> to go to the next document in the collection.<br><br>" 
					+ "If we want to go to the next stage we just have to click on <b>Next stage</b>"},
				
			// Proceso de eliminación de palabras vacías.
			
				{"ayuda_cuadro_texto_contenido_2", "The <b>Stop words</b> are terms that are repeated many times and, therefore, are a poor indicator of content. They are words like articles, prepositions, conjunctions, "
					+ "auxiliary verbs...<br><br>"
					+ "Removing these words also saved us a memory by not having to index them.<br><br>"
					+ "In the <b>Stop words</b> screen we can load the first tokenized document in the collection by pressing <b>Start stop words</b>. "
					+ "The tokenized document will be loaded in the upper box and in the lower part we can see the result of the deletion of the empty words by clicking on the "
					+ "<b>Next term</b> button or the <b>Finish the document</b> (complete document). We can also click on <b>Next document</b> to go to the next tokenized document in the collection.<br><br>"
					+ "If we want to go to the next stage we just have to click on <b>Next stage</b>."},
			
			// Proceso de stemming
				
				{"ayuda_cuadro_texto_contenido_3", "The <b>Stemming</b> stage is a process dependent on the language of the collection. It consists of reducing and grouping words that are semantically related. "
					+ "More precisely, stemming reduces the different forms of words that occur because of inflection or derivation to a common stem and keep them in the index.<br><br>"
					+ "In the <b>Stemming</b> screen we can load the first document to be stemmed from the collection by clicking on <b>Start stemming</b>. The document will be loaded in the top box "
					+ "without being stemmed and in the lower one we can see the result of the process by clicking on the button <b>Next term</b> (term to term) or the button <b>Finish the document</b> (complete document). "
					+ "We can also click on <b>Next document</b> to go to the next document without lematizing the collection.<br><br>"
					+ "The next step is to create the Lucene index. To do this, click <b>Create Index</b>."},
			
			// Proceso de indexación
				
				{"ayuda_cuadro_texto_contenido_4", "The next stage to stemming process is <b>create the index</b><br><br> "
					+ "The fundamental concepts in Lucene are <b>index</b>, <b>document</b>, <b>field</b> and <b>term</b>. An index contains a sequence of documents where:<br>"
					+ "<ul>"
						+ "<li>A <b>document</b> is a sequence of <b>fields</b>.</li>"
						+ "<li>A <b>field</b> is a named sequence of <b>terms</b>.</li>"
						+ "<li>A <b>term</b> is a <b>string</b>.</li>"
					+ "</ul>"
					+ "Lucene's index falls into the family of indexes known as an <b>inverted index</b>. This is because it can list, for a term, the documents that contain it. This is the inverse of the natural relationship, in "
					+ "which documents list terms. The index stores statistics about terms in order to make term-based search more efficient.<br><br>"},
			
			// Ficha de indice completo
				
			{"ayuda_cuadro_texto_contenido_5", "In the <b>Full index</b> tab we have a table with all collection's terms sorted by term's number. This table also shows the number of documents that the "
				+ "term contains and the <b>Idf</b>. All these columns can be sorted in ascending order or descending by their value by clicking on the column's header.<br><br>"
				+ "To the right of the full index's table there is the table of <b>term information</b>. This table shows the document number where the term is located, the position in the stemmed document, "
				+ "the start offset and the end offset. Clicking on any of the rows in the table will load in the lower frame the tagged file and highlight the word in the selected position and document.<br><br>"
				+ "On the right side you can see information about the index"},
			
			// Ficha de creación de gráficas
			
				{"ayuda_cuadro_texto_contenido_6", "The <b>graphs</b> are intended to visualize the distribution of the terms in the collection.<br><br>"
					+ "There are two types:"
					+ "<ul>"
						+ "<li>A <b>graph by term</b> that shows the frequency of term by document.</li>"
						+ "<li>A <b>graph by document</b> where we can see the frequencies of the terms in that document.</li>"
					+ "</ul>"
					+ "To draw the graph we have to click on a term or a document and click on its respective button called <b>Graphic</b>."},
			
			// Ficha de consulta vectorial
				
			{"ayuda_cuadro_texto_contenido_7", "In the <b>vectorial querying</b> tab we can make a query using the <b>vector space model</b>.<br><br>"
				+ "In this model, documents and queries are part of a vector t-space where 't' is the number of indexed terms. Each term is a dimension and its weight is equal to its <b>Tf-idf</b>"
				+ "This model not require weights to be Tf-idf values, but Tf-idf values are believed to produce search results of high quality. "
				+ "The score of document <b>d</b> for query <b>q</b> is the <b>cosine similarity</b> of the weighted query vectors V(q) and V(d)."
				+ "It is given by the expression:<br><br>"
				+ "<table cellpadding=\"2\" cellspacing=\"2\" border=\"0\" style=\"margin-left:auto; margin-right:auto\">"
					+ "<tr>"
						+ "<td valign=\"middle\" align=\"right\" rowspan=\"1\">" +
							"cosine similarity(q,d) &nbsp; = &nbsp;"
						+ "</td>"
						+ "<td valign=\"middle\" align=\"center\">" 
							+ "<table>" 
								+ "<tr><td align=\"center\" style=\"text-align: center\">V(q)&nbsp;·&nbsp;V(d)</td></tr>" 
								+ "<tr><td align=\"center\" style=\"text-align: center\">–––––––––</td></tr>" 
								+ "<tr><td align=\"center\" style=\"text-align: center\">|V(q)|&nbsp;|V(d)|</td></tr>" 
							+ "</table>" 
						+ "</td>" 
					+ "</tr>"
				+ "</table>"
				+ "Where: <ul>"
					+ "<li>V(q)· V(d) is the dot product of the weighted vectors.</li>"
					+ "<li>|V(q)| and |V(d)| are their Euclidean norms.</li>"
				+ "</ul>"
		
				+ "To make a query we will enter a text in the box reserved for it and press <b>ENTER</b> or the button <b>Search</b>. We can also put the maximum number of documents that we want to retrieve "
				+ "in the right drop-down list. Once the query is done, we will obtain the best positioned documents, the transformation of the query, the time taken to retrieve the documents and the "
				+ "total number of documents retrieved.<br><br>"
				+ "If we want to see where in the document are the terms we are looking for we can click on the document in the table and it will automatically be loaded in the box on the right. "
				+ "In addition, we can see an explanation of how it has come to get that weight for that document by clicking on the <b>Details</b> button."},
			
			// Ficha consulta booleana
			
			{"ayuda_cuadro_texto_contenido_8", "In the <b>Boolean query</b> tab we can make a query indicating which terms we want to find in a document or not. For this we will use the logical operators AND, OR, NOT and the "
				+ "parentheses. The results will be the documents that fulfill that condition and, therefore, there is no weight.<br><br>"
				+ "To perform a Boolean query we must enter the query correctly in the text box reserved for it and then press ENTER or the search button. We can also put the maximum number of documents we want "
				+ "to retrieve from the right-hand drop-down list. Once the query is done we will obtain the documents that fulfill it, the transformation of the query timed, the time taken to retrieve the "
				+ "documents, the number of documents retrieved and a version of the query readable for the user in which it can be seen as Lucene Uses the <b>+ (AND)</b>, <b>- (NOT)</b> operators, and the "
				+ "<b>parentheses</b>. For the <b>OR</b> operator does not put anything. If we want to see where in the document are the terms we are looking for we can click on the document in the table and it will "
				+ "automatically be loaded in the box on the right."},
			
			// Ficha consulta probabilística
			
			{"ayuda_cuadro_texto_contenido_9", "In the <b>probabilistic query</b> tab we can make a query using the <b>BM25</b> ranking algorithm.<br><br>"
				+ "This algorithm calculates the probability that a document is relevant to a query depending on the terms in them. It is based on the model of binary independence with which "
				+ "it establishes the weights of the terms in the documents and in the consultations. Use probabilistic arguments and experimental validation. <br>" 
				+ "BM25 has performed well in retrieval tests in TREC (<b>T</b>ext <b>RE</b>trieval <b>C</b>onference) and since then has influenced the classification algorithms Of commercial search engines, "
				+ "including web search engines <br><br>" 
				+ "To perform a probabilistic query we must enter the text to be searched in the box reserved for it, the normalization*, the saturation* and then press <b>ENTER</b> or the <b>search</b> button. "
				+ "We can also put the maximum number of documents we want to retrieve from the right-hand drop-down list. Once the query is done, we will obtain the best positioned documents, "
				+ "the transformation of the query, the time taken to recover the documents and the total number of documents retrieved."
				+ "If we want to see where in the document are the terms we are looking for we can click on the document in the table and it will automatically be loaded in the box on the right. "
				+ "In addition, we can see an explanation of how it has come to get that weight for that document by clicking on the <b>Details</b> button." 
				+ "*<ul><li>The <b>saturation</b> controls non-linear term frequency normalization</li><li>The <b>normalization</b> controls to what degree document length normalizes tf values.</li></ul>"},
			
			// Bibliografía
			
			{"ayuda_cuadro_texto_contenido_10", "Bibliography:<ol>"
					+ "<li>Recuperación de información: Un enfoque práctico y multidisciplinar. F. Cacheda Seijo, J. M. Fernández Luna, J. F. Huete Guadix. Editorial: RA-MA. ISBN: 978-84-9964-112-6</li>"
					+ "<li>Lucene in action. Otis Gospodnetic, Erik Hatcher. Editorial: MANNING. ISBN: 1-932394-28-1</li>"
					+ "<li>Search Engines: Information retrieval in practice. W. Bruce Croft, Donald Metzler, Trevor Strohman. Editorial: Pearson. ISBN: 0-13-136489-8</li>"
					+ "<li>Similitud coseno - <a href=\"#\">https://es.wikipedia.org/wiki/Similitud_coseno</a></li>"
					+ "<li>Apache Lucene - TFIDFSimilarity - <a href=\"#\">https://lucene.apache.org/core/6_3_0/core/org/apache/lucene/search/similarities/TFIDFSimilarity.html</a></li>"
					+ "<li>Apache Lucene - Index File Formats -  <a href=\"#\">https://lucene.apache.org/core/3_5_0/fileformats.html</a></li>"
					+ "<li>Okapi at TREC-3 - <a href=\"#\">http://trec.nist.gov/pubs/trec3/papers/city.ps.gz</a></li>"
				+ "</ol>"},
		
		
		// Ventana visor de documentos.
		
			{"etiqueta_borde_visorDocumentos_stopword", "Stop word file"},
			{"etiqueta_borde_visorDocumentos_archivos", "Documents"},
			{"etiqueta_borde_visorDocumentos_descripcion", "Description"},
			{"etiqueta_ventana_titulo_visorDocumentos", "Original document viewer"},
		
		// Elementos de menú
		
			{"label_menuitem_salir","Exit"},
			{"label_menuitem_inicio","New session"},
			{"label_menuitem_ayuda","Help"},
			{"label_menuitem_docOrig","Originals documents viewer"},
			{"label_menuitem_acercaDe","About..."},
			{"label_menuitem_bArchivo","File"}

	};
};