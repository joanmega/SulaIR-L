package etiquetas;

import java.util.ListResourceBundle;

/**
 *
 * @author joanm
 */
public class Etiquetas_es extends ListResourceBundle{

	@Override
	protected Object[][] getContents() {
		return contenido;
	}

	private Object[][] contenido ={
		
		// Generales
		
			{"etiqueta_boton_aceptar", "Aceptar"},
			{"etiqueta_boton_salir", "Salir"},
			{"etiqueta_label_atencion", "Atención"},
			{"boton_continuar", "Continuar"},
		
		// Pagina Principal
		
			{"etiqueta_label_bienvenida", "Bienvenid@ a SulaIR-L"},
			{"etiqueta_boton_entrar", "Entrar"},
			{"etiqueta_borde_idioma", "Idioma"},
			{"etiqueta_label_desc1", "Una aplicación para el aprendizaje y la enseñanza "},
			{"etiqueta_label_desc2", "de la Recuperación de Información basada en Lucene"},
		
		// Ventana de configuración
		
			{"titulo_ventana_configuracion", "Configuración para la nueva sesión de SulaIR-L"},
			{"etiqueta_label_configuracion", "CONFIGURACIÓN GENERAL"},
			{"etiqueta_label_docColeccion", "Directorio de la colección de documentos"},
			{"etiqueta_label_nomColeccion", "Nombre de la colección"},
			{"etiqueta_checkbox_cbStopWords", "Utilizar archivo de palabras vacías"},
			{"etiqueta_borde_idiomaColeccion", "Idioma de la colección"},
			{"etiqueta_radioButton_espanol", "Español"},
			{"etiqueta_radioButton_ingles", "Inglés"},
			{"etiqueta_button_examinar", "Examinar..."},
			{"etiqueta_borde_nuevaColeccion", "Directorios de la nueva colección"},
			{"etiqueta_borde_existenteColeccion", "Cargar una colección existente"},
			{"etiqueta_label_colecciones","Colecciones"},
			{"etiqueta_label_etapaInicio","Etapa de inicio"},
			{"etiqueta_boton_borrar" , "Borrar"},
			{"cbEtapa_Preprocesado" , "Preprocesamiento de documentos"},
			{"cbEtapa_Indexacion" , "Indexación"},
			{"cbEtapa_Consultas" , "Consultas"},
			{"titulo_ventana_dialog_coleccion", "Seleccione la carpeta de la colección"},
			{"titulo_ventana_dialog_stopWord", "Seleccione el archivo stopWord"},
			{"optionDialog_coleccion_no_seleccionada", "No se ha seleccionado una colección"},
                        {"optionDialog_stopWord_no_seleccionado", "No se ha seleccionado un archivo de palabras vacías"},
			{"optionDialog_coleccion_no_cargada", "Asegúrese de que tiene una colección cargada"},
			{"optionDialog_nombre_coleccion_vacio", "Introduce un nombre para el directorio de trabajo"},
			{"optionDialog_stopword_no_cargado", "Introduce un archivo para las palabras vacías"},
			{"optionDialog_coleccion_existente", "Existe una colección con el mismo nombre, ¿desea sustituirla?"},
			{"optionDialog_titulo_coleccion_existente", "Colección existente"},
			{"optionDialog_directorio_sin_documentos", "El directorio seleccionado no tiene documentos txt, html o xml"},
			{"etiqueta_optionDialog_creacionArchivoErronea", "ERROR: No se ha podido guardar el archivo de configuración. Pruebe otro nombre o compruebe que la carpeta está cerrada o compruebe los permisos."},
			{"etiqueta_optionDialog_cargaArchivoErronea", "ERROR: No se ha podido cargar el archivo de configuración. Compruebe que el archivo de configuración existe."},
			
			// Carga de colección
			
				{"optionDialog_borrar_coleccion_existente", "¿Está seguro que desea eliminar la colección "},
				{"optionDialog_titulo_borrar_coleccion", "Borrar colección"},
				{"etiqueta_optionDialog_cargaColeccionErronea1", "ERROR: No se ha podido cargar la colección en la etapa "},
				{"etiqueta_optionDialog_cargaColeccionErronea2", "Solo se ha realizado hasta la etapa "},
				{"etiqueta_optionDialog_cargaColeccionErronea3", "Asegúrese de que se han terminado las etapas anteriores"},
				{"etiqueta_optionDialog_numDocOriginalesErroneo","ERROR: Faltan documentos en la carpeta original"},
				{"etiqueta_optionDialog_numDocProcesadosErroneo", "ERROR: Faltan documentos procesados de la colección. Cree de nuevo la colección."},
				{"etiqueta_optionDialog_recIndiceErroneo", "ERROR: No se puede recuperar el índice. Creelo de nuevo"},
				{"etiqueta_optionDialog_cargaColeccion", "ERROR: Falta la carpeta original de la colección. Cree de nuevo la colección."},
				
			// Ventana ayuda configuración
				
				{"etiqueta_borde_panel_ayuda_configuracion","Ayuda"},
				{"titulo_ventana_ayuda_configuracion","Ayuda"},
				{"contenido_pane_ayuda_configuracion", "En esta ventana se podrá crear una colección nueva o cargar una ya existente."
				+ "<ul>"
					+ "<li>Para crear una nueva colección primero debemos de <b>seleccionar una carpeta</b> en la que se encuentre la colección. Para ello "
					+ "pulsamos en examinar donde dice <b>Directorio de la colección de documentos</b>. Acto seguido le pondremos un nombre a la colección.<br><br>"
					+ "El siguiente paso es seleccionar si vamos a utilizar un <b>archivo de palabras vacías</b> o no. Si vamos a utilizar un archivo de palabras vacías "
					+ "este deberá estar en formato txt. Para cargarlo debemos de pulsar en <b>examinar</b> en el apartado <b>Utilizar archivo de palabras vacías</b></li><br>"
					+ "<li>Para cargar una coleccion debemos de tener colecciones ya creadas.<br><br>"
					+ "Primero debemos de <b>seleccionar</b> la colección que queremos cargar y acto seguido seleccionar la <b>etapa del proceso</b> desde la que queremos cargar. "
					+ "Recordemos que solamente podremos cargar hasta la etapa que hayamos alcanzado en la creación del sistema de recuperación de información.</li>"
				+ "</ul>"},
		
		// Ventana de procesamiento de documentos
		
			{"ventana_procesamiento_label7", "En este proceso se realiza el análisis léxico de los documentos."},
			{"ventana_procesamiento_label8", "1. Se tokeniza el documento."},
			{"ventana_procesamiento_label9", "2. Se eliminan las palabras vacías (si corresponde)."},
			{"ventana_procesamiento_label10", "3. Se realiza la estemización."},
			{"titulo_ventana_preprocesamiento", "Procesamiento de documentos"},
			
		// Dialog Cargando colección
			
			{"jdCargandoColeccion", "Cargando colección..."},
		
		// Ventana de indexación
		
			// General	
		
				{"etiqueta_label_archivo", "Archivo: "},
				{"etiqueta_tab_preprocesamiento", "Preprocesamiento"},
				{"etiqueta_borde_leyenda_preprocesamiento", "Leyenda"},
				{"etiqueta_boton_siguiente_termino", "Siguiente término"},
				{"etiqueta_boton_terminar_documento", "Terminar documento"},
				{"etiqueta_boton_siguiente_doc", "Siguiente documento"},
				{"etiqueta_boton_siguente_paso","Siquiente paso"},
				{"etiqueta_label_sin_cambios_preprocesamiento", "No cambia"},
				{"etiqueta_label_se_elimina_preprocesamiento", "Se elimina"},
				{"etiqueta_label_se_modifica_preprocesamiento","Se modifica"},
			
			// Preprocesado
				
				// Tokenización

					{"etiqueta_tab_tokenizacion_tokenizacion", "Tokenización"},
					{"etiqueta_borde_tokenizacion_archivo_original", "Archivo original"},
					{"etiqueta_borde_tokenizacion_archivo_tokenizado", "Archivo tokenizado"},
					{"etiqueta_boton_iniciar_tokenizacion", "Iniciar tokenización"},

				// Palabras vacías

					{"etiqueta_tab_tokenizacion_stopwords", "Palabras vacías"},
					{"etiqueta_borde_palabrasVacias_conSW", "Archivo con palabras vacías"},
					{"etiqueta_borde_palabrasVacias_sinSW", "Archivo sin palabas vacías"},
					{"etiqueta_boton_iniciar_SW", "Empezar palabras vacías"},

				// Stemming

					{"etiqueta_tab_tokenizacion_stemming", "Lematización"},
					{"etiqueta_borde_stemming_conLemas", "Archivo no lematizado"},
					{"etiqueta_borde_stemming_sinLemas", "Archivo lematizado"},
					{"etiqueta_boton_inicio_stemming", "Iniciar stemming"},
					{"etiqueta_boton_crear_indice", "Crear indice"},
					{"etiqueta_label_stemming_se_lematiza", "Se lematiza"},
				
			// Ventana de creación del índice
				
				{"titulo_creacion_indice", "Procesando el índice"},
				{"etiqueta_label19_creacion_indice","En este proceso se realiza la creación del índice invertido de la"},
				{"etiqueta_label20_creacion_indice","colección de documentos."},
				{"etiqueta_label21_creacion_indice","Este proceso puede ser lento debido a la cantidad de"},
				{"etiqueta_label22_creacion_indice","documentos que haya en la colección"},
				{"label_estadoIndice_indexando", "Indexando: "},
				{"label_estadoIndice_rellenando", "Rellenando tablas del índice"},
				
			// Indexación
			
				{"etiqueta_tab_indexacion", "Indexación"},
			
				// Indice Completo
			
					{"etiqueta_tab_indice_indiceCompleto", "Índice"},
					{"etiqueta_borde_indexacion_indiceCompleto", "Visualización del índice completo"},
					{"etiqueta_borde_indexacion_informacionTermino", "Información del término"},
					{"etiqueta_borde_indexacion_informacionIndice", "Información del índice"},
					{"etiqueta_borde_indexacion_informacionTerminoDocumento", "Información del término en el documento"},
					{"etiqueta_label_indexacion_rutaIndice", "Ruta del índice:"},
					{"etiqueta_label_indexacion_numDocIndice", "Número de documentos:"},
					{"etiqueta_label_indexacion_numTerminosIndice", "Numero de términos:"},
					{"etiqueta_label_indexacion_versionIndice", "Versión del índice:"},
					{"label_columna_termino", "Término"},
					{"label_columna_numDocumentos", "Num. Documentos"},
					{"label_columna_numOcurrencias", "Frecuencia en la colección"},
					{"label_button_indice_siguienteEtapa", "Siguiente etapa"},
				
				// Gráficas
				
					{"etiqueta_tab_indice_graficas", "Gráficas"},
					{"etiqueta_borde_indexacion_graficas", "Gráfica"},
					{"etiqueta_borde_indexacion_graficasDocumentos", "Documentos"},
					{"etiqueta_borde_indexacion_graficasTerminos", "Términos"},
					{"boton_indexacion_guardar_grafica", "Guardar gráfica"},
					{"etiqueta_radioButton_grafica_descendente", "Descendente"},
					{"etiqueta_radioButton_grafica_ascendente", "Ascendente"},
				
			// Consultas
								
				// General
			
					{"etiqueta_busqueda_buscar", "Búsqueda"},
					{"etiqueta_label_busqueda_consulta", "Consulta"},
					{"etiqueta_borde_busqueda_visualizacionBusqueda", "Visualización de la búsqueda"},
					{"etiqueta_borde_busqueda_documentos", "Documentos"},
					{"etiqueta_borde_busqueda_documento", "Documento"},
					{"etiqueta_boton_busqueda_buscar", "Buscar"},
					{"etiqueta_label_busqueda_numDocumentos", "Número de documentos a recuperar:"},
					{"etiqueta_label_busqueda_tiempoRec", "Tiempo de búsqueda:"},
					{"etiqueta_label_busqueda_documentosRec", "Documentos recuperados:"},
					{"etiqueta_label_busqueda_consultaResultante", "Consulta final:"},
					{"etiqueta_tab_busqueda_explanation", "Detalles"},
					{"optionDialog_consulta_vacia","Introduce una consulta"},
					{"etiqueta_label_explanation", "Detalles del documento: "},
					{"etiqueta_optionDialog_consultaErronea", "La consulta es incorrecta: Usa los operadores lógicos AND, OR, NOT, ) OR (. "},
					{"optionDialog_ver_visor_pdf","¿Desea abrir el visor pdf predeterminado"},
					{"optionDialog_titulo_abrirPDF", "Abrir archivo pdf"},
				
				// Vectorial
				
					{"etiqueta_tab_busqueda_vectorial", "Consulta vectorial"},
				
				// Booleana
				
					{"etiqueta_tab_busqueda_booleana", "Consulta booleana"},
					{"etiqueta_tab_busqueda_booleana_lucene", "Consulta Lucene:"},
				
				// Probabilística
					
					{"etiqueta_tab_busqueda_probabilistica", "Consulta probabilística"},
					{"optionDialog_consulta_vacia_probabilistica","La consulta, la saturación y la normalización no pueden estar vacías"},
					{"optionDialog_consulta_probabilistica_parametros","Valores no válidos: Saturación: [0,∞), Normalización: [0,1]"},
					{"etiqueta_label_busqueda_probabilistica_saturacion", "Saturación:"},
					{"etiqueta_label_busqueda_probabilistica_normalizacion", "Normalización:"},
		
		// Ventana de ayuda
					
			// Contenidos
					
					{"etiqueta_borde_ayuda_contenidos","Contenidos"},
					{"etiqueta_borde_ayuda_descripcion", "Descripción"},
					{"lista_ayuda_contenido_0","Sistema de recuperación de información"},
					{"lista_ayuda_contenido_1","Proceso de tokenización"},
					{"lista_ayuda_contenido_2","Proceso de eliminación de palabras vacías"},
					{"lista_ayuda_contenido_3","Proceso de stemming"},
					{"lista_ayuda_contenido_4", "Proceso de indexación"},
					{"lista_ayuda_contenido_5","Índice completo"},
					{"lista_ayuda_contenido_6", "Gráficas"},
					{"lista_ayuda_contenido_7","Consulta Vectorial"},
					{"lista_ayuda_contenido_8","Consulta Booleana"},
					{"lista_ayuda_contenido_9","Consulta Probabilística"},
					{"lista_ayuda_contenido_10","Bibliografía utilizada"},
					
			
			// Sistema de recuperación de información
					
				{"ayuda_cuadro_texto_contenido_0", "Un <b>Sistema de Recuperación de Información</b> es un sistema por el cuál se recibe una consulta por parte de un usuario y se debe de encontrar, "
					+ "dentro de una colección de documentos, la localización de la información requerida por el usuario.<br><br>"
					+ "Una vez hecha la consulta se recuperan los resultados como una lista ordenada de documentos."},
			
			// Proceso de tokenización
			
				{"ayuda_cuadro_texto_contenido_1", "En el proceso de <b>tokenización</b> se reconoce el texto que contiene los términos que serán indexados. Una vez obtenido el texto se procede a "
					+ "identificar sus términos y a eliminar todos los que no nos sean de utilidad como puede ser signos de puntuacion, acentos, guiones o transformar las letras de "
					+ "mayúscula a minúscula. Con esto permitimos que cuando se introduzca una palabra que esté acompañada con cualquiera de estos símbolos puede ser encontrada en el índice.<br><br>"
					+ "En la pantalla de tokenización podemos cargar el primer documento de la colección pulsando en <b>Iniciar tokenización</b>. Se cargará en en cuadro superior el documento original "
					+ "y en el inferior podemos ir viendo el resultado de la tokenización pulsando en el botón <b>Siguiente término</b> (término a término) o en el botón <b>Terminar documento</b> "
					+ "(documento completo). También podemos pulsar en <b>Siguiente documento</b> para pasar al siguiente documento de la colección.<br><br>"
					+ "Si queremos pasar a la siguiente etapa solamente debemos de pulsar en <b>Siguiente paso</b>."},
			
			// Proceso de eliminación de palabras vacías.
			
				{"ayuda_cuadro_texto_contenido_2", "Las <b>palabras vacías</b> son términos que se repiten muchas veces y, por tanto, son un pobre indicador de contenido. Son palabras como los artículos, "
					+ "preposiciones, conjunciones, verbos auxiliares...<br><br> "
					+ "Quitando estas palabras también conseguimos un ahorro en la memoria al no tener que indexarlas.<br><br>"
					+ "En la pantalla de <b>Palabras vacías</b> podemos cargar el primer documento tokenizado de la colección pulsando en <b>Iniciar palabras vacías</b>. Se cargará en en cuadro superior el documento tokenizado "
					+ "y en el inferior podemos ir viendo el resultado de la eliminación de las palabras vacías pulsando en el botón <b>Siguiente término</b> (término a término) o en el botón "
					+ "<b>Terminar documento</b> (documento completo). También podemos pulsar en <b>Siguiente documento</b> para pasar al siguiente documento tokenizado de la colección.<br><br>"
					+ "Si queremos pasar a la siguiente etapa solamente debemos de pulsar en <b>Siguiente paso</b>."},
			
			// Proceso de stemming
			
				{"ayuda_cuadro_texto_contenido_3", "La etapa de <b>stemming</b> o de <b>lematización</b> es un proceso dependiente del idioma en el que se encuentre la colección, ya que consiste en reducir y agrupar palabras a partir "
					+ "de su lexema. Por tanto, en este proceso nos quedamos solamente con las palabras que tiene la misma raíz y las guardamos en el índice.<br><br>"
					+ "En la pantalla de <b>Lematización</b> podemos cargar el primer documento a lematizar de la colección pulsando en <b>Iniciar stemming</b>. Se cargará en en cuadro superior el documento sin lematizar "
					+ "y en el inferior podemos ir viendo el resultado del proceso pulsando en el botón <b>siguiente término</b> (término a término) o en el botón "
					+ "<b>terminar documento</b> (documento completo). También podemos pulsar en <b>siguiente documento</b> para pasar al siguiente documento sin lematizar de la colección.<br><br>"
					+ "El siguiente paso es crear el índice de Lucene. Para ello debemos de pulsar en <b>Crear índice</b>."},
			
			// Proceso de indexación
			
				{"ayuda_cuadro_texto_contenido_4", "Una vez obtenido los términos stemmizados pasamos al proceso de <b>índexación</b>. <br><br> "
					+ "Los conceptos fundamentales del índice de <b>Lucene</b> son los <b>documentos</b>, los <b>campos</b> y los <b>terminos</b>. Un índice contiene una secuencia de documentos en los cuales:<br>"
					+ "<ul>"
						+ "<li>Un <b>documento</b> es una secuencia de <b>campos</b>.</li>"
						+ "<li>Un <b>campo</b> es una secuencia de <b>términos</b></li>"
						+ "<li>Un <b>término</b> es un <b>String</b></li>"
					+ "</ul>"
					+ "El índice de Lucene se encuentra dentro de la familia llamada <br>indices invertidos</br>. Esto es porque podemos listar, para un término, todos los documentos que lo contienen. Es la forma "
					+ "inversa de la indexación natura en la cual los documentos listan los términos. El índice almacena estadísticas sobre los términos con el fin de hacer más eficiente la búsqueda basada en términos<br><br>"},
			
			// Ficha de indice completo
				
			{"ayuda_cuadro_texto_contenido_5", "En la ficha de <b>Indice completo</b> podemos ver una tabla con todos los términos de la colección ordenados por número de término. En esta tabla también podemos "
				+ "ver el <b>número de ocurrencias</b> del término en la colección y su <b>Idf</b>. Todas estas columnas pueden ser ordenadas en orden ascendete o descendete por su valor pulsando en la cabecera de "
				+ "la columna<br><br>"
				+ "A la derecha de la tabla del índice completo tenemos la tabla <b>Información del término</b>. En esta tabla podemos ver el número de documento en el que se encuentra el término, la posición que "
				+ "ocupa en el documento lematizado y en qué posición empieza y termina. Si pulsamos sobre alguna de las filas de la tabla se cargará en el cuadro inferior el archivo lematizado y se resaltará la palabra "
				+ "en la posición y documento seleccionado<br><br>"
				+ "En la parte derecha se puede ver información respecto al índice."},
			
			// Ficha de creación de gráficas
			
				{"ayuda_cuadro_texto_contenido_6", "Las <b>gráficas</b> tienen como objetivo visualizar la distribución de los términos en la colección.<br><br>"
					+ "Podemos encontrar dos tipos:"
					+ "<ul>"
						+ "<li>Una <b>gráfica por termino</b> donde podemos ver en qué documentos se repite más un término.</li>"
						+ "<li>Una <b>gráfica por documento</b> donde podemos ver las frecuencias de los términos en ese documento.</li>"
					+ "</ul>"
					+ "Para dibujar la gráfica tenemos que pulsar sobre un término o un documento y pulsar sobre su respectivo botón llamado <b>Gráfica</b>"},
			
			// Ficha de consulta vectorial
			
			{"ayuda_cuadro_texto_contenido_7", "En la ficha de <b>consulta vectorial</b> podemos hacer una consulta utilizando el <b>Modelo de espacio vectorial</b>.<br><br>"
				+ "En este modelo, documentos y consultas forman parte de un t-espacio vectorial donde 't' es el número de términos indexados. Cada término es una dimensión y su peso es igual a su <b>Tf-idf</b>. "
				+ "Para un modelo de espacio vectorial no es obligatorio tener los Tf-idf de los términos pero Lucene los utiliza para obtener resultados de mejor calidad. "
				+ "El valor del documento <b>d</b> para la consulta <b>q</b> es la similitud del coseno de los vectores de la consulta ponderados V(q) y V(d)."
				+ "Viene dado por la expresión:<br><br>"
				+ "<table cellpadding=\"2\" cellspacing=\"2\" border=\"0\" style=\"margin-left:auto; margin-right:auto\">"
					+ "<tr>"
						+ "<td valign=\"middle\" align=\"right\" rowspan=\"1\">" +
							"Similitud coseno(q,d) &nbsp; = &nbsp;"
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
				+ "Donde: <ul><li>V(q)· V(d) es el producto escalar de los pesos de los vectores.</li><li>|V(q)|&nbsp;|V(d)| son sus normas euclídeas.</li></ul>"
				+ "Para realizar una consulta introduciremos un texto en el cuadro reservado para ello y pulsaremos <b>ENTER</b> o en el botón <b>Buscar</b>. También podemos poner el número máximo de documentos "
				+ "que queremos recuperar en la lista desplegable de la derecha. Una vez realizada la consulta obtendremos los documentos mejor posicionados, la transformación de la consulta, el tiempo empleado "
				+ "en recuperar los documentos y el número total de documentos recuperados.<br><br>"
				+ "Si deseamos ver en qué lugar del documento se encuentran los términos que buscamos podemos pulsar sobre dicho documento en la tabla y automáticamente se cargará en el cuadro de la derecha. Además, "
				+ "podemos ver una explicación de cómo se ha llegado a conseguir ese peso para ese documento pulsando en el botón de <b>Detalles</b>."},
			
			// Ficha consulta booleana
			
			{"ayuda_cuadro_texto_contenido_8", "En la ficha de <b>consulta booleana</b> podemos hacer una consulta indicando qué terminos queremos encontrar en un documento o no. Para ello utilizaremos los operadores "
				+ "lógicos AND, OR, NOT y los paréntesis. Los resultados serán los documentos que cumplan esa condición y, por tanto, no hay ponderación.<br><br>"
				+ "Para realizar una consulta booleana debemos de introducir correctamente la consulta en el cuadro de texto reservado para ello y después pulsar <b>ENTER</b> o en el botón <b>buscar</b>. También podemos "
				+ "poner el máximo de documentos que queremos recuperar en la lista desplegable de la derecha. Una vez realizada la consulta obtendremos los documentos que la cumplen, la tranformación de la consulta "
				+ "estemizada, el tiempo empleado en recuperar los documentos, el número de documentos recuperados y una versión de la consulta leible para el usuario en la que se puede ver como Lucene utiliza "
				+ "los operadores de <b>+ (AND)</b>, <b>- (NOT)</b> y los <b>paréntesis</b>. Para el operador <b>OR</b> no pone <b>nada</b>."
				+ "Si deseamos ver en qué lugar del documento se encuentran los términos que buscamos podemos pulsar sobre dicho documento en la tabla y automáticamente se cargará en el cuadro de la derecha."},
			
			// Ficha consulta probabilística
			
			{"ayuda_cuadro_texto_contenido_9", "En la ficha de <b>consulta probabilística</b> podemos hacer una consulta utilizando el algoritmo de ranking <b>BM25</b>.<br><br>"
				+ "Este algoritmo calcula la probabilidad de que un documento sea relevante a una consulta dependiendo de los términos que hay en ellos. Se basa en el modelo de independencia binaria "
				+ "con el que establece los pesos de los términos en los documentos y en las consultas. Utiliza argumentos probabilísticos y validación experimental.<br><br>"
				+ "BM25 se ha comportado bien en pruebas de recuperación en TREC (<b>T</b>ext <b>RE</b>trieval <b>C</b>onference) y desde entonces ha influído en los algoritmos de clasificación de los motores "
				+ "de búsqueda comerciales, incluidos los motores de búsqueda web<br><br>"
				+ "Para realizar una consulta probabilística debemos de introducir el texto a buscar en el cuadro reservado para ello, al igual que la saturación y normalización, y despues pulsar <b>ENTER</b> "
				+ "o en el botón de <b>buscar</b>. También podemos poner el máximo de documentos que queremos recuperar en la lista desplegable de la derecha. Una vez realizada la consulta obtendremos los "
				+ "documentos mejor posicionados, la transformación de la consulta, el tiempos empleado en recuperar los documentos y el número total de documentos recuperados.<br><br>"
				+ "Si deseamos ver en qué lugar del documento se encuentran los términos que buscamos podemos pulsar sobre dicho documento en la tabla y automáticamente se cargará en el cuadro de la derecha. Además, "
				+ "podemos ver una explicación de como se ha llegado a conseguir ses peso para ese documento pulsando en el botón de <b>Detalles</b>."
				+ "*<ul><li>La <b>saturación</b> controla la normalización de la frecuencia del término</li><li>La <b>normalización</b> controla hasta qué punto la longitud del documento normaliza los valores de tf.</li></ul>"},
			
			// Bibliografía
			
			{"ayuda_cuadro_texto_contenido_10", "Bibliografía:<ol>"
					+ "<li>Recuperación de información: Un enfoque práctico y multidisciplinar. F. Cacheda Seijo, J. M. Fernández Luna, J. F. Huete Guadix. Editorial: RA-MA. ISBN: 978-84-9964-112-6</li>"
					+ "<li>Lucene in action. Otis Gospodnetic, Erik Hatcher. Editorial: MANNING. ISBN: 1-932394-28-1</li>"
					+ "<li>Search Engines: Information retrieval in practice. W. Bruce Croft, Donald Metzler, Trevor Strohman. Editorial: Pearson. ISBN: 0-13-136489-8</li>"
					+ "<li>Similitud coseno - <a href=\"#\">https://es.wikipedia.org/wiki/Similitud_coseno</a></li>"
					+ "<li>Apache Lucene - TFIDFSimilarity - <a href=\"#\">https://lucene.apache.org/core/6_3_0/core/org/apache/lucene/search/similarities/TFIDFSimilarity.html</a></li>"
					+ "<li>Apache Lucene - Index File Formats -  <a href=\"#\">https://lucene.apache.org/core/3_5_0/fileformats.html</a></li>"
					+ "<li>Okapi at TREC-3 - <a href=\"#\">http://trec.nist.gov/pubs/trec3/papers/city.ps.gz</a></li>"
				+ "</ol>"},
		
		
		// Ventana visor de documentos.
		
			{"etiqueta_borde_visorDocumentos_stopword", "Archivo de palabras vacías"},
			{"etiqueta_borde_visorDocumentos_archivos", "Documentos"},
			{"etiqueta_borde_visorDocumentos_descripcion", "Decripción"},
			{"etiqueta_ventana_titulo_visorDocumentos", "Visor de documentos originales"},
		
		// Elementos de menú
		
			{"label_menuitem_salir","Salir"},
			{"label_menuitem_inicio","Nueva sesión"},
			{"label_menuitem_ayuda","Ayuda"},
			{"label_menuitem_docOrig","Visor de documentos originales"},
			{"label_menuitem_acercaDe","Acerca de..."},
			{"label_menuitem_bArchivo","Archivo"}
		
	};
};