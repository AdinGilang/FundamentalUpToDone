package com.example.fundamental.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamental.data.response.DetailEventResponse
import com.example.fundamental.data.retrofit.ApiConfig
import com.example.fundamental.databinding.FragmentHomeBinding
import com.example.fundamental.ui.finished.FinishedAdapter
import com.example.fundamental.ui.upcoming.UpcomingAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        fetchHomeEvents()
    }

    private fun setupRecyclerViews() {
        _binding?.rvFinishedEvents?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        _binding?.rvUpcomingEvents?.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
    }

    private fun fetchHomeEvents() {
        showLoading(true)
        val clientFinished = ApiConfig.getApiService().getEvents()
        clientFinished.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(call: Call<DetailEventResponse>, response: Response<DetailEventResponse>) {
                if (response.isSuccessful) {
                    val finishedEvents = response.body()?.listEvents ?: emptyList()
                    val adapterFinished = FinishedAdapter().apply {
                        submitList(finishedEvents)
                    }
                    _binding?.rvFinishedEvents?.adapter = adapterFinished
                } else {
                    showError("Gagal memuat event: ${response.message()}")
                }
                showLoading(false)
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                showError("Error: ${t.message}")
                showLoading(false)
            }
        })

        val clientUpcoming = ApiConfig.getApiService().getEvents()
        clientUpcoming.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(call: Call<DetailEventResponse>, response: Response<DetailEventResponse>) {
                if (response.isSuccessful) {
                    val upcomingEvents = response.body()?.listEvents ?: emptyList()
                    val adapterUpcoming = UpcomingAdapter().apply {
                        submitList(upcomingEvents)
                    }
                    _binding?.rvUpcomingEvents?.adapter = adapterUpcoming
                } else {
                    showError("Gagal memuat event: ${response.message()}")
                }
                showLoading(false)
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                showError("Error: ${t.message}")
                showLoading(false)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        _binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
