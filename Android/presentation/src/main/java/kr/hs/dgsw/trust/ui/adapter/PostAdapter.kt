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
import kr.hs.dgsw.trust.ui.util.binding.PreferenceHelper
import kr.hs.dgsw.trust.ui.viewmodel.adapter.ItemPostViewModel

class PostAdapter(private val viewModel: ItemPostViewModel) : ListAdapter<Post, PostAdapter.PostViewHolder>(diffUtil) {

    val onClick = MutableLiveData<Int>()

    inner class PostViewHolder(private val binding: ItemPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {

            with(viewModel) {
                id.set(post.id)
                name.set(post.account.name)
                content.set(post.content)
                createdAt.set(post.createdAt)
                profileImagePath.set(post.account.profileImage)
                isCheckedLiked.set(containLiked(post.likedList))

                with(binding) {
                    vm = viewModel
                    btnCommentPost.setOnClickListener { onClick.postValue(post.id) }

                    chkLikePost.setOnClickListener {
                        val liked = viewModel.isCheckedLiked.get()
                        if (liked == true) {
                            if (getPassword() != null) { // 토큰 기능이 완성되면 현재 토큰이 활성화되어 있는지 확인한다.
                                viewModel.postLiked(post.id, post.account.username, getPassword()!!)
                            } else {
                                viewModel.isFailure.value = "세션이 만료되었습니다. 다시 로그인 해주세요."
                            }
                        } else {
                            viewModel.deleteLiked(post.id, post.account.username, getPassword()!!)
                        }
                    }
                }

            }

        }

        private fun containLiked(list: List<String>): Boolean {
            val username = PreferenceHelper.getUsername(binding.chkLikePost.context)
            return list.contains(username)
        }

        private fun getPassword(): String? {
            return PreferenceHelper.getPassword(binding.chkLikePost.context)
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

    override fun onViewDetachedFromWindow(holder: PostViewHolder) {
        super.onViewDetachedFromWindow(holder)
    }

}