package com.example.storie.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.storie.R
import com.example.storie.data.preferences.SettingPreference
import com.example.storie.data.preferences.dataStore
import com.example.storie.databinding.FragmentProfileBinding
import com.example.storie.ui.activity.setting.SettingViewModel
import com.example.storie.ui.activity.setting.SettingViewModelFactory
import com.example.storie.ui.activity.welcome.WelcomeActivity
import com.example.storie.utils.IdlingResource

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory.getInstance(requireContext())
    }

    private val settingViewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(SettingPreference.getInstance(requireContext().dataStore))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        logoutUser()

        setupSwitchDarkMode()
        switchListener()
        changeLanguage()
    }

    private fun changeLanguage() {
        binding.buttonChangeLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun switchListener() {
        binding.switchDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setupSwitchDarkMode() {
        settingViewModel.getThemeSettings()
            .observe(viewLifecycleOwner) { darkMode ->
                if (darkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.switchDarkMode.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.switchDarkMode.isChecked = false
                }
            }
    }

    private fun logoutUser() {
        binding.buttonActionLogout.setOnClickListener {
            dialogConfirmationLogout()
        }
    }

    private fun dialogConfirmationLogout() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.logout_title))
            setMessage(getString(R.string.logout_confirmation))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                IdlingResource.increment()
                profileViewModel.logout()
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
                IdlingResource.decrement()
            }
            setNegativeButton(getString(R.string.no), null)
            create()
            show()
        }
    }

    private fun setupObserver() {
        profileViewModel.userId.observe(viewLifecycleOwner) { userId ->
            binding.tvUserId.text = userId
        }

        profileViewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.tvNameUser.text = userName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
