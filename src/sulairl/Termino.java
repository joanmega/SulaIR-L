package sulairl;

/**
 * @author Jose Antonio Medina Garcia
 */

public class Termino {

    private int rank;				// Identificador del termino.
    private String term;			// Termino.
    private long ocurrencias;		// Numero de ocurrencias en la coleccion.
    private float idf;				// Idf del termino.
    private int freq;				// Numero de documentos en el que aparece el termino.

    /**
     * Constructor por defecto de un objeto termino del tipo Termino
     */

    public Termino() {
            rank = 0;
            term = "";
            ocurrencias = 0;
            idf = 0;
            freq = 0;
    }	

    /**
     * Constructor que crea un array de frecuencias de terminos del tamaño al numero de documentos.
     * @param numDocs Tamaño del vector de frecuencias igual al n�mero de documentos.
     */

    public Termino(int numDocs) {
            rank = 0;
            term = "";
            ocurrencias = 0;
            idf = 0;
            freq = 0;
    }

    /**
     * Constructor por par�metros de un objeto t�rmino del tipo T�rmino
     * @param unRank Un int que es el n�mero del t�rmino en la colecci�n.
     * @param unTerm Un String que determina dicho t�rmino.
     * @param unasOcurrencias Un long que determina el n�mero de veces que aparece el t�rmino en la colecci�n.
     * @param unIdf Un float que determina el valor del Idf del t�rmino en la colecci�n
     * @param unaFreq Un int que determina el n�mero de documentos en los que aparece el t�rmino.
     * @param unaFreqDoc Un array que contiene la frecuencia del t�rmino por documento.
     */

    public Termino(int unRank, String unTerm, long unasOcurrencias, float unIdf, int unaFreq, int[] unaFreqDoc) {
            rank = unRank;
            term = unTerm;
            ocurrencias = unasOcurrencias;
            idf = unIdf;
            freq = unaFreq;
    }

    /**
     * Constructor por par�metros de un objeto t�rmino del tipo T�rmino
     * @param unRank Un int que es el n�mero del t�rmino en la colecci�n.
     * @param unTerm Un String que determina dicho t�rmino.
     * @param unasOcurrencias Un long que determina el n�mero de veces que aparece el t�rmino en la colecci�n.
     * @param unIdf Un float que determina el valor del Idf del t�rmino en la colecci�n
     * @param unaFreq Un int que determina el n�mero de documentos en los que aparece el t�rmino.
     * @param numDocs N�mero de documentos que componen la colecci�n de documentos.
     */

    public Termino(int unRank, String unTerm, long unasOcurrencias, float unIdf, int unaFreq, int numDocs) {
            rank = unRank;
            term = unTerm;
            ocurrencias = unasOcurrencias;
            idf = unIdf;
            freq = unaFreq;
    }

    /**
     * Devuelve el valor del ID del t�rmino en el �ndice.
     * @return Un entero con el identificador del t�rmino en el �ndice.
     */

    public int getRank(){
            return rank;
    }

    /**
     * Devuelve el t�rmino.
     * @return Un String con el termino.
     */

    public String getTerm(){
            return term;
    }

    /**
     * Devuelve el n�mero de ocurrencias que tiene el t�rmino en la colecci�n.
     * @return Un long con el valor de la ocurrencias.
     */

    public long getOcurrencias(){
            return ocurrencias;
    }

    /**
     * Devuelve el valor del idf del t�rmino en la colecci�n.
     * @return Un float con el valor del idf en la colecci�n.
     */

    public float getIdf(){
            return idf;
    }

    /**
     * Devuelve el n�mero de documentos en los que aparece el t�rmino.
     * @return Un int con el n�mero de documentos en los que aparece el t�rmino.
     */

    public int getFreq(){
            return freq;
    }

    /**
     * Establece el rank (ID) para un t�rmino dado.
     * @param unRank Un int con el valor del rank del t�rmino.
     */

    public void setRank(int unRank){
            rank = unRank;
    }

    /**
     * Establece el valor del t�rmino.
     * @param unTerm Un String con el valor del t�rmino.
     */

    public void setTerm(String unTerm){
            term = unTerm;
    }

    /**
     * Establece el n�mero de ocurrencias del t�rmino en la colecci�n.
     * @param unasOcurrencias Un int con el valor de la ocurrencias del t�rmino.
     */

    public void setOcurrencias(long unasOcurrencias){
            ocurrencias = unasOcurrencias;
    }

    /**
     * Establece el valor del Idf de un t�rmino calculado previamente.
     * @param unIdf Un int con el valor del Idf del t�rmino.
     */

    public void setIdf(float unIdf){
            idf = unIdf;
    }
	
	/**
	 * Imprime la informaci�n obtenida del �ndice sobre un t�rmino.
	 */

    void imprimirTermino() {
        System.out.println("-------------------------------------------");
        System.out.println("Rank: " + getRank() + " Termino: " + getTerm());
        System.out.println("Ocurrencias: " + getOcurrencias());
        System.out.println("Idf: " + getIdf());
        System.out.println("Num Doc: " + getFreq());
    }
}