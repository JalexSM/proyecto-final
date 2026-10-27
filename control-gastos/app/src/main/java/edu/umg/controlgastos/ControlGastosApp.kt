package edu.umg.controlgastos

import android.app.Application
import edu.umg.controlgastos.datos.SQLiteMovimientoRepository
import edu.umg.programacion2.gastos.servicio.MovimientoService

class ControlGastosApp : Application() {
    val servicio: MovimientoService by lazy {
        MovimientoService(SQLiteMovimientoRepository(this))
    }
}