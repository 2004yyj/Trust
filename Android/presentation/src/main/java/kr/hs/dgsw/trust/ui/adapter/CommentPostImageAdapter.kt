package kr.hs.dgsw.trust.ui.adapter

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.hs.dgsw.trust.databinding.ItemCommentPostImageBinding

class CommentPostImageAdapter : ListAdapter<Uri, CommentPostImageAdapter.ViewHolder>(diffUtil) {

    fun clearList() {
        submitList(null)
    }

    inner class ViewHolder(
        private val binding: ItemCommentPostImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imagePath: Uri) = with(binding) {
            val inputStream = root.context.contentResolver.openInputStream(imagePath)
            val image = BitmapFactory.decodeStream(inputStream)

            Glide
                .with(root.context)
                .load(image)
                .into(ivImageListItemCommentPost)
            inputStream?.close()

            btnClearItemCommentPost.setOnClickListener {
                val list = ArrayList<Uri>()
                list.addAll(currentList)
                list.remove(imagePath)
                submitList(list)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemCommentPostImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }
        }
    }
}