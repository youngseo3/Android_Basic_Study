package com.youngseo3.infrean.communityapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.youngseo3.infrean.communityapp.MainActivity
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_intro)
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        binding.noAccountBtn.setOnClickListener {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}