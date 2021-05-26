package kr.hs.dgsw.trust.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kr.hs.dgsw.domain.usecase.account.PostLoginUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentLoginBinding
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.ui.activity.MainActivity
import kr.hs.dgsw.trust.ui.viewmodel.factory.LoginViewModelFactory
import kr.hs.dgsw.trust.ui.viewmodel.fragment.LoginViewModel
import javax.inject.Inject

class LoginFragment : Fragment() {

    companion object {
        private const val TAG = "LoginFragment"
    }

    @Inject
    lateinit var postLoginUseCase: PostLoginUseCase

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding : FragmentLoginBinding

    private lateinit var tilId : TextInputLayout
    private lateinit var tilPw : TextInputLayout
    private lateinit var btnLogin : Button
    private lateinit var btnSignUp : Button

    private lateinit var permissionListener : PermissionListener

    private val navController : NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        val application = requireActivity().application
        (application as MyDaggerApplication).daggerComponent.inject(this)

        viewModel = ViewModelProvider(this, LoginViewModelFactory(postLoginUseCase))[LoginViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.vm = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        tedPermission()
        observe()

        btnLogin.setOnClickListener {
            viewModel.apply {
                if (!username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()) {
                    login()
                    return@setOnClickListener
                }
            }

            Toast.makeText(context, "아이디 또는 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
        }

        btnSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpUserInfoFragment()
            navController.navigate(action)
        }
    }

    private fun tedPermission() {

        permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(context, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(context, "권한이 거부되었습니다. 권한을 거부하시면 앱을 사용하실 수 없습니다.", Toast.LENGTH_SHORT).show()
            }

        }

        if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
            TedPermission.with(context)
                    .setPermissionListener(permissionListener)
                    .setRationaleConfirmText("권한이 필요합니다.")
                    .setDeniedMessage("권한이 거부되었습니다.\n[설정] -> [권한]에서 권한을 허용할 수 있습니다.")
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()
        }
    }

    private fun init() {
        tilId = binding.tilIdLogin
        tilPw = binding.tilPwLogin
        btnLogin = binding.btnLoginLogin
        btnSignUp = binding.btnSignUpLogin
    }

    private fun observe() {
        viewModel.username.observe(viewLifecycleOwner) {
            tilId.error = "아이디를 입력해주세요."
            tilId.isErrorEnabled = it.isEmpty()
        }

        viewModel.password.observe(viewLifecycleOwner) {
            tilPw.error = "비밀번호를 입력해주세요."
            tilPw.isErrorEnabled = it.isEmpty()
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.isFailure.observe(viewLifecycleOwner) {
            Toast.makeText(context, it?:"", Toast.LENGTH_SHORT).show()
        }
    }

}