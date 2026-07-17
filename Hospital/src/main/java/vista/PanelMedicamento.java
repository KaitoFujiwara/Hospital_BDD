package vista;

import controlador.MedicamentoControl;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Medicamento;

public class PanelMedicamento extends javax.swing.JPanel {

    private MedicamentoControl medicamentoControl;

    public PanelMedicamento() {
        initComponents();
        configurarTabla();
        agregarEventosManuales();
    }

    public PanelMedicamento(MedicamentoControl medicamentoControl) {
        initComponents();
        this.medicamentoControl = medicamentoControl;
        configurarTabla();
        actualizarTabla();
        agregarEventosManuales();
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Medicamento", "Dosis", "Descripción"}, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        Tabla.setModel(modelo);
    }

    // Carga los datos de la fila seleccionada automáticamente en los campos de texto
    private void agregarEventosManuales() {
        Tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int filaSeleccionada = Tabla.getSelectedRow();
                if (filaSeleccionada != -1) {
                    txtID.setText(Tabla.getValueAt(filaSeleccionada, 0).toString());
                    txtNombre.setText(Tabla.getValueAt(filaSeleccionada, 1).toString());
                    txtdosis.setText(Tabla.getValueAt(filaSeleccionada, 2).toString());
                    txtinfo.setText(Tabla.getValueAt(filaSeleccionada, 3).toString());
                }
            }
        });
    }

    public void actualizarTabla() {
        if (medicamentoControl == null) {
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) Tabla.getModel();
        modelo.setRowCount(0);

        for (Medicamento medicamento : medicamentoControl.verMedicamentos()) {
            modelo.addRow(new Object[]{
                medicamento.getIdMedicamento(),
                medicamento.getNombreMedicamento(),
                medicamento.getDosis(),
                medicamento.getDescripcion()
            });
        }
    }

    private void buscarMedicamento() {
        if (medicamentoControl == null) {
            JOptionPane.showMessageDialog(this, "No se recibió el controlador");
            return;
        }

        String textoId = BuscarM.getText().trim();

        if (textoId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID del medicamento");
            return;
        }

        int idMedicamento;
        try {
            idMedicamento = Integer.parseInt(textoId);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
            return;
        }

        Medicamento medicamento = medicamentoControl.buscarMedicamento(idMedicamento);
        DefaultTableModel modelo = (DefaultTableModel) Tabla.getModel();
        modelo.setRowCount(0);

        if (medicamento == null) {
            JOptionPane.showMessageDialog(this, "Medicamento no encontrado");
            return;
        }

        modelo.addRow(new Object[]{
            medicamento.getIdMedicamento(),
            medicamento.getNombreMedicamento(),
            medicamento.getDosis(),
            medicamento.getDescripcion()
        });
    }

    private void limpiarCampos() {
        txtID.setText("");
        txtNombre.setText("");
        txtdosis.setText("");
        txtinfo.setText("");
        Tabla.clearSelection();
    }

    private void btnAñadirActionPerformed(java.awt.event.ActionEvent evt) {
        if (medicamentoControl == null) {
            JOptionPane.showMessageDialog(this, "Controlador no disponible");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String dosis = txtdosis.getText().trim();
        String descripcion = txtinfo.getText().trim();

        if (nombre.isEmpty() || dosis.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos (Nombre, Dosis, Descripción) son obligatorios.");
            return;
        }

        boolean exito = medicamentoControl.crearMedicamento(nombre, dosis, descripcion);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Medicamento añadido correctamente.");
            limpiarCampos();
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al añadir el medicamento.");
        }
    }

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {
        if (medicamentoControl == null) {
            JOptionPane.showMessageDialog(this, "Controlador no disponible");
            return;
        }

        String idText = txtID.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un medicamento de la tabla para modificar.");
            return;
        }

        int id = Integer.parseInt(idText);
        String nombre = txtNombre.getText().trim();
        String dosis = txtdosis.getText().trim();
        String descripcion = txtinfo.getText().trim();

        if (nombre.isEmpty() || dosis.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos no pueden quedar vacíos.");
            return;
        }

        boolean exito = medicamentoControl.modificarMedicamento(id, nombre, dosis, descripcion);

        if (exito) {
            JOptionPane.showMessageDialog(this, "Medicamento actualizado correctamente.");
            limpiarCampos();
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el medicamento.");
        }
    }

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        if (medicamentoControl == null) {
            JOptionPane.showMessageDialog(this, "Controlador no disponible");
            return;
        }

        String idText = txtID.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un medicamento de la tabla para eliminar.");
            return;
        }

        int id = Integer.parseInt(idText);
        int confirmar = JOptionPane.showConfirmDialog(
                this, 
                "¿Está seguro de que desea eliminar este medicamento?", 
                "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar == JOptionPane.YES_OPTION) {
            boolean exito = medicamentoControl.eliminarMedicamento(id);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Medicamento eliminado correctamente.");
                limpiarCampos();
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el medicamento.");
            }
        }
    }

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        buscarMedicamento();
    }

    private void todosActionPerformed(java.awt.event.ActionEvent evt) {
        limpiarCampos();
        actualizarTabla();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblIDBuscar = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BuscarM = new javax.swing.JTextPane();
        btnBuscar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        todos = new javax.swing.JButton();
        btnAñadir = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        lblID = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        lblnombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lbldosis = new javax.swing.JLabel();
        txtdosis = new javax.swing.JTextField();
        lblinfo = new javax.swing.JLabel();
        txtinfo = new javax.swing.JTextField();

        // Configuración del Panel Principal (BorderLayout para comportamiento responsivo)
        this.setLayout(new java.awt.BorderLayout(15, 15));
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- ZONA NORTE: Título ---
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); 
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Información de Medicamentos");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        this.add(jLabel1, java.awt.BorderLayout.NORTH);

        // --- ZONA CENTRAL: Formulario + Botones de Acción ---
        // panelCentro sirve como contenedor alineado al centro para evitar que se separen
        javax.swing.JPanel panelCentro = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 45, 10));

        // 1. Subpanel: Formulario (GridBagLayout para perfecta alineación de etiquetas y campos)
        javax.swing.JPanel panelFormulario = new javax.swing.JPanel(new java.awt.GridBagLayout());
        panelFormulario.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Medicamento"));
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(6, 8, 6, 8);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        // Fila 0: ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        lblID.setText("ID:");
        panelFormulario.add(lblID, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtID.setEditable(false);
        txtID.setPreferredSize(new java.awt.Dimension(220, 26));
        panelFormulario.add(txtID, gbc);

        // Fila 1: Nombre
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        lblnombre.setText("Nombre:");
        panelFormulario.add(lblnombre, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNombre.setPreferredSize(new java.awt.Dimension(220, 26));
        panelFormulario.add(txtNombre, gbc);

        // Fila 2: Dosis
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        lbldosis.setText("Dosis:");
        panelFormulario.add(lbldosis, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtdosis.setPreferredSize(new java.awt.Dimension(220, 26));
        panelFormulario.add(txtdosis, gbc);

        // Fila 3: Descripción
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        lblinfo.setText("Descripción:");
        panelFormulario.add(lblinfo, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtinfo.setPreferredSize(new java.awt.Dimension(220, 26));
        panelFormulario.add(txtinfo, gbc);

        // 2. Subpanel: Botones de Acción (GridBagLayout para apilamiento vertical uniforme)
        javax.swing.JPanel panelBotonesAccion = new javax.swing.JPanel(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbcBtn = new java.awt.GridBagConstraints();
        gbcBtn.insets = new java.awt.Insets(8, 0, 8, 0);
        gbcBtn.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbcBtn.gridx = 0;

        btnAñadir.setText("Añadir medicamento");
        btnAñadir.setPreferredSize(new java.awt.Dimension(180, 30));
        btnAñadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAñadirActionPerformed(evt);
            }
        });
        gbcBtn.gridy = 0;
        panelBotonesAccion.add(btnAñadir, gbcBtn);

        btnModificar.setText("Modificar medicamento");
        btnModificar.setPreferredSize(new java.awt.Dimension(180, 30));
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        gbcBtn.gridy = 1;
        panelBotonesAccion.add(btnModificar, gbcBtn);

        btnEliminar.setText("Eliminar medicamento");
        btnEliminar.setPreferredSize(new java.awt.Dimension(180, 30));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        gbcBtn.gridy = 2;
        panelBotonesAccion.add(btnEliminar, gbcBtn);

        // Añadimos ambos subpaneles al contenedor central
        panelCentro.add(panelFormulario);
        panelCentro.add(panelBotonesAccion);
        this.add(panelCentro, java.awt.BorderLayout.CENTER);

        // --- ZONA SUR: Búsqueda y Tabla ---
        javax.swing.JPanel panelSur = new javax.swing.JPanel(new java.awt.BorderLayout(10, 10));

        // Subpanel superior de búsqueda
        javax.swing.JPanel panelBusqueda = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 5));
        lblIDBuscar.setText("Ingrese ID de medicamento:");
        panelBusqueda.add(lblIDBuscar);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(150, 26));
        jScrollPane1.setViewportView(BuscarM);
        panelBusqueda.add(jScrollPane1);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        panelBusqueda.add(btnBuscar);

        todos.setText("Mostrar todos");
        todos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todosActionPerformed(evt);
            }
        });
        panelBusqueda.add(todos);

        panelSur.add(panelBusqueda, java.awt.BorderLayout.NORTH);

        // Configuración y envoltura de la tabla (Se expandirá automáticamente a lo ancho y alto sobrante)
        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Medicamento", "Dosis", "Descripción"
            }
        ));
        jScrollPane2.setViewportView(Tabla);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(450, 220));
        panelSur.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        this.add(panelSur, java.awt.BorderLayout.SOUTH);
    } // </editor-fold>

    // Variables declaration - do not modify                     
    private javax.swing.JTextPane BuscarM;
    private javax.swing.JTable Tabla;
    private javax.swing.JButton btnAñadir;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblIDBuscar;
    private javax.swing.JLabel lbldosis;
    private javax.swing.JLabel lblinfo;
    private javax.swing.JLabel lblnombre;
    private javax.swing.JButton todos;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtdosis;
    private javax.swing.JTextField txtinfo;
    // End of variables declaration                   
}















   /* @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblIDBuscar = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BuscarM = new javax.swing.JTextPane();
        btnBuscar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        todos = new javax.swing.JButton();
        btnAñadir = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        lblID = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        lblnombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lbldosis = new javax.swing.JLabel();
        txtdosis = new javax.swing.JTextField();
        lblinfo = new javax.swing.JLabel();
        txtinfo = new javax.swing.JTextField();

        jPanel1.setPreferredSize(new java.awt.Dimension(500, 550));

        jLabel1.setText("Información de Medicamentos");

        lblIDBuscar.setText("Ingrese ID de medicamento:");

        jScrollPane1.setViewportView(BuscarM);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(Tabla);

        todos.setText("Mostrar todos");
        todos.addActionListener(this::todosActionPerformed);

        btnAñadir.setText("Añadir medicamento");

        btnModificar.setText("Modificar medicamento");

        btnEliminar.setText("Eliminar medicamento");

        lblID.setText("ID:");

        txtID.setEditable(false);

        lblnombre.setText("Nombre:");

        lbldosis.setText("Dosis:");

        lblinfo.setText("Descripción:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jScrollPane2)
                .addGap(22, 22, 22))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblIDBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(todos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblnombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                        .addComponent(lbldosis, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtNombre)
                        .addGap(24, 24, 24)
                        .addComponent(btnModificar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(btnAñadir, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtdosis)
                        .addGap(24, 24, 24)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtID)
                            .addComponent(txtinfo))
                        .addGap(196, 196, 196))))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblID, lbldosis, lblnombre});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblnombre)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(btnAñadir))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblID)
                                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(21, 21, 21)
                        .addComponent(btnModificar)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEliminar)
                            .addComponent(lbldosis)
                            .addComponent(txtdosis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 44, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIDBuscar)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBuscar)
                        .addComponent(todos)))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblID, lbldosis, lblnombre});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblinfo, txtinfo});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 16, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
       String buscarMedicamentoStr = BuscarM.getText().trim();

if (!buscarMedicamentoStr.isEmpty()) {
    DefaultTableModel modeloTabla = (DefaultTableModel) Tabla.getModel();
    
    // Limpiamos la tabla antes de realizar cualquier acción
    modeloTabla.setRowCount(0); 
    
    try {
        // Intentamos parsear el ID recibido del campo de texto
        int idBuscado = Integer.parseInt(buscarMedicamentoStr);
        
        // Ejecutamos la búsqueda usando el método correspondiente
        Medicamento medEncontrado = buscarMedicamento(idBuscado);
        
        if (medEncontrado != null) {
            // Si el medicamento existe, lo agregamos a la tabla usando sus getters
            Object[] fila = {
                medEncontrado.getIdMedicamento(),
                medEncontrado.getNombreMedicamento(),
                medEncontrado.getDosis(),
                medEncontrado.getDescripcion()
            };
            modeloTabla.addRow(fila);
        } else {
            // Si no se encuentra el medicamento en la base de datos
            Object[] filaError = {"No encontrado", "No existe medicamento con ID: " + idBuscado, "", ""};
            modeloTabla.addRow(filaError);
        }
        
    } catch (NumberFormatException e) {
        // Captura el error si el usuario ingresa letras o símbolos en lugar de números
        Object[] filaError = {"ERROR", "Por favor, ingrese un ID numérico válido", "", ""};
        modeloTabla.addRow(filaError);
    }
}
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void todosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_todosActionPerformed
        // TODO add your handling code here:
        javax.swing.table.DefaultTableModel modeloTabla = (javax.swing.table.DefaultTableModel) tabla.getModel();
    
    // 2. Limpiamos la tabla para no duplicar filas si se presiona varias veces
    modeloTabla.setRowCount(0); 
    
    // 3. Recorremos la lista en memoria y extraemos los datos con los getters
    for (modelo.Medicamento med : listaMedicamentosFicticia) {
        Object[] fila = {
            med.getIdMedicamento(),
            med.getNombreMedicamento(),
            med.getDosis(),
            med.getDescripcion()
        };
        // 4. Agregamos la fila a la tabla
        modeloTabla.addRow(fila);
    }
    }//GEN-LAST:event_todosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane BuscarM;
    private javax.swing.JTable Tabla;
    private javax.swing.JButton btnAñadir;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblIDBuscar;
    private javax.swing.JLabel lbldosis;
    private javax.swing.JLabel lblinfo;
    private javax.swing.JLabel lblnombre;
    private javax.swing.JButton todos;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtdosis;
    private javax.swing.JTextField txtinfo;
    // End of variables declaration//GEN-END:variables
}*/
