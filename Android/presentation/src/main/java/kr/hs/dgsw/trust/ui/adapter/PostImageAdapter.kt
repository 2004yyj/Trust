package kr.hs.dgsw.trust.ui.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.domain.entity.Image
import kr.hs.dgsw.trust.databinding.ItemPostImageBinding
import kr.hs.dgsw.trust.databinding.ItemUpdatePostImageBinding

class PostImageAdapter : ListAdapter<Image, RecyclerView.ViewHolder>(diffUtil) {
    val deleteUriImage = MutableLiveData<Uri>()
    val deleteStringImage = MutableLiveData<String>()


    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).type == "URI") {
            URI_IMAGE
        } else if (getItem(position).type == "STRING") {
            STRING_IMAGE
        } else {
            throw Exception()
        }

    }

    inner class UriImageViewHolder(
        private val binding: ItemPostImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imagePath: String, position: Int) {
            binding.imagePath = imagePath

            binding.ibDeleteItemImage.setOnClickListener {
                val curList = ArrayList<Image>()
                currentList.forEach {
                    curList.add(it)
                }
                curList.removeAt(position)
                submitList(curList)

                Log.d("PostImageAdapter", "bind: $imagePath")

                deleteUriImage.value = imagePath.toUri()
            }
        }
    }

    inner class StringImageViewHolder(
        private val binding: ItemUpdatePostImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imagePath: String, position: Int) {
            binding.imagePath = imagePath

            binding.ibDeleteItemImage.setOnClickListener {
                val curList = ArrayList<Image>()
                currentList.forEach {
                    curList.add(it)
                }
                curList.removeAt(position)
                submitList(curList)
                deleteStringImage.value = imagePath
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == URI_IMAGE) {
            UriImageViewHolder(ItemPostImageBinding.inflate(inflater, parent, false))
        } else if (viewType == STRING_IMAGE) {
            StringImageViewHolder(ItemUpdatePostImageBinding.inflate(inflater, parent, false))
        } else {
            throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder , position: Int) {

        if (holder is UriImageViewHolder) {
            holder.bind(getItem(position).imagePath, position)
        } else if (holder is StringImageViewHolder) {
            holder.bind(getItem(position).imagePath, position)
        }
    }

    companion object {
        private const val URI_IMAGE = 0
        private const val STRING_IMAGE = 1

        private val diffUtil = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return false
            }
        }
    }
}