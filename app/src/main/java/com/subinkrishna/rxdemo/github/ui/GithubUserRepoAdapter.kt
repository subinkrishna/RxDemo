package com.subinkrishna.rxdemo.github.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.subinkrishna.rxdemo.R
import com.subinkrishna.rxdemo.ext.setTextOrHide
import com.subinkrishna.rxdemo.github.Repo
import com.subinkrishna.rxdemo.github.User

/**
 * [RecyclerView.Adapter] implementation for user repo
 *
 * @author Subinkrishna Gopi
 */
class GithubUserRepoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        // View types
        private const val VIEW_HEADER = 0
        private const val VIEW_REPO = 1
    }

    private var user: User? = null
    private var repos: List<Repo>? = null

    fun update(user: User?, repos: List<Repo>?) {
        this.user = user
        this.repos = repos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_HEADER -> {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_github_user_profile_header, parent, false)
                HeaderViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_github_user_profile_repo, parent, false)
                RepoViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.name.text = user?.name ?: user?.username
                holder.username.text = "@${user?.username}"
                holder.bio.setTextOrHide(user?.bio)
                Picasso.get().load(user?.avatarUrl)
                        .fit().centerCrop()
                        .placeholder(R.drawable.ic_profile_placeholder_nodpi)
                        .error(R.drawable.ic_profile_placeholder_nodpi)
                        .into(holder.avatar)
            }
            is RepoViewHolder -> {
                val repo = repos?.get(position - if (null == user) 0 else 1)
                holder.name.text = repo?.name
                holder.description.setTextOrHide(repo?.description)
            }
        }
    }

    override fun getItemCount(): Int {
        var count = if (null != user) 1 else 0
        count += repos?.size ?: 0
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_HEADER else VIEW_REPO
    }
}

/** Header ViewHolder */
class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val avatar: ImageView = itemView.findViewById(R.id.avatar)
    val name: TextView = itemView.findViewById(R.id.name)
    val username: TextView = itemView.findViewById(R.id.username)
    val bio: TextView = itemView.findViewById(R.id.bio)
}

/** Item ViewHolder */
class RepoViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val name: TextView = itemView.findViewById(R.id.name)
    val description: TextView = itemView.findViewById(R.id.description)
}