package br.edu.ifsp.scl.sdm.petbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.scl.sdm.petbook.R
import br.edu.ifsp.scl.sdm.petbook.adapter.ConsultaAdapter
import br.edu.ifsp.scl.sdm.petbook.databinding.FragmentListaConsultasBinding
import br.edu.ifsp.scl.sdm.petbook.domain.Consulta
import br.edu.ifsp.scl.sdm.petbook.viewmodel.ConsultaViewModel
import br.edu.ifsp.scl.sdm.petbook.viewmodel.ListaState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ListaConsultasFragment : Fragment() {
    private var _binding: FragmentListaConsultasBinding? = null
    private val binding get() = _binding!!
    lateinit var consultaAdapter: ConsultaAdapter
    val viewModel: ConsultaViewModel by viewModels { ConsultaViewModel.consultaViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListaConsultasBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_listaConsultasFragment_to_cadastroFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllAppointments()
        setupViewModel()

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.filtraConsultas -> {
                        filterConsultas("Consulta")
                        true
                    }
                    R.id.filtraVacinas -> {
                        filterConsultas("Vacinação")
                        true
                    }
                    R.id.filtraCheckups -> {
                        filterConsultas("Check-up")
                        true
                    }
                    R.id.filtraCirurgias -> {
                        filterConsultas("Cirurgia")
                        true
                    }
                    R.id.filtraEmergencia -> {
                        filterConsultas("Emergência")
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun filterConsultas(tipo: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateList.collect { state ->
                when (state) {
                    is ListaState.SearchAllSuccess -> {
                        val filteredConsultas = when (tipo) {
                            "Consulta" -> state.consultas.filter { it.tipo == "Consulta" }
                            "Vacinação" -> state.consultas.filter { it.tipo == "Vacinação" }
                            "Check-up" -> state.consultas.filter { it.tipo == "Check-up" }
                            "Cirurgia" -> state.consultas.filter { it.tipo == "Cirurgia" }
                            "Emergência" -> state.consultas.filter { it.tipo == "Emergência" }
                            else -> state.consultas // Se "consultas" ou outro valor for passado, mostra todas
                        }
                        setupRecyclerView(filteredConsultas)
                    }
                    else -> {}
                }
            }
        }
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

    private fun setupRecyclerView(consultasList: List<Consulta>) {
        consultaAdapter = ConsultaAdapter().apply {
            updateList(consultasList)
        }

        binding.recyclerview.adapter = consultaAdapter

        consultaAdapter.onIntemClick = { consulta ->
            val bundle = Bundle()
            bundle.putInt("idConsulta", consulta.id)
            findNavController().navigate(
                R.id.action_listaConsultasFragment_to_detalheFragment,
                bundle
            )
        }
    }
}
