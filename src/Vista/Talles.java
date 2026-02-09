
package Vista;

import Modelo.Talle;
import Modelo.TalleDao;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Talles extends javax.swing.JFrame {

    TalleDao talledao = new TalleDao();
    DefaultTableModel modelo = new DefaultTableModel();

    public Talles() {
        initComponents();
        setLocationRelativeTo(null);
        ListarTalles();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtTalle = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTalles = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestión de Talles");

        jLabel1.setText("Talle:");

        txtId.setVisible(false);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(evt -> Guardar());

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(evt -> Actualizar());

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(evt -> Eliminar());

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(evt -> LimpiarCampos());

        tablaTalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "ID", "Talle" }
        ));
        tablaTalles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaTallesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaTalles);

        // ----- LAYOUT AUTOMÁTICO (NO TOCAR) -----
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addComponent(txtTalle, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnGuardar)
                            .addGap(18, 18, 18)
                            .addComponent(btnActualizar)
                            .addGap(18, 18, 18)
                            .addComponent(btnEliminar)
                            .addGap(18, 18, 18)
                            .addComponent(btnNuevo)))
                    .addContainerGap(20, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtTalle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(20, 20, 20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnGuardar)
                        .addComponent(btnActualizar)
                        .addComponent(btnEliminar)
                        .addComponent(btnNuevo))
                    .addGap(20, 20, 20)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }

    // ---- MÉTODOS CRUD ----

    private void ListarTalles() {
        List<Talle> lista = talledao.listar();
        modelo = (DefaultTableModel) tablaTalles.getModel();
        modelo.setRowCount(0);

        for (Talle t : lista) {
            modelo.addRow(new Object[]{t.getId(), t.getNombre()});
        }
    }

    private void Guardar() {
        if (txtTalle.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un talle");
            return;
        }

        Talle t = new Talle();
        t.setNombre(txtTalle.getText());

        talledao.registrar(t);
        ListarTalles();
        LimpiarCampos();
    }

    private void Actualizar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un talle de la tabla");
            return;
        }

        Talle t = new Talle();
        t.setId(Integer.parseInt(txtId.getText()));
        t.setNombre(txtTalle.getText());

        talledao.modificar(t);
        ListarTalles();
        LimpiarCampos();
    }

    private void Eliminar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un talle para eliminar");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        talledao.eliminar(id);
        ListarTalles();
        LimpiarCampos();
    }

    private void tablaTallesMouseClicked(java.awt.event.MouseEvent evt) {
        int fila = tablaTalles.rowAtPoint(evt.getPoint());

        txtId.setText(tablaTalles.getValueAt(fila, 0).toString());
        txtTalle.setText(tablaTalles.getValueAt(fila, 1).toString());
    }

    private void LimpiarCampos() {
        txtId.setText("");
        txtTalle.setText("");
    }

    // --- VARIABLES ---
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaTalles;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtTalle;
}
