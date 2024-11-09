package br.edu.ifsp.scl.sdm.petbook

import android.app.Application
import br.edu.ifsp.scl.sdm.petbook.data.ConsultaDatabase
import br.edu.ifsp.scl.sdm.petbook.repository.ConsultaRepository

class ConsultaApplication: Application() {
    val database by lazy { ConsultaDatabase.getDatabase(this) }
    val repository by lazy { ConsultaRepository(database.consultaDAO()) }
}