package com.youngseo3.infrean.communityapp.contentsList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.utils.FBAuth
import com.youngseo3.infrean.communityapp.utils.FBRef

class BookmarkRVAdapter(val context: Context,
                       val items: ArrayList<ContentModel>,
                       val keyList: ArrayList<String>,
                       val bookmarkIdList: MutableList<String>)
    : RecyclerView.Adapter<BookmarkRVAdapter.Viewholder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_content_list, parent, false)

        Log.d("BookmarkRVAdapter", bookmarkIdList.toString())
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: BookmarkRVAdapter.Viewholder, position: Int) {
        holder.bindItems(items[position], keyList[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* itemView는 R.layout.item_content_list을 의미
           item은 items(ContentModel을 받아온 리스트)의 요소 하나하나씩임.     */

        val title = itemView.findViewById<TextView>(R.id.textArea_tv)
        val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea_iv)
        val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea_iv)

        init { // init 부분에 작성하면 한번만 호출됨
            Log.d("BIND", "아이템클릭리스너, $adapterPosition")
            imageViewArea.setOnClickListener { // image 클릭시 웹 뷰 이동
                if (adapterPosition != RecyclerView.NO_POSITION) { // adapterPosition이 유효한 경우에만 작업을 수행
                    itemClick?.onClick(it, adapterPosition)
                    Log.d("BIND", "아이템클릭리스너, $adapterPosition")
                }
            }

        }

        fun bindItems(item: ContentModel, key: String) {

            if (bookmarkIdList.contains(key)) {
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
            } else {
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }

            title.text = item.title
            Glide.with(context)
                .load(item.imageUrl)
                .into(imageViewArea)
        }
    }
}