package com.youngseo3.infrean.communityapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.youngseo3.infrean.communityapp.auth.IntroActivity
import com.youngseo3.infrean.communityapp.databinding.ActivityMainBinding
import com.youngseo3.infrean.communityapp.fragments.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        binding.logoutBtn.setOnClickListener {
//
//            auth.signOut()
//
//            val intent = Intent(this, IntroActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }

        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, HomeFragment())
                        .commit()
                    true
                }
                R.id.fragment_goodtip -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, TipFragment())
                        .commit()
                    true
                }
                R.id.fragment_talk -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, TalkFragment())
                        .commit()
                    true
                }
                R.id.fragment_bookmark -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, BookmarkFragment())
                        .commit()
                    true
                }
                R.id.fragment_store -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, StoreFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}