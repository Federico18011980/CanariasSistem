package Vista;

import Modelo.Caja;
import Modelo.CajaApertura;
import Modelo.CajaAperturaDao;
import Modelo.Cliente;
import Modelo.ClienteDao;
import Modelo.ConfigDao;
import Modelo.Detalle;
import Modelo.Productos;
import Modelo.Venta;
import Modelo.VentaDao;
import Modelo.ProductosDao;
import Modelo.CajaDao;
import Modelo.Config;
import Modelo.Pago;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Frame;
import java.text.DecimalFormat;
import javax.swing.SwingUtilities;


public class PanelVenta extends javax.swing.JPanel {

  private String vendedor;

    public PanelVenta(String vendedor) {
        this.vendedor = vendedor;
        initComponents();
        inicializarVenta();
    }

    private void inicializarVenta() {

        Vdao = new VentaDao();
        cl = new Cliente();
        cld = new ClienteDao();
        Dt = new Detalle();
        V = new Venta();
        detalles = new ArrayList<>();
        Tmp = new DefaultTableModel();
        cajadao = new CajaDao();
        caja = new Caja();
        prodao = new ProductosDao();
        cajaApDao = new CajaAperturaDao();

        modeloTabla = (DefaultTableModel) tablaNuevaVenta.getModel();

        totalPagar = BigDecimal.ZERO;

        item = 0;

        txtCodigoProVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_F1) {

                    Frame frame = (Frame) SwingUtilities.getWindowAncestor(PanelVenta.this);

                    VentaBuscarProducto ventana = new VentaBuscarProducto(frame);
                    ventana.setVisible(true);

                    Productos p = ventana.getProductoSeleccionado();

                    if (p != null) {
                        txtCodigoProVenta.setText(p.getCodigo());
                        txtDescripcionVenta.setText(p.getDescripcion());
                        txtCantidadVenta.setText("1");
                        txtPrecioVenta.setText(String.valueOf(p.getPrecio()));
                        txtStockVenta.setText(String.valueOf(p.getStock()));
                    } else {
                        JOptionPane.showMessageDialog(
                                PanelVenta.this,
                                "Ningún producto seleccionado"
                        );
                    }
                }
            }
        });

        txtRazonCV.setVisible(false);
        txtDireccionCV.setVisible(false);
        txtTelefonoCV.setVisible(false);

    }


    @SuppressWarnings("unchecked")
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nuevaVenta = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnEliminarNVenta = new javax.swing.JButton();
        txtCodigoProVenta = new javax.swing.JTextField();
        txtDescripcionVenta = new javax.swing.JTextField();
        txtCantidadVenta = new javax.swing.JTextField();
        txtPrecioVenta = new javax.swing.JTextField();
        txtStockVenta = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaNuevaVenta = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        labelTotal = new javax.swing.JLabel();
        labelTotalAPagar = new javax.swing.JLabel();
        btnConfirmarVenta = new javax.swing.JButton();
        txtTelefonoCV = new javax.swing.JTextField();
        txtDireccionCV = new javax.swing.JTextField();
        txtRazonCV = new javax.swing.JTextField();
        txtIdProdNV = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtDomicilioClienteNV = new javax.swing.JTextField();
        jLabelTotal = new javax.swing.JLabel();
        LabelSubtotal = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        btnFormaPago = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1005, 670));

        nuevaVenta.setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Código F1 para buscar");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Descripcion");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Cantidad");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Precio");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Stock");

        btnEliminarNVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEliminarNVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/quitar 20x20.png"))); // NOI18N
        btnEliminarNVenta.setText("Quitar ");
        btnEliminarNVenta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEliminarNVenta.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnEliminarNVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarNVentaActionPerformed(evt);
            }
        });

        txtCodigoProVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoProVentaActionPerformed(evt);
            }
        });
        txtCodigoProVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoProVentaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoProVentaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoProVentaKeyTyped(evt);
            }
        });

        txtDescripcionVenta.setEditable(false);

        txtCantidadVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadVentaActionPerformed(evt);
            }
        });
        txtCantidadVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyPressed(evt);
            }
        });

        txtPrecioVenta.setEditable(false);
        txtPrecioVenta.setBackground(new java.awt.Color(255, 255, 255));
        txtPrecioVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioVentaActionPerformed(evt);
            }
        });

        txtStockVenta.setEditable(false);

        tablaNuevaVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descripcion", "Cantidad", "Precio", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaNuevaVenta);

        jLabel8.setText("DNI");

        jLabel9.setText("Nombre");

        txtDni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDniKeyPressed(evt);
            }
        });

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        jLabel10.setText("Email");

        labelTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTotal.setText("Total a Pagar");

        labelTotalAPagar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelTotalAPagar.setText("----------");

        btnConfirmarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/confirmarVenta 24x24.png"))); // NOI18N
        btnConfirmarVenta.setText("Confirmar Venta");
        btnConfirmarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarVentaActionPerformed(evt);
            }
        });

        txtDireccionCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionCVActionPerformed(evt);
            }
        });

        jLabel11.setText("Domicilio");

        jLabelTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelTotal.setText("Total");

        LabelSubtotal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        LabelSubtotal.setText("------------");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel50.setText("Datos del Cliente");

        btnFormaPago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/FormaDePago 24x24.png"))); // NOI18N
        btnFormaPago.setText("Forma de Pago");
        btnFormaPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFormaPagoActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/escoba 20x20.png"))); // NOI18N
        jButton1.setText("Limpiar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Impiar producto");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Quitar producto de la tabla");

        javax.swing.GroupLayout nuevaVentaLayout = new javax.swing.GroupLayout(nuevaVenta);
        nuevaVenta.setLayout(nuevaVentaLayout);
        nuevaVentaLayout.setHorizontalGroup(
            nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nuevaVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(nuevaVentaLayout.createSequentialGroup()
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigoProVenta)
                            .addGroup(nuevaVentaLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 8, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(nuevaVentaLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nuevaVentaLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nuevaVentaLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(35, 35, 35))
                            .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(nuevaVentaLayout.createSequentialGroup()
                                .addComponent(txtStockVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIdProdNV, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(51, 51, 51)
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(nuevaVentaLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(btnEliminarNVenta)))
                        .addContainerGap(28, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nuevaVentaLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(nuevaVentaLayout.createSequentialGroup()
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(nuevaVentaLayout.createSequentialGroup()
                                .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(nuevaVentaLayout.createSequentialGroup()
                                        .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(39, 39, 39)
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(nuevaVentaLayout.createSequentialGroup()
                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDomicilioClienteNV, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel50)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nuevaVentaLayout.createSequentialGroup()
                                .addComponent(txtDireccionCV, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTelefonoCV, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRazonCV, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(54, 54, 54)
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnConfirmarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFormaPago, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nuevaVentaLayout.createSequentialGroup()
                                .addComponent(labelTotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelTotalAPagar))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nuevaVentaLayout.createSequentialGroup()
                                .addComponent(jLabelTotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LabelSubtotal)))
                        .addGap(48, 48, 48))))
        );
        nuevaVentaLayout.setVerticalGroup(
            nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nuevaVentaLayout.createSequentialGroup()
                .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(nuevaVentaLayout.createSequentialGroup()
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(nuevaVentaLayout.createSequentialGroup()
                                .addContainerGap(2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigoProVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtStockVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtIdProdNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(nuevaVentaLayout.createSequentialGroup()
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminarNVenta)
                            .addComponent(jButton1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nuevaVentaLayout.createSequentialGroup()
                        .addComponent(jLabel50)
                        .addGap(3, 3, 3)
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDireccionCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRazonCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDomicilioClienteNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(btnConfirmarVenta)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nuevaVentaLayout.createSequentialGroup()
                        .addComponent(btnFormaPago)
                        .addGap(43, 43, 43))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nuevaVentaLayout.createSequentialGroup()
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTotal)
                            .addComponent(LabelSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(nuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelTotal)
                            .addComponent(labelTotalAPagar))))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(nuevaVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(nuevaVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarNVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarNVentaActionPerformed
        // TODO add your handling code here:
        if (tablaNuevaVenta.getSelectedRow() == -1) {

            JOptionPane.showInternalMessageDialog(null, "Debe seleccionar un articulo para eliminar", "ELIMINAR", 0);

        } else {
            DefaultTableModel modelo = (DefaultTableModel) tablaNuevaVenta.getModel();
            modelo.removeRow(tablaNuevaVenta.getSelectedRow());
            TotalPagar();
            txtCodigoProVenta.requestFocus();
            //this.aplicarDescuento();
        }

    }//GEN-LAST:event_btnEliminarNVentaActionPerformed

    private void txtCodigoProVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoProVentaActionPerformed

    private void txtCodigoProVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProVentaKeyPressed
        
       Productos pro = new Productos();
       ProductosDao prodao = new ProductosDao();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (!"".equals(txtCodigoProVenta.getText())) {

                String cod = txtCodigoProVenta.getText();

                pro = prodao.BuscarProducto(cod);

                if (pro.getDescripcion() != null) {

                    txtDescripcionVenta.setText(pro.getDescripcion());

                    // Precio en BigDecimal
                    BigDecimal precio = pro.getPrecio();

                    // Redondear hacia arriba (equivalente a Math.ceil)
                    BigDecimal precioRedondeado = precio.setScale(0, RoundingMode.CEILING);

                    txtPrecioVenta.setText(precioRedondeado.toString());
                    txtStockVenta.setText(pro.getStock().toString());

                    txtCantidadVenta.requestFocus();

                } else {

                    LimpiarVenta();
                    txtCodigoProVenta.requestFocus();

                }

            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el código del producto");
                txtCodigoProVenta.requestFocus();
            }

        }

    }//GEN-LAST:event_txtCodigoProVentaKeyPressed

    private void txtCodigoProVentaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProVentaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoProVentaKeyReleased

    private void txtCodigoProVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProVentaKeyTyped
        // TODO add your handling code here:
        //event.numberKeyPress(evt);
    }//GEN-LAST:event_txtCodigoProVentaKeyTyped

    private void txtCantidadVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadVentaActionPerformed

    private void txtCantidadVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (!"".equals(txtCantidadVenta.getText())) {

                try {
                    String cod = txtCodigoProVenta.getText();
                    String descripcion = txtDescripcionVenta.getText();

                    // Cantidad como BigDecimal (PERMITE DECIMALES)
                    BigDecimal cant = new BigDecimal(
                        txtCantidadVenta.getText().replace(",", ".")
                    );

                    // Precio como BigDecimal
                    BigDecimal precio = new BigDecimal(
                        txtPrecioVenta.getText().replace(",", ".")
                    ).setScale(2, RoundingMode.HALF_UP);

                    // total = precio * cantidad
                    BigDecimal total = precio.multiply(cant);
                   

                    // Stock también en BigDecimal
                    BigDecimal stock = new BigDecimal(
                        txtStockVenta.getText().replace(",", ".")
                    );

                    if (stock.compareTo(cant) >= 0) {

                        item++;
                       this.Tmp = (DefaultTableModel) tablaNuevaVenta.getModel();

                        // evitar duplicados
                        for (int i = 0; i < this.Tmp.getRowCount(); i++) {
                            if (this.Tmp.getValueAt(i, 1).equals(descripcion)) {
                                JOptionPane.showMessageDialog(null, "El producto ya está registrado");
                                LimpiarVenta();
                                txtCodigoProVenta.requestFocus();
                                return;
                            }
                        }

                        Object[] fila = new Object[5];
                        fila[0] = cod;
                        fila[1] = descripcion;
                        fila[2] = cant.toPlainString();    // conserva decimales exactos
                        fila[3] = precio.toPlainString();
                        fila[4] = total.setScale(2, RoundingMode.HALF_UP).toPlainString();

                        this.Tmp.addRow(fila);
                        tablaNuevaVenta.setModel(this.Tmp);

                        TotalPagar();
                        LimpiarVenta();
                        txtCodigoProVenta.requestFocus();

                    } else {
                        JOptionPane.showMessageDialog(null, "Stock no disponible");
                    }

                } catch (HeadlessException ex) {
                    JOptionPane.showMessageDialog(null, "Error en los datos numéricos: " + ex.getMessage());
                }

            } else {
                JOptionPane.showMessageDialog(null, "Ingrese cantidad");
            }

        }

    }//GEN-LAST:event_txtCantidadVentaKeyPressed

    private void txtPrecioVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioVentaActionPerformed

    private void txtDniKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (!"".equals(txtDni.getText())) {

                this.cl = this.cld.BuscarCliente(Integer.parseInt(this.txtDni.getText()));
                if (this.cl.getNombre() != null) {

                    this.txtNombre.setText("" + this.cl.getNombre());
                    this.txtEmail.setText("" + this.cl.getEmail());
                    this.txtDireccionCV.setText("" + this.cl.getDomicilio());
                    this.txtDomicilioClienteNV.setText("" + this.cl.getDomicilio());
                    this.txtTelefonoCV.setText("" + this.cl.getTelefono());
                    this.txtRazonCV.setText("" + this.cl.getRazon());

                } else {
                    txtDni.setText("");
                    JOptionPane.showMessageDialog(null, "El Cliente No Existe");

                }
            }

        }
    }//GEN-LAST:event_txtDniKeyPressed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void btnConfirmarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarVentaActionPerformed
    
        // Verificar si hay una caja abierta antes de registrar la venta
        
        CajaApertura ap = cajaApDao.cajaAbiertaDelDia();
        
        if (ap == null) {
            JOptionPane.showMessageDialog(this, "No hay caja abierta. No se puede registrar la venta.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Si no hay caja abierta, cancelamos la operación
        }
    if (pagos == null || pagos.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe registrar el pago antes de imprimir");
        return;
    }
        if (V.getCliente() == null || V.getCliente().trim().isEmpty()) {
            V.setCliente("CONSUMIDOR FINAL");
            
        }
        V.setVendedor(vendedor);
      //  V.setTotal();

    List<Detalle> listaDetalle = new ArrayList<>();

    for (int i = 0; i < tablaNuevaVenta.getRowCount(); i++) {
        Detalle d = new Detalle();
        d.setCod_pro(tablaNuevaVenta.getValueAt(i, 0).toString());
        d.setCant(new BigDecimal(tablaNuevaVenta.getValueAt(i, 2).toString()));
        d.setPrecio(new BigDecimal(tablaNuevaVenta.getValueAt(i, 3).toString()));
        listaDetalle.add(d);
    }

   // int idApertura = cajaApDao.obtenerIdAperturaActiva();
    int idApertura = ap.getId(); // Obtener el id de apertura activa
    String usuarioActual = vendedor;

    boolean ok = new VentaDao().registrarVentaConPagos(
            V,
            listaDetalle,
            pagos,
            idApertura,
            usuarioActual
    );

    if (ok) {
        JOptionPane.showMessageDialog(this, "Venta registrada correctamente");
        ActualizarStock();
        pdf(pagos);
        LimpiarTablaVenta();
        LimpiarDatosCliente();
        labelTotalAPagar.setText("------------");
        LabelSubtotal.setText("------------");
        pagos.clear();
    } else {
        JOptionPane.showMessageDialog(this, "Error al registrar la venta");
    }

        labelTotalAPagar.setText("------------");
        LabelSubtotal.setText("------------");
    }//GEN-LAST:event_btnConfirmarVentaActionPerformed

    private void txtDireccionCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionCVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionCVActionPerformed
    private List<Pago> pagos = new ArrayList<>();
    
    private void btnFormaPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFormaPagoActionPerformed
        // TODO add your handling code here:
        totalVenta = totalpagar ;
    if (tablaNuevaVenta.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Debe agregar productos");
        return;
    }

    totalVenta = totalpagar;

    DialogPagos dlg = new DialogPagos(
            (Frame) SwingUtilities.getWindowAncestor(this),
            totalVenta
    );

    dlg.setVisible(true);
    
    //List<Pago> pagos = dlg.getPagos();
    
    pagos = dlg.getPagos();

    if (pagos == null || pagos.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se cargaron pagos");
        return;
    }

    JOptionPane.showMessageDialog(this, "Pagos cargados correctamente");
    
        // ✅ ACÁ TOMAMOS EL TOTAL REAL
        BigDecimal totalFinal = dlg.getTotalPagado();
        montoBruto = dlg.getMontoBruto();
        totalFinalPdf = totalFinal;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        labelTotalAPagar.setText(df.format(totalFinalPdf));
        
        V.setTotal(totalFinal);
 
    }//GEN-LAST:event_btnFormaPagoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        LimpiarVenta();
    }//GEN-LAST:event_jButton1ActionPerformed
    // ====== ESTADO DE LA VENTA ======

private Cliente cl;
private DefaultTableModel Tmp;
private DefaultTableModel modeloTabla;
private List<Detalle> detalles;
private ClienteDao cld;
private VentaDao Vdao;
private Detalle Dt;
private Venta V;
private CajaDao cajadao;
private Caja caja;
private ProductosDao prodao;

private CajaAperturaDao cajaApDao; 

Date fechaVenta = new Date();
String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(fechaVenta);
private BigDecimal totalPagar;
private BigDecimal totalpagar = BigDecimal.ZERO;
private BigDecimal totalVenta = BigDecimal.ZERO;
private BigDecimal montoBruto = BigDecimal.ZERO;
private BigDecimal totalFinalPdf = BigDecimal.ZERO;
private int item;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelSubtotal;
    private javax.swing.JButton btnConfirmarVenta;
    private javax.swing.JButton btnEliminarNVenta;
    private javax.swing.JButton btnFormaPago;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JLabel labelTotalAPagar;
    private javax.swing.JPanel nuevaVenta;
    private javax.swing.JTable tablaNuevaVenta;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtCodigoProVenta;
    private javax.swing.JTextField txtDescripcionVenta;
    private javax.swing.JTextField txtDireccionCV;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtDomicilioClienteNV;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtIdProdNV;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtRazonCV;
    private javax.swing.JTextField txtStockVenta;
    private javax.swing.JTextField txtTelefonoCV;
    // End of variables declaration//GEN-END:variables

    private void LimpiarVenta(){
        txtCodigoProVenta.setText("");
        txtCantidadVenta.setText("");
        txtDescripcionVenta.setText("");
        txtPrecioVenta.setText("");
        txtStockVenta.setText("");
        txtIdProdNV.setText("");
    
    }
    
        
private void TotalPagar() {

    BigDecimal totalPagar = BigDecimal.ZERO;

    int numFila = tablaNuevaVenta.getRowCount();

    for (int i = 0; i < numFila; i++) {

        Object valorCelda = tablaNuevaVenta.getModel().getValueAt(i, 4);

        if (valorCelda == null) continue;

        String valorTexto = valorCelda.toString().replace(",", ".");

        try {

            BigDecimal valor = new BigDecimal(valorTexto);
            totalPagar = totalPagar.add(valor);

        } catch (Exception e) {
            System.out.println("Error al convertir valor de fila " + i + ": " + valorTexto);
        }
    }

    // ----> ESTA LÍNEA ES NECESARIA Y FALTABA <----
    totalpagar = totalPagar.setScale(2, RoundingMode.HALF_UP);

    // Mostramos el total
    LabelSubtotal.setText(totalpagar.toString());
    labelTotalAPagar.setText(totalpagar.toString());
}


     
    private void LimpiarTablaVenta() {
        Tmp = (DefaultTableModel) tablaNuevaVenta.getModel();
        int fila = tablaNuevaVenta.getRowCount();

        for (int i = 0; i < fila; i++) {

            Tmp.removeRow(0);

        }

    }
    



    private void ActualizarStock() {

        for (int i = 0; i < tablaNuevaVenta.getRowCount(); i++) {

            String cod = tablaNuevaVenta.getValueAt(i, 0).toString();

            BigDecimal cant = new BigDecimal(tablaNuevaVenta.getValueAt(i, 2).toString());

            Productos prod = prodao.BuscarProducto(cod);

            BigDecimal stockActual = prod.getStock().subtract(cant);

            prodao.ActualizarStock(stockActual, cod);
        }
    }
    
    private void LimpiarDatosCliente() {
        txtDni.setText("");
        txtDomicilioClienteNV.setText("");
        txtNombre.setText("");
        txtEmail.setText("");
        txtTelefonoCV.setText("");
        txtDireccionCV.setText("");
        txtRazonCV.setText("");
    }
    
    
    private void pdf(List<Pago> pagos) {
    try {
        int id = Vdao.IdVenta();  // ID de la venta actual

        // ===== CREAR DIRECTORIO SI NO EXISTE =====
        String userHome = System.getProperty("user.home");
        String rutaCarpeta = userHome + "/Documents/Canarias/Comprobantes";
        File carpetaPDF = new File(rutaCarpeta);

        if (!carpetaPDF.exists()) {
            carpetaPDF.mkdirs();  // Crea la carpeta completa
        }

        // ===== ARCHIVO PDF =====
        File file = new File(rutaCarpeta + "/venta" + id + ".pdf");
        try (FileOutputStream archivo = new FileOutputStream(file)) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            
     
           // ===== IMAGEN DEL LOGO (Recurso Interno) =====
            Image img = null;
            try {
                // Cargamos el recurso como un stream de bytes
                java.io.InputStream is = getClass().getResourceAsStream("/Img/LogoCabecera200x200.png");

                if (is != null) {
                    byte[] bytes = is.readAllBytes(); // Lee la imagen completa
                    img = Image.getInstance(bytes);
                    img.scaleToFit(100, 100);
                    is.close();
                } else {
                    System.err.println("No se pudo encontrar el logo en el recurso interno /Img/...");
                }
            } catch (Exception e) {
                System.err.println("Error cargando logo interno: " + e.getMessage());
            }

// ... El resto del código (PdfPCell imgCell, etc.) se mantiene igual ...
         
            // ===== ENCABEZADO =====
            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            fecha.add("Factura: " + id + "\n" +
                    "Fecha: " + new SimpleDateFormat("dd-MM-yyyy HH-mm").format(new Date()));
            
            PdfPTable Encabezado = new PdfPTable(4);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);
            Encabezado.setWidths(new float[]{20f, 30f, 70f, 40f});
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);
            
            // Imagen
            // ... luego, al añadirlo a la celda ...
            
            if (img != null) {
                PdfPCell imgCell = new PdfPCell(img);
                imgCell.setBorder(0);
                imgCell.setRowspan(3);
                Encabezado.addCell(imgCell);
            } else {
                // Si no hay imagen, añadimos una celda vacía para no romper la tabla
                PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
                emptyCell.setBorder(0);
                emptyCell.setRowspan(3);
                Encabezado.addCell(emptyCell);
            }
            
            
//            PdfPCell imgCell = new PdfPCell(img);
//            imgCell.setBorder(0);
//            imgCell.setRowspan(3);
//            Encabezado.addCell(imgCell);
            
            // Datos empresa
            Config conf = new ConfigDao().obtenerConfig();
            String cuit = conf.getCuit();
            String nombre = conf.getNombre();
            String telef = conf.getTelefono();
            String domicilio = conf.getDireccion();
            Encabezado.addCell("");
            Encabezado.addCell("Cuit: " + cuit +
                    "\nNombre: " + nombre +
                    "\nTelefono: " + telef +
                    "\nDomicilio: " + domicilio);
            Encabezado.addCell(fecha);
            
            doc.add(Encabezado);
            
            // ===== DATOS DEL CLIENTE =====
            Paragraph Cli = new Paragraph();
            Cli.add(Chunk.NEWLINE);
            Cli.add("Datos del Cliente\n");
            doc.add(Cli);
            
            PdfPTable tablaCli = new PdfPTable(4);
            tablaCli.setWidthPercentage(100);
            tablaCli.getDefaultCell().setBorder(0);
            tablaCli.setWidths(new float[]{20f, 50f, 30f, 40f});
            tablaCli.setHorizontalAlignment(Element.ALIGN_LEFT);
            
            PdfPCell cl1 = new PdfPCell(new Phrase("DNI/CUIT", negrita));
            PdfPCell cl2 = new PdfPCell(new Phrase("Nombre", negrita));
            PdfPCell cl3 = new PdfPCell(new Phrase("Telefono", negrita));
            PdfPCell cl4 = new PdfPCell(new Phrase("Direccion", negrita));
            
            cl1.setBorder(0); cl2.setBorder(0); cl3.setBorder(0); cl4.setBorder(0);
            
            tablaCli.addCell(cl1); tablaCli.addCell(cl2);
            tablaCli.addCell(cl3); tablaCli.addCell(cl4);
            
            tablaCli.addCell(txtDni.getText());
            tablaCli.addCell(txtNombre.getText());
            tablaCli.addCell(txtTelefonoCV.getText());
            tablaCli.addCell(txtDireccionCV.getText());
            
            doc.add(tablaCli);
            
            // ===== ESPACIO =====
            Paragraph espacio = new Paragraph();
            espacio.add(Chunk.NEWLINE);
            doc.add(espacio);
            
          
        // ===== TABLA PRODUCTOS =====
            PdfPTable tablaprod = new PdfPTable(5);
            tablaprod.setWidthPercentage(100);
            tablaprod.getDefaultCell().setBorder(0);
            tablaprod.setWidths(new float[]{10f, 10f, 44f, 18f, 18f});
            
            String[] headers = {"Cantidad", "Código", "Descripcion", "Precio U", "Total"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, negrita));
                cell.setBorder(0);
                cell.setBackgroundColor(BaseColor.GRAY);
                tablaprod.addCell(cell);
            }
            
            for (int i = 0; i < tablaNuevaVenta.getRowCount(); i++) {
                tablaprod.addCell(tablaNuevaVenta.getValueAt(i, 2).toString());
                tablaprod.addCell(tablaNuevaVenta.getValueAt(i, 0).toString());
                tablaprod.addCell(tablaNuevaVenta.getValueAt(i, 1).toString());
                tablaprod.addCell(tablaNuevaVenta.getValueAt(i, 3).toString());
               // tablaprod.addCell(etiquetaDescuento);
                tablaprod.addCell(tablaNuevaVenta.getValueAt(i, 4).toString());
            }
            
            doc.add(tablaprod);
            
            // ===== PAGOS =====
            PdfPTable tablaPagos = new PdfPTable(4);
            tablaPagos.setWidthPercentage(100);
            tablaPagos.setSpacingBefore(10);
            
            tablaPagos.addCell("Forma");
            tablaPagos.addCell("Monto");
            tablaPagos.addCell("Descuento");
            tablaPagos.addCell("Total");
            
            for (Pago p : pagos) {
                tablaPagos.addCell(p.getTipo().name());
                tablaPagos.addCell("$ " + p.getMontoBruto());
                tablaPagos.addCell("$ " + p.getDescuento());
                tablaPagos.addCell("$ " + p.getMontoFinal());
            }
            
            doc.add(tablaPagos);
            
            // ===== INFORMACIÓN FINAL =====
           
            Paragraph totales = new Paragraph();
            totales.add("\nTOTAL BRUTO: $ " + montoBruto);
            totales.add("\nDESCUENTO: $ " + montoBruto.subtract(totalFinalPdf));
            totales.add("\nTOTAL A PAGAR: $ " + totalFinalPdf);
            totales.setAlignment(Element.ALIGN_RIGHT);
            
            doc.add(totales);
            
            Paragraph info = new Paragraph();
            info.add(Chunk.NEWLINE);
            info.add("Total a Pagar: " + totalFinalPdf);
          //  info.add("\n\nF. de Pago: " + lblSeleccionFdePago.getText());
            info.setAlignment(Element.ALIGN_RIGHT);
            doc.add(info);
            
            Paragraph mensaje = new Paragraph();
            mensaje.add(Chunk.NEWLINE);
            mensaje.add("CANARIAS Agradece su preferencia!");
            mensaje.setAlignment(Element.ALIGN_CENTER);
            doc.add(mensaje);
            
            doc.close();
        }

        // ===== CONFIRMAR APERTURA =====
        int respuesta = JOptionPane.showConfirmDialog(
                null,
                "Archivo PDF Generado en:\n" + file.getAbsolutePath() +
                "\n\n¿Desea abrirlo ahora?",
                "PDF guardado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            Desktop.getDesktop().open(file);
        }

    } catch (DocumentException | IOException e) {
        System.out.println(e.toString());
    }
}
 

}
