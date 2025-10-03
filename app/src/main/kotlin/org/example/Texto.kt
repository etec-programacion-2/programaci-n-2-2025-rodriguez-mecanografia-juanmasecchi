package org.example

class Texto {
    private var textoActual: List<String> = listOf()

    // Textos predefinidos de exactamente 100 palabras cada uno
    private val textosDisponibles = listOf(
        // Texto 1 - Sobre programación (50 palabras)
        listOf("La", "programación", "es", "una", "habilidad", "fundamental", "en", "el", "mundo", "moderno", "que", "permite", "crear", "soluciones", "innovadoras", "Los", "desarrolladores", "utilizan", "diferentes", "lenguajes", "como", "Java", "Python", "Kotlin", "y", "JavaScript", "para", "construir", "aplicaciones", "El", "proceso", "incluye", "planificación", "diseño", "codificación", "y", "pruebas", "Los", "programadores", "deben", "pensar", "lógicamente", "y", "resolver", "problemas", "La", "programación", "requiere", "práctica", "constante"),

        // Texto 2 - Sobre naturaleza (50 palabras)
        listOf("Los", "bosques", "tropicales", "son", "ecosistemas", "diversos", "que", "albergan", "millones", "de", "especies", "de", "plantas", "y", "animales", "únicos", "Estos", "ambientes", "húmedos", "proporcionan", "condiciones", "ideales", "para", "vegetación", "exuberante", "Los", "árboles", "gigantes", "forman", "un", "dosel", "que", "filtra", "la", "luz", "solar", "La", "conservación", "de", "estos", "espacios", "es", "crucial", "para", "mantener", "el", "equilibrio", "ecológico", "y", "biodiversidad"),

        // Texto 3 - Sobre tecnología (50 palabras)
        listOf("La", "inteligencia", "artificial", "está", "transformando", "múltiples", "sectores", "incluyendo", "medicina", "educación", "y", "transporte", "Los", "algoritmos", "de", "aprendizaje", "procesan", "enormes", "cantidades", "de", "datos", "para", "identificar", "patrones", "y", "hacer", "predicciones", "Los", "sistemas", "inteligentes", "ayudan", "a", "diagnosticar", "enfermedades", "Los", "vehículos", "autónomos", "utilizan", "sensores", "avanzados", "Es", "importante", "considerar", "las", "implicaciones", "éticas", "de", "estas", "tecnologías", "emergentes"),

        // Texto 4 - Sobre historia (50 palabras)
        listOf("Las", "civilizaciones", "antiguas", "desarrollaron", "sistemas", "de", "escritura", "sofisticados", "que", "preservaron", "conocimientos", "y", "tradiciones", "culturales", "Los", "egipcios", "crearon", "jeroglíficos", "en", "papiros", "y", "monumentos", "Los", "griegos", "perfeccionaron", "el", "alfabeto", "facilitando", "la", "difusión", "de", "ideas", "Los", "romanos", "expandieron", "estos", "sistemas", "estableciendo", "bibliotecas", "Estos", "avances", "en", "comunicación", "escrita", "fueron", "fundamentales", "para", "el", "progreso", "humano"),

        // Texto 5 - Sobre ciencia (50 palabras)
        listOf("El", "método", "científico", "es", "un", "proceso", "sistemático", "que", "permite", "obtener", "conocimiento", "confiable", "Los", "científicos", "formulan", "hipótesis", "basadas", "en", "observaciones", "y", "diseñan", "experimentos", "controlados", "La", "recopilación", "de", "datos", "debe", "ser", "objetiva", "y", "reproducible", "Las", "conclusiones", "se", "publican", "en", "revistas", "especializadas", "Este", "proceso", "de", "revisión", "asegura", "la", "calidad", "de", "la", "investigación", "científica")
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

        println("ðŸ“– TEXTO A ESCRIBIR (${textoActual.size} palabras):")
        println("=".repeat(60))

        // Mostrar texto con formato mejorado (10 palabras por lÃ­nea)
        val palabrasPorLinea = 10
        for (i in textoActual.indices step palabrasPorLinea) {
            val finLinea = minOf(i + palabrasPorLinea, textoActual.size)
            val linea = textoActual.subList(i, finLinea).joinToString(" ")
            println(linea)
        }

        println("=".repeat(60))
    }

    /**
     * Cambia a un texto aleatorio de los disponibles
     */
    fun cambiarTexto() {
        val indiceAleatorio = textosDisponibles.indices.random()
        textoActual = textosDisponibles[indiceAleatorio]
        println("âœ… Texto cambiado exitosamente.")
    }

    /**
     * Obtiene el texto actual como lista de palabras
     */
    fun obtenerTextoActual(): List<String> {
        return textoActual.toList()
    }

    /**
     * Obtiene el nÃºmero de palabras del texto actual
     */
    fun obtenerNumeroPalabras(): Int {
        return textoActual.size
    }
}