
package Vista;

import Modelo.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AnularVentaView extends JFrame {

    private JTextField txtIdVenta;
    private JLabel lblInfo;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnAnular, btnBuscar;

    private VentaDao ventaDao = new VentaDao();
    private ProductosDao proDao = new ProductosDao();
    private final CajaMovimientoDao cajaDao = new CajaMovimientoDao();
    private CajaAperturaDao aperturaDao = new CajaAperturaDao();
    
    private Venta ventaActual;
    private String usuario;
    // Colores de identidad
    private java.awt.Color azulOscuro;
    private java.awt.Color rojoAlizarin; 
    private java.awt.Color grisClaro; 

    public AnularVentaView(String usuarioLogueado) {
        this.azulOscuro = new java.awt.Color(44, 62, 80);
        this.rojoAlizarin = new java.awt.Color(231, 76, 60);
        this.grisClaro = new java.awt.Color(245, 246, 250);
        this.usuario = usuarioLogueado;
        initComponents();
        getContentPane().setBackground(java.awt.Color.WHITE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Sistema Canarias - Módulo de Anulación");
        setSize(850, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- PANEL SUPERIOR (Buscador) ---
        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setBackground(azulOscuro);
        pnlNorte.setPreferredSize(new Dimension(850, 100));
        pnlNorte.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblTitulo = new JLabel("ANULAR COMPROBANTE");
        lblTitulo.setForeground(java.awt.Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        pnlNorte.add(lblTitulo, BorderLayout.NORTH);

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlBusqueda.setOpaque(false);
        
        JLabel lblId = new JLabel("ID de Venta:");
        lblId.setForeground(java.awt.Color.WHITE);
        lblId.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        txtIdVenta = new JTextField(10);
        txtIdVenta.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        btnBuscar = new JButton("Buscar Venta");
        estiloBoton(btnBuscar, new java.awt.Color(52, 152, 219)); // Azul brillante
        
        pnlBusqueda.add(lblId);
        pnlBusqueda.add(txtIdVenta);
        pnlBusqueda.add(btnBuscar);
        pnlNorte.add(pnlBusqueda, BorderLayout.SOUTH);

        add(pnlNorte, BorderLayout.NORTH);

        // --- PANEL CENTRAL (Información y Tabla) ---
        JPanel pnlCentral = new JPanel(new BorderLayout());
        pnlCentral.setBackground(java.awt.Color.WHITE);
        pnlCentral.setBorder(new EmptyBorder(20, 25, 10, 25));

        lblInfo = new JLabel("Ingrese un ID para visualizar los detalles del comprobante...");
        lblInfo.setFont(new Font("SansSerif", Font.ITALIC, 13));
        lblInfo.setForeground(new java.awt.Color(127, 140, 141));
        lblInfo.setBorder(new EmptyBorder(0, 0, 15, 0));
        pnlCentral.add(lblInfo, BorderLayout.NORTH);

        // Configuración de Tabla
        modelo = new DefaultTableModel(new String[]{"CÓDIGO", "CANTIDAD", "PRECIO UNIT.", "SUBTOTAL"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        configurarTabla();
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(java.awt.Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new java.awt.Color(230, 230, 230)));
        pnlCentral.add(scroll, BorderLayout.CENTER);

        add(pnlCentral, BorderLayout.CENTER);

        // --- PANEL SUR (Acciones) ---
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 20));
        pnlSur.setBackground(grisClaro);
        
        btnAnular = new JButton("CONFIRMAR ANULACIÓN");
        btnAnular.setEnabled(false);
        estiloBoton(btnAnular, rojoAlizarin);
        btnAnular.setPreferredSize(new Dimension(250, 45));

        pnlSur.add(btnAnular);
        add(pnlSur, BorderLayout.SOUTH);

        // --- EVENTOS ---
        btnBuscar.addActionListener(e -> buscarVenta());
        btnAnular.addActionListener(e -> anularVenta());
        
        // Enter para buscar
        txtIdVenta.addActionListener(e -> buscarVenta());
    }

    private void estiloBoton(JButton btn, java.awt.Color colorFondo) {
        btn.setBackground(colorFondo);
        btn.setForeground(java.awt.Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configurarTabla() {
        tabla.setRowHeight(35);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setSelectionBackground(new java.awt.Color(236, 240, 241));
        tabla.setSelectionForeground(java.awt.Color.BLACK);
        tabla.setShowVerticalLines(false);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new java.awt.Color(236, 240, 241));
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // --- LÓGICA (Mantenemos tu lógica impecable) ---
    private void buscarVenta() {
        if (txtIdVenta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID de venta.");
            return;
        }

        try {
            int id = Integer.parseInt(txtIdVenta.getText());
            ventaActual = ventaDao.buscarVentaPorId(id);

            if (ventaActual == null) {
                JOptionPane.showMessageDialog(this, "La venta no existe en los registros.");
                return;
            }

            lblInfo.setText("<html><b>CLIENTE:</b> " + ventaActual.getCliente() 
                    + " &nbsp;&nbsp;|&nbsp;&nbsp; <b>FECHA:</b> " + ventaActual.getFecha() 
                    + " &nbsp;&nbsp;|&nbsp;&nbsp; <b>METODO:</b> " + ventaActual.getPago() 
                    + " &nbsp;&nbsp;|&nbsp;&nbsp; <b>ESTADO:</b> " + ventaActual.getEstado() + "</html>");

            cargarDetalles(id);

            boolean esAnulable = !"ANULADA".equalsIgnoreCase(ventaActual.getEstado());
            btnAnular.setEnabled(esAnulable);
            
            if (!esAnulable) {
                lblInfo.setForeground(rojoAlizarin);
            } else {
                lblInfo.setForeground(new java.awt.Color(44, 62, 80));
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.");
        }
    }

    private void cargarDetalles(int idVenta) {
        modelo.setRowCount(0);
        List<Detalle> detalles = ventaDao.listarDetallesPorVenta(idVenta);
        for (Detalle d : detalles) {
            BigDecimal subtotal = d.getPrecio().multiply(d.getCant());
            modelo.addRow(new Object[]{ d.getCod_pro(), d.getCant(), "$ " + d.getPrecio(), "$ " + subtotal });
        }
    }

    private void anularVenta() {
        if (ventaActual == null) return;

        if (ventaDao.tieneDevoluciones(ventaActual.getId())) {
            JOptionPane.showMessageDialog(this, "Operación denegada: La venta tiene devoluciones vinculadas.");
            return;
        }

        int r = JOptionPane.showConfirmDialog(this, 
                "¿Confirmar la anulación definitiva del comprobante ID " + ventaActual.getId() + "?\nEsta acción revertirá stock y afectará caja.", 
                "Advertencia de Seguridad", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (r != JOptionPane.YES_OPTION) return;

        // --- PROCESO DE ANULACIÓN ---
        List<Detalle> detalles = ventaDao.listarDetallesPorVenta(ventaActual.getId());
        for (Detalle d : detalles) {
            proDao.sumarStock(d.getCod_pro(), d.getCant());
        }

        if ("EFECTIVO".equalsIgnoreCase(ventaActual.getPago())) {
            CajaApertura ap = aperturaDao.cajaAbiertaDelDia();
            if (ap != null) {
                CajaMovimiento mov = new CajaMovimiento();
                mov.setIdApertura(ap.getId());
                mov.setFecha(LocalDate.now());
                mov.setHora(LocalTime.now());
                mov.setTipo("ANULACION");
                mov.setMonto(ventaActual.getTotal().negate());
                mov.setDescripcion("Anulación venta ID " + ventaActual.getId());
                mov.setUsuario(usuario);
                cajaDao.registrarMovimiento(mov);
            }
        }

        VentaAnulada va = new VentaAnulada();
        va.setIdVenta(ventaActual.getId());
        va.setTotal(ventaActual.getTotal());
        va.setMotivo("Anulación manual de usuario");
        va.setUsuario(usuario);
        va.setFecha(LocalDate.now().toString());

        VentaAnuladaDao vaDao = new VentaAnuladaDao();
        if (vaDao.registrar(va)) {
            if (ventaDao.marcarVentaAnulada(ventaActual.getId())) {
                JOptionPane.showMessageDialog(this, "Venta anulada con éxito.");
                dispose();
            }
        }
    }
}