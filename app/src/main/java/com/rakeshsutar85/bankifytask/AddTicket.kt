package com.rakeshsutar85.bankifytask

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rakeshsutar85.bankifytask.databinding.ActivityAddTicketBinding
import com.rakeshsutar85.bankifytask.models.Ticket
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddTicket : AppCompatActivity() {
    private lateinit var binding: ActivityAddTicketBinding

    private lateinit var ticket: Ticket
    private var oldTicket: Ticket? = null
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val priorities = resources.getStringArray(R.array.Priority)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, priorities
        )
        try {
            oldTicket = intent.getSerializableExtra("current_ticket") as Ticket
            binding.etTicketName.setText(oldTicket?.name)
            binding.etDescription.setText(oldTicket?.description)
            binding.spinner.setSelection(adapter.getPosition(oldTicket?.priority))
            binding.tvDuedate.text = oldTicket?.dueDate
            isUpdate = true

        } catch (e: Exception) {
            e.printStackTrace()
        }
        var due_date = ""

        binding.btSelectDueDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->

                    due_date =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.tvDuedate.setText("Due date is : $due_date")
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()

        }
        var selectedPriority = ""

        val spinner = binding.spinner

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedPriority = priorities[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.imgSave.setOnClickListener {
            val name = binding.etTicketName.text.toString()
            val description = binding.etDescription.text.toString()
            val priority =  selectedPriority
            val dueDate = binding.tvDuedate.text.toString()
            if (name.isNotEmpty() && description.isNotEmpty() && priority.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm")
                if (isUpdate) {
                    ticket = Ticket(
                        oldTicket?.id,
                        name,
                        description,
                        priority,
                        dueDate,
                        formatter.format(Date()),
                    )
                } else {
                    ticket = Ticket(
                        null,
                        name,
                        description,
                        priority,
                        due_date,
                        formatter.format(Date())
                    )
                }
                val intent = Intent()
                intent.putExtra("ticket", ticket)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(
                    this@AddTicket,
                    "Please enter name, description and priority",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
        }
        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }

    }
}