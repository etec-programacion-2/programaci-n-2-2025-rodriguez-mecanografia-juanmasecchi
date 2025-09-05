package org.example

class Usuario(val nombre: String) {
    private val sesiones: MutableList<SesionDeMecanografia> = mutableListOf()

    fun iniciarNuevaSesion() {
        val sesion = SesionDeMecanografia.nuevaSesion()
        sesiones.add(sesion)
    }

    fun obtenerHistorial(): List<SesionDeMecanografia> {
        return sesiones.toList()
    }

    // Mostrar historial con nombre, id y fecha
    fun mostrarHistorial() {
        if (sesiones.isEmpty()) {
            println("El usuario $nombre no tiene sesiones registradas.")
        } else {
            println("Historial de $nombre:")
            for (sesion in sesiones) {
                println("Usuario: $nombre | ID: ${sesion.id} | Fecha: ${sesion.fecha}")
            }
        }
    }
}