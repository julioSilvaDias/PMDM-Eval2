package com.example.capturaimage_video

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.widget.Button
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val CAPTURA_IMAGEN = 1
    private val CAPTURA_VIDEO = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnFoto = findViewById<Button>(R.id.buttonFoto)
        val btnVideo = findViewById<Button>(R.id.buttonVideo)

        btnFoto.setOnClickListener {
            val fotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(fotoIntent, CAPTURA_IMAGEN)
        }

        btnVideo.setOnClickListener {
            val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(videoIntent, CAPTURA_VIDEO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAPTURA_IMAGEN && resultCode == RESULT_OK) {
            val imagen = data?.extras?.get("data") as? Bitmap
            findViewById<ImageView>(R.id.imageView).setImageBitmap(imagen)
        }

        if (requestCode == CAPTURA_VIDEO && resultCode == RESULT_OK) {
            val video = data?.data
            val videoView = findViewById<VideoView>(R.id.videoView)
            videoView.setVideoURI(video)
            videoView.start()
        }
    }
}
