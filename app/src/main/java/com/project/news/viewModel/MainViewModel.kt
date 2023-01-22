package com.project.news.viewModel

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val callFragment : MutableLiveData<Fragment> = MutableLiveData()

    val backPressed : MutableLiveData<Boolean> = MutableLiveData()

    fun callFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = Bundle()
        callFragment.postValue(fragment)
    }

    fun backPressed() {
        backPressed.postValue(true)
    }
}