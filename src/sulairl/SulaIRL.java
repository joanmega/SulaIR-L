package sulairl;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

/**
 * @author José Antonio Medina García
 */


public class SulaIRL {

	public static String idioma = "es";
	public static ArchivoConf archivoConf = new ArchivoConf();
	public static Index indice = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception{
		// Cambiamos el tema del proyecto
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(SulaIRL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		
		// Creamos el frame de bienvenida y lo centramos en la pantalla.
        Principal p = new Principal();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        p.setLocation(dim.width/2-p.getSize().width/2, dim.height/2-p.getSize().height/2);
        p.setVisible(true);
	}
}
