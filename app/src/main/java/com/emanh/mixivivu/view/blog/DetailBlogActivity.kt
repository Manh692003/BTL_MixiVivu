@file:Suppress("DEPRECATION")

package com.emanh.mixivivu.view.blog

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.emanh.mixivivu.databinding.ActivityDetailBlogBinding
import com.emanh.mixivivu.model.BlogModel
import com.emanh.mixivivu.view.BaseActivity

class DetailBlogActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBlogBinding
    private lateinit var blog: BlogModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initIntro()
        initContent()
    }

    private fun init() {
        blog = intent.getParcelableExtra("objectBlog")!!

        binding.buttonBack.setOnClickListener { finish() }
    }

    private fun initIntro() {
        binding.title.text = blog.title
        binding.date.text = blog.date
        binding.intro.text = blog.intro

        Glide.with(this)
            .load(blog.picList[0])
            .into(binding.picIntro)
    }

    private fun initContent() {
        val contentViews = listOf(
            binding.content1, binding.content2, binding.content3, binding.content4,
            binding.content5, binding.content6, binding.content7, binding.content8,
            binding.content9, binding.content10, binding.content11, binding.content12,
            binding.content13, binding.content14, binding.content15)
        val picViews = listOf(
            binding.picContent1, binding.picContent2, binding.picContent3, binding.picContent4,
            binding.picContent5, binding.picContent6, binding.picContent7, binding.picContent8,
            binding.picContent9, binding.picContent10, binding.picContent11, binding.picContent12,
            binding.picContent13, binding.picContent14, binding.picContent15)

        for (i in contentViews.indices) {
            if (i < blog.content.size) {
                contentViews[i].text = blog.content[i]
                contentViews[i].visibility = View.VISIBLE
            } else {
                contentViews[i].visibility = View.GONE
            }

            if (i + 1 < blog.picList.size) {
                Glide.with(this)
                    .load(blog.picList[i + 1])
                    .into(picViews[i])
                picViews[i].visibility = View.VISIBLE
            } else {
                picViews[i].visibility = View.GONE
            }
        }

        binding.endContent.text = blog.endContent
    }
}