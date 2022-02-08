package com.movocash.movo.view.ui.main.echeckbook.mypayee

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.SearchPayeeRequestModel
import com.movocash.movo.data.model.responsemodel.GetMyPayeeResponseModel
import com.movocash.movo.databinding.FragmentAddPayeeBinding
import com.movocash.movo.utilities.extensions.hideKeyboard
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.echeckbook.mypayee.adapter.AdapterSearchPayee
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.ECheckBookViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment

class AddPayeeFragment : BaseFragment(), View.OnClickListener, AdapterSearchPayee.ISearchPayeeListener, View.OnKeyListener {

    lateinit var binding: FragmentAddPayeeBinding
    private lateinit var eBookViewModel: ECheckBookViewModel
    private var mList: ArrayList<GetMyPayeeResponseModel.Payee> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_payee, container, false)
        eBookViewModel = ViewModelProvider(this).get(ECheckBookViewModel::class.java)
        setUiObserver()
        setViews()
        setListeners()
        return binding.root
    }

    private fun setUiObserver() {
        eBookViewModel.searchPayeeFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })

        eBookViewModel.searchPayeeResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj?.data != null && obj.data!!.payees != null && obj.data!!.payees!!.size > 0) {
                    binding.isData = true
                    mList.clear()
                    mList.addAll(obj.data!!.payees!!)
                    setAdapter()
                } else {
                    binding.isData = false
                    mList.clear()
                    setAdapter()
                }

            }
        })
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvSearch.layoutManager = manager
        val adapter = AdapterSearchPayee(mList)
        binding.rvSearch.adapter = adapter
        adapter.setMyListener(this)
    }

    private fun setViews() {
        binding.etSearch.setText("")
        binding.tvTitle.isSelected = true
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.rlDoSearch.setOnClickListener(this)
        binding.etSearch.setOnKeyListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.rlDoSearch -> performSearch()
            R.id.rlRight -> callFragmentWithReplace(R.id.mainContainer, AddCustomPayeeFragment.newInstance(null, null, "", ""), "AddCustomPayeeFragment")
        }
    }

    private fun performSearch() {
        if (!TextUtils.isEmpty(binding.etSearch.text.toString())) {
            binding.root.hideKeyboard()
            eBookViewModel.getSearchedPayeesList(SearchPayeeRequestModel(binding.etSearch.text.toString(), 0, 500))
        }
    }

    override fun onItemClicked(position: Int) {
        callFragmentWithReplace(R.id.mainContainer, AddCustomPayeeFragment.newInstance(null, null, mList[position].payeeSerialNo!!, mList[position].payeeName!!), "AddCustomPayeeFragment")
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                    performSearch()
                    return true
                }
                else -> {
                }
            }
        }
        return false
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