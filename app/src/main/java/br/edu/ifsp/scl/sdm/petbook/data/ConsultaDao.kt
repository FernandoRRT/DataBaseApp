package br.edu.ifsp.scl.sdm.petbook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsultaDao {
    @Insert
    suspend fun insert(consultaEntity: ConsultaEntity)

    @Query("SELECT * FROM consulta ORDER BY nome")
    fun getAllConsultas(): Flow<List<ConsultaEntity>>

    @Query("SELECT * FROM consulta WHERE id = :id")
    fun getConsultaById(id: Int): Flow<ConsultaEntity>
}