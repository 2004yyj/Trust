package kr.hs.dgsw.trust.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.hs.dgsw.data.util.ServerAddress.ADDR_IMG
import kr.hs.dgsw.domain.entity.Comment
import kr.hs.dgsw.trust.databinding.ItemCommentBinding

class CommentAdapter : ListAdapter<Comment, CommentAdapter.ViewHolder>(DiffUtil) {

    companion object {
        val DiffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return false
            }
        }
    }

    inner class ViewHolder(private val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) = with(binding) {
            binding.comment = comment
            Glide
                .with(root.context)
                .load(ADDR_IMG+comment.account.profileImage)
                .into(ivProfileImagePost)

            val postImageAdapter = PostImageAdapter()

            if (rvImageListComment.adapter == null) {
                rvImageListComment.adapter = postImageAdapter
                val pagerSnapHelper = PagerSnapHelper()
                pagerSnapHelper.attachToRecyclerView(rvImageListComment)
            }
            postImageAdapter.submitList(comment.imageList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemCommentBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}