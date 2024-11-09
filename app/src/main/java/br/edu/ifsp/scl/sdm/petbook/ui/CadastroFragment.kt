package br.edu.ifsp.scl.sdm.petbook.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.scl.sdm.petbook.R
import br.edu.ifsp.scl.sdm.petbook.databinding.FragmentCadastroBinding
import br.edu.ifsp.scl.sdm.petbook.domain.Consulta
import br.edu.ifsp.scl.sdm.petbook.viewmodel.ConsultaState
import br.edu.ifsp.scl.sdm.petbook.viewmodel.ConsultaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class CadastroFragment : Fragment() {
    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!

    val viewModel : ConsultaViewModel by viewModels { ConsultaViewModel.consultaViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner: Spinner = binding.commonLayout.spAtendimento
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tipos_consultas,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            spinner.adapter = adapter
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateCadastro.collect {
                when (it) {
                    ConsultaState.InsertSuccess -> {
                        Snackbar.make(
                            binding.root,
                            "Contato inserido com sucesso",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                    ConsultaState.ShowLoading -> {}
                }
            }
        }
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.cadastro_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_salvarConsulta -> {
                        val nome = binding.commonLayout.etNome.text.toString()
                        val clinica = binding.commonLayout.etClinica.text.toString()
                        val tipo = binding.commonLayout.spAtendimento.selectedItem.toString()
                        val data = binding.commonLayout.etData.text.toString()
                        val descricao = binding.commonLayout.etDescricao.text.toString()

                        val consulta = Consulta(nome=nome, clinica=clinica, tipo=tipo, data=data, descricao=descricao)
                        viewModel.insert(consulta)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}