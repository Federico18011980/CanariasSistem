package Vista;

import Modelo.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;

public class CajaView extends javax.swing.JFrame {

    CajaAperturaDao aperturaDao = new CajaAperturaDao();
    private ResumenCajaView resumenCaja;

    CajaApertura aperturaActual;
    String usuario;

    public CajaView(String usuarioLogueado) {
        initComponents();
        setLocationRelativeTo(null);
        usuario = usuarioLogueado;
        cargarEstadoCaja();
    }

    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Administración de Caja");
        setSize(400, 420);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Administración de Caja");
        lblTitulo.setBounds(110, 10, 200, 30);
        add(lblTitulo);

        JLabel lblMontoInicial = new JLabel("Monto inicial:");
        lblMontoInicial.setBounds(30, 70, 120, 25);
        add(lblMontoInicial);

        txtMontoInicial = new JTextField();
        txtMontoInicial.setBounds(150, 70, 150, 25);
        add(txtMontoInicial);

        btnAbrir = new JButton("Abrir Caja");
        btnAbrir.setBounds(120, 110, 150, 35);
        btnAbrir.addActionListener(e -> abrirCaja());
        add(btnAbrir);

        lblInfo = new JLabel("Caja no abierta.");
        lblInfo.setBounds(30, 160, 350, 25);
        add(lblInfo);

        btnCerrar = new JButton("Cerrar Caja");
        btnCerrar.setBounds(120, 300, 150, 40);
        btnCerrar.addActionListener(e -> cerrarCaja());
        btnCerrar.setEnabled(false);
        add(btnCerrar);
    }

    // =====================================================
    private void cargarEstadoCaja() {

        aperturaActual = aperturaDao.cajaAbiertaDelDia();

        if (aperturaActual == null) {
            lblInfo.setText("Caja no abierta.");
            btnAbrir.setEnabled(true);
            btnCerrar.setEnabled(false);
        } else {
            lblInfo.setText(
                "<html>Caja abierta<br>" +
                "Inicio: " + aperturaActual.getHoraInicio() +
                "<br>Monto inicial: $" + aperturaActual.getMontoInicial() +
                "</html>"
            );

            btnAbrir.setEnabled(false);
            btnCerrar.setEnabled(true);
        }
    }

    // =====================================================
    private void abrirCaja() {

        if (txtMontoInicial.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un monto inicial");
            return;
        }

        BigDecimal monto;
        try {
            monto = new BigDecimal(txtMontoInicial.getText().replace(",", "."));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Monto inválido");
            return;
        }

        CajaApertura apertura = new CajaApertura();
        apertura.setFecha(LocalDate.now());
        apertura.setHoraInicio(LocalTime.now());
        apertura.setMontoInicial(monto);
        apertura.setUsuario(usuario);

        if (aperturaDao.abrirCaja(apertura)) {
            JOptionPane.showMessageDialog(this, "Caja abierta correctamente");
            cargarEstadoCaja();
        } else {
            JOptionPane.showMessageDialog(this, "Error al abrir caja");
        }
    }

    // =====================================================

    private void cerrarCaja(){
        resumenCaja = new ResumenCajaView(usuario); 
        
        if (resumenCaja == null || !resumenCaja.isVisible()) {
            resumenCaja = new ResumenCajaView(usuario);
            resumenCaja.setLocationRelativeTo(null);
            resumenCaja.setVisible(true);
        } else {
            resumenCaja.toFront();
            resumenCaja.requestFocus();
        }
    
    }

    
    // COMPONENTES
    private JButton btnAbrir;
    private JButton btnCerrar;
    private JTextField txtMontoInicial;
    private JLabel lblInfo;
}
