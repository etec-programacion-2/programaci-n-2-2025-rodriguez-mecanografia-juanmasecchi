package org.example

import javax.swing.*
import java.awt.*

class VentanaMenuPrincipal(private val usuario: Usuario) : JFrame() {
    private val texto = Texto()

    init {
        title = "Menú Principal - ${usuario.nombre}"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(500, 400)
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
        val lblTitulo = JLabel("📋 MENÚ PRINCIPAL").apply {
            font = Font("Arial", Font.BOLD, 24)
            foreground = Color.WHITE
        }
        panelTitulo.add(lblTitulo)

        // Panel de bienvenida
        val panelBienvenida = JPanel(FlowLayout(FlowLayout.CENTER)).apply {
            background = Color(236, 240, 241)
            preferredSize = Dimension(0, 50)
        }
        val lblBienvenida = JLabel("👋 ¡Bienvenido, ${usuario.nombre}!").apply {
            font = Font("Arial", Font.BOLD, 16)
            foreground = Color(52, 73, 94)
        }
        panelBienvenida.add(lblBienvenida)

        // Panel central con botones
        val panelCentral = JPanel(GridBagLayout()).apply {
            border = BorderFactory.createEmptyBorder(30, 50, 30, 50)
        }

        val gbc = GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            insets = Insets(10, 10, 10, 10)
            gridx = 0
            weightx = 1.0
        }

        val btnPracticar = JButton("🎯 Practicar mecanografía").apply {
            font = Font("Arial", Font.BOLD, 16)
            background = Color(46, 204, 113)
            foreground = Color.WHITE
            preferredSize = Dimension(300, 50)
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        }

        val btnCambiarTexto = JButton("🔄 Cambiar texto").apply {
            font = Font("Arial", Font.BOLD, 16)
            background = Color(52, 152, 219)
            foreground = Color.WHITE
            preferredSize = Dimension(300, 50)
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        }

        val btnHistorial = JButton("📊 Ver historial").apply {
            font = Font("Arial", Font.BOLD, 16)
            background = Color(155, 89, 182)
            foreground = Color.WHITE
            preferredSize = Dimension(300, 50)
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        }

        val btnSalir = JButton("🚪 Salir").apply {
            font = Font("Arial", Font.BOLD, 16)
            background = Color(231, 76, 60)
            foreground = Color.WHITE
            preferredSize = Dimension(300, 50)
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        }

        gbc.gridy = 0
        panelCentral.add(btnPracticar, gbc)
        gbc.gridy = 1
        panelCentral.add(btnCambiarTexto, gbc)
        gbc.gridy = 2
        panelCentral.add(btnHistorial, gbc)
        gbc.gridy = 3
        panelCentral.add(btnSalir, gbc)

        add(panelTitulo, BorderLayout.NORTH)
        add(panelBienvenida, BorderLayout.CENTER)
        add(panelCentral, BorderLayout.CENTER)

        // Listeners
        btnPracticar.addActionListener {
            dispose()
            VentanaPractica(usuario, texto).isVisible = true
        }

        btnCambiarTexto.addActionListener {
            texto.cambiarTexto()
            JOptionPane.showMessageDialog(
                this,
                "✅ Texto cambiado exitosamente.\n📝 ${texto.obtenerNumeroPalabras()} palabras",
                "Texto Cambiado",
                JOptionPane.INFORMATION_MESSAGE
            )
        }

        btnHistorial.addActionListener {
            mostrarHistorial()
        }

        btnSalir.addActionListener {
            val opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas salir?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION
            )
            if (opcion == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(
                    this,
                    "👋 ¡Gracias por jugar!\n" +
                            "Tu progreso ha sido guardado.\n" +
                            "📈 Recuerda: la práctica constante mejora tus habilidades\n" +
                            "¡Vuelve pronto para seguir practicando! 🎯",
                    "Hasta Pronto",
                    JOptionPane.INFORMATION_MESSAGE
                )
                System.exit(0)
            }
        }
    }

    private fun mostrarHistorial() {
        val ventanaHistorial = JDialog(this, "📊 Historial de Sesiones - ${usuario.nombre}", true)
        ventanaHistorial.setSize(500, 400)
        ventanaHistorial.setLocationRelativeTo(this)

        val panelHistorial = JPanel(BorderLayout(10, 10)).apply {
            border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
        }

        val areaTexto = JTextArea().apply {
            isEditable = false
            font = Font("Monospaced", Font.PLAIN, 12)
        }

        // Obtener sesiones del usuario
        val sesiones = GestorArchivos.cargarSesionesUsuario(usuario.id)

        if (sesiones.isEmpty()) {
            areaTexto.text = "El usuario ${usuario.nombre} no tiene sesiones registradas."
        } else {
            val sb = StringBuilder()
            sb.append("Historial de ${usuario.nombre}:\n")
            sb.append("=".repeat(50)).append("\n\n")

            sesiones.forEachIndexed { index, sesion ->
                sb.append("${index + 1}. ID Sesión: ${sesion.id} | Fecha: ${sesion.fecha}\n")
            }

            sb.append("\n").append("=".repeat(50))
            sb.append("\nTotal de sesiones: ${sesiones.size}")

            areaTexto.text = sb.toString()
        }

        val scrollPane = JScrollPane(areaTexto)
        panelHistorial.add(scrollPane, BorderLayout.CENTER)

        val btnCerrar = JButton("Cerrar").apply {
            font = Font("Arial", Font.BOLD, 14)
            background = Color(149, 165, 166)
            foreground = Color.WHITE
            preferredSize = Dimension(100, 40)
        }

        btnCerrar.addActionListener {
            ventanaHistorial.dispose()
        }

        val panelBoton = JPanel(FlowLayout(FlowLayout.CENTER))
        panelBoton.add(btnCerrar)
        panelHistorial.add(panelBoton, BorderLayout.SOUTH)

        ventanaHistorial.add(panelHistorial)
        ventanaHistorial.isVisible = true
    }
}