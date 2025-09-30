package org.example

import kotlin.math.roundToInt

class EstadisticasJuego {

    /**
     * Calcula las palabras por minuto (WPM)
     * @param palabrasCorrectas NÃºmero de palabras escritas correctamente
     * @param tiempoEnSegundos Tiempo total de escritura en segundos
     * @return WPM calculado
     */
    fun calcularWPM(palabrasCorrectas: Int, tiempoEnSegundos: Long): Double {
        if (tiempoEnSegundos <= 0) return 0.0
        val minutos = tiempoEnSegundos / 60.0
        return if (minutos > 0) palabrasCorrectas / minutos else 0.0
    }

    /**
     * Calcula la precisiÃ³n basada en el estado de las palabras
     * @param palabras Lista de palabras con su estado
     * @return PrecisiÃ³n como porcentaje (0-100)
     */
    fun calcularPrecision(palabras: List<Palabra>): Double {
        if (palabras.isEmpty()) return 0.0

        val palabrasCorrectas = palabras.count { it.estado == EstadoPalabra.CORRECTA }
        return (palabrasCorrectas.toDouble() / palabras.size) * 100
    }

    /**
     * EvalÃºa la respuesta del usuario comparÃ¡ndola palabra por palabra con el texto original
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
     * Genera un reporte completo de estadÃ­sticas
     * @param palabrasEvaluadas Lista de palabras con su estado
     * @param tiempoEnSegundos Tiempo total de escritura
     * @return Objeto ResultadoJuego con todas las estadÃ­sticas
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
     * Muestra las palabras con colores segÃºn su estado (simulado con sÃ­mbolos)
     * @param palabrasEvaluadas Lista de palabras evaluadas
     */
    fun mostrarPalabrasConEstado(palabrasEvaluadas: List<Palabra>) {
        println("\nðŸ“ ANÃLISIS PALABRA por PALABRA:")
        println("=".repeat(50))

        var lineaActual = ""
        for (palabra in palabrasEvaluadas) {
            val simbolo = when (palabra.estado) {
                EstadoPalabra.CORRECTA -> "âœ…"
                EstadoPalabra.INCORRECTA -> "âŒ"
                EstadoPalabra.PENDIENTE -> "â³"
            }

            val palabraConSimbolo = "${palabra.texto}$simbolo"

            // Control de lÃ­nea para no hacer lÃ­neas muy largas
            if (lineaActual.length + palabraConSimbolo.length > 60) {
                println(lineaActual.trim())
                lineaActual = palabraConSimbolo + " "
            } else {
                lineaActual += palabraConSimbolo + " "
            }
        }

        // Imprimir la Ãºltima lÃ­nea
        if (lineaActual.isNotEmpty()) {
            println(lineaActual.trim())
        }

        println("\nðŸ“Š LEYENDA:")
        println("âœ… = Correcta | âŒ = Incorrecta | â³ = No escrita")
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
        println("\nðŸ“Š REPORTE DE ESTADÃSTICAS")
        println("=".repeat(40))
        println("â±ï¸  Tiempo total: ${tiempoSegundos}s")
        println("ðŸ“ Palabras totales: $totalPalabras")
        println("âœ… Palabras correctas: $palabrasCorrectas")
        println("âŒ Palabras incorrectas: $palabrasIncorrectas")
        println("â³ Palabras no escritas: $palabrasPendientes")
        println("ðŸŽ¯ PrecisiÃ³n: ${"%.1f".format(precision)}%")
        println("âš¡ Velocidad: $wpm WPM")

        // ClasificaciÃ³n del rendimiento
        val nivelRendimiento = when {
            wpm >= 40 && precision >= 95 -> "ðŸ† Â¡EXCELENTE!"
            wpm >= 30 && precision >= 90 -> "ðŸ¥‡ Â¡MUY BUENO!"
            wpm >= 20 && precision >= 80 -> "ðŸ¥ˆ Â¡BUENO!"
            wpm >= 10 && precision >= 70 -> "ðŸ¥‰ REGULAR"
            else -> "ðŸ’ª Â¡SIGUE PRACTICANDO!"
        }

        println("ðŸ… Nivel: $nivelRendimiento")
        println("=".repeat(40))
    }
}