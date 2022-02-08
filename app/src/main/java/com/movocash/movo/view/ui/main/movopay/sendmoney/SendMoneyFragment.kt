package com.movocash.movo.view.ui.main.movopay.sendmoney

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.ShareFundRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentSendMoneyBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.utilities.helper.DecimalFilter
import com.movocash.movo.utilities.helper.Permissions
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.AddressBookFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import java.lang.reflect.Type

class SendMoneyFragment : BaseFragment(), View.OnClickListener, ICardClickListener {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    lateinit var binding: FragmentSendMoneyBinding
    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var mList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private val REQ_CODE_CONTACT = 1111
    private lateinit var addressBookFragment: AddressBookFragment
    private var referenceId = ""
    private var primaryAmount = 0.0
    val RESULT_PICK_CONTACT = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_money, container, false)
        binding.tvTitle.isSelected = true
        checkAndSet()
        setListeners()
        requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                    if(granted){
                                        val contactPickerIntent = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                )
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT)
                    }else{
                        Toast.makeText(activity, "Contact permission required to proceed further", Toast.LENGTH_LONG).show()
                    }
                }

        return binding.root
    }

    private fun checkAndSet() {
        binding.isCardSet = false
        var cardModel: GetCardAccountsResponseModel? = null
        val listType: Type = object : TypeToken<GetCardAccountsResponseModel>() {}.type
        cardModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_CARD_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }

        if (cardModel?.obj != null && cardModel.obj!!.cards != null && cardModel.obj!!.cards!!.size > 0) {
            mList.clear()
            mList = cardModel.obj!!.cards!!
            mList1 = ArrayList(mList.filter { it.isPrimaryCardSpecified && it.statusCode!="F"})

            setPrimaryCardData(0)
        }
    }

    private fun setCardData(position: Int) {
        val cardNumber = "*" + mList[position].cardNumber!!.substring(mList[position].cardNumber!!.length - 4)
        val num = "${mList[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = mList[position].balance
        binding.isCardSet = true
        referenceId = mList[position].referenceID!!
        primaryAmount = mList[position].balance
    }
    private fun setPrimaryCardData(position: Int) {
        val cardNumber = "*" + mList1[position].cardNumber!!.substring(mList1[position].cardNumber!!.length - 4)
        val num = "${mList1[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = mList1[position].balance
        binding.isCardSet = true
        referenceId = mList1[position].referenceID!!
        primaryAmount = mList1[position].balance
    }

    private fun setListeners() {
        //binding.llMoney.setOnClickListener(this)
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.ivContact.setOnClickListener(this)
        binding.etAmount.addTextChangedListener(
            DecimalFilter(
                binding.etAmount,
                ActivityBase.activity
            )
        )
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivContact -> {
                    checkPermissions()
                //////////////////////////////////


                /////////////////////////////////////
//                val contactPickerIntent = Intent(
//                    Intent.ACTION_PICK,
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
//                )
//                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT)
                /*binding.root.hideKeyboard()
                addressBookFragment = AddressBookFragment()
                addressBookFragment.setTargetFragment(this@SendMoneyFragment, REQ_CODE_CONTACT)
                addFragment(R.id.mainContainer, addressBookFragment, "selectCatFragment")*/
            }
            R.id.llMoney -> showAccountSelectionDialog(mList, false, this)
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.rlRight -> {
                if (validateInput()) {
                    binding.root.hideKeyboard()
                    val obj = ShareFundRequestModel(
                        referenceId,
                        binding.etAmount.text.toString().toDouble(),
                        binding.etEmail.text.toString(),
                        binding.etComments.text.toString()
                    )
                    addFragment(
                        R.id.mainContainer, SendMoneyConfirmationFragment.newInstance(
                            binding.cardNum!!,
                            obj,
                            primaryAmount
                        ), "SendMoneyDetailsFragment"
                    )
                }
            }
        }
    }

    override fun onClickCard(position: Int) {
        setCardData(position)
    }

 /*   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CODE_CONTACT -> {
                    if (data != null) {
                        binding.etEmail.setText("${data.getStringExtra("countryCode")}${data.getStringExtra("number")}")
                    }
                }
            }
        }
    }*/

    private fun validateInput(): Boolean {
        if (!binding.isCardSet!!) {
            ActivityBase.activity.showToastMessage("Please Select A card")
            return false
        } else if (TextUtils.isEmpty(binding.etAmount.text.toString())) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            return false
        }  else if (binding.etAmount.text.toString().toDouble() == 0.0) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Please Enter Valid Amount")
            return false
        } else if (binding.etAmount.text.toString().toDouble() >= binding.cardAmount!!) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("The amount you are requesting to transfer is greater than your available balance.")
            return false
        } else if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
            binding.etEmail.requestFocus()
            binding.etEmail.errorAnim(ActivityBase.activity)
            return false
        } else {
            return true
        }
    }

    //////////////////////////////////////////
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                RESULT_PICK_CONTACT -> {
                    var cursor: Cursor? = null
                    try {
                        var phoneNo: String? = null
                        var name: String? = null
                        val uri: Uri? = data?.data
                        cursor = context?.contentResolver?.query(uri!!, null, null, null, null)
                        cursor?.moveToFirst()
                        val phoneIndex: Int =
                            cursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val phoneContactName: String =
                            cursor!!.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        phoneNo = cursor?.getString(phoneIndex)
                        //Toast.makeText( ActivityBase.activity, phoneContactName + phoneNo, Toast.LENGTH_LONG ).show()
                        binding.etEmail.setText(phoneNo)
                        //et_name.setText(phoneContactName) //setting the contacts name
                        // mobile_number.setText(phoneNo) //setting the contacts phone number

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            Log.e("ActivityName", "Something went wrong")
        }
    }

//    override fun registerForActivityResult(activityResultContracts: ActivityResultContracts.RequestPermission) {
//
//    }

    private fun checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(ActivityBase.activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            val uri: Uri = Uri.fromParts("package", ActivityBase.activity.packageName, null)
//            intent.data = uri
//            startActivity(intent)
            requestPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
        }
        else
        {
            val contactPickerIntent = Intent(
                Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            )
            startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT)
        }
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