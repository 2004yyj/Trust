package kr.hs.dgsw.trust.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.hs.dgsw.trust.R

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    private val navController: NavController by lazy {
        findNavController(R.id.navHost_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bnv_main)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
}