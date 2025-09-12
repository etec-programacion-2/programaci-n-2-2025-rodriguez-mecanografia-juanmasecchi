package org.example

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object GestorArchivos {
    private val archivoUsuarios = File("usuarios.txt")
    private val archivoSesiones = File("sesiones.txt")

    private val usuarios = mutableMapOf<String, Int>()
    private var ultimoIdUsuario = 0
    private var ultimoIdSesion = 0

    init {
        // Crear archivos si no existen
        if (!archivoUsuarios.exists()) archivoUsuarios.createNewFile()
        if (!archivoSesiones.exists()) archivoSesiones.createNewFile()

        // Cargar usuarios
        archivoUsuarios.forEachLine { linea ->
            val partes = linea.split(",")
            if (partes.size >= 2) {
                val id = partes[0].toInt()
                val nombre = partes[1]
                usuarios[nombre] = id
                if (id > ultimoIdUsuario) ultimoIdUsuario = id
            }
        }

        // Cargar último ID de sesión
        archivoSesiones.forEachLine { linea ->
            val partes = linea.split("|")
            if (partes.size >= 3) {
                val idSesionTexto = partes[2].trim().removePrefix("Sesion: ")
                val idSesion = idSesionTexto.toIntOrNull() ?: 0
                if (idSesion > ultimoIdSesion) ultimoIdSesion = idSesion
            }
        }
    }

    fun obtenerUsuario(nombre: String): Usuario {
        val id = usuarios.getOrPut(nombre) {
            ultimoIdUsuario++
            archivoUsuarios.appendText("$ultimoIdUsuario,$nombre\n")
            ultimoIdUsuario
        }
        return Usuario(id, nombre)
    }

    fun nuevaSesionId(): Int {
        ultimoIdSesion++
        return ultimoIdSesion
    }

    fun guardarSesion(idUsuario: Int, nombre: String, sesion: SesionDeMecanografia) {
        archivoSesiones.appendText("Usuario: $nombre | ID Usuario: $idUsuario | Sesion: ${sesion.id} | Fecha: ${sesion.fecha}\n")
    }

    // ✅ Nueva función para cargar sesiones de un usuario específico
    fun cargarSesionesUsuario(idUsuario: Int): List<SesionDeMecanografia> {
        val sesiones = mutableListOf<SesionDeMecanografia>()

        archivoSesiones.forEachLine { linea ->
            val partes = linea.split("|")
            if (partes.size >= 4) {
                val idUsuarioTexto = partes[1].trim().removePrefix("ID Usuario: ")
                val idUsuarioArchivo = idUsuarioTexto.toIntOrNull()

                if (idUsuarioArchivo == idUsuario) {
                    val idSesionTexto = partes[2].trim().removePrefix("Sesion: ")
                    val fechaTexto = partes[3].trim().removePrefix("Fecha: ")

                    val idSesion = idSesionTexto.toIntOrNull()
                    if (idSesion != null) {
                        sesiones.add(SesionDeMecanografia(idSesion, fechaTexto))
                    }
                }
            }
        }

        return sesiones
    }
}