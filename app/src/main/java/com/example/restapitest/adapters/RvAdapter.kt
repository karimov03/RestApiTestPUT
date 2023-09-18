import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restapitest.databinding.ItemRvBinding
import com.example.restapitest.models.Todo

class RvAdapter (val list: ArrayList<Todo>,val rvaction: rvAction):RecyclerView.Adapter<RvAdapter.vh>(){
    inner class vh(val itemRvBinding: ItemRvBinding):RecyclerView.ViewHolder(itemRvBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vh {
        return vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: vh, position: Int) {
        holder.itemRvBinding.tvName.text=list[position].name
        holder.itemRvBinding.tvNumber.text=list[position].phone
        holder.itemRvBinding.root.setOnClickListener {
            rvaction.OnClick(list,position)
        }

    }
    interface rvAction{
        fun OnClick(list: ArrayList<Todo>, position: Int)
    }

}