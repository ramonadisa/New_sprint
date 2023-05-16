package com.example.sprint

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sprint.databinding.ListTglBinding
import com.example.sprint.db.TglIdentification

class TglRecyclerViewAdapter(
    private val clickListener: (TglIdentification) ->Unit
): RecyclerView.Adapter<TglViewHolder>() {

    private val tglList = ArrayList<TglIdentification>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TglViewHolder {
        val binding = ListTglBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TglViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TglViewHolder, position: Int) {
        holder.bind(tglList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return tglList.size
    }

    fun setList(tgls:List<TglIdentification>){
        tglList.clear()
        tglList.addAll(tgls)
    }

}

class TglViewHolder(private val binding: ListTglBinding):RecyclerView.ViewHolder(binding.root){
    fun bind(tgl:TglIdentification,clickListener: (TglIdentification) -> Unit){
        binding.apply {
            tvTglName.text = tgl.fullName
            tvTglNo.text = tgl.phoneNumber
        }
    }
}