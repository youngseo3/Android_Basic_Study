package com.youngseo3.infrean.communityapp.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.databinding.ActivityBoardWriteBinding
import com.youngseo3.infrean.communityapp.utils.FBAuth
import com.youngseo3.infrean.communityapp.utils.FBRef
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding
    private var isImageUpload = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.boardWriteBtn.setOnClickListener {
            val title = binding.edittextBoardwriteTitle.text.toString()
            val content = binding.edittextBoardwriteContent.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            /* 이미지 이름에 대한 정보를 모르기 때문에 이미지의 이름을
               key값으로 해줘서 이미지에 대한 정보를 찾기 쉽게 함 */
            val key = FBRef.boardRef.push().key.toString()

            FBRef.boardRef
                .child(key)
                .setValue(BoardModel(title, content, uid, time))

            if(isImageUpload) imageUpload(key)


            Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 핸드폰에서 사진 선택
        binding.imageAreaIv.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }
    }
    private fun imageUpload(key: String) { // 이미지 업로드
        // Get the data from an ImageView as bytes
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")

        val imageView = binding.imageAreaIv

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            binding.imageAreaIv.setImageURI(data?.data)
        }
    }
}