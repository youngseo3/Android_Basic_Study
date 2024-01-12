package com.youngseo3.infrean.communityapp.board
import java.io.Serializable

data class BoardModel(
    val title: String = "",
    val content: String = "",
    val uid: String = "",
    val time: String = ""
) : Serializable
