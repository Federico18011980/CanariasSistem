
package Vista;

import Modelo.Categoria;
import Modelo.CategoriaDao;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class CategoriasView extends javax.swing.JFrame {

    CategoriaDao cdao = new CategoriaDao();
    Categoria categoria = new Categoria();
    DefaultTableModel modelo = new DefaultTableModel();

    public CategoriasView() {
        initComponents();
        listarCategorias();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCategorias = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdCat = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombreCat = new javax.swing.JTextField();
        jPanelBotoneraCategoria = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCategorias = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Categorias");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("ID:");

        txtIdCat.setEditable(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Nombre");

        txtNombreCat.setMinimumSize(new java.awt.Dimension(89, 22));
        txtNombreCat.setPreferredSize(new java.awt.Dimension(89, 22));
        txtNombreCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreCatActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo24.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/actualizar24.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/borrar.png"))); // NOI18N
        btnEliminar.setText("Borrar");
        btnEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEliminar.setIconTextGap(2);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelBotoneraCategoriaLayout = new javax.swing.GroupLayout(jPanelBotoneraCategoria);
        jPanelBotoneraCategoria.setLayout(jPanelBotoneraCategoriaLayout);
        jPanelBotoneraCategoriaLayout.setHorizontalGroup(
            jPanelBotoneraCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotoneraCategoriaLayout.createSequentialGroup()
                .addGroup(jPanelBotoneraCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelBotoneraCategoriaLayout.setVerticalGroup(
            jPanelBotoneraCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotoneraCategoriaLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(btnGuardar)
                .addGap(5, 5, 5)
                .addComponent(btnEditar)
                .addGap(5, 5, 5)
                .addComponent(btnEliminar)
                .addContainerGap(110, Short.MAX_VALUE))
        );

        tablaCategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Nombre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaCategoriasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaCategorias);

        javax.swing.GroupLayout panelCategoriasLayout = new javax.swing.GroupLayout(panelCategorias);
        panelCategorias.setLayout(panelCategoriasLayout);
        panelCategoriasLayout.setHorizontalGroup(
            panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCategoriasLayout.createSequentialGroup()
                .addGroup(panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCategoriasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelBotoneraCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCategoriasLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCategoriasLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(5, 5, 5))
                            .addGroup(panelCategoriasLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(35, 35, 35)))
                        .addGroup(panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreCat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        panelCategoriasLayout.setVerticalGroup(
            panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCategoriasLayout.createSequentialGroup()
                .addGroup(panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCategoriasLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(txtIdCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCategoriasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addGroup(panelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCategoriasLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel2))
                    .addComponent(txtNombreCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanelBotoneraCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelCategoriasLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelCategorias, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 490, 330));

        pack();
    }// </editor-fold>//GEN-END:initComponents
public void listarCategorias() {
    modelo = (DefaultTableModel) tablaCategorias.getModel();
    modelo.setRowCount(0); // limpiar tabla

    for (Categoria c : cdao.listar()) {
        modelo.addRow(new Object[]{
            c.getId(),
            c.getNombre()
        });
    }
}
public void limpiarCampos() {
    txtIdCat.setText("");
    txtNombreCat.setText("");
}


    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:

        if (txtIdCat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una categoría");
            return;
        }

        Categoria c = new Categoria();
        c.setId(Integer.parseInt(txtIdCat.getText()));
        c.setNombre(txtNombreCat.getText());

        if (cdao.modificar(c)) {
            JOptionPane.showMessageDialog(null, "Categoría modificada");
            listarCategorias();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(null, "Error al modificar");
        }


    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:

        if (txtNombreCat.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre");
            return;
        }

        Categoria c = new Categoria();
        c.setNombre(txtNombreCat.getText());

        if (cdao.registrar(c)) {
            JOptionPane.showMessageDialog(null, "Categoría registrada");
            listarCategorias();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar categoría");
        }
        
       // cargarCategoria();


    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tablaCategoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaCategoriasMouseClicked
        // TODO add your handling code here:

        int fila = tablaCategorias.rowAtPoint(evt.getPoint());

        txtIdCat.setText(tablaCategorias.getValueAt(fila, 0).toString());
        txtNombreCat.setText(tablaCategorias.getValueAt(fila, 1).toString());


    }//GEN-LAST:event_tablaCategoriasMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:

        if (txtIdCat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una categoría");
            return;
        }

        int id = Integer.parseInt(txtIdCat.getText());

        if (cdao.eliminar(id)) {
            JOptionPane.showMessageDialog(null, "Categoría eliminada");
            listarCategorias();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar");
        }


    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtNombreCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreCatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreCatActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CategoriasView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CategoriasView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CategoriasView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CategoriasView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CategoriasView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanelBotoneraCategoria;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelCategorias;
    private javax.swing.JTable tablaCategorias;
    private javax.swing.JTextField txtIdCat;
    private javax.swing.JTextField txtNombreCat;
    // End of variables declaration//GEN-END:variables
}
