package org.example

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class SesionDeMecanografia(
    val id: Int,
    val fecha: String
) {
    companion object {
        private var contadorId: Int = 0  // Nombre más claro para evitar confusión

        // Método para crear sesión automáticamente con id y fecha
        fun nuevaSesion(): SesionDeMecanografia {
            contadorId++
            val fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            return SesionDeMecanografia(contadorId, fecha)
        }
    }
}