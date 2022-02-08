package com.movocash.movo.view.ui.main.others

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.ContactModel
import com.movocash.movo.databinding.FragmentAddressBookBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.hideKeyboard
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.utilities.helper.Permissions
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.others.adapter.AdapterAddressBook
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
/*import com.github.tamir7.contacts.Contact
import com.github.tamir7.contacts.Contacts*/
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import java.util.*
import kotlin.collections.ArrayList

class AddressBookFragment : BaseFragment(), View.OnClickListener, TextWatcher, AdapterAddressBook.IContactListener {

    lateinit var binding: FragmentAddressBookBinding
    private var adapterContacts: AdapterAddressBook? = null
    private var contactsList = ArrayList<ContactModel>()
    private var searchList = ArrayList<ContactModel>()
    private lateinit var phoneNumberUtil: PhoneNumberUtil
    var finalList: ArrayList<ContactModel> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_address_book, container, false)
        setListeners()
        seeIfPermissions()
        return binding.root
    }

    private fun setListeners() {
        binding.btnSyncNow.setOnClickListener(this)
        binding.etSearch.addTextChangedListener(this)
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
    }

    private fun seeIfPermissions() {
        binding.isLoader = false
        if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.READ_CONTACTS)) {
            if (Constants.CONTACT_LIST.size > 0) {
                binding.isSync = true
                setAdapter()
            } else {
                binding.isSync = true
                syncContacts()
            }
        } else {
            binding.isSync = false
        }
    }

    private fun setAdapter() {
        finalList = ArrayList(ArrayList(Constants.CONTACT_LIST.distinctBy { it.number }).sortedBy { it.name!!.toLowerCase(Locale.ROOT) })
        searchList.clear()
        searchList.addAll(finalList)
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvPhonebook.layoutManager = manager
        adapterContacts = AdapterAddressBook(finalList)
        binding.rvPhonebook.adapter = adapterContacts
        adapterContacts!!.setMyListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {

            }
            R.id.btnSyncNow -> {
                checkPermissions()
            }
        }
    }

    private fun checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(ActivityBase.activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri: Uri = Uri.fromParts("package", ActivityBase.activity.packageName, null)
            intent.data = uri
            startActivity(intent)
        } else {
            if (Permissions.checkPermission(ActivityBase.activity)) {
                binding.isSync = true
                syncContacts()
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    binding.isSync = true
                    syncContacts()
                }
            }
        }
    }

    private fun syncContacts() {
        binding.isLoader = true
        phoneNumberUtil = PhoneNumberUtil.createInstance(ActivityBase.activity)
        /*val contacts = Contacts.getQuery().find()
        setDataInModel(contacts)*/
    }

  /*  private fun setDataInModel(data: List<Contact>) {
        for (i in data.indices) {
            val name = data[i].displayName
            var number = ""
            if (data[i].phoneNumbers.size > 0) {
                if (data[i].phoneNumbers[0].normalizedNumber != null)
                    number = data[i].phoneNumbers[0].normalizedNumber
                else if (data[i].phoneNumbers[0].number != null) {
                    number = data[i].phoneNumbers[0].number
                }
            }
            var countryCode = ""
            try {
                if (!TextUtils.isEmpty(number)) {
                    val numberProto: Phonenumber.PhoneNumber = phoneNumberUtil.parse(number, "")
                    countryCode = "+${numberProto.countryCode}"
                    val obj = ContactModel(countryCode, numberProto.nationalNumber.toString(), name, false)
                    contactsList.add(obj)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        contactsList = contactsList.distinctBy { it.number } as ArrayList
        Constants.CONTACT_LIST = contactsList
        binding.isLoader = false
        setAdapter()
    }*/


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val permissionResults = java.util.ArrayList<Int>()
        for (grantResult in grantResults) {
            permissionResults.add(grantResult)
        }
        if (permissionResults.contains(PackageManager.PERMISSION_DENIED)) {
            ActivityBase.activity.showToastMessage("Permissions Rejected")
            if (!Permissions.checkPermission(ActivityBase.activity)) {
                Permissions.requestPermission(ActivityBase.activity)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun afterTextChanged(s: Editable?) {
        searchContact(s!!, finalList, searchList)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }


    private fun searchContact(s: Editable, mList: ArrayList<ContactModel>, mSearchList: ArrayList<ContactModel>?) {
        if (s.isNotEmpty()) {
            if (mList == null)
                return
            mList.clear()
            if (mSearchList != null && mSearchList.size > 0) {
                for (i in 0 until mSearchList.size) {
                    if (mSearchList[i].name!!.toLowerCase().startsWith(s.trim().toString().toLowerCase())) {
                        mList.add(mSearchList[i])
                    }
                }
            }

            if (mList.size != 0) {
                mList.sortBy { it.name }
            }
            if (adapterContacts != null)
                adapterContacts!!.notifyDataSetChanged()
        } else {
            if (mSearchList == null)
                return
            if (mList != null)
                mList.clear()
            mSearchList.sortBy { it.name }
            mList.addAll(mSearchList)
            if (adapterContacts != null)
                adapterContacts!!.notifyDataSetChanged()
        }
    }

    override fun onClickContact(position: Int) {
        binding.root.hideKeyboard()
        if (finalList.any { it.isSelected }) {
            val number = finalList.filter { it.isSelected }[0].number
            val countryCode = finalList.filter { it.isSelected }[0].countryCode
            val intent = Intent()
            intent.putExtra("number", number)
            intent.putExtra("countryCode", countryCode)
            targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
        }
    }
}
