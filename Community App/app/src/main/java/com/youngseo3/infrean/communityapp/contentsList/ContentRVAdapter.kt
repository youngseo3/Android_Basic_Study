package com.youngseo3.infrean.communityapp.contentsList

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youngseo3.infrean.communityapp.R

class ContentRVAdapter(val context: Context, val items : ArrayList<ContentModel>): RecyclerView.Adapter<ContentRVAdapter.Viewholder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_content_list, parent, false)
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: ContentRVAdapter.Viewholder, position: Int) {

//        if(itemClick != null) { // onBindViewHolder 부분에 작성하면 스크롤시 계속 클릭리스너가 호출됨
//            Log.d("BIND", "아이템클릭리스너+$position+")
//            holder.itemView.setOnClickListener { v ->
//                itemClick?.onClick(v, position)
//            }
//        }
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* itemView는 R.layout.item_content_list을 의미
           item은 items(ContentModel을 받아온 리스트)의 요소 하나하나씩임.     */

        init { // init 부분에 작성하면 한번만 호출됨
            Log.d("BIND", "아이템클릭리스너+$adapterPosition+")
            itemView.setOnClickListener {
                itemClick?.onClick(it, adapterPosition)
            }
        }

        fun bindItems(item: ContentModel) {
            val title = itemView.findViewById<TextView>(R.id.textArea_tv)
            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea_iv)

            title.text = item.title
            Glide.with(context)
                .load(item.imageUrl)
                .into(imageViewArea)
        }
    }
}