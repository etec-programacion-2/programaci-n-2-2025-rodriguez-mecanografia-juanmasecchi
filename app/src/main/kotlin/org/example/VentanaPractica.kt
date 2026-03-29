package org.example

import javax.swing.*
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class VentanaPractica(
    private val usuario: Usuario,
    private val texto: Texto
) : JFrame() {

    private val areaTextoOriginal = JTextArea()
    private val areaTextoUsuario = JTextArea()
    private val lblInstrucciones = JLabel()
    private val btnComenzar = JButton("🚀 Comenzar")
    private val btnEnviar = JButton("✅ Enviar Respuesta")

    private var tiempoInicio: Long = 0
    private var haComenzado = false
    private val estadisticas = EstadisticasJuego()

    init {
        title = "Práctica de Mecanografía - ${usuario.nombre}"
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        setSize(800, 700)
        setLocationRelativeTo(null)

        configurarUI()
    }

    private fun configurarUI() {
        layout = BorderLayout(10, 10)

        // Panel superior
        val panelSuperior = JPanel().apply {
            background = Color(52, 152, 219)
            preferredSize = Dimension(0, 70)
        }

        val lblTitulo = JLabel("🎯 PRÁCTICA DE MECANOGRAFÍA").apply {
            font = Font("Arial", Font.BOLD, 22)
            foreground = Color.WHITE
        }
        panelSuperior.add(lblTitulo)

        // Panel central
        val panelCentral = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(20, 30, 20, 30)
        }

        // Información del texto
        val panelInfo = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
            background = Color(236, 240, 241)
        }
        val lblInfo = JLabel("📝 Texto: ${texto.obtenerNumeroPalabras()} palabras | ⏱️ Se medirá tu velocidad y precisión").apply {
            font = Font("Arial", Font.BOLD, 14)
            foreground = Color(52, 73, 94)
        }
        panelInfo.add(lblInfo)

        // Panel del texto original
        val panelTextoOriginal = JPanel(BorderLayout()).apply {
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color(52, 73, 94), 2),
                    "📖 TEXTO A ESCRIBIR",
                    0,
                    0,
                    Font("Arial", Font.BOLD, 14),
                    Color(52, 73, 94)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
        }

        areaTextoOriginal.apply {
            isEditable = false
            lineWrap = true
            wrapStyleWord = true
            font = Font("Arial", Font.PLAIN, 16)
            background = Color(255, 255, 230)
            text = texto.obtenerTextoActual().joinToString(" ")
            rows = 8
        }

        val scrollTextoOriginal = JScrollPane(areaTextoOriginal)
        panelTextoOriginal.add(scrollTextoOriginal, BorderLayout.CENTER)

        // Panel de instrucciones
        val panelInstrucciones = JPanel(BorderLayout()).apply {
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color(41, 128, 185), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
            background = Color(174, 214, 241)
        }

        lblInstrucciones.apply {
            text = """
                <html><body style='font-family: Arial; font-size: 13px;'>
                <b>⚡ INSTRUCCIONES:</b><br/>
                • Escribe el texto exactamente como aparece arriba<br/>
                • Respeta mayúsculas, minúsculas y acentos<br/>
                • El cronómetro empezará cuando comiences a escribir<br/>
                • Presiona el botón o CTRL+ENTER al terminar para ver tus resultados
                </body></html>
            """.trimIndent()
        }
        panelInstrucciones.add(lblInstrucciones, BorderLayout.CENTER)

        // Panel del texto del usuario
        val panelTextoUsuario = JPanel(BorderLayout()).apply {
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color(52, 73, 94), 2),
                    "✍️ ESCRIBE AQUÍ",
                    0,
                    0,
                    Font("Arial", Font.BOLD, 14),
                    Color(52, 73, 94)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
        }

        areaTextoUsuario.apply {
            lineWrap = true
            wrapStyleWord = true
            font = Font("Arial", Font.PLAIN, 16)
            isEnabled = false
            rows = 8

            addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent) {
                    if (!haComenzado && e.keyChar != KeyEvent.CHAR_UNDEFINED) {
                        haComenzado = true
                        tiempoInicio = System.currentTimeMillis()
                        lblInstrucciones.text = """
                            <html><body style='font-family: Arial; font-size: 13px; color: green;'>
                            <b>✅ ¡Cronómetro iniciado! Escribe con precisión...</b>
                            </body></html>
                        """.trimIndent()
                    }
                }

                override fun keyPressed(e: KeyEvent) {
                    if (e.keyCode == KeyEvent.VK_ENTER && e.isControlDown) {
                        enviarRespuesta()
                    }
                }
            })
        }

        val scrollTextoUsuario = JScrollPane(areaTextoUsuario)
        panelTextoUsuario.add(scrollTextoUsuario, BorderLayout.CENTER)

        // Agregar componentes al panel central
        panelCentral.add(panelInfo)
        panelCentral.add(Box.createRigidArea(Dimension(0, 10)))
        panelCentral.add(panelTextoOriginal)
        panelCentral.add(Box.createRigidArea(Dimension(0, 10)))
        panelCentral.add(panelInstrucciones)
        panelCentral.add(Box.createRigidArea(Dimension(0, 10)))
        panelCentral.add(panelTextoUsuario)

        // Panel inferior con botones
        val panelInferior = JPanel(FlowLayout(FlowLayout.CENTER, 15, 15))

        btnComenzar.apply {
            font = Font("Arial", Font.BOLD, 14)
            background = Color(46, 204, 113)
            foreground = Color.WHITE
            preferredSize = Dimension(150, 45)
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        }

        btnEnviar.apply {
            font = Font("Arial", Font.BOLD, 14)
            background = Color(52, 152, 219)
            foreground = Color.WHITE
            preferredSize = Dimension(180, 45)
            isEnabled = false
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        }

        val btnCancelar = JButton("❌ Cancelar").apply {
            font = Font("Arial", Font.BOLD, 14)
            background = Color(149, 165, 166)
            foreground = Color.WHITE
            preferredSize = Dimension(150, 45)
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        }

        panelInferior.add(btnComenzar)
        panelInferior.add(btnEnviar)
        panelInferior.add(btnCancelar)

        add(panelSuperior, BorderLayout.NORTH)
        add(panelCentral, BorderLayout.CENTER)
        add(panelInferior, BorderLayout.SOUTH)

        // Listeners
        btnComenzar.addActionListener {
            iniciarPractica()
        }

        btnEnviar.addActionListener {
            enviarRespuesta()
        }

        btnCancelar.addActionListener {
            val opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cancelar la práctica?",
                "Confirmar Cancelación",
                JOptionPane.YES_NO_OPTION
            )
            if (opcion == JOptionPane.YES_OPTION) {
                dispose()
                VentanaMenuPrincipal(usuario).isVisible = true
            }
        }
    }

    private fun iniciarPractica() {
        areaTextoUsuario.isEnabled = true
        areaTextoUsuario.requestFocus()
        btnComenzar.isEnabled = false
        btnEnviar.isEnabled = true

        lblInstrucciones.text = """
            <html><body style='font-family: Arial; font-size: 13px; color: orange;'>
            <b>⏳ Listo para comenzar... El cronómetro iniciará cuando empieces a escribir</b>
            </body></html>
        """.trimIndent()
    }

    private fun enviarRespuesta() {
        if (!haComenzado) {
            JOptionPane.showMessageDialog(
                this,
                "⚠️ Aún no has comenzado a escribir.",
                "Atención",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }

        val tiempoFin = System.currentTimeMillis()
        val tiempoTranscurrido = (tiempoFin - tiempoInicio) / 1000

        val respuestaUsuario = areaTextoUsuario.text.trim()

        if (respuestaUsuario.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "⚠️ No has escrito nada. Por favor, escribe el texto.",
                "Atención",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }

        // Evaluar respuesta
        val textoOriginal = texto.obtenerTextoActual()
        val palabrasEvaluadas = estadisticas.evaluarRespuesta(textoOriginal, respuestaUsuario)
        val resultado = estadisticas.generarReporte(palabrasEvaluadas, tiempoTranscurrido)

        // Ir a ventana de resultados
        dispose()
        VentanaResultados(usuario, resultado, palabrasEvaluadas, texto).isVisible = true
    }
}