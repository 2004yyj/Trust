package kr.hs.dgsw.trust.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.usecase.liked.DeleteLikedUseCase
import kr.hs.dgsw.domain.usecase.liked.PostLikedUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.ItemPostBinding
import kr.hs.dgsw.trust.ui.viewmodel.adapter.ItemPostViewModel


class PostAdapter(
    private val postLikedUseCase: PostLikedUseCase,
    private val deleteLikedUseCase: DeleteLikedUseCase,
) : ListAdapter<Post, PostAdapter.PostViewHolder>(diffUtil) {

    private lateinit var onClickPostListener: OnClickPostListener
    private lateinit var onClickDeletePostListener: OnClickDeletePostListener
    private lateinit var onClickUpdatePostListener: OnClickUpdatePostListener

    interface OnClickPostListener {
        fun onClick(id: Int)
    }

    interface OnClickDeletePostListener {
        fun onClick(id: Int)
    }

    interface OnClickUpdatePostListener {
        fun onClick(id: Int)
    }

    fun setOnClickPostListener(listener: (Int) -> Unit) {
        onClickPostListener = object : OnClickPostListener {
            override fun onClick(id: Int) {
                listener(id)
            }

        }
    }

    fun setOnClickDeletePostListener(listener: (Int) -> Unit) {
        onClickDeletePostListener = object : OnClickDeletePostListener {
            override fun onClick(id: Int) {
                listener(id)
            }

        }
    }

    fun setOnClickUpdatePostListener(listener: (Int) -> Unit) {
        onClickUpdatePostListener = object : OnClickUpdatePostListener {
            override fun onClick(id: Int) {
                listener(id)
            }

        }
    }

    inner class PostViewHolder(
        private val binding: ItemPostBinding,
        private val viewModel: ItemPostViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        private val postImageAdapter = ImageAdapter()

        init {
            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(binding.rvContentImage)
            binding.rvContentImage.adapter = postImageAdapter
        }

        fun bind(post: Post) = with(viewModel) {

            val imagePath = post.account.profileImage

            id.set(post.id)
            content.set(post.content)
            name.set(post.account.name)
            createdAt.set(post.createdAt)
            profileImagePath.set(imagePath)
            likedSize.set(post.likedSize)
            likedString.set("좋아요 ${post.likedSize}명")
            admin.set(post.admin)
            imagePathList.addAll(post.imageList)

            with(binding) {
                vm = viewModel
                postImageAdapter.submitList(post.imageList)

                btnCommentPost.setOnClickListener { onClickPostListener.onClick(post.id) }

                chkLikePost.isChecked = post.isChecked
                chkLikePost.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.postLiked(post.id)
                    } else {
                        viewModel.deleteLiked(post.id)
                    }
                }

                btnMenuPost.setOnClickListener {
                    val popupMenu = PopupMenu(binding.root.context, btnMenuPost)
                    popupMenu.inflate(R.menu.admin)
                    popupMenu.setOnMenuItemClickListener {
                        when(it.itemId) {
                            R.id.update_post -> {
                                onClickUpdatePostListener.onClick(post.id)
                            }
                            R.id.delete_post -> {
                                onClickDeletePostListener.onClick(post.id)
                            }
                        }
                        return@setOnMenuItemClickListener true
                    }
                    popupMenu.show()
                }
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