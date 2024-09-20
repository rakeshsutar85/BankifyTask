package com.rakeshsutar85.bankifytask.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rakeshsutar85.bankifytask.models.Ticket

@Dao
interface TicketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ticket: Ticket)

    @Delete
    suspend fun delete(ticket: Ticket)

    @Query("Select * from tickets_table order by id ASC")
    fun getAllTickets(): LiveData<List<Ticket>>

    @Query("UPDATE tickets_table Set name = :name, description = :description, priority = :priority, dueDate =:dueDate WHERE id = :id")
    suspend fun update(id: Int?, name: String?, description: String?,priority: String?, dueDate: String?)
}