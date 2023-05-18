package com.example.sprint.Tgl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.sprint.R
import com.example.sprint.databinding.ListTglBinding
import com.example.sprint.db.TglIdentification

class TglRecyclerViewAdapter(
): RecyclerView.Adapter<TglRecyclerViewAdapter.TglViewHolder>() {

    private val tglList = ArrayList<TglIdentification>()

    class TglViewHolder(private val view: View):RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.tvTglName)
        val state: TextView = view.findViewById(R.id.tvTglState)
        val edit: Button =  view.findViewById(R.id.btnEditTgl)
        val test : Button = view.findViewById(R.id.btnTestTGL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):TglViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.list_tgl,parent,false)
        return TglViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TglRecyclerViewAdapter.TglViewHolder, position: Int) {
       val update =  tglList[position]
        holder.name.text = update.fullName
        holder.state.text = update.state

        holder.edit.setOnClickListener {
            val action = tglTestFragmentDirections.actionTglTestFragmentToTglUpdateFragment2(update)
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return tglList.size
    }

    fun setList(tgls: List<TglIdentification>){
        tglList.clear()
        tglList.addAll(tgls)
    }

}

