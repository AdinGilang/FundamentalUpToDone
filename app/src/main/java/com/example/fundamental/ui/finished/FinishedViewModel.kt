package com.example.fundamental.ui.finished

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundamental.data.response.DetailEventResponse
import com.example.fundamental.data.response.ListEventsItem
import com.example.fundamental.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {
    private val _listReview = MutableLiveData<List<ListEventsItem>?>()
    val listReview: LiveData<List<ListEventsItem>?> get() = _listReview

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    companion object {
        private const val TAG = "FinishedViewModel"
    }

    fun findDicoding(context: Context) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(active = 0)

        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val listEvents = response.body()?.listEvents
                    if (listEvents != null) {
                        _listReview.value = listEvents
                    } else {
                        Log.e(TAG, "onFailure: listEvents is null")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.code()} - ${response.message()}")
                    _errorMessage.value = "failed: ${response.message()}"
                }
            }
            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message ?: "error"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}

