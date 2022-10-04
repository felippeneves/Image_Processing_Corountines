package br.com.felippeneves.imageprocessingcoroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.com.felippeneves.imageprocessingcoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val IMAGE_URL = "https://raw.githubusercontent.com/DevTides/JetpackDogsApp/master/app/src/main/res/drawable/dog.png";
    private val coroutineScope = CoroutineScope(Dispatchers.Main);
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view);

        //Dispatcher recommended for internet call use
        coroutineScope.launch {
            val originalDeferred = coroutineScope.async(Dispatchers.IO) { getOriginalBitmap() }

            val originalBitmap = originalDeferred.await();

            val filteredDeferred = coroutineScope.async(Dispatchers.Default) { Filter.apply(originalBitmap); }

            val filteredBitmap = filteredDeferred.await();

            loadImage(filteredBitmap);
        }
    }

    private fun getOriginalBitmap() =
        URL(IMAGE_URL).openStream().use {
            BitmapFactory.decodeStream(it);
        }

    private fun applyFilter(originalBmp: Bitmap) = Filter.apply(originalBmp);

    private fun loadImage(bmp: Bitmap) {
        binding.progressBar.visibility = View.GONE;
        binding.imageView.setImageBitmap(bmp);
        binding.imageView.visibility = View.VISIBLE;
    }
}