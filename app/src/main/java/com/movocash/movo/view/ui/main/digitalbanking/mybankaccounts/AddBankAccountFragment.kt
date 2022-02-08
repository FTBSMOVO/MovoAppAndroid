package com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CreateBankAccountRequestModel
import com.movocash.movo.databinding.FragmentAddBankAccountBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.BottomSheetSelectorFragment
import com.movocash.movo.viewmodel.BankViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.movocash.movo.MovoApp
import com.movocash.movo.data.model.requestmodel.CustomEditScheduledTransferModel
import com.movocash.movo.data.model.responsemodel.GetBankAccountsResponseModel
import com.movocash.movo.view.ui.main.digitalbanking.cashoutbank.AchDirect
import com.movocash.movo.view.ui.main.digitalbanking.cashoutbank.AchoutToBank
import com.movocash.movo.view.ui.main.digitalbanking.cashoutbank.CashOutBankFragment
import com.movocash.movo.view.ui.main.digitalbanking.cashoutbank.cashintoMovo
import com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts.adapter.AdapterBankAccounts
import com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts.adapter.AdapterMyBankAccounts
import com.movocash.movo.view.ui.main.digitalbanking.scheduletransfer.ScheduleTransferDetailFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import java.util.*

class AddBankAccountFragment : BaseFragment(), View.OnClickListener, BottomSheetSelectorFragment.ISelectListener, IInfoListener, TextWatcher, SwipeRefreshLayout.OnRefreshListener, AdapterBankAccounts.IBankAccounts  {

    lateinit var binding: FragmentAddBankAccountBinding
    // var list: ArrayList<String> = ArrayList(cam)
    var list: ArrayList<String> = ArrayList()

    private var selectedPosition = 0
    private var isTypeSet = false
    private lateinit var bankViewModel: BankViewModel
    private var isMovoSubOpen = false
    private var mList: ArrayList<GetBankAccountsResponseModel.Account> = ArrayList()
    private var nonPlaidmList: ArrayList<GetBankAccountsResponseModel.Account> = ArrayList()

    companion object {
        lateinit var instance: AddBankAccountFragment
        var selectedType = 0
        var isEdit = false
        private var requestObj: CreateBankAccountRequestModel? = null

        fun newInstance(type: Int, isEd: Boolean, obj: CreateBankAccountRequestModel?): AddBankAccountFragment {
            instance = AddBankAccountFragment()
            selectedType = type
            isEdit = isEd
            requestObj = obj
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_bank_account, container, false)
        bankViewModel = ViewModelProvider(this).get(BankViewModel::class.java)
        setUIObserver()
        setListeners()
        setViews()
        getAllBankAccounts()
        return binding.root
    }

    private fun setUIObserver() {
        bankViewModel.editBankAccountFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        bankViewModel.editBankAccountResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc))
                showInfoDialog("", obj.data!!.responseDesc!!, this)
        })


        bankViewModel.getBankAccountsResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            binding.srlBanks.isRefreshing = false
            if (obj != null && obj.data != null && obj.data!!.accounts != null && obj.data!!.accounts!!.size > 0) {
                MovoApp.db.putString(Constants.CONST_MY_BANKS_RESPONSE, Gson().toJson(obj))
                // binding.isData = true
                mList.clear()
                mList.addAll(obj.data!!.accounts!!)
                nonPlaidmList = mList.filter { it.achType==2 } as ArrayList<GetBankAccountsResponseModel.Account>
                setAdapter()
            } else
            {

            }
            //binding.isData = false
        })
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        if (isEdit) {
            binding.model = requestObj
            isTypeSet = true
            binding.tvRight.text = "Save"
        } else {
            binding.model = null
            binding.tvRight.text = "Next"
        }
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.rlbankAccounts.setOnClickListener(this)
        binding.rlSelectAccountType.setOnClickListener(this)
        binding.ivInfo1.setOnClickListener(this)
        binding.ivInfo2.setOnClickListener(this)
//        binding.etNickname.addTextChangedListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft ->{
                //ActivityBase.activity.showToastMessage("Back")
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, MyCardsFragment(), null)
            }
            R.id.rlbankAccounts ->{

                bankAccountsList()

            }
            R.id.rlRight -> {
                if (validateInput()) {
                    if (isEdit)
                        proceedToEdit()
                    else
                        proceedToAdd()
                }
            }
            R.id.rlSelectAccountType -> {
                selectBankDialog()
            }
            R.id.ivInfo1 -> {
                showChequeInfoDialog()
            }
            R.id.ivInfo2 -> {
                showChequeInfoDialog()
            }

        }
    }

    private fun proceedToEdit() {
        val isCheckingAccount = binding.tvAccountType.text.toString() == "Checking Account"
        val obj = CreateBankAccountRequestModel(requestObj!!.bankSerialNumberIfEdit, Constants.CARD_TO_BANK, binding.etName.text.toString(), binding.etBankName.text.toString(), binding.etBankRout.text.toString(), isCheckingAccount, binding.etBankAccount.text.toString(), binding.etNickname.text.toString(), binding.etComments.text.toString())
        bankViewModel.createBankAccount(obj)
    }

    private fun selectBankDialog() {
        if (list.isNotEmpty())
            list.clear()

        list.add("Checking Account")
        list.add("Saving Account")

        val bottomSheet = BottomSheetSelectorFragment(list, selectedPosition, 1)
        if (!bottomSheet.isAdded)
            bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
        bottomSheet.setMyListener(this)
    }

    private fun proceedToAdd() {
        binding.root.hideKeyboard()
        val isCheckingAccount = binding.tvAccountType.text.toString() == "Checking Account"
        val obj = CreateBankAccountRequestModel("", selectedType, binding.etName.text.toString(), binding.etBankName.text.toString(), binding.etBankRout.text.toString(), isCheckingAccount, binding.etBankAccount.text.toString(), binding.etNickname.text.toString(), binding.etComments.text.toString())
        addFragment(R.id.mainContainer, AddBankAccountConfirmationFragment.newInstance(obj), "AddBankAccountConfirmationFragment")
    }



    override fun onSelect(pos: Int, type: Int) {
        selectedPosition = pos - 1
        binding.tvAccountType.text = list[pos]
        isTypeSet = true
    }

    override fun onCancel(type: Int) {
    }

    private fun validateInput(): Boolean {
        if (TextUtils.isEmpty(binding.etBankName.text.toString())) {
            binding.etBankName.requestFocus()
            binding.etBankName.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etBankRout.text.toString())) {
            binding.etBankRout.requestFocus()
            binding.etBankRout.errorAnim(ActivityBase.activity)
            return false
        } else if (binding.etBankRout.text.toString().length < 9) {
            binding.etBankRout.requestFocus()
            binding.etBankRout.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etConfirmRout.text.toString())) {
            binding.etConfirmRout.requestFocus()
            binding.etConfirmRout.errorAnim(ActivityBase.activity)
            return false
        } else if (binding.etBankRout.text.toString() != binding.etConfirmRout.text.toString()) {
            binding.etConfirmRout.requestFocus()
            binding.etConfirmRout.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Routing Number doesn't match")
            return false
        } else if (!isTypeSet) {
            ActivityBase.activity.showToastMessage("Please Select Account Type")
            return false
        } else if (TextUtils.isEmpty(binding.etBankAccount.text.toString())) {
            binding.etBankAccount.requestFocus()
            binding.etBankAccount.errorAnim(ActivityBase.activity)
            return false
        } /*else if (binding.etBankAccount.text.toString().length < 17) {            binding.etBankAccount.requestFocus()            binding.etBankAccount.errorAnim(ActivityBase.activity)
            return false
        } */else if (TextUtils.isEmpty(binding.etConfirmBank.text.toString())) {
            binding.etConfirmBank.requestFocus()
            binding.etConfirmBank.errorAnim(ActivityBase.activity)
            return false
        } else if (binding.etBankAccount.text.toString() != binding.etConfirmBank.text.toString()) {
            binding.etConfirmBank.requestFocus()
            binding.etConfirmBank.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Bank Number doesn't match")
            return false
        } else if (TextUtils.isEmpty(binding.etNickname.text.toString())) {
            binding.etNickname.requestFocus()
            binding.etNickname.errorAnim(ActivityBase.activity)
            return false
        } else
            return true
    }

    override fun onClickOk() {
        gotoHome()
        callFragmentWithReplace(R.id.mainContainer, MyBankAccountFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        if (!TextUtils.isEmpty(binding.etNickname.text.toString())) {
            binding.etNickname.setText(binding.etNickname.text.toString().trim())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()


                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                //callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
                addFragment(R.id.mainContainer, MyCardsFragment(), null)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    private fun bankAccountsList() {
        if (isMovoSubOpen) {
            isMovoSubOpen = false
            binding.elBankAccounts.collapse()
            binding.ivDigitalDrop.rotation = 0f
        } else {
            isMovoSubOpen = true
            binding.elBankAccounts.expand()
            binding.ivDigitalDrop.rotation = 180f
        }
    }


    private fun setAdapter() {
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvBankAccounts.layoutManager = manager
        val adapter = AdapterBankAccounts(nonPlaidmList)
        binding.rvBankAccounts.adapter = adapter
        adapter.setMyListener(this)
    }

    private fun getAllBankAccounts() {
        bankViewModel.getBankAccounts(!binding.srlBanks.isRefreshing)
    }

    override fun onRefresh() {
        getAllBankAccounts()
    }

    override fun onItemClicked(position: Int) {
        val obj = nonPlaidmList[position]
        val requestObj = CreateBankAccountRequestModel(obj.accountSrNo!!, obj.accountType, obj.accountTitle!!, obj.bankName!!, obj.routingNumber!!, obj.accountTypeSpecified, obj.accountNumber!!, obj.accountNickname!!, obj.comments!!)
        addFragment(R.id.mainContainer, AddBankAccountConfirmationFragment.newInstance(requestObj), "AddBankAccountConfirmationFragment")
    }


    override fun onTransfer(position: Int) {
        val obj = nonPlaidmList[position]
        val requestObj = CreateBankAccountRequestModel(obj.accountSrNo!!, obj.accountType, obj.accountTitle!!, obj.bankName!!, obj.routingNumber!!, obj.accountTypeSpecified, obj.accountNumber!!, obj.accountNickname!!, obj.comments!!)
        val reqObj = CustomEditScheduledTransferModel("", obj.accountSrNo!!, obj.accountNickname!!, 0.0, "", 0, "")

        addFragment(R.id.mainContainer, AchoutToBank.newInstance(obj), "achDirect")
    }




}