package com.movocash.movo.view.ui.main.echeckbook.mypayee

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.AddEditPayeeRequestModel
import com.movocash.movo.databinding.FragmentPayeeDetailBinding
import com.movocash.movo.utilities.extensions.IInfoListener
import com.movocash.movo.utilities.extensions.gotoHome
import com.movocash.movo.utilities.extensions.showInfoDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.ECheckBookViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper

class PayeeDetailFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentPayeeDetailBinding
    private lateinit var eBookViewModel: ECheckBookViewModel

    companion object {
        lateinit var instance: PayeeDetailFragment
        lateinit var requestObj: AddEditPayeeRequestModel
        lateinit var userState: String
        var isEdit: Boolean = false

        fun newInstance(obj: AddEditPayeeRequestModel, state: String, editMode: Boolean): PayeeDetailFragment {
            instance = PayeeDetailFragment()
            requestObj = obj
            userState = state
            isEdit = editMode
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payee_detail, container, false)
        eBookViewModel = ViewModelProvider(this).get(ECheckBookViewModel::class.java)
        setUIObserver()
        setViews()
        setListeners()
        return binding.root
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.model = requestObj
        binding.isEdit = isEdit
        binding.userState = userState
    }

    private fun setUIObserver() {
        eBookViewModel.payeeAddedOrUpdateFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        eBookViewModel.payeeAddedOrUpdateResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc)) {
                if (!isEdit) {
                    /**track event when adding payee succesfully*/
                    val paramBundle = Bundle()
                    paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                    FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                        requireContext(),
                        CustomEventNames.EVENT_ADD_PAYEE,
                        paramBundle
                    )
                }

                showInfoDialog("", obj.data!!.responseDesc!!, this)
            }
        })

        eBookViewModel.removeFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        eBookViewModel.removeResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc))
                showInfoDialog("", obj.data!!.responseDesc!!, this)
        })
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.btnDelete -> eBookViewModel.removePayee(requestObj.srNo, requestObj.depositAccountNumber)
            R.id.rlRight -> {
                if (!binding.isEdit!!)
                    eBookViewModel.addUpdatePayee(requestObj, false)
                else {
                    addFragment(R.id.mainContainer, AddCustomPayeeFragment.newInstance(requestObj, userState, "", ""), "AddCustomPayeeFragment")
                }
            }
        }
    }

    override fun onClickOk() {
        gotoHome()
        if (binding.isEdit!!) {
            callFragmentWithReplace(R.id.mainContainer, MyPayeesFragment(), null)
        } else
            callFragmentWithReplace(R.id.mainContainer, AddPayeeFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }
}