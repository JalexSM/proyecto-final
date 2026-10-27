package edu.umg.programacion2.gastos.modelo

data class Movimiento(
    val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val tipo: TipoMovimiento,
    val categoria: Categoria,
    val fecha: String
)
