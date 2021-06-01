package kr.hs.dgsw.trust.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.ui.viewmodel.fragment.CommentViewModel

class CommentFragment : DialogFragment() {

    private lateinit var viewModel: CommentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(this)[CommentViewModel::class.java]

        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        viewModel.postId.value = requireArguments().getInt("postId")
    }

    private fun observe() {
        viewModel.postId.observe(this) {
            viewModel.comment(it)
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