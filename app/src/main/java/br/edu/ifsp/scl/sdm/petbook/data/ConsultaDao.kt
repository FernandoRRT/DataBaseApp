package br.edu.ifsp.scl.sdm.petbook.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ConsultaDao {
    @Insert
    suspend fun insert(consultaEntity: ConsultaEntity)
}