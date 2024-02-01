package com.example.clappass

class Palmada {
    private var identificador: Int = 0
    private var nombreUsuario: String = ""
    private var palmada: Int = 0

    // Constructor por defecto
    constructor()

    // Constructor completo
    constructor(identificador: Int, nombreUsuario: String, palmada: Int) {
        this.identificador = identificador
        this.nombreUsuario = nombreUsuario
        this.palmada = palmada
    }

    // Getter y Setter para identificador
    fun getIdentificador(): Int {
        return identificador
    }

    fun setIdentificador(identificador: Int) {
        this.identificador = identificador
    }

    // Getter y Setter para nombreUsuario
    fun getNombreUsuario(): String {
        return nombreUsuario
    }

    fun setNombreUsuario(nombreUsuario: String) {
        this.nombreUsuario = nombreUsuario
    }

    // Getter y Setter para palmada
    fun getPalmada(): Int {
        return palmada
    }

    fun setPalmada(palmada: Int) {
        this.palmada = palmada
    }
}
