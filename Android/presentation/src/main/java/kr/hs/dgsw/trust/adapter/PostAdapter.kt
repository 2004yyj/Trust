package kr.hs.dgsw.trust.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.hs.dgsw.data.entity.PostResponse
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.adapter.viewholder.PostViewHolder

class PostAdapter() : ListAdapter<Post, PostViewHolder>(diffUtil) {

    interface OnClickCommentPost {
        fun onClick(postId: Int)
    }

    private lateinit var onClickCommentPost: OnClickCommentPost

    fun setOnClickCommentPost(listener: (Int) -> Unit) {
        onClickCommentPost = object : OnClickCommentPost {
            override fun onClick(postId: Int) {
                listener(postId)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PostViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_post, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(currentList[position], onClickCommentPost)
    }


}