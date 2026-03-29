package org.example

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class SesionDeMecanografia(
    val id: Int,
    val fecha: String
) {
    companion object {
        fun nuevaSesion(): SesionDeMecanografia {
            val id = GestorArchivos.nuevaSesionId()
            val fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            return SesionDeMecanografia(id, fecha)
        }
    }
}