package kr.hs.dgsw.trust.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.adapter.PostAdapter.OnClickCommentPost

class CommentFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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