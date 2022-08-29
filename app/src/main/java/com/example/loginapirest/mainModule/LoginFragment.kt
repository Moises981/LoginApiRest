package com.example.loginapirest.mainModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.loginapirest.R
import com.example.loginapirest.common.entities.User
import com.example.loginapirest.databinding.FragmentLoginBinding
import com.example.loginapirest.mainModule.viewModels.MainViewModel
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val _mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupViewModel()
    }

    private fun setupViewModel() {
        _mainViewModel.apiResponse.observe(viewLifecycleOwner) { res ->
            if (res.errorStatus == 0) {
                var message = "Token: ${res.token}"
                if (res.id != 0) {
                    message += "ID: ${res.id}"
                    _mainViewModel.findUserById(id)
                } else {
                    _mainViewModel.findUserById()
                }
                updateUI(message)
                clearUI()
            } else if (res.errorStatus == 400) {
                updateUI(getString(R.string.main_error_server))
            }
        }
    }

    private fun setupUI() {
        with(_binding) {

            etEmail.setText("")
            etPassword.setText("")

            swType.setOnCheckedChangeListener { button, checked ->
                button.text =
                    if (checked) getString(R.string.main_login) else getString(R.string.main_register)
                btnLogin.text = button.text
            }

            btnLogin.setOnClickListener {
                if (validateFields(tilEmail, tilPassword)) {
                    val user = User(
                        email = etEmail.text.toString().trim(),
                        password = etPassword.text.toString().trim()
                    )
                    if (swType.isChecked) _mainViewModel.loginUser(user)
                    else _mainViewModel.registerUser(user)
                }
            }
        }
    }

    private fun clearUI() {
        with(_binding) {
            tvResult.text = ""
            etEmail.setText("")
            etPassword.setText("")
        }
    }

    private fun updateUI(message: String) {
        with(_binding) {
            tvResult.visibility = View.VISIBLE
            tvResult.text = message
        }
    }

    private fun validateFields(vararg inputFields: TextInputLayout): Boolean {
        var success = true
        for (inputField in inputFields) {
            if (inputField.editText!!.text.toString().trim().isEmpty()) {
                inputField.error = getString(R.string.main_validation_error)
                inputField.requestFocus()
            } else {
                inputField.error = null
            }
        }
        return success
    }
}