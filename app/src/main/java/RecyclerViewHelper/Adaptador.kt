package RecyclerViewHelper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import josue.a.crudjosue2a.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.ClaseConexion
import modelo.dataClassProductos

class Adaptador(private var Datos: List<dataClassProductos>) : RecyclerView.Adapter<ViewHolder>() {

    fun actuizarLista(nuevaLista: List<dataClassProductos>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    fun eliminarRegistro(nombreProductos: String, posicion: Int){


        //Quitar el elemento de la vista
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        //Quita de la base de datos
        GlobalScope.launch(Dispatchers.IO){

            //1- Crear un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            val delProducto = objConexion?.prepareStatement("delete tbproductos1 where nombreProducto = ?")!!
            delProducto.setString(1, nombreProductos)
            delProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }

        //Le decmos al adaptador que se elimina
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)

        return ViewHolder(vista)
    }
        override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = Datos[position]
        holder.textView.text = producto.nombreProductos

        val item = Datos[position]
        holder.imgBorrar.setOnClickListener {
            eliminarRegistro(item.nombreProductos, position   )
        }

    }


}