
package edu.umg.programacion2.gastos.servicio

import edu.umg.programacion2.gastos.datos.MovimientoDataSource
import edu.umg.programacion2.gastos.modelo.Movimiento
import edu.umg.programacion2.gastos.modelo.TipoMovimiento

class MovimientoService(
    private val repository: MovimientoDataSource
) {

    fun registrar(movimiento: Movimiento): Movimiento {
        require(movimiento.id == 0) {
            "El ID se asigna automáticamente"
        }

        validarMovimiento(movimiento)

        return repository.guardar(movimiento)
    }

    fun listarMovimientos(): List<Movimiento> {
        return repository.listarTodos()
    }

    fun buscarPorId(id: Int): Movimiento? {
        return repository.buscarPorId(id)
    }

    fun actualizar(movimiento: Movimiento): Boolean {
        validarMovimiento(movimiento)

        if (movimiento.id <= 0) {
            return false
        }

        if (repository.buscarPorId(movimiento.id) == null) {
            return false
        }

        return repository.actualizar(movimiento)
    }

    fun eliminar(id: Int): Boolean {
        return repository.eliminar(id)
    }

    fun calcularTotalIngresos(): Double {
        return repository.listarTodos()
            .filter { it.tipo == TipoMovimiento.INGRESO }
            .sumOf { it.monto }
    }

    fun calcularTotalGastos(): Double {
        return repository.listarTodos()
            .filter { it.tipo == TipoMovimiento.GASTO }
            .sumOf { it.monto }
    }

    fun calcularSaldo(): Double {
        return calcularTotalIngresos() - calcularTotalGastos()
    }

    private fun validarMovimiento(movimiento: Movimiento) {
        require(movimiento.descripcion.isNotBlank()) {
            "La descripción no puede estar vacía"
        }

        require(movimiento.monto.isFinite() && movimiento.monto > 0) {
            "El monto debe ser un número válido mayor que cero"
        }
    }
}
