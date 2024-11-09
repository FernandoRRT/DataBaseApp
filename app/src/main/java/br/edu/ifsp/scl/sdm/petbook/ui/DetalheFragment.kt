package br.edu.ifsp.scl.sdm.petbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.edu.ifsp.scl.sdm.petbook.R
import br.edu.ifsp.scl.sdm.petbook.databinding.FragmentDetalheBinding
import br.edu.ifsp.scl.sdm.petbook.domain.Consulta
import br.edu.ifsp.scl.sdm.petbook.viewmodel.ConsultaViewModel
import br.edu.ifsp.scl.sdm.petbook.viewmodel.DetalheState
import kotlinx.coroutines.launch


class DetalheFragment : Fragment() {
    private var _binding: FragmentDetalheBinding? = null
    private val binding get() = _binding!!
    lateinit var consulta: Consulta
    lateinit var nomeEditText: EditText
    lateinit var clinicaEditText: EditText
    lateinit var atendimentoSpinner: Spinner
    lateinit var dataEditText: EditText
    lateinit var descricaoEditText: EditText

    val viewModel : ConsultaViewModel by viewModels { ConsultaViewModel.consultaViewModelFactory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetalheBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nomeEditText = binding.commonLayout.etNome
        clinicaEditText = binding.commonLayout.etClinica
        dataEditText = binding.commonLayout.etData
        descricaoEditText = binding.commonLayout.etDescricao
        atendimentoSpinner = binding.commonLayout.spAtendimento

        val idConsulta = requireArguments().getInt("idConsulta")
        viewModel.getContactById(idConsulta)
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.stateDetail.collect {
                when (it) {
                    DetalheState.ShowLoading -> {}
                    is DetalheState.GetByIdSuccess -> {
                        fillFields(it.c)
                    }
                }
            }
        }
    }
    private fun fillFields(c: Consulta) {
        consulta=c
        nomeEditText.setText(consulta.nome)
        clinicaEditText.setText(consulta.clinica)
        dataEditText.setText(consulta.data)
        atendimentoSpinner.setSelection(
            resources.getStringArray(R.array.tipos_consultas).indexOf(consulta.tipo)
        )
        descricaoEditText.setText(consulta.descricao)
    }
}
