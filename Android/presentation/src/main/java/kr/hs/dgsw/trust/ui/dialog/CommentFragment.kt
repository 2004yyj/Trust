package kr.hs.dgsw.trust.ui.dialog

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.comment.DeleteCommentUseCase
import kr.hs.dgsw.domain.usecase.comment.GetAllCommentUseCase
import kr.hs.dgsw.domain.usecase.comment.PostCommentUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentCommentBinding
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.function.asMultipart
import kr.hs.dgsw.trust.ui.adapter.CommentAdapter
import kr.hs.dgsw.trust.ui.adapter.CommentPostImageAdapter
import kr.hs.dgsw.trust.ui.decoration.SpaceDecoration
import kr.hs.dgsw.trust.ui.util.setOnTransitionCompletedListener
import kr.hs.dgsw.trust.ui.viewmodel.factory.CommentViewModelFactory
import kr.hs.dgsw.trust.ui.viewmodel.fragment.CommentViewModel
import javax.inject.Inject

class CommentFragment : DialogFragment() {

    @Inject
    lateinit var getAllCommentUseCase: GetAllCommentUseCase

    @Inject
    lateinit var postCommentUseCase: PostCommentUseCase

    @Inject
    lateinit var deleteCommentUseCase: DeleteCommentUseCase

    private lateinit var viewModel: CommentViewModel
    private lateinit var motionLayout: MotionLayout
    private lateinit var binding: FragmentCommentBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private val recyclerAdapter: CommentAdapter by lazy { CommentAdapter() }
    private val recyclerImageAdapter: CommentPostImageAdapter by lazy { CommentPostImageAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        (requireActivity().application as MyDaggerApplication).daggerComponent.inject(this)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment, container, false)
        viewModel =
            ViewModelProvider(
            this,
            CommentViewModelFactory(getAllCommentUseCase, postCommentUseCase, deleteCommentUseCase))[CommentViewModel::class.java]
        binding.vm = viewModel

        viewModel.postId.value = requireArguments().getInt("postId")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observe()

        motionLayout = view.findViewById(R.id.motionLayout_comment)
        motionLayout.setOnTransitionCompletedListener { _, _ ->
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                dismiss()
            }, 10)
        }
    }

    private fun init() = with(binding) {
        val size = resources.getDimensionPixelSize(R.dimen.MY_SIZE)
        val deco = SpaceDecoration(size)
        rvCommentPostImageComment.adapter = recyclerImageAdapter
        rvCommentPostImageComment.addItemDecoration(deco)

        rvCommentListComment.adapter = recyclerAdapter

        recyclerAdapter.deleteComment.observe(viewLifecycleOwner) {
            viewModel.deleteComment(it)
        }

        etCommentPostComment.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                submitComment()
            }
            true
        }

        btnCommentPostComment.setOnClickListener {
            submitComment()
        }

        btnCommentImagePostComment.setOnClickListener {
            activityResultLauncher.launch("image/*")
        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                val list = ArrayList<Uri>()
                if (recyclerImageAdapter.currentList.isNotEmpty()) {
                    list.addAll(recyclerImageAdapter.currentList)
                }
                list.add(it)
                imageListChanged(list)
                recyclerImageAdapter.submitList(list)
            }
        }
    }

    private fun submitComment(): Unit = with(viewModel) {
        val text = postCommentText.value
        if (!text.isNullOrBlank()) {
            postComment(postCommentText.value!!)
            clearPostComment()
        }
    }

    private fun imageListChanged(list: ArrayList<Uri>) = with(viewModel) {
        postImageList.clear()
        list.forEach { uri ->
            with(requireActivity()) {
                val multipartBody = uri.asMultipart("imageList", cacheDir, contentResolver)!!
                postImageList.add(multipartBody)
            }
        }
    }

    private fun clearPostComment() = with(viewModel) {
        postCommentText.value = ""
        postImageList.clear()
        with(binding) {
            etCommentPostComment.setText("")
            recyclerImageAdapter.clearList()
        }
    }

    private fun observe() = with(viewModel) {
        postId.observe(this@CommentFragment) {
            getAllComment(it)
        }
        commentList.observe(this@CommentFragment) {
            recyclerAdapter.submitList(it)
        }
        postCommentText.observe(this@CommentFragment) {
            binding.btnCommentPostComment.visibility =
                if (!it.isNullOrBlank()) VISIBLE else GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(postId: Int) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                    putInt("postId", postId)
                }
            }
    }
}