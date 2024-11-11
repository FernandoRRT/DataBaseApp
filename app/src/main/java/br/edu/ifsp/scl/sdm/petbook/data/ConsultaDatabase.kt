package br.edu.ifsp.scl.sdm.petbook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ConsultaEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class ConsultaDatabase : RoomDatabase() {
    abstract fun consultaDAO(): ConsultaDao

    companion object {
        @Volatile
        private var INSTANCE: ConsultaDatabase? = null

        fun getDatabase(context: Context): ConsultaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ConsultaDatabase::class.java,
                    "consultaPet.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
