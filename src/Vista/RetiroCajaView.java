package Vista;

import Modelo.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RetiroCajaView extends JFrame {

    private JTextField txtMonto;
    private JTextArea txtMotivo;
    private JComboBox<String> cbTipo;
    private JButton btnConfirmar;

    private CajaMovimientoDao cajaDao = new CajaMovimientoDao();
    private CajaAperturaDao aperturaDao = new CajaAperturaDao();
    private String usuario;

    // Colores de alerta (Salida de dinero)
    private java.awt.Color rojoAlerta = new java.awt.Color(192, 57, 43);
    private java.awt.Color fondoGris = new java.awt.Color(242, 244, 244);

    public RetiroCajaView(String usuario) {
        this.usuario = usuario;
        initComponents();
        configurarEstilos();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Movimiento de Salida - Caja");
        setSize(450, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Encabezado ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(rojoAlerta);
        JLabel lblTitulo = new JLabel("REGISTRAR RETIRO DE EFECTIVO");
        lblTitulo.setForeground(java.awt.Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        pnlHeader.add(lblTitulo);
        add(pnlHeader, BorderLayout.NORTH);

        // --- Cuerpo del Formulario ---
        JPanel pnlCuerpo = new JPanel(new GridBagLayout());
        pnlCuerpo.setBackground(java.awt.Color.WHITE);
        pnlCuerpo.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Monto
        gbc.gridx = 0; gbc.gridy = 0;
        pnlCuerpo.add(new JLabel("Monto a retirar:"), gbc);
        
        txtMonto = new JTextField();
        txtMonto.setFont(new Font("SansSerif", Font.BOLD, 16));
        txtMonto.setForeground(rojoAlerta);
        gbc.gridx = 1; gbc.weightx = 1.0;
        pnlCuerpo.add(txtMonto, gbc);

        // Tipo
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        pnlCuerpo.add(new JLabel("Método:"), gbc);
        
        cbTipo = new JComboBox<>(new String[]{"EFECTIVO", "TRANSFERENCIA"});
        gbc.gridx = 1;
        pnlCuerpo.add(cbTipo, gbc);

        // Motivo
        gbc.gridx = 0; gbc.gridy = 2;
        pnlCuerpo.add(new JLabel("Motivo/Concepto:"), gbc);
        
        txtMotivo = new JTextArea(4, 20);
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(txtMotivo);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.BOTH;
        pnlCuerpo.add(sp, gbc);

        add(pnlCuerpo, BorderLayout.CENTER);

        // --- Botonera ---
        JPanel pnlSur = new JPanel();
        pnlSur.setBackground(fondoGris);
        btnConfirmar = new JButton("CONFIRMAR SALIDA");
        btnConfirmar.setBackground(rojoAlerta);
        btnConfirmar.setForeground(java.awt.Color.WHITE);
        btnConfirmar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnConfirmar.setPreferredSize(new Dimension(200, 45));
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnConfirmar.addActionListener(e -> registrarRetiro());
        pnlSur.add(btnConfirmar);
        add(pnlSur, BorderLayout.SOUTH);
    }

    private void configurarEstilos() {
        // Centrar texto del monto
        txtMonto.setHorizontalAlignment(JTextField.RIGHT);
    }

    private void registrarRetiro() {
        String montoStr = txtMonto.getText().trim();
        String motivo = txtMotivo.getText().trim();

        if (montoStr.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            BigDecimal monto = new BigDecimal(montoStr);
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor a cero");
                return;
            }

            // Confirmación de seguridad
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de retirar $" + monto + " de la caja?\nEsta acción quedará registrada a nombre de: " + usuario,
                "Confirmar Movimiento", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) return;

            CajaApertura ap = aperturaDao.cajaAbiertaDelDia();
            if (ap == null) {
                JOptionPane.showMessageDialog(this, "ERROR: No existe una sesión de caja abierta.");
                return;
            }

            CajaMovimiento mov = new CajaMovimiento();
            mov.setIdApertura(ap.getId());
            mov.setTipo("RETIRO");
          //  mov.setSubTipo(cbTipo.getSelectedItem().toString()); // Sugerencia: añadir este campo a tu modelo
            mov.setMonto(monto.negate()); 
            mov.setDescripcion(motivo);
            mov.setUsuario(usuario);
            mov.setFecha(LocalDate.now());
            mov.setHora(LocalTime.now()); // Añadimos la hora exacta

            if (cajaDao.registrarMovimiento(mov)) {
                JOptionPane.showMessageDialog(this, "Salida de caja registrada con éxito.");
                dispose();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un monto numérico válido.");
        }
    }
}
