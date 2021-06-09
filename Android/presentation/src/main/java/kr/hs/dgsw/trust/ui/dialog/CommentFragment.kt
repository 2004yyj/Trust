package kr.hs.dgsw.trust.ui.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.domain.usecase.comment.GetAllCommentUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentCommentBinding
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.ui.adapter.CommentAdapter
import kr.hs.dgsw.trust.ui.util.setOnTransitionCompletedListener
import kr.hs.dgsw.trust.ui.viewmodel.factory.CommentViewModelFactory
import kr.hs.dgsw.trust.ui.viewmodel.fragment.CommentViewModel
import javax.inject.Inject

class CommentFragment : DialogFragment() {

    @Inject
    lateinit var getAllCommentUseCase: GetAllCommentUseCase

    private lateinit var viewModel: CommentViewModel
    private lateinit var motionLayout: MotionLayout
    private lateinit var binding: FragmentCommentBinding
    private val recyclerAdapter: CommentAdapter by lazy { CommentAdapter() }
    private val recyclerView: RecyclerView by lazy { binding.rvCommentListComment }

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
        viewModel = ViewModelProvider(this, CommentViewModelFactory(getAllCommentUseCase))[CommentViewModel::class.java]
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

    private fun init() {
        recyclerView.adapter = recyclerAdapter
    }

    private fun observe() {
        viewModel.postId.observe(this) {
            viewModel.getAllComment(it)
        }
        viewModel.commentList.observe(this) {
            recyclerAdapter.submitList(it)
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