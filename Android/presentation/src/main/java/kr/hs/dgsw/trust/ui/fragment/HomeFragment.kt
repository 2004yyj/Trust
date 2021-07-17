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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kr.hs.dgsw.domain.usecase.liked.DeleteLikedUseCase
import kr.hs.dgsw.domain.usecase.liked.PostLikedUseCase
import kr.hs.dgsw.domain.usecase.post.DeletePostUseCase
import kr.hs.dgsw.domain.usecase.post.GetAllPostUseCase
import kr.hs.dgsw.domain.usecase.post.UpdatePostUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentHomeBinding
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.ui.adapter.PostAdapter
import kr.hs.dgsw.trust.ui.dialog.CommentFragment.Companion.newInstance
import kr.hs.dgsw.trust.ui.viewmodel.factory.PostViewModelFactory
import kr.hs.dgsw.trust.ui.viewmodel.fragment.PostViewModel
import javax.inject.Inject

class HomeFragment : Fragment() {

    private val navController: NavController by lazy {
        findNavController()
    }

    @Inject
    lateinit var getAllPostUseCase: GetAllPostUseCase

    @Inject
    lateinit var postLikedUseCase: PostLikedUseCase

    @Inject
    lateinit var deleteLikedUseCase: DeleteLikedUseCase

    @Inject
    lateinit var deletePostUseCase: DeletePostUseCase

    @Inject
    lateinit var updatePostUseCase: UpdatePostUseCase

    private lateinit var viewModel: PostViewModel

    private lateinit var binding: FragmentHomeBinding

    private val recyclerAdapter: PostAdapter by lazy { PostAdapter(postLikedUseCase, deleteLikedUseCase) }

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HomeFragment", "onCreateView: ")
        val application = requireActivity().application
        (application as MyDaggerApplication).daggerComponent.inject(this)

        viewModel = ViewModelProvider(this, PostViewModelFactory(getAllPostUseCase, updatePostUseCase, deletePostUseCase))[PostViewModel::class.java]
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
        recyclerView = binding.rvPostHome
        swipeRefreshLayout = binding.swipeRefreshLayoutHome
        fabAdd = binding.fabAddHome

        recyclerView.adapter = recyclerAdapter
    }

    private fun observe() {
        swipeRefreshLayout.setOnRefreshListener {
            setPost()
            swipeRefreshLayout.isRefreshing = false
        }
        fabAdd.setOnClickListener {
            navigateHomeToAdd()
        }

        viewModel.postList.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it)
        }
        viewModel.isFailure.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        recyclerAdapter.onClick.observe(viewLifecycleOwner) {
            commentDialogOpen(it)
        }
        recyclerAdapter.onDeleteClick.observe(viewLifecycleOwner) {
            viewModel.deletePost(it)
        }

    }

    private fun navigateHomeToAdd() {
        navController.navigate(R.id.action_homeFragment_to_addFragment)
    }

    private fun setPost() {
        viewModel.getAllPost()
    }

    private fun commentDialogOpen(postId: Int) {
        val fm = requireActivity().supportFragmentManager
        newInstance(postId).showNow(fm, "comment")
    }
}