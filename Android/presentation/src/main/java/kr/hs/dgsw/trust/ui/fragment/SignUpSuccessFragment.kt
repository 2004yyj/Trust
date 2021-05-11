package kr.hs.dgsw.trust.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kr.hs.dgsw.trust.R

class SignUpSuccessFragment : Fragment() {


    private val navController: NavController by lazy {
        findNavController()
    }

    private lateinit var signUp: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

        signUp.setOnClickListener {
            navigateToLogin()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateToLogin()
        }
    }

    private fun init(view: View) {
        signUp = view.findViewById(R.id.btn_confirm_signUpSuccess)
    }

    private fun navigateToLogin() {
        navController.navigateUp()
    }
}