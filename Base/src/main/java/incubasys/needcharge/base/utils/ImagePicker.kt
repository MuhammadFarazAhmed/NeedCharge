package incubasys.needcharge.base.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by SamKazmi on 5/16/2017.
 */

class ImagePicker(private val context: Context) {
    private var mCurrentImagePath: String? = null
    private var photoUri: Uri? = null

    private val intent: Intent
        get() {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.setDataAndTypeAndNormalize(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            val pickerIntent = Intent.createChooser(galleryIntent, "Choose from")
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(context.packageManager) != null) {
                var tempFile: File? = null
                try {
                    tempFile = createImageFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                photoUri = null
                if (tempFile != null) {
                    photoUri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", tempFile)
                }
                if (photoUri != null) {
                    cameraIntent.putExtra("return-data", true)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                }
                pickerIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
            }
            return pickerIntent
        }

    fun startActivity(activity: Activity, OnPermissionCallback: OnPermissionCallback) {
        if (isPermissionGranted(context)) {
            activity.startActivityForResult(intent, REQUEST_PICK_IMAGE)
        } else {
            requestPermission(activity, OnPermissionCallback)
        }
    }

    private fun requestPermission(activity: Activity, onPermissionCallback: OnPermissionCallback?) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            onPermissionCallback?.shouldShowPermissionMessage()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_CHECK_PERMISSION)
        }
    }

    fun startActivity(fragment: Fragment, onPermissionCallback: OnPermissionCallback) {
        if (isPermissionGranted(context)) {
            fragment.startActivityForResult(intent, REQUEST_PICK_IMAGE)
        } else if (fragment.activity != null) {
            requestPermission(fragment.activity!!, onPermissionCallback)
        }
    }

    fun getSelectedImagePath(requestCode: Int, resultCode: Int, data: Intent?): String? {
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                val mPhoto = data.data
                if (mPhoto == photoUri) {
                    return mCurrentImagePath
                }
                try {
                    val projection = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = context.contentResolver.query(mPhoto!!, projection, null, null, null)
                    if (cursor != null) {
                        val column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        cursor.moveToFirst()
                        val path = cursor.getString(column_index)
                        cursor.close()
                        return path
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                return mCurrentImagePath
            }
        } else if (resultCode == Activity.RESULT_CANCELED && mCurrentImagePath != null) {
            dispose()
        }
        return null
    }

    private fun dispose() {
        val tempFile = File(mCurrentImagePath!!)
        if (tempFile.exists())

            tempFile.delete()
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.externalCacheDir
        if (storageDir != null && !storageDir.exists()) {

            storageDir.mkdirs()
        }
        val imagesDir = File(storageDir, "images")
        if (!imagesDir.exists()) {

            imagesDir.mkdirs()
        }
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            imagesDir      /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentImagePath = image.absolutePath
//        Log.v(TAG, mCurrentImagePath)
        return image
    }

    fun onRequestPermissionsResult(grantResults: IntArray): Boolean {
        return grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    private fun isPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    interface OnPermissionCallback {
        fun shouldShowPermissionMessage()
    }

    companion object {
        val REQUEST_CHECK_PERMISSION_SETTINGS = 5441
        private val REQUEST_CHECK_PERMISSION = 1134
        private val REQUEST_PICK_IMAGE = 1121
        private val TAG = "SimpleImagePicker"
        private val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    }
}
