package br.edu.ifsp.scl.sdm.petbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import br.edu.ifsp.scl.sdm.petbook.ConsultaApplication
import br.edu.ifsp.scl.sdm.petbook.domain.Consulta
import br.edu.ifsp.scl.sdm.petbook.repository.ConsultaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ConsultaState {
    data object InsertSuccess : ConsultaState()
    data object ShowLoading : ConsultaState()
}


class ConsultaViewModel (private val repository: ConsultaRepository) : ViewModel(){
    private val _stateCadastro = MutableStateFlow<ConsultaState>(ConsultaState.ShowLoading)
    val stateCadastro = _stateCadastro.asStateFlow()
    fun insert(consultaEntity: Consulta) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(consultaEntity)
        _stateCadastro.value=ConsultaState.InsertSuccess
    }
    companion object {
        fun consultaViewModelFactory() : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val application = checkNotNull(
                        extras[APPLICATION_KEY]
                    )
                    return ConsultaViewModel(
                        (application as ConsultaApplication).repository
                    ) as T
                }
            }
    }
}