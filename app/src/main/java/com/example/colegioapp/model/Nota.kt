package com.example.colegioapp.model

data class Nota(
    var id: String? = "",
    var estudianteId: String? = "",
    var grado: String? = "",
    var materia: String? = "",
    var notaFinal: Double? = 0.0
) {
    override fun toString(): String {
        return "$materia ($grado): $notaFinal"
    }
}

