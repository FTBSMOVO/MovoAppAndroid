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
import com.movocash.movo.data.model.responsemodel.IDNameResponseModel
import com.movocash.movo.databinding.FragmentAddCustomPayeeBinding
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.BottomSheetSelectorFragment
import com.movocash.movo.viewmodel.CommonViewModel
import com.movocash.movo.viewmodel.ECheckBookViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment

class AddCustomPayeeFragment : BaseFragment(), View.OnClickListener, BottomSheetSelectorFragment.ISelectListener, IInfoListener {

    lateinit var binding: FragmentAddCustomPayeeBinding
    private lateinit var commonViewModel: CommonViewModel
    private lateinit var eBookViewModel: ECheckBookViewModel
    private var myStateList: ArrayList<String> = ArrayList()
    private var statePosition = 0
    private var stateList: ArrayList<IDNameResponseModel.Model> = ArrayList()
    private var isStateSet = false

    companion object {
        lateinit var instance: AddCustomPayeeFragment
        var requestModel: AddEditPayeeRequestModel? = null
        var userState: String? = null
        var payeeSrNo: String = ""
        var payeeName: String = ""

        fun newInstance(obj: AddEditPayeeRequestModel?, state: String?, srNo: String, name: String): AddCustomPayeeFragment {
            instance = AddCustomPayeeFragment()
            requestModel = obj
            userState = state
            payeeSrNo = srNo
            payeeName = name
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_custom_payee, container, false)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        eBookViewModel = ViewModelProvider(this).get(ECheckBookViewModel::class.java)
        setUiObserver()
        setViews()
        getStates()
        setListeners()
        return binding.root
    }

    private fun setUiObserver() {
        commonViewModel.statesResponse.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.list != null && obj.list!!.size > 0) {
                    if (stateList.size > 0)
                        stateList.clear()
                    stateList.addAll(obj.list!!)

                    if (requestModel != null) {
                        if (stateList.any { it.iso2 == userState }) {
                            statePosition = stateList.indexOf(stateList.filter { it.iso2 == userState }[0])
                            binding.tvSelectedState.text = stateList.filter { it.iso2 == userState }[0].name
                        }
                    }
                }
            }
        })

        eBookViewModel.payeeAddedOrUpdateFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })

        eBookViewModel.payeeAddedOrUpdateResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj != null && obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc!!)) {
                    showInfoDialog("", obj.data!!.responseDesc!!, this)
                }

            }
        })
    }


    private fun getStates() {
        commonViewModel.getStateByCountry(233)
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.model = requestModel
        binding.userState = userState

        if (requestModel != null)
            isStateSet = true

        if (!TextUtils.isEmpty(payeeSrNo)) {
            binding.payeeName = payeeName
        } else
            binding.payeeName = ""
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.rlState.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlState -> if (stateList.size > 0) {
                showStateDialog()
            }
            R.id.rlRight -> {
                if (validateInput()) {
                    if (requestModel != null) {
                        editPayee()
                    } else {
                        addPayee()
                    }
                }
            }
        }
    }

    private fun addPayee() {
        val obj = AddEditPayeeRequestModel(if (!TextUtils.isEmpty(payeeSrNo)) payeeSrNo else "", binding.etPayeeName.text.toString(), binding.etAddress.text.toString(), binding.etCity.text.toString(), stateList[statePosition].id, stateList[statePosition].iso2, binding.etZip.text.toString(), binding.etNickname.text.toString(), binding.etAccNum.text.toString())
        addFragment(R.id.mainContainer, PayeeDetailFragment.newInstance(obj, binding.tvSelectedState.text.toString(), false), "PayeeDetailFragment")
    }

    private fun editPayee() {
        val obj = AddEditPayeeRequestModel(requestModel!!.srNo, binding.etPayeeName.text.toString(), binding.etAddress.text.toString(), binding.etCity.text.toString(), stateList[statePosition].id, stateList[statePosition].iso2, binding.etZip.text.toString(), binding.etNickname.text.toString(), binding.etAccNum.text.toString())
        eBookViewModel.addUpdatePayee(obj, true)
    }

    private fun validateInput(): Boolean {
        if (TextUtils.isEmpty(binding.etPayeeName.text.toString())) {
            binding.etPayeeName.requestFocus()
            binding.etPayeeName.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etAddress.text.toString())) {
            binding.etAddress.requestFocus()
            binding.etAddress.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etCity.text.toString())) {
            binding.etCity.requestFocus()
            binding.etCity.errorAnim(ActivityBase.activity)
            return false
        } else if (!isStateSet) {
            ActivityBase.activity.showToastMessage("Please Select State")
            return false
        } else if (TextUtils.isEmpty(binding.etZip.text.toString())) {
            binding.etZip.requestFocus()
            binding.etZip.errorAnim(ActivityBase.activity)
            return false
        }
        else if (binding.etZip.text.toString().length < 5) {
            binding.etZip.requestFocus()
            ActivityBase.activity.showToastMessage("Invalid Zip Code")
            binding.etZip.errorAnim(ActivityBase.activity)
            return false
        }

        else if (TextUtils.isEmpty(binding.etNickname.text.toString())) {
            binding.etNickname.requestFocus()
            binding.etNickname.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etAccNum.text.toString())) {
            binding.etAccNum.requestFocus()
            binding.etAccNum.errorAnim(ActivityBase.activity)
            return false
        } /* else if (binding.etAccNum.text.toString().length < 16) {
            binding.etAccNum.requestFocus()
            ActivityBase.activity.showToastMessage("Invalid Account Number")
            binding.etAccNum.errorAnim(ActivityBase.activity)
            return false
        }*/else if (TextUtils.isEmpty(binding.etConfirmNum.text.toString())) {
            binding.etConfirmNum.requestFocus()
            binding.etConfirmNum.errorAnim(ActivityBase.activity)
            return false
        } else if (binding.etConfirmNum.text.toString() != binding.etAccNum.text.toString()) {
            binding.etConfirmNum.requestFocus()
            binding.etConfirmNum.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Account Number not matched")
            return false
        } else {
            return true
        }
    }

    private fun showStateDialog() {
        if (myStateList.size > 0)
            myStateList.clear()

        stateList.mapTo(myStateList) { it.name }
        setBottomSheet(myStateList, statePosition, 1)
    }

    private fun setBottomSheet(list: ArrayList<String>, selectedPosition: Int, type: Int) {
        val bottomSheet = BottomSheetSelectorFragment(list, selectedPosition, type)
        if (!bottomSheet.isAdded)
            bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
        bottomSheet.setMyListener(this)
    }

    override fun onSelect(pos: Int, type: Int) {
        statePosition = pos - 1
        binding.tvSelectedState.text = myStateList[pos]
        isStateSet = true
    }

    override fun onCancel(type: Int) {

    }

    override fun onClickOk() {
        gotoHome()
        callFragmentWithReplace(R.id.mainContainer, MyPayeesFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }
}