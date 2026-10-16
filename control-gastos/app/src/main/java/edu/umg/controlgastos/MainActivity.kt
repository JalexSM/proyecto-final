package edu.umg.controlgastos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.umg.programacion2.gastos.datos.MovimientoRepository
import edu.umg.programacion2.gastos.modelo.Categoria
import edu.umg.programacion2.gastos.modelo.Movimiento
import edu.umg.programacion2.gastos.modelo.TipoMovimiento
import edu.umg.programacion2.gastos.servicio.MovimientoService
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var servicio: MovimientoService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = MovimientoRepository()
        servicio = MovimientoService(repository)

        val txtSaldo = findViewById<TextView>(R.id.txtSaldo)
        val txtIngresos = findViewById<TextView>(R.id.txtIngresos)
        val txtGastos = findViewById<TextView>(R.id.txtGastos)
        val txtResultado = findViewById<TextView>(R.id.txtResultado)
        val btnAgregarGasto = findViewById<Button>(R.id.btnAgregarGasto)

        // Registrar movimientos de prueba
        servicio.registrar(
            Movimiento(
                descripcion = "Salario",
                monto = 4000.00,
                tipo = TipoMovimiento.INGRESO,
                categoria = Categoria.SALARIO
            )
        )

        servicio.registrar(
            Movimiento(
                descripcion = "Almuerzo",
                monto = 35.50,
                tipo = TipoMovimiento.GASTO,
                categoria = Categoria.COMIDA
            )
        )

        fun formatoMoneda(monto: Double): String {
            return String.format(Locale.US, "Q %.2f", monto)
        }

        fun actualizarResumen() {
            txtSaldo.text = formatoMoneda(servicio.calcularSaldo())
            txtIngresos.text = formatoMoneda(servicio.calcularTotalIngresos())
            txtGastos.text = formatoMoneda(servicio.calcularTotalGastos())

            val movimientos = servicio.listarMovimientos()

            txtResultado.text = movimientos.joinToString("\n\n") { movimiento ->
                "ID: ${movimiento.id}\n" +
                        "${movimiento.descripcion}\n" +
                        "Tipo: ${movimiento.tipo}\n" +
                        "Categoría: ${movimiento.categoria}\n" +
                        "Monto: ${formatoMoneda(movimiento.monto)}"
            }
        }

        actualizarResumen()

        btnAgregarGasto.setOnClickListener {
            val intent = android.content.Intent(
                this,
                RegistrarMovimientoActivity::class.java
            )
            startActivity(intent)
        }
    }
}
