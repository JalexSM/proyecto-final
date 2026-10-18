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


        // Obtener el servicio compartido de la aplicación
        val app = application as ControlGastosApp
        servicio = app.servicio

        val txtSaldo = findViewById<TextView>(R.id.txtSaldo)
        val txtIngresos = findViewById<TextView>(R.id.txtIngresos)
        val txtGastos = findViewById<TextView>(R.id.txtGastos)
        val txtResultado = findViewById<TextView>(R.id.txtResultado)
        val btnAgregarMovimiento = findViewById<Button>(R.id.btnAgregarGasto)

        btnAgregarMovimiento.setOnClickListener {
            val intent = Intent(this, RegistrarMovimientoActivity::class.java)
            startActivity(intent)
        }



        fun formatoMoneda(monto: Double): String {
            return String.format(Locale.US, "Q %.2f", monto)
        }

        fun actualizarResumen() {
            txtSaldo.text = formatoMoneda(servicio.calcularSaldo())
            txtIngresos.text = formatoMoneda(servicio.calcularTotalIngresos())
            txtGastos.text = formatoMoneda(servicio.calcularTotalGastos())

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

        actualizarResumen()

    }
}
