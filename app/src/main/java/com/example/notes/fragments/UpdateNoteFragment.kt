package com.example.notes.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.notes.model.NotesData
import com.example.notes.SQLite.mySqLite
import com.example.notes.R
import com.example.notes.databinding.FragmentUpdateNoteBinding

class UpdateNoteFragment : Fragment() {
    private lateinit var binding: FragmentUpdateNoteBinding
    lateinit var navController: NavController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        val view = binding.root

        val sharedPref = activity?.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val id = sharedPref?.getInt("id", -1)!!
        val title = sharedPref?.getString("title", null)!!
        val content = sharedPref?.getString("content", null)

        binding.titleText.setText(title)
        binding.noteText.setText(content)

        val db = mySqLite(requireContext())
        binding.doneBtn.setOnClickListener {
            val title = binding.titleText.text.toString()
            val note = binding.noteText.text.toString()
            if (title.length == 0 && note.length == 0) {
                Toast.makeText(requireContext(), "Please Make Note First", Toast.LENGTH_SHORT)
                    .show()
            } else if (title.length == 0) {
                Toast.makeText(requireContext(), "Please Set Title", Toast.LENGTH_SHORT).show()
            } else {
                db.deleteNote(id)
                val makeNote = NotesData(title, note, id)
                db.insertNote(makeNote)
                navController.navigate(R.id.action_updateNoteFragment_to_listAllNoteFragment)
                Toast.makeText(requireContext(), "Note Updated", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}