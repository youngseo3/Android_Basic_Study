package com.youngseo3.infrean.communityapp.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.comment.CommentModel
import com.youngseo3.infrean.communityapp.comment.CommentRVAdapter
import com.youngseo3.infrean.communityapp.contentsList.ContentRVAdapter
import com.youngseo3.infrean.communityapp.databinding.ActivityBoardInsideBinding
import com.youngseo3.infrean.communityapp.utils.FBAuth
import com.youngseo3.infrean.communityapp.utils.FBRef

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardInsideBinding

    private lateinit var key: String
    lateinit var behavior: BottomSheetBehavior<LinearLayout>
    var isComment: Boolean = false

    lateinit var rvAdapter: CommentRVAdapter // 어댑터
    private val commentDataList = mutableListOf<CommentModel>()

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

        rvAdapter = CommentRVAdapter(commentDataList)
        binding.commentsRecyclerView.adapter = rvAdapter
        getCommentData(key)
        initEvent()
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
                binding.getImageArea.isVisible = false
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
    private fun insertComment(key: String) {
        val text = binding.commentEditText.text.toString()
        val time = FBAuth.getTime()

        FBRef.commentRef.child(key).push()
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(CommentModel(text, time))

        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_LONG).show()
        binding.commentEditText.setText("")
//        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun getCommentData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear() // 호출하지 않으면 리스트에 중복해서 추가됨
                for(dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }
                Log.d("getBookmarkData : ", commentDataList.toString())
                rvAdapter.notifyDataSetChanged() // 어댑터 동기화
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("BoardInsideActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }
    private fun initEvent() {
        persistentBottomSheetEvent()
        binding.buttonComment.setOnClickListener {
            isComment = !isComment
            if(isComment) { // 바텀시트 다이얼로그 펼침
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else { // 바텀시트 다이얼로그 펼침 닫음
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        binding.submitCommentButton.setOnClickListener {
            insertComment(key)
        }
    }

    private fun persistentBottomSheetEvent() {
        behavior = BottomSheetBehavior.from(binding.persistentBottomSheet)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 슬라이드 되는 도중 계속 호출
                // called continuously while dragging
                Log.d(TAG, "onStateChanged: 드래그 중")
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_COLLAPSED-> {
                        Log.d(TAG, "onStateChanged: 접음")
                    }
                    BottomSheetBehavior.STATE_DRAGGING-> {
                        Log.d(TAG, "onStateChanged: 드래그")
                    }
                    BottomSheetBehavior.STATE_EXPANDED-> {
                        Log.d(TAG, "onStateChanged: 펼침")
                    }
                    BottomSheetBehavior.STATE_HIDDEN-> {
                        Log.d(TAG, "onStateChanged: 숨기기")
                    }
                    BottomSheetBehavior.STATE_SETTLING-> {
                        Log.d(TAG, "onStateChanged: 고정됨")
                    }
                }
            }
        })
    }

    private val TAG = "PersistentActivity"
}