package com.movocash.movo.view.ui.main.deposithub

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.databinding.DialogCashDepositBinding
import com.movocash.movo.databinding.DialogCashInBinding
import com.movocash.movo.databinding.DialogDirectDepositBinding
import com.movocash.movo.databinding.FragmentDepositHubBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.watchYoutubeVideo
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.BottomSheetSelectorFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import im.delight.android.webview.AdvancedWebView

class DepositHubFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentDepositHubBinding
    var list: ArrayList<String> = ArrayList()
    private var selectedPosition = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_deposit_hub, container, false)
        setViews()
        setListeners()
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.wvDeposit.setMixedContentAllowed(true)
        binding.wvDeposit.loadUrl(Constants.DEPOSIT_URL)

        binding.wvDeposit.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                binding.pbLoading.visibility = View.GONE
            }
        }

    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlDirectDeposit.setOnClickListener(this)
        binding.rlBankCard.setOnClickListener(this)
        binding.rlChain.setOnClickListener(this)
        binding.rlCheck.setOnClickListener(this)
        binding.rlCashIn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.rlDirectDeposit -> showDirectDepositDialog()
            R.id.rlBankCard -> watchYoutubeVideo("xwf_SjjkbZU")
            R.id.rlChain -> openDepositWeb(Constants.CONST_MOVO_CHAIN)
            R.id.rlCheck -> showCashDepositDialog()
            R.id.rlCashIn -> showCashInDialog()
        }
    }

    private fun showDirectDepositDialog() {
        val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val binding: DialogDirectDepositBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_direct_deposit, null, false)
        dialog.setContentView(binding.root)
        binding.rlCurrency.setOnClickListener {
            if (list.isNotEmpty())
                list.clear()

            list.add("$")
            list.add("%")

            val bottomSheet = BottomSheetSelectorFragment(list, selectedPosition, 1)
            if (!bottomSheet.isAdded)
                bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
            bottomSheet.setMyListener(object : BottomSheetSelectorFragment.ISelectListener {
                override fun onSelect(pos: Int, type: Int) {
                    selectedPosition = pos - 1
                    binding.tvCurrency.text = list[pos]
                }

                override fun onCancel(type: Int) {
                }

            })
        }
        binding.rlCross.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showCashInDialog() {
        val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val binding: DialogCashInBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_cash_in, null, false)
        dialog.setContentView(binding.root)
        binding.btnGreenDotReload.setOnClickListener {
            dialog.dismiss()
            openDepositWeb(Constants.CONST_GREEN_RELOAD)
        }
        binding.btnGreenDotMoneyPack.setOnClickListener {
            dialog.dismiss()
            openDepositWeb(Constants.CONST_GREEN_MONEY)
        }
        binding.btnVisaReady.setOnClickListener {
            dialog.dismiss()
            openDepositWeb(Constants.CONST_VISA_READY)
        }
        binding.rlCross.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showCashDepositDialog(){
        val dialog = Dialog(ActivityBase.activity, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val binding: DialogCashDepositBinding = DataBindingUtil.inflate(LayoutInflater.from(ActivityBase.activity), R.layout.dialog_cash_deposit, null, false)
        dialog.setContentView(binding.root)
        binding.btnIngoApp.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnStepDepo.setOnClickListener {
            dialog.dismiss()
            openDepositWeb(Constants.CONST_STEP_DEPOSIT)
        }
        binding.rlCross.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openDepositWeb(type: Int) {
        addFragment(R.id.mainContainer, DepositWebFragment.newInstance(type), "DepositWebFragment")
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
