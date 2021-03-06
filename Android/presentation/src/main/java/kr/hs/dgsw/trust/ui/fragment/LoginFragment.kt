package kr.hs.dgsw.trust.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kr.hs.dgsw.trust.databinding.LayoutButtonBinding
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.ui.activity.MainActivity
import kr.hs.dgsw.trust.ui.util.PreferenceHelper.autoLogin
import kr.hs.dgsw.trust.ui.util.PreferenceHelper.token
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

    private lateinit var btnLogin : LayoutButtonBinding
    private lateinit var btnSignUp : Button

    private lateinit var tilId: TextInputLayout
    private lateinit var tilPw: TextInputLayout

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

        btnSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpUserInfoFragment()
            navController.navigate(action)
        }
    }

    private fun tedPermission() {

        permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(context, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(context, "????????? ?????????????????????. ????????? ??????????????? ?????? ???????????? ??? ????????????.", Toast.LENGTH_SHORT).show()
            }

        }

        if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
            TedPermission.with(context)
                    .setPermissionListener(permissionListener)
                    .setRationaleConfirmText("????????? ???????????????.")
                    .setDeniedMessage("????????? ?????????????????????.\n[??????] -> [??????]?????? ????????? ????????? ??? ????????????.")
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()
        }
    }

    private fun init() {

        viewModel.autoLoginChk.set(autoLogin)

        tilId = binding.tilIdLogin
        tilPw = binding.tilPwLogin
        btnLogin = binding.btnLoginLogin
        btnSignUp = binding.btnSignUpLogin
    }

    private fun observe() {

        viewModel.username.observe(viewLifecycleOwner) {
            tilId.error = "???????????? ??????????????????."
            tilId.isErrorEnabled = it.isEmpty()
        }

        viewModel.password.observe(viewLifecycleOwner) {
            tilPw.error = "??????????????? ??????????????????."
            tilPw.isErrorEnabled = it.isEmpty()
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) {

            Log.d(TAG, "observe: $it")

            token = it.token
            autoLogin = viewModel.autoLoginChk.get()!!

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.isFailure.observe(viewLifecycleOwner) {
            Toast.makeText(context, it?:"????????? ??????????????????.", Toast.LENGTH_SHORT).show()
        }
    }

}