package com.project.news.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.project.news.R
import com.project.news.data.Bookmark
import com.project.news.data.News
import com.project.news.database.AppDatabase
import com.project.news.databinding.FragmentHomeBinding
import com.project.news.ui.MainActivity
import com.project.news.ui.home.adapters.NewsItemClicked
import com.project.news.ui.home.adapters.NewsRvAdapter
import com.project.news.ui.home.bookmarks.HomeBookmarksFragment
import com.project.news.viewModel.MainViewModel
import com.project.news.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class HomeFragment : Fragment(), NewsItemClicked {
    lateinit var binding :FragmentHomeBinding
    val vm :NewsViewModel by viewModels()
    lateinit var mainViewModel: MainViewModel
    lateinit var adapter: NewsRvAdapter
    lateinit var countries: Array<String>
    lateinit var categories: Array<String>
    var lastSelectedCountry = "in"
    var lastSelectedCategory = "all"

    private lateinit var appDb : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeCountryFilter.setText("India")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setData()

        adapters()

        observers()

        getData()

        listener()

    }

    fun setData() {
        categories = resources.getStringArray(R.array.news_categories)
        countries = resources.getStringArray(R.array.news_countries)
        appDb = AppDatabase.getDatabaseInstance(activity as MainActivity)
    }

    fun getData() {
        vm.getNews(lastSelectedCountry,lastSelectedCategory)
    }

    fun adapters() {
        val countriesShow = arrayListOf<String>()
        countries.forEach {
            countriesShow.add(it.dropLast(5))
        }
        addCategoryChips(categories)
        binding.homeCountryFilter.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, countriesShow)
        )

        adapter = NewsRvAdapter(requireContext(), arrayListOf(),binding.homeNewsRv,this)
        binding.homeNewsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.homeNewsRv.adapter = adapter
    }

    fun observers() {
        vm.newsResponse.observe(viewLifecycleOwner){
            adapter.replaceData(it)
        }

        vm.newsErrorResponse.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    fun listener() {
        binding.homeCountryFilter.setOnItemClickListener { parent, view, position, id ->
            lastSelectedCountry = countries[position].substringAfter("-").replace(" ","")
            getData()
        }

        binding.homeBookmarks.setOnClickListener {
            mainViewModel.callFragment(HomeBookmarksFragment(),Bundle())
        }
    }

    override fun newsClicked(item: News) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
        startActivity(browserIntent)
    }

    override fun addToBookmark(item: News) {
        CoroutineScope(Dispatchers.IO).launch {
            appDb.bookmarksDao().addToBookmarks(Bookmark(item.url, item.title, item.urlToImage))
        }
    }

    override fun removeFromBookmarkClicked(item: News) {
        CoroutineScope(Dispatchers.IO).launch {
            appDb.bookmarksDao().deleteFromBookmarks(Bookmark(item.url, item.title, item.urlToImage))
        }
    }

    override fun shareClicked(item: News) {
        shareText(item.url)
    }

    fun shareText(body: String?) {
        val txtIntent = Intent(Intent.ACTION_SEND)
        txtIntent.type = "text/plain"
        txtIntent.putExtra(Intent.EXTRA_SUBJECT, "Share this news through...")
        txtIntent.putExtra(Intent.EXTRA_TEXT, body)
        startActivity(Intent.createChooser(txtIntent, "Share"))
    }

    var lastSelectedChip : Chip? = null
    fun addCategoryChips(data: Array<String>) {
        binding.homeCategoriesChipGroup.removeAllViews()
        data.forEachIndexed { index, it ->
            val chip = Chip(requireContext())
            chip.text = it
            chip.setChipBackgroundColorResource(R.color.light_grey)
            if(index == 0) {
                lastSelectedChip = chip
                chip.setChipBackgroundColorResource(R.color.teal_200)
            }
            chip.setOnClickListener { view ->
                lastSelectedChip?.setChipBackgroundColorResource(R.color.light_grey)
                lastSelectedChip = chip
                chip.setChipBackgroundColorResource(R.color.teal_200)
                lastSelectedCategory = it
                getData()
            }
            binding.homeCategoriesChipGroup.addView(chip)
        }
    }

}