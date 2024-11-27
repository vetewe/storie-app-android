package com.example.storie.ui.main.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.storie.data.Result
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storie.R
import com.example.storie.data.adapter.StoryAdapter
import com.example.storie.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupAnimation()
        setupRefreshLayout()

        homeViewModel.findStory()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.findStory()
    }

    private fun setupRecyclerView() {
        binding.rvStory.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvStory.adapter = StoryAdapter()
    }

    private fun setupObservers() {
        homeViewModel.story.observe(viewLifecycleOwner) { result ->
            binding.swipeRefreshLayout.isRefreshing = false
            when (result) {
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    (binding.rvStory.adapter as StoryAdapter).submitList(result.data)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        requireView(),
                        getString(R.string.failed_load_story), Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        homeViewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.tvWelcomeUser.text = getString(R.string.welcome_user, name)
        }
    }

    private fun setupRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.findStory()
        }
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.ivWelcomeHome, View.TRANSLATION_Y, -25f, 25f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}