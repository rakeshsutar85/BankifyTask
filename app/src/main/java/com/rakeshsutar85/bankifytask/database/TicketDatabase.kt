package com.rakeshsutar85.bankifytask.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.rakeshsutar85.bankifytask.models.Ticket
import com.rakeshsutar85.bankifytask.utilities.DATABASE_NAME

@Database(entities = [Ticket::class], version = 1, exportSchema = false)
abstract class TicketDatabase : RoomDatabase() {
    abstract fun getTicketDao(): TicketDao
    companion object{
        @Volatile
        private var INSTANCE: TicketDatabase? = null
        fun getDatabase(context : Context) : TicketDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    TicketDatabase::class.java,
                    DATABASE_NAME
                    ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}