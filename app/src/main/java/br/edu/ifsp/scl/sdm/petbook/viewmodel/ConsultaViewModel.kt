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
sealed class ListaState {
    data class SearchAllSuccess(val consultas: List<Consulta>) : ListaState()
    data object ShowLoading : ListaState()
    data object EmptyState : ListaState()
}
sealed class DetalheState {
    data class GetByIdSuccess(val c: Consulta) : DetalheState()
    data object ShowLoading : DetalheState()
}

class ConsultaViewModel (private val repository: ConsultaRepository) : ViewModel(){
    private val _stateCadastro = MutableStateFlow<ConsultaState>(ConsultaState.ShowLoading)
    val stateCadastro = _stateCadastro.asStateFlow()

    private val _stateList = MutableStateFlow<ListaState>(ListaState.ShowLoading)
    val stateList = _stateList.asStateFlow()

    private val _stateDetail = MutableStateFlow<DetalheState>(DetalheState.ShowLoading)
    val stateDetail = _stateDetail.asStateFlow()




    fun insert(consultaEntity: Consulta) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(consultaEntity)
        _stateCadastro.value=ConsultaState.InsertSuccess
    }

    fun getAllContacts(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllConsultas().collect{ result ->
                if (result.isEmpty()){
                    _stateList.value = ListaState.EmptyState
                }else{
                    _stateList.value = ListaState.SearchAllSuccess(result)
                }
            }
        }
    }

    fun getContactById(id: Int) {
        viewModelScope.launch {
            repository.getConsultaById(id).collect { result ->
                _stateDetail.value = DetalheState.GetByIdSuccess(result)
            }
        }
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