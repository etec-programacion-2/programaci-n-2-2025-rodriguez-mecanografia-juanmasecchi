package org.example

import kotlin.math.roundToInt

class EstadisticasJuego {

    /**
     * Calcula las palabras por minuto (WPM)
     * @param palabrasCorrectas Número de palabras escritas correctamente
     * @param tiempoEnSegundos Tiempo total de escritura en segundos
     * @return WPM calculado
     */
    fun calcularWPM(palabrasCorrectas: Int, tiempoEnSegundos: Long): Double {
        if (tiempoEnSegundos <= 0) return 0.0
        val minutos = tiempoEnSegundos / 60.0
        return if (minutos > 0) palabrasCorrectas / minutos else 0.0
    }

    /**
     * Calcula la precisión basada en el estado de las palabras
     * @param palabras Lista de palabras con su estado
     * @return Precisión como porcentaje (0-100)
     */
    fun calcularPrecision(palabras: List<Palabra>): Double {
        if (palabras.isEmpty()) return 0.0

        val palabrasCorrectas = palabras.count { it.estado == EstadoPalabra.CORRECTA }
        return (palabrasCorrectas.toDouble() / palabras.size) * 100
    }

    /**
     * Evalúa la respuesta del usuario comparándola palabra por palabra con el texto original
     * @param textoOriginal Lista de palabras del texto original
     * @param respuestaUsuario Texto escrito por el usuario
     * @return Lista de palabras con su estado evaluado
     */
    fun evaluarRespuesta(textoOriginal: List<String>, respuestaUsuario: String): List<Palabra> {
        val palabrasUsuario = respuestaUsuario.trim().split("\\s+".toRegex())
        val palabrasEvaluadas = mutableListOf<Palabra>()

        // Evaluar cada palabra del texto original
        for (i in textoOriginal.indices) {
            val palabraOriginal = textoOriginal[i]
            val palabraUsuario = if (i < palabrasUsuario.size) palabrasUsuario[i] else ""

            val estado = when {
                palabraUsuario.isEmpty() -> EstadoPalabra.PENDIENTE
                palabraUsuario == palabraOriginal -> EstadoPalabra.CORRECTA
                else -> EstadoPalabra.INCORRECTA
            }

            palabrasEvaluadas.add(Palabra(palabraOriginal, estado))
        }

        return palabrasEvaluadas
    }

    /**
     * Genera un reporte completo de estadísticas
     * @param palabrasEvaluadas Lista de palabras con su estado
     * @param tiempoEnSegundos Tiempo total de escritura
     * @return Objeto ResultadoJuego con todas las estadísticas
     */
    fun generarReporte(palabrasEvaluadas: List<Palabra>, tiempoEnSegundos: Long): ResultadoJuego {
        val precision = calcularPrecision(palabrasEvaluadas)
        val palabrasCorrectas = palabrasEvaluadas.count { it.estado == EstadoPalabra.CORRECTA }
        val palabrasIncorrectas = palabrasEvaluadas.count { it.estado == EstadoPalabra.INCORRECTA }
        val palabrasPendientes = palabrasEvaluadas.count { it.estado == EstadoPalabra.PENDIENTE }
        val wpm = calcularWPM(palabrasCorrectas, tiempoEnSegundos)

        return ResultadoJuego(
            palabrasCorrectas = palabrasCorrectas,
            palabrasIncorrectas = palabrasIncorrectas,
            palabrasPendientes = palabrasPendientes,
            totalPalabras = palabrasEvaluadas.size,
            precision = precision,
            wpm = wpm.roundToInt(),
            tiempoSegundos = tiempoEnSegundos
        )
    }

    /**
     * Muestra las palabras con símbolos según su estado
     * ✅ = Palabra correcta (escrita exactamente como el original)
     * ❌ = Palabra incorrecta (con errores de ortografía, mayúsculas o acentos)
     * ⏳ = Palabra no escrita (el usuario no llegó a escribirla)
     *
     * @param palabrasEvaluadas Lista de palabras evaluadas
     */
    fun mostrarPalabrasConEstado(palabrasEvaluadas: List<Palabra>) {
        println("\n📝 ANÁLISIS PALABRA POR PALABRA:")
        println("=".repeat(50))

        var lineaActual = ""
        for (palabra in palabrasEvaluadas) {
            val simbolo = when (palabra.estado) {
                EstadoPalabra.CORRECTA -> "✅"
                EstadoPalabra.INCORRECTA -> "❌"
                EstadoPalabra.PENDIENTE -> "⏳"
            }

            val palabraConSimbolo = "${palabra.texto}$simbolo"

            // Control de línea para no hacer líneas muy largas
            if (lineaActual.length + palabraConSimbolo.length > 60) {
                println(lineaActual.trim())
                lineaActual = palabraConSimbolo + " "
            } else {
                lineaActual += palabraConSimbolo + " "
            }
        }

        // Imprimir la última línea
        if (lineaActual.isNotEmpty()) {
            println(lineaActual.trim())
        }

        println("\n📖 LEYENDA:")
        println("✅ = Correcta | ❌ = Incorrecta | ⏳ = No escrita")
    }
}

/**
 * Data class para encapsular todos los resultados del juego
 */
data class ResultadoJuego(
    val palabrasCorrectas: Int,
    val palabrasIncorrectas: Int,
    val palabrasPendientes: Int,
    val totalPalabras: Int,
    val precision: Double,
    val wpm: Int,
    val tiempoSegundos: Long
) {
    fun mostrarReporte() {
        println("\n📊 REPORTE DE ESTADÍSTICAS")
        println("=".repeat(40))
        println("⏱️  Tiempo total: ${tiempoSegundos}s")
        println("📝 Palabras totales: $totalPalabras")
        println("✅ Palabras correctas: $palabrasCorrectas")
        println("❌ Palabras incorrectas: $palabrasIncorrectas")
        println("⏳ Palabras no escritas: $palabrasPendientes")
        println("🎯 Precisión: ${"%.1f".format(precision)}%")
        println("⚡ Velocidad: $wpm WPM")

        // Clasificación del rendimiento
        val nivelRendimiento = when {
            wpm >= 40 && precision >= 95 -> "🏆 ¡EXCELENTE!"
            wpm >= 30 && precision >= 90 -> "🥇 ¡MUY BUENO!"
            wpm >= 20 && precision >= 80 -> "🥈 ¡BUENO!"
            wpm >= 10 && precision >= 70 -> "🥉 REGULAR"
            else -> "💪 ¡SIGUE PRACTICANDO!"
        }

        println("🎖️ Nivel: $nivelRendimiento")
        println("=".repeat(40))
    }
}