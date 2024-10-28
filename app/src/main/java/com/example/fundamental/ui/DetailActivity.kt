package com.example.fundamental.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.fundamental.R
import com.example.fundamental.data.database.Note
import com.example.fundamental.data.repository.NoteRepository
import com.example.fundamental.data.response.DetailEventResponse
import com.example.fundamental.data.response.Event
import com.example.fundamental.data.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailActivity : AppCompatActivity() {

    private lateinit var eventImage: ImageView
    private lateinit var eventName: TextView
    private lateinit var eventDescription: TextView
    private lateinit var eventOwner: TextView
    private lateinit var eventQuota: TextView
    private lateinit var btnOpenButton: Button
    private lateinit var beginTime: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var loveButton: FloatingActionButton

    private var eventButton: String? = null
    private lateinit var noteRepository: NoteRepository
    private var isFavorite: Boolean = false // Menyimpan status favorit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        eventImage = findViewById(R.id.eventImage)
        eventName = findViewById(R.id.eventName)
        eventDescription = findViewById(R.id.eventDescription)
        eventOwner = findViewById(R.id.eventOwner)
        eventQuota = findViewById(R.id.eventQuota)
        beginTime = findViewById(R.id.beginTime)
        btnOpenButton = findViewById(R.id.eventButton)
        progressBar = findViewById(R.id.progressBar)
        loveButton = findViewById(R.id.loveButton)

        noteRepository = NoteRepository(application)

        val note = intent.getParcelableExtra<Note>("note")
        if (note != null) {
            displayNoteDetails(note)
        } else {
            val eventId = intent.getIntExtra("EVENT_ID", -1)
            if (eventId != -1) {
                loadEventDetail(eventId)
            } else {
                Log.e("DetailActivity", "Event ID tidak valid")
                Toast.makeText(this, "Event ID tidak valid", Toast.LENGTH_SHORT).show()
            }
        }

        btnOpenButton.setOnClickListener {
            openEventButton()
        }

        loveButton.setOnClickListener {
            toggleFavoriteEvent()
        }
    }

    private fun displayNoteDetails(note: Note) {
        eventName.text = note.title
        eventDescription.text = note.description
        eventOwner.text = note.eventOwner
        eventQuota.text = note.eventQuota
        beginTime.text = note.beginTime
        eventImage.contentDescription = note.coverImage

        eventButton = note.eventLink // Mengambil link acara dari note

        Glide.with(this)
            .load(note.coverImage)
            .into(eventImage)

        // Set isFavorite ke true karena ini adalah detail acara favorit
        isFavorite = true
        updateFavoriteIcon() // Memperbarui ikon favorit
    }

    private fun loadEventDetail(eventId: Int) {
        progressBar.visibility = View.VISIBLE

        val client = ApiConfig.getApiService().getEventDetail(eventId)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val detailResponse = response.body()
                    if (detailResponse != null && !detailResponse.error) {
                        val event = detailResponse.event
                        displayEventDetails(event)
                    } else {
                        Log.e("DetailActivity", "Event tidak ditemukan atau terdapat kesalahan: ${detailResponse?.message}")
                        Toast.makeText(this@DetailActivity, "Event tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("DetailActivity", "Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@DetailActivity, "Gagal memuat data: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("DetailActivity", "Gagal memuat detail event: ${t.message}")
                Toast.makeText(this@DetailActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayEventDetails(event: Event) {
        eventName.text = event.name
        eventDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        eventOwner.text = "Organizer: ${event.ownerName}"
        eventQuota.text = "Quota: ${event.quota - event.registrants}"
        beginTime.text = "Begin Time: ${event.beginTime}"
        eventImage.contentDescription = event.mediaCover

        eventButton = event.link
        Glide.with(this)
            .load(event.mediaCover)
            .into(eventImage)

        // Cek apakah acara ini ditandai sebagai favorit
        checkIfFavorite(event.name) // Periksa status favorit berdasarkan nama acara
    }

    private fun checkIfFavorite(eventTitle: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingNote = noteRepository.getNotesByTitle(eventTitle)
            isFavorite = existingNote != null // Memperbarui status favorit
            runOnUiThread {
                updateFavoriteIcon() // Memperbarui ikon favorit
            }
        }
    }

    private fun updateFavoriteIcon() {
        // Mengubah ikon sesuai dengan status favorit
        if (isFavorite) {
            loveButton.setImageResource(R.drawable.ic_favorite_black_24dp) // Ikon favorit solid
        } else {
            loveButton.setImageResource(R.drawable.baseline_favorite_border_24) // Ikon favorit border
        }
    }

    private fun openEventButton() {
        if (!eventButton.isNullOrEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventButton))
            startActivity(intent)
        } else {
            Toast.makeText(this, "Link tidak tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleFavoriteEvent() {
        CoroutineScope(Dispatchers.IO).launch {
            val existingNote = noteRepository.getNotesByTitle(eventName.text.toString())

            if (existingNote != null) {
                noteRepository.delete(existingNote)
                isFavorite = false // Set status favorit menjadi false
                runOnUiThread {
                    Toast.makeText(this@DetailActivity, "Event removed from favorites", Toast.LENGTH_SHORT).show()
                    updateFavoriteIcon() // Memperbarui ikon favorit
                }
            } else {
                val title = eventName.text?.toString()
                val description = eventDescription.text?.toString()
                val coverImage = eventImage.contentDescription?.toString()
                val beginTime = this@DetailActivity.beginTime.text?.toString()
                val eventQuota = eventQuota.text?.toString()
                val eventOwner = eventOwner.text?.toString()

                if (title.isNullOrEmpty() || description.isNullOrEmpty() || coverImage.isNullOrEmpty() ||
                    beginTime.isNullOrEmpty() || eventQuota.isNullOrEmpty() || eventOwner.isNullOrEmpty()
                ) {
                    runOnUiThread {
                        Toast.makeText(this@DetailActivity, "Data tidak lengkap, event gagal disimpan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val note = Note(
                        title = title,
                        description = description,
                        coverImage = coverImage,
                        beginTime = beginTime,
                        eventQuota = eventQuota,
                        eventOwner = eventOwner,
                        eventLink = eventButton, // Menyimpan event link di sini
                        isFavorite = true
                    )
                    noteRepository.insert(note)
                    isFavorite = true // Set status favorit menjadi true
                    runOnUiThread {
                        Toast.makeText(this@DetailActivity, "Event saved as favorite", Toast.LENGTH_SHORT).show()
                        updateFavoriteIcon() // Memperbarui ikon favorit
                    }
                }
            }
        }
    }
}


