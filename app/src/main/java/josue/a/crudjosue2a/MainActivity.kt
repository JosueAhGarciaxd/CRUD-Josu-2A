package josue.a.crudjosue2a

import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.dataClassProductos
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1-Mandamos a llamar a todos los elementos de la pantalla
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtPrecio = findViewById<EditText>(R.id.txtPrecio)
        val txtCantidad = findViewById<EditText>(R.id.txtCantidad)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        fun limpiar(){
            txtNombre.setText("")
            txtPrecio.setText("")
            txtCantidad.setText("")
        }

        /////////////////////////////////////TODO: MOSTRAR DATOS ///////////////////////////////////
        val rcvProducto= findViewById<RecyclerView>(R.id.rcvProductos)

        //Asignar un layout al RecyclerView
        rcvProducto.layoutManager = LinearLayoutManager(this)

        //Funcion para obtener datos

        fun obtenerDatos(): List<dataClassProductos>{
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbProductos1")!!

            val productos = mutableListOf<dataClassProductos>()
            while (resultSet.next()){

                val uuid= resultSet.getString("uuid")
                val nombre = resultSet.getString("nombreProducto")
                val precio= resultSet.getInt("precio")
                val cantidad= resultSet.getInt("cantidad")
                val producto = dataClassProductos(uuid, nombre, precio, cantidad)
                productos.add(producto)
            }
            return productos
        }

        //asignar un adaptador
        CoroutineScope(Dispatchers.IO).launch {
            val productosDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val miAdapter = Adaptador(productosDB)
                rcvProducto.adapter = miAdapter
            }

        }

        /////////////////////////////todo: GUARDAD PRODUCTOS//////////////////////////////////////////

        //2- Programar el boton
        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){

                //Guardad datos
                //1-Crea un objeto de la clase Conexion
                val ClaseConexion = ClaseConexion().cadenaConexion()

                //2- Creo una variable que contenga un PrepareStatement
                val addProducto = ClaseConexion?.prepareStatement("insert into tbproductos1(uuid, nombreProducto, precio, cantidad) values(?,?,?,?)")!!
                addProducto.setString(1, UUID.randomUUID().toString())
                addProducto.setString(2, txtNombre.text.toString())
                addProducto.setInt(3, txtPrecio.text.toString().toInt())
                addProducto.setInt(4, txtCantidad.text.toString().toInt())
                addProducto.executeUpdate()

                val nuevosProductos = obtenerDatos()
                withContext(Dispatchers.Main){
                    (rcvProducto.adapter as? Adaptador)?.actuizarLista(nuevosProductos)
                }

            }
            ///limpiar()

        }

        ////SCREEN////
    }
}