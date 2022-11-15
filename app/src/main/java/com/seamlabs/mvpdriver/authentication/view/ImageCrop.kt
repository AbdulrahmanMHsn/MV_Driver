//package com.seamlabs.mvpdriver.authentication.view
//
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.navigation.Navigation
//import com.futuremind.recyclerviewfastscroll.Utils
//import com.seamlabs.mvpdriver.common.base.BaseFragment
//import com.seamlabs.mvpdriver.databinding.FragmentImageCropBinding
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//import java.sql.Timestamp
//
//class ImageCrop : BaseFragment<FragmentImageCropBinding>() {
//
//    private val file: File? = null
//    private var f: File? = null
//
//
//    override fun getViewBinding(): FragmentImageCropBinding {
//        return FragmentImageCropBinding.inflate(layoutInflater)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initView()
//        setBtnListeners()
//    }
//
//    private fun initView() {
//        binding.cropImageView.cropShape = CropImageView.CropShape.RECTANGLE
//        binding.cropImageView.scaleType = CropImageView.ScaleType.FIT_CENTER
//        binding.cropImageView.isShowProgressBar = true
//        binding.cropImageView.setFixedAspectRatio(true)
//        if (Utils.SHared_image_path!=null){
//            val file = File(Utils.SHared_image_path)
//            val bitmap = BitmapFactory.decodeFile(file.path)
//            binding.cropImageView.setImageBitmap(bitmap)
//        }
//        else{
//            Navigation.findNavController(requireView()).navigateUp()
//        }
//
//    }
//
//    private fun setBtnListeners() {
//        binding.cancelCrop.setOnClickListener {
//            Navigation.findNavController(
//                requireView()
//            ).navigateUp()
//        }
//
//        binding.rotateCrop.setOnClickListener { cropImageView?.rotateImage(90) }
//
//        binding.okCrop.setOnClickListener {
//            val bitmap = cropImageView?.croppedImage
//            if (bitmap!=null){
//                cacheImg(bitmap, "Profile_img_BlueRide" + Timestamp(System.currentTimeMillis()))
//                Utils.SHared_image_path = f?.absolutePath
//            }
//
//            Navigation.findNavController(requireView()).navigateUp()
//        }
//    }
//
//    private fun cacheImg(imageToSave: Bitmap, fileName: String) {
//        f = File(requireActivity().applicationContext.cacheDir, fileName)
//        try {
//            f?.createNewFile()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        try {
//            val out = FileOutputStream(f)
//            imageToSave.compress(Bitmap.CompressFormat.PNG, 50 /*ignored for PNG*/, out)
//            out.flush()
//            out.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//
//}