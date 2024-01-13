package com.youngseo3.infrean.communityapp.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.databinding.ActivityBoardInsideBinding
import com.youngseo3.infrean.communityapp.utils.FBAuth
import com.youngseo3.infrean.communityapp.utils.FBRef

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardInsideBinding

    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        // 1.
        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)
        // 2.
//        val bundle = intent.getBundleExtra("board")
//        val dataModel = bundle?.getSerializable("board") as BoardModel

//        binding.titleArea.text = dataModel.title
//        binding.textArea.text = dataModel.content
//        binding.timeArea.text = dataModel.time

//        Toast.makeText(this, dataModel.title, Toast.LENGTH_LONG).show()

        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }

    }
    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                try {
                    val dataModel = snapshot.getValue(BoardModel::class.java)
                    if (dataModel != null) {
                        binding.titleArea.text = dataModel.title
                        binding.textArea.text = dataModel.content
                        binding.timeArea.text = dataModel.time
                        val myUid = FBAuth.getUid()
                        val writeUid = dataModel.uid

                        if(myUid.equals(writeUid)) {
                            binding.boardSettingIcon.isVisible = true
                            Toast.makeText(baseContext, "내가 글쓴이", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(baseContext, "아뇨 뚱인데요", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: java.lang.Exception) {
                    Log.d("BoardInsideActivity", "삭제 완료")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("BoardInsideActivity", "loadPost:OnCancelled", error.toException())
            }
        }

        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun getImageData(key: String) {
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child("$key.png")

// ImageView in your Activity
        val imageView = binding.getImageArea

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

    private fun showDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this, "수정 버튼을 눌렀습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show()
            alertDialog!!.dismiss()

            finish()
        }

    }
}