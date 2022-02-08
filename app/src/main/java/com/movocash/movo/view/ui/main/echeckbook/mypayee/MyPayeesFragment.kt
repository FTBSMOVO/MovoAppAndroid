package com.movocash.movo.view.ui.main.echeckbook.mypayee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.AddEditPayeeRequestModel
import com.movocash.movo.data.model.responsemodel.GetMyPayeeResponseModel
import com.movocash.movo.databinding.FragmentMyPayeesBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.echeckbook.makepayment.MakePaymentFragment
import com.movocash.movo.view.ui.main.echeckbook.mypayee.adapter.AdapterMyPayees
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.ECheckBookViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment

class MyPayeesFragment : BaseFragment(), View.OnClickListener, AdapterMyPayees.IPayeeListener, SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentMyPayeesBinding
    private lateinit var viewModel: ECheckBookViewModel
    private var mList: ArrayList<GetMyPayeeResponseModel.Payee> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_payees, container, false)
        viewModel = ViewModelProvider(this).get(ECheckBookViewModel::class.java)
        setUiObserver()
        setListeners()
        setViews()
        getMyPayees()
        return binding.root
    }

    private fun getMyPayees() {
        viewModel.getMyPayeesList(!binding.srlPayee.isRefreshing)
    }

    private fun setUiObserver() {
        viewModel.myPayeeFailure.observe(viewLifecycleOwner, Observer {
            binding.srlPayee.isRefreshing = false
            ActivityBase.activity.showToastMessage(it)
        })

        viewModel.myPayeeResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                binding.srlPayee.isRefreshing = false
                if (obj?.data != null && obj.data!!.payees != null && obj.data!!.payees!!.size > 0) {
                    MovoApp.db.putString(Constants.CONST_MY_PAYEE_RESPONSE, Gson().toJson(obj))
                    binding.isData = true
                    mList.clear()
                    mList.addAll(obj.data!!.payees!!)
                    setAdapter()

                } else {
                    binding.isData = false
                }

            }
        })
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvPayee.layoutManager = manager
        val adapter = AdapterMyPayees(mList)
        binding.rvPayee.adapter = adapter
        adapter.setMyListener(this)
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.isData = true
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.srlPayee.setOnRefreshListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
        }
    }

    override fun onItemClicked(position: Int) {
        val myObj = mList[position]
        val obj = AddEditPayeeRequestModel(myObj.payeeSerialNo!!, myObj.payeeName!!, myObj.payeeAddress!!, myObj.payeeCity!!, 0, "", myObj.payeeZip!!, myObj.payeeNickname!!, myObj.payeeAccountNumber!!)
        addFragment(R.id.mainContainer, PayeeDetailFragment.newInstance(obj, myObj.payeeState!!, true), "PayeeDetailFragment")
    }

    override fun onPayClicked(position: Int) {
        addFragment(R.id.mainContainer, MakePaymentFragment.newInstance(mList[position].payeeSerialNo!!, false), "")
    }

    override fun onRefresh() {
        getMyPayees()
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