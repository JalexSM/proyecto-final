package edu.umg.controlgastos.datos

import android.content.ContentValues
import android.content.Context
import edu.umg.controlgastos.modelo.PerfilUsuario

class PerfilRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    /**
     * Guarda un perfil nuevo o actualiza el existente.
     */
    fun guardarPerfil(perfil: PerfilUsuario): Boolean {
        val db = dbHelper.writableDatabase

        val valores = ContentValues().apply {
            put("id", 1)
            put("nombre", perfil.nombre)
            put("correo", perfil.correo)
            put("telefono", perfil.telefono)
            put("ocupacion", perfil.ocupacion)
        }

        val resultado = db.insertWithOnConflict(
            "perfil_usuario",
            null,
            valores,
            android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
        )

        return resultado != -1L
    }

    /**
     * Recupera el perfil guardado.
     * Devuelve null si todavía no existe uno.
     */
    fun obtenerPerfil(): PerfilUsuario? {
        val db = dbHelper.readableDatabase

        db.query(
            "perfil_usuario",
            arrayOf("nombre", "correo", "telefono", "ocupacion"),
            "id = ?",
            arrayOf("1"),
            null,
            null,
            null
        ).use { cursor ->

            if (cursor.moveToFirst()) {
                return PerfilUsuario(
                    nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow("nombre")
                    ),
                    correo = cursor.getString(
                        cursor.getColumnIndexOrThrow("correo")
                    ),
                    telefono = cursor.getString(
                        cursor.getColumnIndexOrThrow("telefono")
                    ),
                    ocupacion = cursor.getString(
                        cursor.getColumnIndexOrThrow("ocupacion")
                    )
                )
            }
        }

        return null
    }

    fun cerrar() {
        dbHelper.close()
    }
}