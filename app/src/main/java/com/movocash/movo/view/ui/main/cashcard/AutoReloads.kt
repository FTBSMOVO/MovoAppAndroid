package com.movocash.movo.view.ui.main.cashcard

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.AddUserScheduleReloadsRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.data.model.responsemodel.GetFrequesnciesResponseModel
import com.movocash.movo.data.model.responsemodel.getUserScheduleReloadResponseModel
import com.movocash.movo.databinding.FragmentAutoReloadsBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.utilities.utils.DateUtil
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.BottomSheetDatePicker2
import com.movocash.movo.view.ui.bottomsheet.BottomSheetDatePickerFragment
import com.movocash.movo.view.ui.bottomsheet.BottomSheetSelectorFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.CardViewModel
import com.movocash.movo.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.fragment_auto_reloads.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AutoReloads :  BaseFragment(), View.OnClickListener, BottomSheetDatePicker2.ISelectDobListener, BottomSheetDatePickerFragment.ISelectDobListener, BottomSheetSelectorFragment.ISelectListener,
    IInfoListener {

    private var paymentDate: String? = null
    private var reloadDate: String? = null

    lateinit var binding: FragmentAutoReloadsBinding
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
    private lateinit var getDataObj:getUserScheduleReloadResponseModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_auto_reloads,
            container,
            false
        )
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
       // val sharedPref = activity?.getSharedPreferences("mydb",Context.MODE_PRIVATE) ?: return


        commonViewModel.getFrequencies()
        cardsViewModel.getUserScheduleReload(secondaryRefID)
       // ActivityBase.activity.showToastMessage(MovoApp.db.getString(secondaryRefID).toString())




       // binding.etUserName.setText(MovoApp.db.getString(secondaryRefID))
        setListeners()
        setUiObserver()
        setViews()
       // checkAndSet()
        commonViewModel.getFrequencies()

        binding.amountDue.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                binding.amountDue.setText("")

        }


        return binding.root
    }

    private fun setViews() {

        /*if(getDataisEnabled)
        {
            binding.switch1.isChecked = true
        }
        else
        {
            binding.switch1.isChecked = false
        }*/

       // ActivityBase.activity.showToastMessage("switch : " + getDataisEnabled)



        //binding.switch1.isChecked

    }

    private fun setListeners() {
        binding.switch1.setOnClickListener(this)
        binding.etServiceProvider.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.rlLeft.setOnClickListener(this)
        binding.paymentAmount.setOnClickListener(this)
        binding.dueDate.setOnClickListener(this)
        binding.reloadDate.setOnClickListener(this)
        binding.nextDueDate.setOnClickListener(this)
        binding.nextReloadDate.setOnClickListener(this)
        binding.recurringCriteria.setOnClickListener(this)
        binding.etUserName.setOnClickListener(this)
        //binding.etPass.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)
        binding.rlLeft.setOnClickListener(this)
        binding.imageView.setOnClickListener(this)
        binding.imageView2.setOnClickListener(this)
        binding.textView4.setOnClickListener(this)
        binding.dueDateInfo.setOnClickListener(this)
        binding.reloadDateInfo.setOnClickListener(this)
        binding.eyeIcon.setOnClickListener(this)

    }

    companion object {
        lateinit var instance: AutoReloads
        var secondaryRefID = ""

        fun newInstance(isMain: String): AutoReloads {
            instance = AutoReloads()
            secondaryRefID = isMain
            return instance
        }
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



    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeftSide -> openSideMenu(
                R.id.mainContainer,
                SideMenuFragment(),
                "SideMenuFragment"
            )
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                binding.rlRight.visibility = View.GONE
                binding.btnConfirm.visibility = View.VISIBLE
                binding.btnCancel.visibility = View.VISIBLE
                enableAll()


            }
            R.id.etServiceProvider -> {
                //binding.etServiceProvider.setText("")

            }
            R.id.amountDue -> {

                binding.amountDue.setText("")

            }
            R.id.eyeIcon -> {

                val sharedPrefNotSaved = activity?.getPreferences(Context.MODE_PRIVATE)
                val sharedPref = activity?.getSharedPreferences("mydb",Context.MODE_PRIVATE)
                /*val temp1 = sharedPref!!.getString(secondaryRefID+1, "")
                    binding.etPasss.setText(temp1)*/
                if (binding.eyeIcon.isChecked) {
                    binding.etPasss.setText("***")
                } else {
                    val temp1 = sharedPref!!.getString(secondaryRefID+1, "")
                    binding.etPasss.setText(temp1)
                }

            }
            R.id.dueDate -> {


                showDateDialog()
                //ActivityBase.activity.showToastMessage("success Case")


            }
            R.id.reloadDate -> {

                showDateDialog2()


            }
            R.id.nextDueDate -> {


            }
            R.id.nextReloadDate -> {


            }
            R.id.dueDateInfo -> {

                showreloadInfoDialog("", "Select your vendor payment due date", "")

            }
            R.id.reloadDateInfo -> {


                showreloadInfoDialog("", "Select your cash card reload date", "")
            }
            R.id.recurringCriteria -> {
                showSecretDialog()


            }
            R.id.etUserName -> {

                binding.amountDue.setText("")

            }
            R.id.etPass -> {


            }
            R.id.imageView -> {
                showDateDialog()


            }
            R.id.imageView2 -> {
                showDateDialog2()


            }
            R.id.textView4 -> {

                showSecretDialog()

            }
            R.id.btnCancel -> {

                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()

            }
            R.id.switch1 -> {

                if (getDatacardReferenceId == "") {
                    if (switch1.isChecked)
                        enableAll()
                } else if (getDatacardReferenceId != "") {
                    if (switch1.isChecked) {

                        isEnabled = true

                        confirmToggledialog(
                            "abc",
                            "Are you you want to enable this schedule reload?",
                            this
                        )
                    } else {
                        isEnabled = false

                        confirmToggledialog(
                            "abc",
                            "Are you you want to disable this schedule reload?",
                            this
                        )
                    }
                }

                /* if(switch1.isChecked)
                {
                    if(getDataserviceProvider=="")
                    {
                        enableAll()
                    }
                    else
                        disableAll()
                }*/

            }
            R.id.btnConfirm -> {

                if (validateInput()) {

                    if (switch1.isChecked) {
                        isEnabled = true
                    } else {
                        isEnabled = false
                    }


                    /* cardsViewModel.addUserScheduleReload(AddUserScheduleReloadsRequestModel("",referenceId,isEnabled,binding.etServiceProvider.text.toString(),binding.amountDue.text.toString().toDouble(),
                paymentDate!!,reloadDate!!,"","",questionPosition))*/

//                cardsViewModel.addUserScheduleReload(AddUserScheduleReloadsRequestModel("",referenceId,isEnabled,binding.etServiceProvider.text.toString(),binding.amountDue.text.toString().toDouble(),
//                    paymentDate!!,reloadDate!!,"","",questionPosition))
                    if (getDatacardReferenceId == "") {
                        cardsViewModel.addUserScheduleReload(
                            AddUserScheduleReloadsRequestModel(
                                "",
                                secondaryRefID,
                                isEnabled,
                                "",
                                binding.amountDue.text.toString().toDouble(),
                                paymentDate!!,
                                reloadDate!!,
                                "",
                                "",
                                questionPosition + 1
                            )
                        )

                        /*if(binding.etUserName.text.toString()!="") {
                            MovoApp.db.putString(secondaryRefID, binding.etUserName.text.toString())
                            MovoApp.db.putString(secondaryRefID + 1, binding.etPasss.text.toString())


                            val sharedPrefNotSaved = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                            val sharedPref = activity?.getSharedPreferences("mydb",Context.MODE_PRIVATE) ?: return
                            with (sharedPref.edit()) {
                                putString(secondaryRefID, binding.etUserName.text.toString())
                                // add more
                                apply()
                            }
                            with (sharedPref.edit()) {
                                putString(secondaryRefID + 1, binding.etPasss.text.toString())
                                // add more
                                apply()
                            }
                        }*/
                    } else {
                        if(binding.amountDue.text.toString().contains("$"))
                        {
                            cardsViewModel.addUserScheduleReload(
                                AddUserScheduleReloadsRequestModel(
                                    "",
                                    secondaryRefID,
                                    isEnabled,
                                    "",
                                    getDatapaymentAmount,
                                    binding.dueDate.text.toString()!!,
                                    binding.reloadDate.text.toString()!!,
                                    "",
                                    "",
                                    questionPosition + 1
                                )
                            )
                        }
                        else
                        {
                            cardsViewModel.addUserScheduleReload(
                                AddUserScheduleReloadsRequestModel(
                                    "",
                                    secondaryRefID,
                                    isEnabled,
                                    "",
                                    binding.amountDue.text.toString().toDouble() ,
                                    binding.dueDate.text.toString()!!,
                                    binding.reloadDate.text.toString()!!,
                                    "",
                                    "",
                                    questionPosition + 1
                                )
                            )
                        }

                        /*if(binding.etUserName.text.toString()!="") {

                           val sharedPrefNotSaved = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                            val sharedPref = activity?.getSharedPreferences("mydb",Context.MODE_PRIVATE) ?: return


                            // https://developer.android.com/training/data-storage/shared-preferences
                            // read goes here
                            val temp = sharedPref.getString(secondaryRefID, "")
                            //end

                            // put goes here
                            with (sharedPref.edit()) {
                                putString("Tempabcde", binding.etUserName.text.toString())
                                // add more
                                apply()
                            }
                            // end
                            MovoApp.db.putString(secondaryRefID, binding.etUserName.text.toString())
                            MovoApp.db.putString(secondaryRefID + 1, binding.etPasss.text.toString())


                            with (sharedPref.edit()) {
                                putString(secondaryRefID, binding.etUserName.text.toString())
                                // add more
                                apply()
                            }
                            if(binding.etPasss.text.toString()!="***") {
                                with(sharedPref.edit()) {
                                    putString(secondaryRefID + 1, binding.etPasss.text.toString())
                                    // add more
                                    apply()
                                }
                            }
                        }*/
                    }


                }


            }

        }
    }
    private fun disableAll()
    {
        //binding.eyeIcon.isEnabled=false
        disableEditText(binding.etServiceProvider)
        disableEditText(binding.amountDue)

        disableTextView(binding.dueDate)
        disableImageView(binding.imageView)

        disableTextView(binding.reloadDate)
        disableImageView(binding.imageView2)

        disableEditText(binding.nextDueDate)
        disableEditText(binding.nextReloadDate)

        disableTextView(binding.textView4)//frequency
        disableImageView(binding.recurringCriteria)

        //disableEditText(binding.amountDue)

        disableEditText(binding.etUserName)
        disableEditText(binding.etPasss)
    }

    private fun enableAll()
    {
      //  binding.eyeIcon.isEnabled=true
        enableEditText(binding.etServiceProvider)
        enableEditText(binding.amountDue)

        enableTextView(binding.dueDate)
        enableImageView(binding.imageView)

        enableTextView(binding.reloadDate)
        enableImageView(binding.imageView2)

       /* enableEditText(binding.nextDueDate)
        enableEditText(binding.nextReloadDate)*/

        enableTextView(binding.textView4)//frequency
        enableImageView(binding.recurringCriteria)

        //disableEditText(binding.amountDue)

        enableEditText(binding.etUserName)
        enableEditText(binding.etPasss)
    }


    private fun setUiObserver() {

        commonViewModel.getFrequesnciesResponse.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                // Toast.makeText(activity, "Frequencies GOT", Toast.LENGTH_LONG).show()
                if (obj.list != null && obj.list!!.size > 0) {
                    if (mFrequencyList.size > 0)
                        mFrequencyList.clear()
                    mFrequencyList.addAll(obj.list!!)
                    // Toast.makeText(activity, mFrequencyList.size.toString(), Toast.LENGTH_LONG).show()
                }
            }
        })

        commonViewModel.getFrequencyFailure.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                Toast.makeText(activity, res, Toast.LENGTH_LONG).show()
                /*callFragmentWithReplace(
                    R.id.authContainer,
                    EnrollmentFragment(),
                    "EnrollmentFragment"
                )*/
            }
        })

        cardsViewModel.addUserScheduleReloadSuccess.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
               // Toast.makeText(activity, "USER ADDED", Toast.LENGTH_LONG).show()
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()


            }
        })

        cardsViewModel.addUserScheduleReloadFailure.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                Toast.makeText(activity, res, Toast.LENGTH_LONG).show()
               // Toast.makeText(activity, "User NOt Added", Toast.LENGTH_LONG).show()

                /*callFragmentWithReplace(
                    R.id.authContainer,
                    EnrollmentFragment(),
                    "EnrollmentFragment"
                )*/
            }
        })

        cardsViewModel.getUserScheduleReloadResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
              /*  Toast.makeText(activity, "got data", Toast.LENGTH_LONG).show()
                Toast.makeText(
                    activity,
                    "service provider :" + obj.userData!!.serviceProvider,
                    Toast.LENGTH_LONG
                ).show()
                Toast.makeText(
                    activity,
                    " Amount :" + obj.userData!!.paymentAmount,
                    Toast.LENGTH_LONG
                ).show()
                Toast.makeText(
                    activity,
                    "switch from server :" + obj.userData!!.isEnabled,
                    Toast.LENGTH_LONG
                ).show()*/

                if (obj != null) {
                    getDataisEnabled = obj.userData!!.isEnabled!!

                    getDataObj = obj
                }
                getDataserviceProvider = obj.userData!!.serviceProvider!!
                getDatapaymentAmount = obj.userData!!.paymentAmount!!
                getDatapaymentDueDate = obj.userData!!.paymentDueDate!!
                getDataautoReloadDate = obj.userData!!.autoReloadDate!!
                getDatafrequencyId = obj.userData!!.frequencyId!!
                getDatacardReferenceId = obj.userData!!.cardReferenceId!!
               /* Toast.makeText(
                    activity,
                    "getDataobj.isenabled :" + getDataObj.userData!!.isEnabled!!,
                    Toast.LENGTH_LONG
                ).show()*/
                if(getDataObj.userData!!.frequencyId==1)
                {
                    binding.constraintLayout19.visibility=View.GONE
                    binding.constraintLayout20.visibility=View.GONE

                }
                if (getDataObj.userData!!.isEnabled!!) {
                    binding.switch1.isChecked = true
                } else {
                    binding.switch1.isChecked = false
                }

               /* if (obj.userData!!.cardReferenceId == "") {

                } else {
                   // if (MovoApp.db.getString(secondaryRefID) != "") {
                       // binding.etUserName.setText(MovoApp.db.getString(secondaryRefID))
                        binding.etPasss.setText("***")


                        val sharedPrefNotSaved = activity?.getPreferences(Context.MODE_PRIVATE)
                        val sharedPref = activity?.getSharedPreferences("mydb",Context.MODE_PRIVATE)


                        // https://developer.android.com/training/data-storage/shared-preferences
                        // read goes here
                        val temp = sharedPref!!.getString(secondaryRefID, "")
                        binding.etUserName.setText(temp)
                    *//*val temp1 = sharedPref!!.getString(secondaryRefID+1, "")
                    binding.etPasss.setText(temp1)*//*
                    //}
                }*/

                if (obj.userData!!.cardReferenceId != "") {
                    /*getDataid = obj.userData!!.id!!
                    getDatacardReferenceId = obj.userData!!.cardReferenceId!!
                    getDataisEnabled = obj.userData!!.isEnabled!!
                    getDataserviceProvider = obj.userData!!.serviceProvider!!
                    getDatapaymentAmount = obj.userData!!.paymentAmount!!
                    getDatapaymentDueDate = obj.userData!!.paymentDueDate!!
                    getDataautoReloadDate = obj.userData!!.autoReloadDate!!
                    getDatanextPaymentDueDate = obj.userData!!.nextPaymentDueDate!!
                    getDatanextAutoReloadDate = obj.userData!!.nextAutoReloadDate!!
                    getDatafrequencyId = obj.userData!!.frequencyId!!*/
                }

                //  binding.etServiceProvider.setText(obj.userData!!.serviceProvider)

                if (obj.userData!!.cardReferenceId == "") {

                    // getData.cardReferenceId

                    binding.rlRight.visibility = View.GONE

                    if (obj.userData!!.isEnabled!!) {

                    } else {
                        disableAll()
                    }

                    // enableAll()
                    /*enableEditText(binding.etServiceProvider)
                    enableEditText(binding.amountDue)

                    enableTextView(binding.dueDate)
                    enableImageView(binding.imageView)

                    enableTextView(binding.reloadDate)
                    enableImageView(binding.imageView2)

                    enableEditText(binding.nextDueDate)
                    enableEditText(binding.nextReloadDate)

                    enableTextView(binding.textView4)//frequency
                    enableImageView(binding.recurringCriteria)

                    //disableEditText(binding.amountDue)

                    enableEditText(binding.etUserName)
                    enableEditText(binding.etPass)*/


                } else {


                    binding.rlRight.visibility = View.VISIBLE
                    binding.model = obj.userData
                    disableAll()
                    binding.btnConfirm.visibility = View.GONE
                    binding.btnCancel.visibility = View.GONE

                    /* disableEditText(binding.etServiceProvider)
                    disableEditText(binding.amountDue)

                    disableTextView(binding.dueDate)
                    disableImageView(binding.imageView)

                    disableTextView(binding.reloadDate)
                    disableImageView(binding.imageView2)

                    disableEditText(binding.nextDueDate)
                    disableEditText(binding.nextReloadDate)

                    disableTextView(binding.textView4)//frequency
                    disableImageView(binding.recurringCriteria)

                    //disableEditText(binding.amountDue)

                    disableEditText(binding.etUserName)
                    disableEditText(binding.etPass)*/

                    /*binding.rlRight.visibility = View.VISIBLE
                    if(getDataisEnabled )
                    {
                        binding.switch1.isEnabled=true
                    }
                    else{
                        binding.switch1.isEnabled=false

                    }
                    binding.etServiceProvider.setText(getDataserviceProvider)
                    binding.paymentAmount.setText(getDatapaymentAmount.toString())
                    binding.dueDate.setText(getDatapaymentDueDate )
                    binding.reloadDate.setText(getDataautoReloadDate )
                    binding.nextDueDate.setText(getDatanextPaymentDueDate)
                    binding.nextReloadDate.setText(getDatanextAutoReloadDate )
                    binding.textView4.setText(getDatafrequencyId.toString())*/

                }


            }
        })

        cardsViewModel.getUserScheduleReloadFailure.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                Toast.makeText(activity, res, Toast.LENGTH_LONG).show()
               // Toast.makeText(activity, "data nai mila", Toast.LENGTH_LONG).show()

                /*callFragmentWithReplace(
                    R.id.authContainer,
                    EnrollmentFragment(),
                    "EnrollmentFragment"
                )*/
            }
        })



    }

    private fun validateDate(): Boolean {

            val dateafter = binding.dueDate.text.toString()
            val date = binding.reloadDate.text.toString()
            val dateFormat = SimpleDateFormat(
                "MM-dd-yyyy"
            )
            var convertedDate = Date()
            var convertedDate2 = Date()
            //try {
            convertedDate = dateFormat.parse(date)
            convertedDate2 = dateFormat.parse(dateafter)
            if (convertedDate2.after(convertedDate)) {
               // ActivityBase.activity.showToastMessage("true")

                return true

            } else {


              //  ActivityBase.activity.showToastMessage("false")

                return false
            }
            /*} catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return false

        }*/
            //return false

       // return false
    }


    private fun validateInput(): Boolean {




        validateDate()
        if (TextUtils.isEmpty(binding.amountDue.text.toString())) {

            binding.paymentAmount.requestFocus()
            binding.paymentAmount.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.dueDate.text.toString())) {
            binding.dueDate.requestFocus()
            binding.dueDate.errorAnim(ActivityBase.activity)
            return false
        }else if (TextUtils.isEmpty(binding.reloadDate.text.toString())) {
            binding.reloadDate.requestFocus()
            binding.reloadDate.errorAnim(ActivityBase.activity)
            return false
        }
        else if (!validateDate()) {
            binding.dueDate.requestFocus()
            binding.dueDate.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Due Date should not be greater than Reload Date")
            return false
        }
        else {
            return true
        }
    }

    private fun showSecretDialog() {
        if (questionList.size > 0)
            questionList.clear()

        mFrequencyList.mapTo(questionList) { it.name }
        setBottomSheet(questionList, questionPosition, 1)
    }
    private fun setBottomSheet(list: ArrayList<String>, selectedPosition: Int, type: Int) {
        val bottomSheet = BottomSheetSelectorFragment(list, selectedPosition, type)
        bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
        bottomSheet.setMyListener(this)
    }




        override fun onDateCancel() {

    }


    override fun onSelect(pos: Int, type: Int) {
        when (type) {
            1 -> {
                questionPosition = pos - 1
                binding.textView4.text = questionList[pos]


            }
        }
    }

    override fun onCancel(type: Int) {

    }

    private fun showDateDialog() {
        val bottomSheet: BottomSheetDatePickerFragment = if (paymentDate != null) {
            BottomSheetDatePickerFragment(paymentDate!!, Constants.CONST_SCHEDULE)
        } else {
            BottomSheetDatePickerFragment("", Constants.CONST_SCHEDULE)
        }
        if (!bottomSheet.isAdded)
            bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
        bottomSheet.setMyListener(this)
    }
    private fun showDateDialog2() {
        val bottomSheet: BottomSheetDatePicker2 = if (reloadDate != null) {
            BottomSheetDatePicker2(reloadDate!!, Constants.CONST_SCHEDULE)
        } else {
            BottomSheetDatePicker2("", Constants.CONST_SCHEDULE)
        }
        if (!bottomSheet.isAdded)
            bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
        bottomSheet.setMyListener(this)
    }

    override fun onSelectDob(dob: String) {
        paymentDate = dob
        binding.dueDate.text = DateUtil.setAppFormatDate(paymentDate!!)
    }

    override fun onSelectDob2(dob: String) {
        reloadDate = dob
        binding.reloadDate.text = DateUtil.setAppFormatDate(reloadDate!!)
    }

    override fun onSelectDob(dob: String, view: View) {
        TODO("Not yet implemented")
    }

    override fun onDateCancel2() {
        ActivityBase.activity.showToastMessage("date not selected")

    }

    override fun onClickOk() {

        if (validateInput()) {

            if (binding.switch1.isChecked) {
                isEnabled = true
            } else {
                isEnabled = false
            }
          //  ActivityBase.activity.showToastMessage("switch : " + getDataisEnabled)

            /* cardsViewModel.addUserScheduleReload(AddUserScheduleReloadsRequestModel("",referenceId,isEnabled,binding.etServiceProvider.text.toString(),binding.amountDue.text.toString().toDouble(),
            paymentDate!!,reloadDate!!,"","",questionPosition))*/

//                cardsViewModel.addUserScheduleReload(AddUserScheduleReloadsRequestModel("",referenceId,isEnabled,binding.etServiceProvider.text.toString(),binding.amountDue.text.toString().toDouble(),
//                    paymentDate!!,reloadDate!!,"","",questionPosition))

            cardsViewModel.addUserScheduleReload(
                AddUserScheduleReloadsRequestModel(
                    "",
                    secondaryRefID,
                    isEnabled,
                    "",
                    getDatapaymentAmount,
                    getDatapaymentDueDate!!,
                    getDataautoReloadDate!!,
                    "",
                    "",
                    getDatafrequencyId
                )
            )

/*
            if(binding.etUserName.text.toString()!="") {
                MovoApp.db.putString(secondaryRefID, binding.etUserName.text.toString())
                MovoApp.db.putString(secondaryRefID + 1, binding.etPasss.text.toString())

                val sharedPrefNotSaved = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                val sharedPref = activity?.getSharedPreferences("mydb",Context.MODE_PRIVATE) ?: return
                with (sharedPref.edit()) {
                    putString(secondaryRefID, binding.etUserName.text.toString())
                    // add more
                    apply()
                }
                if(binding.etPasss.text.toString()!="***")
                {
                    with (sharedPref.edit()) {
                        putString(secondaryRefID + 1, binding.etPasss.text.toString())
                        // add more
                        apply()
                    }
                }

            }*/

        }
        else{
            ActivityBase.activity.showToastMessage("data not validated")

        }
    }

    override fun onClickCancel() {

       // ActivityBase.activity.showToastMessage("cancel activated")
        if(switch1.isChecked)
        {
            switch1.isChecked= false
        }
        else
            switch1.isChecked = true
    }

/*    private fun checkAndSet() {
       // binding.isCardSet = false
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
            //setCardData(1)
            primaryCardObj = mList1[0]
            setPrimaryCardData(0)
        }
    }

    private fun setPrimaryCardData(position: Int) {
        val cardNumber = "*" + mList1[position].cardNumber!!.substring(mList1[position].cardNumber!!.length - 4)
        val num = "${mList1[position].programAbbreviation}${cardNumber}"
        binding.cardAmount = mList1[position].balance
        referenceId = mList1[position].referenceID!!
       // primaryAmount = mList1[position].balance
    }*/
}