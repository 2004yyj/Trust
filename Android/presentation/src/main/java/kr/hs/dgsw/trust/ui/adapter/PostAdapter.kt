package kr.hs.dgsw.trust.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.data.util.ServerAddress.ADDR_IMG
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.usecase.liked.DeleteLikedUseCase
import kr.hs.dgsw.domain.usecase.liked.PostLikedUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.ItemPostBinding
import kr.hs.dgsw.trust.ui.viewmodel.adapter.ItemPostViewModel


class PostAdapter(
    private val postLikedUseCase: PostLikedUseCase,
    private val deleteLikedUseCase: DeleteLikedUseCase
) : ListAdapter<Post, PostAdapter.PostViewHolder>(diffUtil) {

    val onClick = MutableLiveData<Int>()

    inner class PostViewHolder(
        private val binding: ItemPostBinding,
        private val viewModel: ItemPostViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) = with(viewModel) {

            val imagePath = ADDR_IMG+post.account.profileImage

            id.set(post.id)
            content.set(post.content)
            name.set(post.account.name)
            createdAt.set(post.createdAt)
            profileImagePath.set(imagePath)
            likedSize.set(post.likedSize)
            likedString.set("좋아요 ${post.likedSize}명")
            imagePathList.addAll(post.imageList)

            with(binding) {
                vm = viewModel

                val postImageAdapter = PostImageAdapter()

                rvContentImage.adapter = postImageAdapter
                postImageAdapter.submitList(post.imageList)

                val pagerSnapHelper = PagerSnapHelper()
                pagerSnapHelper.attachToRecyclerView(rvContentImage)

                btnCommentPost.setOnClickListener { onClick.postValue(post.id) }

                chkLikePost.isChecked = post.isChecked
                chkLikePost.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.postLiked(post.id)
                    } else {
                        viewModel.deleteLiked(post.id)
                    }
                }
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

        return PostViewHolder(
            DataBindingUtil.inflate(
                inflater,
                R.layout.item_post,
                parent,
                false
            ),
            ItemPostViewModel(postLikedUseCase, deleteLikedUseCase)
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}