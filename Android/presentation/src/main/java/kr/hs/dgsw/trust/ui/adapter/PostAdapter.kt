package kr.hs.dgsw.trust.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.ui.adapter.viewholder.PostViewHolder

class PostAdapter : ListAdapter<Post, PostViewHolder>(diffUtil) {

    interface OnClickCommentListener {
        fun onClick(postId: Int)
    }

    private lateinit var onClickCommentListener: OnClickCommentListener

    fun setOnClickCommentListener(listener: (Int) -> Unit) {
        onClickCommentListener = object : OnClickCommentListener {
            override fun onClick(postId: Int) {
                listener(postId)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return false
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PostViewHolder(DataBindingUtil.inflate(inflater, R.layout.item_post, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position), onClickCommentListener)
    }


}