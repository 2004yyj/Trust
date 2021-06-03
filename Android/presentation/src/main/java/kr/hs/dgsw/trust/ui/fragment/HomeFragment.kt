package kr.hs.dgsw.trust.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.domain.usecase.liked.DeleteLikedUseCase
import kr.hs.dgsw.domain.usecase.liked.PostLikedUseCase
import kr.hs.dgsw.domain.usecase.post.GetAllPostUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentHomeBinding
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.ui.adapter.PostAdapter
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
    private val recyclerAdapter: PostAdapter by lazy { PostAdapter(recyclerViewModel) }
    private val recyclerView: RecyclerView by lazy { binding.rvPostHome }

    private val toolbar: Toolbar by lazy { binding.toolbarHome }

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        viewModel.getAllPost()

        viewModel.isFailure.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        val appbarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.boardFragment,
                R.id.chatFragment,
                R.id.userInfoFragment
            ), null
        )
        NavigationUI.setupWithNavController(toolbar, navController, appbarConfiguration)
    }

    private fun init() {
        recyclerView.adapter = recyclerAdapter
        recyclerAdapter.onClick.observe(viewLifecycleOwner) {
            commentDialogOpen(it)
        }
    }

    private fun observe() {
        viewModel.postList.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it)
        }

        recyclerViewModel.isFailure
    }

    private fun commentDialogOpen(postId: Int) {
        val fm = requireActivity().supportFragmentManager
        CommentFragment.newInstance(postId).showNow(fm, "comment")
    }
}