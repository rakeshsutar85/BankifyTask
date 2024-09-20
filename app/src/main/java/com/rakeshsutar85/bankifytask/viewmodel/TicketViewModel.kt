package com.rakeshsutar85.bankifytask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rakeshsutar85.bankifytask.database.TicketDatabase
import com.rakeshsutar85.bankifytask.models.Ticket
import com.rakeshsutar85.bankifytask.repository.TicketsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TicketViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : TicketsRepository
    val allTickets : LiveData<List<Ticket>>
    init {
        val dao = TicketDatabase.getDatabase(application).getTicketDao()
        repository = TicketsRepository(dao)
        allTickets = repository.allTickets
    }
    fun deleteTicket(ticket: Ticket) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(ticket)
    }
    fun updateTicket(ticket: Ticket) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(ticket)
    }
    fun insertTicket(ticket: Ticket) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(ticket)
    }

}

