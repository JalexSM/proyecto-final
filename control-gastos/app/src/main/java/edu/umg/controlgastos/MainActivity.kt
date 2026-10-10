package edu.umg.controlgastos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.umg.programacion2.gastos.modelo.Categoria
import edu.umg.programacion2.gastos.modelo.Gasto
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // ==========================================
        // 1. CREAR UN GASTO DESDE GASTOS-CORE
        // ==========================================

        val gasto = Gasto(
            id = 1,
            descripcion = "Almuerzo",
            monto = 35.50,
            categoria = Categoria.COMIDA
        )

        // ==========================================
        // 2. OBTENER LOS ELEMENTOS DE LA INTERFAZ
        // ==========================================

        val txtSaldo = findViewById<TextView>(R.id.txtSaldo)
        val txtIngresos = findViewById<TextView>(R.id.txtIngresos)
        val txtGastos = findViewById<TextView>(R.id.txtGastos)
        val txtResultado = findViewById<TextView>(R.id.txtResultado)

        val btnAgregarGasto =
            findViewById<Button>(R.id.btnAgregarGasto)

        // ==========================================
        // 3. MOSTRAR LOS DATOS DEL GASTO
        // ==========================================

        val formatoMoneda = { monto: Double ->
            String.format(Locale.US, "Q %.2f", monto)
        }

        // Este ejemplo contiene un gasto, pero ningún ingreso.
        val ingresos = 0.0
        val totalGastos = gasto.monto
        val saldo = ingresos - totalGastos

        txtSaldo.text = formatoMoneda(saldo)
        txtIngresos.text = formatoMoneda(ingresos)
        txtGastos.text = formatoMoneda(totalGastos)

        txtResultado.text =
            "Conexión con gastos-core correcta\n\n" +
                    "Descripción: ${gasto.descripcion}\n" +
                    "Monto: ${formatoMoneda(gasto.monto)}\n" +
                    "Categoría: ${gasto.categoria}"

        // ==========================================
        // 4. BOTÓN PARA AGREGAR UN GASTO
        // ==========================================

        btnAgregarGasto.setOnClickListener {
            Toast.makeText(
                this,
                "En el siguiente paso crearemos el formulario",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ==========================================
        // 5. AJUSTAR LA PANTALLA A LAS BARRAS DEL SISTEMA
        // ==========================================

        val vistaPrincipal = findViewById<android.view.View>(R.id.main)

        val paddingIzquierdo = vistaPrincipal.paddingLeft
        val paddingSuperior = vistaPrincipal.paddingTop
        val paddingDerecho = vistaPrincipal.paddingRight
        val paddingInferior = vistaPrincipal.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(vistaPrincipal) { vista, insets ->

            val barrasSistema =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            vista.setPadding(
                paddingIzquierdo + barrasSistema.left,
                paddingSuperior + barrasSistema.top,
                paddingDerecho + barrasSistema.right,
                paddingInferior + barrasSistema.bottom
            )

            insets
        }
    }
}