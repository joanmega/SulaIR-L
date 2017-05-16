package sulairl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.util.Pair;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.html.HTMLDocument;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author José Antonio Medina García
 */
public class Aplicacion extends javax.swing.JFrame {

    public static Coleccion col;
    private String textoOriginal;
    private String textoTokenizado;
    private String textoTokeSW;
    private String textoSW;
    private String textoNoStemming;
    private String textoStemming;
    private int docTokenizado;
    private int docStopWord;
    private final Map<Integer, ArchivosColeccion> archivosOriginales;
    private final Map<Integer, ArchivosColeccion> archivosTokenizados;
    private final Map<Integer, ArchivosColeccion> archivosStopWords;
    private final Map<Integer, ArchivosColeccion> archivosStemizado;
    private int tamanoSeleccionadoTok;
    private int tamanoSeleccionadoSW;
    private int tamanoSeleccionadoStemming;
    private int docStemming;
    private Index indice;
    private DefaultTableModel dtmTablaIndiceCompleto;
	private final DefaultTableModel dtmTablaBusquedaVectorial;
	private final DefaultTableModel dtmTablaBusquedaBooleana;
	private final DefaultTableModel dtmTablaBusquedaProb;
	private DefaultListModel dlmDocumentos;
	private DefaultListModel dlmTerminos;
	private DefaultListModel dlmListaArchivoOri;
	private DefaultListModel dlmListaArchivoStopWord;
	private DefaultListModel dlmListaContenidos;
	public int etapa;
	JFreeChart chart;

			
    public Aplicacion() throws IOException {
        initComponents();
        col = Configuracion.col;
        archivosOriginales = col.getArchivoOriginal();
        archivosTokenizados = col.getArchivoTokenizados();
        archivosStopWords = col.getArchivoStopWords();
        archivosStemizado = col.getArchivoStemizados();
        textoOriginal = "";
        textoTokenizado = "";
        textoTokeSW = "";
        textoSW = "";
        textoNoStemming = "";
        textoStemming = "";
        docTokenizado = 0;
        docStopWord = 0;
        tamanoSeleccionadoTok = 0;
        tamanoSeleccionadoSW = 0;
        tamanoSeleccionadoStemming = 0;
        dtmTablaIndiceCompleto = new DefaultTableModel();
		dtmTablaBusquedaVectorial = new DefaultTableModel();
		dtmTablaBusquedaBooleana = new DefaultTableModel();
		dtmTablaBusquedaProb = new DefaultTableModel();
		dlmDocumentos = new DefaultListModel();
		dlmTerminos = new DefaultListModel();
		dlmListaArchivoOri = new DefaultListModel();
		dlmListaArchivoStopWord = new DefaultListModel();
		dlmListaContenidos = new DefaultListModel();
		etapa = 0;
		indice = SulaIRL.indice;
		chart = null;
		cambiarIdioma();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jdCreacionIndice = new javax.swing.JDialog();
        jPanelIndice = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        bContinuar = new javax.swing.JButton();
        jProgreso = new javax.swing.JProgressBar();
        lEstadoIndice = new javax.swing.JLabel();
        jdDetalles = new javax.swing.JDialog();
        jScrollPane14 = new javax.swing.JScrollPane();
        tExplanation = new javax.swing.JTextPane();
        jdAyuda = new javax.swing.JDialog();
        pContenidos = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jListaContenidos = new javax.swing.JList<>();
        pDescripcionContenidos = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jPaneAyuda = new javax.swing.JTextPane();
        jdAcercaDe = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jdVisorDocumentos = new javax.swing.JDialog();
        pVisorDescripcion = new javax.swing.JPanel();
        jScrollPane24 = new javax.swing.JScrollPane();
        jPaneVisorDocumentos = new javax.swing.JTextPane();
        pVisorArchivosOriginales = new javax.swing.JPanel();
        jScrollPane25 = new javax.swing.JScrollPane();
        jListaArchivosOri = new javax.swing.JList<>();
        pVisorArchivoSW = new javax.swing.JPanel();
        jScrollPane26 = new javax.swing.JScrollPane();
        jListaStopWord = new javax.swing.JList<>();
        groupOrdenaGrafica = new javax.swing.ButtonGroup();
        jDialogGuardaGrafica = new javax.swing.JDialog();
        jFileChooserGuardaGrafica = new javax.swing.JFileChooser();
        jOpciones = new javax.swing.JTabbedPane();
        tPreprocesado = new javax.swing.JPanel();
        tTokenizacion = new javax.swing.JTabbedPane();
        pTokenizacion = new javax.swing.JPanel();
        pOriginal = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tOriginal = new javax.swing.JTextPane();
        pTokenizado = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tTokenizado = new javax.swing.JTextPane();
        bInicioTokenizacion = new javax.swing.JButton();
        bSiguienteTokenizacion = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tNomArchivo = new javax.swing.JTextField();
        bSiguienteDocTokenizacion = new javax.swing.JButton();
        bSiguientePasoTokenizacion = new javax.swing.JButton();
        bTerminarDocTokenizado = new javax.swing.JButton();
        pLeyendaTokenizacion = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lSeModificaTok = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        lSinCambioTok = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        lSeEliminaTok = new javax.swing.JLabel();
        bAyudaTokenizacion = new javax.swing.JButton();
        pStopWords = new javax.swing.JPanel();
        pConStopWords = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tTokenizadoSW = new javax.swing.JTextPane();
        pSinStopWords = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tStopWords = new javax.swing.JTextPane();
        bInicioStopWords = new javax.swing.JButton();
        bSiguienteStopWord = new javax.swing.JButton();
        tNomArchivoStopWord = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        bTerminarDocStopWord = new javax.swing.JButton();
        bSiguienteDocStopWord = new javax.swing.JButton();
        bSiguientePasoStopWord = new javax.swing.JButton();
        pLeyendaStopWords = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        bSeEliminaSW = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        bSinCambiosSW = new javax.swing.JLabel();
        bAyudaStopWords = new javax.swing.JButton();
        pLematizacion = new javax.swing.JPanel();
        pConLemas = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tNoStemizado = new javax.swing.JTextPane();
        pSinLemas = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tStemizado = new javax.swing.JTextPane();
        bInicioStemming = new javax.swing.JButton();
        bSiguienteStemming = new javax.swing.JButton();
        bTerminarDocStemming = new javax.swing.JButton();
        bSiguienteDocStemming = new javax.swing.JButton();
        bCrearIndice = new javax.swing.JButton();
        tNomArchivoNoStemming = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pLeyendaStemming = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lSeLematizaSt = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lSinCambiosSt = new javax.swing.JLabel();
        bAyudaStemming = new javax.swing.JButton();
        tIndexacion = new javax.swing.JPanel();
        tIndice = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        pIndiceCompleto = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTablaIndiceCompleto = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        pInformacionTermino = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTablaPosiciones =  new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        pInformacionIndice = new javax.swing.JPanel();
        lRutaIndice = new javax.swing.JLabel();
        lNumDocIndice = new javax.swing.JLabel();
        lNumTerminosIndice = new javax.swing.JLabel();
        lNumDocumentos = new javax.swing.JLabel();
        lDirectorioIndice = new javax.swing.JLabel();
        lNumTerminos = new javax.swing.JLabel();
        lVersionIndice1 = new javax.swing.JLabel();
        lVersionIndice = new javax.swing.JLabel();
        pInformacionTerminoDocumento = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tTextoIndice = new javax.swing.JTextPane();
        jLabel15 = new javax.swing.JLabel();
        lArchivoStemmingIndice = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lTfxIdf = new javax.swing.JLabel();
        bBusqueda = new javax.swing.JButton();
        bAyudaIndexacion = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        pGraficasTerminos = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jListaTerminos = new javax.swing.JList<>();
        jPanel12 = new javax.swing.JPanel();
        jRadioAsc = new javax.swing.JRadioButton();
        jRadioDesc = new javax.swing.JRadioButton();
        pGraficasDocumentos = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jListaDocumentos = new javax.swing.JList<>();
        pGraficas = new javax.swing.JPanel();
        jScrollPaneGrafica = new javax.swing.JScrollPane();
        jPanelGrafica = new javax.swing.JPanel();
        bGrafica = new javax.swing.JButton();
        bAyudaGraficas = new javax.swing.JButton();
        tConsultaVec = new javax.swing.JPanel();
        pBusquedaVectorial = new javax.swing.JPanel();
        lBusquedaVectorial = new javax.swing.JLabel();
        tBusquedaVectorial = new javax.swing.JTextField();
        bBusquedaVectorial = new javax.swing.JButton();
        jcNumDocVectorial = new javax.swing.JComboBox<>();
        lNumDocVectorial = new javax.swing.JLabel();
        lTiempoVectorial = new javax.swing.JLabel();
        lConsResVectorial = new javax.swing.JLabel();
        tConsResVectorial = new javax.swing.JTextField();
        lDocRecVectorial = new javax.swing.JLabel();
        lRecVectorial = new javax.swing.JLabel();
        lTimeVec = new javax.swing.JLabel();
        pVisualizacionVectorial = new javax.swing.JPanel();
        pTablaVectorial = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTablaDocVectorial = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        bExplanationVec = new javax.swing.JButton();
        pDocVectorial = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tPanelDocVectorial = new javax.swing.JTextPane();
        lNombreVectorial = new javax.swing.JLabel();
        tDocVectorial = new javax.swing.JTextField();
        bAyudaVectorial = new javax.swing.JButton();
        tConsultaBool = new javax.swing.JPanel();
        pBusquedaBooleana = new javax.swing.JPanel();
        lBusquedaBooleana = new javax.swing.JLabel();
        tBusquedaBooleana = new javax.swing.JTextField();
        bBusquedaBooleana = new javax.swing.JButton();
        jcNumDocBooleana = new javax.swing.JComboBox<>();
        lNumDocBooleana = new javax.swing.JLabel();
        lTiempoBooleana = new javax.swing.JLabel();
        lConsResBooleana = new javax.swing.JLabel();
        tConsResBooleana = new javax.swing.JTextField();
        lDocRecBooleana = new javax.swing.JLabel();
        lRecBooleana = new javax.swing.JLabel();
        lTimeBool = new javax.swing.JLabel();
        lConsBooleanaLucene = new javax.swing.JLabel();
        tConsBoolLucene = new javax.swing.JTextField();
        pVisualizacionBooleana = new javax.swing.JPanel();
        pTablaBooleana = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTablaDocBooleana = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        pDocBooleana = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        tPanelDocBooleana = new javax.swing.JTextPane();
        lNombreBooleana = new javax.swing.JLabel();
        tDocBooleana = new javax.swing.JTextField();
        bAyudaBooleana = new javax.swing.JButton();
        tConsultaProb = new javax.swing.JPanel();
        pBusquedaProb = new javax.swing.JPanel();
        lBusquedaProb = new javax.swing.JLabel();
        tBusquedaProb = new javax.swing.JTextField();
        bBusquedaProb = new javax.swing.JButton();
        jcNumDocProb = new javax.swing.JComboBox<>();
        lNumDocProb = new javax.swing.JLabel();
        lTiempoProb = new javax.swing.JLabel();
        lConsResProb = new javax.swing.JLabel();
        tConsResProb = new javax.swing.JTextField();
        lDocRecProb = new javax.swing.JLabel();
        lRecProb = new javax.swing.JLabel();
        lTimeProb = new javax.swing.JLabel();
        lSaturacion = new javax.swing.JLabel();
        tNormalizacion = new javax.swing.JTextField();
        tSaturacion = new javax.swing.JTextField();
        lNormalizacion = new javax.swing.JLabel();
        pVisualizacionProb = new javax.swing.JPanel();
        pTablaProb = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTablaDocProb = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        bExplanationProb = new javax.swing.JButton();
        pDocProb = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        tPanelDocProb = new javax.swing.JTextPane();
        lNombreProb = new javax.swing.JLabel();
        tDocProb = new javax.swing.JTextField();
        bAyudaProbabilística = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        bArchivo = new javax.swing.JMenu();
        bInicio = new javax.swing.JMenuItem();
        bSalir = new javax.swing.JMenuItem();
        bSulairl = new javax.swing.JMenu();
        bDocOrig = new javax.swing.JMenuItem();
        bAyuda = new javax.swing.JMenuItem();
        bAcercaDe = new javax.swing.JMenuItem();

        jdCreacionIndice.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jdCreacionIndice.setTitle("Procesamiento de documentos");
        jdCreacionIndice.setBackground(new java.awt.Color(204, 204, 204));
        jdCreacionIndice.setMinimumSize(new java.awt.Dimension(500, 217));
        jdCreacionIndice.setModal(true);
        jdCreacionIndice.setResizable(false);
        jdCreacionIndice.setSize(new java.awt.Dimension(350, 260));
        jdCreacionIndice.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                jdCreacionIndiceWindowOpened(evt);
            }
        });

        jPanelIndice.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel19.setText("En este proceso se realiza la creación del índice invertido de la");

        jLabel20.setText("colección de documentos.");

        jLabel21.setText("Este proceso puede ser lento debido a la cantidad de");

        jLabel22.setText("documentos que haya en la colección");

        javax.swing.GroupLayout jPanelIndiceLayout = new javax.swing.GroupLayout(jPanelIndice);
        jPanelIndice.setLayout(jPanelIndiceLayout);
        jPanelIndiceLayout.setHorizontalGroup(
            jPanelIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelIndiceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelIndiceLayout.setVerticalGroup(
            jPanelIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelIndiceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bContinuar.setText("Continuar");
        bContinuar.setEnabled(false);
        bContinuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bContinuarActionPerformed(evt);
            }
        });

        jProgreso.setStringPainted(true);

        javax.swing.GroupLayout jdCreacionIndiceLayout = new javax.swing.GroupLayout(jdCreacionIndice.getContentPane());
        jdCreacionIndice.getContentPane().setLayout(jdCreacionIndiceLayout);
        jdCreacionIndiceLayout.setHorizontalGroup(
            jdCreacionIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdCreacionIndiceLayout.createSequentialGroup()
                .addGap(196, 196, 196)
                .addComponent(bContinuar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jdCreacionIndiceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdCreacionIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgreso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lEstadoIndice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelIndice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jdCreacionIndiceLayout.setVerticalGroup(
            jdCreacionIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdCreacionIndiceLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jPanelIndice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(lEstadoIndice, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bContinuar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jdDetalles.setMinimumSize(new java.awt.Dimension(600, 500));
        jdDetalles.setModal(true);

        tExplanation.setEditable(false);
        tExplanation.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        tExplanation.setDoubleBuffered(true);
        jScrollPane14.setViewportView(tExplanation);

        javax.swing.GroupLayout jdDetallesLayout = new javax.swing.GroupLayout(jdDetalles.getContentPane());
        jdDetalles.getContentPane().setLayout(jdDetallesLayout);
        jdDetallesLayout.setHorizontalGroup(
            jdDetallesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdDetallesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addContainerGap())
        );
        jdDetallesLayout.setVerticalGroup(
            jdDetallesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdDetallesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jdAyuda.setMinimumSize(new java.awt.Dimension(758, 300));
        jdAyuda.setModal(true);
        jdAyuda.setResizable(false);

        pContenidos.setBorder(javax.swing.BorderFactory.createTitledBorder("Contenidos"));

        jListaContenidos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Sistema de recuperación de información", "Proceso de tokenización", "Proceso de eliminación de palabras vacías", "Proceso de stemming", "Proceso de indexación", "Índice completo", "Gráficas", "Consulta Vectorial", "Consulta Booleana", "Consulta Probabilística", "Bibliografía utilizada" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jListaContenidos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListaContenidos.setToolTipText("");
        jListaContenidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListaContenidosMouseClicked(evt);
            }
        });
        jScrollPane19.setViewportView(jListaContenidos);

        javax.swing.GroupLayout pContenidosLayout = new javax.swing.GroupLayout(pContenidos);
        pContenidos.setLayout(pContenidosLayout);
        pContenidosLayout.setHorizontalGroup(
            pContenidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pContenidosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addContainerGap())
        );
        pContenidosLayout.setVerticalGroup(
            pContenidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pContenidosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane19)
                .addContainerGap())
        );

        pDescripcionContenidos.setBorder(javax.swing.BorderFactory.createTitledBorder("Descripción"));

        jPaneAyuda.setEditable(false);
        jPaneAyuda.setContentType("text/html"); // NOI18N
        jScrollPane20.setViewportView(jPaneAyuda);

        javax.swing.GroupLayout pDescripcionContenidosLayout = new javax.swing.GroupLayout(pDescripcionContenidos);
        pDescripcionContenidos.setLayout(pDescripcionContenidosLayout);
        pDescripcionContenidosLayout.setHorizontalGroup(
            pDescripcionContenidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDescripcionContenidosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pDescripcionContenidosLayout.setVerticalGroup(
            pDescripcionContenidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDescripcionContenidosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jdAyudaLayout = new javax.swing.GroupLayout(jdAyuda.getContentPane());
        jdAyuda.getContentPane().setLayout(jdAyudaLayout);
        jdAyudaLayout.setHorizontalGroup(
            jdAyudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdAyudaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pContenidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pDescripcionContenidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jdAyudaLayout.setVerticalGroup(
            jdAyudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdAyudaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdAyudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pDescripcionContenidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pContenidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jdAcercaDe.setTitle("Acerca de... / About...");
        jdAcercaDe.setMinimumSize(new java.awt.Dimension(400, 270));
        jdAcercaDe.setModal(true);
        jdAcercaDe.setResizable(false);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Trabajo Fin de Grado /  Final Project"));

        jLabel4.setText("Autor: José Antonio Medina García");

        jLabel5.setText("Tutor: Juan Manuel Fernández Luna");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/ETSIIT1.png"))); // NOI18N

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/UGR1.jpg"))); // NOI18N

        jButton1.setText("Web");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jButton1))
                .addGap(91, 91, 91))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(12, 12, 12)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jdAcercaDeLayout = new javax.swing.GroupLayout(jdAcercaDe.getContentPane());
        jdAcercaDe.getContentPane().setLayout(jdAcercaDeLayout);
        jdAcercaDeLayout.setHorizontalGroup(
            jdAcercaDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdAcercaDeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jdAcercaDeLayout.setVerticalGroup(
            jdAcercaDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdAcercaDeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jdVisorDocumentos.setMinimumSize(new java.awt.Dimension(1043, 670));
        jdVisorDocumentos.setModal(true);
        jdVisorDocumentos.setResizable(false);
        jdVisorDocumentos.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                jdVisorDocumentosWindowOpened(evt);
            }
        });

        pVisorDescripcion.setBorder(javax.swing.BorderFactory.createTitledBorder("Descripción"));

        jPaneVisorDocumentos.setEditable(false);
        jPaneVisorDocumentos.setContentType("text/html"); // NOI18N
        jScrollPane24.setViewportView(jPaneVisorDocumentos);

        javax.swing.GroupLayout pVisorDescripcionLayout = new javax.swing.GroupLayout(pVisorDescripcion);
        pVisorDescripcion.setLayout(pVisorDescripcionLayout);
        pVisorDescripcionLayout.setHorizontalGroup(
            pVisorDescripcionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisorDescripcionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pVisorDescripcionLayout.setVerticalGroup(
            pVisorDescripcionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisorDescripcionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane24)
                .addContainerGap())
        );

        pVisorArchivosOriginales.setBorder(javax.swing.BorderFactory.createTitledBorder("Archivos originales"));

        jListaArchivosOri.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListaArchivosOri.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jListaArchivosOriFocusGained(evt);
            }
        });
        jListaArchivosOri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListaArchivosOriMouseClicked(evt);
            }
        });
        jScrollPane25.setViewportView(jListaArchivosOri);

        javax.swing.GroupLayout pVisorArchivosOriginalesLayout = new javax.swing.GroupLayout(pVisorArchivosOriginales);
        pVisorArchivosOriginales.setLayout(pVisorArchivosOriginalesLayout);
        pVisorArchivosOriginalesLayout.setHorizontalGroup(
            pVisorArchivosOriginalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisorArchivosOriginalesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane25, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addContainerGap())
        );
        pVisorArchivosOriginalesLayout.setVerticalGroup(
            pVisorArchivosOriginalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisorArchivosOriginalesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane25, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addContainerGap())
        );

        pVisorArchivoSW.setBorder(javax.swing.BorderFactory.createTitledBorder("Archivo Stop Word"));

        jListaStopWord.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListaStopWord.setVisibleRowCount(1);
        jListaStopWord.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jListaStopWordFocusGained(evt);
            }
        });
        jListaStopWord.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListaStopWordMouseClicked(evt);
            }
        });
        jScrollPane26.setViewportView(jListaStopWord);

        javax.swing.GroupLayout pVisorArchivoSWLayout = new javax.swing.GroupLayout(pVisorArchivoSW);
        pVisorArchivoSW.setLayout(pVisorArchivoSWLayout);
        pVisorArchivoSWLayout.setHorizontalGroup(
            pVisorArchivoSWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisorArchivoSWLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        pVisorArchivoSWLayout.setVerticalGroup(
            pVisorArchivoSWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisorArchivoSWLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane26, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jdVisorDocumentosLayout = new javax.swing.GroupLayout(jdVisorDocumentos.getContentPane());
        jdVisorDocumentos.getContentPane().setLayout(jdVisorDocumentosLayout);
        jdVisorDocumentosLayout.setHorizontalGroup(
            jdVisorDocumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdVisorDocumentosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdVisorDocumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pVisorArchivosOriginales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pVisorArchivoSW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pVisorDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jdVisorDocumentosLayout.setVerticalGroup(
            jdVisorDocumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdVisorDocumentosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdVisorDocumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pVisorDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jdVisorDocumentosLayout.createSequentialGroup()
                        .addComponent(pVisorArchivosOriginales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pVisorArchivoSW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jFileChooserGuardaGrafica.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        jFileChooserGuardaGrafica.setFileHidingEnabled(false);

        javax.swing.GroupLayout jDialogGuardaGraficaLayout = new javax.swing.GroupLayout(jDialogGuardaGrafica.getContentPane());
        jDialogGuardaGrafica.getContentPane().setLayout(jDialogGuardaGraficaLayout);
        jDialogGuardaGraficaLayout.setHorizontalGroup(
            jDialogGuardaGraficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jFileChooserGuardaGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jDialogGuardaGraficaLayout.setVerticalGroup(
            jDialogGuardaGraficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jFileChooserGuardaGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SulaIR - L");
        setMaximumSize(new java.awt.Dimension(1280, 750));
        setMinimumSize(new java.awt.Dimension(1280, 750));
        setPreferredSize(new java.awt.Dimension(1280, 750));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jOpciones.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jOpciones.setMaximumSize(new java.awt.Dimension(634, 124));
        jOpciones.setMinimumSize(new java.awt.Dimension(634, 124));
        jOpciones.setPreferredSize(new java.awt.Dimension(634, 124));

        tTokenizacion.setMaximumSize(new java.awt.Dimension(900, 666));
        tTokenizacion.setMinimumSize(new java.awt.Dimension(900, 666));
        tTokenizacion.setPreferredSize(new java.awt.Dimension(900, 666));

        pTokenizacion.setMaximumSize(new java.awt.Dimension(100000, 666666));
        pTokenizacion.setMinimumSize(new java.awt.Dimension(1000, 666));
        pTokenizacion.setPreferredSize(new java.awt.Dimension(1000, 666));

        pOriginal.setBorder(javax.swing.BorderFactory.createTitledBorder("Archivo original"));
        pOriginal.setMaximumSize(new java.awt.Dimension(737, 249));
        pOriginal.setMinimumSize(new java.awt.Dimension(737, 249));
        pOriginal.setPreferredSize(new java.awt.Dimension(737, 249));

        tOriginal.setEditable(false);
        tOriginal.setAutoscrolls(false);
        tOriginal.setFocusable(false);
        tOriginal.setMaximumSize(new java.awt.Dimension(700, 200));
        tOriginal.setMinimumSize(new java.awt.Dimension(700, 200));
        tOriginal.setPreferredSize(new java.awt.Dimension(700, 200));
        tOriginal.setSelectionColor(new java.awt.Color(255, 255, 255));
        jScrollPane7.setViewportView(tOriginal);

        javax.swing.GroupLayout pOriginalLayout = new javax.swing.GroupLayout(pOriginal);
        pOriginal.setLayout(pOriginalLayout);
        pOriginalLayout.setHorizontalGroup(
            pOriginalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pOriginalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                .addContainerGap())
        );
        pOriginalLayout.setVerticalGroup(
            pOriginalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pOriginalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addContainerGap())
        );

        pTokenizado.setBorder(javax.swing.BorderFactory.createTitledBorder("Archivo tokenizado"));
        pTokenizado.setMaximumSize(new java.awt.Dimension(737, 249));
        pTokenizado.setMinimumSize(new java.awt.Dimension(737, 249));
        pTokenizado.setPreferredSize(new java.awt.Dimension(737, 249));

        tTokenizado.setEditable(false);
        tTokenizado.setAutoscrolls(false);
        tTokenizado.setFocusable(false);
        tTokenizado.setMaximumSize(new java.awt.Dimension(700, 200));
        tTokenizado.setMinimumSize(new java.awt.Dimension(700, 200));
        tTokenizado.setPreferredSize(new java.awt.Dimension(700, 200));
        tTokenizado.setSelectionColor(new java.awt.Color(255, 255, 255));
        jScrollPane8.setViewportView(tTokenizado);

        javax.swing.GroupLayout pTokenizadoLayout = new javax.swing.GroupLayout(pTokenizado);
        pTokenizado.setLayout(pTokenizadoLayout);
        pTokenizadoLayout.setHorizontalGroup(
            pTokenizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTokenizadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
                .addContainerGap())
        );
        pTokenizadoLayout.setVerticalGroup(
            pTokenizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTokenizadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addContainerGap())
        );

        bInicioTokenizacion.setText("Iniciar Tokenización");
        bInicioTokenizacion.setMaximumSize(new java.awt.Dimension(171, 23));
        bInicioTokenizacion.setMinimumSize(new java.awt.Dimension(171, 23));
        bInicioTokenizacion.setPreferredSize(new java.awt.Dimension(171, 23));
        bInicioTokenizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInicioTokenizacionActionPerformed(evt);
            }
        });

        bSiguienteTokenizacion.setText("Siguiente Término");
        bSiguienteTokenizacion.setEnabled(false);
        bSiguienteTokenizacion.setMaximumSize(new java.awt.Dimension(150, 23));
        bSiguienteTokenizacion.setMinimumSize(new java.awt.Dimension(150, 23));
        bSiguienteTokenizacion.setPreferredSize(new java.awt.Dimension(150, 23));
        bSiguienteTokenizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSiguienteTokenizacionActionPerformed(evt);
            }
        });

        jLabel1.setText("Archivo: ");

        tNomArchivo.setEditable(false);

        bSiguienteDocTokenizacion.setText("Siguiente Documento");
        bSiguienteDocTokenizacion.setEnabled(false);
        bSiguienteDocTokenizacion.setMaximumSize(new java.awt.Dimension(170, 23));
        bSiguienteDocTokenizacion.setMinimumSize(new java.awt.Dimension(170, 23));
        bSiguienteDocTokenizacion.setPreferredSize(new java.awt.Dimension(170, 23));
        bSiguienteDocTokenizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSiguienteDocTokenizacionActionPerformed(evt);
            }
        });

        bSiguientePasoTokenizacion.setText("Siguiente Paso");
        bSiguientePasoTokenizacion.setMaximumSize(new java.awt.Dimension(140, 23));
        bSiguientePasoTokenizacion.setMinimumSize(new java.awt.Dimension(140, 23));
        bSiguientePasoTokenizacion.setPreferredSize(new java.awt.Dimension(140, 23));
        bSiguientePasoTokenizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSiguientePasoTokenizacionActionPerformed(evt);
            }
        });

        bTerminarDocTokenizado.setText("Terminar Documento");
        bTerminarDocTokenizado.setEnabled(false);
        bTerminarDocTokenizado.setMaximumSize(new java.awt.Dimension(165, 23));
        bTerminarDocTokenizado.setMinimumSize(new java.awt.Dimension(165, 23));
        bTerminarDocTokenizado.setPreferredSize(new java.awt.Dimension(165, 23));
        bTerminarDocTokenizado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTerminarDocTokenizadoActionPerformed(evt);
            }
        });

        pLeyendaTokenizacion.setBorder(javax.swing.BorderFactory.createTitledBorder("Leyenda"));
        pLeyendaTokenizacion.setPreferredSize(new java.awt.Dimension(150, 127));

        jPanel8.setBackground(new java.awt.Color(0, 0, 153));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        lSeModificaTok.setText("Se modifica");

        jPanel9.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        lSinCambioTok.setText("Sin cambios");

        jPanel10.setBackground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        lSeEliminaTok.setText("Se elimina");

        javax.swing.GroupLayout pLeyendaTokenizacionLayout = new javax.swing.GroupLayout(pLeyendaTokenizacion);
        pLeyendaTokenizacion.setLayout(pLeyendaTokenizacionLayout);
        pLeyendaTokenizacionLayout.setHorizontalGroup(
            pLeyendaTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLeyendaTokenizacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pLeyendaTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pLeyendaTokenizacionLayout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lSeModificaTok))
                    .addGroup(pLeyendaTokenizacionLayout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lSinCambioTok))
                    .addGroup(pLeyendaTokenizacionLayout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lSeEliminaTok)))
                .addContainerGap())
        );
        pLeyendaTokenizacionLayout.setVerticalGroup(
            pLeyendaTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLeyendaTokenizacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pLeyendaTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lSinCambioTok)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pLeyendaTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lSeModificaTok)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(pLeyendaTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lSeEliminaTok)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        bAyudaTokenizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/help_icono.png"))); // NOI18N
        bAyudaTokenizacion.setToolTipText("Ayuda");
        bAyudaTokenizacion.setMaximumSize(new java.awt.Dimension(25, 25));
        bAyudaTokenizacion.setMinimumSize(new java.awt.Dimension(25, 25));
        bAyudaTokenizacion.setPreferredSize(new java.awt.Dimension(25, 25));
        bAyudaTokenizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAyudaTokenizacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pTokenizacionLayout = new javax.swing.GroupLayout(pTokenizacion);
        pTokenizacion.setLayout(pTokenizacionLayout);
        pTokenizacionLayout.setHorizontalGroup(
            pTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTokenizacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pTokenizacionLayout.createSequentialGroup()
                        .addComponent(bInicioTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSiguienteTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bTerminarDocTokenizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSiguienteDocTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSiguientePasoTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pTokenizacionLayout.createSequentialGroup()
                        .addGroup(pTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pTokenizado, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pTokenizacionLayout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tNomArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(bAyudaTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(pOriginal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pLeyendaTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(189, Short.MAX_VALUE))
        );
        pTokenizacionLayout.setVerticalGroup(
            pTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pTokenizacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tNomArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bAyudaTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pOriginal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pTokenizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pLeyendaTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pTokenizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bInicioTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSiguienteTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTerminarDocTokenizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSiguienteDocTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSiguientePasoTokenizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(232, 232, 232))
        );

        tTokenizacion.addTab("Tokenización", pTokenizacion);

        pConStopWords.setBorder(javax.swing.BorderFactory.createTitledBorder("Archivo tokenizado"));
        pConStopWords.setMaximumSize(new java.awt.Dimension(737, 249));
        pConStopWords.setMinimumSize(new java.awt.Dimension(737, 249));
        pConStopWords.setPreferredSize(new java.awt.Dimension(737, 249));

        tTokenizadoSW.setEditable(false);
        tTokenizadoSW.setAutoscrolls(false);
        tTokenizadoSW.setFocusable(false);
        tTokenizadoSW.setMaximumSize(new java.awt.Dimension(700, 200));
        tTokenizadoSW.setMinimumSize(new java.awt.Dimension(700, 200));
        tTokenizadoSW.setPreferredSize(new java.awt.Dimension(700, 200));
        tTokenizadoSW.setSelectionColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(tTokenizadoSW);

        javax.swing.GroupLayout pConStopWordsLayout = new javax.swing.GroupLayout(pConStopWords);
        pConStopWords.setLayout(pConStopWordsLayout);
        pConStopWordsLayout.setHorizontalGroup(
            pConStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConStopWordsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                .addContainerGap())
        );
        pConStopWordsLayout.setVerticalGroup(
            pConStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConStopWordsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                .addContainerGap())
        );

        pSinStopWords.setBorder(javax.swing.BorderFactory.createTitledBorder("Archivo sin palabras vacías"));
        pSinStopWords.setMaximumSize(new java.awt.Dimension(737, 249));
        pSinStopWords.setMinimumSize(new java.awt.Dimension(737, 249));
        pSinStopWords.setPreferredSize(new java.awt.Dimension(737, 249));

        tStopWords.setEditable(false);
        tStopWords.setAutoscrolls(false);
        tStopWords.setFocusable(false);
        tStopWords.setMaximumSize(new java.awt.Dimension(700, 200));
        tStopWords.setMinimumSize(new java.awt.Dimension(700, 200));
        tStopWords.setPreferredSize(new java.awt.Dimension(700, 200));
        tStopWords.setSelectionColor(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(tStopWords);

        javax.swing.GroupLayout pSinStopWordsLayout = new javax.swing.GroupLayout(pSinStopWords);
        pSinStopWords.setLayout(pSinStopWordsLayout);
        pSinStopWordsLayout.setHorizontalGroup(
            pSinStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSinStopWordsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        pSinStopWordsLayout.setVerticalGroup(
            pSinStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSinStopWordsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addContainerGap())
        );

        bInicioStopWords.setText("Iniciar Palabras Vacías");
        bInicioStopWords.setMaximumSize(new java.awt.Dimension(171, 23));
        bInicioStopWords.setMinimumSize(new java.awt.Dimension(171, 23));
        bInicioStopWords.setPreferredSize(new java.awt.Dimension(171, 23));
        bInicioStopWords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInicioStopWordsActionPerformed(evt);
            }
        });

        bSiguienteStopWord.setText("Siguiente Término");
        bSiguienteStopWord.setEnabled(false);
        bSiguienteStopWord.setMaximumSize(new java.awt.Dimension(150, 23));
        bSiguienteStopWord.setMinimumSize(new java.awt.Dimension(150, 23));
        bSiguienteStopWord.setPreferredSize(new java.awt.Dimension(150, 23));
        bSiguienteStopWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSiguienteStopWordActionPerformed(evt);
            }
        });

        tNomArchivoStopWord.setEditable(false);

        jLabel2.setText("Archivo: ");

        bTerminarDocStopWord.setText("Terminar Documento");
        bTerminarDocStopWord.setEnabled(false);
        bTerminarDocStopWord.setMaximumSize(new java.awt.Dimension(165, 23));
        bTerminarDocStopWord.setMinimumSize(new java.awt.Dimension(165, 23));
        bTerminarDocStopWord.setPreferredSize(new java.awt.Dimension(165, 23));
        bTerminarDocStopWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTerminarDocStopWordActionPerformed(evt);
            }
        });

        bSiguienteDocStopWord.setText("Siguiente Documento");
        bSiguienteDocStopWord.setEnabled(false);
        bSiguienteDocStopWord.setMaximumSize(new java.awt.Dimension(170, 23));
        bSiguienteDocStopWord.setMinimumSize(new java.awt.Dimension(170, 23));
        bSiguienteDocStopWord.setPreferredSize(new java.awt.Dimension(170, 23));
        bSiguienteDocStopWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSiguienteDocStopWordActionPerformed(evt);
            }
        });

        bSiguientePasoStopWord.setText("Siguiente Paso");
        bSiguientePasoStopWord.setMaximumSize(new java.awt.Dimension(140, 23));
        bSiguientePasoStopWord.setMinimumSize(new java.awt.Dimension(140, 23));
        bSiguientePasoStopWord.setPreferredSize(new java.awt.Dimension(140, 23));
        bSiguientePasoStopWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSiguientePasoStopWordActionPerformed(evt);
            }
        });

        pLeyendaStopWords.setBorder(javax.swing.BorderFactory.createTitledBorder("Leyenda"));
        pLeyendaStopWords.setPreferredSize(new java.awt.Dimension(150, 127));

        jPanel5.setBackground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        bSeEliminaSW.setText("Se elimina");

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        bSinCambiosSW.setText("Sin cambios");

        javax.swing.GroupLayout pLeyendaStopWordsLayout = new javax.swing.GroupLayout(pLeyendaStopWords);
        pLeyendaStopWords.setLayout(pLeyendaStopWordsLayout);
        pLeyendaStopWordsLayout.setHorizontalGroup(
            pLeyendaStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLeyendaStopWordsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pLeyendaStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pLeyendaStopWordsLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSeEliminaSW))
                    .addGroup(pLeyendaStopWordsLayout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSinCambiosSW)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        pLeyendaStopWordsLayout.setVerticalGroup(
            pLeyendaStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLeyendaStopWordsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pLeyendaStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bSinCambiosSW)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pLeyendaStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bSeEliminaSW)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        bAyudaStopWords.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/help_icono.png"))); // NOI18N
        bAyudaStopWords.setToolTipText("Ayuda");
        bAyudaStopWords.setMaximumSize(new java.awt.Dimension(25, 25));
        bAyudaStopWords.setMinimumSize(new java.awt.Dimension(25, 25));
        bAyudaStopWords.setPreferredSize(new java.awt.Dimension(25, 25));
        bAyudaStopWords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAyudaStopWordsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pStopWordsLayout = new javax.swing.GroupLayout(pStopWords);
        pStopWords.setLayout(pStopWordsLayout);
        pStopWordsLayout.setHorizontalGroup(
            pStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pStopWordsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pStopWordsLayout.createSequentialGroup()
                        .addComponent(bInicioStopWords, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSiguienteStopWord, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bTerminarDocStopWord, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSiguienteDocStopWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSiguientePasoStopWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pStopWordsLayout.createSequentialGroup()
                        .addGroup(pStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pStopWordsLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tNomArchivoStopWord, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 445, Short.MAX_VALUE)
                                .addComponent(bAyudaStopWords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pConStopWords, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pSinStopWords, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pLeyendaStopWords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(192, Short.MAX_VALUE))
        );
        pStopWordsLayout.setVerticalGroup(
            pStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pStopWordsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(tNomArchivoStopWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bAyudaStopWords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pConStopWords, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pSinStopWords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pLeyendaStopWords, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pStopWordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bInicioStopWords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSiguienteStopWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTerminarDocStopWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSiguienteDocStopWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSiguientePasoStopWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tTokenizacion.addTab("Palabras Vacías", null, pStopWords, "");

        pConLemas.setBorder(javax.swing.BorderFactory.createTitledBorder("Archivo sin lematización"));
        pConLemas.setMaximumSize(new java.awt.Dimension(737, 249));
        pConLemas.setMinimumSize(new java.awt.Dimension(737, 249));
        pConLemas.setPreferredSize(new java.awt.Dimension(737, 249));

        tNoStemizado.setEditable(false);
        tNoStemizado.setAutoscrolls(false);
        tNoStemizado.setFocusable(false);
        tNoStemizado.setMaximumSize(new java.awt.Dimension(700, 200));
        tNoStemizado.setMinimumSize(new java.awt.Dimension(700, 200));
        tNoStemizado.setPreferredSize(new java.awt.Dimension(700, 200));
        tNoStemizado.setSelectionColor(new java.awt.Color(255, 255, 255));
        jScrollPane3.setViewportView(tNoStemizado);

        javax.swing.GroupLayout pConLemasLayout = new javax.swing.GroupLayout(pConLemas);
        pConLemas.setLayout(pConLemasLayout);
        pConLemasLayout.setHorizontalGroup(
            pConLemasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConLemasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                .addContainerGap())
        );
        pConLemasLayout.setVerticalGroup(
            pConLemasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConLemasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addContainerGap())
        );

        pSinLemas.setBorder(javax.swing.BorderFactory.createTitledBorder("Archivo lematizado"));
        pSinLemas.setMaximumSize(new java.awt.Dimension(737, 249));
        pSinLemas.setMinimumSize(new java.awt.Dimension(737, 249));
        pSinLemas.setPreferredSize(new java.awt.Dimension(737, 249));
        pSinLemas.setRequestFocusEnabled(false);

        tStemizado.setEditable(false);
        tStemizado.setAutoscrolls(false);
        tStemizado.setFocusable(false);
        tStemizado.setMaximumSize(new java.awt.Dimension(700, 200));
        tStemizado.setMinimumSize(new java.awt.Dimension(700, 200));
        tStemizado.setPreferredSize(new java.awt.Dimension(700, 200));
        tStemizado.setSelectionColor(new java.awt.Color(255, 255, 255));
        jScrollPane4.setViewportView(tStemizado);

        javax.swing.GroupLayout pSinLemasLayout = new javax.swing.GroupLayout(pSinLemas);
        pSinLemas.setLayout(pSinLemasLayout);
        pSinLemasLayout.setHorizontalGroup(
            pSinLemasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSinLemasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                .addContainerGap())
        );
        pSinLemasLayout.setVerticalGroup(
            pSinLemasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSinLemasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addContainerGap())
        );

        bInicioStemming.setText("Iniciar Stemming");
        bInicioStemming.setMaximumSize(new java.awt.Dimension(171, 23));
        bInicioStemming.setMinimumSize(new java.awt.Dimension(171, 23));
        bInicioStemming.setPreferredSize(new java.awt.Dimension(171, 23));
        bInicioStemming.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInicioStemmingActionPerformed(evt);
            }
        });

        bSiguienteStemming.setText("Siguiente Término");
        bSiguienteStemming.setEnabled(false);
        bSiguienteStemming.setMaximumSize(new java.awt.Dimension(150, 23));
        bSiguienteStemming.setMinimumSize(new java.awt.Dimension(150, 23));
        bSiguienteStemming.setPreferredSize(new java.awt.Dimension(150, 23));
        bSiguienteStemming.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSiguienteStemmingActionPerformed(evt);
            }
        });

        bTerminarDocStemming.setText("Terminar Documento");
        bTerminarDocStemming.setEnabled(false);
        bTerminarDocStemming.setMaximumSize(new java.awt.Dimension(165, 23));
        bTerminarDocStemming.setMinimumSize(new java.awt.Dimension(165, 23));
        bTerminarDocStemming.setPreferredSize(new java.awt.Dimension(165, 23));
        bTerminarDocStemming.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTerminarDocStemmingActionPerformed(evt);
            }
        });

        bSiguienteDocStemming.setText("Siguiente Documento");
        bSiguienteDocStemming.setEnabled(false);
        bSiguienteDocStemming.setMaximumSize(new java.awt.Dimension(170, 23));
        bSiguienteDocStemming.setMinimumSize(new java.awt.Dimension(170, 23));
        bSiguienteDocStemming.setPreferredSize(new java.awt.Dimension(170, 23));
        bSiguienteDocStemming.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSiguienteDocStemmingActionPerformed(evt);
            }
        });

        bCrearIndice.setText("Crear Indice");
        bCrearIndice.setMaximumSize(new java.awt.Dimension(140, 23));
        bCrearIndice.setMinimumSize(new java.awt.Dimension(140, 23));
        bCrearIndice.setPreferredSize(new java.awt.Dimension(140, 23));
        bCrearIndice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCrearIndiceActionPerformed(evt);
            }
        });

        tNomArchivoNoStemming.setEditable(false);

        jLabel3.setText("Archivo: ");

        pLeyendaStemming.setBorder(javax.swing.BorderFactory.createTitledBorder("Leyenda"));
        pLeyendaStemming.setPreferredSize(new java.awt.Dimension(150, 127));

        jPanel2.setBackground(new java.awt.Color(0, 0, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        lSeLematizaSt.setText("Se lematiza");

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        lSinCambiosSt.setText("Sin cambios");

        javax.swing.GroupLayout pLeyendaStemmingLayout = new javax.swing.GroupLayout(pLeyendaStemming);
        pLeyendaStemming.setLayout(pLeyendaStemmingLayout);
        pLeyendaStemmingLayout.setHorizontalGroup(
            pLeyendaStemmingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLeyendaStemmingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pLeyendaStemmingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pLeyendaStemmingLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lSeLematizaSt))
                    .addGroup(pLeyendaStemmingLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lSinCambiosSt)))
                .addContainerGap())
        );
        pLeyendaStemmingLayout.setVerticalGroup(
            pLeyendaStemmingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLeyendaStemmingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pLeyendaStemmingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lSinCambiosSt)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pLeyendaStemmingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lSeLematizaSt)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        bAyudaStemming.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/help_icono.png"))); // NOI18N
        bAyudaStemming.setToolTipText("Ayuda");
        bAyudaStemming.setMaximumSize(new java.awt.Dimension(25, 25));
        bAyudaStemming.setMinimumSize(new java.awt.Dimension(25, 25));
        bAyudaStemming.setPreferredSize(new java.awt.Dimension(25, 25));
        bAyudaStemming.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAyudaStemmingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pLematizacionLayout = new javax.swing.GroupLayout(pLematizacion);
        pLematizacion.setLayout(pLematizacionLayout);
        pLematizacionLayout.setHorizontalGroup(
            pLematizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLematizacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pLematizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pLematizacionLayout.createSequentialGroup()
                        .addComponent(pSinLemas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pLeyendaStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pLematizacionLayout.createSequentialGroup()
                        .addComponent(bInicioStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSiguienteStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bTerminarDocStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSiguienteDocStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCrearIndice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pLematizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pLematizacionLayout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tNomArchivoNoStemming, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bAyudaStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(pConLemas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(192, Short.MAX_VALUE))
        );
        pLematizacionLayout.setVerticalGroup(
            pLematizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLematizacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pLematizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pLematizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(tNomArchivoNoStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bAyudaStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pConLemas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pLematizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pSinLemas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pLeyendaStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pLematizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bInicioStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSiguienteStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bTerminarDocStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSiguienteDocStemming, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bCrearIndice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tTokenizacion.addTab("Lematización", pLematizacion);

        javax.swing.GroupLayout tPreprocesadoLayout = new javax.swing.GroupLayout(tPreprocesado);
        tPreprocesado.setLayout(tPreprocesadoLayout);
        tPreprocesadoLayout.setHorizontalGroup(
            tPreprocesadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tTokenizacion, javax.swing.GroupLayout.DEFAULT_SIZE, 1105, Short.MAX_VALUE)
        );
        tPreprocesadoLayout.setVerticalGroup(
            tPreprocesadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tTokenizacion, javax.swing.GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE)
        );

        jOpciones.addTab("Preprocesado", tPreprocesado);

        pIndiceCompleto.setBorder(javax.swing.BorderFactory.createTitledBorder("Visualización del índice completo"));

        jTablaIndiceCompleto.setAutoCreateRowSorter(true);
        jTablaIndiceCompleto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rank", "Text", "Ocurrencias Coleccion", "Idf"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablaIndiceCompleto.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTablaIndiceCompleto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablaIndiceCompletoMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTablaIndiceCompleto);

        javax.swing.GroupLayout pIndiceCompletoLayout = new javax.swing.GroupLayout(pIndiceCompleto);
        pIndiceCompleto.setLayout(pIndiceCompletoLayout);
        pIndiceCompletoLayout.setHorizontalGroup(
            pIndiceCompletoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIndiceCompletoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pIndiceCompletoLayout.setVerticalGroup(
            pIndiceCompletoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIndiceCompletoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addContainerGap())
        );

        pInformacionTermino.setBorder(javax.swing.BorderFactory.createTitledBorder("Información del término"));

        jTablaPosiciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Doc", "Pos", "StartOffSet", "EndOffSet"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablaPosiciones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTablaPosiciones.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTablaPosiciones.getTableHeader().setResizingAllowed(false);
        jTablaPosiciones.getTableHeader().setReorderingAllowed(false);
        jTablaPosiciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablaPosicionesMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTablaPosiciones);

        javax.swing.GroupLayout pInformacionTerminoLayout = new javax.swing.GroupLayout(pInformacionTermino);
        pInformacionTermino.setLayout(pInformacionTerminoLayout);
        pInformacionTerminoLayout.setHorizontalGroup(
            pInformacionTerminoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInformacionTerminoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        pInformacionTerminoLayout.setVerticalGroup(
            pInformacionTerminoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInformacionTerminoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pInformacionIndice.setBorder(javax.swing.BorderFactory.createTitledBorder("Información del índice"));
        pInformacionIndice.setMaximumSize(new java.awt.Dimension(415, 149));
        pInformacionIndice.setMinimumSize(new java.awt.Dimension(415, 149));
        pInformacionIndice.setPreferredSize(new java.awt.Dimension(415, 149));

        lRutaIndice.setText("Ruta del indice:");

        lNumDocIndice.setText("Número de documentos:");

        lNumTerminosIndice.setText("Numero de términos:");

        lNumDocumentos.setText("jLabel14");

        lDirectorioIndice.setText("jLabel15");

        lNumTerminos.setText("jLabel16");

        lVersionIndice1.setText("Versión del índice:");

        lVersionIndice.setText("jLabel15");

        javax.swing.GroupLayout pInformacionIndiceLayout = new javax.swing.GroupLayout(pInformacionIndice);
        pInformacionIndice.setLayout(pInformacionIndiceLayout);
        pInformacionIndiceLayout.setHorizontalGroup(
            pInformacionIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInformacionIndiceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pInformacionIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pInformacionIndiceLayout.createSequentialGroup()
                        .addComponent(lNumDocIndice)
                        .addGap(9, 9, 9)
                        .addComponent(lNumDocumentos))
                    .addGroup(pInformacionIndiceLayout.createSequentialGroup()
                        .addComponent(lNumTerminosIndice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lNumTerminos))
                    .addGroup(pInformacionIndiceLayout.createSequentialGroup()
                        .addComponent(lVersionIndice1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lVersionIndice))
                    .addGroup(pInformacionIndiceLayout.createSequentialGroup()
                        .addComponent(lRutaIndice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lDirectorioIndice)))
                .addContainerGap(227, Short.MAX_VALUE))
        );
        pInformacionIndiceLayout.setVerticalGroup(
            pInformacionIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInformacionIndiceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pInformacionIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lRutaIndice)
                    .addComponent(lDirectorioIndice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pInformacionIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lNumDocIndice)
                    .addComponent(lNumDocumentos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pInformacionIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lNumTerminosIndice)
                    .addComponent(lNumTerminos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pInformacionIndiceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lVersionIndice1)
                    .addComponent(lVersionIndice))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pInformacionTerminoDocumento.setBorder(javax.swing.BorderFactory.createTitledBorder("Información del término en el documento"));

        tTextoIndice.setEditable(false);
        tTextoIndice.setPreferredSize(new java.awt.Dimension(1061, 315));
        jScrollPane9.setViewportView(tTextoIndice);

        jLabel15.setText("Archivo");

        jLabel16.setText("Tf x Idf");

        javax.swing.GroupLayout pInformacionTerminoDocumentoLayout = new javax.swing.GroupLayout(pInformacionTerminoDocumento);
        pInformacionTerminoDocumento.setLayout(pInformacionTerminoDocumentoLayout);
        pInformacionTerminoDocumentoLayout.setHorizontalGroup(
            pInformacionTerminoDocumentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInformacionTerminoDocumentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pInformacionTerminoDocumentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pInformacionTerminoDocumentoLayout.createSequentialGroup()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 877, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(pInformacionTerminoDocumentoLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lArchivoStemmingIndice, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lTfxIdf, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(270, 270, 270))))
        );
        pInformacionTerminoDocumentoLayout.setVerticalGroup(
            pInformacionTerminoDocumentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pInformacionTerminoDocumentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pInformacionTerminoDocumentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lTfxIdf, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(lArchivoStemmingIndice, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addContainerGap())
        );

        bBusqueda.setText("Siguiente etapa");
        bBusqueda.setMaximumSize(new java.awt.Dimension(128, 23));
        bBusqueda.setMinimumSize(new java.awt.Dimension(128, 23));
        bBusqueda.setPreferredSize(new java.awt.Dimension(128, 23));
        bBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBusquedaActionPerformed(evt);
            }
        });

        bAyudaIndexacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/help_icono.png"))); // NOI18N
        bAyudaIndexacion.setToolTipText("Ayuda");
        bAyudaIndexacion.setMaximumSize(new java.awt.Dimension(25, 25));
        bAyudaIndexacion.setMinimumSize(new java.awt.Dimension(25, 25));
        bAyudaIndexacion.setPreferredSize(new java.awt.Dimension(25, 25));
        bAyudaIndexacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAyudaIndexacionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(pIndiceCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pInformacionIndice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pInformacionTermino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAyudaIndexacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(pInformacionTerminoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(83, 83, 83))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(bAyudaIndexacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(pInformacionTermino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pInformacionIndice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pIndiceCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pInformacionTerminoDocumento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(174, 174, 174))
        );

        tIndice.addTab("Indice Completo", jPanel11);

        pGraficasTerminos.setBorder(javax.swing.BorderFactory.createTitledBorder("Términos"));
        pGraficasTerminos.setMaximumSize(new java.awt.Dimension(150, 756));
        pGraficasTerminos.setMinimumSize(new java.awt.Dimension(150, 756));
        pGraficasTerminos.setPreferredSize(new java.awt.Dimension(150, 756));

        jListaTerminos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListaTerminos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListaTerminosMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(jListaTerminos);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Orden"));

        groupOrdenaGrafica.add(jRadioAsc);
        jRadioAsc.setSelected(true);
        jRadioAsc.setText("Ascendente");
        jRadioAsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioAscActionPerformed(evt);
            }
        });

        groupOrdenaGrafica.add(jRadioDesc);
        jRadioDesc.setText("Descendente");
        jRadioDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioDescActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioAsc)
                    .addComponent(jRadioDesc)))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioAsc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioDesc)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pGraficasTerminosLayout = new javax.swing.GroupLayout(pGraficasTerminos);
        pGraficasTerminos.setLayout(pGraficasTerminosLayout);
        pGraficasTerminosLayout.setHorizontalGroup(
            pGraficasTerminosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pGraficasTerminosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pGraficasTerminosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        pGraficasTerminosLayout.setVerticalGroup(
            pGraficasTerminosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pGraficasTerminosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pGraficasDocumentos.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos"));
        pGraficasDocumentos.setMaximumSize(new java.awt.Dimension(142, 756));
        pGraficasDocumentos.setMinimumSize(new java.awt.Dimension(142, 756));
        pGraficasDocumentos.setPreferredSize(new java.awt.Dimension(142, 756));

        jListaDocumentos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListaDocumentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListaDocumentosMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(jListaDocumentos);

        javax.swing.GroupLayout pGraficasDocumentosLayout = new javax.swing.GroupLayout(pGraficasDocumentos);
        pGraficasDocumentos.setLayout(pGraficasDocumentosLayout);
        pGraficasDocumentosLayout.setHorizontalGroup(
            pGraficasDocumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pGraficasDocumentosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addContainerGap())
        );
        pGraficasDocumentosLayout.setVerticalGroup(
            pGraficasDocumentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pGraficasDocumentosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pGraficas.setBorder(javax.swing.BorderFactory.createTitledBorder("Gráfica"));
        pGraficas.setMaximumSize(new java.awt.Dimension(1000, 622));
        pGraficas.setMinimumSize(new java.awt.Dimension(649, 622));
        pGraficas.setPreferredSize(new java.awt.Dimension(637, 622));

        jPanelGrafica.setAutoscrolls(true);
        jPanelGrafica.setLayout(new java.awt.BorderLayout());
        jScrollPaneGrafica.setViewportView(jPanelGrafica);

        bGrafica.setText("Dibujar Gráfica");
        bGrafica.setEnabled(false);
        bGrafica.setMaximumSize(new java.awt.Dimension(113, 23));
        bGrafica.setMinimumSize(new java.awt.Dimension(113, 23));
        bGrafica.setPreferredSize(new java.awt.Dimension(113, 23));
        bGrafica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGraficaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pGraficasLayout = new javax.swing.GroupLayout(pGraficas);
        pGraficas.setLayout(pGraficasLayout);
        pGraficasLayout.setHorizontalGroup(
            pGraficasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pGraficasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneGrafica)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pGraficasLayout.createSequentialGroup()
                .addContainerGap(275, Short.MAX_VALUE)
                .addComponent(bGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(204, 204, 204))
        );
        pGraficasLayout.setVerticalGroup(
            pGraficasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pGraficasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneGrafica, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        bAyudaGraficas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/help_icono.png"))); // NOI18N
        bAyudaGraficas.setToolTipText("Ayuda");
        bAyudaGraficas.setMaximumSize(new java.awt.Dimension(25, 25));
        bAyudaGraficas.setMinimumSize(new java.awt.Dimension(25, 25));
        bAyudaGraficas.setPreferredSize(new java.awt.Dimension(25, 25));
        bAyudaGraficas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAyudaGraficasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pGraficasTerminos, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pGraficas, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pGraficasDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bAyudaGraficas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(117, 117, 117))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pGraficas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bAyudaGraficas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pGraficasTerminos, javax.swing.GroupLayout.PREFERRED_SIZE, 622, Short.MAX_VALUE)
                    .addComponent(pGraficasDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 622, Short.MAX_VALUE))
                .addContainerGap(168, Short.MAX_VALUE))
        );

        tIndice.addTab("Gráficas", jPanel18);

        javax.swing.GroupLayout tIndexacionLayout = new javax.swing.GroupLayout(tIndexacion);
        tIndexacion.setLayout(tIndexacionLayout);
        tIndexacionLayout.setHorizontalGroup(
            tIndexacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tIndice)
        );
        tIndexacionLayout.setVerticalGroup(
            tIndexacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tIndexacionLayout.createSequentialGroup()
                .addComponent(tIndice)
                .addGap(0, 0, 0))
        );

        jOpciones.addTab("Indexación", tIndexacion);

        pBusquedaVectorial.setBorder(javax.swing.BorderFactory.createTitledBorder("Búsqueda"));
        pBusquedaVectorial.setMaximumSize(new java.awt.Dimension(750, 124));
        pBusquedaVectorial.setMinimumSize(new java.awt.Dimension(750, 124));
        pBusquedaVectorial.setPreferredSize(new java.awt.Dimension(750, 124));

        lBusquedaVectorial.setText("Buscar");

        tBusquedaVectorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBusquedaVectorialActionPerformed(evt);
            }
        });

        bBusquedaVectorial.setText("Aceptar");
        bBusquedaVectorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBusquedaVectorialActionPerformed(evt);
            }
        });

        jcNumDocVectorial.setAutoscrolls(true);

        lNumDocVectorial.setText("Número de documentos:");

        lTiempoVectorial.setText("Tiempo de recuperación:");

        lConsResVectorial.setText("Consulta resultante:");

        tConsResVectorial.setEditable(false);

        lRecVectorial.setText("Recuperados:");

        javax.swing.GroupLayout pBusquedaVectorialLayout = new javax.swing.GroupLayout(pBusquedaVectorial);
        pBusquedaVectorial.setLayout(pBusquedaVectorialLayout);
        pBusquedaVectorialLayout.setHorizontalGroup(
            pBusquedaVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBusquedaVectorialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pBusquedaVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pBusquedaVectorialLayout.createSequentialGroup()
                        .addComponent(lBusquedaVectorial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tBusquedaVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bBusquedaVectorial))
                    .addGroup(pBusquedaVectorialLayout.createSequentialGroup()
                        .addComponent(lTiempoVectorial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lTimeVec))
                    .addGroup(pBusquedaVectorialLayout.createSequentialGroup()
                        .addComponent(lConsResVectorial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tConsResVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pBusquedaVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pBusquedaVectorialLayout.createSequentialGroup()
                        .addComponent(lNumDocVectorial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcNumDocVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pBusquedaVectorialLayout.createSequentialGroup()
                        .addComponent(lRecVectorial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lDocRecVectorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(201, Short.MAX_VALUE))
        );
        pBusquedaVectorialLayout.setVerticalGroup(
            pBusquedaVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBusquedaVectorialLayout.createSequentialGroup()
                .addGroup(pBusquedaVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lBusquedaVectorial)
                    .addComponent(tBusquedaVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bBusquedaVectorial)
                    .addComponent(lNumDocVectorial)
                    .addComponent(jcNumDocVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(pBusquedaVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pBusquedaVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lTiempoVectorial)
                        .addComponent(lTimeVec)
                        .addComponent(lRecVectorial))
                    .addComponent(lDocRecVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pBusquedaVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lConsResVectorial)
                    .addComponent(tConsResVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pVisualizacionVectorial.setBorder(javax.swing.BorderFactory.createTitledBorder("Visualización de la búsqueda"));
        pVisualizacionVectorial.setMaximumSize(new java.awt.Dimension(1081, 539));
        pVisualizacionVectorial.setMinimumSize(new java.awt.Dimension(1081, 539));
        pVisualizacionVectorial.setPreferredSize(new java.awt.Dimension(1081, 539));

        pTablaVectorial.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos"));
        pTablaVectorial.setMaximumSize(new java.awt.Dimension(232, 492));
        pTablaVectorial.setMinimumSize(new java.awt.Dimension(232, 492));

        jTablaDocVectorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Doc.", "Nom.", "Relev."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablaDocVectorial.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTablaDocVectorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablaDocVectorialMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(jTablaDocVectorial);

        bExplanationVec.setText("Detalles");
        bExplanationVec.setEnabled(false);
        bExplanationVec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExplanationVecActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pTablaVectorialLayout = new javax.swing.GroupLayout(pTablaVectorial);
        pTablaVectorial.setLayout(pTablaVectorialLayout);
        pTablaVectorialLayout.setHorizontalGroup(
            pTablaVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTablaVectorialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pTablaVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bExplanationVec))
                .addContainerGap())
        );
        pTablaVectorialLayout.setVerticalGroup(
            pTablaVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTablaVectorialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bExplanationVec)
                .addContainerGap())
        );

        pDocVectorial.setBorder(javax.swing.BorderFactory.createTitledBorder("Documento"));
        pDocVectorial.setMaximumSize(new java.awt.Dimension(807, 484));
        pDocVectorial.setMinimumSize(new java.awt.Dimension(807, 484));
        pDocVectorial.setPreferredSize(new java.awt.Dimension(807, 484));

        jScrollPane13.setAutoscrolls(true);

        tPanelDocVectorial.setEditable(false);
        tPanelDocVectorial.setContentType("text/html"); // NOI18N
        tPanelDocVectorial.setMaximumSize(new java.awt.Dimension(854, 537));
        tPanelDocVectorial.setMinimumSize(new java.awt.Dimension(854, 537));
        tPanelDocVectorial.setPreferredSize(new java.awt.Dimension(854, 537));
        jScrollPane13.setViewportView(tPanelDocVectorial);

        lNombreVectorial.setText("Nombre");

        tDocVectorial.setEditable(false);

        javax.swing.GroupLayout pDocVectorialLayout = new javax.swing.GroupLayout(pDocVectorial);
        pDocVectorial.setLayout(pDocVectorialLayout);
        pDocVectorialLayout.setHorizontalGroup(
            pDocVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDocVectorialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pDocVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                    .addGroup(pDocVectorialLayout.createSequentialGroup()
                        .addComponent(lNombreVectorial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tDocVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pDocVectorialLayout.setVerticalGroup(
            pDocVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pDocVectorialLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pDocVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tDocVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lNombreVectorial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pVisualizacionVectorialLayout = new javax.swing.GroupLayout(pVisualizacionVectorial);
        pVisualizacionVectorial.setLayout(pVisualizacionVectorialLayout);
        pVisualizacionVectorialLayout.setHorizontalGroup(
            pVisualizacionVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisualizacionVectorialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pTablaVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pDocVectorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pVisualizacionVectorialLayout.setVerticalGroup(
            pVisualizacionVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisualizacionVectorialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pVisualizacionVectorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pTablaVectorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pDocVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        bAyudaVectorial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/help_icono.png"))); // NOI18N
        bAyudaVectorial.setToolTipText("Ayuda");
        bAyudaVectorial.setMaximumSize(new java.awt.Dimension(25, 25));
        bAyudaVectorial.setMinimumSize(new java.awt.Dimension(25, 25));
        bAyudaVectorial.setPreferredSize(new java.awt.Dimension(25, 25));
        bAyudaVectorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAyudaVectorialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tConsultaVecLayout = new javax.swing.GroupLayout(tConsultaVec);
        tConsultaVec.setLayout(tConsultaVecLayout);
        tConsultaVecLayout.setHorizontalGroup(
            tConsultaVecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tConsultaVecLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tConsultaVecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pVisualizacionVectorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tConsultaVecLayout.createSequentialGroup()
                        .addComponent(pBusquedaVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAyudaVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tConsultaVecLayout.setVerticalGroup(
            tConsultaVecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tConsultaVecLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tConsultaVecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pBusquedaVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bAyudaVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pVisualizacionVectorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(149, Short.MAX_VALUE))
        );

        jOpciones.addTab("Consulta Vectorial", tConsultaVec);

        pBusquedaBooleana.setBorder(javax.swing.BorderFactory.createTitledBorder("Búsqueda"));
        pBusquedaBooleana.setMaximumSize(new java.awt.Dimension(750, 124));
        pBusquedaBooleana.setMinimumSize(new java.awt.Dimension(750, 124));
        pBusquedaBooleana.setName(""); // NOI18N
        pBusquedaBooleana.setPreferredSize(new java.awt.Dimension(750, 124));

        lBusquedaBooleana.setText("Buscar");

        tBusquedaBooleana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBusquedaBooleanaActionPerformed(evt);
            }
        });

        bBusquedaBooleana.setText("Aceptar");
        bBusquedaBooleana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBusquedaBooleanaActionPerformed(evt);
            }
        });

        jcNumDocBooleana.setAutoscrolls(true);

        lNumDocBooleana.setText("Número de documentos:");

        lTiempoBooleana.setText("Tiempo de recuperación:");

        lConsResBooleana.setText("Consulta resultante:");

        tConsResBooleana.setEditable(false);

        lRecBooleana.setText("Recuperados:");

        lConsBooleanaLucene.setText("Consulta Lucene:");

        tConsBoolLucene.setEditable(false);
        tConsBoolLucene.setMaximumSize(new java.awt.Dimension(6, 20));

        javax.swing.GroupLayout pBusquedaBooleanaLayout = new javax.swing.GroupLayout(pBusquedaBooleana);
        pBusquedaBooleana.setLayout(pBusquedaBooleanaLayout);
        pBusquedaBooleanaLayout.setHorizontalGroup(
            pBusquedaBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBusquedaBooleanaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pBusquedaBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pBusquedaBooleanaLayout.createSequentialGroup()
                        .addComponent(lBusquedaBooleana)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tBusquedaBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bBusquedaBooleana))
                    .addGroup(pBusquedaBooleanaLayout.createSequentialGroup()
                        .addComponent(lTiempoBooleana)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lTimeBool))
                    .addGroup(pBusquedaBooleanaLayout.createSequentialGroup()
                        .addComponent(lConsResBooleana)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tConsResBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pBusquedaBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pBusquedaBooleanaLayout.createSequentialGroup()
                        .addComponent(lConsBooleanaLucene)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tConsBoolLucene, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pBusquedaBooleanaLayout.createSequentialGroup()
                        .addGroup(pBusquedaBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pBusquedaBooleanaLayout.createSequentialGroup()
                                .addComponent(lNumDocBooleana)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jcNumDocBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pBusquedaBooleanaLayout.createSequentialGroup()
                                .addComponent(lRecBooleana)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lDocRecBooleana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 183, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
        );
        pBusquedaBooleanaLayout.setVerticalGroup(
            pBusquedaBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBusquedaBooleanaLayout.createSequentialGroup()
                .addGroup(pBusquedaBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lBusquedaBooleana)
                    .addComponent(tBusquedaBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bBusquedaBooleana)
                    .addComponent(lNumDocBooleana)
                    .addComponent(jcNumDocBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(pBusquedaBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lDocRecBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pBusquedaBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lRecBooleana)
                        .addComponent(lTiempoBooleana)
                        .addComponent(lTimeBool)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pBusquedaBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lConsResBooleana)
                    .addComponent(tConsResBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lConsBooleanaLucene)
                    .addComponent(tConsBoolLucene, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pVisualizacionBooleana.setBorder(javax.swing.BorderFactory.createTitledBorder("Visualización de la búsqueda"));
        pVisualizacionBooleana.setMaximumSize(new java.awt.Dimension(1081, 539));
        pVisualizacionBooleana.setMinimumSize(new java.awt.Dimension(1081, 539));

        pTablaBooleana.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos"));
        pTablaBooleana.setMaximumSize(new java.awt.Dimension(232, 492));
        pTablaBooleana.setMinimumSize(new java.awt.Dimension(232, 492));
        pTablaBooleana.setPreferredSize(new java.awt.Dimension(232, 492));

        jTablaDocBooleana.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Doc.", "Nom.", "Relev."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablaDocBooleana.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTablaDocBooleana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablaDocBooleanaMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(jTablaDocBooleana);

        javax.swing.GroupLayout pTablaBooleanaLayout = new javax.swing.GroupLayout(pTablaBooleana);
        pTablaBooleana.setLayout(pTablaBooleanaLayout);
        pTablaBooleanaLayout.setHorizontalGroup(
            pTablaBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTablaBooleanaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        pTablaBooleanaLayout.setVerticalGroup(
            pTablaBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTablaBooleanaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane15)
                .addContainerGap())
        );

        pDocBooleana.setBorder(javax.swing.BorderFactory.createTitledBorder("Documento"));
        pDocBooleana.setMaximumSize(new java.awt.Dimension(807, 484));
        pDocBooleana.setMinimumSize(new java.awt.Dimension(807, 484));
        pDocBooleana.setPreferredSize(new java.awt.Dimension(807, 484));

        jScrollPane16.setAutoscrolls(true);

        tPanelDocBooleana.setEditable(false);
        tPanelDocBooleana.setContentType("text/html"); // NOI18N
        tPanelDocBooleana.setMaximumSize(new java.awt.Dimension(854, 537));
        tPanelDocBooleana.setMinimumSize(new java.awt.Dimension(854, 537));
        tPanelDocBooleana.setPreferredSize(new java.awt.Dimension(854, 537));
        jScrollPane16.setViewportView(tPanelDocBooleana);

        lNombreBooleana.setText("Nombre");

        tDocBooleana.setEditable(false);

        javax.swing.GroupLayout pDocBooleanaLayout = new javax.swing.GroupLayout(pDocBooleana);
        pDocBooleana.setLayout(pDocBooleanaLayout);
        pDocBooleanaLayout.setHorizontalGroup(
            pDocBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDocBooleanaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pDocBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                    .addGroup(pDocBooleanaLayout.createSequentialGroup()
                        .addComponent(lNombreBooleana)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tDocBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 575, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pDocBooleanaLayout.setVerticalGroup(
            pDocBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDocBooleanaLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pDocBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lNombreBooleana)
                    .addComponent(tDocBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pVisualizacionBooleanaLayout = new javax.swing.GroupLayout(pVisualizacionBooleana);
        pVisualizacionBooleana.setLayout(pVisualizacionBooleanaLayout);
        pVisualizacionBooleanaLayout.setHorizontalGroup(
            pVisualizacionBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisualizacionBooleanaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pTablaBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pDocBooleana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pVisualizacionBooleanaLayout.setVerticalGroup(
            pVisualizacionBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisualizacionBooleanaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pVisualizacionBooleanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pTablaBooleana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pDocBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        bAyudaBooleana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/help_icono.png"))); // NOI18N
        bAyudaBooleana.setToolTipText("Ayuda");
        bAyudaBooleana.setMaximumSize(new java.awt.Dimension(25, 25));
        bAyudaBooleana.setMinimumSize(new java.awt.Dimension(25, 25));
        bAyudaBooleana.setPreferredSize(new java.awt.Dimension(25, 25));
        bAyudaBooleana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAyudaBooleanaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tConsultaBoolLayout = new javax.swing.GroupLayout(tConsultaBool);
        tConsultaBool.setLayout(tConsultaBoolLayout);
        tConsultaBoolLayout.setHorizontalGroup(
            tConsultaBoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tConsultaBoolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tConsultaBoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pVisualizacionBooleana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tConsultaBoolLayout.createSequentialGroup()
                        .addComponent(pBusquedaBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAyudaBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tConsultaBoolLayout.setVerticalGroup(
            tConsultaBoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tConsultaBoolLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tConsultaBoolLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pBusquedaBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bAyudaBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pVisualizacionBooleana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(149, Short.MAX_VALUE))
        );

        jOpciones.addTab("Consulta Booleana", tConsultaBool);

        pBusquedaProb.setBorder(javax.swing.BorderFactory.createTitledBorder("Búsqueda"));
        pBusquedaProb.setMaximumSize(new java.awt.Dimension(750, 124));
        pBusquedaProb.setMinimumSize(new java.awt.Dimension(750, 124));
        pBusquedaProb.setPreferredSize(new java.awt.Dimension(750, 124));

        lBusquedaProb.setText("Buscar");

        tBusquedaProb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBusquedaProbActionPerformed(evt);
            }
        });

        bBusquedaProb.setText("Aceptar");
        bBusquedaProb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBusquedaProbActionPerformed(evt);
            }
        });

        jcNumDocProb.setAutoscrolls(true);

        lNumDocProb.setText("Número de documentos:");

        lTiempoProb.setText("Tiempo de recuperación:");

        lConsResProb.setText("Consulta resultante:");

        tConsResProb.setEditable(false);

        lRecProb.setText("Recuperados:");

        lSaturacion.setText("Saturación");

        tNormalizacion.setColumns(4);
        tNormalizacion.setText("0.75");

        tSaturacion.setColumns(4);
        tSaturacion.setText("1.2");

        lNormalizacion.setText("Normalización");

        javax.swing.GroupLayout pBusquedaProbLayout = new javax.swing.GroupLayout(pBusquedaProb);
        pBusquedaProb.setLayout(pBusquedaProbLayout);
        pBusquedaProbLayout.setHorizontalGroup(
            pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBusquedaProbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pBusquedaProbLayout.createSequentialGroup()
                        .addComponent(lBusquedaProb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tBusquedaProb, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bBusquedaProb))
                    .addGroup(pBusquedaProbLayout.createSequentialGroup()
                        .addComponent(lTiempoProb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lTimeProb))
                    .addGroup(pBusquedaProbLayout.createSequentialGroup()
                        .addComponent(lConsResProb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tConsResProb, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pBusquedaProbLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pBusquedaProbLayout.createSequentialGroup()
                                .addComponent(lNumDocProb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jcNumDocProb, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pBusquedaProbLayout.createSequentialGroup()
                                .addComponent(lSaturacion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tSaturacion, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lNormalizacion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tNormalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pBusquedaProbLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lRecProb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lDocRecProb, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)))
                .addContainerGap(106, Short.MAX_VALUE))
        );
        pBusquedaProbLayout.setVerticalGroup(
            pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBusquedaProbLayout.createSequentialGroup()
                .addGroup(pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pBusquedaProbLayout.createSequentialGroup()
                        .addGroup(pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lNumDocProb)
                            .addComponent(jcNumDocProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lDocRecProb, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lRecProb, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pBusquedaProbLayout.createSequentialGroup()
                        .addGroup(pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lBusquedaProb)
                            .addComponent(tBusquedaProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bBusquedaProb))
                        .addGap(11, 11, 11)
                        .addGroup(pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lTiempoProb)
                            .addComponent(lTimeProb))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lConsResProb)
                        .addComponent(tConsResProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pBusquedaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lSaturacion)
                        .addComponent(tSaturacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lNormalizacion)
                        .addComponent(tNormalizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pVisualizacionProb.setBorder(javax.swing.BorderFactory.createTitledBorder("Visualización de la búsqueda"));
        pVisualizacionProb.setMaximumSize(new java.awt.Dimension(1081, 539));
        pVisualizacionProb.setMinimumSize(new java.awt.Dimension(1081, 539));
        pVisualizacionProb.setPreferredSize(new java.awt.Dimension(1081, 539));

        pTablaProb.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos"));
        pTablaProb.setMaximumSize(new java.awt.Dimension(232, 492));
        pTablaProb.setMinimumSize(new java.awt.Dimension(232, 492));
        pTablaProb.setName(""); // NOI18N
        pTablaProb.setPreferredSize(new java.awt.Dimension(232, 492));

        jTablaDocProb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Doc.", "Nom.", "Relev."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablaDocProb.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTablaDocProb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablaDocProbMouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(jTablaDocProb);

        bExplanationProb.setText("Detalles");
        bExplanationProb.setEnabled(false);
        bExplanationProb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExplanationProbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pTablaProbLayout = new javax.swing.GroupLayout(pTablaProb);
        pTablaProb.setLayout(pTablaProbLayout);
        pTablaProbLayout.setHorizontalGroup(
            pTablaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTablaProbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pTablaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(bExplanationProb)
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pTablaProbLayout.setVerticalGroup(
            pTablaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTablaProbLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bExplanationProb)
                .addContainerGap())
        );

        pDocProb.setBorder(javax.swing.BorderFactory.createTitledBorder("Documento"));
        pDocProb.setMaximumSize(new java.awt.Dimension(866, 484));
        pDocProb.setMinimumSize(new java.awt.Dimension(866, 484));
        pDocProb.setPreferredSize(new java.awt.Dimension(866, 484));

        jScrollPane18.setAutoscrolls(true);

        tPanelDocProb.setEditable(false);
        tPanelDocProb.setContentType("text/html"); // NOI18N
        tPanelDocProb.setMaximumSize(new java.awt.Dimension(854, 537));
        tPanelDocProb.setMinimumSize(new java.awt.Dimension(854, 537));
        tPanelDocProb.setPreferredSize(new java.awt.Dimension(854, 537));
        jScrollPane18.setViewportView(tPanelDocProb);

        lNombreProb.setText("Nombre");

        tDocProb.setEditable(false);

        javax.swing.GroupLayout pDocProbLayout = new javax.swing.GroupLayout(pDocProb);
        pDocProb.setLayout(pDocProbLayout);
        pDocProbLayout.setHorizontalGroup(
            pDocProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDocProbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pDocProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 772, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pDocProbLayout.createSequentialGroup()
                        .addComponent(lNombreProb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tDocProb, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pDocProbLayout.setVerticalGroup(
            pDocProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pDocProbLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pDocProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lNombreProb)
                    .addComponent(tDocProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pVisualizacionProbLayout = new javax.swing.GroupLayout(pVisualizacionProb);
        pVisualizacionProb.setLayout(pVisualizacionProbLayout);
        pVisualizacionProbLayout.setHorizontalGroup(
            pVisualizacionProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisualizacionProbLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pTablaProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pDocProb, javax.swing.GroupLayout.PREFERRED_SIZE, 811, Short.MAX_VALUE)
                .addContainerGap())
        );
        pVisualizacionProbLayout.setVerticalGroup(
            pVisualizacionProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVisualizacionProbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pVisualizacionProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pTablaProb, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                    .addComponent(pDocProb, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE))
                .addContainerGap())
        );

        bAyudaProbabilística.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/help_icono.png"))); // NOI18N
        bAyudaProbabilística.setToolTipText("Ayuda");
        bAyudaProbabilística.setMaximumSize(new java.awt.Dimension(25, 25));
        bAyudaProbabilística.setMinimumSize(new java.awt.Dimension(25, 25));
        bAyudaProbabilística.setPreferredSize(new java.awt.Dimension(25, 25));
        bAyudaProbabilística.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAyudaProbabilísticaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tConsultaProbLayout = new javax.swing.GroupLayout(tConsultaProb);
        tConsultaProb.setLayout(tConsultaProbLayout);
        tConsultaProbLayout.setHorizontalGroup(
            tConsultaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tConsultaProbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tConsultaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tConsultaProbLayout.createSequentialGroup()
                        .addComponent(pBusquedaProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAyudaProbabilística, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pVisualizacionProb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        tConsultaProbLayout.setVerticalGroup(
            tConsultaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tConsultaProbLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tConsultaProbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pBusquedaProb, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bAyudaProbabilística, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pVisualizacionProb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(149, Short.MAX_VALUE))
        );

        jOpciones.addTab("Consulta Probabilistica", tConsultaProb);

        bArchivo.setText("Archivo");

        bInicio.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.ALT_MASK));
        bInicio.setText("Volver al inicio");
        bInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInicioActionPerformed(evt);
            }
        });
        bArchivo.add(bInicio);

        bSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        bSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/exit.png"))); // NOI18N
        bSalir.setMnemonic('S');
        bSalir.setText("Salir");
        bSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSalirActionPerformed(evt);
            }
        });
        bArchivo.add(bSalir);

        jMenuBar1.add(bArchivo);

        bSulairl.setText("SulaIR-L");

        bDocOrig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/VisorIcono.png"))); // NOI18N
        bDocOrig.setText("Documentos Originales");
        bDocOrig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDocOrigActionPerformed(evt);
            }
        });
        bSulairl.add(bDocOrig);

        bAyuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/help_icono.png"))); // NOI18N
        bAyuda.setText("Ayuda");
        bAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAyudaActionPerformed(evt);
            }
        });
        bSulairl.add(bAyuda);

        bAcercaDe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/info.png"))); // NOI18N
        bAcercaDe.setText("Acerca de...");
        bAcercaDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAcercaDeActionPerformed(evt);
            }
        });
        bSulairl.add(bAcercaDe);

        jMenuBar1.add(bSulairl);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, 847, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jOpciones.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSalirActionPerformed
		System.exit(0);
    }//GEN-LAST:event_bSalirActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
		if (etapa == 0){
			jOpciones.setEnabledAt(0, true);
			jOpciones.setSelectedIndex(0);
			tTokenizacion.setEnabledAt(1, false);
			tTokenizacion.setEnabledAt(2, false);
			jOpciones.setEnabledAt(1, false);
			jOpciones.setEnabledAt(2, false);
			jOpciones.setEnabledAt(3, false);
			jOpciones.setEnabledAt(4, false);
		}else{
			if (etapa == 1){
				bCrearIndice.setEnabled(false);
				jOpciones.setEnabledAt(0, true);
				tTokenizacion.setEnabledAt(1, true);
				tTokenizacion.setEnabledAt(2, true);
				jOpciones.setEnabledAt(1, true);
				jOpciones.setSelectedIndex(1);
				jOpciones.setEnabledAt(2, false);
				jOpciones.setEnabledAt(3, false);
				jOpciones.setEnabledAt(4, false);
			}else{
				bCrearIndice.setEnabled(false);
				bBusqueda.setEnabled(false);
				jOpciones.setEnabledAt(0, true);
				tTokenizacion.setEnabledAt(1, true);
				tTokenizacion.setEnabledAt(2, true);
				jOpciones.setEnabledAt(1, true);
				jOpciones.setEnabledAt(2, true);
				jOpciones.setEnabledAt(3, true);
				jOpciones.setEnabledAt(4, true);
				jOpciones.setSelectedIndex(2);
			}
		}
    }//GEN-LAST:event_formWindowOpened

    private void bInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInicioActionPerformed
        try {
            col.liberarMemoria();
            archivosOriginales.clear();
            archivosTokenizados.clear();
            archivosStopWords.clear();
            archivosStemizado.clear();
            if (indice!=null){
				indice.cerrarIndice();
            }
			dlmDocumentos.clear();
			dlmTerminos.clear();
			dlmListaArchivoOri.clear();
			dlmListaArchivoStopWord.clear();
            col = null;
			chart = null;
			Runtime garbage = Runtime.getRuntime();
			garbage.gc();
            Configuracion conf = new Configuracion();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            conf.setLocation(dim.width/2-conf.getSize().width/2, dim.height/2-conf.getSize().height/2);
            conf.setVisible(true);
            dispose();
        } catch (IOException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bInicioActionPerformed

    private void bTerminarDocStopWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTerminarDocStopWordActionPerformed
        while(!textoTokeSW.trim().equals("")){
            siguienteTermStopWords();
        }
        bTerminarDocStopWord.setEnabled(false);
    }//GEN-LAST:event_bTerminarDocStopWordActionPerformed

    private void bSiguienteDocStopWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSiguienteDocStopWordActionPerformed
        cargarDocumentoStopWord();
    }//GEN-LAST:event_bSiguienteDocStopWordActionPerformed

    private void bSiguientePasoStopWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSiguientePasoStopWordActionPerformed
        tTokenizacion.setEnabledAt(2, true);
        tTokenizacion.setSelectedIndex(2);
    }//GEN-LAST:event_bSiguientePasoStopWordActionPerformed

    private void bInicioStopWordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInicioStopWordsActionPerformed
        docStopWord = 0;
		cargarDocumentoStopWord();
    }//GEN-LAST:event_bInicioStopWordsActionPerformed

    private void bSiguienteStopWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSiguienteStopWordActionPerformed
        siguienteTermStopWords();
    }//GEN-LAST:event_bSiguienteStopWordActionPerformed

    private void bInicioStemmingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInicioStemmingActionPerformed
        docStemming = 0;
		if (col.getStopWord()==null){
				cargarDocumentoStemmingConSW();
			}else{
				cargarDocumentoStemmingSinSW();
		}
    }//GEN-LAST:event_bInicioStemmingActionPerformed

    private void bSiguienteStemmingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSiguienteStemmingActionPerformed
		siguienteTermStemizado();
    }//GEN-LAST:event_bSiguienteStemmingActionPerformed

    private void bSiguienteDocStemmingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSiguienteDocStemmingActionPerformed
        if (col.getStopWord()==null){
            cargarDocumentoStemmingConSW();
        }else{
            cargarDocumentoStemmingSinSW();
        }
    }//GEN-LAST:event_bSiguienteDocStemmingActionPerformed

    private void bTerminarDocStemmingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTerminarDocStemmingActionPerformed
        while(!textoNoStemming.trim().equals("")){
            siguienteTermStemizado();
        }
        bTerminarDocStemming.setEnabled(false);
    }//GEN-LAST:event_bTerminarDocStemmingActionPerformed

    private void bCrearIndiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCrearIndiceActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdCreacionIndice.setLocation(dim.width/2-jdCreacionIndice.getSize().width/2, dim.height/2-jdCreacionIndice.getSize().height/2);
		jdCreacionIndice.setVisible(true);
    }//GEN-LAST:event_bCrearIndiceActionPerformed

    private void bTerminarDocTokenizadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTerminarDocTokenizadoActionPerformed
        while(!textoOriginal.trim().equals("")){
            siguienteTermTokenizado();
        }
        bTerminarDocTokenizado.setEnabled(false);
    }//GEN-LAST:event_bTerminarDocTokenizadoActionPerformed

    private void bSiguientePasoTokenizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSiguientePasoTokenizacionActionPerformed
        if (col.getStopWord()!=null){
            tTokenizacion.setEnabledAt(1, true);
            tTokenizacion.setSelectedIndex(1);
        }else{
            tTokenizacion.setEnabledAt(2, true);
            tTokenizacion.setSelectedIndex(2);
        }
    }//GEN-LAST:event_bSiguientePasoTokenizacionActionPerformed

    private void bSiguienteDocTokenizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSiguienteDocTokenizacionActionPerformed
        cargarDocumentoTokenizado();
    }//GEN-LAST:event_bSiguienteDocTokenizacionActionPerformed

    private void bSiguienteTokenizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSiguienteTokenizacionActionPerformed
        siguienteTermTokenizado();
    }//GEN-LAST:event_bSiguienteTokenizacionActionPerformed

    private void bInicioTokenizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInicioTokenizacionActionPerformed
        docTokenizado = 0;
        cargarDocumentoTokenizado();
    }//GEN-LAST:event_bInicioTokenizacionActionPerformed

    private void bContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bContinuarActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdCreacionIndice.setLocation(dim.width/2-jdCreacionIndice.getSize().width/2, dim.height/2-jdCreacionIndice.getSize().height/2);
		jOpciones.setEnabledAt(1, true);
		jOpciones.setSelectedIndex(1);
		jdCreacionIndice.dispose();
    }//GEN-LAST:event_bContinuarActionPerformed

    private void jdCreacionIndiceWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jdCreacionIndiceWindowOpened
		SulaIRL.archivoConf.setEtapa(1);
		if (SulaIRL.archivoConf.guardarConfiguración(col.getNombre())){
			HebraIndice hebraIndice = new HebraIndice();
			hebraIndice.start();
		}else{
			JOptionPane.showOptionDialog(this, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_optionDialog_creacionArchivoErronea"), ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_atencion"), 
				JOptionPane.ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE, null, new Object[]{ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar")}, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar"));
		}
    }//GEN-LAST:event_jdCreacionIndiceWindowOpened

    private void bBusquedaVectorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBusquedaVectorialActionPerformed
		jTablaDocVectorial.setModel(dtmTablaBusquedaVectorial);
		jTablaDocVectorial.removeAll();
		tPanelDocVectorial.setText("");
		tDocVectorial.setText("");
		dtmTablaBusquedaVectorial.setRowCount(0);
		if (!tBusquedaVectorial.getText().equals("")){
			try {
				String[] columnNames = {"Doc.", "Nom.", "Relev."};
				dtmTablaBusquedaVectorial.setColumnIdentifiers(columnNames);
				ScoreDoc[] hits = indice.queryVect(StringUtils.stripAccents(tBusquedaVectorial.getText()), col.getStopWord(), jcNumDocVectorial.getSelectedIndex()+1);
				tConsResVectorial.setText(indice.getConsVec());
				lDocRecVectorial.setText(hits.length + " Docs.");
				lTimeVec.setText(indice.getTimeVec() + " us");
				for (ScoreDoc hit: hits){
					BigDecimal bd = new BigDecimal(Float.toString(hit.score));
					bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
					Object[] fila = {hit.doc, indice.getNombreFichero(hit.doc), bd};
					dtmTablaBusquedaVectorial.addRow(fila);
				}
			} catch (IOException | ParseException ex) {
				Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
			}
		}else{
			JOptionPane.showOptionDialog(this, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("optionDialog_consulta_vacia"), ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_atencion"), 
				JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar")},ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar"));
		}
    }//GEN-LAST:event_bBusquedaVectorialActionPerformed

    private void tBusquedaVectorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBusquedaVectorialActionPerformed
		ActionEvent e = null;
		bBusquedaVectorialActionPerformed(e);
    }//GEN-LAST:event_tBusquedaVectorialActionPerformed

    private void jTablaDocVectorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaDocVectorialMouseClicked
		ArchivosColeccion original = col.getArchivoOriginal().get(Integer.parseInt(jTablaDocVectorial.getValueAt(jTablaDocVectorial.getSelectedRow(), 0).toString()));
		try {
			tDocVectorial.setText(jTablaDocVectorial.getValueAt(jTablaDocVectorial.getSelectedRow(), 1).toString());
			String textoFichero = FileUtils.readFileToString(new File(original.getRutaFichero()), "UTF-8");
			String consulta =  tBusquedaVectorial.getText();
			Analyzer analyzer = new StandardAnalyzer();
			if (col.getStopWord() != null){
				analyzer = new StandardAnalyzer(new FileReader(col.getStopWord()));
			}
			consulta = indice.procesarStringConsulta(consulta, analyzer);
			Query query = new QueryParser("text", new StandardAnalyzer()).parse(consulta);
//			textoFichero = textoFichero.replace("\n", "<br>");
			Scorer scorer = new QueryScorer(query);
			Highlighter highlighter = new Highlighter(scorer);
			highlighter.setTextFragmenter(new SimpleSpanFragmenter((QueryScorer) scorer, Integer.MAX_VALUE));
			highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
			TokenStream tokenStream = new SimpleAnalyzer().tokenStream(null, new StringReader(textoFichero));
			tPanelDocVectorial.setDocument(new HTMLDocument());
			tPanelDocVectorial.getDocument().putProperty("IgnoreCharsetDirective", true);
			tPanelDocVectorial.setText(highlighter.getBestFragment(tokenStream, textoFichero));
			tPanelDocVectorial.setCaretPosition(0);
			bExplanationVec.setEnabled(true);
		} catch (IOException | InvalidTokenOffsetsException | ParseException ex) {
			Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
		}
    }//GEN-LAST:event_jTablaDocVectorialMouseClicked

    private void bExplanationVecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExplanationVecActionPerformed
		tExplanation.setText(indice.getExplanationVec(Integer.parseInt(jTablaDocVectorial.getValueAt(jTablaDocVectorial.getSelectedRow(), 0).toString())));
		jdDetalles.setTitle(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_explanation") + jTablaDocVectorial.getValueAt(jTablaDocVectorial.getSelectedRow(), 1).toString());
		tExplanation.setCaretPosition(0);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdDetalles.setLocation(dim.width/2-jdDetalles.getSize().width/2, dim.height/2-jdDetalles.getSize().height/2);
		jdDetalles.setVisible(true);
    }//GEN-LAST:event_bExplanationVecActionPerformed

    private void bBusquedaBooleanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBusquedaBooleanaActionPerformed
		String consultaBooleana = StringUtils.stripAccents(tBusquedaBooleana.getText());
		tPanelDocBooleana.setText("");
		tDocBooleana.setText("");
		if (!consultaBooleana.equals("")){
			Pattern pattern = Pattern.compile("^(\\(*\\s*)*(NOT{0,1}\\s*)?(\\(*\\s*)*\\p{Alnum}+\\s*(\\s*(AND|OR)\\s+(NOT{0,1}\\s+)?(\\(*\\s*)*\\p{Alnum}+\\s*\\)*)*$");
			Matcher m = pattern.matcher(consultaBooleana.trim());
			if (m.matches()){
				try {
					jTablaDocBooleana.setModel(dtmTablaBusquedaBooleana);
					jTablaDocBooleana.removeAll();
					dtmTablaBusquedaBooleana.setRowCount(0);
					String[] columnNames = {"Doc.", "Nom."};
					dtmTablaBusquedaBooleana.setColumnIdentifiers(columnNames);
					ScoreDoc[] hits = indice.queryBool(tBusquedaBooleana.getText()+ " ", jcNumDocBooleana.getSelectedIndex()+1);
					if (hits!=null){
						tConsResBooleana.setText(indice.getConsBool());
						lDocRecBooleana.setText(hits.length + " Docs.");
						lTimeBool.setText(indice.getTimeBool() + " us");
						tConsBoolLucene.setText(indice.getConsBoolLucene());
						for (ScoreDoc hit: hits){
							BigDecimal bd = new BigDecimal(Float.toString(hit.score));
							bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
							Object[] fila = {hit.doc, indice.getNombreFichero(hit.doc), bd};
							dtmTablaBusquedaBooleana.addRow(fila);
						}
					}else{
						JOptionPane.showOptionDialog(this, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_optionDialog_consultaErronea"), ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_atencion"), 
							JOptionPane.ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE, null, new Object[]{ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar")},ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar"));
					}
				} catch (IOException ex) {
					Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
				}
			} else {
				JOptionPane.showOptionDialog(this, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_optionDialog_consultaErronea"), ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_atencion"), 
					JOptionPane.ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE, null, new Object[]{ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar")}, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar"));
			}
		}else{
			JOptionPane.showOptionDialog(this, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("optionDialog_consulta_vacia"), ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_atencion"), 
				JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar")},ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar"));
		}
    }//GEN-LAST:event_bBusquedaBooleanaActionPerformed

    private void jTablaDocBooleanaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaDocBooleanaMouseClicked
		ArchivosColeccion original = col.getArchivoOriginal().get(Integer.parseInt(jTablaDocBooleana.getValueAt(jTablaDocBooleana.getSelectedRow(), 0).toString()));
		try {
			tDocBooleana.setText(jTablaDocBooleana.getValueAt(jTablaDocBooleana.getSelectedRow(), 1).toString());
			String textoFichero = FileUtils.readFileToString(new File(original.getRutaFichero()), "UTF-8");
			String consulta = tBusquedaBooleana.getText();
			Analyzer analyzer = new StandardAnalyzer();
			if (col.getIdioma().equals("en")){
				analyzer = new EnglishAnalyzer(null);
			}else{
				analyzer = new SpanishAnalyzer(null);
			}
			String terminosConsulta = tBusquedaBooleana.getText();
			terminosConsulta = terminosConsulta.replaceAll("AND|OR|NOT|\\)|\\(", "").replaceAll("\\s+", " ");
			Query query = new QueryParser("text", new StandardAnalyzer()).parse(terminosConsulta);
			BooleanQuery booleanQuery = new BooleanQuery.Builder()
				.add(query, BooleanClause.Occur.MUST)
				.build();
			Scorer scorer = new QueryScorer(query);
			Highlighter highlighter = new Highlighter(scorer);
			highlighter.setTextFragmenter(new SimpleSpanFragmenter((QueryScorer) scorer, Integer.MAX_VALUE));
			highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
			TokenStream tokenStream = new SimpleAnalyzer().tokenStream(null, new StringReader(textoFichero));
			tPanelDocBooleana.setDocument(new HTMLDocument());
			tPanelDocBooleana.getDocument().putProperty("IgnoreCharsetDirective", true);
			tPanelDocBooleana.setText(highlighter.getBestFragment(tokenStream, textoFichero));
			tPanelDocBooleana.setCaretPosition(0);
		} catch (IOException | InvalidTokenOffsetsException | ParseException ex) {
			Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
		}
    }//GEN-LAST:event_jTablaDocBooleanaMouseClicked

    private void tBusquedaBooleanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBusquedaBooleanaActionPerformed
		ActionEvent e = null;
        bBusquedaBooleanaActionPerformed(e);
    }//GEN-LAST:event_tBusquedaBooleanaActionPerformed

    private void tBusquedaProbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBusquedaProbActionPerformed
		ActionEvent e = null;
        bBusquedaProbActionPerformed(e);
    }//GEN-LAST:event_tBusquedaProbActionPerformed

    private void bBusquedaProbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBusquedaProbActionPerformed
		jTablaDocProb.setModel(dtmTablaBusquedaProb);
		jTablaDocProb.removeAll();
		tPanelDocProb.setText("");
		dtmTablaBusquedaProb.setRowCount(0);
		tDocProb.setText("");
		if (!tBusquedaProb.getText().equals("") && !tSaturacion.getText().equals("") && !tNormalizacion.getText().equals("")){
			float saturacion = Float.parseFloat(tSaturacion.getText().replaceAll(",", "."));
			float normalizacion = Float.parseFloat(tNormalizacion.getText().replaceAll(",", "."));
			if (normalizacion>=0 && normalizacion<=1 && saturacion >=0){
						try {
						String[] columnNames = {"Doc.", "Nom.", "Relev."};
						dtmTablaBusquedaProb.setColumnIdentifiers(columnNames);
						ScoreDoc[] hits = indice.queryProb(StringUtils.stripAccents(tBusquedaProb.getText()), col.getStopWord(), jcNumDocProb.getSelectedIndex()+1, normalizacion, saturacion);
						tConsResProb.setText(indice.getConsProb());
						lDocRecProb.setText(hits.length + " Docs.");
						lTimeProb.setText(indice.getTimeProb() + " us");
						for (ScoreDoc hit: hits){
							BigDecimal bd = new BigDecimal(Float.toString(hit.score));
							bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
							Object[] fila = {hit.doc, indice.getNombreFichero(hit.doc), bd};
							dtmTablaBusquedaProb.addRow(fila);
						}
					} catch (IOException | ParseException ex) {
						Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
					}
			}else{
				JOptionPane.showOptionDialog(this, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("optionDialog_consulta_probabilistica_parametros"), ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_atencion"), 
					JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar")},ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar"));
			}
		}else{
				JOptionPane.showOptionDialog(this, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("optionDialog_consulta_vacia_probabilistica"), ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_atencion"), 
					JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar")},ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_aceptar"));
		}
    }//GEN-LAST:event_bBusquedaProbActionPerformed

    private void jTablaDocProbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaDocProbMouseClicked
		ArchivosColeccion original = col.getArchivoOriginal().get(Integer.parseInt(jTablaDocProb.getValueAt(jTablaDocProb.getSelectedRow(), 0).toString()));
		try {
			tDocProb.setText(jTablaDocProb.getValueAt(jTablaDocProb.getSelectedRow(), 1).toString());
			String textoFichero = FileUtils.readFileToString(new File(original.getRutaFichero()), "UTF-8");
			String consulta =  tBusquedaProb.getText();
			Analyzer analyzer = new StandardAnalyzer();
			if (col.getStopWord() != null){
				analyzer = new StandardAnalyzer(new FileReader(col.getStopWord()));
			}
			consulta = indice.procesarStringConsulta(consulta, analyzer);
			Query query = new QueryParser("text", new StandardAnalyzer()).parse(consulta);
//			textoFichero = textoFichero.replace("\n", "<br>");
			Scorer scorer = new QueryScorer(query);
			Highlighter highlighter = new Highlighter(scorer);
			highlighter.setTextFragmenter(new SimpleSpanFragmenter((QueryScorer) scorer, Integer.MAX_VALUE));
			highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
			TokenStream tokenStream = new SimpleAnalyzer().tokenStream(null, new StringReader(textoFichero));
			tPanelDocProb.setDocument(new HTMLDocument());
			tPanelDocProb.getDocument().putProperty("IgnoreCharsetDirective", true);
			tPanelDocProb.setText(highlighter.getBestFragment(tokenStream, textoFichero));
			tPanelDocProb.setCaretPosition(0);
			bExplanationProb.setEnabled(true);
		} catch (IOException | InvalidTokenOffsetsException | ParseException ex) {
			Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
		}
    }//GEN-LAST:event_jTablaDocProbMouseClicked

    private void bExplanationProbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExplanationProbActionPerformed
		tExplanation.setText(indice.getExplanationProb(Integer.parseInt(jTablaDocProb.getValueAt(jTablaDocProb.getSelectedRow(), 0).toString())));
		jdDetalles.setTitle(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_explanation") + jTablaDocProb.getValueAt(jTablaDocProb.getSelectedRow(), 1).toString());
		tExplanation.setCaretPosition(0);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdDetalles.setLocation(dim.width/2-jdDetalles.getSize().width/2, dim.height/2-jdDetalles.getSize().height/2);
		jdDetalles.setVisible(true);
    }//GEN-LAST:event_bExplanationProbActionPerformed

    private void bGraficaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGraficaActionPerformed
        if (chart != null){
			guardarGrafica();
        }
    }//GEN-LAST:event_bGraficaActionPerformed

    private void jTablaPosicionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaPosicionesMouseClicked
        try {
            int docID = Integer.parseInt(jTablaPosiciones.getValueAt(jTablaPosiciones.getSelectedRow(), 0).toString());
            int startOffSet = Integer.parseInt(jTablaPosiciones.getValueAt(jTablaPosiciones.getSelectedRow(), 2).toString());
            int endOffSet = Integer.parseInt(jTablaPosiciones.getValueAt(jTablaPosiciones.getSelectedRow(), 3).toString());
            String ficheroStemming = indice.getFicheroStemming(docID);
            File f = new File (ficheroStemming);

			TFIDFSimilarity similarity = new ClassicSimilarity();
			
            // Establecemos el tf x idf del término en el documento seleccionado en el documento.
            int idDoc = (int)jTablaPosiciones.getValueAt(jTablaPosiciones.getSelectedRow(), 0);
			Path p = Paths.get(col.getRutaColeccion() + "indice/");  // Indicamos la carpeta del índice
			Directory directory = FSDirectory.open(p); // Almacenamos el índice en el directorio
			IndexReader ireader = DirectoryReader.open(directory);
		
			Term t = new Term("text", jTablaIndiceCompleto.getValueAt(jTablaIndiceCompleto.getSelectedRow(), 1).toString());
			PostingsEnum docEnum = MultiFields.getTermDocsEnum(ireader, "text", t.bytes());
            while ((docEnum.nextDoc()) != PostingsEnum.NO_MORE_DOCS) {
				if (idDoc == docEnum.docID()){
					double tf_idf = (similarity.tf(docEnum.freq()) * Double.parseDouble(jTablaIndiceCompleto.getValueAt(jTablaIndiceCompleto.getSelectedRow(), 4).toString()));
					BigDecimal bd = new BigDecimal(tf_idf);
					bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
					lTfxIdf.setText(""+ bd);
					System.out.println(similarity.tf(docEnum.freq()) + " x " + Float.parseFloat(jTablaIndiceCompleto.getValueAt(jTablaIndiceCompleto.getSelectedRow(), 4).toString()) + " = " + tf_idf);
				}
            }
            if (!lArchivoStemmingIndice.getText().equals(f.getName())){
                lArchivoStemmingIndice.setText(f.getName());
                tTextoIndice.setText(FileUtils.readFileToString(f, "UTF-8"));
            }
            tTextoIndice.getHighlighter().removeAllHighlights();
            tTextoIndice.setCaretPosition(startOffSet);
            DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
            tTextoIndice.getHighlighter().addHighlight(startOffSet, endOffSet, highlightPainter);

        } catch (IOException | BadLocationException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTablaPosicionesMouseClicked

    private void jTablaIndiceCompletoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaIndiceCompletoMouseClicked
        try {
            jTablaPosiciones.removeAll();
            dtmTablaIndiceCompleto = new DefaultTableModel();
            jTablaPosiciones.setModel(dtmTablaIndiceCompleto);
            String[] columnNames = {"Doc ID", "Pos", "Start offset", "End offset"};
            dtmTablaIndiceCompleto.setColumnIdentifiers(columnNames);
            String termino = jTablaIndiceCompleto.getValueAt(jTablaIndiceCompleto.getSelectedRow(), 1).toString();
            ArrayList<int[]> listaPosiciones = indice.getPosiciones(termino);
            listaPosiciones.forEach((posiciones) -> {
//          System.out.println("Termino: " + termino + " Documento: " + posiciones[0] + " Posición: " + posiciones[1] + " StartOffset: " + posiciones[2] + " EndOffset: " +  posiciones[3]);
                Object[] fila = { posiciones[0], posiciones[1], posiciones[2], posiciones[3]};
                dtmTablaIndiceCompleto.addRow(fila);
            });
        } catch (IOException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTablaIndiceCompletoMouseClicked

    private void bBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBusquedaActionPerformed
		SulaIRL.archivoConf.setEtapa(2);
		if (SulaIRL.archivoConf.guardarConfiguración(col.getNombre())){        
			jOpciones.setEnabledAt(2, true);
			jOpciones.setEnabledAt(3, true);
			jOpciones.setEnabledAt(4, true);
			jOpciones.setSelectedIndex(2);
		}
		
    }//GEN-LAST:event_bBusquedaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(new URI("http://bahia.ugr.es/~sulairl/"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }//GEN-LAST:event_jButton1ActionPerformed

    private void bAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAcercaDeActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdAcercaDe.setLocation(dim.width/2-jdAcercaDe.getSize().width/2, dim.height/2-jdAcercaDe.getSize().height/2);
		jdAcercaDe.setVisible(true);
    }//GEN-LAST:event_bAcercaDeActionPerformed

    private void bAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAyudaActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdAyuda.setLocation(dim.width/2-jdAyuda.getSize().width/2, dim.height/2-jdAyuda.getSize().height/2);
		jdAyuda.setVisible(true);
    }//GEN-LAST:event_bAyudaActionPerformed

    private void jdVisorDocumentosWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jdVisorDocumentosWindowOpened
		// Cargamos los documetos originales
		jListaArchivosOri.setModel(dlmListaArchivoOri);
		for (ArchivosColeccion archivoOri : col.getDocOriginales()){
			dlmListaArchivoOri.add(archivoOri.getNumArchivo(), new File(archivoOri.getRutaFichero()).getName());
		}
		jListaStopWord.setModel(dlmListaArchivoStopWord);
		if (col.getStopWord() != null){
			dlmListaArchivoStopWord.addElement(col.getStopWord().getName());
		}
		pVisorArchivoSW.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_visorDocumentos_stopword")));
		pVisorArchivosOriginales.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_visorDocumentos_archivos")));
		pVisorDescripcion.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_visorDocumentos_descripcion")));
		jdVisorDocumentos.setTitle(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_ventana_titulo_visorDocumentos"));
    }//GEN-LAST:event_jdVisorDocumentosWindowOpened

    private void bDocOrigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDocOrigActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdVisorDocumentos.setLocation(dim.width/2-jdVisorDocumentos.getSize().width/2, dim.height/2-jdVisorDocumentos.getSize().height/2);
		jdVisorDocumentos.setVisible(true);
    }//GEN-LAST:event_bDocOrigActionPerformed

    private void jListaStopWordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jListaStopWordFocusGained
		jListaArchivosOri.clearSelection();
    }//GEN-LAST:event_jListaStopWordFocusGained

    private void jListaArchivosOriFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jListaArchivosOriFocusGained
		jListaStopWord.clearSelection(); 
    }//GEN-LAST:event_jListaArchivosOriFocusGained

    private void jListaArchivosOriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListaArchivosOriMouseClicked
		ArchivosColeccion original = col.getDocOriginales().get(jListaArchivosOri.getSelectedIndex());
		if (!(FilenameUtils.getExtension(original.getRutaFichero()).equals("pdf"))){
			try {
				String textoFichero = FileUtils.readFileToString(new File(original.getRutaFichero()), "UTF-8");
				if (FilenameUtils.getExtension(original.getRutaFichero()).equals("html") || 
					FilenameUtils.getExtension(original.getRutaFichero()).equals("xml") ||
					FilenameUtils.getExtension(original.getRutaFichero()).equals("htm")){
					jPaneVisorDocumentos.setContentType("text/html");
					jPaneVisorDocumentos.setDocument(new HTMLDocument());
					jPaneVisorDocumentos.getDocument().putProperty("IgnoreCharsetDirective", true);
				}else{
					jPaneVisorDocumentos.setContentType("text/plain");
					jPaneVisorDocumentos.setDocument(new DefaultStyledDocument());
				}
				jPaneVisorDocumentos.setText(textoFichero);
				jPaneVisorDocumentos.setCaretPosition(0);
			} catch (IOException ex) {
				Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
			}
		}else{
			int option = JOptionPane.showOptionDialog(this, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("optionDialog_ver_visor_pdf"), ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("optionDialog_titulo_abrirPDF"), JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (option == JOptionPane.YES_OPTION){
				if (Desktop.isDesktopSupported()) {
					try {
						File myFile = new File(original.getRutaFichero());
						Desktop.getDesktop().open(myFile);
					} catch (IOException ex) {}
				}
			}
		}
    }//GEN-LAST:event_jListaArchivosOriMouseClicked

    private void jListaStopWordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListaStopWordMouseClicked
		if (col.getStopWord() != null){
			try {
				String textoFichero = FileUtils.readFileToString(col.getStopWord(), "ISO-8859-1");
				jPaneVisorDocumentos.setContentType("text/plain");
				jPaneVisorDocumentos.setDocument(new DefaultStyledDocument());
//				jPaneVisorDocumentos.getDocument().putProperty("IgnoreCharsetDirective", true);
				jPaneVisorDocumentos.setText(textoFichero);
				jPaneVisorDocumentos.setCaretPosition(0);
			} catch (IOException ex) {
				Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
    }//GEN-LAST:event_jListaStopWordMouseClicked

    private void jListaContenidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListaContenidosMouseClicked
		jPaneAyuda.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("ayuda_cuadro_texto_contenido_" + jListaContenidos.getSelectedIndex()));
		jPaneAyuda.setCaretPosition(0);
    }//GEN-LAST:event_jListaContenidosMouseClicked

    private void bAyudaTokenizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAyudaTokenizacionActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdAyuda.setLocation(dim.width/2-jdAyuda.getSize().width/2, dim.height/2-jdAyuda.getSize().height/2);
		jListaContenidos.setSelectedIndex(1);
		jPaneAyuda.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("ayuda_cuadro_texto_contenido_1"));
		jPaneAyuda.setCaretPosition(0);
		jdAyuda.setVisible(true);
    }//GEN-LAST:event_bAyudaTokenizacionActionPerformed

    private void bAyudaStopWordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAyudaStopWordsActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdAyuda.setLocation(dim.width/2-jdAyuda.getSize().width/2, dim.height/2-jdAyuda.getSize().height/2);
		jListaContenidos.setSelectedIndex(2);
		jPaneAyuda.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("ayuda_cuadro_texto_contenido_2"));
		jPaneAyuda.setCaretPosition(0);
		jdAyuda.setVisible(true);
    }//GEN-LAST:event_bAyudaStopWordsActionPerformed

    private void bAyudaStemmingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAyudaStemmingActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdAyuda.setLocation(dim.width/2-jdAyuda.getSize().width/2, dim.height/2-jdAyuda.getSize().height/2);
		jListaContenidos.setSelectedIndex(3);
		jPaneAyuda.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("ayuda_cuadro_texto_contenido_3"));
		jPaneAyuda.setCaretPosition(0);
		jdAyuda.setVisible(true);
    }//GEN-LAST:event_bAyudaStemmingActionPerformed

    private void bAyudaIndexacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAyudaIndexacionActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdAyuda.setLocation(dim.width/2-jdAyuda.getSize().width/2, dim.height/2-jdAyuda.getSize().height/2);
		jListaContenidos.setSelectedIndex(5);
		jPaneAyuda.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("ayuda_cuadro_texto_contenido_5"));
		jPaneAyuda.setCaretPosition(0);
		jdAyuda.setVisible(true);
    }//GEN-LAST:event_bAyudaIndexacionActionPerformed

    private void bAyudaGraficasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAyudaGraficasActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdAyuda.setLocation(dim.width/2-jdAyuda.getSize().width/2, dim.height/2-jdAyuda.getSize().height/2);
		jListaContenidos.setSelectedIndex(6);
		jPaneAyuda.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("ayuda_cuadro_texto_contenido_6"));
		jPaneAyuda.setCaretPosition(0);
		jdAyuda.setVisible(true);
    }//GEN-LAST:event_bAyudaGraficasActionPerformed

    private void bAyudaVectorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAyudaVectorialActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdAyuda.setLocation(dim.width/2-jdAyuda.getSize().width/2, dim.height/2-jdAyuda.getSize().height/2);
		jListaContenidos.setSelectedIndex(7);
		jPaneAyuda.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("ayuda_cuadro_texto_contenido_7"));
		jPaneAyuda.setCaretPosition(0);
		jdAyuda.setVisible(true);
    }//GEN-LAST:event_bAyudaVectorialActionPerformed

    private void bAyudaBooleanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAyudaBooleanaActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdAyuda.setLocation(dim.width/2-jdAyuda.getSize().width/2, dim.height/2-jdAyuda.getSize().height/2);
		jListaContenidos.setSelectedIndex(8);
		jPaneAyuda.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("ayuda_cuadro_texto_contenido_8"));
		jPaneAyuda.setCaretPosition(0);
		jdAyuda.setVisible(true);
    }//GEN-LAST:event_bAyudaBooleanaActionPerformed

    private void bAyudaProbabilísticaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAyudaProbabilísticaActionPerformed
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		jdAyuda.setLocation(dim.width/2-jdAyuda.getSize().width/2, dim.height/2-jdAyuda.getSize().height/2);
		jListaContenidos.setSelectedIndex(9);
		jPaneAyuda.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("ayuda_cuadro_texto_contenido_9"));
		jPaneAyuda.setCaretPosition(0);
		jdAyuda.setVisible(true);
    }//GEN-LAST:event_bAyudaProbabilísticaActionPerformed

    private void jRadioAscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioAscActionPerformed
        crearGraficaTermino(jListaTerminos.getSelectedValue());
    }//GEN-LAST:event_jRadioAscActionPerformed

    private void jRadioDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioDescActionPerformed
        crearGraficaTermino(jListaTerminos.getSelectedValue());
    }//GEN-LAST:event_jRadioDescActionPerformed

    private void jListaDocumentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListaDocumentosMouseClicked
        if (jListaDocumentos.isSelectionEmpty() && jListaTerminos.isSelectionEmpty()){
            bGrafica.setEnabled(false);
        }else{
			try {
				crearGraficaDocumentos(jListaDocumentos.getSelectedIndex());
				bGrafica.setEnabled(true);
			} catch (IOException ex) {
				Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
			}
        }
    }//GEN-LAST:event_jListaDocumentosMouseClicked

    private void jListaTerminosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListaTerminosMouseClicked
        if (jListaTerminos.isSelectionEmpty() && jListaDocumentos.isSelectionEmpty()){
            bGrafica.setEnabled(false);
        }else{
            crearGraficaTermino(jListaTerminos.getSelectedValue());
            bGrafica.setEnabled(true);
        }
    }//GEN-LAST:event_jListaTerminosMouseClicked

    private void cargarDocumentoTokenizado(){
		if (docTokenizado < archivosOriginales.size()){
            File ArchivoOriginal = new File (archivosOriginales.get(docTokenizado).getRutaFichero());
            File ArchivoTok = new File (archivosTokenizados.get(docTokenizado).getRutaFichero());
            bSiguienteTokenizacion.setEnabled(true);
            bTerminarDocTokenizado.setEnabled(true);
            bSiguienteDocTokenizacion.setEnabled(true);
            bInicioTokenizacion.setEnabled(false);
            if (docTokenizado==archivosOriginales.size()-1){
				bSiguienteDocTokenizacion.setEnabled(false);
				bInicioTokenizacion.setEnabled(true);
            }
            tNomArchivo.setText(ArchivoOriginal.getName());
            tOriginal.setText("");
            tTokenizado.setText("");
            tamanoSeleccionadoTok = 0;
            try {
                AutoDetectParser parser = new AutoDetectParser();
				ParseContext parseContext = new ParseContext();
				Metadata metadata = new Metadata();
				BodyContentHandler ch = new BodyContentHandler(-1);
				//Parseamos el stream para el texto original
				FileInputStream input = new FileInputStream(ArchivoOriginal);
				parser.parse(input, ch, metadata, parseContext);
				textoOriginal = ch.toString();
				textoOriginal = textoOriginal.replaceAll("[\\’\\‘\\“\\”\\|,.'!?;:\\-–−•¡¿»«—\\+@#©²³†/…|ʊ°®=ª´\\&]", "$0 ").replaceAll("([\\p{Digit}]+)([\\p{Alpha}]+)", "$1 $2").replaceAll("\\+|\\s+", " ");
				tOriginal.setText(textoOriginal);
				tOriginal.setCaretPosition(0);
				//Parseamos el stream para el texto tokenizado
				input = new FileInputStream(ArchivoTok.getPath());
				ch = new BodyContentHandler(-1);
				parser.parse(input, ch, metadata, parseContext);
				textoTokenizado = ch.toString();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | SAXException | TikaException ex) {
		Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
		}
		docTokenizado++;
		bSiguienteTokenizacion.requestFocus();
    }
	
    private void siguienteTermTokenizado(){
		String palabraTokenizada, palabraOriginal;
		String terminacion = "";
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet asetAbajo;
		SimpleAttributeSet attrsAbajo = new SimpleAttributeSet();
		try {
            if (!textoTokenizado.trim().equals("")){
				palabraOriginal = textoOriginal.substring(0, textoOriginal.indexOf(' ')).trim();
				palabraTokenizada = textoTokenizado.substring(0, textoTokenizado.indexOf(' ')).trim();
				// Selección del texto del cuadro de arriba
				DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
				tOriginal.getHighlighter().addHighlight(tamanoSeleccionadoTok, tamanoSeleccionadoTok+ palabraOriginal.length(), highlightPainter);
				tOriginal.setCaretPosition(tamanoSeleccionadoTok);
				tamanoSeleccionadoTok += palabraOriginal.length()+1;
				// Si las dos palabras son iguales...
				if(palabraOriginal.trim().equals(palabraTokenizada.trim())){
//                    System.out.println("Compara: " + palabraOriginal.trim() + "(" + palabraOriginal.length() + ") - " + palabraTokenizada + "(" + palabraTokenizada.length() + ") Azul");
                    asetAbajo = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
                    StyleConstants.setBold(attrsAbajo, false);
                    attrsAbajo.addAttributes(asetAbajo);
                    tTokenizado.getStyledDocument().insertString(tTokenizado.getText().length(), palabraTokenizada, attrsAbajo);
                    textoOriginal = textoOriginal.substring(palabraOriginal.length()+1);
                    textoTokenizado = textoTokenizado.substring(palabraTokenizada.length()+1);
				}else{
                    // Si es la misma palabra pero se ha convertido a minúscula
                    if(StringUtils.stripAccents(palabraOriginal.toLowerCase().trim()).equals(palabraTokenizada.trim())){
			//			System.out.println("Compara: " + palabraOriginal.toLowerCase().trim() + "(" + palabraOriginal.length() + ") - " + palabraTokenizada + "(" + palabraTokenizada.length() + ") Verde");
						asetAbajo = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLUE);
						StyleConstants.setBold(attrsAbajo, true);
						attrsAbajo.addAttributes(asetAbajo);
						tTokenizado.getStyledDocument().insertString(tTokenizado.getText().length(), palabraTokenizada, attrsAbajo);
						textoOriginal = textoOriginal.substring(palabraOriginal.length()+1);
						textoTokenizado = textoTokenizado.substring(palabraTokenizada.length()+1);
                    }else{
			//			System.out.println("Compara: " + palabraOriginal.toLowerCase().trim() + "(" + palabraOriginal.length() + ") - " + palabraTokenizada + "(" + palabraTokenizada.length() + ")");
						String pre = "", palabra, post = "";
						palabra = palabraOriginal;
						Pattern ptt = Pattern.compile("^[\\p{Punct}–•¡¿»«—\\+\\-@#©/…|ʊ°®−=~“”²³†ª\\&\\’\\‘\\“\\”]+");
						Matcher m = ptt.matcher(palabra);
						if (m.find()){
                            pre = m.group(0);
						}
						palabra = m.replaceAll("");
						Pattern pt2 = Pattern.compile("[\\p{Punct}–•¡¿»«—+\\-@#©/.[()]…|ʊ°®−=~“”²³†ª\\&\\’\\‘\\“\\”]+");
						Matcher m2 = pt2.matcher(palabra);
						if (m2.find()){
                            post = m2.group(0);
						}
						palabra = m2.replaceAll("");
			//			System.out.println("Pre: " + pre + " Palabra: " + palabra + " Post: " + post);
						if (StringUtils.stripAccents(palabra.toLowerCase()).equals(palabraTokenizada)){
			//				System.out.println("Compara: " + pre + palabra + post + "(" + (palabra.length()+post.length()+pre.length()) + ") - " + palabraTokenizada + "(" + palabraTokenizada.length() + ") Multiple");
							StyleConstants.setBold(attrsAbajo, true);
							asetAbajo = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.RED);
							attrsAbajo.addAttributes(asetAbajo);					
							tTokenizado.getStyledDocument().insertString(tTokenizado.getText().length(), pre, attrsAbajo);
							if (palabra.equals(palabraTokenizada)){
								StyleConstants.setBold(attrsAbajo, false);
								asetAbajo = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
								attrsAbajo.addAttributes(asetAbajo);
								tTokenizado.getStyledDocument().insertString(tTokenizado.getText().length(), palabra.toLowerCase(), attrsAbajo);
							}else{
								asetAbajo = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLUE);
								StyleConstants.setBold(attrsAbajo, true);
								attrsAbajo.addAttributes(asetAbajo);
								tTokenizado.getStyledDocument().insertString(tTokenizado.getText().length(), palabra.toLowerCase(), attrsAbajo);
							}
							StyleConstants.setBold(attrsAbajo, true);
							asetAbajo = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.RED);
							attrsAbajo.addAttributes(asetAbajo);					
							tTokenizado.getStyledDocument().insertString(tTokenizado.getText().length(), post, attrsAbajo);
							textoOriginal = textoOriginal.substring(palabraOriginal.length()+1);
							textoTokenizado = textoTokenizado.substring(palabraTokenizada.length()+1);
							
						}else{// Si la palabra es diferente
//							System.out.println("Compara: " + palabraOriginal.trim() + "(" + palabraOriginal.trim().length() + ") - " + palabraTokenizada + "(" + palabraTokenizada.length() + ") Rojo");
							asetAbajo = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.RED);
							StyleConstants.setBold(attrsAbajo, true);
							attrsAbajo.addAttributes(asetAbajo);
							tTokenizado.getStyledDocument().insertString(tTokenizado.getText().length(), palabraOriginal.toLowerCase(), attrsAbajo);
							textoOriginal = textoOriginal.substring(palabraOriginal.length()+1);
						}
                    }
				}
				asetAbajo = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.RED);
				attrsAbajo.addAttributes(asetAbajo);					
				tTokenizado.getStyledDocument().insertString(tTokenizado.getText().length(), terminacion + " ", attrsAbajo);
			}else{
		//		System.out.println("Compara: " + palabraOriginal.trim() + "(" + palabraOriginal.trim().length() + ") - " + palabraTokenizada + "(" + palabraTokenizada.length() + ") Rojo");
				asetAbajo = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.RED);
				StyleConstants.setBold(attrsAbajo, true);
				attrsAbajo.addAttributes(asetAbajo);
				tTokenizado.getStyledDocument().insertString(tTokenizado.getText().length(), textoOriginal.toLowerCase() + " ", attrsAbajo);
				textoOriginal = "";
            }
		} catch (BadLocationException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (textoOriginal.trim().equals("")){
            bSiguienteTokenizacion.setEnabled(false);
            bTerminarDocTokenizado.setEnabled(false);
        }
    }
		
    private void cargarDocumentoStopWord() {
		if (docStopWord < archivosTokenizados.size()){
			File archivoTok = new File (archivosTokenizados.get(docStopWord).getRutaFichero());
			File archivoStopWord = new File (archivosStopWords.get(docStopWord).getRutaFichero());
			bSiguienteStopWord.setEnabled(true);
			bTerminarDocStopWord.setEnabled(true);
			bSiguienteDocStopWord.setEnabled(true);
			bInicioStopWords.setEnabled(false);
			if (docStopWord==archivosTokenizados.size()-1){
				bSiguienteDocStopWord.setEnabled(false);
				bInicioStopWords.setEnabled(true);
			}
			tNomArchivoStopWord.setText(archivoTok.getName());
			tStopWords.setText("");
			tTokenizadoSW.setText("");
			tamanoSeleccionadoSW = 0;
			try {
				AutoDetectParser parser = new AutoDetectParser();
				ParseContext parseContext = new ParseContext();
				Metadata metadata = new Metadata();
				BodyContentHandler ch = new BodyContentHandler(-1);
				//Parseamos el stream para el texto original
				FileInputStream input = new FileInputStream(archivoTok.getPath());
				parser.parse(input, ch, metadata, parseContext);
				textoTokeSW = ch.toString();
				tTokenizadoSW.setText(textoTokeSW);
				tTokenizadoSW.setCaretPosition(0);
				//Parseamos el stream para el texto tokenizado
				input = new FileInputStream(archivoStopWord.getPath());
				ch = new BodyContentHandler(-1);
				parser.parse(input, ch, metadata, parseContext);
				textoSW = ch.toString();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | SAXException | TikaException ex) {
                Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
		}
		docStopWord++;
		bSiguienteStopWord.requestFocus();
    }
	
    private void siguienteTermStopWords(){
		String palabraSW, palabraTokenizada;
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset;
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		try {
			if (!textoSW.trim().equals("")){
				palabraTokenizada = textoTokeSW.substring(0, textoTokeSW.indexOf(' '));
				palabraSW = textoSW.substring(0, textoSW.indexOf(' '));
				// Selección del texto del cuadro de arriba
				DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
				tTokenizadoSW.getHighlighter().addHighlight(tamanoSeleccionadoSW, tamanoSeleccionadoSW+palabraTokenizada.length(), highlightPainter);
				tamanoSeleccionadoSW += palabraTokenizada.length()+1;
				tTokenizadoSW.setCaretPosition(tamanoSeleccionadoSW);
				// Si las dos palabras son iguales...
				if(palabraTokenizada.trim().equals(palabraSW.trim())){
		//			System.out.println("Compara: " + palabraOriginal.trim() + "(" + palabraOriginal.length() + ") - " + palabraTokenizada + "(" + palabraTokenizada.length() + ") Azul");
					aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
					StyleConstants.setBold(attrs, false);
					attrs.addAttributes(aset);
					tStopWords.getStyledDocument().insertString(tStopWords.getText().length(), palabraTokenizada + " ", attrs);
					textoTokeSW = textoTokeSW.substring(palabraTokenizada.length()+1);
					textoSW = textoSW.substring(palabraTokenizada.length()+1);
				}else{	// La palabra se ha eliminado.
					aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.RED);
					StyleConstants.setBold(attrs, true);
					attrs.addAttributes(aset);
					tStopWords.getStyledDocument().insertString(tStopWords.getText().length(), palabraTokenizada + " ", attrs);
					textoTokeSW = textoTokeSW.substring(palabraTokenizada.length()+1);
				}
			}else{
				aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.RED);
				StyleConstants.setBold(attrs, true);
				attrs.addAttributes(aset);
				tStopWords.getStyledDocument().insertString(tStopWords.getText().length(), textoTokeSW, attrs);
				textoTokeSW  = "";
			}
		} catch (BadLocationException ex) {
			Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (textoTokeSW.trim().equals("")){
			bSiguienteStopWord.setEnabled(false);
			bTerminarDocStopWord.setEnabled(false);
		}
    }
	
    private void cargarDocumentoStemmingConSW(){
        if (docStemming < archivosTokenizados.size()){
            File archivoTok = new File (archivosTokenizados.get(docStemming).getRutaFichero());
            File archivoStemming = new File (archivosStemizado.get(docStemming).getRutaFichero());
            bSiguienteStemming.setEnabled(true);
            bTerminarDocStemming.setEnabled(true);
            bSiguienteDocStemming.setEnabled(true);
            bInicioStemming.setEnabled(false);
            if (docStemming==archivosTokenizados.size()-1){
                bSiguienteDocStemming.setEnabled(false);
				bInicioStemming.setEnabled(true);
            }
            tNomArchivoNoStemming.setText(archivoTok.getName());
            tNoStemizado.setText("");
            tStemizado.setText("");
            tamanoSeleccionadoStemming = 0;
            try {
                AutoDetectParser parser = new AutoDetectParser();
				ParseContext parseContext = new ParseContext();
				Metadata metadata = new Metadata();
				BodyContentHandler ch = new BodyContentHandler(-1);
				//Parseamos el stream para el texto original
				FileInputStream input = new FileInputStream(archivoTok);
				parser.parse(input, ch, metadata, parseContext);
				textoNoStemming = ch.toString();
				tNoStemizado.setText(textoNoStemming);
				tNoStemizado.setCaretPosition(0);
				//Parseamos el stream para el texto tokenizado
				input = new FileInputStream(archivoStemming);
				ch = new BodyContentHandler(-1);
				parser.parse(input, ch, metadata, parseContext);
				textoStemming = ch.toString();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | SAXException | TikaException ex) {
                Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	docStemming++;
	bSiguienteStemming.requestFocus();
    }
	
    private void cargarDocumentoStemmingSinSW(){
        if (docStemming < archivosStopWords.size()){
            File archivoStopWords = new File (archivosStopWords.get(docStemming).getRutaFichero());
            File archivoStemming = new File (archivosStemizado.get(docStemming).getRutaFichero());
            bSiguienteStemming.setEnabled(true);
            bTerminarDocStemming.setEnabled(true);
            bSiguienteDocStemming.setEnabled(true);
            bInicioStemming.setEnabled(false);
            if (docStemming==archivosStopWords.size()-1){
                bSiguienteDocStemming.setEnabled(false);
                bInicioStemming.setEnabled(true);
            }
            tNomArchivoNoStemming.setText(archivoStopWords.getName());
            tNoStemizado.setText("");
            tStemizado.setText("");
            tamanoSeleccionadoStemming = 0;
            try {
                AutoDetectParser parser = new AutoDetectParser();
		ParseContext parseContext = new ParseContext();
		Metadata metadata = new Metadata();
		BodyContentHandler ch = new BodyContentHandler(-1);
		//Parseamos el stream para el texto original
		FileInputStream input = new FileInputStream(archivoStopWords);
		parser.parse(input, ch, metadata, parseContext);
		textoNoStemming = ch.toString();
		tNoStemizado.setText(textoNoStemming);
		tNoStemizado.setCaretPosition(0);
		//Parseamos el stream para el texto tokenizado
		input = new FileInputStream(archivoStemming);
		ch = new BodyContentHandler(-1);
		parser.parse(input, ch, metadata, parseContext);
		textoStemming = ch.toString();
            } catch (FileNotFoundException ex) {
		Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | SAXException | TikaException ex) {
                Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
	docStemming++;
	bSiguienteStemming.requestFocus();
    }
	
    private void siguienteTermStemizado(){
        String palabraStemizada, palabraSinStemizar;
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset;
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		try {
			if (!textoNoStemming.trim().equals("")){
				palabraSinStemizar = textoNoStemming.substring(0, textoNoStemming.indexOf(' '));
				palabraStemizada = textoStemming.substring(0, textoStemming.indexOf(' '));
				// Selección del texto del cuadro de arriba
				DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
				tNoStemizado.getHighlighter().addHighlight(tamanoSeleccionadoStemming, tamanoSeleccionadoStemming+palabraSinStemizar.length(), highlightPainter);
				tamanoSeleccionadoStemming += palabraSinStemizar.length()+1;
				tNoStemizado.setCaretPosition(tamanoSeleccionadoStemming);
				// Si las dos palabras son iguales...
				if(palabraSinStemizar.trim().equals(palabraStemizada.trim())){
//                  System.out.println("Compara: " + palabraOriginal.trim() + "(" + palabraOriginal.length() + ") - " + palabraTokenizada + "(" + palabraTokenizada.length() + ") Azul");
					aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
					StyleConstants.setBold(attrs, false);
					attrs.addAttributes(aset);
					tStemizado.getStyledDocument().insertString(tStemizado.getText().length(), palabraStemizada + " ", attrs);
				}else{	// La palabra se ha eliminado.
					aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLUE);
					StyleConstants.setBold(attrs, true);
					attrs.addAttributes(aset);
					tStemizado.getStyledDocument().insertString(tStemizado.getText().length(), palabraStemizada + " ", attrs);
				}
				textoNoStemming = textoNoStemming.substring(palabraSinStemizar.length()+1);
				textoStemming = textoStemming.substring(palabraStemizada.length()+1);
			}else{
				aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLUE);
				StyleConstants.setBold(attrs, true);
				attrs.addAttributes(aset);
				tStemizado.getStyledDocument().insertString(tStemizado.getText().length(), textoNoStemming, attrs);
				textoNoStemming = "";
			}
		} catch (BadLocationException ex) {
			Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (textoNoStemming.trim().equals("")){
            bSiguienteStemming.setEnabled(false);
            bTerminarDocStemming.setEnabled(false);
		}
    }
	
    public void rellenarIndiceCompleto() throws IOException{
		DefaultTableModel dtm = new DefaultTableModel(){
			Class[] types = new Class [] {
				java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Float.class
			};
			@Override
			public Class getColumnClass(int columnIndex) {
				return types [columnIndex];
			}
		};
		jTablaIndiceCompleto.setModel(dtm);
		
		String nombreTermino = ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("label_columna_termino");
		String numDocumentos = ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("label_columna_numDocumentos");
		String numOcurrencias = ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("label_columna_numOcurrencias");
		
		String[] header = new String[] { "Rank", nombreTermino, numDocumentos, numOcurrencias, "Idf"};
		dtm.setColumnIdentifiers(header);
		
		Path p = Paths.get(col.getRutaColeccion() + "indice/");  // Indicamos la carpeta del índice
        Directory directory = FSDirectory.open(p); // Almacenamos el índice en el directorio
		IndexReader ireader = DirectoryReader.open(directory);
		TFIDFSimilarity tfidfsimilarity = new ClassicSimilarity();

		Terms terminos = MultiFields.getTerms(ireader, "text");
		TermsEnum termino = terminos.iterator();
		int numTerms = 1;
		BytesRef rf = termino.next();
		Termino ter;
		jListaTerminos.setModel(dlmTerminos);
		while (rf!=null){
			ter = new Termino(ireader.numDocs());
			Term t = new Term("text", rf.utf8ToString());	
			long freqTerm = ireader.totalTermFreq(t);		// Ocurrencias	
			String term = rf.utf8ToString();				// Término
			BigDecimal bd = new BigDecimal(Float.toString(tfidfsimilarity.idf(ireader.docFreq(t), ireader.numDocs())));
			bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
			float idf = bd.floatValue();					// Idf
			int freq = ireader.docFreq(t);					// Documentos en los que aparece el término
            Object[] fila = {numTerms, term, freq , freqTerm, idf};
            dtm.addRow(fila);
			numTerms++;
			rf = termino.next();
			dlmTerminos.addElement(term);
		}
		jListaDocumentos.setModel(dlmDocumentos);
		for (int i = 0; i<indice.getNumDocs(); i++){
            dlmDocumentos.addElement(i + " - " + indice.getNombreFichero(i));
			jcNumDocVectorial.addItem("" + (i+1));
			jcNumDocProb.addItem("" + (i+1));
			jcNumDocBooleana.addItem("" + (i+1));
		}
		jcNumDocVectorial.setSelectedIndex(jcNumDocVectorial.getItemCount()-1);
		jcNumDocProb.setSelectedIndex(jcNumDocProb.getItemCount()-1);
		jcNumDocBooleana.setSelectedIndex(jcNumDocBooleana.getItemCount()-1);
		lDirectorioIndice.setText(indice.getDirectory());
		lNumDocumentos.setText("" + indice.getNumDocs());
		lVersionIndice.setText("" + indice.getVersion());
		lNumTerminos.setText("" + (numTerms-1));
    }
		
	
    public void crearGraficaTermino(String term){
		try {
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			ArrayList<Pair<Integer, Integer>> frecuencias = new ArrayList<>();
			chart  = ChartFactory.createBarChart(jListaTerminos.getSelectedValue(),"", "", dataset, PlotOrientation.VERTICAL, true, true, false);
			CategoryPlot plot = chart.getCategoryPlot();		
			Path p = Paths.get(col.getRutaColeccion() + "indice/");  // Indicamos la carpeta del índice
			Directory directory = FSDirectory.open(p); // Almacenamos el índice en el directorio
			IndexReader ireader = DirectoryReader.open(directory);
			
			PostingsEnum docEnum = MultiFields.getTermDocsEnum(ireader, "text", new BytesRef(term));
			while (docEnum.nextDoc() != PostingsEnum.NO_MORE_DOCS){
				frecuencias.add(new Pair(docEnum.freq(), docEnum.docID()));
			}
			Collections.sort(frecuencias, Comparator.comparing(freq -> freq.getKey()));
	
			if (jRadioDesc.isSelected()){
				Collections.reverse(frecuencias);
			}
			
			frecuencias.forEach((freq) -> {
				dataset.addValue(freq.getKey(), "Docs", freq.getValue());
			});
			
			org.jfree.chart.axis.CategoryAxis xAxis = plot.getDomainAxis();
			xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
			xAxis.setLowerMargin(0.01);      // Margenes respecto al borde izquierdo y derecho de la gráfica
			xAxis.setUpperMargin(0.01);
			//        xAxis.setCategoryMargin(0.01);
			xAxis.setMaximumCategoryLabelLines(3);

			ValueAxis rangeAxis = plot.getRangeAxis();
			rangeAxis.setAutoRange(true); // Y-Axis range will be set automatically based on the supplied data
			rangeAxis.setLabel("Freq");


			BarRenderer renderer = (BarRenderer)plot.getRenderer();
			renderer.setMaximumBarWidth(0.01);  // Anchura máxima de la barra
			renderer.setBaseItemLabelsVisible(true);
			renderer.setItemMargin(1);

			ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.BOTTOM_CENTER);
			renderer.setPositiveItemLabelPositionFallback(position);


			ChartPanel chartPanel = new ChartPanel(chart, true, true, true, true, true);
			chartPanel.setAutoscrolls(true);

			jPanelGrafica.removeAll();
			jPanelGrafica.setLayout(new BorderLayout());
			jPanelGrafica.setAutoscrolls(true);
			jPanelGrafica.add(chartPanel, BorderLayout.CENTER);
			jPanelGrafica.validate();
		} catch (IOException ex) {
			Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
	
    public void crearGraficaDocumentos(int idDoc) throws IOException{
        ArrayList<Double> listaFrecuencias = new ArrayList<>();
        XYSeriesCollection dataset = new XYSeriesCollection();
        chart = ChartFactory.createXYLineChart(jListaDocumentos.getSelectedValue(), "Terms", "Freq", dataset, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart, true, true, true, true, true);
        XYSplineRenderer renderer = new XYSplineRenderer(1);
        
        XYSeries xy = new XYSeries("Term");

		
		Path p = Paths.get(col.getRutaColeccion() + "indice/");  // Indicamos la carpeta del índice
		Directory directory = FSDirectory.open(p); // Almacenamos el índice en el directorio
		IndexReader ireader = DirectoryReader.open(directory);
		
		Terms terms = ireader.getTermVector(idDoc, "text");
		TermsEnum it = terms.iterator();
		BytesRef bytesRef = it.next();
		while(bytesRef != null){
			listaFrecuencias.add(Math.log(it.totalTermFreq())+1);
			bytesRef = it.next();
		}
        Collections.sort(listaFrecuencias);
        Collections.reverse(listaFrecuencias);
        for (int i = 0; i < listaFrecuencias.size(); i++){
            xy.add(i, listaFrecuencias.get(i));
        }
        
        renderer.setSeriesShapesVisible(0, false);
        dataset.addSeries(xy);
        chart.getXYPlot().setRenderer(renderer);
        
        chartPanel.setAutoscrolls(true);
     
        jPanelGrafica.removeAll();
        jPanelGrafica.setLayout(new BorderLayout());
        jPanelGrafica.setAutoscrolls(true);
        jPanelGrafica.add(chartPanel, BorderLayout.CENTER);
        jPanelGrafica.validate();
	}
	
	public void guardarGrafica(){
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".png", "png", "png");
		jFileChooserGuardaGrafica.setFileFilter(filter);
		int seleccion_archivo = jFileChooserGuardaGrafica.showSaveDialog(this);
		if (seleccion_archivo == jFileChooserGuardaGrafica.APPROVE_OPTION){
			try {
				File file = new File(jFileChooserGuardaGrafica.getSelectedFile()+".png");
				ChartUtilities.saveChartAsPNG(file, chart, 1500, 768);
			} catch (IOException ex) {
				Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	public void activarPaneles(int panel){
		jOpciones.setEnabledAt(panel, true);
		jOpciones.setSelectedIndex(panel);
	}

	
	private class HebraIndice extends Thread{
		public HebraIndice(){
//			System.out.println("Se crea la hebra");
		}
	
		@Override
		public void run() {
			try {
				ActionEvent e = null;
				bCrearIndice.setEnabled(false);
				indice = new Index();
				indice.createIndexWriter();
				for (int i = 0; i<col.getArchivoStemizados().size(); i++){
					indice.createIndex(col.getArchivoStemizados().get(i));
					jProgreso.setValue((i*100/col.getArchivoStemizados().size())-1);
					lEstadoIndice.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("label_estadoIndice_indexando") + new File(col.getArchivoStemizados().get(i).getRutaFichero()).getName());
				}
				indice.cerrarIndexWriter();
				lEstadoIndice.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("label_estadoIndice_rellenando"));
				indice.createIndexReader();
				rellenarIndiceCompleto();
				lEstadoIndice.setText("");
				jProgreso.setValue(100);
				jTablaIndiceCompleto.setAutoCreateRowSorter(true);
				bContinuar.setEnabled(true);
			} catch (IOException | SAXException | TikaException ex) {
				Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	};
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem bAcercaDe;
    private javax.swing.JMenu bArchivo;
    private javax.swing.JMenuItem bAyuda;
    private javax.swing.JButton bAyudaBooleana;
    private javax.swing.JButton bAyudaGraficas;
    private javax.swing.JButton bAyudaIndexacion;
    private javax.swing.JButton bAyudaProbabilística;
    private javax.swing.JButton bAyudaStemming;
    private javax.swing.JButton bAyudaStopWords;
    private javax.swing.JButton bAyudaTokenizacion;
    private javax.swing.JButton bAyudaVectorial;
    private javax.swing.JButton bBusqueda;
    private javax.swing.JButton bBusquedaBooleana;
    private javax.swing.JButton bBusquedaProb;
    private javax.swing.JButton bBusquedaVectorial;
    private javax.swing.JButton bContinuar;
    private javax.swing.JButton bCrearIndice;
    private javax.swing.JMenuItem bDocOrig;
    private javax.swing.JButton bExplanationProb;
    private javax.swing.JButton bExplanationVec;
    private javax.swing.JButton bGrafica;
    private javax.swing.JMenuItem bInicio;
    private javax.swing.JButton bInicioStemming;
    private javax.swing.JButton bInicioStopWords;
    private javax.swing.JButton bInicioTokenizacion;
    private javax.swing.JMenuItem bSalir;
    private javax.swing.JLabel bSeEliminaSW;
    private javax.swing.JButton bSiguienteDocStemming;
    private javax.swing.JButton bSiguienteDocStopWord;
    private javax.swing.JButton bSiguienteDocTokenizacion;
    private javax.swing.JButton bSiguientePasoStopWord;
    private javax.swing.JButton bSiguientePasoTokenizacion;
    private javax.swing.JButton bSiguienteStemming;
    private javax.swing.JButton bSiguienteStopWord;
    private javax.swing.JButton bSiguienteTokenizacion;
    private javax.swing.JLabel bSinCambiosSW;
    private javax.swing.JMenu bSulairl;
    private javax.swing.JButton bTerminarDocStemming;
    private javax.swing.JButton bTerminarDocStopWord;
    private javax.swing.JButton bTerminarDocTokenizado;
    private javax.swing.ButtonGroup groupOrdenaGrafica;
    private javax.swing.JButton jButton1;
    private javax.swing.JDialog jDialogGuardaGrafica;
    private javax.swing.JFileChooser jFileChooserGuardaGrafica;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jListaArchivosOri;
    private javax.swing.JList<String> jListaContenidos;
    private javax.swing.JList<String> jListaDocumentos;
    private javax.swing.JList<String> jListaStopWord;
    private javax.swing.JList<String> jListaTerminos;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JTabbedPane jOpciones;
    private javax.swing.JTextPane jPaneAyuda;
    private javax.swing.JTextPane jPaneVisorDocumentos;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelGrafica;
    private javax.swing.JPanel jPanelIndice;
    private javax.swing.JProgressBar jProgreso;
    private javax.swing.JRadioButton jRadioAsc;
    private javax.swing.JRadioButton jRadioDesc;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JScrollPane jScrollPaneGrafica;
    private javax.swing.JTable jTablaDocBooleana;
    private javax.swing.JTable jTablaDocProb;
    private javax.swing.JTable jTablaDocVectorial;
    private javax.swing.JTable jTablaIndiceCompleto;
    private javax.swing.JTable jTablaPosiciones;
    private javax.swing.JComboBox<String> jcNumDocBooleana;
    private javax.swing.JComboBox<String> jcNumDocProb;
    private javax.swing.JComboBox<String> jcNumDocVectorial;
    private javax.swing.JDialog jdAcercaDe;
    private javax.swing.JDialog jdAyuda;
    private javax.swing.JDialog jdCreacionIndice;
    private javax.swing.JDialog jdDetalles;
    private javax.swing.JDialog jdVisorDocumentos;
    private javax.swing.JLabel lArchivoStemmingIndice;
    private javax.swing.JLabel lBusquedaBooleana;
    private javax.swing.JLabel lBusquedaProb;
    private javax.swing.JLabel lBusquedaVectorial;
    private javax.swing.JLabel lConsBooleanaLucene;
    private javax.swing.JLabel lConsResBooleana;
    private javax.swing.JLabel lConsResProb;
    private javax.swing.JLabel lConsResVectorial;
    private javax.swing.JLabel lDirectorioIndice;
    private javax.swing.JLabel lDocRecBooleana;
    private javax.swing.JLabel lDocRecProb;
    private javax.swing.JLabel lDocRecVectorial;
    private javax.swing.JLabel lEstadoIndice;
    private javax.swing.JLabel lNombreBooleana;
    private javax.swing.JLabel lNombreProb;
    private javax.swing.JLabel lNombreVectorial;
    private javax.swing.JLabel lNormalizacion;
    private javax.swing.JLabel lNumDocBooleana;
    private javax.swing.JLabel lNumDocIndice;
    private javax.swing.JLabel lNumDocProb;
    private javax.swing.JLabel lNumDocVectorial;
    private javax.swing.JLabel lNumDocumentos;
    private javax.swing.JLabel lNumTerminos;
    private javax.swing.JLabel lNumTerminosIndice;
    private javax.swing.JLabel lRecBooleana;
    private javax.swing.JLabel lRecProb;
    private javax.swing.JLabel lRecVectorial;
    private javax.swing.JLabel lRutaIndice;
    private javax.swing.JLabel lSaturacion;
    private javax.swing.JLabel lSeEliminaTok;
    private javax.swing.JLabel lSeLematizaSt;
    private javax.swing.JLabel lSeModificaTok;
    private javax.swing.JLabel lSinCambioTok;
    private javax.swing.JLabel lSinCambiosSt;
    private javax.swing.JLabel lTfxIdf;
    private javax.swing.JLabel lTiempoBooleana;
    private javax.swing.JLabel lTiempoProb;
    private javax.swing.JLabel lTiempoVectorial;
    private javax.swing.JLabel lTimeBool;
    private javax.swing.JLabel lTimeProb;
    private javax.swing.JLabel lTimeVec;
    private javax.swing.JLabel lVersionIndice;
    private javax.swing.JLabel lVersionIndice1;
    private javax.swing.JPanel pBusquedaBooleana;
    private javax.swing.JPanel pBusquedaProb;
    private javax.swing.JPanel pBusquedaVectorial;
    private javax.swing.JPanel pConLemas;
    private javax.swing.JPanel pConStopWords;
    private javax.swing.JPanel pContenidos;
    private javax.swing.JPanel pDescripcionContenidos;
    private javax.swing.JPanel pDocBooleana;
    private javax.swing.JPanel pDocProb;
    private javax.swing.JPanel pDocVectorial;
    private javax.swing.JPanel pGraficas;
    private javax.swing.JPanel pGraficasDocumentos;
    private javax.swing.JPanel pGraficasTerminos;
    private javax.swing.JPanel pIndiceCompleto;
    private javax.swing.JPanel pInformacionIndice;
    private javax.swing.JPanel pInformacionTermino;
    private javax.swing.JPanel pInformacionTerminoDocumento;
    private javax.swing.JPanel pLematizacion;
    private javax.swing.JPanel pLeyendaStemming;
    private javax.swing.JPanel pLeyendaStopWords;
    private javax.swing.JPanel pLeyendaTokenizacion;
    private javax.swing.JPanel pOriginal;
    private javax.swing.JPanel pSinLemas;
    private javax.swing.JPanel pSinStopWords;
    private javax.swing.JPanel pStopWords;
    private javax.swing.JPanel pTablaBooleana;
    private javax.swing.JPanel pTablaProb;
    private javax.swing.JPanel pTablaVectorial;
    private javax.swing.JPanel pTokenizacion;
    private javax.swing.JPanel pTokenizado;
    private javax.swing.JPanel pVisorArchivoSW;
    private javax.swing.JPanel pVisorArchivosOriginales;
    private javax.swing.JPanel pVisorDescripcion;
    private javax.swing.JPanel pVisualizacionBooleana;
    private javax.swing.JPanel pVisualizacionProb;
    private javax.swing.JPanel pVisualizacionVectorial;
    private javax.swing.JTextField tBusquedaBooleana;
    private javax.swing.JTextField tBusquedaProb;
    private javax.swing.JTextField tBusquedaVectorial;
    private javax.swing.JTextField tConsBoolLucene;
    private javax.swing.JTextField tConsResBooleana;
    private javax.swing.JTextField tConsResProb;
    private javax.swing.JTextField tConsResVectorial;
    private javax.swing.JPanel tConsultaBool;
    private javax.swing.JPanel tConsultaProb;
    private javax.swing.JPanel tConsultaVec;
    private javax.swing.JTextField tDocBooleana;
    private javax.swing.JTextField tDocProb;
    private javax.swing.JTextField tDocVectorial;
    private javax.swing.JTextPane tExplanation;
    private javax.swing.JPanel tIndexacion;
    private javax.swing.JTabbedPane tIndice;
    private javax.swing.JTextPane tNoStemizado;
    private javax.swing.JTextField tNomArchivo;
    private javax.swing.JTextField tNomArchivoNoStemming;
    private javax.swing.JTextField tNomArchivoStopWord;
    private javax.swing.JTextField tNormalizacion;
    private javax.swing.JTextPane tOriginal;
    private javax.swing.JTextPane tPanelDocBooleana;
    private javax.swing.JTextPane tPanelDocProb;
    private javax.swing.JTextPane tPanelDocVectorial;
    private javax.swing.JPanel tPreprocesado;
    private javax.swing.JTextField tSaturacion;
    private javax.swing.JTextPane tStemizado;
    private javax.swing.JTextPane tStopWords;
    private javax.swing.JTextPane tTextoIndice;
    private javax.swing.JTabbedPane tTokenizacion;
    private javax.swing.JTextPane tTokenizado;
    private javax.swing.JTextPane tTokenizadoSW;
    // End of variables declaration//GEN-END:variables

	private void cambiarIdioma(){
		
		// Preprocesamiento
		
		jOpciones.setTitleAt(0, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_preprocesamiento"));
		
			// Tokenización

			jLabel1.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_archivo"));
			tTokenizacion.setTitleAt(0, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_tokenizacion_tokenizacion"));
			pOriginal.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_tokenizacion_archivo_original")));
			pTokenizado.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_tokenizacion_archivo_tokenizado")));
			bInicioTokenizacion.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_iniciar_tokenizacion"));
			bSiguienteTokenizacion.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_siguiente_termino"));
			bTerminarDocTokenizado.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_terminar_documento"));
			bSiguienteDocTokenizacion.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_siguiente_doc"));
			bSiguientePasoTokenizacion.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_siguente_paso"));
			pLeyendaTokenizacion.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_leyenda_preprocesamiento")));
			lSeEliminaTok.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_se_elimina_preprocesamiento"));
			lSeModificaTok.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_se_modifica_preprocesamiento"));
			lSinCambioTok.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_sin_cambios_preprocesamiento"));

			// palabras vacías
			
			tTokenizacion.setTitleAt(1, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_tokenizacion_stopwords"));
			jLabel2.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_archivo"));
			pConStopWords.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_palabrasVacias_conSW")));
			pSinStopWords.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_palabrasVacias_sinSW")));
			bInicioStopWords.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_iniciar_SW"));
			bSiguienteStopWord.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_siguiente_termino"));
			bTerminarDocStopWord.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_terminar_documento"));
			bSiguienteDocStopWord.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_siguiente_doc"));
			bSiguientePasoStopWord.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_siguente_paso"));
			pLeyendaStopWords.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_leyenda_preprocesamiento")));
			bSinCambiosSW.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_sin_cambios_preprocesamiento"));
			bSeEliminaSW.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_se_elimina_preprocesamiento"));
			
			// Stemming
			
			tTokenizacion.setTitleAt(2, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_tokenizacion_stemming"));
			jLabel3.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_archivo"));
			pConLemas.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_stemming_conLemas")));
			pSinLemas.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_stemming_sinLemas")));
			bInicioStemming.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_inicio_stemming"));
			bSiguienteStemming.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_siguiente_termino"));
			bTerminarDocStemming.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_terminar_documento"));
			bSiguienteDocStemming.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_siguiente_doc"));
			bCrearIndice.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_crear_indice"));
			pLeyendaStemming.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_leyenda_preprocesamiento")));
			lSinCambiosSt.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_sin_cambios_preprocesamiento"));
			lSeLematizaSt.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_stemming_se_lematiza"));
			
			// Ventana de creación del índice
			
			jdCreacionIndice.setTitle(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("titulo_creacion_indice"));
			jLabel19.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label19_creacion_indice"));
			jLabel20.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label20_creacion_indice"));
			jLabel21.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label21_creacion_indice"));
			jLabel22.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label22_creacion_indice"));
			bContinuar.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("boton_continuar"));
			
			
		// Indexación
		
		jOpciones.setTitleAt(1, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_indexacion"));
			
			// Indice completo
			
			tIndice.setTitleAt(0, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_indice_indiceCompleto"));
			pIndiceCompleto.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_indexacion_indiceCompleto")));
			pInformacionTermino.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_indexacion_informacionTermino")));
			pInformacionIndice.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_indexacion_informacionIndice")));
			pInformacionTerminoDocumento.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_indexacion_informacionTerminoDocumento")));
			lRutaIndice.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_indexacion_rutaIndice"));
			lNumDocIndice.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_indexacion_numDocIndice"));
			lNumTerminosIndice.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_indexacion_numTerminosIndice"));
			lVersionIndice1.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_indexacion_versionIndice"));
			jLabel15.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_archivo"));
			
			
			// Gráficas
			
			tIndice.setTitleAt(1, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_indice_graficas"));
			pGraficas.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_indexacion_graficas")));
			pGraficasDocumentos.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_indexacion_graficasDocumentos")));
			pGraficasTerminos.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_indexacion_graficasTerminos")));
			bGrafica.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("boton_indexacion_guardar_grafica"));
	

			// Busquedas

			// Vectorial
			
			jOpciones.setTitleAt(2, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_busqueda_vectorial"));
			pBusquedaVectorial.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_busqueda_buscar")));
			pVisualizacionVectorial.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_busqueda_visualizacionBusqueda")));
			pTablaVectorial.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_busqueda_documentos")));
			pDocVectorial.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_busqueda_documento")));
			lBusquedaVectorial.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_busqueda_buscar"));
			bBusquedaVectorial.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_busqueda_buscar"));
			lNumDocVectorial.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_numDocumentos"));
			lTiempoVectorial.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_tiempoRec"));
			lRecVectorial.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_documentosRec"));
			lConsResVectorial.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_consultaResultante"));
			lNombreVectorial.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_archivo"));
			bExplanationVec.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_busqueda_explanation"));
			
			// Booleana
			
			jOpciones.setTitleAt(3, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_busqueda_booleana"));
			pBusquedaBooleana.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_busqueda_buscar")));
			pVisualizacionBooleana.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_busqueda_visualizacionBusqueda")));
			pTablaBooleana.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_busqueda_documentos")));
			pDocBooleana.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_busqueda_documento")));
			lBusquedaBooleana.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_busqueda_buscar"));
			bBusquedaBooleana.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_busqueda_buscar"));
			lNumDocBooleana.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_numDocumentos"));
			lTiempoBooleana.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_tiempoRec"));
			lRecBooleana.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_documentosRec"));
			lConsResBooleana.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_consultaResultante"));
			lNombreBooleana.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_archivo"));
			lConsBooleanaLucene.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_busqueda_booleana_lucene"));
			
			// Probabilística

			jOpciones.setTitleAt(4, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_busqueda_probabilistica"));
			pBusquedaProb.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_busqueda_buscar")));
			pVisualizacionProb.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_busqueda_visualizacionBusqueda")));
			pTablaProb.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_busqueda_documentos")));
			pDocProb.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_busqueda_documento")));
			lBusquedaProb.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_busqueda_buscar"));
			bBusquedaProb.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_busqueda_buscar"));
			lNumDocProb.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_numDocumentos"));
			lTiempoProb.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_tiempoRec"));
			lRecProb.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_documentosRec"));
			lConsResProb.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_consultaResultante"));
			lNombreProb.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_archivo"));
			bExplanationProb.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_tab_busqueda_explanation"));
			lSaturacion.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_probabilistica_saturacion"));
			lNormalizacion.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_busqueda_probabilistica_normalizacion"));
			
			// Panel de ayuda.
			
			dlmListaContenidos.clear();
			jListaContenidos.setModel(dlmListaContenidos);
			for (int i = 0; i < 11; i++){
				dlmListaContenidos.add(i, ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("lista_ayuda_contenido_"+i));
			}
			
			pContenidos.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_ayuda_contenidos")));
			pDescripcionContenidos.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_ayuda_descripcion")));
			
			bSalir.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("label_menuitem_salir"));
			bInicio.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("label_menuitem_inicio"));
			bAyuda.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("label_menuitem_ayuda"));
			bDocOrig.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("label_menuitem_docOrig"));
			bAcercaDe.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("label_menuitem_acercaDe"));
			bArchivo.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("label_menuitem_bArchivo"));
	}
}
