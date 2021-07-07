package kr.hs.dgsw.trust.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.domain.entity.Comment
import kr.hs.dgsw.trust.databinding.ItemCommentBinding
import kr.hs.dgsw.trust.ui.util.PreferenceHelper.username

class CommentAdapter : ListAdapter<Comment, CommentAdapter.ViewHolder>(DiffUtil) {

    val deleteComment = MutableLiveData<Int>()

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

            val postImageAdapter = PostImageAdapter()

            if (rvImageListComment.adapter == null) {
                rvImageListComment.adapter = postImageAdapter
                val pagerSnapHelper = PagerSnapHelper()
                pagerSnapHelper.attachToRecyclerView(rvImageListComment)
            }

            if (comment.account.username == username) {
                btnDeleteComment.visibility = View.VISIBLE
            }
            btnDeleteComment.setOnClickListener {
                deleteComment.value = comment.id
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