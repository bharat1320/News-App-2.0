package com.project.news.ui.home.bookmarks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.project.news.data.Bookmark
import com.project.news.data.News
import com.project.news.database.AppDatabase
import com.project.news.databinding.FragmentHomeBookmarksBinding
import com.project.news.ui.MainActivity
import com.project.news.ui.home.adapters.NewsItemClicked
import com.project.news.ui.home.adapters.NewsRvAdapter
import com.project.news.viewModel.MainViewModel
import com.project.news.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeBookmarksFragment : Fragment(), NewsItemClicked {
    lateinit var binding : FragmentHomeBookmarksBinding
//    var vm : NewsViewModel by viewModels()
//    var mainViewModel: MainViewModel by viewModels()
    lateinit var adapter: NewsRvAdapter

    private lateinit var appDb : AppDatabase
    var newsData : MutableLiveData<List<Bookmark>> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDb = AppDatabase.getDatabaseInstance(activity as MainActivity)

        adapters()

        observers()

        getData()

        listener()

    }

    fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            val bookmarksData = appDb.bookmarksDao().getAllBookmarks()
            newsData.postValue(bookmarksData)
        }
    }

    fun adapters() {
//        adapter = NewsRvAdapter(requireContext(), arrayListOf(),binding.homeBookmarksNewsRv,this,true)
//        binding.homeBookmarksNewsRv.layoutManager = LinearLayoutManager(requireContext())
//        binding.homeBookmarksNewsRv.adapter = adapter
    }

    fun observers() {
//        vm.newsResponse.observe(viewLifecycleOwner){
//            adapter.replaceData(it)
//        }
//
//        vm.newsErrorResponse.observe(viewLifecycleOwner){
//            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//        }

        newsData.observe(viewLifecycleOwner){ bookmarksData->
            val data = arrayListOf<News>()
            bookmarksData.forEach {
                data.add(News(it.title,it.url,it.urlToImage))
            }
            adapter.replaceData(data)
        }
    }

    fun listener() {
        binding.homeBookmarksBack.setOnClickListener {
//            mainViewModel.backPressed()
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

}
