package com.youngseo3.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

// 보통은 ViewModel에서 저렇게 아래와 같이 딸랑 변수하나만 만들어서
// 사용하지는 않고 LiveData(등등)을 이용해서 함께 씀

class MainViewModel(num: Int): ViewModel() {

    var countValue = num

    init {
        Log.d("MainViewModel", "init")
    }

    fun plus() {
        countValue++
        Log.d("MainViewModel", countValue.toString())
    }

    fun minus() {
        countValue--
        Log.d("MainViewModel", countValue.toString())
    }

    fun getCount(): Int {
        return countValue
    }
}