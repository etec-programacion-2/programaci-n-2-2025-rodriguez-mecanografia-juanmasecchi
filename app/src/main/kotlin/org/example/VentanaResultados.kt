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
        setSize(650, 550)
        setLocationRelativeTo(null)

        configurarUI()
    }

    private fun configurarUI() {
        layout = BorderLayout(0, 0)

        // Panel superior con tema violeta
        val panelSuperior = JPanel().apply {
            background = Color(106, 27, 154)
            preferredSize = Dimension(0, 90)
            layout = BorderLayout()
        }

        val panelTextos = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = Color(106, 27, 154)
            border = BorderFactory.createEmptyBorder(20, 0, 20, 0)
        }

        val lblTitulo = JLabel("📊 RESULTADOS").apply {
            font = Font("Arial", Font.BOLD, 28)
            foreground = Color.WHITE
            alignmentX = Component.CENTER_ALIGNMENT
        }

        val nivelRendimiento = when {
            resultado.wpm >= 40 && resultado.precision >= 95 -> "🏆 ¡EXCELENTE!"
            resultado.wpm >= 30 && resultado.precision >= 90 -> "🥇 ¡MUY BUENO!"
            resultado.wpm >= 20 && resultado.precision >= 80 -> "🥈 ¡BUENO!"
            resultado.wpm >= 10 && resultado.precision >= 70 -> "🥉 REGULAR"
            else -> "💪 ¡SIGUE PRACTICANDO!"
        }

        val lblNivel = JLabel(nivelRendimiento).apply {
            font = Font("Arial", Font.BOLD, 16)
            foreground = Color(230, 230, 250)
            alignmentX = Component.CENTER_ALIGNMENT
        }

        panelTextos.add(lblTitulo)
        panelTextos.add(Box.createRigidArea(Dimension(0, 5)))
        panelTextos.add(lblNivel)
        panelSuperior.add(panelTextos, BorderLayout.CENTER)

        // Panel central con estadísticas
        val panelCentral = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(30, 40, 30, 40)
            background = Color(250, 250, 255)
        }

        // Tarjetas de estadísticas principales
        val panelEstadisticas = JPanel(GridLayout(2, 2, 20, 20)).apply {
            background = Color(250, 250, 255)
            maximumSize = Dimension(600, 220)
        }

        panelEstadisticas.add(crearTarjeta("⚡ Velocidad", "${resultado.wpm}", "PPM", Color(126, 87, 194)))
        panelEstadisticas.add(crearTarjeta("🎯 Precisión", String.format("%.1f", resultado.precision), "%", Color(149, 117, 205)))
        panelEstadisticas.add(crearTarjeta("⏱️ Tiempo", "${resultado.tiempoSegundos}", "seg", Color(171, 146, 217)))
        panelEstadisticas.add(crearTarjeta("✅ Correctas", "${resultado.palabrasCorrectas}", "/${resultado.totalPalabras}", Color(94, 53, 177)))

        panelCentral.add(panelEstadisticas)
        panelCentral.add(Box.createRigidArea(Dimension(0, 30)))

        // Consejos
        val panelConsejos = crearPanelConsejos()
        panelCentral.add(panelConsejos)

        // Panel inferior con botones
        val panelInferior = JPanel(FlowLayout(FlowLayout.CENTER, 20, 20)).apply {
            background = Color(250, 250, 255)
        }

        val btnPracticarOtra = JButton("🔄 Practicar Otra Vez").apply {
            font = Font("Arial", Font.BOLD, 16)
            background = Color(126, 87, 194)
            foreground = Color.WHITE
            preferredSize = Dimension(220, 50)
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color(106, 27, 154), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            )

            addMouseListener(object : java.awt.event.MouseAdapter() {
                override fun mouseEntered(e: java.awt.event.MouseEvent) {
                    background = Color(106, 27, 154)
                }
                override fun mouseExited(e: java.awt.event.MouseEvent) {
                    background = Color(126, 87, 194)
                }
            })
        }

        val btnMenu = JButton("🏠 Volver al Menú").apply {
            font = Font("Arial", Font.BOLD, 16)
            background = Color(149, 117, 205)
            foreground = Color.WHITE
            preferredSize = Dimension(220, 50)
            isFocusPainted = false
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color(126, 87, 194), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            )

            addMouseListener(object : java.awt.event.MouseAdapter() {
                override fun mouseEntered(e: java.awt.event.MouseEvent) {
                    background = Color(126, 87, 194)
                }
                override fun mouseExited(e: java.awt.event.MouseEvent) {
                    background = Color(149, 117, 205)
                }
            })
        }

        panelInferior.add(btnPracticarOtra)
        panelInferior.add(btnMenu)

        add(panelSuperior, BorderLayout.NORTH)
        add(panelCentral, BorderLayout.CENTER)
        add(panelInferior, BorderLayout.SOUTH)

        // Listeners
        btnPracticarOtra.addActionListener {
            dispose()
            VentanaPracticaModerna(usuario, texto).isVisible = true
        }

        btnMenu.addActionListener {
            dispose()
            VentanaMenuPrincipal(usuario).isVisible = true
        }
    }

    private fun crearTarjeta(titulo: String, valor: String, unidad: String, color: Color): JPanel {
        val tarjeta = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = color
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        }

        val lblTitulo = JLabel(titulo).apply {
            font = Font("Arial", Font.BOLD, 14)
            foreground = Color.WHITE
            alignmentX = Component.CENTER_ALIGNMENT
        }

        val lblValor = JLabel(valor).apply {
            font = Font("Arial", Font.BOLD, 36)
            foreground = Color.WHITE
            alignmentX = Component.CENTER_ALIGNMENT
        }

        val lblUnidad = JLabel(unidad).apply {
            font = Font("Arial", Font.BOLD, 14)
            foreground = Color(230, 230, 250)
            alignmentX = Component.CENTER_ALIGNMENT
        }

        tarjeta.add(lblTitulo)
        tarjeta.add(Box.createRigidArea(Dimension(0, 10)))
        tarjeta.add(lblValor)
        tarjeta.add(lblUnidad)

        return tarjeta
    }

    private fun crearPanelConsejos(): JPanel {
        val panel = JPanel(BorderLayout()).apply {
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color(171, 146, 217), 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
            background = Color(243, 229, 245)
            maximumSize = Dimension(600, 150)
        }

        val consejos = when {
            resultado.precision < 70 -> """
                <html><body style='font-family: Arial; font-size: 14px; color: #4A148C;'>
                <b>💡 Consejos para mejorar tu precisión:</b><br/><br/>
                • Enfócate en la precisión antes que en la velocidad<br/>
                • Lee bien el texto antes de empezar a escribir<br/>
                • Mantén una postura correcta y usa todos los dedos
                </body></html>
            """.trimIndent()

            resultado.wpm < 20 -> """
                <html><body style='font-family: Arial; font-size: 14px; color: #4A148C;'>
                <b>💡 Consejos para mejorar tu velocidad:</b><br/><br/>
                • Practica ejercicios de velocidad de escritura<br/>
                • Memoriza la posición de las teclas<br/>
                • Practica regularmente para desarrollar memoria muscular
                </body></html>
            """.trimIndent()

            resultado.precision >= 90 && resultado.wpm >= 30 -> """
                <html><body style='font-family: Arial; font-size: 14px; color: #4A148C;'>
                <b>💡 ¡Excelente trabajo!</b><br/><br/>
                • Sigue así, tu rendimiento es sobresaliente<br/>
                • Intenta textos más desafiantes<br/>
                • Considera participar en competencias de mecanografía
                </body></html>
            """.trimIndent()

            else -> """
                <html><body style='font-family: Arial; font-size: 14px; color: #4A148C;'>
                <b>💡 Continúa mejorando:</b><br/><br/>
                • Buen progreso, sigue practicando<br/>
                • Mantén el equilibrio entre velocidad y precisión<br/>
                • Practica diariamente para mejorar consistentemente
                </body></html>
            """.trimIndent()
        }

        val lblConsejos = JLabel(consejos)
        panel.add(lblConsejos, BorderLayout.CENTER)

        return panel
    }
}