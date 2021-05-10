package kr.hs.dgsw.trust.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentSignUpUserInfoBinding
import kr.hs.dgsw.trust.viewmodel.fragment.SignUpViewModel

class SignUpUserInfoFragment : Fragment() {

    private lateinit var viewModel : SignUpViewModel

    private lateinit var binding : FragmentSignUpUserInfoBinding

    private lateinit var toolbar: Toolbar

    private lateinit var btnProfileImageAdd: ImageButton
    private lateinit var ivProfileImage: ImageView

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

        NavigationUI.setupWithNavController(toolbar, navController)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24)

        btnSignUp.setOnClickListener {
            viewModel.signUp()
            navController.navigate(R.id.action_signUpUserInfoFragment_to_signUpSuccessFragment)
        }

        btnProfileImageAdd.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 1)
        }
    }

    private fun init() {
        btnSignUp = binding.btnSignUpSignUp
        btnProfileImageAdd = binding.btnProfileImageAddSignUp
        ivProfileImage = binding.ivProfileImageSignUp
        toolbar = binding.toolbarSignUp
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val data = Uri.parse(intent?.dataString)

            val inputStream = requireActivity().contentResolver.openInputStream(data)
            val image = BitmapFactory.decodeStream(inputStream)
            ivProfileImage.setImageBitmap(image)
            inputStream?.close()
        }

    }
}