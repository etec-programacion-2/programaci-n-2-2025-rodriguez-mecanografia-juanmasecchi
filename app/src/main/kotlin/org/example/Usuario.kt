package org.example

class Usuario(val id: Int, val nombre: String) {
    private val sesiones: MutableList<SesionDeMecanografia> = mutableListOf()

    init {
        // Cargar sesiones existentes del usuario al crear la instancia
        cargarSesiones()
    }

    private fun cargarSesiones() {
        val sesionesCargadas = GestorArchivos.cargarSesionesUsuario(id)
        sesiones.addAll(sesionesCargadas)
    }

    fun iniciarNuevaSesion() {
        val sesion = SesionDeMecanografia.nuevaSesion()
        sesiones.add(sesion)

        // ✅ Guardar sesión en archivo
        GestorArchivos.guardarSesion(id, nombre, sesion)
    }

    fun mostrarHistorial() {
        if (sesiones.isEmpty()) {
            println("El usuario $nombre no tiene sesiones registradas.")
        } else {
            println("Historial de $nombre:")
            for (sesion in sesiones) {
                println("Usuario: $nombre | ID Sesión: ${sesion.id} | Fecha: ${sesion.fecha}")
            }
        }
    }
}
