package com.youngseo3.infrean.communityapp.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.databinding.ActivityBoardEditBinding
import com.youngseo3.infrean.communityapp.utils.FBAuth
import com.youngseo3.infrean.communityapp.utils.FBRef

class BoardEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardEditBinding
    private lateinit var key: String

    private lateinit var writerUid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.editBtn.setOnClickListener {
            editBoardData(key)
        }
    }

    private fun getImageData(key: String) {
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child("$key.png")

// ImageView in your Activity
        val imageView = binding.imageAreaIv

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(imageView)
            } else {
                Log.d("getImageData", "이미지 가져오기 실패")
            }
        })
    }

    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val dataModel = snapshot.getValue(BoardModel::class.java)
                if (dataModel != null) {
                    binding.edittextBoardwriteTitle.setText(dataModel.title)
                    binding.edittextBoardwriteContent.setText(dataModel.content)
                    writerUid = dataModel.uid
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("BoardInsideActivity", "loadPost:OnCancelled", error.toException())
            }
        }

        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun editBoardData(key: String){
        FBRef.boardRef
            .child(key)
            .setValue(
                BoardModel(binding.edittextBoardwriteTitle.text.toString(),
                                binding.edittextBoardwriteContent.text.toString(),
                                writerUid,
                                FBAuth.getTime()))
        finish()
    }
}