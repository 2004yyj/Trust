package kr.hs.dgsw.trust.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.textfield.TextInputLayout
import kr.hs.dgsw.domain.usecase.account.PostSignUpUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentSignUpUserInfoBinding
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.function.asMultipart
import kr.hs.dgsw.trust.viewmodel.factory.SignUpViewModelFactory
import kr.hs.dgsw.trust.viewmodel.fragment.SignUpViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

class SignUpUserInfoFragment : Fragment() {

    private lateinit var viewModel : SignUpViewModel

    private lateinit var binding : FragmentSignUpUserInfoBinding

    @Inject
    lateinit var postSignUpUseCase: PostSignUpUseCase

    private lateinit var toolbar: Toolbar
    private lateinit var ivProfileImage: ImageView
    private lateinit var btnProfileImageAdd: ImageButton
    private lateinit var btnSignUp : Button
    private lateinit var tilUsername: TextInputLayout
    private lateinit var tilName: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilPasswordChk: TextInputLayout


    private lateinit var multipartBody: MultipartBody.Part

    private val navController: NavController by lazy {
        findNavController()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application
        (application as MyDaggerApplication).daggerComponent.inject(this)

        viewModel = ViewModelProvider(requireActivity(), SignUpViewModelFactory(postSignUpUseCase))[SignUpViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_user_info, container, false)

        binding.vm = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observe()

        NavigationUI.setupWithNavController(toolbar, navController)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24)

        btnSignUp.setOnClickListener {

            val name = viewModel.name.value
            val username = viewModel.username.value
            val password = viewModel.password.value
            val passwordChk = viewModel.passwordChk.value

            if(!name.isNullOrEmpty() && !username.isNullOrEmpty() && !password.isNullOrEmpty() && !passwordChk.isNullOrEmpty()) {
                if (password == passwordChk) {
                    val multipartBody = if (this::multipartBody.isInitialized) {
                        multipartBody
                    } else {
                        null
                    }
                    viewModel.signUp(multipartBody)

                } else {
                    Toast.makeText(context, "비밀번호를 다시 한번 확인해 주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "빈 칸이 없는지 다시 한번 확인해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        btnProfileImageAdd.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 1)
        }
    }

    private fun observe() {
        viewModel.isFailure.observe(viewLifecycleOwner) {
            Toast.makeText(context, it?:"", Toast.LENGTH_SHORT).show()
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) {
            val action = SignUpUserInfoFragmentDirections.actionSignUpUserInfoFragmentToSignUpSuccessFragment()
            navController.navigate(action)
        }

        viewModel.username.observe(viewLifecycleOwner) {
            tilUsername.error = "필수 항목입니다."
            tilUsername.isErrorEnabled = it.isEmpty()
        }

        viewModel.name.observe(viewLifecycleOwner) {
            tilName.error = "필수 항목입니다."
            tilName.isErrorEnabled = it.isEmpty()
        }

        viewModel.password.observe(viewLifecycleOwner) {
            tilPassword.error = "필수 항목입니다."
            tilPassword.isErrorEnabled = it.isEmpty()
        }

        viewModel.passwordChk.observe(viewLifecycleOwner) {
            val pw = tilPassword.editText?.text.toString()
            tilPasswordChk.apply {
                if (it.isEmpty()) {
                    error = "필수 항목입니다."
                    isErrorEnabled = it.isEmpty()
                } else {
                    error = "비밀번호가 일치하지 않습니다."
                    isErrorEnabled = (pw != it)
                }
            }
        }
    }

    private fun init() {
        btnSignUp = binding.btnSignUpSignUp
        btnProfileImageAdd = binding.btnProfileImageAddSignUp
        ivProfileImage = binding.ivProfileImageSignUp
        toolbar = binding.toolbarSignUp
        tilUsername = binding.tilUsernameSignUp
        tilName = binding.tilNameSignUp
        tilPassword = binding.tilPwSignUp
        tilPasswordChk = binding.tilPwChkSignUp

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navController.navigateUp()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val uri : Uri? = Uri.parse(intent?.dataString)

            if (uri != null) {
                val inputStream = requireActivity().contentResolver.openInputStream(uri)
                val image = BitmapFactory.decodeStream(inputStream)
                multipartBody = uri.asMultipart(requireActivity().contentResolver)!!
                ivProfileImage.setImageBitmap(image)
                inputStream?.close()
            }
        }
    }


}