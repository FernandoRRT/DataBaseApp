package br.edu.ifsp.scl.sdm.petbook.repository

import br.edu.ifsp.scl.sdm.petbook.data.ConsultaDao
import br.edu.ifsp.scl.sdm.petbook.domain.Consulta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class ConsultaRepository  (private val consultaDao: ConsultaDao) {

    suspend fun insert(consulta: Consulta) {
        return consultaDao.insert(consulta.toEntity())
    }

    suspend fun update(consulta: Consulta) {
        return consultaDao.update(consulta.toEntity())
    }

    suspend fun delete(consulta: Consulta) {
        return consultaDao.delete(consulta.toEntity())
    }

    fun getAllConsultas(): Flow<List<Consulta>> {
        return consultaDao.getAllConsultas().map { consultaEntityList ->
            consultaEntityList.map { it.toDomain() }
        }
    }

    fun getConsultaById(id: Int): Flow<Consulta> {
        return consultaDao.getConsultaById(id).filterNotNull().map { it.toDomain() }
    }
}