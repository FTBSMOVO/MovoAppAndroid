package com.movocash.movo.view.ui.main.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.AddressInfo
import com.movocash.movo.data.model.requestmodel.UpdateProfileRequestModel
import com.movocash.movo.data.model.requestmodel.UploadProfilePicRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.data.model.responsemodel.IDNameResponseModel
import com.movocash.movo.data.remote.callback.ICallBackUri
import com.movocash.movo.databinding.FragmentProfileBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.utilities.helper.Permissions
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.BottomSheetSelectorFragment
import com.movocash.movo.view.ui.bottomsheet.MediaBottomSheetFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.movopay.sendmoney.SendMoneyFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.CommonViewModel
import java.io.IOException
import java.lang.reflect.Type
import java.util.*

class ProfileFragment : BaseFragment(), View.OnClickListener, MediaBottomSheetFragment.ISelectListener, BottomSheetSelectorFragment.ISelectListener {

    lateinit var binding: FragmentProfileBinding
    private var isContactOpen = true
    private var isAddressOpen = true
    private var isShipOpen = true
    private lateinit var bottomSheetListFragment: MediaBottomSheetFragment
    private lateinit var accountViewModel: AccountViewModel
    private var itemList: ArrayList<String> = ArrayList()
    private val REQ_CODE_CAMERA = 1
    private val REQ_CODE_GALLERY = 2
    private var cameraImageUri: Uri? = null
    private lateinit var commonViewModel: CommonViewModel
    private var cardList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var cardList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var myStateList: ArrayList<String> = ArrayList()
    private var statePosition = 0
    private var shippingStatePosition = 0
    private var stateList: ArrayList<IDNameResponseModel.Model> = ArrayList()
    private var isStateSet = false
    private var isNumberValid = false
    lateinit var reqObj: UpdateProfileRequestModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUiObserver()
        setViews()
        setListeners()
        callApis()
        return binding.root
    }

    private fun callApis() {
        accountViewModel.getMyProfile(true)
    }

    private fun setUiObserver() {
        commonViewModel.statesResponse.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.list != null && obj.list!!.size > 0) {
                    if (stateList.size > 0)
                        stateList.clear()
                    stateList.addAll(obj.list!!)

                    if (binding.model != null) {
                        if (stateList.any { it.iso2 == binding.model!!.stateCode!! }) {
                            statePosition = stateList.indexOf(stateList.filter { it.iso2 == binding.model!!.stateCode!! }[0])
                           // binding.tvSelectedState.text = stateList.filter { it.iso2 == binding.model!!.stateCode!! }[0].name
                        }

                        if (!TextUtils.isEmpty(binding.model!!.billingStateCode)) {
                            if (stateList.any { it.iso2 == binding.model!!.billingStateCode }) {
                                shippingStatePosition = stateList.indexOf(stateList.filter { it.iso2 == binding.model!!.billingStateCode!! }[0])
                                //binding.tvSelectedShippingState.text = stateList.filter { it.iso2 == binding.model!!.billingStateCode!! }[0].name
                            }
                        }
                    }
                }
            }
        })

        commonViewModel.urlResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.data != null && obj.data.size > 0 && !TextUtils.isEmpty(obj.data[0].url)) {
                    accountViewModel.uploadProfilePic(UploadProfilePicRequestModel(obj.data[0].url, obj.data[0].thumbUrl))
                    MovoApp.db.putString(Constants.USER_IMAGE, obj.data[0].url)
                    MovoApp.db.putString(Constants.USER_THUMB, obj.data[0].thumbUrl)
                }
            }
        })

        commonViewModel.urlFailure.observe(viewLifecycleOwner, Observer { msg ->
            binding.isFail = true
            binding.isLoading = false
            ActivityBase.activity.showToastMessage(msg)
        })

        accountViewModel.picUploadSuccess.observe(viewLifecycleOwner, Observer { msg ->
            binding.isFail = false
            binding.isLoading = false
            ActivityBase.activity.showToastMessage("Profile picture updated")
        })

        accountViewModel.picUploadFailure.observe(viewLifecycleOwner, Observer { msg ->
            binding.isFail = true
            binding.isLoading = false
            ActivityBase.activity.showToastMessage(msg)
        })


        accountViewModel.getProfileFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        accountViewModel.getProfileResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.data != null) {
                    MovoApp.db.putString(Constants.USER_NUMBER, obj.data!!.cellNumber!!)
                    MovoApp.db.putString(Constants.EMAIL, obj.data!!.email!!)

                    if (!TextUtils.isEmpty(obj.data!!.billingAddress1)) {
                        val shipAddress = "${obj.data!!.billingAddress1}, ${obj.data!!.city} , ${obj.data!!.billingStateCode},${obj.data!!.billingPostalCode} , USA"
                        MovoApp.db.putString(Constants.CONST_MY_SHIPPING_INFO, shipAddress)
                    } else
                        MovoApp.db.putString(Constants.CONST_MY_SHIPPING_INFO, "")

                    binding.model = obj.data
                    commonViewModel.getStateByCountry(233)
                }
            }
        })

        accountViewModel.profileCodeFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })

        accountViewModel.profileCodeSent.observe(viewLifecycleOwner, Observer {
            callFragmentWithReplace(R.id.mainContainer, VerifyProfileCodeFragment.newInstance(reqObj), "VerifyProfileCodeFragment")
        })


    }

    private fun setViews() {
        binding.tvUsername.setText(MovoApp.db.getString(Constants.REMEMBERED_USER_NAME))
        binding.ccpNumber.registerPhoneNumberTextView(binding.tvHomePhone)
        binding.ccpNumber.setPhoneNumberInputValidityListener { _, b ->
            isNumberValid = b
        }
        if (!TextUtils.isEmpty(MovoApp.db.getString(Constants.USER_THUMB))) {
            binding.isImageSet = true
            binding.ivUser.load(MovoApp.db.getString(Constants.USER_THUMB)!!)
        } else
            binding.isImageSet = false

        binding.isFail = false
        binding.isLoading = false
        binding.tvName.text = "${MovoApp.db.getString(Constants.FIRST_NAME)} ${MovoApp.db.getString(Constants.LAST_NAME)}"
        binding.tvTitle.isSelected = true
        var cardModel: GetCardAccountsResponseModel? = null
        val listType: Type = object : TypeToken<GetCardAccountsResponseModel>() {}.type
        cardModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_CARD_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }

        if (cardModel?.obj != null   && cardModel.obj!!.cards != null  && cardModel.obj!!.cards!!.size > 0) {

            //val cardList = cardModel.obj!!.cards

            cardList.clear()
            cardList = cardModel.obj!!.cards!!
            cardList1 = ArrayList(cardList.filter { it.isPrimaryCardSpecified && it.statusCode!="F"})

           /* var primaryList = ArrayList<GetCardAccountsResponseModel.Card>()
            var secondaryList = ArrayList<GetCardAccountsResponseModel.Card>()
            if (cardList!!.any { it.isPrimaryCardSpecified }) {
                primaryList = ArrayList(cardList.filter { it.isPrimaryCardSpecified && it.statusCode != "F" })
            }*/


           /* val cardNumber =
                    "*" +  primaryList[0].cardNumber!!.substring( primaryList[0].cardNumber!!.length - 4)
            val num = "${ primaryList[0].programAbbreviation}${cardNumber}"
            binding.cardNum = num
            binding.cardAmount = primaryList[0].balance*/
            val cardNumber = "*" + cardList1[0].cardNumber!!.substring(cardList1[0].cardNumber!!.length - 4)
            val num = "${cardList1[0].programAbbreviation}${cardNumber}"
            binding.cardNum = num
            binding.cardAmount = cardList1[0].balance

            /*val cardNumber =
                    "*" + cardModel.obj!!.cards!![0].cardNumber!!.substring(cardModel.obj!!.cards!![0].cardNumber!!.length - 4)
            val num = "${cardModel.obj!!.cards!![0].programAbbreviation}${cardNumber}"
            binding.cardNum = num
            binding.cardAmount = cardModel.obj!!.cards!![0].balance*/
        }
      /*  binding.ccpNumber.setCountryForPhoneCode(+1)
        binding.ccpNumber.setDefaultCountryUsingNameCode("US")*/
    }

    private fun setListeners() {
        binding.rlRight.setOnClickListener(this)
        binding.rlContactHeader.setOnClickListener(this)
        binding.rlAddressHeader.setOnClickListener(this)
        binding.rlShippingHeader.setOnClickListener(this)
        binding.llCredit.setOnClickListener(this)
        binding.rlUserPic.setOnClickListener(this)
        binding.rlLeft.setOnClickListener(this)
        /*binding.rlStatePicker.setOnClickListener(this)
        binding.rlShippingStatePicker.setOnClickListener(this)*/
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlRight -> {
               /* if (validateInput()) {
                    val obj = AddressInfo(binding.tvAddress1.text.toString(), 233, stateList[statePosition].id, stateList[statePosition].iso2, binding.etCity.text.toString(), binding.etZip.text.toString())
                    val shipObj = AddressInfo(binding.tvShipping1.text.toString(), 233, stateList[shippingStatePosition].id, stateList[shippingStatePosition].iso2, binding.etShipCity.text.toString(), binding.etShipZip.text.toString())
                    reqObj = UpdateProfileRequestModel(binding.tvEmail.text.toString(), "+${binding.ccpNumber.phoneNumber.countryCode}", binding.ccpNumber.phoneNumber.nationalNumber.toString(), obj, shipObj)
                    accountViewModel.sendProfileCode()
                }*/
            }
           /* R.id.rlStatePicker -> if (stateList.size > 0) {
                showStateDialog(statePosition, 1)
            }
            R.id.rlShippingStatePicker -> if (stateList.size > 0) {
                showStateDialog(shippingStatePosition, 2)
            }*/
            R.id.rlUserPic -> {
                if (binding.isLoading!!) {
                    ActivityBase.activity.showToastMessage("Uploading...")
                } else if (binding.isFail!!) {
                    uploadProfilePic()
                } else
                    showImageDialog()
            }
            R.id.llCredit -> {}
            R.id.rlContactHeader -> {
               /* if (isContactOpen) {
                    binding.ivRightContact.rotation = 0f
                    isContactOpen = false
                    binding.elContact.collapse()
                } else {
                    binding.ivRightContact.rotation = 180f
                    isContactOpen = true
                    binding.elContact.expand()
                }*/
            }
            R.id.rlAddressHeader -> {
                /*if (isAddressOpen) {
                    binding.ivRightAddress.rotation = 0f
                    isAddressOpen = false
                    binding.elAddress.collapse()
                } else {
                    binding.ivRightAddress.rotation = 180f
                    isAddressOpen = true
                    binding.elAddress.expand()
                }*/
            }
            R.id.rlShippingHeader -> {
               /* if (isShipOpen) {
                    binding.ivRightShipping.rotation = 0f
                    isShipOpen = false
                    binding.elShipping.collapse()
                } else {
                    binding.ivRightShipping.rotation = 180f
                    isShipOpen = true
                    binding.elShipping.expand()
                }*/
            }
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
        }
    }

    private fun uploadProfilePic() {
        binding.isFail = false
        binding.isLoading = true
        commonViewModel.uploadFullImage(cameraImageUri)
    }

  /*  private fun validateInput(): Boolean {
        if (TextUtils.isEmpty(binding.tvEmail.text.toString())) {
            binding.tvEmail.requestFocus()
            binding.tvEmail.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Email cannot be empty")
            return false
        } else if (!ActivityBase.activity.isEmailValid(binding.tvEmail.text.toString())) {
            binding.tvEmail.requestFocus()
            binding.tvEmail.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Invalid Email")
            return false
        } else if (TextUtils.isEmpty(binding.tvHomePhone.text.toString())) {
            binding.tvHomePhone.requestFocus()
            binding.tvHomePhone.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Phone cannot be empty")
            return false
        }
        else if (binding.tvHomePhone.text.toString().length < 10) {
            binding.tvHomePhone.requestFocus()
            binding.tvHomePhone.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Invalid PhoneNum")
            return false
        }



        else if (TextUtils.isEmpty(binding.tvAddress1.text.toString())) {
            binding.tvAddress1.requestFocus()
            binding.tvAddress1.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Address cannot be empty")
            return false
        } else if (TextUtils.isEmpty(binding.etCity.text.toString())) {
            binding.etCity.requestFocus()
            binding.etCity.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("City cannot be empty")
            return false
        } else if (TextUtils.isEmpty(binding.etZip.text.toString())) {
            binding.etZip.requestFocus()
            binding.etZip.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("ZipCode cannot be empty")
            return false
        } else if (binding.etZip.text.toString().length < 5) {
            binding.etZip.requestFocus()
            binding.etZip.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Invalid Zipcode")
            return false
        }else if (TextUtils.isEmpty(binding.tvShipping1.text.toString())) {
            binding.tvShipping1.requestFocus()
            binding.tvShipping1.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Address cannot be empty")
            return false
        } else if (TextUtils.isEmpty(binding.etShipCity.text.toString())) {
            binding.etShipCity.requestFocus()
            binding.etShipCity.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("City cannot be empty")
            return false
        } else if (TextUtils.isEmpty(binding.tvSelectedShippingState.text.toString())) {
            ActivityBase.activity.showToastMessage("Please Select State")
            return false
        } else if (TextUtils.isEmpty(binding.etShipZip.text.toString())) {
            binding.etShipZip.requestFocus()
            binding.etShipZip.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("ZipCode cannot be empty")
            return false
        } else
            return true
    }*/

    private fun showStateDialog(pos: Int, type: Int) {
        if (myStateList.size > 0)
            myStateList.clear()

        stateList.mapTo(myStateList) { it.name }
        setBottomSheet(myStateList, pos, type)
    }

    private fun setBottomSheet(list: ArrayList<String>, selectedPosition: Int, type: Int) {
        val bottomSheet = BottomSheetSelectorFragment(list, selectedPosition, type)
        if (!bottomSheet.isAdded)
            bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
        bottomSheet.setMyListener(this)
    }

    override fun onSelect(pos: Int, type: Int) {
        when (type) {
            1 -> {
                statePosition = pos - 1
              //  binding.tvSelectedState.text = myStateList[pos]
                isStateSet = true
            }
            2 -> {
                shippingStatePosition = pos - 1
              //  binding.tvSelectedShippingState.text = myStateList[pos]
            }
        }

    }

    override fun onCancel(type: Int) {

    }

    private fun showImageDialog() {
        itemList.clear()
        itemList.add("Camera")
        itemList.add("Photo Album")
        bottomSheetListFragment = MediaBottomSheetFragment(itemList, "Cancel")
        bottomSheetListFragment.setMyListener(this)
        if (!bottomSheetListFragment.isAdded) {
            bottomSheetListFragment.show(ActivityBase.activity.supportFragmentManager, "")
        }
    }

    override fun onMediaSelect(pos: Int) {
        when (itemList[pos]) {
            "Camera" -> {
                if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.CAMERA)) {
                    ActivityBase.activity.startCamera(REQ_CODE_CAMERA)
                }
            }
            "Photo Album" -> {
               // if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        ActivityBase.activity.showGallery(REQ_CODE_GALLERY)
                    }
               // }
            }
        }
    }

    override fun onMediaCancel() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CODE_GALLERY ->
                    if (data != null) {
                        try {
                            ActivityBase.activity.processGalleryPhoto(data, object : ICallBackUri {
                                override fun imageUri(result: Uri?) {
                                    binding.isImageSet = true
                                    cameraImageUri = ActivityBase.activity.compressFile(result!!)
                                    binding.ivUser.setImageBitmap(ActivityBase.activity.handleSamplingAndRotationBitmap(cameraImageUri!!))
                                    uploadProfilePic()
                                }
                            })
                        } catch (e: IOException) {
                            e.printStackTrace()
                            ActivityBase.activity.showToastMessage("Failed")
                        }
                    }
                REQ_CODE_CAMERA ->
                    ActivityBase.activity.processCapturedPhoto(object : ICallBackUri {
                        override fun imageUri(result: Uri?) {
                            binding.isImageSet = true
                            cameraImageUri = ActivityBase.activity.compressFile(result!!)
                            binding.ivUser.setImageBitmap(ActivityBase.activity.handleSamplingAndRotationBitmap(cameraImageUri!!))
                            uploadProfilePic()
                        }
                    })
            }
        }
    }

    //////////////////////////////////////////////////////

    private fun saveCardsResponse(obj: GetCardAccountsResponseModel) {
        val cardList = obj.obj!!.cards

        var primaryList = ArrayList<GetCardAccountsResponseModel.Card>()
        var secondaryList = ArrayList<GetCardAccountsResponseModel.Card>()
        if (cardList!!.any { it.isPrimaryCardSpecified }) {
            primaryList = ArrayList(cardList.filter { it.isPrimaryCardSpecified })
        }
        if (cardList.any { !it.isPrimaryCardSpecified }) {
            secondaryList = ArrayList(cardList.filter { !it.isPrimaryCardSpecified })
        }

        if (primaryList.isNotEmpty()) {
            cardList.clear()
            cardList.addAll(primaryList)
        }

        if (secondaryList.isNotEmpty())
            cardList.addAll(secondaryList)

        obj.obj!!.cards = cardList
        MovoApp.db.putString(Constants.CONST_CARD_RESPONSE, Gson().toJson(obj))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()


                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                // callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
                addFragment(R.id.mainContainer, MyCardsFragment(), null)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}