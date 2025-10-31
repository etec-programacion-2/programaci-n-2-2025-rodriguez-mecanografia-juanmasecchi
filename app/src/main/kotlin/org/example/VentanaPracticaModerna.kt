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

    private val panelTexto = JPanel(WrapLayout(FlowLayout.LEFT, 8, 8))
    private val lblVelocidad = JLabel("0")
    private val lblPrecision = JLabel("0.0")

    private var tiempoInicio: Long = 0
    private var haComenzado = false
    private val estadisticas = EstadisticasJuego()
    private var textoUsuario = StringBuilder()
    private val palabrasOriginales = texto.obtenerTextoActual()
    private val lblsPalabras = mutableListOf<JLabel>()

    private var palabrasCorrectas = 0
    private var palabrasEvaluadasCount = 0

    init {
        title = "Test de Mecanografía - ${usuario.nombre}"
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        setSize(1100, 750)
        setLocationRelativeTo(null)
        contentPane.background = Color(250, 250, 250)

        configurarUI()
        crearPalabrasTexto()

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
            foreground = Color(0, 150, 255)
            background = Color.WHITE
            isFocusPainted = false
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color(0, 150, 255), 2, true),
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

        // Panel del texto (sin scroll)
        panelTexto.apply {
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

        val lblInstrucciones = JLabel("Presiona ESC para volver al menú").apply {
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

        val panelVel = crearTarjeta("Velocidad", Color(155, 89, 182), Color(120, 60, 150), "⚡", lblVelocidad, "PPM")
        val panelPre = crearTarjeta("Precisión", Color(243, 156, 18), Color(200, 120, 10), "◎", lblPrecision, "%")

        panel.add(panelVel)
        panel.add(panelPre)
        return panel
    }

    private fun crearPalabrasTexto() {
        panelTexto.removeAll()
        lblsPalabras.clear()

        for (palabra in palabrasOriginales) {
            val lblPalabra = JLabel("$palabra ").apply {
                font = Font("Arial", Font.PLAIN, 22)
                foreground = Color(180, 180, 180)
            }
            lblsPalabras.add(lblPalabra)
            panelTexto.add(lblPalabra)
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

        if (c == ' ' || c == '\n') {
            avanzarPalabra()
        } else {
            textoUsuario.append(c)
            actualizarVisualizacion()
        }
    }

    private fun borrarCaracter() {
        if (textoUsuario.isNotEmpty()) {
            textoUsuario.deleteCharAt(textoUsuario.length - 1)
            actualizarVisualizacion()
        }
    }

    private fun avanzarPalabra() {
        if (palabrasEvaluadasCount < palabrasOriginales.size) {
            val palabraOriginal = palabrasOriginales[palabrasEvaluadasCount]
            val palabraEscrita = textoUsuario.toString()

            if (palabraEscrita == palabraOriginal) {
                lblsPalabras[palabrasEvaluadasCount].foreground = Color(46, 204, 113)
                palabrasCorrectas++
            } else {
                lblsPalabras[palabrasEvaluadasCount].foreground = Color(231, 76, 60)
            }

            palabrasEvaluadasCount++
            textoUsuario.clear()

            if (palabrasEvaluadasCount >= palabrasOriginales.size) finalizarPractica()
        }
    }

    private fun actualizarVisualizacion() {
        if (palabrasEvaluadasCount < palabrasOriginales.size) {
            val palabraOriginal = palabrasOriginales[palabrasEvaluadasCount]
            val palabraActual = textoUsuario.toString()
            lblsPalabras[palabrasEvaluadasCount].foreground =
                if (palabraOriginal.startsWith(palabraActual)) Color(100, 100, 100)
                else Color(231, 76, 60)
        }
    }

    private fun iniciarActualizacionEstadisticas() {
        val timer = Timer(100) { if (haComenzado) actualizarEstadisticas() }
        timer.start()
    }

    private fun actualizarEstadisticas() {
        val tiempoTranscurrido = (System.currentTimeMillis() - tiempoInicio) / 1000
        if (tiempoTranscurrido > 0) {
            val wpm = estadisticas.calcularWPM(palabrasCorrectas, tiempoTranscurrido)
            lblVelocidad.text = String.format("%.1f", wpm)
        }
        if (palabrasEvaluadasCount > 0) {
            val precision = (palabrasCorrectas.toDouble() / palabrasEvaluadasCount) * 100
            lblPrecision.text = String.format("%.1f", precision)
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
        palabrasCorrectas = 0
        palabrasEvaluadasCount = 0
        textoUsuario.clear()
        lblVelocidad.text = "0"
        lblPrecision.text = "0.0"
        crearPalabrasTexto()
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

/**
 * Layout que acomoda las palabras y permite que se ajusten automáticamente
 * sin necesidad de barras de desplazamiento.
 */
class WrapLayout(align: Int = FlowLayout.LEFT, hgap: Int = 5, vgap: Int = 5) : FlowLayout(align, hgap, vgap) {
    override fun preferredLayoutSize(target: Container): Dimension {
        return layoutSize(target)
    }

    private fun layoutSize(target: Container): Dimension {
        synchronized(target.treeLock) {
            val insets = target.insets
            val maxWidth = target.parent?.width ?: 1000
            var width = 0
            var height = insets.top + insets.bottom + vgap * 2
            var rowWidth = 0
            var rowHeight = 0

            for (component in target.components) {
                if (component.isVisible) {
                    val d = component.preferredSize
                    if (rowWidth + d.width > maxWidth && rowWidth != 0) {
                        width = maxOf(width, rowWidth)
                        height += rowHeight + vgap
                        rowWidth = 0
                        rowHeight = 0
                    }
                    rowWidth += d.width + hgap
                    rowHeight = maxOf(rowHeight, d.height)
                }
            }
            width = maxOf(width, rowWidth)
            height += rowHeight
            return Dimension(width + insets.left + insets.right + hgap * 2, height)
        }
    }
}
