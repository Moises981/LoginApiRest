package com.example.loginapirest.mainModule

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.loginapirest.databinding.FragmentProfileBinding
import com.example.loginapirest.mainModule.viewModels.MainViewModel


class ProfileFragment : Fragment() {

    private lateinit var _binding: FragmentProfileBinding
    private val _mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(_binding) {
            _mainViewModel.currentUser.observe(viewLifecycleOwner) {
                hideKeyboard()
                it?.let {
                    tvEmail.text = it.email
                    tvName.text = it.fullName
                    tvText.text = it.text
                    tvUrl.text = it.url
                    Glide.with(this@ProfileFragment).load(it.avatar).diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    ).centerCrop().circleCrop()
                        .into(imgPhoto)
                }
            }
        }
        _binding.btnLogout.setOnClickListener {
            _mainViewModel.clearUser()
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}