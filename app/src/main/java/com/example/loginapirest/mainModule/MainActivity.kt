package com.example.loginapirest.mainModule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.loginapirest.R
import com.example.loginapirest.databinding.ActivityMainBinding
import com.example.loginapirest.mainModule.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val _mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        launchFragments()
    }

    private fun launchFragments() {
        val loginFragment = LoginFragment()
        val profileFragment = ProfileFragment()

        supportFragmentManager.beginTransaction().add(R.id.mainContainer, profileFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.mainContainer, loginFragment).commit()

        _mainViewModel.currentUser.observe(this) {
            if (it != null) {
                supportFragmentManager.beginTransaction().hide(loginFragment).show(profileFragment)
                    .commit()
            } else {
                supportFragmentManager.beginTransaction().hide(profileFragment).show(loginFragment)
                    .commit()
            }
        }
    }

}
