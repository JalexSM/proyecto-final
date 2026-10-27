
package edu.umg.programacion2.gastos.datos

import edu.umg.programacion2.gastos.modelo.Movimiento

class MovimientoRepository : MovimientoDataSource {

    private val movimientos = mutableListOf<Movimiento>()
    private var siguienteId = 1




    override fun guardar(movimiento: Movimiento): Movimiento {
        val movimientoConId = movimiento.copy(id = siguienteId)
        siguienteId++
        movimientos.add(movimientoConId)
        return movimientoConId
    }

    override fun listarTodos(): List<Movimiento> {
        return movimientos.toList()
    }

    override fun buscarPorId(id: Int): Movimiento? {
        return movimientos.find { it.id == id }
    }

    override fun actualizar(movimientoActualizado: Movimiento): Boolean {
        val indice = movimientos.indexOfFirst {
            it.id == movimientoActualizado.id
        }

        if (indice == -1) {
            return false
        }

        movimientos[indice] = movimientoActualizado
        return true
    }

    override fun eliminar(id: Int): Boolean {
        return movimientos.removeIf { it.id == id }
    }
}
