package org.example

import java.util.Scanner

class Juego {
    private val reader = Scanner(System.`in`)
    private var usuario: Usuario? = null
    private val texto = Texto()
    private val estadisticas = EstadisticasJuego()

    /**
     * Inicia el juego completo de mecanografía
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
        println("    🎮 JUEGO DE MECANOGRAFÍA 🎮")
        println("=".repeat(50))
        println("¡Bienvenido al juego de práctica de mecanografía!")
        println("📝 Textos de 50 palabras")
        println("📊 Estadísticas detalladas: WPM y precisión")
        println("🎯 ¡Mejora tu velocidad y precisión!")
        println()
    }

    /**
     * Configura el usuario del juego
     */
    private fun configurarUsuario(): Boolean {
        print("Por favor, ingresa tu nombre: ")
        val nombre = reader.nextLine().trim()

        if (nombre.isBlank()) {
            println("❌ Error: Debe ingresar un nombre válido.")
            return false
        }

        usuario = GestorArchivos.obtenerUsuario(nombre)
        println("👋 ¡Hola, $nombre!")
        return true
    }

    /**
     * Inicia una nueva sesión para el usuario
     */
    private fun iniciarSesion() {
        usuario?.let {
            it.iniciarNuevaSesion()
            println("✅ Nueva sesión iniciada correctamente.")
            println()
        }
    }

    /**
     * Muestra el menú principal y maneja las opciones
     */
    private fun mostrarMenuPrincipal() {
        var continuar = true

        while (continuar) {
            println("\n📋 MENÚ PRINCIPAL")
            println("=".repeat(30))
            println("1. 🎯 Practicar mecanografía")
            println("2. 🔄 Cambiar texto")
            println("3. 📊 Ver historial")
            println("4. 🚪 Salir")
            println("=".repeat(30))
            print("Selecciona una opción (1-4): ")

            when (reader.nextLine().trim()) {
                "1" -> practicarMecanografia()
                "2" -> cambiarTexto()
                "3" -> verHistorial()
                "4" -> {
                    continuar = false
                    despedida()
                }
                else -> println("❌ Opción inválida. Por favor, selecciona del 1 al 4.")
            }
        }
    }

    /**
     * Ejecuta la práctica de mecanografía con medición de tiempo y estadísticas
     */
    private fun practicarMecanografia() {
        println("\n🎯 PRÁCTICA DE MECANOGRAFÍA")
        println("=".repeat(40))
        println("📝 Texto: ${texto.obtenerNumeroPalabras()} palabras")
        println("⏱️  Se medirá tu velocidad y precisión")
        println()

        texto.mostrarTexto()
        println()
        println("⚡ INSTRUCCIONES:")
        println("- Escribe el texto exactamente como aparece arriba")
        println("- Respeta mayúsculas, minúsculas y acentos")
        println("- El cronómetro empezará cuando comiences a escribir")
        println("- Presiona ENTER al terminar para ver tus resultados")
        println()
        println("Presiona ENTER cuando estés listo para comenzar...")

        reader.nextLine() // Pausa para que el usuario lea

        println("\n🚀 ¡COMIENZA AHORA! Escribe el texto:")
        print("➤ ")

        // Medir tiempo de escritura
        val tiempoInicio = System.currentTimeMillis()
        val respuestaUsuario = reader.nextLine()
        val tiempoFin = System.currentTimeMillis()

        val tiempoTranscurrido = (tiempoFin - tiempoInicio) / 1000 // Convertir a segundos

        evaluarRespuestaConEstadisticas(respuestaUsuario, tiempoTranscurrido)
    }

    /**
     * Evalúa la respuesta del usuario con estadísticas completas
     */
    private fun evaluarRespuestaConEstadisticas(respuesta: String, tiempoSegundos: Long) {
        println("\n⏱️ TIEMPO TRANSCURRIDO: ${tiempoSegundos}s")

        // Evaluar respuesta palabra por palabra
        val textoOriginal = texto.obtenerTextoActual()
        val palabrasEvaluadas = estadisticas.evaluarRespuesta(textoOriginal, respuesta)

        // Generar reporte completo
        val resultado = estadisticas.generarReporte(palabrasEvaluadas, tiempoSegundos)

        // Mostrar análisis palabra por palabra
        estadisticas.mostrarPalabrasConEstado(palabrasEvaluadas)

        // Mostrar reporte de estadísticas
        resultado.mostrarReporte()

        // Guardar estadísticas en sesión (si se implementa persistencia adicional)
        mostrarConsejos(resultado)
    }

    /**
     * Muestra consejos basados en el rendimiento
     */
    private fun mostrarConsejos(resultado: ResultadoJuego) {
        println("\n💡 CONSEJOS PARA MEJORAR:")
        println("=".repeat(40))

        when {
            resultado.precision < 70 -> {
                println("🎯 Enfócate en la precisión antes que en la velocidad")
                println("📖 Lee bien el texto antes de empezar a escribir")
                println("✋ Mantén una postura correcta y usa todos los dedos")
            }
            resultado.wpm < 20 -> {
                println("⚡ Practica ejercicios de velocidad de escritura")
                println("🔤 Memoriza la posición de las teclas")
                println("⏰ Practica regularmente para desarrollar memoria muscular")
            }
            resultado.precision >= 90 && resultado.wpm >= 30 -> {
                println("🏆 ¡Excelente trabajo! Sigue así")
                println("📈 Intenta textos más desafiantes")
                println("🎪 Considera participar en competencias de mecanografía")
            }
            else -> {
                println("📊 Buen progreso, sigue practicando")
                println("⚖️  Mantén el equilibrio entre velocidad y precisión")
                println("🗓️  Practica diariamente para mejorar consistentemente")
            }
        }

        println("=".repeat(40))
    }

    /**
     * Cambia el texto actual por uno aleatorio
     */
    private fun cambiarTexto() {
        println("\n🔄 CAMBIAR TEXTO")
        println("=".repeat(25))
        texto.cambiarTexto()
    }

    /**
     * Muestra el historial del usuario
     */
    private fun verHistorial() {
        println("\n📊 HISTORIAL DE SESIONES")
        println("=".repeat(25))
        usuario?.mostrarHistorial()
    }

    /**
     * Muestra mensaje de despedida
     */
    private fun despedida() {
        println("\n👋 ¡Gracias por jugar!")
        println("Tu progreso ha sido guardado.")
        println("📈 Recuerda: la práctica constante mejora tus habilidades")
        println("¡Vuelve pronto para seguir practicando! 🎯")
        println("=".repeat(50))
    }

    /**
     * Cierra los recursos del juego
     */
    fun cerrar() {
        reader.close()
    }
}