package com.youngseo3.infrean.communityapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.youngseo3.infrean.communityapp.R
import com.youngseo3.infrean.communityapp.contentsList.ContentListActivity
import com.youngseo3.infrean.communityapp.databinding.FragmentTipBinding

class TipFragment : Fragment() {

    private lateinit var binding: FragmentTipBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tip, container, false)


        binding.category1.setOnClickListener {
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("category", "category1")
            startActivity(intent)
        }
        binding.category2.setOnClickListener {
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("category", "category2")
            startActivity(intent)
        }

        return binding.root
    }

}