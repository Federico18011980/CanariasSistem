package Vista;

import Modelo.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class DialogPagos extends javax.swing.JDialog {

    private BigDecimal totalVenta;
    private BigDecimal totalPagado = BigDecimal.ZERO;
    private BigDecimal montoBruto = BigDecimal.ZERO;
    private BigDecimal saldo;
    private List<Pago> pagos = new ArrayList<>();
    private ConfigDao confDao = new ConfigDao();

    // Colores Canarias System
    private java.awt.Color azulOscuro = new java.awt.Color(44, 62, 80);
    private java.awt.Color verdeDinero = new java.awt.Color(39, 174, 96);
    private java.awt.Color rojoBoton = new java.awt.Color(231, 76, 60);
    private java.awt.Color grisFondo = new java.awt.Color(245, 246, 250);

    public DialogPagos(java.awt.Frame parent, BigDecimal totalVenta) {
        super(parent, true);
        this.totalVenta = totalVenta;
        this.saldo = totalVenta;
        
        initComponents();
        this.getContentPane().setBackground(java.awt.Color.WHITE);
        this.setLocationRelativeTo(null);
        
        cmbTipoPago.setModel(new DefaultComboBoxModel<>(TipoPago.values()));
        cargarTiposPago();
        actualizarTotales();
    }

    private void initComponents() {
        setTitle("Finalizar Venta - Gestión de Cobro");
        setSize(750, 600);
        setLayout(new BorderLayout());

        // --- PANEL SUPERIOR: TARJETA DE TOTAL ---
        JPanel pnlHeader = new JPanel(new GridLayout(1, 1));
        pnlHeader.setBackground(azulOscuro);
        pnlHeader.setBorder(new EmptyBorder(20, 25, 20, 25));

        lblTotalVenta = new JLabel("TOTAL A COBRAR: $" + totalVenta);
        lblTotalVenta.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTotalVenta.setForeground(java.awt.Color.WHITE);
        lblTotalVenta.setHorizontalAlignment(SwingConstants.CENTER);
        pnlHeader.add(lblTotalVenta);
        add(pnlHeader, BorderLayout.NORTH);

        // --- PANEL CENTRAL: ENTRADA DE DATOS Y TABLA ---
        JPanel pnlCentral = new JPanel();
        pnlCentral.setLayout(new BoxLayout(pnlCentral, BoxLayout.Y_AXIS));
        pnlCentral.setBackground(java.awt.Color.WHITE);
        pnlCentral.setBorder(new EmptyBorder(20, 25, 10, 25));

        // Subpanel: Inputs
        JPanel pnlInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlInputs.setOpaque(false);

        cmbTipoPago = new JComboBox<>();
        cmbTipoPago.setPreferredSize(new Dimension(150, 35));
        
        txtMonto = new JTextField();
        txtMonto.setPreferredSize(new Dimension(120, 35));
        txtMonto.setFont(new Font("SansSerif", Font.BOLD, 16));
        txtMonto.setHorizontalAlignment(JTextField.CENTER);

        btnAgregarPago = new JButton("AGREGAR");
        estiloBoton(btnAgregarPago, verdeDinero);
        
        btnEliminar = new JButton("QUITAR");
        estiloBoton(btnEliminar, rojoBoton);

        pnlInputs.add(new JLabel("Método:"));
        pnlInputs.add(cmbTipoPago);
        pnlInputs.add(new JLabel("Monto:"));
        pnlInputs.add(txtMonto);
        pnlInputs.add(btnAgregarPago);
        pnlInputs.add(btnEliminar);

        pnlCentral.add(pnlInputs);
       pnlCentral.add(Box.createVerticalStrut(20)); // Espacio vertical de 20 píxeles

        // Tabla
        tblPagos = new JTable();
        tblPagos.setModel(new DefaultTableModel(
            new Object[][]{}, 
            new String[]{"TIPO", "BRUTO", "DESC.", "A COBRAR"}
        ) { boolean[] canEdit = new boolean[]{false, false, false, false};
            @Override public boolean isCellEditable(int r, int c) { return canEdit[c]; }
        });
        configurarTabla();
        
        JScrollPane scroll = new JScrollPane(tblPagos);
        scroll.setPreferredSize(new Dimension(650, 150));
        pnlCentral.add(scroll);
        add(pnlCentral, BorderLayout.CENTER);

        // --- PANEL SUR: RESUMEN Y FINALIZAR ---
        JPanel pnlSur = new JPanel(new BorderLayout());
        pnlSur.setBackground(grisFondo);
        pnlSur.setBorder(new EmptyBorder(15, 25, 15, 25));

        JPanel pnlResumen = new JPanel(new GridLayout(3, 1, 5, 5));
        pnlResumen.setOpaque(false);
        
        lblTotalPagado = new JLabel("Total Bruto: $0.00");
        lblTotalPagado.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        lblTotalCancelado = new JLabel("Total con Descuento: $0.00");
        lblTotalCancelado.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTotalCancelado.setForeground(verdeDinero);
        
        lblSaldo = new JLabel("SALDO PENDIENTE: $0.00");
        lblSaldo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblSaldo.setForeground(rojoBoton);

        pnlResumen.add(lblTotalPagado);
        pnlResumen.add(lblTotalCancelado);
        pnlResumen.add(lblSaldo);

        btnFinalizar = new JButton("FINALIZAR Y FACTURAR");
        estiloBoton(btnFinalizar, azulOscuro);
        btnFinalizar.setPreferredSize(new Dimension(200, 50));
        btnFinalizar.setFont(new Font("SansSerif", Font.BOLD, 14));

        pnlSur.add(pnlResumen, BorderLayout.WEST);
        pnlSur.add(btnFinalizar, BorderLayout.EAST);
        add(pnlSur, BorderLayout.SOUTH);

        // Eventos
        btnAgregarPago.addActionListener(e -> btnAgregarPagoActionPerformed(null));
        btnEliminar.addActionListener(e -> btnEliminarActionPerformed(null));
        btnFinalizar.addActionListener(e -> btnFinalizarActionPerformed(null));
        txtMonto.addActionListener(e -> btnAgregarPagoActionPerformed(null));
    }

    private void estiloBoton(JButton btn, java.awt.Color color) {
        btn.setBackground(color);
        btn.setForeground(java.awt.Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configurarTabla() {
        tblPagos.setRowHeight(30);
        JTableHeader h = tblPagos.getTableHeader();
        h.setFont(new Font("SansSerif", Font.BOLD, 12));
        h.setBackground(new java.awt.Color(230, 230, 230));
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<4; i++) tblPagos.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    // --- LÓGICA MANTENIDA ---

    private void btnAgregarPagoActionPerformed(java.awt.event.ActionEvent evt) {
        if (txtMonto.getText().trim().isEmpty()) return;
        try {
            BigDecimal monto = new BigDecimal(txtMonto.getText());
            if (monto.compareTo(BigDecimal.ZERO) <= 0) return;
            if (monto.compareTo(saldo) > 0) {
                JOptionPane.showMessageDialog(this, "El monto supera el saldo pendiente");
                return;
            }

            TipoPago tipo = (TipoPago) cmbTipoPago.getSelectedItem();
            Pago pago = crearPago(tipo, monto);
            pagos.add(pago);

            montoBruto = montoBruto.add(pago.getMontoBruto());
            totalPagado = totalPagado.add(pago.getMontoFinal());
            saldo = totalVenta.subtract(montoBruto);

            agregarPagoATabla(pago);
            actualizarTotales();
            txtMonto.setText("");
            txtMonto.requestFocus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Monto inválido");
        }
    }

    private void btnFinalizarActionPerformed(java.awt.event.ActionEvent evt) {
        if (pagos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar al menos un pago");
            return;
        }
        if (montoBruto.compareTo(totalVenta) < 0) {
            JOptionPane.showMessageDialog(this, "El monto pagado no cubre el total");
            return;
        }
        this.dispose();
    }

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = tblPagos.getSelectedRow();
        if (fila == -1) return;
        Pago p = pagos.get(fila);
        montoBruto = montoBruto.subtract(p.getMontoBruto());
        totalPagado = totalPagado.subtract(p.getMontoFinal());
        pagos.remove(fila);
        ((DefaultTableModel) tblPagos.getModel()).removeRow(fila);
        actualizarTotales();
    }

    private void actualizarTotales() {
        if (totalVenta == null) totalVenta = BigDecimal.ZERO;
        saldo = totalVenta.subtract(montoBruto);
        lblTotalVenta.setText("TOTAL A COBRAR: $" + totalVenta);
        lblTotalPagado.setText("Total Bruto: $" + montoBruto);
        lblTotalCancelado.setText("Total Real (con desc.): $" + totalPagado);
        lblSaldo.setText("SALDO PENDIENTE: $" + saldo);
        
        if (saldo.compareTo(BigDecimal.ZERO) == 0) {
            lblSaldo.setForeground(verdeDinero);
            lblSaldo.setText("PAGO COMPLETADO ✔");
        } else {
            lblSaldo.setForeground(rojoBoton);
        }
    }

    private Pago crearPago(TipoPago tipo, BigDecimal montoBruto) {
        BigDecimal porcentaje = obtenerPorcentajeDescuento(tipo);
        BigDecimal descuento = montoBruto.multiply(porcentaje).divide(new BigDecimal("100"));
        BigDecimal montoFinal = montoBruto.subtract(descuento);

        Pago p = new Pago();
        p.setTipo(tipo);
        p.setMontoBruto(montoBruto);
        p.setDescuento(descuento);
        p.setMontoFinal(montoFinal);
        return p;
    }

    private BigDecimal obtenerPorcentajeDescuento(TipoPago tipo) {
        Config conf = confDao.obtenerConfig();
        switch (tipo) {
            case EFECTIVO: return conf.getEfectivo();
            case TRANSFERENCIA: return conf.getTransferencia();
            case CREDITO: return conf.getCredito();
            default: return BigDecimal.ZERO;
        }
    }
    
          /*=====ZONA DE GETTERS=====*/
    
    public List<Pago> getPagos() {
        return pagos;
    }
    
    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public BigDecimal getMontoBruto() {
        return montoBruto;
    }

    private void cargarTiposPago() {
        cmbTipoPago.removeAllItems();
        cmbTipoPago.addItem(TipoPago.EFECTIVO);
        cmbTipoPago.addItem(TipoPago.TRANSFERENCIA);
        cmbTipoPago.addItem(TipoPago.CREDITO);
    }

    private void agregarPagoATabla(Pago p) {
        DefaultTableModel mod = (DefaultTableModel) tblPagos.getModel();
        mod.addRow(new Object[]{p.getTipo(), p.getMontoBruto(), p.getDescuento(), p.getMontoFinal()});
    }

    // Declaración de variables UI necesarias
    private javax.swing.JButton btnAgregarPago, btnEliminar, btnFinalizar;
    private javax.swing.JComboBox<TipoPago> cmbTipoPago;
    private javax.swing.JLabel lblSaldo, lblTotalCancelado, lblTotalPagado, lblTotalVenta;
    private javax.swing.JTable tblPagos;
    private javax.swing.JTextField txtMonto;
}