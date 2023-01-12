package com.project.news.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.news.R
import com.project.news.database.AppDatabase
import com.project.news.databinding.ActivityMainBinding
import com.project.news.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object{
        fun LOG(data :String) {
            Log.d("/@/",data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(HomeFragment(), Bundle())

    }

    @SuppressLint("WrongConstant")
    fun loadFragment(fragment: Fragment, bundle: Bundle, fromFragment: Fragment? = null, hideAnimation : Boolean = false, onGoingBack :Boolean = false) {
        /**       put BackStack History     **/
        if (fromFragment != null) {
            bundle.putBoolean(fromFragment::class.simpleName, true)
        }

//        CommonFunctions.hideKeyboard(this,binding.navView)

        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().apply {
            if(hideAnimation) {
                /**            It is showing bottom nav view ,thus it is parent fragment and is also not coming from back button
                thus it doesn't need animation **/
                replace(R.id.main_fragment_container, fragment)
                commit()
            }
            else {
                setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                add(R.id.main_fragment_container, fragment)
                addToBackStack(null)
                commit()
            }
        }

        LOG("FragmentOpen :- ${fragment::class.simpleName.toString()}")
    }
}