package org.example

class Texto {
    private var textoActual: List<String> = listOf()

    // Textos predefinidos para práctica de mecanografía
    private val textosDisponibles = listOf(
        listOf("El", "gato", "subió", "al", "tejado", "para", "descansar", "bajo", "el", "sol"),
        listOf("La", "programación", "en", "Kotlin", "es", "divertida", "y", "eficiente", "para", "desarrolladores"),
        listOf("Practicar", "mecanografía", "mejora", "la", "velocidad", "de", "escritura", "significativamente"),
        listOf("Los", "datos", "estructurados", "facilitan", "el", "procesamiento", "de", "información", "compleja"),
        listOf("Una", "buena", "postura", "es", "fundamental", "para", "escribir", "correctamente", "en", "el", "teclado")
    )

    init {
        // Inicializar con el primer texto disponible
        if (textosDisponibles.isNotEmpty()) {
            textoActual = textosDisponibles[0]
        }
    }

    /**
     * Muestra el texto actual completo
     */
    fun mostrarTexto() {
        if (textoActual.isEmpty()) {
            println("No hay texto cargado.")
            return
        }

        println("Texto actual:")
        println(textoActual.joinToString(" "))
    }

    /**
     * Cambia a un texto aleatorio de los disponibles
     */
    fun cambiarTexto() {
        val indiceAleatorio = textosDisponibles.indices.random()
        textoActual = textosDisponibles[indiceAleatorio]
        println("✅ Texto cambiado exitosamente.")
    }

}