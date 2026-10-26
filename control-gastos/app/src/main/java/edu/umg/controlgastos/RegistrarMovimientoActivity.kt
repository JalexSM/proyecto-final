package edu.umg.controlgastos


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Bundle
import android.widget.Button
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.umg.programacion2.gastos.modelo.Categoria
import edu.umg.programacion2.gastos.modelo.Movimiento
import edu.umg.programacion2.gastos.modelo.TipoMovimiento
import edu.umg.programacion2.gastos.servicio.MovimientoService


class RegistrarMovimientoActivity : AppCompatActivity() {
    private lateinit var rgTipoMovimiento: RadioGroup
    private lateinit var etDescripcion: EditText
    private lateinit var etMonto: EditText
    private lateinit var spCategoria: Spinner
    private lateinit var btnGuardarMovimiento: Button
    private lateinit var btnCancelar: Button
    private lateinit var servicio: MovimientoService

    private var movimientoId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_movimiento)
        servicio = (application as ControlGastosApp).servicio
        servicio = (application as ControlGastosApp).servicio


        // 1. Conectar las variables con los elementos del XML
        rgTipoMovimiento = findViewById(R.id.rgTipoMovimiento)
        etDescripcion = findViewById(R.id.etDescripcion)
        etMonto = findViewById(R.id.etMonto)
        spCategoria = findViewById(R.id.spCategoria)
        btnGuardarMovimiento = findViewById(R.id.btnGuardarMovimiento)
        btnCancelar = findViewById(R.id.btnCancelar)



        // 2. Cargar las categorías iniciales
        cargarCategorias(TipoMovimiento.GASTO)

        // 3. Cambiar las categorías según el tipo seleccionado
        rgTipoMovimiento.setOnCheckedChangeListener { _, checkedId ->
            val tipo = if (checkedId == R.id.rbIngreso) {
                TipoMovimiento.INGRESO
            } else {
                TipoMovimiento.GASTO
            }

            cargarCategorias(tipo)
        }
        movimientoId = intent.getIntExtra("movimiento_id", -1)
            .takeIf { it > 0 }

        if (movimientoId != null) {
            cargarMovimientoParaEditar()
        }



        // 4. Validar los datos al pulsar Guardar
        btnGuardarMovimiento.setOnClickListener {
            guardarMovimiento()
        }

        // 5. Regresar a la pantalla anterior
        btnCancelar.setOnClickListener {
            finish()
        }

    }

    private fun cargarCategorias(tipo: TipoMovimiento) {
        val categorias = when (tipo) {
            TipoMovimiento.INGRESO -> listOf(
                Categoria.SALARIO,
                Categoria.VENTAS,
                Categoria.OTROS_INGRESOS
            )

            TipoMovimiento.GASTO -> listOf(
                Categoria.COMIDA,
                Categoria.TRANSPORTE,
                Categoria.ENTRETENIMIENTO,
                Categoria.SERVICIOS,
                Categoria.ESTUDIOS,
                Categoria.OTROS_GASTOS
            )
        }

        val nombres = categorias.map { categoria ->
            categoria.name
                .replace("_", " ")
                .lowercase()
                .replaceFirstChar { it.uppercase() }
        }

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nombres
        )

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spCategoria.adapter = adaptador
    }


    private fun cargarMovimientoParaEditar() {
        val id = movimientoId ?: return
        val movimiento = servicio.buscarPorId(id)

        if (movimiento == null) {
            Toast.makeText(
                this,
                "No se encontró el movimiento",
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }

        etDescripcion.setText(movimiento.descripcion)
        etMonto.setText(movimiento.monto.toString())

        // Seleccionar ingreso o gasto
        rgTipoMovimiento.check(
            if (movimiento.tipo == TipoMovimiento.INGRESO) {
                R.id.rbIngreso
            } else {
                R.id.rbGasto
            }
        )

        // Cargar las categorías correspondientes al tipo
        cargarCategorias(movimiento.tipo)

        // Seleccionar la categoría guardada
        val categorias = when (movimiento.tipo) {
            TipoMovimiento.INGRESO -> listOf(
                Categoria.SALARIO,
                Categoria.VENTAS,
                Categoria.OTROS_INGRESOS
            )

            TipoMovimiento.GASTO -> listOf(
                Categoria.COMIDA,
                Categoria.TRANSPORTE,
                Categoria.ENTRETENIMIENTO,
                Categoria.SERVICIOS,
                Categoria.ESTUDIOS,
                Categoria.OTROS_GASTOS
            )
        }

        val posicion = categorias.indexOf(movimiento.categoria)

        if (posicion >= 0) {
            spCategoria.setSelection(posicion)
        }

        btnGuardarMovimiento.text = "Actualizar movimiento"
    }


    private fun guardarMovimiento() {
        val descripcion = etDescripcion.text.toString().trim()
        val montoTexto = etMonto.text.toString().trim()
        val monto = montoTexto.toDoubleOrNull()

        if (descripcion.isEmpty()) {
            etDescripcion.error = "Escribe una descripción"
            etDescripcion.requestFocus()
            return
        }

        if (monto == null || !monto.isFinite() || monto <= 0) {
            etMonto.error = "Ingresa un monto válido mayor que cero"
            etMonto.requestFocus()
            return
        }

        val tipo = if (rgTipoMovimiento.checkedRadioButtonId == R.id.rbIngreso) {
            TipoMovimiento.INGRESO
        } else {
            TipoMovimiento.GASTO
        }

        val categorias = when (tipo) {
            TipoMovimiento.INGRESO -> listOf(
                Categoria.SALARIO,
                Categoria.VENTAS,
                Categoria.OTROS_INGRESOS
            )

            TipoMovimiento.GASTO -> listOf(
                Categoria.COMIDA,
                Categoria.TRANSPORTE,
                Categoria.ENTRETENIMIENTO,
                Categoria.SERVICIOS,
                Categoria.ESTUDIOS,
                Categoria.OTROS_GASTOS
            )
        }


        val categoria = categorias[spCategoria.selectedItemPosition]



        try {
            if (movimientoId == null) {
                // Registrar un movimiento nuevo
                servicio.registrar(
                    Movimiento(
                        descripcion = descripcion,
                        monto = monto,
                        tipo = tipo,
                        categoria = categoria,
                        fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    )
                )

                Toast.makeText(
                    this,
                    "Movimiento registrado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                val movimientoOriginal = servicio.buscarPorId(movimientoId!!)

                if (movimientoOriginal == null) {
                    Toast.makeText(
                        this,
                        "No se encontró el movimiento",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

                // Actualizar un movimiento existente
                val actualizado = servicio.actualizar(
                    Movimiento(
                        id = movimientoId!!,
                        descripcion = descripcion,
                        monto = monto,
                        tipo = tipo,
                        categoria = categoria,
                        fecha = movimientoOriginal.fecha

                    )
                )

                if (!actualizado) {
                    Toast.makeText(
                        this,
                        "No se pudo actualizar el movimiento",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

                Toast.makeText(
                    this,
                    "Movimiento actualizado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            }

            finish()

        } catch (e: IllegalArgumentException) {
            Toast.makeText(
                this,
                e.message ?: "No se pudo guardar el movimiento",
                Toast.LENGTH_LONG
            ).show()
        }

    }


    }
