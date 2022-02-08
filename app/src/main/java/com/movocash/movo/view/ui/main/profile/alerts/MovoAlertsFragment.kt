package com.movocash.movo.view.ui.main.profile.alerts

import android.content.Context
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
import com.movocash.movo.data.model.requestmodel.AddUpdateUserAlerts
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.data.model.responsemodel.GetUserAlertsResponseModel
import com.movocash.movo.databinding.*
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.AccountViewModel
import java.lang.reflect.Type

class MovoAlertsFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentCashAlertBinding
    private lateinit var accountViewModel: AccountViewModel
    private var mList = ArrayList<GetUserAlertsResponseModel.MainObj>()
    private var cardList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var cardList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var referenceId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cash_alert, container, false)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUiObserver()
        setViews()
        setListeners()
        return binding.root
    }

    private fun checkAndSet() {
        var cardModel: GetCardAccountsResponseModel? = null
        val listType: Type = object : TypeToken<GetCardAccountsResponseModel>() {}.type
        cardModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_CARD_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }

        if (cardModel?.obj != null && cardModel.obj!!.cards != null && cardModel.obj!!.cards!!.size > 0) {
            cardList.clear()
            cardList = cardModel.obj!!.cards!!
            cardList1 = ArrayList(cardList.filter { it.isPrimaryCardSpecified && it.statusCode!="F"})
            setPrimaryCardData(0)
        }
    }

    private fun setCardData(position: Int) {
        val cardNumber = "*" + cardList[position].cardNumber!!.substring(cardList[position].cardNumber!!.length - 4)
        val num = "${cardList[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = cardList[position].balance
        referenceId = cardList[position].referenceID!!
    }

    private fun setPrimaryCardData(position: Int) {
        val cardNumber = "*" + cardList1[position].cardNumber!!.substring(cardList1[position].cardNumber!!.length - 4)
        val num = "${cardList1[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = cardList1[position].balance
        referenceId = cardList1[position].referenceID!!
    }


    private fun setViews() {
        binding.tvTitle.isSelected = true
        checkAndSet()
        accountViewModel.getUserAlerts()
    }

    private fun setUiObserver() {
        accountViewModel.getUserAlertsFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        accountViewModel.getUserAlertsResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.mainList != null && obj.mainList!!.size > 0) {
                    mList.clear()
                    mList.addAll(obj.mainList!!)
                    setAlertChildren()
                }

            }
        })
    }

    private fun setAlertChildren() {
        if (binding.llAlert.childCount > 0)
            binding.llAlert.removeAllViews()

        for (i in 0 until mList.size) {
            val headerBinding: AlertHeaderItemBinding = DataBindingUtil.inflate(ActivityBase.activity.layoutInflater, R.layout.alert_header_item, null, false)
            headerBinding.tvHeader.text = mList[i].name

            if (mList[i].alertsList != null && mList[i].alertsList!!.size > 0) {
                headerBinding.rlBottom.visibility = View.GONE
            } else {
                headerBinding.rlBottom.visibility = View.VISIBLE
                headerBinding.tvMainDesc.text = mList[i].description
            }

            if (mList[i].isMultiple) {
                headerBinding.tvRightDebit.visibility = View.VISIBLE
            } else
                headerBinding.tvRightDebit.visibility = View.GONE

            headerBinding.rlBottom.setOnClickListener {
                callFragmentWithReplace(R.id.mainContainer, SetAlertsFragment.newInstance(AddUpdateUserAlerts(mList[i].alertId, mList[i].alertTypeId!!, "", 1, 0.0, "", "", false), binding.cardNum!!, false, mList[i].name!!, mList[i].isOperator, mList[i].description!!), "")
            }

            headerBinding.tvRightDebit.setOnClickListener {
                callFragmentWithReplace(R.id.mainContainer, SetAlertsFragment.newInstance(AddUpdateUserAlerts(mList[i].alertId, mList[i].alertTypeId!!, "", 1, 0.0, "", "", false), binding.cardNum!!, false, mList[i].name!!, mList[i].isOperator, mList[i].description!!), "")
            }
            binding.llAlert.addView(headerBinding.root)

            if (mList[i].alertsList != null && mList[i].alertsList!!.size > 0) {
                for (j in mList[i].alertsList!!.indices) {

                    val descriptionItemBinding: AlertDescriptionItemBinding = DataBindingUtil.inflate(ActivityBase.activity.layoutInflater, R.layout.alert_description_item, null, false)
                    val emailItemBinding: AlertEmailItemBinding = DataBindingUtil.inflate(ActivityBase.activity.layoutInflater, R.layout.alert_email_item, null, false)
                    val smsItemBinding: AlertSmsItemBinding = DataBindingUtil.inflate(ActivityBase.activity.layoutInflater, R.layout.alert_sms_item, null, false)
                    val pushItemBinding: AlertPushItemBinding = DataBindingUtil.inflate(ActivityBase.activity.layoutInflater, R.layout.alert_push_item, null, false)

                    if (mList[i].alertsList!![j].amount != 0.0) {
                        if (mList[i].alertsList!![j].operatorTypeId == Constants.CONST_OPERATOR_GREATER) {
                            var a: String
                            //a = String.format("%.2f", {mList[i].alertsList!![j].amount})
                            var b: Double
                            b = mList[i].alertsList!![j].amount
                            a = String.format("%.2f", b)
                            descriptionItemBinding.tvDescription.text = "When an amount greater than or equals to " + a  /*$${mList[i].alertsList!![j].amount}*/ +" is deducted from this account"
                        } else {
                            descriptionItemBinding.tvDescription.text = "When an amount less than or equals to "+ String.format("%.2f", {mList[i].alertsList!![j].amount}) +" is deducted from this account"
                        }

                        descriptionItemBinding.rlDescriptionMain.setOnClickListener {
                            callFragmentWithReplace(R.id.mainContainer, SetAlertsFragment.newInstance(AddUpdateUserAlerts(mList[i].alertsList!![j].alertId, mList[i].alertsList!![j].alertTypeId!!, mList[i].alertsList!![j].id!!, mList[i].alertsList!![j].operatorTypeId, mList[i].alertsList!![j].amount, mList[i].alertsList!![j].sms!!, mList[i].alertsList!![j].email!!, mList[i].alertsList!![j].mobilePush), binding.cardNum!!, true, mList[i].name!!, mList[i].isOperator, mList[i].description!!), "")
                        }

                        binding.llAlert.addView(descriptionItemBinding.root)
                    }


                    if (!TextUtils.isEmpty(mList[i].alertsList!![j].email)) {
                        emailItemBinding.tvEmail.text = mList[i].alertsList!![j].email
                        emailItemBinding.rlEmailMain.setOnClickListener {
                            callFragmentWithReplace(R.id.mainContainer, SetAlertsFragment.newInstance(AddUpdateUserAlerts(mList[i].alertsList!![j].alertId, mList[i].alertsList!![j].alertTypeId!!, mList[i].alertsList!![j].id!!, mList[i].alertsList!![j].operatorTypeId, mList[i].alertsList!![j].amount, mList[i].alertsList!![j].sms!!, mList[i].alertsList!![j].email!!, mList[i].alertsList!![j].mobilePush), binding.cardNum!!, true, mList[i].name!!, mList[i].isOperator, mList[i].description!!), "")
                        }
                        binding.llAlert.addView(emailItemBinding.root)
                    }

                    if (!TextUtils.isEmpty(mList[i].alertsList!![j].sms)) {
                        smsItemBinding.tvPhone.text = mList[i].alertsList!![j].sms
                        smsItemBinding.rlSMSMain.setOnClickListener {
                            callFragmentWithReplace(R.id.mainContainer, SetAlertsFragment.newInstance(AddUpdateUserAlerts(mList[i].alertsList!![j].alertId, mList[i].alertsList!![j].alertTypeId!!, mList[i].alertsList!![j].id!!, mList[i].alertsList!![j].operatorTypeId, mList[i].alertsList!![j].amount, mList[i].alertsList!![j].sms!!, mList[i].alertsList!![j].email!!, mList[i].alertsList!![j].mobilePush), binding.cardNum!!, true, mList[i].name!!, mList[i].isOperator, mList[i].description!!), "")
                        }
                        binding.llAlert.addView(smsItemBinding.root)
                    }

                    if (mList[i].alertsList!![j].mobilePush) {
                        pushItemBinding.rlPushMain.setOnClickListener {
                            callFragmentWithReplace(R.id.mainContainer, SetAlertsFragment.newInstance(AddUpdateUserAlerts(mList[i].alertsList!![j].alertId, mList[i].alertsList!![j].alertTypeId!!, mList[i].alertsList!![j].id!!, mList[i].alertsList!![j].operatorTypeId, mList[i].alertsList!![j].amount, mList[i].alertsList!![j].sms!!, mList[i].alertsList!![j].email!!, mList[i].alertsList!![j].mobilePush), binding.cardNum!!, true, mList[i].name!!, mList[i].isOperator, mList[i].description!!), "")
                        }
                        binding.llAlert.addView(pushItemBinding.root)
                    }


                }
            }

        }
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.llAmount.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.llAmount -> {
            }
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