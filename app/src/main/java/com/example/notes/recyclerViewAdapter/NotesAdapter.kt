package com.example.notes.recyclerViewAdapter

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.model.NotesData
import com.example.notes.SQLite.mySqLite
import com.example.notes.R

class NotesAdapter(
    private var notes: List<NotesData>,
    private val context: Context,
    private val navController: NavController
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    var arrID = ArrayList<Int>()

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText = itemView.findViewById<TextView>(R.id.title_notes_design)
        val noteText = itemView.findViewById<TextView>(R.id.content_notes_design)
        val noteBox = itemView.findViewById<CardView>(R.id.noteBox)
        val notesBorder = itemView.findViewById<ConstraintLayout>(R.id.notesBorder)
        val defaultImg = itemView.findViewById<ImageView>(R.id.defaultImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_design, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleText.text = note.title
        holder.noteText.text = note.content

        val db = mySqLite(context)
        holder.noteBox.setOnLongClickListener {
            val id = notes[position].Id
            arrID.add(id)
            holder.notesBorder.setBackgroundResource(R.drawable.selected_notes_border)
            holder.noteBox.cardElevation = 12f * context.resources.displayMetrics.density
            holder.noteBox.outlineSpotShadowColor = Color.WHITE
            Handler().postDelayed({
                db.deleteNote(id)
                refreshData(db.getAllNotes())
                holder.notesBorder.setBackgroundResource(R.drawable.notes_border)
            }, 3000)

            true
        }

        holder.noteBox.setOnClickListener {
            val title = notes[position].title
            val content = notes[position].content
            val id = notes[position].Id

            holder.notesBorder.setBackgroundResource(R.drawable.notes_border)
            val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            sharedPref?.edit()?.apply {
                putInt("id", id)
                putString("title", title)
                putString("content", content)
                apply()
            }
            navController.navigate(R.id.action_listAllNoteFragment_to_updateNoteFragment)
        }
    }

    fun refreshData(newNotes: List<NotesData>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
