package org.example

import javax.swing.*
import java.awt.*

class VentanaResultados(
    private val usuario: Usuario,
    private val resultado: ResultadoJuego,
    private val palabrasEvaluadas: List<Palabra>,
    private val texto: Texto
) : JFrame() {

    init {
        title = "Resultados - ${usuario.nombre}"
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        setSize(700, 700)
        setLocationRelativeTo(null)

        configurarUI()
    }

    private fun configurarUI() {
        layout = BorderLayout(10, 10)

        // Panel superior
        val panelSuperior = JPanel().apply {
            background = Color(46, 204, 113)
            preferredSize = Dimension(0, 70)
        }

        val lblTitulo = JLabel("📊 RESULTADOS DE LA PRÁCTICA").apply {
            font = Font("Arial", Font.BOLD, 22)
            foreground = Color.WHITE
        }
        panelSuperior.add(lblTitulo)

        // Panel central con scroll
        val panelCentral = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(20, 30, 20, 30)
        }

        // Estadísticas principales
        val panelEstadisticas = crearPanelEstadisticas()

        // Análisis de palabras
        val panelPalabras = crearPanelPalabras()

        // Consejos
        val panelConsejos = crearPanelConsejos()

        panelCentral.add(panelEstadisticas)
        panelCentral.add(Box.createRigidArea(Dimension(0, 15)))
        panelCentral.add(panelPalabras)
        panelCentral.add(Box.createRigidArea(Dimension(0, 15)))
        panelCentral.add(panelConsejos)

        val scrollPane = JScrollPane(panelCentral).apply {
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            border = null
        }

        // Panel inferior con botones
        val panelInferior = JPanel(FlowLayout(FlowLayout.CENTER, 15, 15))

        val btnPracticarOtra = JButton("🔄 Practicar Otra Vez").apply {
            font = Font("Arial", Font.BOLD, 14)
            background = Color(52, 152, 219)
            foreground = Color.WHITE
            preferredSize = Dimension(200, 45)
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        }

        val btnMenu = JButton("🏠 Volver al Menú").apply {
            font = Font("Arial", Font.BOLD, 14)
            background = Color(149, 165, 166)
            foreground = Color.WHITE
            preferredSize = Dimension(200, 45)
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        }

        panelInferior.add(btnPracticarOtra)
        panelInferior.add(btnMenu)

        add(panelSuperior, BorderLayout.NORTH)
        add(scrollPane, BorderLayout.CENTER)
        add(panelInferior, BorderLayout.SOUTH)

        // Listeners
        btnPracticarOtra.addActionListener {
            dispose()
            VentanaPractica(usuario, texto).isVisible = true
        }

        btnMenu.addActionListener {
            dispose()
            VentanaMenuPrincipal(usuario).isVisible = true
        }
    }

    private fun crearPanelEstadisticas(): JPanel {
        val panel = JPanel().apply {
            layout = GridLayout(4, 2, 15, 10)
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color(52, 73, 94), 2),
                    "📈 ESTADÍSTICAS",
                    0,
                    0,
                    Font("Arial", Font.BOLD, 16),
                    Color(52, 73, 94)
                ),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
            background = Color.WHITE
        }

        panel.add(crearEtiquetaEstadistica("⏱️ Tiempo total:", "${resultado.tiempoSegundos}s"))
        panel.add(crearEtiquetaEstadistica("📝 Palabras totales:", "${resultado.totalPalabras}"))
        panel.add(crearEtiquetaEstadistica("✅ Correctas:", "${resultado.palabrasCorrectas}", Color(46, 204, 113)))
        panel.add(crearEtiquetaEstadistica("❌ Incorrectas:", "${resultado.palabrasIncorrectas}", Color(231, 76, 60)))
        panel.add(crearEtiquetaEstadistica("⏳ No escritas:", "${resultado.palabrasPendientes}", Color(241, 196, 15)))
        panel.add(crearEtiquetaEstadistica("🎯 Precisión:", "${"%.1f".format(resultado.precision)}%", Color(155, 89, 182)))
        panel.add(crearEtiquetaEstadistica("⚡ Velocidad:", "${resultado.wpm} WPM", Color(52, 152, 219)))

        val nivelRendimiento = when {
            resultado.wpm >= 40 && resultado.precision >= 95 -> "🏆 ¡EXCELENTE!"
            resultado.wpm >= 30 && resultado.precision >= 90 -> "🥇 ¡MUY BUENO!"
            resultado.wpm >= 20 && resultado.precision >= 80 -> "🥈 ¡BUENO!"
            resultado.wpm >= 10 && resultado.precision >= 70 -> "🥉 REGULAR"
            else -> "💪 ¡SIGUE PRACTICANDO!"
        }

        panel.add(crearEtiquetaEstadistica("🎖️ Nivel:", nivelRendimiento, Color(230, 126, 34)))

        return panel
    }

    private fun crearEtiquetaEstadistica(titulo: String, valor: String, color: Color = Color.BLACK): JLabel {
        return JLabel("<html><b>$titulo</b><br/><span style='color: rgb(${color.red},${color.green},${color.blue}); font-size: 16px;'>$valor</span></html>").apply {
            font = Font("Arial", Font.PLAIN, 13)
        }
    }

    private fun crearPanelPalabras(): JPanel {
        val panel = JPanel(BorderLayout()).apply {
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color(52, 73, 94), 2),
                    "🔍 ANÁLISIS PALABRA POR PALABRA",
                    0,
                    0,
                    Font("Arial", Font.BOLD, 16),
                    Color(52, 73, 94)
                ),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
            background = Color.WHITE
        }

        val textoPalabras = StringBuilder("<html><body style='font-family: Arial; font-size: 13px;'>")

        palabrasEvaluadas.forEach { palabra ->
            val simbolo = when (palabra.estado) {
                EstadoPalabra.CORRECTA -> "✅"
                EstadoPalabra.INCORRECTA -> "❌"
                EstadoPalabra.PENDIENTE -> "⏳"
            }
            textoPalabras.append("${palabra.texto}$simbolo ")
        }

        textoPalabras.append("<br/><br/><b>📖 LEYENDA:</b><br/>")
        textoPalabras.append("✅ = Correcta | ❌ = Incorrecta | ⏳ = No escrita")
        textoPalabras.append("</body></html>")

        val lblPalabras = JLabel(textoPalabras.toString())
        panel.add(lblPalabras, BorderLayout.CENTER)

        return panel
    }

    private fun crearPanelConsejos(): JPanel {
        val panel = JPanel(BorderLayout()).apply {
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color(52, 73, 94), 2),
                    "💡 CONSEJOS PARA MEJORAR",
                    0,
                    0,
                    Font("Arial", Font.BOLD, 16),
                    Color(52, 73, 94)
                ),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
            background = Color(255, 255, 220)
        }

        val consejos = when {
            resultado.precision < 70 -> """
                <html><body style='font-family: Arial; font-size: 13px;'>
                🎯 Enfócate en la precisión antes que en la velocidad<br/>
                📖 Lee bien el texto antes de empezar a escribir<br/>
                ✋ Mantén una postura correcta y usa todos los dedos
                </body></html>
            """.trimIndent()

            resultado.wpm < 20 -> """
                <html><body style='font-family: Arial; font-size: 13px;'>
                ⚡ Practica ejercicios de velocidad de escritura<br/>
                🔤 Memoriza la posición de las teclas<br/>
                ⏰ Practica regularmente para desarrollar memoria muscular
                </body></html>
            """.trimIndent()

            resultado.precision >= 90 && resultado.wpm >= 30 -> """
                <html><body style='font-family: Arial; font-size: 13px;'>
                🏆 ¡Excelente trabajo! Sigue así<br/>
                📈 Intenta textos más desafiantes<br/>
                🎪 Considera participar en competencias de mecanografía
                </body></html>
            """.trimIndent()

            else -> """
                <html><body style='font-family: Arial; font-size: 13px;'>
                📊 Buen progreso, sigue practicando<br/>
                ⚖️ Mantén el equilibrio entre velocidad y precisión<br/>
                🗓️ Practica diariamente para mejorar consistentemente
                </body></html>
            """.trimIndent()
        }

        val lblConsejos = JLabel(consejos)
        panel.add(lblConsejos, BorderLayout.CENTER)

        return panel
    }
}