package br.com.fleury.digital.usingfotoapparat

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import io.fotoapparat.Fotoapparat
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import kotlinx.android.synthetic.main.activity_main.btn_capture
import kotlinx.android.synthetic.main.activity_main.camera_view
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

  private val permissions = arrayOf(
    CAMERA
  )

  private var fotoApparat: Fotoapparat? = null
  private lateinit var photoFile: File
  private var tempURI: Uri? = null
//  private var mCurrentPhotoPath: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    createFotoApparat()

    btn_capture.setOnClickListener { takePhoto() }

  }

  override fun onStop() {
    super.onStop()
    fotoApparat?.stop()
  }

  override fun onStart() {
    super.onStart()
    if (hasNoPermissions()) {
      requestPermission()
    } else {
      fotoApparat?.start()
    }
  }

  private fun takePhoto() {
    if (hasNoPermissions()) {
      requestPermission()
    } else {

      val photoPreview: Intent? = Intent(this, PhotoPreviewActivity::class.java)


      try {
        photoFile = createImageFile()
        val photoURI: Uri = FileProvider.getUriForFile(
          this,
          BuildConfig.APPLICATION_ID + ".fileprovider",
          photoFile
        )
        tempURI = photoURI

        fotoApparat?.takePicture()?.saveToFile(photoFile)

//        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, photoURI)
//        imageView.setImageBitmap(bitmap)
//        Glide.with(this).load(mCurrentPhotoPath).into(imageView)

        photoPreview?.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

        startActivityForResult(photoPreview, 1)

      } catch (e: IOException) {
        tempURI = null
      }

    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if(requestCode == 1){
      if (resultCode == Activity.RESULT_OK){
        Uri.fromFile(File(photoFile.absolutePath))
      }
    }
  }


  private fun hasNoPermissions(): Boolean {
    return ContextCompat.checkSelfPermission(
      this,
      CAMERA
    ) != PackageManager.PERMISSION_GRANTED
  }

  private fun requestPermission() {
    ActivityCompat.requestPermissions(this, permissions, 0)
  }

  fun createFotoApparat() {
    fotoApparat = Fotoapparat(context = this,
      view = camera_view,
      lensPosition = back(),
      cameraErrorCallback = { error ->
        Log.e("RICARDO TESTE", error.toString())
      }
    )
  }


  @Throws(IOException::class)
  fun createImageFile(): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
      "JPG_${timeStamp}_", /* prefix */
      ".jpg", /* suffix */
      storageDir /* directory */
    )
      .apply {
        // Save a file: path for use with ACTION_VIEW intents
        val mCurrentPhotoPath = absolutePath
      }
  }

}
