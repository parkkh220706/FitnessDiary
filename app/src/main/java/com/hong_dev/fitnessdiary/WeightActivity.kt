package com.hong_dev.fitnessdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.drawerlayout.widget.DrawerLayout
import com.hong_dev.fitnessdiary.databinding.ActivityWeightBinding

class WeightActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeightBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.weightDrawerlayout

        binding.btnMenu.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(Gravity.LEFT)){
                drawerLayout.openDrawer(Gravity.LEFT)
            } else {
                drawerLayout.closeDrawer(Gravity.LEFT)
            }
        }

        binding.nav2.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_weight -> {
                    val intent = Intent(this, WeightActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }



    }
}