package edu.umg.programacion2.gastos.datos

import edu.umg.programacion2.gastos.modelo.Movimiento

interface MovimientoDataSource {

    fun guardar(movimiento: Movimiento): Movimiento

    fun listarTodos(): List<Movimiento>

    fun buscarPorId(id: Int): Movimiento?

    fun actualizar(movimientoActualizado: Movimiento): Boolean

    fun eliminar(id: Int): Boolean
}

