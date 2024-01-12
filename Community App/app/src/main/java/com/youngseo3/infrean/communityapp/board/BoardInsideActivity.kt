package com.youngseo3.infrean.communityapp.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.databinding.ActivityBoardInsideBinding
import com.youngseo3.infrean.communityapp.utils.FBRef

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardInsideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        // 1.
        val key = intent.getStringExtra("key").toString()
        getBoardData(key)

        // 2.
//        val bundle = intent.getBundleExtra("board")
//        val dataModel = bundle?.getSerializable("board") as BoardModel

//        binding.titleArea.text = dataModel.title
//        binding.textArea.text = dataModel.content
//        binding.timeArea.text = dataModel.time

//        Toast.makeText(this, dataModel.title, Toast.LENGTH_LONG).show()

    }

    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(BoardModel::class.java)
                if (dataModel != null) {
                    binding.titleArea.text = dataModel.title
                    binding.textArea.text = dataModel.content
                    binding.timeArea.text = dataModel.time
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("BoardInsideActivity", "loadPost:OnCancelled", error.toException())
            }
        }

        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}