package kr.hs.dgsw.trust.ui.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import kr.hs.dgsw.trust.ui.viewmodel.factory.SignUpViewModelFactory
import kr.hs.dgsw.trust.ui.viewmodel.fragment.SignUpViewModel
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

    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

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
                    viewModel.postSignUp(multipartBody)

                } else {
                    Toast.makeText(context, "??????????????? ?????? ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "??? ?????? ????????? ?????? ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show()
            }
        }

        btnProfileImageAdd.setOnClickListener {
            activityResultLauncher.launch("image/*")
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
            tilUsername.error = "?????? ???????????????."
            tilUsername.isErrorEnabled = it.isEmpty()
        }

        viewModel.name.observe(viewLifecycleOwner) {
            tilName.error = "?????? ???????????????."
            tilName.isErrorEnabled = it.isEmpty()
        }

        viewModel.password.observe(viewLifecycleOwner) {
            tilPassword.error = "?????? ???????????????."
            tilPassword.isErrorEnabled = it.isEmpty()
        }

        viewModel.passwordChk.observe(viewLifecycleOwner) {
            val pw = tilPassword.editText?.text.toString()
            tilPasswordChk.apply {
                if (it.isEmpty()) {
                    error = "?????? ???????????????."
                    isErrorEnabled = it.isEmpty()
                } else {
                    error = "??????????????? ???????????? ????????????."
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

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                val inputStream = requireActivity().contentResolver.openInputStream(it)
                val image = BitmapFactory.decodeStream(inputStream)
                with(requireActivity()) {
                    multipartBody = it.asMultipart("profileImage", cacheDir, contentResolver)!!
                }
                ivProfileImage.setImageBitmap(image)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navController.navigateUp()
        }
    }


}