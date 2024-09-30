package com.emanh.mixivivu.view.blog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emanh.mixivivu.R
import com.emanh.mixivivu.databinding.FragmentBlogBinding
import com.emanh.mixivivu.viewModel.BlogViewModel

class BlogFragment : Fragment() {
    private var _binding: FragmentBlogBinding? = null

    private val binding get() = _binding!!
    private val viewModel = BlogViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlogBinding.inflate(inflater, container, false)

        initBlog()

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBlog() {
        viewModel.loadBlog()

        binding.progressBlog.visibility = View.VISIBLE
        viewModel.blog.observe(viewLifecycleOwner) {
            binding.listBlog.layoutManager = LinearLayoutManager(requireContext())
            binding.listBlog.adapter = BlogAdapter(it)
            binding.progressBlog.visibility = View.GONE
        }
    }
}