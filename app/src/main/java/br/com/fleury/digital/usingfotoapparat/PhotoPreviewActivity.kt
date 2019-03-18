package br.com.fleury.digital.usingfotoapparat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photo_preview.photo_preview
import java.io.File
import java.io.InputStream

class PhotoPreviewActivity: AppCompatActivity() {

  lateinit var photoUri : Uri

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_photo_preview)

    photoUri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT)

//    val imageUri = Uri.fromFile(File(imagePath))

    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)

    photo_preview.setImageBitmap(bitmap)

//    Glide.with(this).load(imagePath).into(photo_preview)

    photo_preview.setOnClickListener { returnIntent() }
  }

  fun returnIntent(){
    val returnIntent = Intent()

    returnIntent.data = photoUri

    setResult(Activity.RESULT_OK, returnIntent)
    finish()
  }

}