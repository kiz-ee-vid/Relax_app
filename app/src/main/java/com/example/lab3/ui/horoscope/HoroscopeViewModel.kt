package com.example.lab3.ui.horoscope

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.data.ApiItem
import com.example.lab3.data.NetworkModule
import com.example.lab3.data.RepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HoroscopeViewModel: ViewModel() {

    val horoscope = MutableLiveData<ApiItem?>()

    fun getHoroscope(sign: String){
        val repositoryImpl = RepositoryImpl(NetworkModule.provideApiService(NetworkModule.provideProductRetrofit()))
        CoroutineScope(Dispatchers.Default).launch {
            val data = repositoryImpl.sendResponse(sign)
            withContext(Dispatchers.Main) {
                println(data.toString())
                horoscope.value = data
            }
        }
    }

}