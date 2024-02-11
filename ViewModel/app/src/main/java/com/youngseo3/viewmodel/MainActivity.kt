package com.youngseo3.viewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.youngseo3.viewmodel.databinding.ActivityMainBinding

// ViewModel

// 1 - https://developer.android.com/topic/libraries/architecture/viewmodel#coroutines
// 2 - https://developer.android.com/guide/components/activities/activity-lifecycle?hl=ko

// 안드로이드의 생명 주기를 관리하기 쉽다.

// onSaveInstanceState() 사용해서 데이터를 관리할 수 있지만
// 적절하지 않다.

// 안드로이드의 생명 주기를 관리하기 쉽다라는 말과 같은 말인데
// 상태(LifeCycle)가 변경될 때 마다 데이터를 관리해줘야 하는데
// 이 관리가 편하다

// UI컨트롤러(Activity, Fragment)에서 모든 것을 다 하려고 하면 복잡해진다.
// 그래서 ViewModel 을 사용하면 테스트나 관리가 용이하다.

class MainActivity : AppCompatActivity() {

    //    private var countValue = 0
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        // 화면을 전환한다. 그니까 세로상태에서 가로상태로 놓이게 되면 저장하고 있던(countValue)텍스트값이 0으로 초기화됨
        // 따라서 ViewModel을 사용해서 이를 해결하고자 함. 즉 ViewModel을 사용하면 화면 전환을 해도 데이터가 초기화되지 않음
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        Log.d("MainActivity", "onCreate")

        binding.result.text = viewModel.countValue.toString()

        binding.plus.setOnClickListener{
            viewModel.plus()
            binding.result.text = viewModel.getCount().toString()
            // ViewModel 사용 X
//            countValue++
//            resultArea.text= countValue.toString()
        }

        binding.minus.setOnClickListener{
            viewModel.minus()
            binding.result.text = viewModel.getCount().toString()
            // ViewModel 사용 X
//            countValue--
//            resultArea.text= countValue.toString()
        }

        binding.showFragment.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameArea, TestFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d("MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()

        Log.d("MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()

        Log.d("MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()

        Log.d("MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("MainActivity", "onDestroy")
    }

}