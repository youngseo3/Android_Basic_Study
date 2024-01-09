package com.youngseo3.infrean.communityapp.contentsList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.utils.FBAuth
import com.youngseo3.infrean.communityapp.utils.FBRef

class ContentListActivity : AppCompatActivity() {

    lateinit var myRef: DatabaseReference
    lateinit var rvAdapter: ContentRVAdapter // 어댑터

    val bookmarkIDdList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val items = ArrayList<ContentModel>()
        val itemKeyList = ArrayList<String>()

        val rv: RecyclerView = findViewById(R.id.content_list_rv)
        rvAdapter = ContentRVAdapter(baseContext, items, itemKeyList, bookmarkIDdList)

//        items.add(ContentModel("title4", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FblYPPY%2Fbtq66v0S4wu%2FRmuhpkXUO4FOcrlOmVG4G1%2Fimg.png", "https://philosopher-chan.tistory.com/1235"))
//        items.add(ContentModel("title5", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FznKK4%2Fbtq665AUWem%2FRUawPn5Wwb4cQ8BetEwN40%2Fimg.png", "https://philosopher-chan.tistory.com/1236"))
//        items.add(ContentModel("title6", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbtig9C%2Fbtq65UGxyWI%2FPRBIGUKJ4rjMkI7KTGrxtK%2Fimg.png", "https://philosopher-chan.tistory.com/1237"))
//        items.add(ContentModel("gif2", "https://blog.kakaocdn.net/dn/dq8OFz/btq4AuchShs/PqmCKZK20PEYKtUssYe0U0/img.gif", "https://forwardhyang.tistory.com/42"))
//        for(i in 0..items.size-1) { // 데이터 집어넣기
//            myRef2.push().setValue(
//                items[i]
//            )
//            Log.d("ContentListActivity", items.toString())
//
//        }
        // Write a message to the database
        val database = Firebase.database
        val category = intent.getStringExtra("category") // 인텐트 받기

        if(category == "category1") {
            myRef = database.getReference("contents")

        } else if (category == "category2") {
            myRef = database.getReference("contents2")
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(dataModel in dataSnapshot.children) {
//                    Log.d("ContentListActivity", dataModel.toString())
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString())
                }
//                Log.d("ContentListActivity", items.toString())
                rvAdapter.notifyDataSetChanged() // 어댑터 동기화하라는 의미임 추가안하면 비동기로 처리되서 이미지가 안나옴
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)
        getBookmarkData()
        rv.adapter = rvAdapter // 어댑터 연결
        rv.layoutManager = GridLayoutManager(this, 2) // GridLayout 설정

        rvAdapter.itemClick = object : ContentRVAdapter.ItemClick { // 아이템 클릭 이벤트 처리
            override fun onClick(view: View, position: Int) {
                Toast.makeText(baseContext, items[position].title, Toast.LENGTH_LONG).show()

                val intent = Intent(this@ContentListActivity, ContentShowActivity::class.java)
                intent.putExtra("webUrl", items[position].webUrl)
                startActivity(intent)
            }
        }
    }

    private fun getBookmarkData() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                bookmarkIDdList.clear() // 호출하지 않으면 리스트에 중복해서 추가됨
                for(dataModel in dataSnapshot.children) {
                    bookmarkIDdList.add(dataModel.key.toString())
                }
                Log.d("getBookmarkData : ", bookmarkIDdList.toString())
                rvAdapter.notifyDataSetChanged() // 어댑터 동기화
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }
}