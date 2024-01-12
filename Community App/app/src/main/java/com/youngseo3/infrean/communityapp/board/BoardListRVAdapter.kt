package com.youngseo3.infrean.communityapp.board

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.youngseo3.infrean.communityapp.R

class BoardListRVAdapter(val boardList: MutableList<BoardModel>): RecyclerView.Adapter<BoardListRVAdapter.ViewHolder>() {

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    var itemClickListener: onItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoardListRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_board_list, parent, false)

        Log.d("ContentRVAdapter", boardList.toString())
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: BoardListRVAdapter.ViewHolder, position: Int) {
        holder.bindItems(boardList[position])
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val title = itemView.findViewById<TextView>(R.id.textview_boardlist_title)
        val content = itemView.findViewById<TextView>(R.id.textview_boardlist_content)
        val time = itemView.findViewById<TextView>(R.id.textview_boardlist_time)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) { // adapterPosition이 유효한 경우에만 작업을 수행
                    itemClickListener?.onItemClick(adapterPosition)
                    Log.d("BIND", "아이템클릭리스너, $adapterPosition")

                }
            }
        }
        fun bindItems(item: BoardModel) {
            title.text = item.title
            content.text = item.content
            time.text = item.time
        }
    }
}