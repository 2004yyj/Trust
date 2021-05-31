package kr.hs.dgsw.trust.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.trust.databinding.ItemPostBinding
import kr.hs.dgsw.trust.ui.adapter.PostAdapter

class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post, onClickCommentListener: PostAdapter.OnClickCommentListener) {
        binding.post = post
        binding.btnCommentPost.setOnClickListener {
            onClickCommentListener.onClick(post.id)
        }
    }
}