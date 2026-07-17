package vista;

import controlador.ConsultaControl;
import controlador.DoctorControl;
import controlador.MedicamentoControl;
import controlador.PacienteControl;
import controlador.RecetaControl;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class VentanaPrincipal extends JFrame {

    private final PacienteControl pacienteControl;
    private final DoctorControl doctorControl;
    private final MedicamentoControl medicamentoControl;
    private final ConsultaControl consultaControl;
    private final RecetaControl recetaControl;

    private final PanelPaciente panelPaciente;
    private final PanelDoctor panelDoctor;
    private final PanelMedicamento panelMedicamento;
    private final PanelConsulta panelConsulta;
    private final PanelRecetas panelRecetas;

    private final CardLayout cardLayout;
    private final JPanel panelContenido;

    private JButton btnPacientes;
    private JButton btnDoctores;
    private JButton btnMedicamentos;
    private JButton btnConsultas;
    private JButton btnRecetas;

    public VentanaPrincipal() {
        pacienteControl = new PacienteControl();
        doctorControl = new DoctorControl();
        medicamentoControl = new MedicamentoControl();

        consultaControl = new ConsultaControl(
                pacienteControl,
                doctorControl
        );

        recetaControl = new RecetaControl(
                consultaControl,
                medicamentoControl
        );

        panelPaciente = new PanelPaciente(pacienteControl);
        panelDoctor = new PanelDoctor(doctorControl);
        panelMedicamento = new PanelMedicamento(medicamentoControl);

        panelConsulta = new PanelConsulta(
                consultaControl,
                pacienteControl,
                doctorControl
        );

        panelRecetas = new PanelRecetas(
                recetaControl,
                consultaControl,
                medicamentoControl,
                pacienteControl,
                doctorControl
        );

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        configurarVentana();
        crearInterfaz();
        conectarEventos();
    }

    private void configurarVentana() {
        setTitle("Sistema Hospitalario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 750);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void crearInterfaz() {
        JPanel encabezado = crearEncabezado();
        JPanel menu = crearMenu();

        panelContenido.add(
                agregarScroll(panelPaciente),
                "PACIENTES"
        );

        panelContenido.add(
                agregarScroll(panelDoctor),
                "DOCTORES"
        );

        panelContenido.add(
                agregarScroll(panelMedicamento),
                "MEDICAMENTOS"
        );

        panelContenido.add(
                agregarScroll(panelConsulta),
                "CONSULTAS"
        );

        panelContenido.add(
                agregarScroll(panelRecetas),
                "RECETAS"
        );

        add(encabezado, BorderLayout.NORTH);
        add(menu, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);

        cardLayout.show(panelContenido, "PACIENTES");
    }

    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(new Color(25, 105, 145));
        encabezado.setPreferredSize(new Dimension(0, 65));

        JLabel titulo = new JLabel(
                "SISTEMA DE CONTROL HOSPITALARIO",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        encabezado.add(titulo, BorderLayout.CENTER);

        return encabezado;
    }

    private JPanel crearMenu() {
        JPanel menu = new JPanel(new GridLayout(5, 1, 5, 8));

        menu.setPreferredSize(new Dimension(190, 0));
        menu.setBackground(new Color(225, 238, 245));
        menu.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        btnPacientes = crearBoton("Pacientes");
        btnDoctores = crearBoton("Doctores");
        btnMedicamentos = crearBoton("Medicamentos");
        btnConsultas = crearBoton("Consultas");
        btnRecetas = crearBoton("Recetas");

        menu.add(btnPacientes);
        menu.add(btnDoctores);
        menu.add(btnMedicamentos);
        menu.add(btnConsultas);
        menu.add(btnRecetas);

        return menu;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);

        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBackground(Color.WHITE);

        return boton;
    }

    private JScrollPane agregarScroll(JPanel panel) {
        JScrollPane scroll = new JScrollPane(panel);

        scroll.setBorder(null);

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);

        return scroll;
    }

    private void conectarEventos() {
        btnPacientes.addActionListener(e -> {
            panelPaciente.actualizarTabla();
            cardLayout.show(panelContenido, "PACIENTES");
        });

        btnDoctores.addActionListener(e -> {
            panelDoctor.actualizarTabla();
            cardLayout.show(panelContenido, "DOCTORES");
        });

        btnMedicamentos.addActionListener(e -> {
            panelMedicamento.actualizarTabla();
            cardLayout.show(panelContenido, "MEDICAMENTOS");
        });

        btnConsultas.addActionListener(e -> {
            panelConsulta.actualizarCombos();
            panelConsulta.actualizarTabla();
            cardLayout.show(panelContenido, "CONSULTAS");
        });

        btnRecetas.addActionListener(e -> {
            panelRecetas.actualizarCombos();
            panelRecetas.actualizarTabla();
            cardLayout.show(panelContenido, "RECETAS");
        });
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
