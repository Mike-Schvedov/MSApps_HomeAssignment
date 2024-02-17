package com.mikeschvedov.msapps_home_assignment.ui.welcome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mikeschvedov.msapps_home_assignment.R
import com.mikeschvedov.msapps_home_assignment.data.models.Article
import com.mikeschvedov.msapps_home_assignment.utility.Utilities

class WelcomeRecyclerAdapter(
    private val articleCallback: OnArticleClickListener,
    private val favoriteCallback: OnFavoriteClickListener,
    private val isFavoritesList: Boolean = false
) :
    RecyclerView.Adapter<WelcomeRecyclerAdapter.ArticleViewHolder>() {

    var articleList: List<Article> = emptyList()

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var articleTitle: TextView = itemView.findViewById(R.id.article_title_textview)
        var articleDate: TextView = itemView.findViewById(R.id.article_date_textview)
        var articleImage: ImageView = itemView.findViewById(R.id.article_image)
        var articleFavoriteTrigger: ImageView =
            itemView.findViewById(R.id.article_favorite_clickable)

        init {
            // Add click listener to the title
            articleTitle.setOnClickListener {
                val position = adapterPosition
                // Check if position is valid
                if (position != RecyclerView.NO_POSITION) {
                    val article = articleList[position]
                    // Call method in callback interface to handle the action
                    articleCallback.onArticleClicked(article)
                }
            }

            // Add click listener to the favorite button
            articleFavoriteTrigger.setOnClickListener {
                val position = adapterPosition
                // Check if position is valid
                if (position != RecyclerView.NO_POSITION) {
                    val article = articleList[position]
                    // Toggle favorite state
                    article.isFavorite = !article.isFavorite
                    // Update UI
                    notifyItemChanged(position)
                    // Call method in callback interface to handle the action
                    favoriteCallback.onFavoriteClicked(article)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_view_holder, parent, false)
        return ArticleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articleList[position]

        // Set article title
        holder.articleTitle.text = article.title

        // Format and set article published date
        val inputDate = article.published_at
        val formattedDate = Utilities.formatDate(inputDate)

        holder.articleDate.text = formattedDate

        // Set article image
        Glide.with(holder.itemView.context).load(article.image).into(holder.articleImage)

        // Set favorite button drawable based on favorite state
        val favoriteDrawableRes = if (isFavoritesList) {
            R.drawable.ic_baseline_delete_24
        } else {
            if (article.isFavorite) {
                R.drawable.ic_baseline_favorite_24
            } else {
                R.drawable.ic_baseline_favorite_border_24
            }
        }

        holder.articleFavoriteTrigger.setImageResource(favoriteDrawableRes)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    // Article Clicked  interface
    interface OnArticleClickListener {
        fun onArticleClicked(article: Article)
    }

    // Favorite  Clicked  interface
    interface OnFavoriteClickListener {
        fun onFavoriteClicked(article: Article)
    }
}