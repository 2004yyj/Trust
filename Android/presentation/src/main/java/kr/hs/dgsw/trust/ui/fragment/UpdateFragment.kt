package kr.hs.dgsw.trust.ui.fragment

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
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kr.hs.dgsw.domain.entity.Image
import kr.hs.dgsw.domain.usecase.post.UpdatePostUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.databinding.FragmentUpdateBinding
import kr.hs.dgsw.trust.databinding.LayoutButtonBinding
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.function.asMultipart
import kr.hs.dgsw.trust.ui.adapter.PostImageAdapter
import kr.hs.dgsw.trust.ui.viewmodel.factory.UpdateViewModelFactory
import kr.hs.dgsw.trust.ui.viewmodel.fragment.UpdateViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

class UpdateFragment : Fragment() {

    private val navController: NavController by lazy {
        findNavController()
    }

    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    private lateinit var binding: FragmentUpdateBinding
    private lateinit var viewModel: UpdateViewModel

    private lateinit var imageRecyclerView: RecyclerView
    private lateinit var imageAdapter: PostImageAdapter
    private lateinit var fabAddImage: FloatingActionButton
    private lateinit var btnSubmit: LayoutButtonBinding

    @Inject
    lateinit var updatePostUseCase: UpdatePostUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update, container, false)
        (requireActivity().application as MyDaggerApplication).daggerComponent.inject(this)

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.rvContentImage)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this, UpdateViewModelFactory(updatePostUseCase))[UpdateViewModel::class.java]
        binding.vm = viewModel

        imageRecyclerView = binding.rvContentImage
        imageAdapter = PostImageAdapter()
        imageRecyclerView.adapter = imageAdapter

        fabAddImage = binding.fabAddImageAdd
        btnSubmit = binding.btnSubmitAdd

        val postId = requireArguments().getInt("postId")
        val isAnonymous = requireArguments().getBoolean("isAnonymous")
        val content = requireArguments().getString("content")
        val defaultImageList = requireArguments().getStringArrayList("defaultImageList")

        defaultImageList?.forEach {
            val imageList = ArrayList<Image>()
            imageAdapter.currentList.forEach { image ->
                imageList.add(image)
            }
            imageList.add(Image(it, "STRING"))
            imageAdapter.submitList(imageList)
        }

        with(viewModel) {
            this.postId = postId

            isSuccess.observe(viewLifecycleOwner) {
                navController.navigateUp()
            }
            isFailure.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }

            fabAddImage.setOnClickListener {
                activityResultLauncher.launch("image/*")
            }

            activityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
                if (it != null) {
                    updateFileList.add(it)
                    val imageList = ArrayList<Image>()
                    imageAdapter.currentList.forEach { image ->
                        imageList.add(image)
                    }
                    imageList.add(Image(it.toString(), "URI"))
                    imageAdapter.submitList(imageList)
                }
            }

            btnSubmit.text = "수정"
            viewModel.isAnonymous.set(isAnonymous)
            viewModel.content.set(content)

            btnSubmit.setOnClick {
                val multipartBuilder = MultipartBody.Builder()
                var uriCnt = 0
                updateFileList.forEach {
                    val multipart = with(requireActivity()) {
                        it.asMultipart("updateFileList", cacheDir, contentResolver)!!
                    }
                    multipartBuilder.addPart(multipart)
                    uriCnt++
                }

                val partList = if (uriCnt > 0) {
                    multipartBuilder.build().parts
                } else {
                    null
                }
                viewModel.updatePost(partList)
            }

            imageAdapter.deleteStringImage.observe(viewLifecycleOwner) {
                deleteFileList.add(it)
                val imageList = ArrayList<Image>()
                imageAdapter.currentList.forEach { image ->
                    imageList.add(image)
                }
                imageList.remove(Image(it, "STRING"))
                imageAdapter.submitList(imageList)
            }

            imageAdapter.deleteUriImage.observe(viewLifecycleOwner) {
                updateFileList.remove(it)
                val imageList = ArrayList<Image>()
                imageAdapter.currentList.forEach { image ->
                    imageList.add(image)
                }
                imageList.remove(Image(it.toString(), "URI"))
                imageAdapter.submitList(imageList)
            }
        }
    }
}