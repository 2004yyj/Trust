package kr.hs.dgsw.trust.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.ItemPostBinding
import kr.hs.dgsw.trust.ui.viewmodel.adapter.ItemPostViewModel

class PostAdapter : ListAdapter<Post, PostAdapter.PostViewHolder>(diffUtil) {

    val onClick = MutableLiveData<Int>()

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ItemPostViewModel()

        fun bind(post: Post) {

            with(viewModel) {
                id.value = post.id
                name.value = post.account.name
                content.value = post.content
                createdAt.value = post.createdAt
                profileImagePath.value = post.account.profileImage

            }

            binding.vm = viewModel
            binding.btnCommentPost.setOnClickListener {
                onClick.postValue(post.id)
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
        holder.bind(getItem(position))
    }


}