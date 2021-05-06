package kr.hs.dgsw.trust.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentLoginBinding
import kr.hs.dgsw.trust.ui.activity.MainActivity
import kr.hs.dgsw.trust.viewmodel.fragment.LoginViewModel

class LoginFragment : Fragment() {

    companion object {
        private const val TAG = "LoginFragment"
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding : FragmentLoginBinding

    private lateinit var tilId : TextInputLayout
    private lateinit var tilPw : TextInputLayout
    private lateinit var btnLogin : Button
    private lateinit var btnSignUp : Button

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.vm = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observe()

        btnLogin.setOnClickListener {
            //TODO 서버통신으로 아이디 비밀번호 확인
            viewModel.login()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpUserInfoFragment)
        }
    }

    private fun init() {
        tilId = binding.tilIdLogin
        tilPw = binding.tilPwLogin
        btnLogin = binding.btnLoginLogin
        btnSignUp = binding.btnSignUpLogin
    }

    private fun observe() {
        viewModel.id.observe(viewLifecycleOwner) {
            tilId.error = "아이디를 입력해주세요."
            tilId.isErrorEnabled = it.isEmpty()
        }

        viewModel.pw.observe(viewLifecycleOwner) {
            tilPw.error = "비밀번호를 입력해주세요."
            tilPw.isErrorEnabled = it.isEmpty()
        }
    }

}