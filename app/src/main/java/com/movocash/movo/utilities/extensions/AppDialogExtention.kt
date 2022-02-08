package com.movocash.movo.utilities.extensions

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.*
import com.movocash.movo.utilities.Constants
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.base.ActivityBase.Companion.activity
import com.movocash.movo.view.ui.main.others.adapter.AdapterDialogCards
import kotlin.coroutines.coroutineContext

var loadingDialoge: Dialog? = null

fun Context.showAppLoader() {
    if (loadingDialoge != null) {
        if (!loadingDialoge!!.isShowing) {
            loadingDialoge = Dialog(this, R.style.Theme_Dialog_Loader)
            loadingDialoge!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            loadingDialoge!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            loadingDialoge!!.setContentView(R.layout.dialoge_app_loader)
            loadingDialoge!!.setCancelable(false)
            loadingDialoge!!.show()
        }
    } else {
        loadingDialoge = Dialog(this, R.style.Theme_Dialog_Loader)
        loadingDialoge!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialoge!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialoge!!.setContentView(R.layout.dialoge_app_loader)
        loadingDialoge!!.setCancelable(false)
        loadingDialoge!!.show()
    }
}

fun hideAppLoader() {
    if (loadingDialoge != null && loadingDialoge!!.isShowing) {
        try {
            loadingDialoge!!.dismiss()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
}

fun showSelectBankDialog() {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(false)
    val binding: DialogSelectBankBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_select_bank, null, false)
    dialog.setContentView(binding.root)

    binding.rlOK.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun showSelectFrequencyDialog(listener: IFrequencyList) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogSelectFrequencyBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_select_frequency, null, false)
    dialog.setContentView(binding.root)

    binding.tvOneTime.setOnClickListener {
        dialog.dismiss()
        listener.onItemSelect(Constants.CONST_FREQUENCY_ONCE)
    }
    binding.tvSpecific.setOnClickListener {
        dialog.dismiss()
        listener.onItemSelect(Constants.CONST_FREQUENCY_DATE)
    }
    dialog.show()
}

interface IFrequencyList {
    fun onItemSelect(type: Int)
}

fun showAccountSelectionDialog(cardList: ArrayList<GetCardAccountsResponseModel.Card>, isSecondaryShow: Boolean, listener: ICardClickListener) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogSelectCardBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_select_card, null, false)
    dialog.setContentView(binding.root)



    var primaryCardList = ArrayList<GetCardAccountsResponseModel.Card>()
    var secondaryCardList = ArrayList<GetCardAccountsResponseModel.Card>()
    var sortList = ArrayList<GetCardAccountsResponseModel.Card>()

    if (cardList.any { it.isPrimaryCardSpecified }) {
        primaryCardList = ArrayList(cardList.filter { it.isPrimaryCardSpecified})
    }

    val manager = LinearLayoutManager(ActivityBase.activity)
    binding.rvPrimary.layoutManager = manager
    val adapter = AdapterDialogCards(primaryCardList)
    binding.rvPrimary.adapter = adapter
    adapter.setMyListener(object : AdapterDialogCards.IDialogCardsListener {
        override fun onClickCard(position: Int) {
            dialog.dismiss()
            listener.onClickCard(cardList.indexOf(primaryCardList[position]))
        }

    })

    if (cardList.any { !it.isPrimaryCardSpecified }) {
        secondaryCardList = ArrayList(cardList.filter { !it.isPrimaryCardSpecified  })
        sortList = ArrayList(secondaryCardList.sortedBy { it.programAbbreviation?.toString() })
        secondaryCardList = sortList

    }

    if (isSecondaryShow) {
        if (secondaryCardList.isNotEmpty()) {
            binding.rlHeadSecondary.visibility = View.VISIBLE
            val manager2 = LinearLayoutManager(ActivityBase.activity)
            binding.rvSecondary.layoutManager = manager2
            val adapter2 = AdapterDialogCards(secondaryCardList)
            binding.rvSecondary.adapter = adapter2
            adapter2.setMyListener(object : AdapterDialogCards.IDialogCardsListener {
                override fun onClickCard(position: Int) {
                    dialog.dismiss()
                    listener.onClickCard(cardList.indexOf(secondaryCardList[position]))
                }

            })
        } else {
            binding.rlHeadSecondary.visibility = View.GONE
        }
    } else {
        binding.rlHeadSecondary.visibility = View.GONE
    }

    binding.rlHeadPrimary.setOnClickListener {
        binding.rvSecondary.visibility = View.GONE
        if (binding.rvPrimary.visibility == View.VISIBLE) {
            binding.ivPrimaryCard.rotation = 0f
            binding.rvPrimary.visibility = View.GONE
        } else {
            binding.ivPrimaryCard.rotation = 90f
            binding.rvPrimary.visibility = View.VISIBLE
        }
    }

    binding.rlHeadSecondary.setOnClickListener {
        binding.rvPrimary.visibility = View.GONE
        if (binding.rvSecondary.visibility == View.VISIBLE) {
            binding.ivSecondaryCard.rotation = 0f
            binding.rvSecondary.visibility = View.GONE
        } else {
            binding.ivSecondaryCard.rotation = 90f
            binding.rvSecondary.visibility = View.VISIBLE
        }
    }
    dialog.show()
}

fun showAccountSelectionDialogClosedCards(cardList: ArrayList<GetCardAccountsResponseModel.Card>, isSecondaryShow: Boolean, listener: ICardClickListener) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogSelectCardBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_select_card, null, false)
    dialog.setContentView(binding.root)


//mList.addAll(ArrayList(cardList.filter { !it.isPrimaryCardSpecified && it.statusCode!="F"}))

    var primaryCardList = ArrayList<GetCardAccountsResponseModel.Card>()
    var secondaryCardList = ArrayList<GetCardAccountsResponseModel.Card>()

    if (cardList.any { it.isPrimaryCardSpecified }) {
        primaryCardList = ArrayList(cardList.filter { it.isPrimaryCardSpecified  && it.statusCode!="F"})
    }

    val manager = LinearLayoutManager(ActivityBase.activity)
    binding.rvPrimary.layoutManager = manager
    val adapter = AdapterDialogCards(primaryCardList)
    binding.rvPrimary.adapter = adapter
    adapter.setMyListener(object : AdapterDialogCards.IDialogCardsListener {
        override fun onClickCard(position: Int) {
            dialog.dismiss()
            listener.onClickCard(cardList.indexOf(primaryCardList[position]))
        }

    })

    if (cardList.any { !it.isPrimaryCardSpecified }) {
        secondaryCardList = ArrayList(cardList.filter { !it.isPrimaryCardSpecified })
    }

    if (isSecondaryShow) {
        if (secondaryCardList.isNotEmpty()) {
            binding.rlHeadSecondary.visibility = View.VISIBLE
            val manager2 = LinearLayoutManager(ActivityBase.activity)
            binding.rvSecondary.layoutManager = manager2
            val adapter2 = AdapterDialogCards(secondaryCardList)
            binding.rvSecondary.adapter = adapter2
            adapter2.setMyListener(object : AdapterDialogCards.IDialogCardsListener {
                override fun onClickCard(position: Int) {
                    dialog.dismiss()
                    listener.onClickCard(cardList.indexOf(secondaryCardList[position]))
                }

            })
        } else {
            binding.rlHeadSecondary.visibility = View.GONE
        }
    } else {
        binding.rlHeadSecondary.visibility = View.GONE
    }

    binding.rlHeadPrimary.setOnClickListener {
        binding.rvSecondary.visibility = View.GONE
        if (binding.rvPrimary.visibility == View.VISIBLE) {
            binding.ivPrimaryCard.rotation = 0f
            binding.rvPrimary.visibility = View.GONE
        } else {
            binding.ivPrimaryCard.rotation = 90f
            binding.rvPrimary.visibility = View.VISIBLE
        }
    }

    binding.rlHeadSecondary.setOnClickListener {
        binding.rvPrimary.visibility = View.GONE
        if (binding.rvSecondary.visibility == View.VISIBLE) {
            binding.ivSecondaryCard.rotation = 0f
            binding.rvSecondary.visibility = View.GONE
        } else {
            binding.ivSecondaryCard.rotation = 90f
            binding.rvSecondary.visibility = View.VISIBLE
        }
    }
    dialog.show()
}

interface ICardClickListener {
    fun onClickCard(position: Int)
}

fun showSelectPayeeDialog() {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(false)
    val binding: DialogAddPayeeBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_add_payee, null, false)
    dialog.setContentView(binding.root)

    binding.rlAddPayees.setOnClickListener {
        dialog.dismiss()
    }

    binding.rlCancel.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}

fun showInfoDialog(title: String, msg: String, listener: IInfoListener) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogDataBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_data, null, false)
    dialog.setContentView(binding.root)

    if (!TextUtils.isEmpty(title))
        binding.tvTitle.text = ""

    binding.tvMessage.text = msg
    binding.btnOk.setOnClickListener {

        listener.onClickOk()
        dialog.dismiss()
    }

    dialog.setOnCancelListener {
        listener.onClickOk()
    }

    dialog.show()
}


fun showreloadInfoDialog(title: String, msg: String,msg2: String) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogInfoReloadBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_info_reload, null, false)
    dialog.setContentView(binding.root)

/*    if (!TextUtils.isEmpty(title))
        binding.tvTitle.text = title*/

    binding.tvmessage.text = msg
   // binding.tvMessage2.text = msg2
    binding.btnOk.setOnClickListener {

        // listener.onClickOk()
        dialog.dismiss()
    }

    dialog.setOnCancelListener {
        //listener.onClickOk
        dialog.dismiss()
    }

    dialog.show()
}

fun showMyInfoDialog(title: String, msg: String,msg2: String) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogDataBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_data, null, false)
    dialog.setContentView(binding.root)

    if (!TextUtils.isEmpty(title))
        binding.tvTitle.text = title

    binding.tvMessage.text = msg
    binding.tvMessage2.text = msg2
    binding.btnOk.setOnClickListener {

       // listener.onClickOk()
        dialog.dismiss()
    }

    dialog.setOnCancelListener {
        //listener.onClickOk
        dialog.dismiss()
    }

    dialog.show()
}
fun showStylishDialog(title: String, msg: String,msg2: String) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogSignupBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_signup, null, false)
    dialog.setContentView(binding.root)

    if (!TextUtils.isEmpty(title))
        binding.tvTitle.text = title

    binding.tvMessage.text = msg
   // binding.tvMessage2.text = msg2
    binding.btnOk.setOnClickListener {

        // listener.onClickOk()
        dialog.dismiss()
    }

    dialog.setOnCancelListener {
        //listener.onClickOk
        dialog.dismiss()
    }

    dialog.show()
}
fun showStylishDialog2(title: String, msg: String,msg2: String) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: StylishDialog2Binding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.stylish_dialog2, null, false)
    dialog.setContentView(binding.root)

//    if (!TextUtils.isEmpty(title))
//        binding.tvTitle.text = title

    binding.tvMessage.text = msg
    // binding.tvMessage2.text = ""
    binding.btnOk.setOnClickListener {

        // listener.onClickOk()
        dialog.dismiss()
    }

    dialog.setOnCancelListener {
        //listener.onClickOk
        dialog.dismiss()
    }

    dialog.show()
}

fun showEmailOtp(title: String, msg: String, listener: IInfoListener) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: EmailOtpBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.email_otp, null, false)
    dialog.setContentView(binding.root)

    if (!TextUtils.isEmpty(title))
        binding.tvTitle.text = ""

    binding.tvMessage.text = msg



    binding.et1.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {

            if (binding.et1.text.toString().length == 1) {
                binding.et1.clearFocus()
                binding.et2.requestFocus()
            }
        }
    })

    binding.et2.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {

            if (binding.et2.text.toString().length == 1) {
                binding.et2.clearFocus()
                binding.et3.requestFocus()
            }
        }
    })
    var pin2 = "pin2"

    binding.et3.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {

            if (binding.et3.text.toString().length == 1) {
                binding.et3.clearFocus()
                binding.et4.requestFocus()
            }
        }
    })

    binding.et4.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {


        }
    })




    binding.btnOk.setOnClickListener {
        var pin = "${binding.et1.text.toString()}${binding.et2.text.toString()}${binding.et3.text.toString()}${binding.et4.text.toString()}"
        var pin1 = binding.et1.text.toString()+ binding.et2.text.toString()+ binding.et3.text.toString()+ binding.et4.text.toString()

        MovoApp.db.putString(Constants.MY_EMAIL_CODE,pin1)

        listener.onClickOk()
        dialog.dismiss()
    }

    dialog.setOnCancelListener {
        listener.onClickOk()
    }
    dialog.show()
}



interface IInfoListener {
    fun onClickOk()
    fun onClickCancel()

}
interface IInfoListener2 {
    fun onClickOk2()
    fun onClickCancel2()

}

fun showPlasticCardDialog(listener: IInfoListener) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogPlasticCardBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_plastic_card, null, false)
    dialog.setContentView(binding.root)

    if (!TextUtils.isEmpty(MovoApp.db.getString(Constants.CONST_MY_SHIPPING_INFO)!!)) {
        binding.tvDescription.text = "Your plastic card will arrive within 7-10 business days to ${MovoApp.db.getString(Constants.CONST_MY_SHIPPING_INFO)!!} for a fee of $5.95. If 10 business days have passed and you have not received your card, please contact MoPro Support."
    } else {
        binding.tvDescription.text = "Your plastic card will arrive within 7-10 business days. The card can only be sent to a home address at this time. Make sure to update your profile contact information first. If 10 business days have passed and you have not received your card, please contact MoPro Support."
    }

//    binding.tvDescription.text = msg
    binding.btnConfirm.setOnClickListener {
        //ReissueCardDialog("abc")


        listener.onClickOk()
        dialog.dismiss()
    }

    binding.btnCancel.setOnClickListener {

        dialog.dismiss()
    }

    dialog.setOnCancelListener {
        listener.onClickOk()
    }

    dialog.show()
}


fun confirmReloadsdialog(title: String, msg: String,listener: IInfoListener) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogPlasticCardBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_plastic_card, null, false)
    dialog.setContentView(binding.root)
    binding.tvDescription.text =msg

    /*if (!TextUtils.isEmpty(MovoApp.db.getString(Constants.CONST_MY_SHIPPING_INFO)!!)) {
        binding.tvDescription.text = "Your plastic card will arrive within 7-10 business days to ${MovoApp.db.getString(Constants.CONST_MY_SHIPPING_INFO)!!} for a fee of $5.95. If 10 business days have passed and you have not received your card, please contact MoPro Support."
    } else {
        binding.tvDescription.text = "Your plastic card will arrive within 7-10 business days. The card can only be sent to a home address at this time. Make sure to update your profile contact information first. If 10 business days have passed and you have not received your card, please contact MoPro Support."
    }*/

//    binding.tvDescription.text = msg
    binding.btnConfirm.setOnClickListener {
        //ReissueCardDialog("abc")


        listener.onClickOk()
        dialog.dismiss()
    }

    binding.btnCancel.setOnClickListener {

        listener.onClickCancel()
        dialog.dismiss()
    }

    dialog.setOnCancelListener {
        listener.onClickOk()
    }

    dialog.show()
}

fun confirmToggledialog(title: String, msg: String,listener: IInfoListener) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogEditToggleBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_edit_toggle, null, false)
    dialog.setContentView(binding.root)
    binding.tvDescription.text =msg

    /*if (!TextUtils.isEmpty(MovoApp.db.getString(Constants.CONST_MY_SHIPPING_INFO)!!)) {
        binding.tvDescription.text = "Your plastic card will arrive within 7-10 business days to ${MovoApp.db.getString(Constants.CONST_MY_SHIPPING_INFO)!!} for a fee of $5.95. If 10 business days have passed and you have not received your card, please contact MoPro Support."
    } else {
        binding.tvDescription.text = "Your plastic card will arrive within 7-10 business days. The card can only be sent to a home address at this time. Make sure to update your profile contact information first. If 10 business days have passed and you have not received your card, please contact MoPro Support."
    }*/

//    binding.tvDescription.text = msg
    binding.btnConfirm.setOnClickListener {
        //ReissueCardDialog("abc")


        listener.onClickOk()
        dialog.dismiss()
    }

    binding.btnCancel.setOnClickListener {

        listener.onClickCancel()
        dialog.dismiss()
    }

    dialog.setOnCancelListener {
        listener.onClickOk()
    }

    dialog.show()
}

fun confirmToggledialog2(title: String, msg: String,listener: IInfoListener2) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogEditToggleBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_edit_toggle, null, false)
    dialog.setContentView(binding.root)
    binding.tvDescription.text =msg

    /*if (!TextUtils.isEmpty(MovoApp.db.getString(Constants.CONST_MY_SHIPPING_INFO)!!)) {
        binding.tvDescription.text = "Your plastic card will arrive within 7-10 business days to ${MovoApp.db.getString(Constants.CONST_MY_SHIPPING_INFO)!!} for a fee of $5.95. If 10 business days have passed and you have not received your card, please contact MoPro Support."
    } else {
        binding.tvDescription.text = "Your plastic card will arrive within 7-10 business days. The card can only be sent to a home address at this time. Make sure to update your profile contact information first. If 10 business days have passed and you have not received your card, please contact MoPro Support."
    }*/

//    binding.tvDescription.text = msg
    binding.btnConfirm.setOnClickListener {
        //ReissueCardDialog("abc")


        listener.onClickOk2()
        dialog.dismiss()
    }

    binding.btnCancel.setOnClickListener {

        listener.onClickCancel2()
        dialog.dismiss()
    }

    dialog.setOnCancelListener {
        listener.onClickOk2()
    }

    dialog.show()
}
private fun ReissueCardDialog(title: String) {
    val builder = AlertDialog.Builder(activity)
   // builder.setTitle("Coming Soon!")
    builder.setMessage("Coming Soon!")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
        //Toast.makeText(activity,
                //android.R.string.yes, Toast.LENGTH_SHORT).show()
    }

  /*  builder.setNegativeButton(android.R.string.no) { dialog, which ->
        Toast.makeText(activity,
                android.R.string.no, Toast.LENGTH_SHORT).show()
    }

    builder.setNeutralButton("Maybe") { dialog, which ->
        Toast.makeText(activity,
                "Maybe", Toast.LENGTH_SHORT).show()
    }*/
    builder.show()

}

fun showChequeInfoDialog() {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    val binding: DialogChequeInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_cheque_info, null, false)
    dialog.setContentView(binding.root)
    binding.rlMain.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}

fun showConfirmationDialog(msg: String, listener: IInfoListener) {
    val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(false)
    val binding: DialogConfirmationBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_confirmation, null, false)
    dialog.setContentView(binding.root)

    binding.errorMsg = msg

    binding.btnCancel.setOnClickListener {
        dialog.dismiss()
        listener.onClickCancel()
    }

    binding.btnTryAgain.setOnClickListener {
        dialog.dismiss()
        listener.onClickOk()
    }
    dialog.show()
}