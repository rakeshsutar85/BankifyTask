package com.rakeshsutar85.bankifytask.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tickets_table")
data class Ticket(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "description")val description: String?,
    @ColumnInfo(name = "priority") val priority: String?,
    @ColumnInfo(name = "dueDate") val dueDate: String?,
    @ColumnInfo(name = "date")val date: String?
): Serializable
