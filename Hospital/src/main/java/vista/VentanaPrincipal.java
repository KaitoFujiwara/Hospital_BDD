package vista;

import controlador.PacienteControl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

public class VentanaPrincipal extends JFrame {

    private final PacienteControl pacienteControl;
    private final PanelPaciente panelPaciente;

    public VentanaPrincipal() {

        pacienteControl = new PacienteControl();
        panelPaciente = new PanelPaciente(pacienteControl);

        setTitle("Sistema Hospitalario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(panelPaciente, BorderLayout.CENTER);
    }
}

   /* 
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}*/
