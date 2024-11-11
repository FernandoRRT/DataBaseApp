package br.edu.ifsp.scl.sdm.petbook.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.edu.ifsp.scl.sdm.petbook.domain.Consulta
import java.time.LocalDate

@Entity(tableName = "consulta")
data class ConsultaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val clinica: String,
    val tipo: String,
    val data: LocalDate,
    val descricao: String
) {
    fun toDomain(): Consulta {
        return Consulta(id, nome, clinica, tipo, data, descricao)
    }
}
