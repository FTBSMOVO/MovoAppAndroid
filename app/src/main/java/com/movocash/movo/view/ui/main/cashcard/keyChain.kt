package com.movocash.movo.view.ui.main.cashcard

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.data.model.responsemodel.GetFrequesnciesResponseModel
import com.movocash.movo.data.model.responsemodel.getUserScheduleReloadResponseModel
import com.movocash.movo.databinding.FragmentKeyChainBinding
import com.movocash.movo.databinding.FragmentSchedulerAndKeyChainBinding
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.CardViewModel
import com.movocash.movo.viewmodel.CommonViewModel


class keyChain : BaseFragment(), View.OnClickListener {



    private lateinit var commonViewModel: CommonViewModel
    private var mFrequencyList: ArrayList<GetFrequesnciesResponseModel.Model> = ArrayList()
    private var questionList: ArrayList<String> = ArrayList()
    private var questionPosition = 0
    private lateinit var cardsViewModel: CardViewModel
    private var referenceId = ""
    private var isEnabled = true

    // private lateinit var getData: AddUserScheduleReloadsRequestModel


    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var mList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()

    lateinit var primaryCardObj: GetCardAccountsResponseModel.Card

    private lateinit var getDataid: String
    private lateinit var getDatacardReferenceId: String
    private var getDataisEnabled: Boolean = true
    private lateinit var getDataserviceProvider: String
    private var getDatapaymentAmount: Double = 0.0
    private lateinit var getDatapaymentDueDate: String
    private lateinit var getDataautoReloadDate: String
    private lateinit var getDatanextPaymentDueDate: String
    private lateinit var getDatanextAutoReloadDate: String
    private  var getDatafrequencyId:Int = 0
    private lateinit var getDataObj: getUserScheduleReloadResponseModel


    lateinit var binding: FragmentKeyChainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_key_chain, container, false)
        // binding.tvTitle.isSelected = true
        setListeners()
        //setUiObserver()


       // binding.etPasss.setText("***")


        val sharedPrefNotSaved = activity?.getPreferences(Context.MODE_PRIVATE)
        val sharedPref = activity?.getSharedPreferences("mydb",Context.MODE_PRIVATE)


        // https://developer.android.com/training/data-storage/shared-preferences
        // read goes here
        val temp = sharedPref!!.getString(keyChain.secondaryRefID, "")
        val temp2 = sharedPref!!.getString(keyChain.secondaryRefID+2, "")

        if(temp!="")
        {
            binding.etUserName.setText(temp)
        }
        if(temp2!="")
        {
            binding.etServiceProvider.setText(temp2)
        }
        if(temp=="" && temp2=="")
        {
            binding.rlRight.visibility=View.GONE
        }
        else
        {
            binding.rlRight.visibility=View.VISIBLE
            binding.btnCancel.visibility=View.GONE
            binding.btnConfirm.visibility=View.GONE
            disableAll()
        }

        // activateReviewInfo()
        return binding.root
    }

    private fun setListeners() {
         binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.rlLeft.setOnClickListener(this)
        binding.eyeIcon.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()

            R.id.rlRight -> {

                binding.btnCancel.visibility=View.VISIBLE
                binding.btnConfirm.visibility=View.VISIBLE
                enableAll()

            }
            R.id.eyeIcon -> {

                val sharedPrefNotSaved = activity?.getPreferences(Context.MODE_PRIVATE)
                val sharedPref = activity?.getSharedPreferences("mydb", Context.MODE_PRIVATE)
                /*val temp1 = sharedPref!!.getString(secondaryRefID+1, "")
                    binding.etPasss.setText(temp1)*/
                if (binding.eyeIcon.isChecked) {
                    binding.etPasss.setText("***")
                } else {
                    val temp1 = sharedPref!!.getString(keyChain.secondaryRefID + 1, "")
                    binding.etPasss.setText(temp1)
                }

            }
            R.id.btnCancel -> {

                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()

            }
            R.id.btnConfirm->{
                if(binding.etUserName.text.toString()!="") {
                    MovoApp.db.putString(keyChain.secondaryRefID, binding.etUserName.text.toString())
                    MovoApp.db.putString(keyChain.secondaryRefID + 1, binding.etPasss.text.toString())


                    val sharedPrefNotSaved = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                    val sharedPref = activity?.getSharedPreferences("mydb",Context.MODE_PRIVATE) ?: return
                    with (sharedPref.edit()) {
                        putString(keyChain.secondaryRefID, binding.etUserName.text.toString())
                        // add more
                        apply()
                    }
                    if(binding.etPasss.text.toString()!="***") {
                        with(sharedPref.edit()) {
                            putString(keyChain.secondaryRefID + 1, binding.etPasss.text.toString())
                            // add more
                            apply()
                        }
                    }
                    with (sharedPref.edit()) {
                        putString(keyChain.secondaryRefID + 2, binding.etServiceProvider.text.toString())
                        // add more
                        apply()
                    }
                }
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()

            }
        }
    }



    private fun disableAll()
    {
        //binding.eyeIcon.isEnabled=false
        disableEditText(binding.etServiceProvider)

        disableEditText(binding.etUserName)
        disableEditText(binding.etPasss)
    }

    private fun enableAll()
    {
        //  binding.eyeIcon.isEnabled=true
        enableEditText(binding.etServiceProvider)

        enableEditText(binding.etUserName)
        enableEditText(binding.etPasss)
    }

    private fun disableEditText(editText: EditText) {
        /* editText.isFocusable = false*/
        editText.isEnabled = false
        /*editText.isCursorVisible = false*/
        // editText.keyListener = null
        editText.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun enableEditText(editText: EditText) {
        editText.isFocusable = true
        editText.isEnabled = true
        editText.isCursorVisible = true
        // editText.setOnClickListener(this)
        // editText.keyListener = null
        editText.setTextColor(Color.BLACK)
        editText.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun disableTextView(textview: TextView) {

        textview.setOnClickListener(null)

    }
    private fun disableImageView(textview: ImageView) {

        textview.setOnClickListener(null)

    }
    private fun enableImageView(textview: ImageView) {

        textview.setOnClickListener(this)

    }
    private fun enableTextView(textview: TextView) {

        textview.setOnClickListener(this)
        textview.setTextColor(Color.BLACK)

    }



    companion object {
        lateinit var instance: keyChain
        var secondaryRefID = ""

        fun newInstance(isMain: String): keyChain {
            instance = keyChain()
            secondaryRefID = isMain
            return instance
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()

                callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


}