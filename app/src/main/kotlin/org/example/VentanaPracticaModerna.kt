package org.example

import javax.swing.*
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.border.EmptyBorder

class VentanaPracticaModerna(
    private val usuario: Usuario,
    private val texto: Texto
) : JFrame() {

    private val panelTexto = JPanel()
    private val lblVelocidad = JLabel("0")
    private val lblPrecision = JLabel("100")

    private var tiempoInicio: Long = 0
    private var haComenzado = false
    private val estadisticas = EstadisticasJuego()

    private val textoCompleto: String
    private val lblsCaracteres = mutableListOf<JLabel>()
    private var posicionActual = 0

    private var caracteresCorrectos = 0
    private var caracteresTotales = 0

    init {
        // Unir todas las palabras en un texto completo con espacios
        textoCompleto = texto.obtenerTextoActual().joinToString(" ")

        title = "Test de Mecanografía - ${usuario.nombre}"
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        setSize(1100, 750)
        setLocationRelativeTo(null)
        contentPane.background = Color(250, 250, 250)

        configurarUI()
        crearCaracteresTexto()

        addKeyListener(object : KeyAdapter() {
            override fun keyTyped(e: KeyEvent) {
                procesarTecla(e.keyChar)
            }

            override fun keyPressed(e: KeyEvent) {
                when (e.keyCode) {
                    KeyEvent.VK_BACK_SPACE -> borrarCaracter()
                    KeyEvent.VK_ESCAPE -> volverAlMenu()
                }
            }
        })

        isFocusable = true
        requestFocusInWindow()
    }

    private fun configurarUI() {
        layout = BorderLayout(0, 0)

        val panelPrincipal = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = Color(250, 250, 250)
            border = EmptyBorder(50, 100, 50, 100)
        }

        // Panel de estadísticas
        val panelEstadisticas = crearPanelEstadisticas()
        panelEstadisticas.alignmentX = Component.CENTER_ALIGNMENT
        panelPrincipal.add(panelEstadisticas)
        panelPrincipal.add(Box.createRigidArea(Dimension(0, 40)))

        // Botón "Inténtalo de vuelta"
        val btnReintentar = JButton("Inténtalo de vuelta").apply {
            font = Font("Arial", Font.BOLD, 16)
            foreground = Color(126, 87, 194)
            background = Color.WHITE
            isFocusPainted = false
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color(126, 87, 194), 2, true),
                EmptyBorder(12, 30, 12, 30)
            )
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            isOpaque = true
            maximumSize = Dimension(300, 50)
            alignmentX = Component.CENTER_ALIGNMENT
            addActionListener { reiniciarPractica() }
        }

        panelPrincipal.add(btnReintentar)
        panelPrincipal.add(Box.createRigidArea(Dimension(0, 50)))

        // Panel del texto con FlowLayout para que las letras fluyan naturalmente
        panelTexto.apply {
            layout = FlowLayout(FlowLayout.LEFT, 0, 5)
            preferredSize = Dimension(900, 400)
            maximumSize = Dimension(900, 400)
            background = Color.WHITE
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color(220, 220, 220), 1, true),
                EmptyBorder(20, 20, 20, 20)
            )
        }

        panelPrincipal.add(panelTexto)
        panelPrincipal.add(Box.createRigidArea(Dimension(0, 20)))

        val lblInstrucciones = JLabel("Presiona ESC para volver al menú | Backspace para borrar").apply {
            font = Font("Arial", Font.PLAIN, 12)
            foreground = Color(150, 150, 150)
            alignmentX = Component.CENTER_ALIGNMENT
        }
        panelPrincipal.add(lblInstrucciones)

        add(panelPrincipal, BorderLayout.CENTER)
    }

    private fun crearPanelEstadisticas(): JPanel {
        val panel = JPanel(GridLayout(1, 2, 20, 0)).apply {
            background = Color(250, 250, 250)
            maximumSize = Dimension(700, 160)
            alignmentX = Component.CENTER_ALIGNMENT
        }

        fun crearTarjeta(
            titulo: String,
            colorFondo: Color,
            colorHeader: Color,
            icono: String,
            labelValor: JLabel,
            unidad: String
        ): JPanel {
            val tarjeta = JPanel(BorderLayout()).apply {
                background = colorFondo
                border = BorderFactory.createLineBorder(colorFondo.darker(), 1, true)
            }

            val header = JPanel(FlowLayout(FlowLayout.LEFT, 15, 12)).apply {
                background = colorHeader
            }

            val lblIcono = JLabel(icono).apply {
                font = Font("Arial", Font.BOLD, 22)
                foreground = Color.WHITE
            }

            val lblTitulo = JLabel(titulo).apply {
                font = Font("Arial", Font.BOLD, 16)
                foreground = Color.WHITE
            }

            header.add(lblIcono)
            header.add(lblTitulo)

            val cuerpo = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                background = colorFondo
                border = EmptyBorder(15, 25, 15, 25)
            }

            labelValor.font = Font("Arial", Font.BOLD, 48)
            labelValor.foreground = Color.WHITE
            val lblUnidad = JLabel(unidad).apply {
                font = Font("Arial", Font.BOLD, 20)
                foreground = Color.WHITE
            }

            cuerpo.add(labelValor)
            cuerpo.add(lblUnidad)

            tarjeta.add(header, BorderLayout.NORTH)
            tarjeta.add(cuerpo, BorderLayout.CENTER)

            return tarjeta
        }

        val panelVel = crearTarjeta("Velocidad", Color(126, 87, 194), Color(106, 27, 154), "⚡", lblVelocidad, "PPM")
        val panelPre = crearTarjeta("Precisión", Color(255, 128, 0), Color(255, 110, 0), "◎", lblPrecision, "%")

        panel.add(panelVel)
        panel.add(panelPre)
        return panel
    }

    private fun crearCaracteresTexto() {
        panelTexto.removeAll()
        lblsCaracteres.clear()

        for (caracter in textoCompleto) {
            val lblCaracter = JLabel(caracter.toString()).apply {
                font = Font("Monospaced", Font.BOLD, 24)
                foreground = Color(180, 180, 180)
            }
            lblsCaracteres.add(lblCaracter)
            panelTexto.add(lblCaracter)
        }

        // Resaltar el primer carácter
        if (lblsCaracteres.isNotEmpty()) {
            lblsCaracteres[0].foreground = Color(100, 100, 100)
        }

        panelTexto.revalidate()
        panelTexto.repaint()
    }

    private fun procesarTecla(c: Char) {
        if (!haComenzado) {
            haComenzado = true
            tiempoInicio = System.currentTimeMillis()
            iniciarActualizacionEstadisticas()
        }

        if (posicionActual >= textoCompleto.length) {
            return // Ya terminó
        }

        val caracterEsperado = textoCompleto[posicionActual]
        caracteresTotales++

        if (c == caracterEsperado) {
            // Carácter correcto
            lblsCaracteres[posicionActual].foreground = Color(46, 204, 113) // Verde
            caracteresCorrectos++
            posicionActual++

            // Resaltar siguiente carácter
            if (posicionActual < lblsCaracteres.size) {
                lblsCaracteres[posicionActual].foreground = Color(100, 100, 100)
            }

            // Verificar si terminó
            if (posicionActual >= textoCompleto.length) {
                finalizarPractica()
            }
        } else {
            // Carácter incorrecto
            lblsCaracteres[posicionActual].foreground = Color(231, 76, 60) // Rojo
        }

        actualizarEstadisticas()
    }

    private fun borrarCaracter() {
        if (posicionActual > 0) {
            // Volver al carácter anterior
            posicionActual--

            // Restaurar color del carácter actual
            lblsCaracteres[posicionActual].foreground = Color(100, 100, 100)

            // Si había un siguiente, quitarle el resaltado
            if (posicionActual + 1 < lblsCaracteres.size) {
                val siguienteColor = when (lblsCaracteres[posicionActual + 1].foreground) {
                    Color(46, 204, 113) -> Color(46, 204, 113) // Verde se mantiene
                    Color(231, 76, 60) -> Color(231, 76, 60) // Rojo se mantiene
                    else -> Color(180, 180, 180) // Gris por defecto
                }
                lblsCaracteres[posicionActual + 1].foreground = siguienteColor
            }

            // Ajustar contador de caracteres totales
            if (caracteresTotales > 0) {
                caracteresTotales--

                // Si el carácter que borramos estaba correcto, restar de correctos
                if (lblsCaracteres[posicionActual].foreground == Color(46, 204, 113)) {
                    caracteresCorrectos--
                }
            }

            actualizarEstadisticas()
        }
    }

    private fun iniciarActualizacionEstadisticas() {
        val timer = Timer(100) {
            if (haComenzado && posicionActual < textoCompleto.length) {
                actualizarEstadisticas()
            }
        }
        timer.start()
    }

    private fun actualizarEstadisticas() {
        val tiempoTranscurrido = (System.currentTimeMillis() - tiempoInicio) / 1000

        // Calcular PPM (palabras por minuto) basado en caracteres
        // Aproximación: 5 caracteres = 1 palabra
        if (tiempoTranscurrido > 0) {
            val palabrasEscritas = caracteresCorrectos / 5.0
            val minutos = tiempoTranscurrido / 60.0
            val ppm = if (minutos > 0) palabrasEscritas / minutos else 0.0
            lblVelocidad.text = String.format("%.0f", ppm)
        }

        // Calcular precisión
        if (caracteresTotales > 0) {
            val precision = (caracteresCorrectos.toDouble() / caracteresTotales) * 100
            lblPrecision.text = String.format("%.0f", precision)
        }
    }

    private fun finalizarPractica() {
        actualizarEstadisticas()
        JOptionPane.showMessageDialog(
            this,
            "¡Práctica completada!\n\n⚡ Velocidad: ${lblVelocidad.text} PPM\n🎯 Precisión: ${lblPrecision.text}%",
            "Resultados",
            JOptionPane.INFORMATION_MESSAGE
        )
    }

    private fun reiniciarPractica() {
        haComenzado = false
        posicionActual = 0
        caracteresCorrectos = 0
        caracteresTotales = 0
        lblVelocidad.text = "0"
        lblPrecision.text = "100"
        crearCaracteresTexto()
        requestFocusInWindow()
    }

    private fun volverAlMenu() {
        val opcion = JOptionPane.showConfirmDialog(
            this, "¿Estás seguro de que deseas volver al menú?",
            "Confirmar", JOptionPane.YES_NO_OPTION
        )
        if (opcion == JOptionPane.YES_OPTION) {
            dispose()
            VentanaMenuPrincipal(usuario).isVisible = true
        }
    }
}