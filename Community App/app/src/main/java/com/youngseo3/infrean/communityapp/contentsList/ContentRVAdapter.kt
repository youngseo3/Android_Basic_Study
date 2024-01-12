package com.youngseo3.infrean.communityapp.contentsList

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.utils.FBAuth
import com.youngseo3.infrean.communityapp.utils.FBRef

class ContentRVAdapter(val context: Context,
                       val items: ArrayList<ContentModel>,
                       val keyList: ArrayList<String>,
                       val bookmarkIdList: MutableList<String>)
    : RecyclerView.Adapter<ContentRVAdapter.Viewholder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_content_list, parent, false)

        Log.d("ContentRVAdapter", bookmarkIdList.toString())
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: ContentRVAdapter.Viewholder, position: Int) {

//        if(itemClick != null) { // onBindViewHolder 부분에 작성하면 스크롤시 계속 클릭리스너가 호출됨
//            Log.d("BIND", "아이템클릭리스너+$position+")
//            holder.itemView.setOnClickListener { v ->
//                itemClick?.onClick(v, position)
//            }
//        }
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

            bookmarkArea.setOnClickListener { // bookmark 클릭 이벤트
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val currentKey = keyList[adapterPosition]
                    Toast.makeText(context, currentKey, Toast.LENGTH_LONG).show()

                    if(bookmarkIdList.contains(currentKey)) {
                        // 북마크가 있을 때
                        FBRef.bookmarkRef
                            .child(FBAuth.getUid())
                            .child(currentKey)
                            .removeValue()
                    } else {
                        // 북마크가 없을 때
                        FBRef.bookmarkRef
                            .child(FBAuth.getUid())
                            .child(currentKey)
                            .setValue(BookmarkModel(true))
                      }
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