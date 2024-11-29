package com.example.storie.ui.main.home

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storie.R
import com.example.storie.data.adapter.LoadingStateAdapter
import com.example.storie.data.adapter.StoryAdapter
import com.example.storie.databinding.FragmentHomeBinding
import com.example.storie.ui.activity.maps.MapsActivity
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory.getInstance(requireContext())
    }
    private var isDataInitiallyLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
        setupAction()
    }

    private fun setupAction() {
        binding.buttonMaps.setOnClickListener {
            val intentMaps = Intent(requireActivity(), MapsActivity::class.java)
            startActivity(intentMaps)
        }
    }

    private fun setupRecyclerView() {
        val storyAdapter = StoryAdapter()

        binding.rvStory.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        homeViewModel.story.observe(viewLifecycleOwner) {
            storyAdapter.submitData(lifecycle, it)
        }

        storyAdapter.addLoadStateListener { loadState ->
            _binding?.let { binding ->
                if (!isDataInitiallyLoaded && loadState.source.refresh is LoadState.Loading) {
                    binding.progressBar.isVisible = true
                } else {
                    binding.progressBar.isVisible = false
                    if (loadState.source.refresh is LoadState.NotLoading) {
                        isDataInitiallyLoaded = true
                    }
                }

                if (loadState.source.refresh is LoadState.Error) {
                    val error = (loadState.source.refresh as LoadState.Error).error
                    Snackbar.make(
                        binding.root,
                        "Error: ${error.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupObservers() {
        homeViewModel.userName.observe(viewLifecycleOwner) { name ->
            _binding?.let { binding ->
                binding.tvWelcomeUser.text = getString(R.string.welcome_user, name)
            }
        }
    }

    private fun setupAnimation() {
        _binding?.let { binding ->
            ObjectAnimator.ofFloat(binding.ivWelcomeHome, View.TRANSLATION_Y, -25f, 25f).apply {
                duration = 5900
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()
            ObjectAnimator.ofFloat(binding.ivBannerHome2, View.TRANSLATION_Y, -30f, 30f).apply {
                duration = 6500
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}