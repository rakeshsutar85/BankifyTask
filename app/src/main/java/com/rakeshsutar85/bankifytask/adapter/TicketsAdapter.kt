package com.rakeshsutar85.bankifytask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rakeshsutar85.bankifytask.R
import com.rakeshsutar85.bankifytask.models.Ticket
import kotlin.random.Random

class TicketsAdapter(private val context: Context, private val listener: TicketsItemClickInterface) : RecyclerView.Adapter<TicketsAdapter.TicketsViewHolder>() {

    private val TicketsList = ArrayList<Ticket>()
    private val fullList = ArrayList<Ticket>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsViewHolder {
        return TicketsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return TicketsList.size
    }

    override fun onBindViewHolder(holder: TicketsViewHolder, position: Int) {
        val currentTicket = TicketsList[position]
        holder.ticketName.text = currentTicket.name
        holder.ticketName.isSelected = true
        holder.ticketDescription.text = currentTicket.description
        holder.ticketPriority.text = currentTicket.priority
        holder.ticketDueDate.text = currentTicket.dueDate
        holder.date.text = currentTicket.date
        holder.date.isSelected = true
        holder.tickets_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))
        holder.tickets_layout.setOnClickListener {
            listener.onItemClicked(TicketsList[holder.adapterPosition])
        }
        holder.tickets_layout.setOnLongClickListener {
            listener.onItemLongClicked(TicketsList[holder.adapterPosition], holder.tickets_layout)
            true
        }

    }


    fun updateList(newList : List<Ticket>){
        fullList.clear()
        fullList.addAll(newList)
        TicketsList.clear()
        TicketsList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search: String){
        TicketsList.clear()
        for (item in fullList){
            if(item.name?.lowercase()?.contains(search.lowercase()) == true ||
                item.description?.lowercase()?.contains(search.lowercase()) == true){
                TicketsList.add(item)
            }
        }
        notifyDataSetChanged()
    }


    fun randomColor() : Int{
        val list = ArrayList<Int>()
        list.add(R.color.TicketColor1)
        list.add(R.color.TicketColor2)
        list.add(R.color.TicketColor3)
        list.add(R.color.TicketColor4)
        list.add(R.color.TicketColor5)
        list.add(R.color.TicketColor6)
        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    inner class TicketsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tickets_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val ticketName = itemView.findViewById<TextView>(R.id.tv_ticketName)
        val ticketDescription = itemView.findViewById<TextView>(R.id.tv_ticketDescription)
        val ticketPriority = itemView.findViewById<TextView>(R.id.tv_ticketPriority)
        val ticketDueDate = itemView.findViewById<TextView>(R.id.tv_ticketDeuDate)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
    }

    interface TicketsItemClickInterface{
        fun onItemClicked(ticket: Ticket)
        fun onItemLongClicked(ticket: Ticket, cardView: CardView)
    }

}