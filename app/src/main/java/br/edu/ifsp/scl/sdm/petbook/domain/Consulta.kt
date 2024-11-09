package br.edu.ifsp.scl.sdm.petbook.domain

import br.edu.ifsp.scl.sdm.petbook.data.ConsultaEntity

data class Consulta(
    val id: Int = 0,
    val nome: String,
    val clinica: String,
    val tipo: String,
    val data: String,
    val descricao: String
) {
    fun toEntity(): ConsultaEntity {
        return ConsultaEntity(id, nome, clinica, tipo, data, descricao)
    }
}