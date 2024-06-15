package com.example.notes.fragments

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
import com.example.notes.databinding.FragmentAddNoteBinding

class AddNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddNoteBinding
    lateinit var navController: NavController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        val view = binding.root


        val db = mySqLite(requireContext())

        binding.doneBtn.setOnClickListener {
            val title = binding.titleText.text.toString()
            val note = binding.noteText.text.toString()
            if(title.length == 0 && note.length == 0) {
                Toast.makeText(requireContext(), "Please Make Note First", Toast.LENGTH_SHORT).show()
            }
            else if(title.length == 0) {
                Toast.makeText(requireContext(), "Please Set Title", Toast.LENGTH_SHORT).show()
            }
            else {
                val makeNote = NotesData(title,note,0)
                db.insertNote(makeNote)
                Toast.makeText(requireContext(), "Note Added", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_addNoteFragment_to_listAllNoteFragment)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
}