
package edu.umg.controlgastos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import edu.umg.programacion2.gastos.servicio.MovimientoService
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var servicio: MovimientoService
    private lateinit var txtSaldo: TextView
    private lateinit var txtIngresos: TextView
    private lateinit var txtGastos: TextView
    private lateinit var txtResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        servicio = (application as ControlGastosApp).servicio

        txtSaldo = findViewById(R.id.txtSaldo)
        txtIngresos = findViewById(R.id.txtIngresos)
        txtGastos = findViewById(R.id.txtGastos)
        txtResultado = findViewById(R.id.txtResultado)

        val btnAgregarMovimiento =
            findViewById<Button>(R.id.btnAgregarGasto)

        btnAgregarMovimiento.setOnClickListener {
            val intent = Intent(
                this,
                RegistrarMovimientoActivity::class.java
            )
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarResumen()
    }

    private fun actualizarResumen() {
        txtSaldo.text = formatoMoneda(servicio.calcularSaldo())
        txtIngresos.text =
            formatoMoneda(servicio.calcularTotalIngresos())
        txtGastos.text =
            formatoMoneda(servicio.calcularTotalGastos())

        val movimientos = servicio.listarMovimientos()

        txtResultado.text = if (movimientos.isEmpty()) {
            "Todavía no hay movimientos registrados."
        } else {
            movimientos.joinToString("\n\n") { movimiento ->
                "ID: ${movimiento.id}\n" +
                        "${movimiento.descripcion}\n" +
                        "Tipo: ${movimiento.tipo}\n" +
                        "Categoría: ${movimiento.categoria}\n" +
                        "Monto: ${formatoMoneda(movimiento.monto)}"
            }
        }
    }

    private fun formatoMoneda(monto: Double): String {
        return String.format(Locale.US, "Q %.2f", monto)
    }
}
