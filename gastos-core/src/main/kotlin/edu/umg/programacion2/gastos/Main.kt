package edu.umg.programacion2.gastos

import edu.umg.programacion2.gastos.datos.MovimientoRepository
import edu.umg.programacion2.gastos.modelo.Categoria
import edu.umg.programacion2.gastos.modelo.Movimiento
import edu.umg.programacion2.gastos.modelo.TipoMovimiento
import edu.umg.programacion2.gastos.servicio.MovimientoService

fun main() {
    val repository = MovimientoRepository()
    val servicio = MovimientoService(repository)


    println("=== MOVIMIENTOS REGISTRADOS ===")



    println("\n=== RESUMEN FINANCIERO ===")
    println("Total de ingresos: Q${servicio.calcularTotalIngresos()}")
    println("Total de gastos: Q${servicio.calcularTotalGastos()}")
    println("Saldo disponible: Q${servicio.calcularSaldo()}")
}
