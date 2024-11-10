package br.edu.ifsp.scl.sdm.petbook.domain

import br.edu.ifsp.scl.sdm.petbook.data.ConsultaEntity

data class Consulta(
    var id: Int = 0,
    var nome: String,
    var clinica: String,
    var tipo: String,
    var data: String,
    var descricao: String
) {
    fun toEntity(): ConsultaEntity {
        return ConsultaEntity(id, nome, clinica, tipo, data, descricao)
    }
}