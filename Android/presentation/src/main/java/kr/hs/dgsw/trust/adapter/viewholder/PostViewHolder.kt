package kr.hs.dgsw.trust.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.trust.adapter.PostAdapter.OnClickCommentPost
import kr.hs.dgsw.trust.databinding.ItemPostBinding
import java.text.SimpleDateFormat
import java.util.*

class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post, onClickCommentPost: OnClickCommentPost) {
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)

        binding.run {
            tvNamePost.text = post.account.name
            tvCreatedAtPost.text = sdf.format(Date(post.createdAt))
            btnCommentPost.setOnClickListener {
                onClickCommentPost.onClick(post.id)
            }
        }
    }
}