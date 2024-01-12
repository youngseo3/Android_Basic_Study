package com.youngseo3.infrean.communityapp.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.databinding.ActivityBoardWriteBinding
import com.youngseo3.infrean.communityapp.utils.FBAuth
import com.youngseo3.infrean.communityapp.utils.FBRef

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.boardWriteBtn.setOnClickListener {
            val title = binding.edittextBoardwriteTitle.text.toString()
            val content = binding.edittextBoardwriteContent.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            FBRef.boardRef
                .push()
                .setValue(BoardModel(title, content, uid, time))

            Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}