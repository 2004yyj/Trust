package kr.hs.dgsw.trust.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentSignUpUserInfoBinding
import kr.hs.dgsw.trust.viewmodel.fragment.SignUpViewModel

class SignUpUserInfoFragment : Fragment() {

    private lateinit var viewModel : SignUpViewModel

    private lateinit var binding : FragmentSignUpUserInfoBinding

    private val navController: NavController by lazy {
        findNavController()
    }

    private lateinit var btnSignUp : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_user_info, container, false)

        binding.vm = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        btnSignUp.setOnClickListener {
            viewModel.signUp()
            navController.navigate(R.id.action_signUpUserInfoFragment_to_signUpSuccessFragment)
        }
    }

    private fun init() {
        btnSignUp = binding.btnSignUpSignUp
    }
}