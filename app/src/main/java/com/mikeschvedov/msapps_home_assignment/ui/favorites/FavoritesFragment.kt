package com.mikeschvedov.msapps_home_assignment.ui.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikeschvedov.msapps_home_assignment.R
import com.mikeschvedov.msapps_home_assignment.data.models.Article
import com.mikeschvedov.msapps_home_assignment.databinding.FragmentFavoritesBinding
import com.mikeschvedov.msapps_home_assignment.ui.welcome.WelcomeRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoritesFragment : Fragment(), WelcomeRecyclerAdapter.OnArticleClickListener,
    WelcomeRecyclerAdapter.OnFavoriteClickListener {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoritesViewModel: FavoritesViewModel
    private val adapter: WelcomeRecyclerAdapter by lazy { WelcomeRecyclerAdapter(this, this, isFavoritesList = true) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /* ViewModel */
        favoritesViewModel =
            ViewModelProvider(this).get(FavoritesViewModel::class.java)

        /* Binding */
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*  Setting up the Recycler Adapter */
        binding.favoritesRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesRecyclerview.adapter = adapter

        /* Observe Adapter Changes */
        checkIfAdapterIsEmpty()

        /* On Click Listeners */
        setOnClickListeners()

        /* Observe the LiveData in the ViewModel */
        observeLiveData()

        /* First time fetching of favorites */
        favoritesViewModel.fetchAllFavorites()

        return root
    }

    private fun setOnClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_favorites_to_nav_welcome)
        }
    }

    private fun checkIfAdapterIsEmpty() {
        /*Checking if favorites list is empty */
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()

                if (adapter.itemCount == 0) {
                    binding.noItemsTextView.visibility = View.VISIBLE
                    binding.favoritesRecyclerview.visibility = View.GONE
                } else {
                    binding.noItemsTextView.visibility = View.GONE
                    binding.favoritesRecyclerview.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeLiveData() {
        // Observe articles livedata
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            // Update the adapter when the LiveData changes
            adapter.articleList = favorites
            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged()
        }

        // Observe error livedata
        favoritesViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            // Display error message to the user
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onArticleClicked(article: Article) {
        // Handle action to open URL in browser
        openUrlInBrowser(article.url)
    }

    // Method to open URL in browser
    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onFavoriteClicked(article: Article) {
        favoritesViewModel.removeArticleFromFavorites(article)
        // Update the recycler list
        favoritesViewModel.fetchAllFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}