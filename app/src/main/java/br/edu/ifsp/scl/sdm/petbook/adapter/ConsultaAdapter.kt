package br.edu.ifsp.scl.sdm.petbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.sdm.petbook.databinding.ConsultaCelulaBinding
import br.edu.ifsp.scl.sdm.petbook.domain.Consulta

class ConsultaAdapter: RecyclerView.Adapter<ConsultaAdapter.ConsultaViewHolder>() {
    var consultasLista = ArrayList<Consulta>()
    private lateinit var binding: ConsultaCelulaBinding
    fun updateList(newList: List<Consulta> ){
        consultasLista = newList as ArrayList<Consulta>
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConsultaViewHolder {
        binding = ConsultaCelulaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConsultaViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ConsultaViewHolder, position: Int) {
        holder.tipoVH.text = consultasLista[position].tipo
        holder.dataVH.text = consultasLista[position].data
    }
    override fun getItemCount(): Int {
        return consultasLista.size
    }
    inner class ConsultaViewHolder(view: ConsultaCelulaBinding): RecyclerView.ViewHolder(view.root)
    {
        val tipoVH = view.tipo
        val dataVH = view.data
    }
}