package com.example.clappass

class Palmada {
    var nombreUsuario: String = ""
    var puntuación: Long = 0

    // Constructor por defecto
    constructor()


    // Constructor completo
    constructor(nombreUsuario: String, puntuación: Long) {
        this.nombreUsuario = nombreUsuario
        this.puntuación = puntuación
    }


    override fun toString():String{
        return "nombreUsuario: "+nombreUsuario+" | "+puntuación
    }



}

