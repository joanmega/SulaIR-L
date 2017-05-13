package sulairl;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.border.TitledBorder;
import sun.awt.AppContext;

/**
 * @author José Antonio Medina García
 */

public class Principal extends javax.swing.JFrame {
	
    /**
     * Creates new form Principal
     */
    public Principal() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgIdioma = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lBienvenida = new javax.swing.JLabel();
        lDesc1 = new javax.swing.JLabel();
        lDesc2 = new javax.swing.JLabel();
        pIdioma = new javax.swing.JPanel();
        rbEspanol = new javax.swing.JRadioButton();
        rbIngles = new javax.swing.JRadioButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        bEntrar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SulaIR-L");
        setResizable(false);

        jPanel1.setMaximumSize(new java.awt.Dimension(387, 384));
        jPanel1.setMinimumSize(new java.awt.Dimension(387, 384));
        jPanel1.setPreferredSize(new java.awt.Dimension(387, 384));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/Logo2.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        lBienvenida.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        lBienvenida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lBienvenida.setText("Bienvenid@ a SulaIR-L");

        lDesc1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lDesc1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lDesc1.setText("Una aplicación para el aprendizaje y la enseñanza ");

        lDesc2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lDesc2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lDesc2.setText("de la Recuperación de Información basada en Lucene");

        pIdioma.setBorder(javax.swing.BorderFactory.createTitledBorder("Idioma"));

        bgIdioma.add(rbEspanol);
        rbEspanol.setSelected(true);
        rbEspanol.setText("Español");
        rbEspanol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbEspanolActionPerformed(evt);
            }
        });

        bgIdioma.add(rbIngles);
        rbIngles.setText("Inglés");
        rbIngles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbInglesActionPerformed(evt);
            }
        });

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/FLGSPAIN.png"))); // NOI18N
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
        });

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/FLGUK.png"))); // NOI18N
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pIdiomaLayout = new javax.swing.GroupLayout(pIdioma);
        pIdioma.setLayout(pIdiomaLayout);
        pIdiomaLayout.setHorizontalGroup(
            pIdiomaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIdiomaLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbEspanol)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbIngles)
                .addGap(17, 17, 17))
        );
        pIdiomaLayout.setVerticalGroup(
            pIdiomaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIdiomaLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(pIdiomaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13)
                    .addGroup(pIdiomaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rbIngles)
                        .addComponent(rbEspanol)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bEntrar.setText("Entrar");
        bEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEntrarActionPerformed(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/UGR1.jpg"))); // NOI18N

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/ETSIIT1.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lDesc2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lDesc1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lBienvenida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(bEntrar)
                            .addComponent(pIdioma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lBienvenida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lDesc1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lDesc2)
                .addGap(33, 33, 33)
                .addComponent(bEntrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pIdioma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbEspanolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbEspanolActionPerformed
		SulaIRL.idioma = "es";
		cambiarIdioma();
    }//GEN-LAST:event_rbEspanolActionPerformed

    private void rbInglesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbInglesActionPerformed
		SulaIRL.idioma = "en";
		cambiarIdioma();
    }//GEN-LAST:event_rbInglesActionPerformed

    private void bEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEntrarActionPerformed
		if (rbIngles.isSelected()){
			Locale locale = new Locale("en", "US");
			Locale.setDefault(locale);
			AppContext.getAppContext().put("JComponent.defaultLocale", locale);
		}
		Configuracion conf = new Configuracion();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        conf.setLocation(dim.width/2-conf.getSize().width/2, dim.height/2-conf.getSize().height/2);
        conf.setVisible(true);
		dispose();
    }//GEN-LAST:event_bEntrarActionPerformed

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
		SulaIRL.idioma = "es";
		rbEspanol.setSelected(true);
		cambiarIdioma();
    }//GEN-LAST:event_jLabel13MouseClicked

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
		SulaIRL.idioma = "en";
		rbIngles.setSelected(true);
		cambiarIdioma();
    }//GEN-LAST:event_jLabel14MouseClicked

	private void cambiarIdioma(){
		lBienvenida.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_bienvenida"));
		bEntrar.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_boton_entrar"));
		pIdioma.setBorder(new TitledBorder(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_borde_idioma")));
		lDesc1.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_desc1"));
		lDesc2.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_label_desc2"));
		rbEspanol.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_radioButton_espanol"));
		rbIngles.setText(ResourceBundle.getBundle("etiquetas.Etiquetas_"+SulaIRL.idioma).getString("etiqueta_radioButton_ingles"));
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bEntrar;
    private javax.swing.ButtonGroup bgIdioma;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lBienvenida;
    private javax.swing.JLabel lDesc1;
    private javax.swing.JLabel lDesc2;
    private javax.swing.JPanel pIdioma;
    private javax.swing.JRadioButton rbEspanol;
    private javax.swing.JRadioButton rbIngles;
    // End of variables declaration//GEN-END:variables
}
