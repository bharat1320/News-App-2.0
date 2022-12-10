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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.news.News
import com.project.news.R
import com.project.news.databinding.FragmentHomeBinding
import com.project.news.ui.home.adapters.NewsItemClicked
import com.project.news.ui.home.adapters.NewsRvAdapter
import com.project.news.viewModel.NewsViewModel


class HomeFragment : Fragment(), NewsItemClicked {
    lateinit var binding :FragmentHomeBinding
    lateinit var vm :NewsViewModel
    lateinit var adapter: NewsRvAdapter
    lateinit var countries: Array<String>
    lateinit var categories: Array<String>
    var lastSelectedCountry = "in"
    var lastSelectedCategory = "all"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeCountryFilter.setText("India")
        binding.homeCategoriesFilter.setText("All")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this)[NewsViewModel::class.java]
        categories = resources.getStringArray(R.array.news_categories)
        countries = resources.getStringArray(R.array.news_countries)

        adapters()

        observers()

        getData()

        listener()

    }

    fun getData() {
        vm.repository.getNews(lastSelectedCountry,lastSelectedCategory)
    }

    fun adapters() {
        val countriesShow = arrayListOf<String>()
        countries.forEach {
            countriesShow.add(it.dropLast(5))
        }

        binding.homeCountryFilter.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, countriesShow))
        binding.homeCategoriesFilter.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories))

        adapter = NewsRvAdapter(requireContext(), arrayListOf(),binding.homeNews,this)
        binding.homeNews.layoutManager = LinearLayoutManager(requireContext())
        binding.homeNews.adapter = adapter
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

        binding.homeCategoriesFilter.setOnItemClickListener { parent, view, position, id ->
            lastSelectedCategory = categories[position]
            getData()
        }
    }

    override fun newsClicked(item: News) {
//        val toast = Toast(requireContext())
//        val view = ImageView(requireContext())
//        Glide.with(requireContext()).load(item.urlToImage).into(view)
//        toast.setView(view)
//        toast.show()
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
        startActivity(browserIntent)
    }

}