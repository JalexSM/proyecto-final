package edu.umg.controlgastos.datos

import android.content.ContentValues
import android.content.Context
import edu.umg.programacion2.gastos.datos.MovimientoDataSource
import edu.umg.programacion2.gastos.modelo.Categoria
import edu.umg.programacion2.gastos.modelo.Movimiento
import edu.umg.programacion2.gastos.modelo.TipoMovimiento

class SQLiteMovimientoRepository(
    context: Context
) : MovimientoDataSource {

    private val dbHelper = DatabaseHelper(context.applicationContext)

    override fun guardar(movimiento: Movimiento): Movimiento {
        val db = dbHelper.writableDatabase

        val valores = ContentValues().apply {
            put("descripcion", movimiento.descripcion)
            put("monto", movimiento.monto)
            put("tipo", movimiento.tipo.name)
            put("categoria", movimiento.categoria.name)
            put("fecha", movimiento.fecha)
        }

        val id = db.insertOrThrow("movimientos", null, valores)

        return movimiento.copy(id = id.toInt())
    }

    override fun listarTodos(): List<Movimiento> {
        val movimientos = mutableListOf<Movimiento>()
        val db = dbHelper.readableDatabase

        db.query(
            "movimientos",
            arrayOf(
                "id",
                "descripcion",
                "monto",
                "tipo",
                "categoria",
                "fecha"
            ),
            null,
            null,
            null,
            null,
            "id DESC"
        ).use { cursor ->

            while (cursor.moveToNext()) {
                val movimiento = Movimiento(
                    id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                    ),
                    descripcion = cursor.getString(
                        cursor.getColumnIndexOrThrow("descripcion")
                    ),
                    monto = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("monto")
                    ),
                    tipo = TipoMovimiento.valueOf(
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("tipo")
                        )
                    ),
                    categoria = Categoria.valueOf(
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("categoria")
                        )
                    ),
                    fecha = cursor.getString(
                        cursor.getColumnIndexOrThrow("fecha")
                    )
                )

                movimientos.add(movimiento)
            }
        }

        return movimientos
    }

    override fun buscarPorId(id: Int): Movimiento? {
        val db = dbHelper.readableDatabase

        db.query(
            "movimientos",
            arrayOf(
                "id",
                "descripcion",
                "monto",
                "tipo",
                "categoria",
                "fecha"
            ),
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use { cursor ->

            if (cursor.moveToFirst()) {
                return Movimiento(
                    id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                    ),
                    descripcion = cursor.getString(
                        cursor.getColumnIndexOrThrow("descripcion")
                    ),
                    monto = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("monto")
                    ),
                    tipo = TipoMovimiento.valueOf(
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("tipo")
                        )
                    ),
                    categoria = Categoria.valueOf(
                        cursor.getString(
                            cursor.getColumnIndexOrThrow("categoria")
                        )
                    ),
                    fecha = cursor.getString(
                        cursor.getColumnIndexOrThrow("fecha")
                    )
                )
            }
        }

        return null
    }

    override fun actualizar(
        movimientoActualizado: Movimiento
    ): Boolean {
        val db = dbHelper.writableDatabase

        val valores = ContentValues().apply {
            put("descripcion", movimientoActualizado.descripcion)
            put("monto", movimientoActualizado.monto)
            put("tipo", movimientoActualizado.tipo.name)
            put("categoria", movimientoActualizado.categoria.name)
            put("fecha", movimientoActualizado.fecha)
        }

        val filasActualizadas = db.update(
            "movimientos",
            valores,
            "id = ?",
            arrayOf(movimientoActualizado.id.toString())
        )

        return filasActualizadas > 0
    }

    override fun eliminar(id: Int): Boolean {
        val db = dbHelper.writableDatabase

        val filasEliminadas = db.delete(
            "movimientos",
            "id = ?",
            arrayOf(id.toString())
        )

        return filasEliminadas > 0
    }

    fun cerrar() {
        dbHelper.close()
    }
}