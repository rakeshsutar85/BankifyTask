package com.rakeshsutar85.bankifytask

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rakeshsutar85.bankifytask.adapter.TicketsAdapter
import com.rakeshsutar85.bankifytask.database.TicketDatabase
import com.rakeshsutar85.bankifytask.databinding.ActivityMainBinding
import com.rakeshsutar85.bankifytask.models.Ticket
import com.rakeshsutar85.bankifytask.viewmodel.TicketViewModel


class MainActivity : AppCompatActivity(), TicketsAdapter.TicketsItemClickInterface,
    PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TicketViewModel
    private lateinit var adapter: TicketsAdapter
    private lateinit var database: TicketDatabase
    lateinit var selectedTicket: Ticket

    private val updateTicket =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val ticket = it.data?.getSerializableExtra("ticket") as? Ticket
                if (ticket != null) {
                    viewModel.updateTicket(ticket)
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[TicketViewModel::class.java]

        viewModel.allTickets.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }
        database = TicketDatabase.getDatabase(this)

    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        adapter = TicketsAdapter(this, this)
        binding.recyclerView.adapter = adapter
        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val ticket = it.data?.getSerializableExtra("ticket") as? Ticket
                    if (ticket != null) {
                        viewModel.insertTicket(ticket)
                    }
                }
            }

        binding.imgFilter.setOnClickListener {
            val popup = PopupMenu(this, binding.imgFilter)
            popup.inflate(R.menu.filter_menu)
            popup.setOnMenuItemClickListener { item ->
                if (item.title == "Ascending" ){
                    viewModel.allTickets.observe(this) { list ->
                        list?.let {
                            adapter.updateList(list.reversed())
                        }
                    }
                }
                else if (item.title == "Descending"){
                    viewModel.allTickets.observe(this) { list ->
                        list?.let {
                            adapter.updateList(list)
                        }
                    }
                }
                true
            }
            popup.show()
        }

        binding.fbAddTicket.setOnClickListener {
            val intent = Intent(this, AddTicket::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterList(newText)
                }
                return true
            }

        })

    }

    override fun onItemClicked(ticket: Ticket) {
        val intent1 = Intent(this@MainActivity, AddTicket::class.java)
        intent1.putExtra("current_ticket", ticket)
        updateTicket.launch(intent1)
    }

    override fun onItemLongClicked(ticket: Ticket, cardView: CardView) {
        selectedTicket = ticket
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_ticket) {
            viewModel.deleteTicket(selectedTicket)
            return true
        }
        return false
    }

}