package com.rakeshsutar85.bankifytask.repository

import androidx.lifecycle.LiveData
import com.rakeshsutar85.bankifytask.database.TicketDao
import com.rakeshsutar85.bankifytask.models.Ticket

class TicketsRepository(private val ticketDao: TicketDao) {
    val allTickets: LiveData<List<Ticket>> = ticketDao.getAllTickets()

    suspend fun insert(ticket: Ticket) {
        ticketDao.insert(ticket)
    }

    suspend fun delete(ticket: Ticket) {
        ticketDao.delete(ticket)
    }

    suspend fun update(ticket: Ticket) {
        ticketDao.update(ticket.id, ticket.name, ticket.description, ticket.priority, ticket.dueDate)
    }
}