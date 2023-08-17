package org.d3ifcool.arfi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import org.d3ifcool.arfi.databinding.ActivityMainBinding
import org.d3ifcool.arfi.layout.AboutFragment
import org.d3ifcool.arfi.layout.HomeFragment
import org.d3ifcool.arfi.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setUpContentFragment()
        setUpNavbar()
        setupBackpresssed()
    }
    private fun setUpNavbar(){
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home_button -> {
                    val homeFragment = HomeFragment()
                    val fragmentManager = supportFragmentManager

                    fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_content, homeFragment, HomeFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.about_button -> {
                    val aboutFragment = AboutFragment()
                    val fragmentManager = supportFragmentManager

                    fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_content, aboutFragment, AboutFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun setUpContentFragment(){
        val homeFragment = HomeFragment()
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)

        if (fragment !is HomeFragment) {
            fragmentManager
                .beginTransaction()
                .add(R.id.fragment_content, homeFragment, HomeFragment::class.java.simpleName)
                .commit()
            }
        }
    private fun setupBackpresssed() {
        onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            })
    }
}