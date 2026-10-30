package edu.umg.controlgastos

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.umg.controlgastos.datos.PerfilRepository
import edu.umg.controlgastos.modelo.PerfilUsuario

class PerfilActivity : AppCompatActivity() {

    private lateinit var repository: PerfilRepository

    private lateinit var etNombre: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etOcupacion: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        repository = PerfilRepository(this)

        etNombre = findViewById(R.id.etNombre)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
        etOcupacion = findViewById(R.id.etOcupacion)

        val btnGuardar = findViewById<Button>(R.id.btnGuardarPerfil)
        val btnVolver = findViewById<Button>(R.id.btnVolverPerfil)

        cargarPerfil()

        btnGuardar.setOnClickListener {
            guardarPerfil()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cargarPerfil() {
        val perfil = repository.obtenerPerfil()

        if (perfil != null) {
            etNombre.setText(perfil.nombre)
            etCorreo.setText(perfil.correo)
            etTelefono.setText(perfil.telefono)
            etOcupacion.setText(perfil.ocupacion)
        }
    }

    private fun guardarPerfil() {
        val nombre = etNombre.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val ocupacion = etOcupacion.text.toString().trim()

        if (nombre.isEmpty()) {
            etNombre.error = "Ingresa tu nombre"
            etNombre.requestFocus()
            return
        }

        if (correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.error = "Ingresa un correo válido"
            etCorreo.requestFocus()
            return
        }

        if (telefono.isEmpty()) {
            etTelefono.error = "Ingresa tu teléfono"
            etTelefono.requestFocus()
            return
        }

        if (ocupacion.isEmpty()) {
            etOcupacion.error = "Ingresa tu ocupación"
            etOcupacion.requestFocus()
            return
        }

        val perfil = PerfilUsuario(
            nombre = nombre,
            correo = correo,
            telefono = telefono,
            ocupacion = ocupacion
        )

        val guardado = repository.guardarPerfil(perfil)

        if (guardado) {
            Toast.makeText(
                this,
                "Datos personales guardados correctamente",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                "No se pudieron guardar los datos",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        repository.cerrar()
    }
}