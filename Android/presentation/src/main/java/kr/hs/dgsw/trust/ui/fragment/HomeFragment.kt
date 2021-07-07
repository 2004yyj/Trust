package kr.hs.dgsw.trust.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kr.hs.dgsw.domain.usecase.liked.DeleteLikedUseCase
import kr.hs.dgsw.domain.usecase.liked.PostLikedUseCase
import kr.hs.dgsw.domain.usecase.post.GetAllPostUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentHomeBinding
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.ui.adapter.PostAdapter
import kr.hs.dgsw.trust.ui.dialog.CommentFragment.Companion.newInstance
import kr.hs.dgsw.trust.ui.viewmodel.adapter.ItemPostViewModel
import kr.hs.dgsw.trust.ui.viewmodel.factory.ItemPostViewModelFactory
import kr.hs.dgsw.trust.ui.viewmodel.factory.PostViewModelFactory
import kr.hs.dgsw.trust.ui.viewmodel.fragment.PostViewModel
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var getAllPostUseCase: GetAllPostUseCase

    @Inject
    lateinit var postLikedUseCase: PostLikedUseCase

    @Inject
    lateinit var deleteLikedUseCase: DeleteLikedUseCase

    private lateinit var viewModel: PostViewModel

    private lateinit var recyclerViewModel: ItemPostViewModel

    private lateinit var binding: FragmentHomeBinding

    private val recyclerAdapter: PostAdapter by lazy { PostAdapter(postLikedUseCase, deleteLikedUseCase) }
    private val recyclerView: RecyclerView by lazy { binding.rvPostHome }
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy { binding.swipeRefreshLayoutHome }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HomeFragment", "onCreateView: ")
        val application = requireActivity().application
        (application as MyDaggerApplication).daggerComponent.inject(this)

        viewModel = ViewModelProvider(this, PostViewModelFactory(getAllPostUseCase))[PostViewModel::class.java]
        recyclerViewModel = ViewModelProvider(this, ItemPostViewModelFactory(postLikedUseCase, deleteLikedUseCase))[ItemPostViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        observe()
        setPost()
    }

    private fun init() {
        recyclerView.adapter = recyclerAdapter
    }

    private fun observe() {
        swipeRefreshLayout.setOnRefreshListener {
            setPost()
            swipeRefreshLayout.isRefreshing = false
        }
        recyclerAdapter.onClick.observe(viewLifecycleOwner) {
            commentDialogOpen(it)
        }
        viewModel.postList.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it)
        }
        viewModel.isFailure.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPost() {
        viewModel.getAllPost()
    }

    private fun commentDialogOpen(postId: Int) {
        val fm = requireActivity().supportFragmentManager
        newInstance(postId).showNow(fm, "comment")
    }
}