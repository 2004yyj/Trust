package kr.hs.dgsw.trust.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import kr.hs.dgsw.trust.R

class SignUpFragment : Fragment() {

    private lateinit var toolbar : Toolbar
    private lateinit var signUpSuccessFragment: SignUpSuccessFragment

    private val navController : NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        NavigationUI.setupWithNavController(toolbar, navController)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24)
    }

    private fun init(view: View) {
        signUpSuccessFragment
        toolbar = view.findViewById(R.id.toolbar)
    }
}