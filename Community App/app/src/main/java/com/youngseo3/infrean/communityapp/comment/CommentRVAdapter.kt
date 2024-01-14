package com.youngseo3.infrean.communityapp.comment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.youngseo3.infrean.communityapp.R

class CommentRVAdapter(val commentList: MutableList<CommentModel>): RecyclerView.Adapter<CommentRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_comment_list, parent, false)

        Log.d("CommentRVAdapter", commentList.toString())
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommentRVAdapter.ViewHolder, position: Int) {
        holder.bindItems(commentList[position])
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var content= itemView.findViewById<TextView>(R.id.textview_comment_content)
        var time = itemView.findViewById<TextView>(R.id.textview_comment_time)

        fun bindItems(item: CommentModel) {
            content.text = item.commentText
            time.text = item.commentCreatedTime
        }
    }
}