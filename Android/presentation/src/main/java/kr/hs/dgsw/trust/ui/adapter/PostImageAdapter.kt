package kr.hs.dgsw.trust.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.hs.dgsw.data.util.ServerAddress.ADDR_IMG
import kr.hs.dgsw.trust.databinding.ItemPostImageBinding

class PostImageAdapter : ListAdapter<String, PostImageAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(
        private val binding: ItemPostImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imagePath: String) = with(binding) {
            Glide
                .with(root.context)
                .load(ADDR_IMG+imagePath)
                .into(ivImageListItemPost)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemPostImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}