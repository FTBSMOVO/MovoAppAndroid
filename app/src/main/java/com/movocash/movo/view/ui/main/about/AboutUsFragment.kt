package com.movocash.movo.view.ui.main.about

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentAboutUsBinding
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.android.play.core.review.testing.FakeReviewManager
import com.movocash.movo.MovoApp
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment

class AboutUsFragment: BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentAboutUsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_us, container, false)
        binding.tvTitle.isSelected = true
        setListeners()
       // activateReviewInfo()
        return binding.root
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(),"SideMenuFragment")
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

  /*  private fun activateReviewInfo()
    {
        val manager = FakeReviewManager(MovoApp.context)


        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result

                val flow = manager.launchReviewFlow(ActivityBase.activity, reviewInfo)
                flow.addOnCompleteListener { task ->
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                    if (task.isSuccessful) {

                        Toast.makeText(MovoApp.context, "Review is completed", Toast.LENGTH_SHORT)
                                .show()


                        //accountViewModel.logoutUser()
                    }
                    else
                    {
                        //accountViewModel.logoutUser()
                    }
                    *//*Toast.makeText(MovoApp.context, "Review is completed", Toast.LENGTH_SHORT)
                            .show()*//*
                }

            } else {
                // There was some problem, log or handle the error code.
                //@ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
                // accountViewModel.logoutUser()
            }
        }



    }*/
}




