package br.edu.ifsp.scl.sdm.petbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.scl.sdm.petbook.R
import br.edu.ifsp.scl.sdm.petbook.databinding.FragmentDetalheBinding
import br.edu.ifsp.scl.sdm.petbook.domain.Consulta
import br.edu.ifsp.scl.sdm.petbook.viewmodel.ConsultaViewModel
import br.edu.ifsp.scl.sdm.petbook.viewmodel.DetalheState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


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
                    DetalheState.DeleteSuccess -> {
                        Snackbar.make(
                            binding.root,
                            "Consulta removida",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }

                    DetalheState.UpdateSuccess -> {
                        Snackbar.make(
                            binding.root,
                            "Consulta atualizada!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }

                    DetalheState.ShowLoading -> {}

                    is DetalheState.GetByIdSuccess -> {
                        fillFields(it.c)
                    }
                }
            }
        }


    val menuHost: MenuHost = requireActivity()

    menuHost.addMenuProvider(object: MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.detalhe_menu, menu)
        }
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_alterarConsulta -> {
                    val nome = nomeEditText.text.toString()
                    val clinica = clinicaEditText.text.toString()
                    val data = dataEditText.text.toString()
                    val tipo = atendimentoSpinner.selectedItem.toString()
                    val descricao = descricaoEditText.text.toString()

                    // Validação da data
                    if (isValidDate(data)) {
                        consulta.nome = nome
                        consulta.clinica = clinica
                        consulta.data = data
                        consulta.tipo = tipo
                        consulta.descricao = descricao
                        viewModel.update(consulta)
                        true
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Por favor, insira uma data válida no formato dd/MM/yyyy.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        false
                    }
                }
                R.id.action_excluirConsulta ->{
                    viewModel.delete(consulta)
                    true
                }
                else -> false
            }
        }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
}

    private fun fillFields(c: Consulta) {
        consulta = c
        nomeEditText.setText(consulta.nome)
        clinicaEditText.setText(consulta.clinica)

        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        dataEditText.setText(consulta.data)
        atendimentoSpinner.setSelection(
            resources.getStringArray(R.array.tipos_consultas).indexOf(consulta.tipo)
        )
        descricaoEditText.setText(consulta.descricao)
    }

    fun isValidDate(date: String): Boolean {
        val regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$".toRegex()
        return regex.matches(date)
    }
}
