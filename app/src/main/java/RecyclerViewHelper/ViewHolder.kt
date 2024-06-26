package RecyclerViewHelper

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import josue.a.crudjosue2a.R

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.txtProductoCard)
    val imgEditar: ImageView = view.findViewById(R.id.igmEditar)
    val imgBorrar: ImageView = view.findViewById(R.id.imgBorrar)
}