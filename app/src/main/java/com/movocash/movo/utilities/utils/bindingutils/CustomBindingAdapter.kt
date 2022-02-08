/**/package com.movocash.movo.utilities.utils.bindingutils

import android.graphics.Color.parseColor
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CustomPaymentDetailModel
import com.movocash.movo.data.model.requestmodel.MyCustomScheduleTransferModels
import com.movocash.movo.data.model.responsemodel.GetBankAccountsResponseModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.convertCountToMonth
import com.movocash.movo.utilities.helper.TypeFaceEditText
import com.movocash.movo.utilities.helper.TypeFaceTextView
import com.movocash.movo.utilities.utils.DateUtil
import com.movocash.movo.view.ui.base.ActivityBase
import java.lang.String.format
import java.text.MessageFormat.format
import kotlin.math.abs

class CustomBindingAdapter {
    companion object {
        @BindingAdapter("visibleGone")
        @JvmStatic
        fun showHide(view: View, show: Boolean) {
            view.visibility = if (show) View.VISIBLE else View.GONE
        }

        @BindingAdapter("app:setFirstLetter")
        @JvmStatic
        fun firstLetter(view: TypeFaceTextView, data: String) {
            if (data.isNotEmpty()) {
                view.text = data[0].toUpperCase().toString()
            }
        }


        @BindingAdapter("app:setCardType")
        @JvmStatic
        fun setType(view: TypeFaceTextView, data: Int) {
            when (data) {
                Constants.CONST_PRIMARY -> {
                    view.text = "Primary Account"
                }
                Constants.CONST_PURSE -> {

                }
                Constants.CONST_SECONDARY -> {
                    view.text = "CASH Card"
                }
                Constants.CONST_BALANCE -> {

                }
            }
        }

        @BindingAdapter("app:setFrequency")
        @JvmStatic
        fun setFrequency(view: TextView, data: Double) {
            when (data) {
                1.0 -> {
                    view.text = "One Time"
                }
                2.0 -> {
                    view.text = "One Time"

                }
                3.0 -> {
                    view.text = "One Time"
                }


            }
        }

        @BindingAdapter("app:setFrequency")
        @JvmStatic
        fun setFrequency(view: TextView, data: Int) {
            when (data) {
                1 -> {
                    view.text = "One Time"
                }
                2 -> {
                    view.text = "Weekly"

                }
                3 -> {
                    view.text = "Bi-Monthly"
                }
                4 -> {
                    view.text = "Monthly"
                }
                5 -> {
                    view.text = "Yearly"
                }


            }
        }

        @BindingAdapter("app:setCardNumber")
        @JvmStatic
        fun setNumber(view: TypeFaceTextView, data: GetCardAccountsResponseModel.Card) {
            val cardNumber = "*" + data.cardNumber!!.substring(data.cardNumber!!.length - 4)
            view.text = "${data.programAbbreviation}${cardNumber}"
        }

        @BindingAdapter("app:setPayeeCardNumber")
        @JvmStatic
        fun setPayeeNumber(view: TypeFaceTextView, data: String) {
            val cardNumber = "****" + data.substring(data.length - 4)
            view.text = "${cardNumber}"
        }

        @BindingAdapter("app:setFloatValue")
        @JvmStatic
        fun setTextFloat(view: TypeFaceTextView, value: Double) {
            var temp = value
            if (temp < 0) {
                temp = abs(temp)
            }
            view.text = "$${String.format("%.2f", temp)} USD"
        }
        @BindingAdapter("app:setFloatValue")
        @JvmStatic
        fun setTextFloat(view: EditText, value: Double) {
            var temp = value
            if (temp < 0) {
                temp = abs(temp)
            }
            view.setText("$${String.format("%.2f", temp)} USD")
        }

        @BindingAdapter("app:setCardDate")
        @JvmStatic
        fun setMainDate(view: TypeFaceTextView, temp: String) {
            var date = temp
            if (date != null && date.isNotEmpty()) {
                if (date.contains("T"))
                    date = date.split("T")[0]
                val split = date.split("-")
                val year = split[0]
                val month = split[1]
                val day = split[2]

                val monthName = convertCountToMonth(month.toInt())

                view.text = "$monthName $day,$year"
            } else {
                view.text = "---"
            }
        }
        @BindingAdapter("app:setCardDate")
        @JvmStatic
        fun setMainDate(view: TextView, temp: String) {
            var date = temp
            if (date != null && date.isNotEmpty()) {
                if (date.contains("T"))
                    date = date.split("T")[0]
                val split = date.split("-")
                val year = split[0]
                val month = split[1]
                val day = split[2]

                val monthName = convertCountToMonth(month.toInt())

                //view.text = "$monthName $day,$year"
                view.text = "$month-$day-$year"
            } else {
                view.text = "---"
            }
        }
        @BindingAdapter("app:setCardDate")
        @JvmStatic
        fun setMainDate(view: EditText, temp: String) {
            var date = temp
            if (date != null && date.isNotEmpty()) {
                if (date.contains("T"))
                    date = date.split("T")[0]
                val split = date.split("-")
                val year = split[0]
                val month = split[1]
                val day = split[2]

                val monthName = convertCountToMonth(month.toInt())

                view.setText("$monthName $day,$year")
            } else {
                view.setText("---")
            }
        }

        @BindingAdapter("app:setSlashDate")
        @JvmStatic
        fun setExpDate(view: TypeFaceTextView, temp: String) {
            var date = temp
            if (date != null && date.isNotEmpty()) {
                if (date.contains("T"))
                    date = date.split("T")[0]

                val split = date.split("-")
                val year = split[0]
                val month = split[1].toInt()
                var stringMonth = "$month"
                if (month < 10)
                    stringMonth = "0$month"

                view.text = "$stringMonth/$year"
            } else {
                view.text = "---"
            }
        }

        @BindingAdapter("app:setSigns")
        @JvmStatic
        fun setSignValue(view: TypeFaceTextView, amount: Double) {
            if (amount < 0) {
                view.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.red))
                view.text = "-"
            } else {
                view.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                view.text = "+"
            }
        }

        @BindingAdapter("app:setCurrentDate")
        @JvmStatic
        fun setNowDate(view: TypeFaceTextView, amount: String) {
            var date = DateUtil.getCurrentDate()
            if (date!!.contains("T")) {
                date = date.split("T")[0]
            }
            val split = date!!.split("-")
            val year = split[0]
            val month = split[1]
            val day = split[2]

            val monthName = convertCountToMonth(month.toInt())

            view.text = "$monthName $day,$year"
        }

        @BindingAdapter("app:setNumberOnCard")
        @JvmStatic
        fun setNumberCard(view: TypeFaceTextView, cardNum: String) {
            view.text = "${cardNum[0]}${cardNum[1]}${cardNum[2]}${cardNum[3]} ${cardNum[4]}${cardNum[5]}${cardNum[6]}${cardNum[7]} ${cardNum[8]}${cardNum[9]}${cardNum[10]}${cardNum[11]} ${cardNum[12]}${cardNum[13]}${cardNum[14]}${cardNum[15]}"
        }

        @BindingAdapter("app:setUserName")
        @JvmStatic
        fun setName(view: TypeFaceEditText, s: String) {
            view.setText("${MovoApp.db.getString(Constants.FIRST_NAME)} ${MovoApp.db.getString(Constants.LAST_NAME)}")
        }

        @BindingAdapter("app:setLoginDate")
        @JvmStatic
        fun setLastDate(view: TypeFaceTextView, temp: String) {
            var date = MovoApp.db.getString(Constants.USER_LAST_LOGIN)
            if (date == null || date.isEmpty()) {
                date = DateUtil.getCurrentDate()
            }
            if (date!!.contains("T"))
                date = date!!.split("T")[0]
            val split = date.split("-")
            val year = split[0]
            val month = split[1]
            val day = split[2]

            val monthName = convertCountToMonth(month.toInt())
            view.text = "$monthName $day,$year"
        }

        @BindingAdapter("app:setBankNum")
        @JvmStatic
        fun setBankAccNum(view: TypeFaceTextView, data: GetBankAccountsResponseModel.Account) {
            //Toast.makeText(ActivityBase.activity, data.accountNumber, Toast.LENGTH_LONG).show()
            if(data.accountNumber!!.length>6)
            {
                val cardNumber = "*" + data.accountNumber!!.substring(data.accountNumber!!.length - 4)
                view.text = "${data.accountNickname}${cardNumber}"
            }
            else
            {
                val cardNumber = "*" + data.accountNumber!!
                view.text = "${data.accountNickname}${cardNumber}"
            }
            /*val cardNumber = "*" + data.accountNumber!!.substring(data.accountNumber!!.length - 4)
            view.text = "${data.accountNickname}${cardNumber}"*/
        }

        @BindingAdapter("app:setAccountType")
        @JvmStatic
        fun setAccountType(view: TypeFaceTextView, data: GetBankAccountsResponseModel.Account) {
            //Toast.makeText(ActivityBase.activity, data.accountNumber, Toast.LENGTH_LONG).show()
            if(data.achType == Constants.CARD_TO_BANK)
            {
                view.text = "Type: Card to Bank"

            }
            else
            {
                view.text = "Type: Bank to MOVO"
            }
            /*val cardNumber = "*" + data.accountNumber!!.substring(data.accountNumber!!.length - 4)
            view.text = "${data.accountNickname}${cardNumber}"*/
        }

        @BindingAdapter("app:setTransferType")
        @JvmStatic
        fun setTransferType(view: TypeFaceTextView, data: MyCustomScheduleTransferModels) {
            //Toast.makeText(ActivityBase.activity, data.accountNumber, Toast.LENGTH_LONG).show()
            if(data.achType == Constants.CARD_TO_BANK.toLong())
            {
                view.text = "Movo to Bank"
            }
            else
            {
                view.text = "Bank to Movo"
            }
            /*val cardNumber = "*" + data.accountNumber!!.substring(data.accountNumber!!.length - 4)
            view.text = "${data.accountNickname}${cardNumber}"*/
        }

        @BindingAdapter("app:setTransactionSign")
        @JvmStatic
        fun setTransactionSign(view: TypeFaceTextView, data: MyCustomScheduleTransferModels) {
            //Toast.makeText(ActivityBase.activity, data.accountNumber, Toast.LENGTH_LONG).show()
            if(data.achType == Constants.CARD_TO_BANK.toLong())
            {
                view.text = "-"
            }
            else
            {
                view.text = "+"
                view.setTextColor(parseColor("#228B22"))

            }
            /*val cardNumber = "*" + data.accountNumber!!.substring(data.accountNumber!!.length - 4)
            view.text = "${data.accountNickname}${cardNumber}"*/
        }

        @BindingAdapter("app:setAccountName")
        @JvmStatic
        fun setAccountName(view: TypeFaceTextView, data: MyCustomScheduleTransferModels) {
            //Toast.makeText(ActivityBase.activity, data.accountNumber, Toast.LENGTH_LONG).show()
            if(data.achType == Constants.CARD_TO_BANK.toLong())
            {
                view.text = "From Movo"
            }
            else
            {
                view.text = "From Bank"
            }
            /*val cardNumber = "*" + data.accountNumber!!.substring(data.accountNumber!!.length - 4)
            view.text = "${data.accountNickname}${cardNumber}"*/
        }
        @BindingAdapter("app:setAccountName2")
        @JvmStatic
        fun setAccountName2(view: TypeFaceTextView, data: MyCustomScheduleTransferModels) {
            //Toast.makeText(ActivityBase.activity, data.accountNumber, Toast.LENGTH_LONG).show()
            if(data.achType == Constants.CARD_TO_BANK.toLong())
            {
                view.text = "To Bank"
            }
            else
            {
                view.text = "To Movo"
            }
            /*val cardNumber = "*" + data.accountNumber!!.substring(data.accountNumber!!.length - 4)
            view.text = "${data.accountNickname}${cardNumber}"*/
        }

        @BindingAdapter("app:setTitle")
        @JvmStatic
        fun setTitle(view: TypeFaceTextView, data: MyCustomScheduleTransferModels) {
            //Toast.makeText(ActivityBase.activity, data.accountNumber, Toast.LENGTH_LONG).show()
            if(data.achType == Constants.CARD_TO_BANK.toLong())
            {
                view.text = "Cash out to Bank"
            }
            else
            {
                view.text = "Cash in to Movo"
            }
            /*val cardNumber = "*" + data.accountNumber!!.substring(data.accountNumber!!.length - 4)
            view.text = "${data.accountNickname}${cardNumber}"*/
        }

        @BindingAdapter("app:setFromAccount")
        @JvmStatic
        fun setFromAccount(view: TypeFaceTextView, data: MyCustomScheduleTransferModels) {
            //Toast.makeText(ActivityBase.activity, data.accountNumber, Toast.LENGTH_LONG).show()
            if(data.achType == Constants.CARD_TO_BANK.toLong())
            {
                view.text = data.fromAccount
            }
            else
            {
                view.text = data.toAccount
            }
            /*val cardNumber = "*" + data.accountNumber!!.substring(data.accountNumber!!.length - 4)
            view.text = "${data.accountNickname}${cardNumber}"*/
        }
        @BindingAdapter("app:setToAccount")
        @JvmStatic
        fun setToAccount(view: TypeFaceTextView, data: MyCustomScheduleTransferModels) {
            //Toast.makeText(ActivityBase.activity, data.accountNumber, Toast.LENGTH_LONG).show()
            if(data.achType == Constants.CARD_TO_BANK.toLong())
            {
                view.text = data.toAccount
            }
            else
            {
                view.text = data.fromAccount
            }
            /*val cardNumber = "*" + data.accountNumber!!.substring(data.accountNumber!!.length - 4)
            view.text = "${data.accountNickname}${cardNumber}"*/
        }

        @BindingAdapter("app:setPaymentStatus")
        @JvmStatic
        fun setStatus(view: TypeFaceTextView, status: String) {
            when (status) {
                Constants.PAY_SCHEDULED -> view.text = "Scheduled"
                Constants.PAY_FAILED -> view.text = "Failed"
                Constants.PAY_PROCESSED -> view.text = "Processed"
                Constants.PAY_CANCELLED -> view.text = "Cancelled"
                Constants.PAY_IN_PROGRESS -> view.text = "In Progress"
                Constants.PAY_DEFFERRED -> view.text = "Deferred"
                Constants.PAY_DEBIT_SUCCESSFUL -> view.text = "Successful"
                Constants.PAY_LOGGED -> view.text = "Logged"
            }
        }

        @BindingAdapter("app:setPaymentStatusIcons")
        @JvmStatic
        fun setStatusIcons(view: ImageView, status: String) {
            when (status) {
                Constants.PAY_SCHEDULED -> view.setImageResource(R.drawable.ic_scheduled_transfer)
                Constants.PAY_FAILED -> view.setImageResource(R.drawable.ic_failed)
                Constants.PAY_PROCESSED -> view.setImageResource(R.drawable.ic_success)
                Constants.PAY_CANCELLED -> view.setImageResource(R.drawable.ic_cancelled)
                Constants.PAY_IN_PROGRESS -> view.setImageResource(R.drawable.ic_in_progress)
                Constants.PAY_DEFFERRED -> view.setImageResource(R.drawable.ic_scheduled_transfer)
                Constants.PAY_DEBIT_SUCCESSFUL -> view.setImageResource(R.drawable.ic_success)
                Constants.PAY_LOGGED ->view.setImageResource(R.drawable.ic_scheduled_transfer)
            }
        }

        @BindingAdapter("app:setMakePaymentStatusIcons")
        @JvmStatic
        fun setMakeStatusIcons(view: ImageView, status: String) {
            when (status) {
                Constants.PAYMENT_LOGGED -> view.setImageResource(R.drawable.ic_scheduled_transfer)
                Constants.PAY_SCHEDULED -> view.setImageResource(R.drawable.ic_scheduled_transfer)
                Constants.PAY_PROCESSED -> view.setImageResource(R.drawable.ic_scheduled_transfer)
                Constants.PAY_DEBIT_SUCCESSFUL -> view.setImageResource(R.drawable.ic_success)
                Constants.PAY_FAILED -> view.setImageResource(R.drawable.ic_failed)
                Constants.PAY_CANCELLED -> view.setImageResource(R.drawable.ic_cancelled)
                Constants.PAYMENT_RETURNED -> view.setImageResource(R.drawable.ic_cancelled)
                Constants.PAY_IN_PROGRESS -> view.setImageResource(R.drawable.ic_in_progress)
            }
        }

        const val PAY_SCHEDULED = "S"
        const val PAY_FAILED = "F"
        const val PAY_PROCESSED = "P"
        const val PAY_CANCELLED = "C"
        const val PAY_IN_PROGRESS = "I"
        const val PAY_DEFFERRED = "D"
        const val PAY_DEBIT_SUCCESSFUL = "B"
        const val PAY_LOGGED = "L"

        @BindingAdapter("app:setCardStatus")
        @JvmStatic
        fun setStatusCards(view: ImageView, status: String) {
            when (status) {
                Constants.CARD_CLOSED -> view.setImageResource(R.drawable.ic_closed_card)
                Constants.CARD_INACTIVE -> view.setImageResource(R.drawable.ic_closed_card)
                Constants.CARD_ISSUED_INACTIVE -> view.setImageResource(R.drawable.ic_closed_card)

                else -> view.setImageResource(R.drawable.logo)
            }
        }

        @BindingAdapter("app:setAccountStatus")
        @JvmStatic
        fun setStatusAccount(view: TextView, status: String) {
            when (status) {
                Constants.ACCOUNT_FAILED -> view.setText("(Failed)")
                Constants.ACCOUNT_LOGGED -> view.setText("(Logged)")
                Constants.ACCOUNT_VERIFIED -> view.setText("(Verified)")
                Constants.ACCOUNT_INPROGRESS -> view.setText("(In Progress)")
                Constants.NOT_APPLICABLE -> view.setText("(Not Verified)")
                else -> view.setText("(Closed)")
            }
        }

       /* @BindingAdapter("app:setAccountStatus")
        @JvmStatic
        fun setStatusAccount(view: TextView, status: String) {
            when (status) {
                Constants.ACCOUNT_FAILED -> view.setText("(Closed)")
                Constants.ACCOUNT_LOGGED -> view.setImageResource(R.drawable.ic_closed_card)
                Constants.ACCOUNT_VERIFIED -> view.setImageResource(R.drawable.ic_closed_card)
                Constants.ACCOUNT_INPROGRESS -> view.setImageResource(R.drawable.ic_closed_card)
                Constants.NOT_APPLICABLE -> view.setImageResource(R.drawable.ic_closed_card)
                else -> view.setImageResource(R.drawable.logo)
            }
        }*/

        @BindingAdapter("app:setCardStatusString")
        @JvmStatic
        fun setStatusCardsString(view: TextView, status: String) {
            when (status) {
                Constants.CARD_CLOSED -> view.setText("(Closed)")
                Constants.CARD_INACTIVE -> view.setText("(Issued & inActive)")
                else -> view.setText("(Open - All Transactions Allowed)")
            }
        }


    }
}