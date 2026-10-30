package edu.umg.controlgastos.datos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE movimientos (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                descripcion TEXT NOT NULL,
                monto REAL NOT NULL,
                tipo TEXT NOT NULL,
                categoria TEXT NOT NULL,
                fecha TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Actualización de la versión 1 a la versión 2.
        if (oldVersion < 2) {
            crearTablaPerfil(db)
        }
    }

    private fun crearTablaPerfil(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS perfil_usuario (
                id INTEGER PRIMARY KEY CHECK (id = 1),
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL,
                telefono TEXT NOT NULL,
                ocupacion TEXT NOT NULL
            )
            """.trimIndent()
        )
    }


    companion object {
        private const val DATABASE_NAME = "control_gastos.db"
        private const val DATABASE_VERSION = 2
    }
}