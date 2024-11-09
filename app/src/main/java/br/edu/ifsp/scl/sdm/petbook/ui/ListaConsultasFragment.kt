package br.edu.ifsp.scl.sdm.petbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.scl.sdm.petbook.R
import br.edu.ifsp.scl.sdm.petbook.adapter.ConsultaAdapter
import br.edu.ifsp.scl.sdm.petbook.databinding.FragmentListaConsultasBinding
import br.edu.ifsp.scl.sdm.petbook.domain.Consulta
import br.edu.ifsp.scl.sdm.petbook.viewmodel.ConsultaViewModel
import br.edu.ifsp.scl.sdm.petbook.viewmodel.ListaState
import kotlinx.coroutines.launch

class ListaConsultasFragment : Fragment() {
    private var _binding: FragmentListaConsultasBinding? = null
    private val binding get() = _binding!!
    lateinit var consultaAdapter: ConsultaAdapter
    val viewModel : ConsultaViewModel by viewModels { ConsultaViewModel.consultaViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaConsultasBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_listaConsultasFragment_to_cadastroFragment) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllContacts()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateList.collect {
                when (it) {
                    is ListaState.SearchAllSuccess -> {
                        setupRecyclerView(it.consultas)
                    }
                    ListaState.ShowLoading -> {}
                    ListaState.EmptyState -> {binding.textEmptyList.visibility = View.VISIBLE}
                }
            }
        }
    }
    private fun setupRecyclerView(consultastList: List<Consulta>)
    {
        consultaAdapter = ConsultaAdapter().apply { updateList(consultastList) }
        binding.recyclerview.adapter = consultaAdapter

        consultaAdapter.onIntemClick = {
            val bundle = Bundle()
            bundle.putInt("idConsulta", it.id)
            findNavController().navigate(
                R.id.action_listaConsultasFragment_to_detalheFragment,
                bundle)
        }
    }
}