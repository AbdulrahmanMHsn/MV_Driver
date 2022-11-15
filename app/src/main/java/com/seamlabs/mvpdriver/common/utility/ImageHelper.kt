

package com.seamlabs.mvpdriver.common.utility


import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ExifInterface
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.*
import kotlin.math.roundToInt


class ImageHelper(private val context: Context) {

    companion object {
        private const val maxHeight = 1280.0f
        private const val maxWidth = 1280.0f
    }


    fun isPermissionGranted(permission: String): Boolean {
        return try {
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        } catch (ex: IllegalStateException) {
            return false
        }
    }

    open fun clearMyFiles(fileName:String?) {
        fileName?.let {
            val files = File(context.cacheDir, fileName).delete()
        }
    }


    /**
     * Reduces the size of an image without affecting its quality.
     *
     * @param imagePath -Path of an image
     * @return
     */
    fun compressImage(imagePath: String): String {
        var scaledBitmap: Bitmap? = null
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(imagePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth
        var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
        val maxRatio = (maxWidth / maxHeight)
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            when {
                imgRatio < maxRatio -> {
                    imgRatio = (maxHeight / actualHeight)
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = maxHeight.toInt()
                }
                imgRatio > maxRatio -> {
                    imgRatio = (maxWidth / actualWidth)
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = maxWidth.toInt()
                }
                else -> {
                    actualHeight = maxHeight.toInt()
                    actualWidth = maxWidth.toInt()
                }
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
            bmp = BitmapFactory.decodeFile(imagePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp!!,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
        bmp.recycle()
        val exif: ExifInterface
        try {
            exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            when (orientation) {
                6 -> {
                    matrix.postRotate(90F)
                }
                3 -> {
                    matrix.postRotate(180F)
                }
                8 -> {
                    matrix.postRotate(270F)
                }
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }


        var out: FileOutputStream? = null

        val f = File(context.cacheDir, "image.png")

        try {
            out = FileOutputStream(f)

            val byte = ByteArrayOutputStream()
            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, byte)

            val data = byte.toByteArray()

            out.write(data)
            out.flush()
            out.close()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return f.absolutePath
    }


    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

}

//
//import android.Manifest
//import android.content.ContentResolver
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.ImageDecoder
//import android.net.Uri
//import android.os.Build
//import android.provider.MediaStore
//import android.provider.OpenableColumns
//import android.util.Log
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.core.content.ContentProviderCompat.requireContext
//import androidx.core.content.FileProvider
//import com.seamlabs.tadweer.BuildConfig
//import com.seamlabs.tadweer.R
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//
//object ImageHelper {
//    private var file: File? = null
//
//
//    private fun subscribeActivityResult() {
//        activityResultsDataLive.observe(viewLifecycleOwner, {
//            if (it != null) {
//                try {
//                    if (it.data!=null){
//
//                        val selectedImage = it.data
//                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//                        val cursor =
//                            requireActivity().contentResolver.query(
//                                selectedImage!!,
//                                filePathColumn,
//                                null,
//                                null,
//                                null
//                            )
//                        if (cursor != null) {
//                            try {
//                                val columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0])
//                                cursor.moveToFirst()
//                                imagePath = cursor.getString(columnIndex)
//                            }
//                            catch (ex : Exception){
//                                Log.i("TAGTAGTAG", "subscribeActivityResult: ${ex.message}")
//
//                            }
//                            cursor.close()
//                        }
//                        else{
//                            Log.i("TAGTAGTAG", "subscribeActivityResult: empty")
//
//                        }
//                    }
//                }
//                catch (ex : Exception){
//                    //
//                    Log.i("TAGTAGTAG", "subscribeActivityResult: ${ex.message}")
//
//                }
//                if (imagePath.isNullOrEmpty()) {
//                    Log.i("TAGTAGTAG", "subscribeActivityResult: empty")
//                }else{
//                    Log.i("TAGTAGTAG", "subscribeActivityResult: No empty")
//
//                }
//            }
//        })
////        activityResultsDataLive.observe(viewLifecycleOwner, {
////            if (it != null) {
////                try {
////                    if (it.data != null) {
////                        file = File(
////                            requireContext().cacheDir,
////                            requireContext().contentResolver.getFileName(it.data!!)
////                        )
////                        getCapturedImage(it.data!!)
////
////                        it.data?.let { uri->
////                            sharedImageBitmap = getCapturedImage(uri)
////                            sharedImageName = requireContext().contentResolver.getFileName(uri)
////                        }
////
////                        cacheImg(
////                            getCapturedImage(it.data!!),
////                            "Profile_img_tadweer" + Timestamp(System.currentTimeMillis())
////                        )
////                        sharedImageBitmap?.let {
////                            Glide.with(requireContext()).load(sharedImageBitmap).apply(
////                                RequestOptions().circleCrop()).into(binding.img.imageView)
////                        }
////
////                        sharedImageName?.let {
////                            binding.img.txtImg.text = sharedImageName
////                        }
////
////                        file?.let {
////                            vm.setSharedImagePath(it.absolutePath)
////                        }
////                    } else {
////                        Log.i("TAGTAGTAG", "subscribeActivityResult: empty")
////                        makeToast(getString(R.string.someThing_went_wrong))
////                    }
////
////                } catch (ex: Exception) {
////                    Log.i("TAGTAGTAG", "subscribeActivityResult: ${ex.message}")
////                    makeToast(getString(R.string.someThing_went_wrong))
////                }
////            }
////        })
//    }
//
//
//    private fun subscribePermissions() {
//        permissionsResultsLive.observe(viewLifecycleOwner, {
//            it?.keys.apply {
//                this?.forEach { st ->
//                    when (st) {
//                        Manifest.permission.READ_EXTERNAL_STORAGE -> {
//                            if (it?.get(Manifest.permission.READ_EXTERNAL_STORAGE) == true) {
//                                performCameraAndGalleyAction()
//                            } else {
//                                showExplanation(
//                                    getString(R.string.permission_needed),
//                                    getString(R.string.picture_permission_explanation),
//                                    storagePermissions
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        })
//    }
//
//
//    private fun performCameraAndGalleyAction() {
//        val camIntent = Intent("android.media.action.IMAGE_CAPTURE")
//        val f = File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg")
//        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
//        val gallIntent = Intent(Intent.ACTION_GET_CONTENT)
//        gallIntent.type = "image/*"
//        val chooser = Intent.createChooser(gallIntent, "Choose your photo")
//        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(camIntent))
//        launchActivityForResult(chooser)
//    }
//
//    private fun launchActivityForResult(intent: Intent) {
//        try {
//            resultLauncher.launch(intent)
//        } catch (ex: Exception) {
//            Log.d("myDebug", "BaseFragment launchActivityForResult   " + ex.localizedMessage)
//        }
//    }
//
//    var resultLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            val data: Intent? = result.data
//            activityResultsData.value = data
//        }
//
//    private fun getCapturedImage(selectedPhotoUri: Uri,context: Context): Bitmap {
//        return when {
//            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
//                context.contentResolver,
//                selectedPhotoUri
//            )
//            else -> {
//                val source =
//                    ImageDecoder.createSource(requireContext().contentResolver, selectedPhotoUri)
//                ImageDecoder.decodeBitmap(source)
//            }
//        }
//    }
//
////    private fun cacheImg(imageToSave: Bitmap, fileName: String) {
////        file = File(requireActivity().applicationContext.cacheDir, fileName)
////        try {
////            file!!.createNewFile()
////        } catch (e: IOException) {
////            e.printStackTrace()
////        }
////        try {
////            val out = FileOutputStream(file)
////            imageToSave.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, out)
////            out.flush()
////            out.close()
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
////    }
//
//
//    private fun ContentResolver.getFileName(fileUri: Uri): String {
//        var name = ""
//        val returnCursor = this.query(fileUri, null, null, null, null)
//        if (returnCursor != null) {
//            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//            returnCursor.moveToFirst()
//            name = returnCursor.getString(nameIndex)
//            returnCursor.close()
//        }
//        return name
//    }
//
//
//}

