package kr.hs.dgsw.trust.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kr.hs.dgsw.domain.usecase.post.PostPostUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentAddBinding
import kr.hs.dgsw.trust.databinding.LayoutButtonBinding
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.function.asMultipart
import kr.hs.dgsw.trust.ui.adapter.PostImageAdapter
import kr.hs.dgsw.trust.ui.viewmodel.factory.AddViewModelFactory
import kr.hs.dgsw.trust.ui.viewmodel.fragment.AddViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

class AddFragment : Fragment() {

    private val navController: NavController by lazy {
        findNavController()
    }

    private val uriList = ArrayList<Uri>()

    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    private lateinit var binding: FragmentAddBinding
    private lateinit var viewModel: AddViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: PostImageAdapter
    private lateinit var fabAddImage: FloatingActionButton
    private lateinit var btnSubmit: LayoutButtonBinding

    private lateinit var multipartBodyBuilder: MultipartBody.Builder

    @Inject
    lateinit var postPostUseCase: PostPostUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        (requireActivity().application as MyDaggerApplication).daggerComponent.inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this, AddViewModelFactory(postPostUseCase))[AddViewModel::class.java]
        binding.vm = viewModel

        multipartBodyBuilder = MultipartBody.Builder()

        recyclerView = binding.rvContentImage
        imageAdapter = PostImageAdapter()
        recyclerView.adapter = imageAdapter

        fabAddImage = binding.fabAddImageAdd

        btnSubmit = binding.btnSubmitAdd

        with(viewModel) {
            isSuccess.observe(viewLifecycleOwner) {
                navController.navigateUp()
            }
            isFailure.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }

            fabAddImage.setOnClickListener {
                activityResultLauncher.launch("image/*")
            }

            activityResultLauncher =
                registerForActivityResult(ActivityResultContracts.GetContent()) {
                    if (it != null) {
                        with(requireActivity()) {
                            multipartBodyBuilder.addPart(
                                it.asMultipart("imageList", cacheDir, contentResolver)!!
                            )
                            addImageList(multipartBodyBuilder.build().parts)
                        }
                        uriList.add(it)
                        imageAdapter.submitList(uriList as List<Uri>)
                    }
                }
        }
    }
}