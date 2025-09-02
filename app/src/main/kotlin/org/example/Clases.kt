enum class EstadoPalabra {   //define un conjunto cerrado y fijo de valores
    PENDIENTE, CORRECTA, INCORRECTA
}

data class Palabra(    //define un contenedor de datos, con funcionalidades automáticas que facilitan el manejo de información(texto, estado)
    val texto: String,                           
    val estado: EstadoPalabra = EstadoPalabra.PENDIENTE  
)   
