package com.example.fundamental.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundamental.R
import com.example.fundamental.data.database.Note
import com.example.fundamental.databinding.ItemEventBinding

class NoteAdapter(private val onItemClick: (Note) -> Unit) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
        holder.itemView.setOnClickListener {
            onItemClick(note)
        }
    }

    class NoteViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.tittle.text = note.title
            // Use the truncateDescription method to display the summary
            binding.summary.text = truncateDescription(note.description)

            Glide.with(binding.root.context)
                .load(note.coverImage)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.ImagePhoto)
        }

        private fun truncateDescription(description: String): String {
            val maxLength = 50 // Set the maximum length you want to display
            return if (description.length > maxLength) {
                "${description.substring(0, maxLength)}..." // Append ellipsis if truncated
            } else {
                description
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }
}
