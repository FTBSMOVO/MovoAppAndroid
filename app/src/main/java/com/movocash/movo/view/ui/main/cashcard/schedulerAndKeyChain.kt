package com.movocash.movo.view.ui.main.cashcard

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentAboutUsBinding
import com.movocash.movo.databinding.FragmentKeyChainBinding
import com.movocash.movo.databinding.FragmentSchedulerAndKeyChainBinding
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [schedulerAndKeyChain.newInstance] factory method to
 * create an instance of this fragment.
 */
class schedulerAndKeyChain: BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentSchedulerAndKeyChainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scheduler_and_key_chain, container, false)
       // binding.tvTitle.isSelected = true



        setListeners()
        // activateReviewInfo()
        return binding.root
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.btnkeychain.setOnClickListener(this)
        binding.btnscheduler.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.btnscheduler-> openSideMenu(R.id.mainContainer, AutoReloads.newInstance(secondaryRefID),"SideMenuFragment")
            R.id.btnkeychain -> openSideMenu(R.id.mainContainer, keyChain.newInstance(secondaryRefID),"SideMenuFragment")
        }
    }


    companion object {
        lateinit var instance: schedulerAndKeyChain
        var secondaryRefID = ""

        fun newInstance(isMain: String): schedulerAndKeyChain {
            instance = schedulerAndKeyChain()
            secondaryRefID = isMain
            return instance
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()

                callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


}




