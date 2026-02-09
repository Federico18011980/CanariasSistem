package Vista;

import Modelo.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class DevolucionesView extends JFrame {

    VentaDao ventaDao = new VentaDao();
    DevolucionesDao devolucionDao = new DevolucionesDao();
    ProductosDao proDao = new ProductosDao();
    CajaMovimientoDao cajaDao = new CajaMovimientoDao();
    CajaAperturaDao aperturaDao = new CajaAperturaDao();
    private ConfigDao confDao = new ConfigDao();
    
    DefaultTableModel modeloDetalles;
    Venta ventaActual;
    String usuario;
    BigDecimal porcentaje = BigDecimal.ZERO;
    BigDecimal montoFinal = BigDecimal.ZERO;

    // COMPONENTES UI
    private JTextField txtIdVenta, txtCantidad;
    private JLabel lblInfoVenta;
    private JTable tablaDetalles;
    private JRadioButton rbDevolucion, rbCambio;
    private JButton btnBuscar, btnConfirmar;

    // COLORES PREMIUM
    private java.awt.Color azulOscuro = new java.awt.Color(44, 62, 80);
    private java.awt.Color grisFondo = new java.awt.Color(245, 246, 250);
    private java.awt.Color verdeEsmeralda = new java.awt.Color(46, 204, 113);

    public DevolucionesView(String usuarioLogueado) {
        this.usuario = usuarioLogueado;
        initComponents();
        getContentPane().setBackground(java.awt.Color.WHITE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Soporte Canarias - Gestión de Cambios y Devoluciones");
        setSize(950, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- PANEL NORTE: BUSCADOR ---
        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setBackground(azulOscuro);
        pnlNorte.setPreferredSize(new Dimension(850, 110));
        pnlNorte.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblTitulo = new JLabel("DEVOLUCIONES Y CAMBIOS");
        lblTitulo.setForeground(java.awt.Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        pnlNorte.add(lblTitulo, BorderLayout.NORTH);

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlBusqueda.setOpaque(false);
        
        JLabel lblVenta = new JLabel("ID Venta:");
        lblVenta.setForeground(java.awt.Color.WHITE);
        lblVenta.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        txtIdVenta = new JTextField(10);
        txtIdVenta.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        btnBuscar = new JButton("Buscar Comprobante");
        estiloBoton(btnBuscar, new java.awt.Color(52, 152, 219));
        
        pnlBusqueda.add(lblVenta);
        pnlBusqueda.add(txtIdVenta);
        pnlBusqueda.add(btnBuscar);
        pnlNorte.add(pnlBusqueda, BorderLayout.SOUTH);

        add(pnlNorte, BorderLayout.NORTH);

        // --- PANEL CENTRAL: TABLA ---
        JPanel pnlCentral = new JPanel(new BorderLayout());
        pnlCentral.setBackground(java.awt.Color.WHITE);
        pnlCentral.setBorder(new EmptyBorder(15, 25, 10, 25));

        lblInfoVenta = new JLabel("<html><i>Seleccione un comprobante para listar los productos...</i></html>");
        lblInfoVenta.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblInfoVenta.setForeground(new java.awt.Color(127, 140, 141));
        lblInfoVenta.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnlCentral.add(lblInfoVenta, BorderLayout.NORTH);

        modeloDetalles = new DefaultTableModel(new String[]{"CÓDIGO", "CANT. VENDIDA", "PRECIO ORIG.", "MONTO REINTEGRO"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalles = new JTable(modeloDetalles);
        configurarTabla();
        
        JScrollPane scroll = new JScrollPane(tablaDetalles);
        scroll.getViewport().setBackground(java.awt.Color.WHITE);
        pnlCentral.add(scroll, BorderLayout.CENTER);

        add(pnlCentral, BorderLayout.CENTER);

        // --- PANEL SUR: ACCIONES ---
        JPanel pnlSur = new JPanel(new GridBagLayout());
        pnlSur.setBackground(grisFondo);
        pnlSur.setPreferredSize(new Dimension(850, 140));
        pnlSur.setBorder(new EmptyBorder(10, 25, 10, 25));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        pnlSur.add(new JLabel("Cantidad a Procesar:"), gbc);
        
        txtCantidad = new JTextField(10); // Aumentamos un poco las columnas
        txtCantidad.setFont(new Font("SansSerif", Font.BOLD, 16)); // Fuente más grande
        txtCantidad.setHorizontalAlignment(JTextField.CENTER); // Centramos el número
        txtCantidad.setPreferredSize(new Dimension(100, 35)); // FORZAMOS el tamaño (Ancho, Alto)

// Opcional: Un borde redondeado o con color para que resalte
        txtCantidad.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
//        txtCantidad = new JTextField(8);
//        txtCantidad.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 1; 
        pnlSur.add(txtCantidad, gbc);

        rbCambio = new JRadioButton("Cambio de Producto");
        rbCambio.setSelected(true);
        rbCambio.setOpaque(false);
        rbDevolucion = new JRadioButton("Devolución de Dinero");
        rbDevolucion.setOpaque(false);
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbCambio); bg.add(rbDevolucion);

        gbc.gridx = 2; 
        pnlSur.add(rbCambio, gbc);
        gbc.gridx = 3;
        pnlSur.add(rbDevolucion, gbc);

        // Botón Confirmar
        btnConfirmar = new JButton("CONFIRMAR OPERACIÓN");
        estiloBoton(btnConfirmar, verdeEsmeralda);
        btnConfirmar.setPreferredSize(new Dimension(200, 40));
        gbc.gridx = 4; gbc.gridheight = 1;
        pnlSur.add(btnConfirmar, gbc);

        add(pnlSur, BorderLayout.SOUTH);

        // --- EVENTOS ---
        btnBuscar.addActionListener(e -> buscarVenta());
        btnConfirmar.addActionListener(e -> procesarDevolucion());
        txtIdVenta.addActionListener(e -> buscarVenta());
    }

    private void estiloBoton(JButton btn, java.awt.Color colorFondo) {
        btn.setBackground(colorFondo);
        btn.setForeground(java.awt.Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    private void configurarTabla() {
        tablaDetalles.setRowHeight(35);
        tablaDetalles.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaDetalles.setSelectionBackground(new java.awt.Color(236, 240, 241));
        tablaDetalles.setSelectionForeground(java.awt.Color.BLACK);
        tablaDetalles.setShowVerticalLines(false);

        JTableHeader header = tablaDetalles.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new java.awt.Color(236, 240, 241));
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tablaDetalles.getColumnCount(); i++) {
            tablaDetalles.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // --- LÓGICA (Mantenemos tu funcionalidad tal cual) ---
    private void buscarVenta() {
        if (txtIdVenta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID de venta.");
            return;
        }
        try {
            int idVenta = Integer.parseInt(txtIdVenta.getText());
            ventaActual = ventaDao.buscarVentaPorId(idVenta);

            if (ventaActual == null) {
                JOptionPane.showMessageDialog(this, "Venta no encontrada.");
                return;
            }

            lblInfoVenta.setText("<html><b>CLIENTE:</b> " + ventaActual.getCliente() +
                                 " &nbsp;&nbsp;|&nbsp;&nbsp; <b>FECHA:</b> " + ventaActual.getFecha() +
                                 " &nbsp;&nbsp;|&nbsp;&nbsp; <b>PAGO:</b> " + ventaActual.getPago() + "</html>");
            lblInfoVenta.setForeground(azulOscuro);
            cargarDetalles(idVenta);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser numérico.");
        }
    }

    private void cargarDetalles(int idVenta) {
        Config conf = confDao.obtenerConfig();
        porcentaje = conf.getEfectivo();
        modeloDetalles.setRowCount(0);

        for (Detalle d : ventaDao.listarDetallesPorVenta(idVenta)) {
            BigDecimal precioOriginal = d.getPrecio();
            BigDecimal PrecioDescuento = precioOriginal.multiply(porcentaje).divide(new BigDecimal("100"));
            montoFinal = precioOriginal.subtract(PrecioDescuento);
            BigDecimal montoFinalDecimales = montoFinal.setScale(2, RoundingMode.HALF_UP);
            
            modeloDetalles.addRow(new Object[]{ d.getCod_pro(), d.getCant(), "$ " + d.getPrecio(), "$ " + montoFinalDecimales });
        }
    }

    private void procesarDevolucion() {
        int fila = tablaDetalles.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.");
            return;
        }
        if (txtCantidad.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cantidad.");
            return;
        }

        try {
            BigDecimal cantidad = new BigDecimal(txtCantidad.getText());
            // Limpiamos el "$ " del valor de la tabla para convertir a BigDecimal
            String precioStr = modeloDetalles.getValueAt(fila, 3).toString().replace("$ ", "").replace(",", ".");
            BigDecimal precio = new BigDecimal(precioStr);
            String codigo = modeloDetalles.getValueAt(fila, 0).toString();
            BigDecimal total = precio.multiply(cantidad);
            String tipo = rbDevolucion.isSelected() ? "DEVOLUCION" : "CAMBIO";

            // VALIDACIÓN DE STOCK
            BigDecimal cantidadVendida = ventaDao.cantidadVendida(ventaActual.getId(), codigo);
            BigDecimal cantidadYaDevuelta = devolucionDao.cantidadDevuelta(ventaActual.getId(), codigo);
            BigDecimal disponibleParaDevolver = cantidadVendida.subtract(cantidadYaDevuelta);

            if (cantidad.compareTo(disponibleParaDevolver) > 0) {
                JOptionPane.showMessageDialog(this, "No puede devolver más de lo vendido.\nDisponible: " + disponibleParaDevolver);
                return;
            }

            // REGISTRO
            Devolucion d = new Devolucion();
            d.setIdVenta(ventaActual.getId());
            d.setCodProducto(codigo);
            d.setCantidad(cantidad);
            d.setPrecio(precio);
            d.setTotal(total);
            d.setTipo(tipo);
            d.setFormaPago(ventaActual.getPago());
            d.setFecha(LocalDate.now().toString());
            d.setUsuario(usuario);

            if (devolucionDao.registrarDevolucion(d)) {
                proDao.sumarStock(codigo, cantidad);
                if (tipo.equals("DEVOLUCION") && ventaActual.getPago().equalsIgnoreCase("EFECTIVO")) {
                    CajaApertura ap = aperturaDao.cajaAbiertaDelDia();
                    if (ap != null) {
                        CajaMovimiento mov = new CajaMovimiento();
                        mov.setMonto(total.negate()); // Salida de dinero
                        mov.setTipo("DEVOLUCION");
                        mov.setDescripcion("Devolución venta ID: " + ventaActual.getId());
                        mov.setFecha(LocalDate.now());
                        mov.setHora(java.time.LocalTime.now());
                        mov.setUsuario(usuario);
                        mov.setIdApertura(ap.getId());
                        cajaDao.registrarMovimiento(mov);
                    }
                }
                JOptionPane.showMessageDialog(this, tipo + " procesada con éxito.");
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en los datos ingresados.");
        }
    }
}

