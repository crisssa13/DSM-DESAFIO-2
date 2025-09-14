package com.example.colegioapp.model

data class Estudiante(
    var id: String? = "",
    var nombre: String? = "",
    var apellido: String? = "",
    var grado: String? = "",
    var edad: Int? = 0,
    var direccion: String? = "",
    var telefono: String? = ""
) {
    override fun toString(): String {
        return "$nombre $apellido - $grado"
    }
}
