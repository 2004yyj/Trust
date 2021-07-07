package kr.hs.dgsw.trust.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kr.hs.dgsw.trust.R

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar

    private val navController: NavController by lazy {
        findNavController(R.id.navHost_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar_main)

        val appbarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment)
        )
        NavigationUI.setupWithNavController(toolbar, navController, appbarConfiguration)
    }
}