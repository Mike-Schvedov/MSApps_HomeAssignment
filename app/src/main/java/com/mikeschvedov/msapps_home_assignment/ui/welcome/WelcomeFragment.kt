package com.mikeschvedov.msapps_home_assignment.ui.welcome

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikeschvedov.msapps_home_assignment.R
import com.mikeschvedov.msapps_home_assignment.data.models.Article
import com.mikeschvedov.msapps_home_assignment.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment(), WelcomeRecyclerAdapter.OnArticleClickListener,
    WelcomeRecyclerAdapter.OnFavoriteClickListener {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var welcomeViewModel: WelcomeViewModel
    private val adapter: WelcomeRecyclerAdapter by lazy { WelcomeRecyclerAdapter(this, this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /* ViewModel */
        welcomeViewModel =
            ViewModelProvider(this).get(WelcomeViewModel::class.java)

        /* Binding */
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*  Setting up the Recycler Adapter */
        binding.articleRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.articleRecyclerview.adapter = adapter

        /* On Click Listeners */
        setOnClickListeners()

        /* Observe the LiveData in the ViewModel */
        observeLiveData()

        println("this should run")

        return root
    }

    // Implement method from callback
    override fun onArticleClicked(article: Article) {
        // Handle action to open URL in browser
        openUrlInBrowser(article.url)
    }

    // Method to open URL in browser
    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun observeLiveData() {
        // Observe articles livedata
        welcomeViewModel.articles.observe(viewLifecycleOwner) { articles ->
            // Update the adapter when the LiveData changes
            adapter.articleList = articles
            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged()
        }

        // Observe error livedata
        welcomeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            // Display error message to the user
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setOnClickListeners() {
        // Creating a list of all category views
        val categoryButtons = listOf(
            binding.categoryBusiness,
            binding.categoryEntertainment,
            binding.categoryHealth,
            binding.categoryScience,
            binding.categorySports,
            binding.categoryTechnology
        )

        // Setting onClickListeners on all categories
        categoryButtons.forEach { button ->
            button.setOnClickListener {
                // Providing the tag (category name) as a query for the api
                val categoryName = button.tag as String
                welcomeViewModel.fetchArticlesByCategory(categoryName)
            }
        }

        // Navigate to favorites page
        binding.favoritesPageFab.setOnClickListener {
            findNavController().navigate(R.id.action_nav_welcome_to_nav_favorites)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFavoriteClicked(article: Article) {
        println("on favorite clied inside framgent")
        welcomeViewModel.toggleArticleFavoriteStatus(article)
    }
}