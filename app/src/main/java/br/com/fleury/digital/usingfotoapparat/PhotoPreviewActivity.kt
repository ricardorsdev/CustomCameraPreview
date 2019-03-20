package br.com.fleury.digital.usingfotoapparat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_photo_preview.btn_cancel
import kotlinx.android.synthetic.main.activity_photo_preview.btn_save
import kotlinx.android.synthetic.main.activity_photo_preview.photo_preview

class PhotoPreviewActivity: AppCompatActivity() {

  private lateinit var photoUri : Uri

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_photo_preview)

    photoUri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT)

    var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
    bitmap = bitmap.rotate(90f)
    photo_preview.setImageBitmap(bitmap)

    btn_save.setOnClickListener { returnIntent(true) }
    btn_cancel.setOnClickListener { returnIntent(false) }

  }

  private fun returnIntent(isOk: Boolean){
    val returnIntent = Intent()

    returnIntent.putExtra("photoOk", isOk)
    setResult(Activity.RESULT_OK, returnIntent)
    finish()
  }

  private fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
  }

}