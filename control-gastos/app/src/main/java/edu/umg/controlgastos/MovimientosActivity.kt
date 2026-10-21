package edu.umg.controlgastos

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import edu.umg.programacion2.gastos.modelo.Movimiento
import edu.umg.programacion2.gastos.modelo.TipoMovimiento
import edu.umg.programacion2.gastos.servicio.MovimientoService
import java.util.Locale

class MovimientosActivity : AppCompatActivity() {

    private lateinit var servicio: MovimientoService
    private lateinit var contenedorIngresos: LinearLayout
    private lateinit var contenedorGastos: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimientos)



        servicio = (application as ControlGastosApp).servicio

        contenedorIngresos = findViewById(R.id.contenedorIngresos)
        contenedorGastos = findViewById(R.id.contenedorGastos)

        findViewById<android.widget.Button>(R.id.btnRegresar)
            .setOnClickListener {
                finish()
            }

        mostrarMovimientos()
    }

    override fun onResume() {
        super.onResume()

        if (::contenedorIngresos.isInitialized) {
            mostrarMovimientos()
        }
    }

    private fun mostrarMovimientos() {
        contenedorIngresos.removeAllViews()
        contenedorGastos.removeAllViews()

        val movimientos = servicio.listarMovimientos()

        val ingresos = movimientos.filter {
            it.tipo == TipoMovimiento.INGRESO
        }

        val gastos = movimientos.filter {
            it.tipo == TipoMovimiento.GASTO
        }

        mostrarVacioSiNecesario(
            contenedorIngresos,
            ingresos,
            "No hay ingresos"
        )

        mostrarVacioSiNecesario(
            contenedorGastos,
            gastos,
            "No hay gastos"
        )

        ingresos.forEach { agregarTarjeta(it, contenedorIngresos) }
        gastos.forEach { agregarTarjeta(it, contenedorGastos) }
    }

    private fun mostrarVacioSiNecesario(
        contenedor: LinearLayout,
        movimientos: List<Movimiento>,
        mensaje: String
    ) {
        if (movimientos.isEmpty()) {
            val texto = TextView(this).apply {
                text = mensaje
                setTextColor(0xFFA9B6CA.toInt())
                textSize = 13f
                setPadding(4, 8, 4, 8)
            }

            contenedor.addView(texto)
        }
    }

    private fun agregarTarjeta(
        movimiento: Movimiento,
        contenedor: LinearLayout
    ) {
        val tarjeta = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(12, 14, 12, 14)
            setBackgroundColor(0xFF1B293D.toInt())
        }

        val descripcion = TextView(this).apply {
            text = movimiento.descripcion
            setTextColor(0xFFFFFFFF.toInt())
            textSize = 14f
        }

        val monto = TextView(this).apply {
            val signo = if (
                movimiento.tipo == TipoMovimiento.INGRESO
            ) "+" else "−"

            text = "$signo ${formatoMoneda(movimiento.monto)}"
            setTextColor(
                if (movimiento.tipo == TipoMovimiento.INGRESO)
                    0xFF91E4B8.toInt()
                else
                    0xFFFFB5B5.toInt()
            )
            textSize = 15f
            setPadding(0, 8, 0, 0)
        }

        val categoria = TextView(this).apply {
            text = movimiento.categoria.name.replace("_", " ")
            setTextColor(0xFFA9B6CA.toInt())
            textSize = 12f
            setPadding(0, 6, 0, 0)
        }

        tarjeta.addView(descripcion)
        tarjeta.addView(monto)
        tarjeta.addView(categoria)

        val parametros = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        parametros.bottomMargin = dp(10)

        contenedor.addView(tarjeta, parametros)
        tarjeta.setOnClickListener {
            mostrarOpciones(movimiento)
        }
    }




    private fun mostrarOpciones(movimiento: Movimiento) {
        val opciones = arrayOf("Editar", "Eliminar")

        AlertDialog.Builder(this)
            .setTitle(movimiento.descripcion)
            .setItems(opciones) { _, seleccion ->
                when (seleccion) {
                    0 -> editarMovimiento(movimiento)
                    1 -> confirmarEliminacion(movimiento)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun editarMovimiento(movimiento: Movimiento) {
        val intent = Intent(
            this,
            RegistrarMovimientoActivity::class.java
        )

        intent.putExtra("movimiento_id", movimiento.id)
        startActivity(intent)
    }

    private fun confirmarEliminacion(movimiento: Movimiento) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar movimiento")
            .setMessage(
                "¿Seguro que deseas eliminar \"${movimiento.descripcion}\"?"
            )
            .setPositiveButton("Eliminar") { _, _ ->
                val eliminado = servicio.eliminar(movimiento.id)

                if (eliminado) {
                    Toast.makeText(
                        this,
                        "Movimiento eliminado",
                        Toast.LENGTH_SHORT
                    ).show()

                    mostrarMovimientos()
                } else {
                    Toast.makeText(
                        this,
                        "No se pudo eliminar el movimiento",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun formatoMoneda(monto: Double): String {
        return String.format(Locale.US, "Q %.2f", monto)
    }
    private fun dp(valor: Int): Int {
        return (valor * resources.displayMetrics.density).toInt()
    }
}
