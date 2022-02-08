package com.movocash.movo.view.ui.main.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentWebBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment

class WebContentFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentWebBinding
    var url = ""

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: WebContentFragment
        var type = 0

        fun newInstance(t: Int): WebContentFragment {
            instance = WebContentFragment()
            type = t
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web, container, false)
        setTitle()
        setListeners()
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setTitle() {
        binding.tvTitle.isSelected = true
        when (type) {
            Constants.WEB_SUPPORT -> {
                binding.rlLeftBack.visibility = View.GONE
                binding.rlLeft.visibility = View.VISIBLE
                binding.tvTitle.text = "MoPro Support"
                url = Constants.MO_PRO_SUPPORT_URL
            }
            Constants.WEB_TERMS -> {
                binding.rlLeftBack.visibility = View.GONE
                binding.rlLeft.visibility = View.VISIBLE
                binding.tvTitle.text = "Terms & Conditions"
                url = Constants.TERMS_CONDITION_URL
            }
            Constants.WEB_PRIVACY -> {
                binding.rlLeftBack.visibility = View.GONE
                binding.rlLeft.visibility = View.VISIBLE
                binding.tvTitle.text = "Privacy Policy"
                url = Constants.PRIVACY_POLICY_URL
            }
            Constants.WEB_AGREEMENT -> {
                binding.rlLeftBack.visibility = View.VISIBLE
                binding.rlLeft.visibility = View.GONE
                binding.tvTitle.text = "Terms and Conditions"
                url = Constants.AGREEMENT_URL
            }
            Constants.WEB_COASTAL -> {
                binding.rlLeftBack.visibility = View.VISIBLE
                binding.rlLeft.visibility = View.GONE
                binding.tvTitle.text = "Coastal Bank Privacy Policy"
                url = Constants.COASTAL_URL
            }
            Constants.WEB_MOVO_PRIVACY -> {
                binding.rlLeftBack.visibility = View.VISIBLE
                binding.rlLeft.visibility = View.GONE
                binding.tvTitle.text = resources.getString(R.string.movo_privacy)
                url = Constants.MOVO_PRIVACY_URL
            }
        }

        binding.wvContent.setMixedContentAllowed(true)
        binding.wvContent.loadUrl(url)
        binding.wvContent.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                binding.pbLoading.visibility = View.GONE
            }
        }

    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlLeftBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.rlLeftBack -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
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