package com.movocash.movo.view.ui.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Base64OutputStream
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.acuant.acuantcamera.camera.AcuantCameraActivity
import com.acuant.acuantcamera.camera.AcuantCameraOptions
import com.acuant.acuantcamera.constant.ACUANT_EXTRA_CAMERA_OPTIONS
import com.acuant.acuantcamera.constant.ACUANT_EXTRA_IMAGE_URL
import com.acuant.acuantcamera.constant.ACUANT_EXTRA_PDF417_BARCODE
import com.acuant.acuantcamera.initializer.MrzCameraInitializer
import com.acuant.acuantcommon.exception.AcuantException
import com.acuant.acuantcommon.initializer.AcuantInitializer
import com.acuant.acuanthgliveness.model.FaceCapturedImage
import com.acuant.acuantimagepreparation.AcuantImagePreparation
import com.acuant.acuantimagepreparation.background.EvaluateImageListener
import com.acuant.acuantimagepreparation.initializer.ImageProcessorInitializer
import com.acuant.acuantimagepreparation.model.AcuantImage
import com.acuant.acuantimagepreparation.model.CroppingData
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.movocash.movo.FacialLivenessActivity
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.Document
import com.movocash.movo.data.model.requestmodel.MediaRequestModel
import com.movocash.movo.data.remote.callback.ICallBackUri
import com.movocash.movo.databinding.FragmentMediaBinding
import com.movocash.movo.kycFlow.Failed_Signup
import com.movocash.movo.kycFlow.SignupFragment
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.AccountViewModel
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class MediaFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentMediaBinding
    private lateinit var accountViewModel: AccountViewModel
    private val REQ_CODE_DOC_FRONT = 1
    private val REQ_CODE_DOC_BACK = 2
    private val REQ_CODE_SELFIE = 3
    private val REQ_CODE_CROP_FRONT = 4
    private val REQ_CODE_CROP_BACK = 5
    private val REQ_CODE_CROP_SELFIE = 6
    lateinit var cropFragment: CropFragment
    lateinit var cropCircleFragment: CircularCropFragment
    private var docFrontUri: Uri? = null
    private var docBackUri: Uri? = null
    private var selfieUri: Uri? = null
    private var b64FrontDoc = ""
    private var b64BackDoc = ""
    private var b64Selfie = ""
    var selfie_bitmap: Bitmap? = null
    var front_bitmap: Bitmap? = null
    var back_bitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_media, container, false)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        try {

            context?.let {
                AcuantInitializer.initialize(
                    "acuant.config.xml",
                    it,
                    listOf(ImageProcessorInitializer(), /*EchipInitializer(),*/ MrzCameraInitializer()),initCallback
                )
            }
        } catch (e: AcuantException) {
            Log.e("Acuant Error", e.toString())
        }

        setUiObserver()
        setView()
        setListener()
        return binding.root
    }

    private fun setView() {
        binding.isDocSet = false
        binding.isDocBack = false
        binding.isImageSet = false
    }

    private fun setUiObserver() {
        accountViewModel.mediaFailure.observe(viewLifecycleOwner, Observer { obj ->
           // ActivityBase.activity.showToastMessage(obj.messages.toString())

            if(obj.data!!.decisionId == Constants.INSTANT_PASS)
            {
                callFragmentWithReplace(R.id.authContainer, SuccessFragment(), "SignInFragment")
            }
            else if(obj.data!!.decisionId == Constants.FAIL)
            {
               // ActivityBase.activity.showToastMessage("Instant Failed")
                //showInfoDialog("", obj.messages.toString(), this)
                //callFragmentWithReplace(R.id.mainContainer, SignupFragment(), null)
                callFragmentWithReplace(R.id.authContainer, Failed_Signup.newInstance(obj.data!!.description), null)
            }
            else if(obj.data!!.decisionId == Constants.OTP_SCREEN)
            {
                accountViewModel.sendVerificationCode(true)
                // EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
            }
            else if(obj.data!!.decisionId == Constants.ID_VERFICATION)
            {
                EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
            }

        })

        accountViewModel.mediaSent.observe(viewLifecycleOwner, Observer { obj ->
           //ActivityBase.activity.showToastMessage("success Case")

            if(obj.data!!.decisionId == Constants.INSTANT_PASS)
            {
                callFragmentWithReplace(R.id.authContainer, SuccessFragment(), "SignInFragment")
            }
            else if(obj.data!!.decisionId == Constants.FAIL)
            {
                //ActivityBase.activity.showToastMessage("Instant Failed")
                //showInfoDialog("", obj.messages.toString(), this)
                //callFragmentWithReplace(R.id.mainContainer, SignupFragment(), null)
                callFragmentWithReplace(R.id.authContainer, Failed_Signup.newInstance(obj.data!!.description), null)
            }
            else if(obj.data!!.decisionId == Constants.OTP_SCREEN)
            {
                accountViewModel.sendVerificationCode(true)
                // EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
            }
            else if(obj.data!!.decisionId == Constants.ID_VERFICATION)
            {
                EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
            }

        })
    }

    private fun setListener() {
        binding.btnContinue.setOnClickListener(this)
        binding.tvCancel.setOnClickListener(this)
        binding.ivNoDoc.setOnClickListener(this)
        binding.ivDoc.setOnClickListener(this)
        binding.ivNoDocBack.setOnClickListener(this)
        binding.ivDocBack.setOnClickListener(this)
        binding.ivSelfie.setOnClickListener(this)
        binding.ivNoSelfie.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnContinue -> {
                if (validateInput()) {
                    /**trigger event when user clicks on continue*/
                    val paramBundle = Bundle()
                    paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                    FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                        requireContext(),
                        CustomEventNames.EVENT_CLICK_ID_VER_CONT,
                        paramBundle
                    )

                    sendMediaFiles()
                }
            }
            R.id.tvCancel -> {
                EnrollmentFragment.binding.vpEnrol.setCurrentItem(0, true)
            }
            R.id.ivNoDoc -> {
//                if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.CAMERA)) {
//                    ActivityBase.activity.startCamera(REQ_CODE_DOC_FRONT)
//                }
                val cameraIntent = Intent(context, AcuantCameraActivity::class.java)

                cameraIntent.putExtra(
                    ACUANT_EXTRA_CAMERA_OPTIONS,
                    AcuantCameraOptions
                        .DocumentCameraOptionsBuilder()
                        .build()
                    /*Acuant has temporarily kept the constructor public for backward compatibility,
                     * but it will become private in the near future. Acuant strongly recommends that
                     * you use the provided builder for all new implementations.*/
                )

                startActivityForResult(cameraIntent, 4)
            }
            R.id.ivDoc -> {
                /*if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.CAMERA)) {
                    ActivityBase.activity.startCamera(REQ_CODE_DOC_FRONT)
                }*/
                val cameraIntent = Intent(context, AcuantCameraActivity::class.java)

                cameraIntent.putExtra(
                    ACUANT_EXTRA_CAMERA_OPTIONS,
                    AcuantCameraOptions
                        .DocumentCameraOptionsBuilder()
                        .build()
                    /*Acuant has temporarily kept the constructor public for backward compatibility,
                     * but it will become private in the near future. Acuant strongly recommends that
                     * you use the provided builder for all new implementations.*/
                )

                startActivityForResult(cameraIntent, 4)
            }
            R.id.ivNoDocBack -> {
                /* if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.CAMERA)) {
                    ActivityBase.activity.startCamera(REQ_CODE_DOC_BACK)
                }*/

                val cameraIntent = Intent(context, AcuantCameraActivity::class.java)

                cameraIntent.putExtra(
                    ACUANT_EXTRA_CAMERA_OPTIONS,
                    AcuantCameraOptions
                        .DocumentCameraOptionsBuilder()
                        .build()
                    /*Acuant has temporarily kept the constructor public for backward compatibility,
                     * but it will become private in the near future. Acuant strongly recommends that
                     * you use the provided builder for all new implementations.*/
                )

                startActivityForResult(cameraIntent, 3)
            }
            R.id.ivDocBack -> {
                /*if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.CAMERA)) {
                    ActivityBase.activity.startCamera(REQ_CODE_DOC_BACK)
                }*/

                val cameraIntent = Intent(context, AcuantCameraActivity::class.java)

                cameraIntent.putExtra(
                    ACUANT_EXTRA_CAMERA_OPTIONS,
                    AcuantCameraOptions
                        .DocumentCameraOptionsBuilder()
                        .build()
                    /*Acuant has temporarily kept the constructor public for backward compatibility,
                     * but it will become private in the near future. Acuant strongly recommends that
                     * you use the provided builder for all new implementations.*/
                )

                startActivityForResult(cameraIntent, 3)
            }
            R.id.ivSelfie -> {
                /*if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.CAMERA)) {
                    ActivityBase.activity.startFrontCamera(REQ_CODE_SELFIE)
                    Toast.makeText(context,"Selfie", Toast.LENGTH_SHORT).show();
                }*/

               // Toast.makeText(context, "IMAGE CAPTURED!", Toast.LENGTH_SHORT).show();

                val cameraIntent = Intent(
                    context,
                    FacialLivenessActivity::class.java
                )
                startActivityForResult(cameraIntent, 2)
            }
            R.id.ivNoSelfie -> {
                /* if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.CAMERA)) {
                    ActivityBase.activity.startFrontCamera(REQ_CODE_SELFIE)
                }*/

                //Toast.makeText(context, "IMAGE CAPTURED!", Toast.LENGTH_SHORT).show();

                val cameraIntent = Intent(
                    context,
                    FacialLivenessActivity::class.java
                )
                startActivityForResult(cameraIntent, 2)
            }
        }
    }

    private fun sendMediaFiles() {
    /*    b64FrontDoc = ActivityBase.activity.uriToBase64(
            ActivityBase.activity.compressFile(
                docFrontUri!!
            )
        )
        b64BackDoc = ActivityBase.activity.uriToBase64(ActivityBase.activity.compressFile(docBackUri!!))
        b64Selfie = ActivityBase.activity.uriToBase64(ActivityBase.activity.compressFile(selfieUri!!))*/
        b64FrontDoc = encodeImage(front_bitmap!!).toString()
        b64BackDoc = encodeImage(back_bitmap!!).toString()
        b64Selfie = encodeImage(selfie_bitmap!!).toString()

        val list = ArrayList<Document>()
        list.add(Document(Constants.MEDIA_DOC_FRONT, b64FrontDoc))
        list.add(Document(Constants.MEDIA_DOC_BACK, b64BackDoc))
        list.add(Document(Constants.MEDIA_SELFIE, b64Selfie))

        accountViewModel.sendMediaFiles(
            MediaRequestModel(
                MovoApp.db.getString(Constants.MY_USER_ID)!!,
                list
            )
        )
    }

    private fun validateInput(): Boolean {
        if (!binding.isDocSet!!) {
            ActivityBase.activity.showToastMessage("Please Upload Document Front..")
            return false
        } else if (!binding.isDocBack!!) {
            ActivityBase.activity.showToastMessage("Please Upload Document Back..")
            return false
        }else if (!binding.isImageSet!!) {
            ActivityBase.activity.showToastMessage("Please Upload Selfie..")
            return false
        } else {
            return true
        }
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CODE_DOC_FRONT ->
                    ActivityBase.activity.processCapturedPhoto(object : ICallBackUri {
                        override fun imageUri(result: Uri?) {
                            cropFragment = CropFragment.newInstance(result!!.path!!, true)
                            cropFragment.setTargetFragment(
                                ActivityBase.activity.getRootParentFragment(
                                    this@MediaFragment
                                ), REQ_CODE_CROP_FRONT
                            )
                            addFragment(R.id.authContainer, cropFragment, "photoEffectFragment")
                        }
                    })
                REQ_CODE_SELFIE ->
                    ActivityBase.activity.processCapturedPhoto(object : ICallBackUri {
                        override fun imageUri(result: Uri?) {
                            cropCircleFragment = CircularCropFragment.newInstance(
                                result!!.path!!,
                                true
                            )
                            cropCircleFragment.setTargetFragment(
                                ActivityBase.activity.getRootParentFragment(
                                    this@MediaFragment
                                ), REQ_CODE_CROP_SELFIE
                            )
                            addFragment(
                                R.id.authContainer,
                                cropCircleFragment,
                                "photoEffectFragment"
                            )

                            //Toast.makeText(context, "ERROR OCCURED!!!", Toast.LENGTH_SHORT).show();
                        }
                    })

                REQ_CODE_CROP_FRONT -> {
                    if (data != null) {
                        docFrontUri = Uri.parse(data.getStringExtra("cropImage"))
                        binding.ivDoc.setImageBitmap(
                            ActivityBase.activity.handleSamplingAndRotationBitmap(
                                docFrontUri!!
                            )
                        )
                        binding.isDocSet = true
                    }
                }
                REQ_CODE_CROP_BACK -> {
                    if (data != null) {
                        docBackUri = Uri.parse(data.getStringExtra("cropImage"))
                        binding.ivDocBack.setImageBitmap(
                            ActivityBase.activity.handleSamplingAndRotationBitmap(
                                docBackUri!!
                            )
                        )
                        binding.isDocBack = true
                    }
                }

                REQ_CODE_CROP_SELFIE -> {
                    if (data != null) {
                        selfieUri = Uri.parse(data.getStringExtra("cropImage"))
                        binding.ivSelfie.setImageBitmap(
                            ActivityBase.activity.handleSamplingAndRotationBitmap(
                                selfieUri!!
                            )
                        )
                        binding.isImageSet = true
                    }
                }

                REQ_CODE_DOC_BACK -> {
                    ActivityBase.activity.processCapturedPhoto(object : ICallBackUri {
                        override fun imageUri(result: Uri?) {
                            cropFragment = CropFragment.newInstance(result!!.path!!, false)
                            cropFragment.setTargetFragment(
                                ActivityBase.activity.getRootParentFragment(
                                    this@MediaFragment
                                ), REQ_CODE_CROP_BACK
                            )
                            addFragment(R.id.authContainer, cropFragment, "photoEffectFragment")
                        }
                    })
                }
            }
        }

        if (requestCode == 2) {
            if(resultCode == FacialLivenessActivity.RESPONSE_SUCCESS_CODE){
                val faceImage = FaceCapturedImage.bitmapImage
               // imageview?.setImageBitmap(faceImage)
                binding.ivSelfie.setImageBitmap(faceImage)
                binding.isImageSet = true
                selfie_bitmap = faceImage
               // Toast.makeText(context, "IMAGE CAPTURED!", Toast.LENGTH_SHORT).show();
            }
            else{
                //handle error
                Toast.makeText(context, "ERROR OCCURED!!!", Toast.LENGTH_SHORT).show();
            }
        }

        val REQUEST_CODE=4
        if (requestCode == REQUEST_CODE  ) {
            val capturedImageUrl = data?.getStringExtra(ACUANT_EXTRA_IMAGE_URL)
            val capturedBarcodeString = data?.getStringExtra(ACUANT_EXTRA_PDF417_BARCODE)
            //  imageview?.setImageBitmap(capturedImageUrl)

            AcuantImagePreparation.evaluateImage(
                requireContext(),
                CroppingData(capturedImageUrl!!),
                object :
                    EvaluateImageListener {

                    override fun onSuccess(image: AcuantImage) {

                        /*Glide.with(this@MainActivity).load(image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(imageview!!);*/

                       val img: Bitmap = image.image

                        binding.ivDoc.setImageBitmap(img)
                        front_bitmap = img

                    }

                    override fun onError(error: com.acuant.acuantcommon.model.Error) {
                        //showAcuDialog(error.errorDescription)
                    }
                })
            binding.isDocSet = true
        }


        if (requestCode == 3  ) {
            val capturedImageUrl = data?.getStringExtra(ACUANT_EXTRA_IMAGE_URL)
            val capturedBarcodeString = data?.getStringExtra(ACUANT_EXTRA_PDF417_BARCODE)
            //  imageview?.setImageBitmap(capturedImageUrl)

            AcuantImagePreparation.evaluateImage(
                requireContext(),
                CroppingData(capturedImageUrl!!),
                object : EvaluateImageListener {

                    override fun onSuccess(image: AcuantImage) {

                        /*Glide.with(this@MainActivity).load(image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(imageview!!);*/

                        val img: Bitmap = image.image

                        binding.ivDocBack.setImageBitmap(img)
                        back_bitmap= img



                    }

                    override fun onError(error: com.acuant.acuantcommon.model.Error) {
                        //showAcuDialog(error.errorDescription)
                    }
                })
            binding.isDocBack = true
        }
    }

    override fun onClickOk() {
        EnrollmentFragment.binding.vpEnrol.setCurrentItem(2, true)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT)
    }

    val initCallback = object: com.acuant.acuantcommon.initializer.IAcuantPackageCallback {

        override fun onInitializeSuccess() {

             //Toast.makeText(context,"SDK INITIALIZED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

        }

        override fun onInitializeFailed(error: List<com.acuant.acuantcommon.model.Error>) {

           // Toast.makeText(context,"SDK NOT INITIALIZED", Toast.LENGTH_SHORT).show();

        }
    }

}

