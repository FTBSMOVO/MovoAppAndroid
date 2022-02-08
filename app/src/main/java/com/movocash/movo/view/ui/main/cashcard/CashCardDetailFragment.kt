package com.movocash.movo.view.ui.main.cashcard

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.i2cinc.mcpsdk.MCPSDKManager
import com.i2cinc.mcpsdk.config.ScreenPresentingOption
import com.i2cinc.mcpsdk.config.UIConfig
import com.i2cinc.mcpsdk.interfaces.MCPSDKCallback
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.GetAuthTokenRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentCashCardDetailBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper
import com.movocash.movo.utilities.extensions.IInfoListener
import com.movocash.movo.utilities.extensions.copyText
import com.movocash.movo.utilities.extensions.showInfoDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.CardViewModel


class CashCardDetailFragment : BaseFragment(), View.OnClickListener, IInfoListener, MCPSDKCallback {

    lateinit var binding: FragmentCashCardDetailBinding
    private var cardImage: Bitmap? = null
    private lateinit var cardsViewModel: CardViewModel
    private lateinit var cardCVV: String
    var cardToken: String = ""
    lateinit var referenceID: String

    companion object {
        lateinit var instance: CashCardDetailFragment
        lateinit var cardObj: GetCardAccountsResponseModel.Card

        fun newInstance(obj: GetCardAccountsResponseModel.Card): CashCardDetailFragment {
            instance = CashCardDetailFragment()
            cardObj = obj
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cash_card_detail, container, false)
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        setUiObserver()
        setViews()
        setListeners()
        return binding.root
    }

    private fun setUiObserver() {

        cardsViewModel.authFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.authResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (obj?.cardData != null && obj.cardData!!.cvV2 != null) {
                cardCVV = obj.cardData!!.cvV2!!
                binding.model!!.cvv = cardCVV
                binding.model = binding.model!!
            }
        })


        cardsViewModel.authTokenFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })
        cardsViewModel.authTokenResponse.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.data != null && obj.data!!.signOnToken != null && !TextUtils.isEmpty(obj.data!!.signOnToken))
                    cardToken = obj.data!!.signOnToken!!

                startTaskForCardUI()
            }

        })

    }

    private fun startTaskForCardUI() {
        val config = UIConfig()
        config.presentingOption = ScreenPresentingOption.DIALOG
        config.backgroundColor = ContextCompat.getColor(ActivityBase.activity, R.color.red).toString()
        config.loadingIndicatorColor = ContextCompat.getColor(ActivityBase.activity, R.color.white).toString()
        MCPSDKManager.getInstance().setUiConfig(config)
        val params: HashMap<String, String> = HashMap()
        params["cardRefNo"] = referenceID
        params["authToken"] = cardToken
        MCPSDKManager.getInstance().startTask(Constants.MOVO_GET_CARD_INFO, params, this)
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.model = cardObj
        referenceID = cardObj.referenceID!!
        cardImage = encodeAsBitmap(cardObj.cardNumber!!)
        cardsViewModel.getCardAuthData(cardObj.referenceID!!)
    }


    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.llEditCard.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.btnCopyNum.setOnClickListener(this)
        binding.btnReload.setOnClickListener(this)
        binding.btnTransferBalance.setOnClickListener(this)
        binding.btnViewQR.setOnClickListener(this)
        binding.btnViewInfo.setOnClickListener(this)
        binding.btnScheduleReloads.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnViewInfo -> {
                cardsViewModel.getAuthToken(GetAuthTokenRequestModel(referenceID, ""))
            }
            R.id.btnScheduleReloads -> {
                //addFragment(R.id.mainContainer, AutoReloads.newInstance(referenceID), "ReloadCashCardFragment")
                addFragment(R.id.mainContainer, schedulerAndKeyChain.newInstance(referenceID), "ReloadCashCardFragment")
            }
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.btnHideCard -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                addFragment(R.id.mainContainer, AddCashCardFragment.newInstance(false), "AddCashCardFragment")
            }
            R.id.btnCopyNum -> {
                /**track click on copy cash card when user clicks on it*/
                val paramBundle = Bundle()
                paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                    requireContext(),
                    CustomEventNames.EVENT_COPY_CASH_CARD,
                    paramBundle
                )

                ActivityBase.activity.showToastMessage("Card Number Copied..")
                ActivityBase.activity.copyText(cardObj.cardNumber!!)
            }
            R.id.btnReload -> {
                /**track click on reload cash card when user clicks on it*/
                val paramBundle = Bundle()
                paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                    requireContext(),
                    CustomEventNames.EVENT_RELOAD_CASH_CARD,
                    paramBundle
                )

                addFragment(
                    R.id.mainContainer,
                    ReloadCashCardFragment.newInstance(cardObj),
                    "ReloadCashCardFragment"
                )


            }
            R.id.btnTransferBalance -> {
                if (cardObj.balance == 0.0) {
                    showInfoDialog("Alert", "You have insufficient balance to transfer", this)

                } else
                    addFragment(R.id.mainContainer, UnloadCashCardFragment.newInstance(cardObj), "UnloadCashCardFragment")

            }
            R.id.btnViewQR -> {
                addFragment(R.id.mainContainer, CardQRFragment.newInstance(cardImage!!, cardObj.balance), "CardQRFragment")
            }
            R.id.llEditCard -> {
                callFragmentWithReplace(R.id.mainContainer, ChangeCashCardNameFragment.newInstance(cardObj), "AddCashCardFragment")
            }
        }
    }

    @Throws(WriterException::class)
    fun encodeAsBitmap(str: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val manager = ActivityBase.activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = manager.defaultDisplay
            val point = Point()
            display.getSize(point)
            val width = point.x
            val height = point.y
            val result: BitMatrix
            try {
                result = MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 600, 600, null)
            } catch (iae: IllegalArgumentException) {
                // Unsupported format
                return null
            }

            val w = result.width
            val h = result.height
            val pixels = IntArray(w * h)
            for (y in 0 until h) {
                val offset = y * w
                for (x in 0 until w) {
                    pixels[offset + x] = if (result.get(x, y)) Color.parseColor("#333333") else Color.WHITE
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
            return bitmap

        } catch (ex: Exception) {
            ex.printStackTrace()
            return bitmap
        }

    }

    override fun onClickOk() {

    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }

    override fun onLoadingStarted(): Boolean {
        return true
    }

    override fun onLoadingCompleted() {
    }

    override fun onSuccess(p0: MutableMap<String, String>?) {
    }

    override fun onError(p0: String?, p1: String?) {
    }

    override fun onCancel() {
    }
}