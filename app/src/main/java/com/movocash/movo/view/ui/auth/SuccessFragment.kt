package com.movocash.movo.view.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentCongratulationBinding
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper

class SuccessFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentCongratulationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_congratulation, container, false)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.btnSignIn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnSignIn -> callFragmentWithReplace(R.id.authContainer, SignInFragment(), null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**Calling the function to track the welcome page event to firebase */
        val paramBundle = Bundle()
        paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
        FirebaseAnalyticsEventHelper.trackAnalyticEvent(
            requireContext(),
            CustomEventNames.EVENT_WELCOME,
            paramBundle
        )
    }
}