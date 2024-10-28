package com.example.fundamental.ui.favorite

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundamental.R
import com.example.fundamental.ui.DetailActivity

class FavoriteFragment : Fragment() {

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)

        noteAdapter = NoteAdapter { note ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("note", note)
            }
            startActivity(intent)
        }
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favoriteNotes.observe(viewLifecycleOwner) { notes ->
            progressBar.visibility = if (notes.isNullOrEmpty()) View.GONE else View.VISIBLE
            noteAdapter.submitList(notes)
            if (notes.isEmpty()) {
                Toast.makeText(context, "No favorites yet", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Tambahkan method untuk memperbarui daftar favorit
    fun updateFavoriteList() {
        viewModel.favoriteNotes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.submitList(notes)
        }
    }
}


