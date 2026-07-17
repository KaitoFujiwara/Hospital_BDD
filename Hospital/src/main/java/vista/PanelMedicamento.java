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
    }

    public PanelMedicamento(MedicamentoControl medicamentoControl) {
        initComponents();
        this.medicamentoControl = medicamentoControl;
        configurarTabla();
        actualizarTabla();
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BuscarM = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        todos = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(500, 550));

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI", 1, 18));
        jLabel1.setText("Buscar Medicamento");

        jLabel2.setText("Ingrese ID de medicamento:");

        jScrollPane1.setViewportView(BuscarM);

        jButton1.setText("Buscar");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Medicamento", "Dosis", "Descripción"}
        ));
        jScrollPane2.setViewportView(Tabla);

        todos.setText("Mostrar todos");
        todos.addActionListener(this::todosActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);

        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.LEADING
                                )
                                        .addComponent(
                                                jScrollPane2,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                488,
                                                Short.MAX_VALUE
                                        )
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(
                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED
                                                )
                                                .addComponent(
                                                        jScrollPane1,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        100,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                                .addPreferredGap(
                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED
                                                )
                                                .addComponent(jButton1)
                                                .addGap(18, 18, 18)
                                                .addComponent(todos)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(
                                                        jLabel1,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        190,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(
                                        jLabel1,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        47,
                                        javax.swing.GroupLayout.PREFERRED_SIZE
                                )
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(
                                        javax.swing.GroupLayout.Alignment.CENTER
                                )
                                        .addComponent(jLabel2)
                                        .addComponent(
                                                jScrollPane1,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                30,
                                                javax.swing.GroupLayout.PREFERRED_SIZE
                                        )
                                        .addComponent(jButton1)
                                        .addComponent(todos))
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED
                                )
                                .addComponent(
                                        jScrollPane2,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        413,
                                        Short.MAX_VALUE
                                )
                                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }
    // </editor-fold>

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        buscarMedicamento();
    }

    private void todosActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarTabla();
    }

    // Variables declaration - do not modify
    private javax.swing.JTextPane BuscarM;
    private javax.swing.JTable Tabla;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton todos;
    // End of variables declaration
}
   /* @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BuscarM = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        todos = new javax.swing.JButton();

        jPanel1.setPreferredSize(new java.awt.Dimension(500, 550));

        jLabel1.setText("Buscar Medicamento");

        jLabel2.setText("Ingrese ID de medicamento:");

        jScrollPane1.setViewportView(BuscarM);

        jButton1.setText("Buscar");
        jButton1.addActionListener(this::jButton1ActionPerformed);

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(165, 165, 165)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(179, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(todos))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(todos)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
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
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton todos;
    // End of variables declaration//GEN-END:variables
}*/
