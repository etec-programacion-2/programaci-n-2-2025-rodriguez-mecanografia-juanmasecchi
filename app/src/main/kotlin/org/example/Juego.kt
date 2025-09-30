package org.example

import java.util.Scanner

class Juego {
    private val reader = Scanner(System.`in`)
    private var usuario: Usuario? = null
    private val texto = Texto()
    private val estadisticas = EstadisticasJuego()

    /**
     * Inicia el juego completo de mecanografÃ­a
     */
    fun iniciar() {
        mostrarBienvenida()

        if (!configurarUsuario()) {
            return
        }

        iniciarSesion()
        mostrarMenuPrincipal()
    }

    /**
     * Muestra la pantalla de bienvenida
     */
    private fun mostrarBienvenida() {
        println("=".repeat(50))
        println("    ðŸŽ® JUEGO DE MECANOGRAFÃA ðŸŽ®")
        println("=".repeat(50))
        println("Â¡Bienvenido al juego de prÃ¡ctica de mecanografÃ­a!")
        println("ðŸ“ Textos de 100 palabras")
        println("ðŸ“Š EstadÃ­sticas detalladas: WPM y precisiÃ³n")
        println("ðŸŽ¯ Â¡Mejora tu velocidad y precisiÃ³n!")
        println()
    }

    /**
     * Configura el usuario del juego
     */
    private fun configurarUsuario(): Boolean {
        print("Por favor, ingresa tu nombre: ")
        val nombre = reader.nextLine().trim()

        if (nombre.isBlank()) {
            println("âŒ Error: Debe ingresar un nombre vÃ¡lido.")
            return false
        }

        usuario = GestorArchivos.obtenerUsuario(nombre)
        println("ðŸ‘‹ Â¡Hola, $nombre!")
        return true
    }

    /**
     * Inicia una nueva sesiÃ³n para el usuario
     */
    private fun iniciarSesion() {
        usuario?.let {
            it.iniciarNuevaSesion()
            println("âœ… Nueva sesiÃ³n iniciada correctamente.")
            println()
        }
    }

    /**
     * Muestra el menÃº principal y maneja las opciones
     */
    private fun mostrarMenuPrincipal() {
        var continuar = true

        while (continuar) {
            println("\nðŸ“‹ MENÃš PRINCIPAL")
            println("=".repeat(30))
            println("1. ðŸŽ¯ Practicar mecanografÃ­a")
            println("2. ðŸ“„ Cambiar texto")
            println("3. ðŸ“Š Ver historial")
            println("4. ðŸšª Salir")
            println("=".repeat(30))
            print("Selecciona una opciÃ³n (1-4): ")

            when (reader.nextLine().trim()) {
                "1" -> practicarMecanografia()
                "2" -> cambiarTexto()
                "3" -> verHistorial()
                "4" -> {
                    continuar = false
                    despedida()
                }
                else -> println("âŒ OpciÃ³n invÃ¡lida. Por favor, selecciona del 1 al 4.")
            }
        }
    }

    /**
     * Ejecuta la prÃ¡ctica de mecanografÃ­a con mediciÃ³n de tiempo y estadÃ­sticas
     */
    private fun practicarMecanografia() {
        println("\nðŸŽ¯ PRÃCTICA DE MECANOGRAFÃA")
        println("=".repeat(40))
        println("ðŸ“ Texto: ${texto.obtenerNumeroPalabras()} palabras")
        println("â±ï¸  Se medirÃ¡ tu velocidad y precisiÃ³n")
        println()

        texto.mostrarTexto()
        println()
        println("âš¡ Instrucciones:")
        println("- Escribe el texto exactamente como aparece")
        println("- Se medirÃ¡ el tiempo desde que empieces a escribir")
        println("- Presiona ENTER cuando estÃ©s listo...")

        reader.nextLine() // Pausa para que el usuario lea

        println("\nðŸš€ Â¡COMENZANDO! Escribe el texto:")
        print("Tu respuesta: ")

        // Medir tiempo de escritura
        val tiempoInicio = System.currentTimeMillis()
        val respuestaUsuario = reader.nextLine()
        val tiempoFin = System.currentTimeMillis()

        val tiempoTranscurrido = (tiempoFin - tiempoInicio) / 1000 // Convertir a segundos

        evaluarRespuestaConEstadisticas(respuestaUsuario, tiempoTranscurrido)
    }

    /**
     * EvalÃºa la respuesta del usuario con estadÃ­sticas completas
     */
    private fun evaluarRespuestaConEstadisticas(respuesta: String, tiempoSegundos: Long) {
        println("\nâ±ï¸ TIEMPO TRANSCURRIDO: ${tiempoSegundos}s")

        // Evaluar respuesta palabra por palabra
        val textoOriginal = texto.obtenerTextoActual()
        val palabrasEvaluadas = estadisticas.evaluarRespuesta(textoOriginal, respuesta)

        // Generar reporte completo
        val resultado = estadisticas.generarReporte(palabrasEvaluadas, tiempoSegundos)

        // Mostrar anÃ¡lisis palabra por palabra
        estadisticas.mostrarPalabrasConEstado(palabrasEvaluadas)

        // Mostrar reporte de estadÃ­sticas
        resultado.mostrarReporte()

        // Guardar estadÃ­sticas en sesiÃ³n (si se implementa persistencia adicional)
        mostrarConsejos(resultado)
    }

    /**
     * Muestra consejos basados en el rendimiento
     */
    private fun mostrarConsejos(resultado: ResultadoJuego) {
        println("\nðŸ’¡ CONSEJOS PARA MEJORAR:")
        println("=".repeat(40))

        when {
            resultado.precision < 70 -> {
                println("ðŸŽ¯ EnfÃ³cate en la precisiÃ³n antes que en la velocidad")
                println("ðŸ“– Lee bien el texto antes de empezar a escribir")
                println("âœ‹ MantÃ©n una postura correcta y usa todos los dedos")
            }
            resultado.wpm < 20 -> {
                println("âš¡ Practica ejercicios de velocidad de escritura")
                println("ðŸ”¤ Memoriza la posiciÃ³n de las teclas")
                println("â° Practica regularmente para desarrollar memoria muscular")
            }
            resultado.precision >= 90 && resultado.wpm >= 30 -> {
                println("ðŸ† Â¡Excelente trabajo! Sigue asÃ­")
                println("ðŸ“ˆ Intenta textos mÃ¡s desafiantes")
                println("ðŸŽª Considera participar en competencias de mecanografÃ­a")
            }
            else -> {
                println("ðŸ“Š Buen progreso, sigue practicando")
                println("âš–ï¸  MantÃ©n el equilibrio entre velocidad y precisiÃ³n")
                println("ðŸ—“ï¸  Practica diariamente para mejorar consistentemente")
            }
        }

        println("=".repeat(40))
    }

    /**
     * Cambia el texto actual por uno aleatorio
     */
    private fun cambiarTexto() {
        println("\nðŸ“„ CAMBIAR TEXTO")
        println("=".repeat(25))
        texto.cambiarTexto()
    }

    /**
     * Muestra el texto actual
     */

    /**
     * Muestra el historial del usuario
     */
    private fun verHistorial() {
        println("\nðŸ“Š HISTORIAL")
        println("=".repeat(25))
        usuario?.mostrarHistorial()
    }

    /**
     * Muestra mensaje de despedida
     */
    private fun despedida() {
        println("\nðŸ‘‹ Â¡Gracias por jugar!")
        println("Tu progreso ha sido guardado.")
        println("ðŸ“ˆ Recuerda: la prÃ¡ctica constante mejora tus habilidades")
        println("Â¡Vuelve pronto para seguir practicando! ðŸŽ¯")
        println("=".repeat(50))
    }

    /**
     * Cierra los recursos del juego
     */
    fun cerrar() {
        reader.close()
    }
}