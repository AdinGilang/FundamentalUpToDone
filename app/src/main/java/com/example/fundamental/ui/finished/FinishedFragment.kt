package com.example.fundamental.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamental.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val finishedViewModel =
            ViewModelProvider(this).get(FinishedViewModel::class.java)
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val adapter = FinishedAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        finishedViewModel.findDicoding(requireContext())

        finishedViewModel.listReview.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                adapter.submitList(list)
            }
        }
        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        finishedViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
