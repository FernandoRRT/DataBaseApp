package br.edu.ifsp.scl.sdm.petbook.domain

import br.edu.ifsp.scl.sdm.petbook.data.ConsultaEntity
import java.time.LocalDate

data class Consulta(
    var id: Int = 0,
    var nome: String,
    var clinica: String,
    var tipo: String,
    var data: LocalDate,
    var descricao: String
) {
    fun toEntity(): ConsultaEntity {
        return ConsultaEntity(id, nome, clinica, tipo, data, descricao)
    }
}