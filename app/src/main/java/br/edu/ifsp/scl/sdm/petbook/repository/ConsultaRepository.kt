package br.edu.ifsp.scl.sdm.petbook.repository

import br.edu.ifsp.scl.sdm.petbook.data.ConsultaDao
import br.edu.ifsp.scl.sdm.petbook.domain.Consulta

class ConsultaRepository  (private val consultaDao: ConsultaDao) {

    suspend fun insert(consulta: Consulta) {
        return consultaDao.insert(consulta.toEntity())
    }
}