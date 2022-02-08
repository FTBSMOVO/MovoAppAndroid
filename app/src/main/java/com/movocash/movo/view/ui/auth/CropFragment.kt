package com.movocash.movo.view.ui.auth

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentCropImageBinding
import com.movocash.movo.utilities.extensions.convertBitmapToUri
import com.movocash.movo.utilities.extensions.getBitmap
import com.movocash.movo.view.ui.base.ActivityBase
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment

class CropFragment : BaseFragment(), View.OnClickListener {

    private var finalBitmap: Bitmap? = null
    lateinit var binding: FragmentCropImageBinding
    private var cropImageUri: Uri? = null
    private lateinit var intent: Intent

    companion object {

        lateinit var instance: CropFragment
        var filePath: String? = null
        var isFront = false

        fun newInstance(fileSrc: String, isFront: Boolean): CropFragment {
            instance = CropFragment()
            filePath = fileSrc
            this.isFront = isFront
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_crop_image, container, false)
        setListener()
        setData()
        return binding.root
    }

    private fun setListener() {
        binding.tvNext.setOnClickListener(this)
        binding.tvCancel.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvNext -> {
                cropImageUri = ActivityBase.activity.convertBitmapToUri(getBitmap(binding.rlCropPhoto)!!)
                intent = Intent()
                intent.putExtra("cropImage", cropImageUri.toString())
                targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            }

            R.id.tvCancel -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            }
        }
    }

    private fun setData() {
        if (isFront){
            binding.tvSide.text = "Document Front"
        }else
            binding.tvSide.text = "Document Back"


        val options: BitmapFactory.Options
        var bitmap: Bitmap? = null

        try {
            bitmap = BitmapFactory.decodeFile(filePath)
        } catch (e: OutOfMemoryError) {
            try {
                options = BitmapFactory.Options()
                options.inSampleSize = 2
                bitmap = BitmapFactory.decodeFile(filePath, options)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }


        try {
            val exif = ExifInterface(filePath!!)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }
            bitmap = Bitmap.createBitmap(bitmap!!, 0, 0, bitmap!!.width, bitmap.height, matrix, true) // rotating bitmap
        } catch (e: Exception) {
        }

        try {
            bitmap = bitmap!!.copy(Bitmap.Config.ARGB_8888, true)
            finalBitmap = bitmap
            binding.ivPost.setImageBitmap(bitmap)
            binding.ivPostBackground.setImageBitmap(bitmap)
            binding.ivPostBackgroundMid.setImageBitmap(bitmap)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

}