package com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts

import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CreateBankAccountRequestModel
import com.movocash.movo.data.model.requestmodel.SendVerificationCodeRequestModel
import com.movocash.movo.data.model.requestmodel.UpdateProfileRequestModel
import com.movocash.movo.data.model.requestmodel.VerifyBankCodeRequestModel
import com.movocash.movo.databinding.FragmentVerifyProfileCodeBinding
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.profile.VerifyProfileCodeFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.BankViewModel
import kotlinx.android.synthetic.main.fragment_bank_otp.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class bankOtpFragment : BaseFragment(),View.OnClickListener, IInfoListener {
    // TODO: Rename and change types of parameters

    private lateinit var accountViewModel: AccountViewModel
    private var countDownTimer: CountDownTimer? = null
    lateinit var emailOtp : EditText
    lateinit var smsOtp : EditText
    lateinit var timer : TextView
    lateinit var resend : TextView
    lateinit var btnDone : Button
    lateinit var btnBack : RelativeLayout
    private lateinit var bankViewModel: BankViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_bank_otp, container, false)
        bankViewModel = ViewModelProvider(this).get(BankViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
         emailOtp = root.findViewById<EditText>(R.id.EmailOtp)
        smsOtp = root.findViewById<EditText>(R.id.smsOtp)
        timer = root.findViewById<TextView>(R.id.tvTimer)
        resend = root.findViewById<TextView>(R.id.tvResend)
        btnDone = root.findViewById<Button>(R.id.btnDone)
        btnBack = root.findViewById<RelativeLayout>(R.id.rlBack)
        emailOtp.setOnClickListener(this)
        smsOtp.setOnClickListener(this)
        btnDone.setOnClickListener(this)
        resend.setOnClickListener(this)
        btnBack.setOnClickListener(this)

        setUiObserver()
        startTimer()



        return root
    }

    companion object {
        lateinit var instance: bankOtpFragment
        lateinit var requestObj: CreateBankAccountRequestModel

        fun newInstance(obj: CreateBankAccountRequestModel): bankOtpFragment {
            instance = bankOtpFragment()
            requestObj = obj
            return instance
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvResend -> {
                if (countDownTimer != null)
                    countDownTimer!!.cancel()
                accountViewModel.sendBankOtp()
                /*startTimer()
                resend.visibility = View.GONE*/
               // Toast.makeText(activity, "Resend Working",Toast.LENGTH_LONG).show()
            }
            R.id.rlBack -> {
                if (countDownTimer != null)
                    countDownTimer!!.cancel()
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            }
            R.id.btnDone -> {
                if (!TextUtils.isEmpty(emailOtp.text.toString()) && !TextUtils.isEmpty(smsOtp.text.toString())) {
                   // Toast.makeText(activity, emailOtp.text.toString()+smsOtp.text.toString(), Toast.LENGTH_LONG).show()
                   accountViewModel.verifyBankVerificationCode(VerifyBankCodeRequestModel(smsOtp.text.toString(),emailOtp.text.toString()))
                } else {
                    //Toast.makeText(activity, emailOtp.text.toString()+TextUtils.isEmpty(smsOtp.text.toString()), Toast.LENGTH_LONG).show()
                    emailOtp.requestFocus()
                    emailOtp.errorAnim(ActivityBase.activity)
                }

            }
        }
    }

    private fun setUiObserver() {


        accountViewModel.verifyBankCodeSuccess.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                //Toast.makeText(activity, res, Toast.LENGTH_LONG).show()
                bankViewModel.createBankAccount(requestObj)


                //showInfoDialog("", res, this)
            }
        })
        accountViewModel.verifyBankCodeFailure.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                Toast.makeText(activity, res, Toast.LENGTH_LONG).show()
                /*bankViewModel.createBankAccount(requestObj)


                showInfoDialog("", res, this)*/
            }
        })




        accountViewModel.bankCodeSent.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                startTimer()
                resend.visibility = View.GONE
                /*bankViewModel.createBankAccount(requestObj)


                showInfoDialog("", res, this)*/
            }
        })

        bankViewModel.createBankAccountResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc))
                showInfoDialog("", obj.data!!.responseDesc!!, this)
        })

    }

private fun startTimer() {
    countDownTimer = object : CountDownTimer((60 * 20 * 1000).toLong(), 100) {
        override fun onTick(l: Long) {
            val seconds = l / 1000
            val currentMinute = seconds / 60
            timer.text = String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60)
            if (currentMinute == 0L && seconds == 0L) {
                resend.visibility = View.VISIBLE
                timer.text = "00:00"
                if (countDownTimer != null) {
                    countDownTimer!!.cancel()
                }
            } else {

            }
        }

        override fun onFinish() {
            resend.visibility = View.VISIBLE
            timer.text = "00:00"
            if (countDownTimer != null) {
                countDownTimer!!.cancel()
            }
        }
    }.start()
}

    override fun onClickOk() {
       // gotoHome()
        //callFragmentWithReplace(R.id.mainContainer, MyBankAccountFragment(), null)
       // Toast.makeText(activity, "Onclick working", Toast.LENGTH_LONG).show()
        gotoHome()
        callFragmentWithReplace(R.id.mainContainer, MyBankAccountFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }

/*    override fun onClick(v: View?) {
        when (v!!.id) {
            *//*et1 -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.btnDelete -> bankViewModel.removeBankAccount(AddBankAccountConfirmationFragment.requestObj.bankSerialNumberIfEdit)
            R.id.rlRight -> {
                if (!isEdit) {
                    // bankViewModel.createBankAccount(requestObj)
                    // addFragment(R.id.mainContainer, bankOtpFragment(), null)
                    accountViewModel.sendBankOtp()

                    callFragmentWithReplace(R.id.mainContainer, bankOtpFragment(), "asdsa")
                }
                else {
                    addFragment(R.id.mainContainer, AddBankAccountFragment.newInstance(
                        AddBankAccountConfirmationFragment.requestObj.accountType, true,
                        AddBankAccountConfirmationFragment.requestObj
                    ), "AddBankAccountFragment")
                }
            }*//*
        }
    }*/
}