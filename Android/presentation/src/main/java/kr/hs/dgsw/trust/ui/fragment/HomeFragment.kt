package kr.hs.dgsw.trust.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.adapter.PostAdapter

class HomeFragment : Fragment() {

    private lateinit var recyclerAdapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar_home)
        recyclerView = view.findViewById(R.id.rv_post_home)
        recyclerAdapter = PostAdapter()

        val appbarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.boardFragment,
                R.id.chatFragment,
                R.id.userInfoFragment
            ), null
        )
        NavigationUI.setupWithNavController(toolbar, navController, appbarConfiguration)

        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerAdapter.submitList(
            listOf(
                Post(1),
                Post(2),
                Post(3),
                Post(4),
                Post(5),
                Post(6),
            )
        )
    }
}