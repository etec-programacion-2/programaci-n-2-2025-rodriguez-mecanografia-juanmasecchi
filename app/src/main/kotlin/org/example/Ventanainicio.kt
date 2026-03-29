package org.example

import javax.swing.*
import java.awt.*

class VentanaInicio : JFrame() {
    private val campoNombre = JTextField(20)
    private val btnEnviar = JButton("Enviar")

    init {
        title = "Juego de Mecanografía - Inicio de Sesión"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(450, 250)
        setLocationRelativeTo(null)

        configurarUI()
    }

    private fun configurarUI() {
        layout = BorderLayout(10, 10)

        // Panel superior con título
        val panelTitulo = JPanel().apply {
            background = Color(41, 128, 185)
            preferredSize = Dimension(0, 80)
        }
        val lblTitulo = JLabel("🎮 JUEGO DE MECANOGRAFÍA 🎮").apply {
            font = Font("Arial", Font.BOLD, 24)
            foreground = Color.WHITE
        }
        panelTitulo.add(lblTitulo)

        // Panel central con formulario
        val panelCentral = JPanel(GridBagLayout()).apply {
            border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
        }
        val gbc = GridBagConstraints().apply {
            insets = Insets(5, 5, 5, 5)
            fill = GridBagConstraints.HORIZONTAL
        }

        val lblNombre = JLabel("Por favor, ingresa tu nombre:").apply {
            font = Font("Arial", Font.PLAIN, 14)
        }

        campoNombre.apply {
            font = Font("Arial", Font.PLAIN, 14)
        }

        btnEnviar.apply {
            font = Font("Arial", Font.BOLD, 14)
            background = Color(46, 204, 113)
            foreground = Color.WHITE
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        }

        // Agregar componentes
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.gridwidth = 2
        panelCentral.add(lblNombre, gbc)

        gbc.gridy = 1
        panelCentral.add(campoNombre, gbc)

        gbc.gridy = 2
        gbc.fill = GridBagConstraints.NONE
        gbc.anchor = GridBagConstraints.CENTER
        panelCentral.add(btnEnviar, gbc)

        add(panelTitulo, BorderLayout.NORTH)
        add(panelCentral, BorderLayout.CENTER)

        // Acción del botón
        btnEnviar.addActionListener {
            iniciarSesion()
        }

        // Enter en el campo de texto
        campoNombre.addActionListener {
            iniciarSesion()
        }
    }

    private fun iniciarSesion() {
        val nombre = campoNombre.text.trim()

        if (nombre.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "❌ Error: Debe ingresar un nombre válido.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            )
            return
        }

        val usuario = GestorArchivos.obtenerUsuario(nombre)
        usuario.iniciarNuevaSesion()

        JOptionPane.showMessageDialog(
            this,
            "👋 ¡Hola, $nombre!\n✅ Nueva sesión iniciada correctamente.",
            "Bienvenido",
            JOptionPane.INFORMATION_MESSAGE
        )

        dispose()
        VentanaMenuPrincipal(usuario).isVisible = true
    }
}