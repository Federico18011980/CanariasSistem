
package Vista;

import Modelo.CajaApertura;
import Modelo.CajaAperturaDao;
import Modelo.CajaCierre;
import Modelo.CajaCierreDao;
import Modelo.CajaMovimientoDao;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ResumenCajaView extends JFrame {

    private JLabel lblEntradas, lblSalidas, lblEfectivo;
    private JLabel lblDebito, lblCredito, lblTransferencia;
    private JLabel lblSaldoFinal, lblDifEfectivo;
    private JLabel lblDineroFisico, lblDiferenciaCaja;
    private JTextField txtDinero;

    private CajaMovimientoDao movDao = new CajaMovimientoDao();
    private CajaAperturaDao aperturaDao = new CajaAperturaDao();
    private CajaCierreDao cierreDao = new CajaCierreDao();
    private CajaApertura apertura;
    private String usuario;
    private BigDecimal efectivocierre =BigDecimal.ZERO;

    public ResumenCajaView(String usuario) {
        setTitle("Resumen de Caja");
        setSize(400, 600);
        setLayout(null);
        setLocationRelativeTo(null);
        this.usuario = usuario;
        apertura = aperturaDao.cajaAbiertaDelDia();

        if (apertura == null) {
            JOptionPane.showMessageDialog(this, "No hay caja abierta");
            dispose();
            return;
        }

        crearUI();
        cargarResumen();
    }



private void crearUI() {
    // 1. Configuración de Formato de Moneda (Local)
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.getDefault());

    // 2. Configuración del Layout
    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 15, 8, 15); // Más aire entre elementos
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;

    // 3. Creación de Etiquetas con Formato (Ejemplo con valores en 0)
    // El texto se verá como: "Efectivo: $ 0.00"
    lblEntradas = new JLabel("Entradas: " + formatoMoneda.format(0));
    lblSalidas = new JLabel("Salidas: " + formatoMoneda.format(0));
    lblEfectivo = new JLabel("Efectivo: " + formatoMoneda.format(0));
    lblDebito = new JLabel("Débito: " + formatoMoneda.format(0));
    lblCredito = new JLabel("Crédito: " + formatoMoneda.format(0));
    lblTransferencia = new JLabel("Transferencia: " + formatoMoneda.format(0));
    lblSaldoFinal = new JLabel("<html><b>Saldo Final: " + formatoMoneda.format(0) + "</b></html>");
    lblDifEfectivo = new JLabel("Dinere esperado: " + formatoMoneda.format(0));
    lblDineroFisico = new JLabel ("Dinero en caja: " );
    lblDiferenciaCaja = new JLabel("");

    // Estilo especial para el Saldo Final (Negrita y azul)
    lblSaldoFinal.setForeground(new Color(0, 51, 153));
    lblSaldoFinal.setFont(new Font("Arial", Font.BOLD, 14));
    // Inicializar txtDinero
    txtDinero = new JTextField(10); // Tamaño sugerido
    
    // Restringir entrada a números y un solo punto decimal
    txtDinero.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            char c = evt.getKeyChar();
            if (!(Character.isDigit(c) || c == '.')) {
                evt.consume();
            }
            if (c == '.' && txtDinero.getText().contains(".")) {
                evt.consume();
            }
        }
    });
    
    txtDinero.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    @Override
    public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarDiferencia(); }
    @Override
    public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarDiferencia(); }
    @Override
    public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarDiferencia(); }
});

    JLabel[] etiquetas = {
        lblEntradas, lblSalidas, lblEfectivo, lblDebito, 
        lblCredito, lblTransferencia, lblSaldoFinal, lblDifEfectivo, lblDineroFisico,
        lblDiferenciaCaja
    };

//    for (int i = 0; i < etiquetas.length; i++) {
//        gbc.gridy = i;
//        add(etiquetas[i], gbc);
//    }

    // Recorremos las etiquetas hasta lblDifEfectivo (la anterior a Dinero Físico)
    JLabel[] etiquetasPrevias = {
        lblEntradas, lblSalidas, lblEfectivo, lblDebito, 
        lblCredito, lblTransferencia, lblSaldoFinal, lblDifEfectivo
    };

    int fila = 0;
    for (JLabel etiqueta : etiquetasPrevias) {
        gbc.gridy = fila++;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Ocupa dos columnas para mantener el ancho
        add(etiqueta, gbc);
    }

    // --- FILA ESPECIAL: Dinero Físico + JTextField ---
    gbc.gridy = fila++;
    gbc.gridx = 0;
    gbc.gridwidth = 1; // Solo una columna
    gbc.fill = GridBagConstraints.NONE; // Que no se estire demasiado
    gbc.anchor = GridBagConstraints.WEST;
    add(lblDineroFisico, gbc);

    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0; // Le damos peso para que el campo de texto crezca
    add(txtDinero, gbc);
    // --------------------------------------------------

    // Etiqueta de diferencia debajo
    gbc.gridy = fila++;
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    gbc.weightx = 0;
    add(lblDiferenciaCaja, gbc);

    // ... (resto de botones)




    // Espaciador antes de los botones
    gbc.gridy++;
    add(Box.createVerticalStrut(20), gbc);

    // 4. Botones con Iconos
    // Nota: Asegúrate de tener las imágenes en una carpeta 'resources' de tu proyecto
    JButton btnRetiros = crearBotonConIcono("Ver Retiros", "/Img/reporte 24x24.png");
    btnRetiros.addActionListener(e -> new HistorialRetirosView().setVisible(true));
    gbc.gridy++;
    add(btnRetiros, gbc);

    JButton btnRetirar = crearBotonConIcono("Retirar Dinero", "/Img/dinero-24.png");
    btnRetirar.addActionListener(e -> new RetiroCajaView(this.usuario).setVisible(true));
    gbc.gridy++;
    add(btnRetirar, gbc);
    
    JButton btnHistorico = crearBotonConIcono("Histórico de Cierres", "/Img/reporte 24x24.png");
    btnHistorico.addActionListener(e -> new HistoricoCierresView().setVisible(true));
    gbc.gridy++;
    add(btnHistorico, gbc);
    // Agrégalo al layout (gbc.gridy++)

    JButton btnCerrar = crearBotonConIcono("Cerrar Caja", "/Img/GuardarTodo24.png");
    btnCerrar.setBackground(new Color(46, 204, 113)); // Verde éxito
    btnCerrar.setForeground(Color.WHITE); // Texto blanco
    btnCerrar.setFont(new Font("Arial", Font.BOLD, 12));
    btnCerrar.addActionListener(e -> cerrarCaja());
    gbc.gridy++;
    add(btnCerrar, gbc);
}

// Método auxiliar para no repetir código al crear botones
private JButton crearBotonConIcono(String texto, String rutaIcono) {
    JButton boton = new JButton(texto);
    try {
        ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
        // Redimensionar icono a 20x20 píxeles
        Image img = icono.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        boton.setIcon(new ImageIcon(img));
    } catch (Exception e) {
        System.out.println("No se pudo cargar el icono: " + rutaIcono);
    }
    boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mano al pasar por encima
    boton.setFocusPainted(false); // Quita el recuadro feo al hacer clic
    return boton;
}


    private void cerrarCaja() {
        String textoFisico = txtDinero.getText().trim();
        if (textoFisico.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el monto físico en caja.");
            txtDinero.requestFocus();
            return;
        }

        BigDecimal montoFisico = new BigDecimal(textoFisico);
        CajaCierre cierre = generarObjetoCierre(montoFisico); // Usamos el método que creamos antes

        // Validar diferencia
        if (cierre.getDiferencia().compareTo(BigDecimal.ZERO) != 0) {
            int confirmar = JOptionPane.showConfirmDialog(this,
                    "Hay una diferencia de $" + cierre.getDiferencia() + ". ¿Cerrar de todos modos?",
                    "Confirmar Diferencia", JOptionPane.YES_NO_OPTION);
            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // UN SOLO LLAMADO AL DAO

        // ... (después de ejecutarCierreCompleto)
        if (cierreDao.ejecutarCierreCompleto(cierre)) {
            StringBuilder reporte = new StringBuilder();
            reporte.append("=== RESUMEN DE CIERRE ===\n");
            reporte.append(String.format("Usuario: %s\n", cierre.getUsuario()));
            reporte.append(String.format("Efectivo en Sistema: $%.2f\n",
                    cierre.getTotalEfectivo().subtract(cierre.getTotalSalidas().abs()).add(apertura.getMontoInicial())));
            reporte.append(String.format("Efectivo Físico: $%.2f\n",
                    montoFisico));
            reporte.append(String.format("Diferencia: $%.2f\n", cierre.getDiferencia()));
            reporte.append("=========================\n");
            reporte.append("¡Caja cerrada con éxito!");

            JOptionPane.showMessageDialog(this, reporte.toString(), "Reporte de Cierre", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al procesar el cierre.");

        }
    }

// Cambiamos el nombre y ahora este método RETORNA el objeto sin guardar nada aún
private CajaCierre generarObjetoCierre(BigDecimal montoFisicoReal) {
    int id = apertura.getId();
    BigDecimal montoInicial = apertura.getMontoInicial();
    BigDecimal ventasEfe = movDao.totalPorTipo(id, "EFECTIVO");
    BigDecimal ventasTrans = movDao.totalPorTipo(id, "TRANSFERENCIA"); 
    BigDecimal ventasCred = movDao.totalPorTipo(id, "CREDITO"); 
    
    BigDecimal totalSalidas = movDao.totalPorTipo(id, "RETIRO")
            .add(movDao.totalPorTipo(id, "DEVOLUCION"))
            .add(movDao.totalPorTipo(id, "ANULACION"));

    BigDecimal esperadoEfectivo = montoInicial.add(ventasEfe).subtract(totalSalidas.abs());
    BigDecimal saldoFinalTotal = montoInicial.add(ventasEfe).add(ventasTrans).add(ventasCred).subtract(totalSalidas.abs());

    CajaCierre cierre = new CajaCierre();
    cierre.setFecha(java.time.LocalDate.now());
    cierre.setHoraCierre(java.time.LocalTime.now());
    cierre.setMontoFinal(saldoFinalTotal);
    cierre.setTotalEfectivo(ventasEfe);
    cierre.setTotalTransferencia(ventasTrans);
    cierre.setTotalCredito(ventasCred);
    cierre.setTotalSalidas(totalSalidas);
    
    // La diferencia es: Lo que hay físicamente - Lo que debería haber en efectivo
    cierre.setDiferencia(montoFisicoReal.subtract(esperadoEfectivo)); 
    
    cierre.setUsuario(usuario);
    cierre.setIdApertura(id);
    
    return cierre;
}

    private void cargarResumen() {

        int id = apertura.getId();

        BigDecimal montoInicial = apertura.getMontoInicial();
        BigDecimal efectivo = movDao.totalPorTipo(id, "EFECTIVO");
        BigDecimal transferencia = movDao.totalPorTipo(id, "TRANSFERENCIA");
        BigDecimal credito = movDao.totalPorTipo(id, "CREDITO");

        BigDecimal devoluciones = movDao.totalPorTipo(id, "DEVOLUCION");
        BigDecimal anulaciones = movDao.totalPorTipo(id, "ANULACION");
        BigDecimal retiros = movDao.totalPorTipo(id, "RETIRO");
        
        BigDecimal difEfectivo = montoInicial.add(efectivo).subtract(retiros.abs());

        BigDecimal ingresos = efectivo
                .add(transferencia)
                .add(credito);

        BigDecimal egresos = retiros
                .add(devoluciones)
                .add(anulaciones);

        BigDecimal saldoFinal = montoInicial
                .add(ingresos)
                .subtract(egresos.abs());

        lblEfectivo.setText("Ventas Efectivo: $" + efectivo);
        lblTransferencia.setText("Ventas Transferencias: $" + transferencia);
        lblCredito.setText("Ventas Crédito: $" + credito);
        lblSalidas.setText("Salidas Efectivo: $" + egresos);
        lblSaldoFinal.setText("SALDO FINAL: $" + saldoFinal);
        lblEntradas.setText("Saldo inicial: $" + montoInicial);
        lblDifEfectivo.setText("Efectivo Esperado: $" + difEfectivo);
        lblDineroFisico.setText("Dinero Físico: ");
        lblDiferenciaCaja.setText("Diferencia de Caja: ");
        
    }
    
    private BigDecimal getMontoFisico() {
        try {
            String texto = txtDinero.getText().trim();
            return texto.isEmpty() ? BigDecimal.ZERO : new BigDecimal(texto);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
    
    private void actualizarDiferencia() {
    try {
        // 1. Obtener el valor esperado (puedes extraerlo de una variable global o recalcular)
        // Usamos el cálculo que ya tienes en cargarResumen()
        BigDecimal montoInicial = apertura.getMontoInicial();
        BigDecimal efectivoVentas = movDao.totalPorTipo(apertura.getId(), "EFECTIVO");
        BigDecimal retiros = movDao.totalPorTipo(apertura.getId(), "RETIRO");
        BigDecimal esperado = montoInicial.add(efectivoVentas).subtract(retiros.abs());

        // 2. Obtener lo ingresado por el usuario
        String textoFisico = txtDinero.getText().trim();
        BigDecimal fisico = textoFisico.isEmpty() ? BigDecimal.ZERO : new BigDecimal(textoFisico);

        // 3. Calcular diferencia (Físico - Esperado)
        BigDecimal diferencia = fisico.subtract(esperado);

        // 4. Formatear y mostrar con color
        if (diferencia.compareTo(BigDecimal.ZERO) < 0) {
            lblDiferenciaCaja.setForeground(Color.RED);
            lblDiferenciaCaja.setText("Diferencia: -$" + diferencia.abs() + " (Faltante)");
        } else if (diferencia.compareTo(BigDecimal.ZERO) > 0) {
            lblDiferenciaCaja.setForeground(new Color(0, 153, 51)); // Verde oscuro
            lblDiferenciaCaja.setText("Diferencia: +$" + diferencia + " (Sobrante)");
        } else {
            lblDiferenciaCaja.setForeground(Color.BLACK);
            lblDiferenciaCaja.setText("Diferencia: $0.00 (Caja Cuadrada)");
        }
    } catch (NumberFormatException e) {
        lblDiferenciaCaja.setText("Diferencia: --");
    }
}
    
    // Método auxiliar para cargar y redimensionar iconos de forma limpia
//    private ImageIcon obtenerIcono(String ruta) {
//        try {
//            java.net.URL url = getClass().getResource(ruta);
//            if (url != null) {
//                ImageIcon icon = new ImageIcon(url);
//                // Redimensionar a 24x24 para que se vea uniforme
//                Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
//                return new ImageIcon(img);
//            }
//        } catch (Exception e) {
//            System.out.println("Error al cargar icono: " + ruta);
//        }
//        return null; // Si falla, el botón simplemente no tendrá icono
//    }
}

