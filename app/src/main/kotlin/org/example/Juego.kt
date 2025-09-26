package org.example

import java.util.Scanner

class Juego {
    private val reader = Scanner(System.`in`)
    private var usuario: Usuario? = null
    private val texto = Texto()

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
            println("2. 📝 Cambiar texto")
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
                else -> println("❌ Opción inválida. Por favor, selecciona del 1 al 5.")
            }
        }
    }

    /**
     * Ejecuta la práctica de mecanografía
     */
    private fun practicarMecanografia() {
        println("\n🎯 PRÁCTICA DE MECANOGRAFÍA")
        println("=".repeat(40))
        println("Escribe el siguiente texto exactamente como aparece:")
        println()

        texto.mostrarTexto()
        println()
        println("Presiona ENTER cuando estés listo y escribe el texto...")
        reader.nextLine() // Pausa para que el usuario lea

        print("Tu respuesta: ")
        val respuestaUsuario = reader.nextLine()

        evaluarRespuesta(respuestaUsuario)
    }

    /**
     * Evalúa la respuesta del usuario
     */
    private fun evaluarRespuesta(respuesta: String) {
        println("\n📈 RESULTADO:")
        println("=".repeat(25))
        println("Tu respuesta: \"$respuesta\"")

        // Calcular estadísticas básicas
        val palabrasUsuario = respuesta.split(" ").size
        println("Palabras escritas: $palabrasUsuario")

        println("\n¡Práctica completada! 🏆")
    }

    /**
     * Cambia el texto actual por uno aleatorio
     */
    private fun cambiarTexto() {
        println("\n📝 CAMBIAR TEXTO")
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
        println("\n📊 HISTORIAL")
        println("=".repeat(25))
        usuario?.mostrarHistorial()
    }

    /**
     * Muestra mensaje de despedida
     */
    private fun despedida() {
        println("\n👋 ¡Gracias por jugar!")
        println("Tu progreso ha sido guardado.")
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