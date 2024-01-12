package com.youngseo3.infrean.communityapp.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.board.BoardInsideActivity
import com.youngseo3.infrean.communityapp.board.BoardListRVAdapter
import com.youngseo3.infrean.communityapp.board.BoardModel
import com.youngseo3.infrean.communityapp.board.BoardWriteActivity
import com.youngseo3.infrean.communityapp.contentsList.ContentRVAdapter
import com.youngseo3.infrean.communityapp.contentsList.ContentShowActivity
import com.youngseo3.infrean.communityapp.databinding.FragmentTalkBinding
import com.youngseo3.infrean.communityapp.utils.FBRef

class TalkFragment : Fragment() {

    private lateinit var binding: FragmentTalkBinding

    private var boardDataList = mutableListOf<BoardModel>()
    private var boardKeyList = mutableListOf<String>()

    lateinit var boardRVAdapter: BoardListRVAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)

        binding.imageviewTalkWrite.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        // 어댑터 연결
        boardRVAdapter = BoardListRVAdapter(boardDataList)
        binding.rvTalkBoardlist.adapter = boardRVAdapter

        getFBBoardData() // 데이터 불러오기

        boardRVAdapter.itemClickListener = object : BoardListRVAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, BoardInsideActivity::class.java)
                // 1. 데이터의 키 값을 intent로 넘겨주기
                intent.putExtra("key", boardKeyList[position])

                // 2. 객체를 intent로 넘겨주기
//                val bundle = Bundle()
//                bundle.putSerializable("board", boardDataList[position])
//                intent.apply {
//                    this.putExtra("board", bundle)
//                }

                startActivity(intent)
            }
        }

        return binding.root
    }

    private fun getFBBoardData() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                boardDataList.clear()

                for (dataModel in snapshot.children) {
                    Log.d("Talk", dataModel.toString())
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                boardDataList.reverse()
                boardKeyList.reverse()
                boardRVAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("TalkFragment", "loadPost:OnCancelled", error.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }
}