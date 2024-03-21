package com.example.emmaleegomez_sharedpreference

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.emmaleegomez_sharedpreference.PreferencesRepository
import com.example.emmaleegomez_sharedpreference.R
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var changeImage: Button
    private lateinit var randomImage: ImageView
    private lateinit var imageTitle: EditText
    private lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferencesRepository.initialize(this)
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        preferencesRepository = PreferencesRepository.get()

        changeImage = findViewById(R.id.change_image)
        randomImage = findViewById(R.id.random_image)
        imageTitle = findViewById(R.id.image_title)

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.change_image -> {
                    updateImage()
                }
            }
        }

        changeImage.setOnClickListener(clickListener)

        val titleListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updateTitle()
            }
        }

        imageTitle.onFocusChangeListener = titleListener

        lifecycleScope.launch {
            preferencesRepository.storedImageId.collect { resourceId ->
                randomImage.setImageResource(resourceId)
            }
        }

        lifecycleScope.launch {
            preferencesRepository.storedImageTitle.collect { title ->
                imageTitle.setText(title)
            }
        }
    }

    private fun updateImage() {
        val imageList = arrayOf(
            R.drawable.cero,
            R.drawable.uno,
            R.drawable.dos,
            R.drawable.tres,
            R.drawable.cuatro,
            R.drawable.cinco,
            R.drawable.seis,
            R.drawable.siete,
            R.drawable.ocho,
            R.drawable.nueve
        )
        val randomNum = Random.nextInt(imageList.size)
        val newRandomImage = imageList[randomNum]
        val title = imageTitle.text.toString()
        randomImage.setImageResource(imageList[randomNum])

        lifecycleScope.launch {
            preferencesRepository.setStoredImageId(newRandomImage)
            preferencesRepository.setStoredImageTitle(title)
        }
    }

    private fun updateTitle() {
        val title = imageTitle.text.toString()
        lifecycleScope.launch {
            preferencesRepository.setStoredImageTitle(title)
        }
    }
}