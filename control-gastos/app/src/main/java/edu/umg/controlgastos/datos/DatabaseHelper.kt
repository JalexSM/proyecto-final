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
        // Aquí agregaremos migraciones cuando cambie la estructura.
        // No eliminamos la tabla para evitar perder los datos.
    }

    companion object {
        private const val DATABASE_NAME = "control_gastos.db"
        private const val DATABASE_VERSION = 1
    }
}