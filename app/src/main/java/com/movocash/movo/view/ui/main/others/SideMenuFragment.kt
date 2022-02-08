package com.movocash.movo.view.ui.main.others

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.Plaid.PlaidMain
import com.movocash.movo.Plaid.PlaidMainFragment
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.UploadProfilePicRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.data.remote.callback.ICallBackUri
import com.movocash.movo.databinding.FragmentSideMenuBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.utilities.helper.Permissions
import com.movocash.movo.view.ui.auth.AuthActivity
import com.movocash.movo.view.ui.auth.SignInFragment
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.MediaBottomSheetFragment
import com.movocash.movo.view.ui.main.about.AboutUsFragment
import com.movocash.movo.view.ui.main.activatecard.ActivateCardFragment
import com.movocash.movo.view.ui.main.cashcard.CashCardFragment
import com.movocash.movo.view.ui.main.deposithub.DepositHubFragment
import com.movocash.movo.view.ui.main.digitalbanking.DirectDepositFragment
import com.movocash.movo.view.ui.main.digitalbanking.cashoutbank.CashOutBankFragment
import com.movocash.movo.view.ui.main.digitalbanking.cashoutbank.cashintoMovo
import com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts.AddBankAccountFragment
import com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts.MyBankAccountFragment
import com.movocash.movo.view.ui.main.digitalbanking.scheduletransfer.ScheduleTransferFragment
import com.movocash.movo.view.ui.main.digitalbanking.transferactivity.TransferActivityFragment
import com.movocash.movo.view.ui.main.echeckbook.makepayment.MakePaymentFragment
import com.movocash.movo.view.ui.main.echeckbook.mypayee.AddPayeeFragment
import com.movocash.movo.view.ui.main.echeckbook.mypayee.MyPayeesFragment
import com.movocash.movo.view.ui.main.echeckbook.paymenthistory.PaymentHistoryFragment
import com.movocash.movo.view.ui.main.echeckbook.schedulepayments.SchedulePaymentsFragment
import com.movocash.movo.view.ui.main.lockunlock.LockUnLockCardFragment
import com.movocash.movo.view.ui.main.movocash.AccountSummaryFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.movopay.OrderPhysicalCard
import com.movocash.movo.view.ui.main.movopay.history.HistoryFragment
import com.movocash.movo.view.ui.main.movopay.sendmoney.SendMoneyFragment
import com.movocash.movo.view.ui.main.movopay.social.SocialMediaFragment
import com.movocash.movo.view.ui.main.password.ChangePasswordFragment
import com.movocash.movo.view.ui.main.profile.BioAuthFragment
import com.movocash.movo.view.ui.main.profile.PassCodeFragment
import com.movocash.movo.view.ui.main.profile.ProfileFragment
import com.movocash.movo.view.ui.main.profile.WebContentFragment
import com.movocash.movo.view.ui.main.profile.alerts.MovoAlertsFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException
import java.lang.reflect.Type
import com.movocash.movo.view.ui.auth.SignInFragment.UpdateButtonListener





class SideMenuFragment : BaseFragment(), View.OnClickListener, MediaBottomSheetFragment.ISelectListener,SignInFragment.UpdateButtonListener,SignInFragment.ISelectListener {

    lateinit var binding: FragmentSideMenuBinding
    private lateinit var accountViewModel: AccountViewModel
    private var isMovoSubOpen = false
    private var isDigitalSubOpen = false
    private var isECheckOpen = false
    private var isProfileClicked = false
    private var isMovoPayOpen = false
    private var itemList: ArrayList<String> = ArrayList()
    private val REQ_CODE_CAMERA = 1
    private val REQ_CODE_GALLERY = 2
    private var cameraImageUri: Uri? = null
    private lateinit var commonViewModel: CommonViewModel
    private lateinit var bottomSheetListFragment: MediaBottomSheetFragment
    private var transitionFlag = 0
    private lateinit var programID: String

    private var referenceId = ""
    private var primaryAmount = 0.0

    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var mList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var mList2: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    lateinit var primaryCardObj: GetCardAccountsResponseModel.Card

    var updateButton: UpdateButtonListener? = null

    companion object {
        lateinit var instance: SideMenuFragment
    }

    init {
        instance = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_side_menu, container, false)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUiObserver()
        setListeners()
        setView()
        checkAndSet()
        return binding.root
    }

    private fun checkAndSet() {
        //binding.isCardSet = false
        var cardModel: GetCardAccountsResponseModel? = null
        val listType: Type = object : TypeToken<GetCardAccountsResponseModel>() {}.type
        cardModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_CARD_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }

        if (cardModel?.obj != null && cardModel.obj!!.cards != null && cardModel.obj!!.cards!!.size > 0) {
            mList.clear()
            mList = cardModel.obj!!.cards!!
            mList1 = ArrayList(mList.filter { it.isPrimaryCardSpecified && it.statusCode!="F"})
            //setCardData(1)
            primaryCardObj = mList1[0]
            setPrimaryCardData(0)
        }
    }

    private fun setPrimaryCardData(position: Int) {
        val cardNumber = "*" + mList1[position].cardNumber!!.substring(mList1[position].cardNumber!!.length - 4)
        val num = "${mList1[position].programAbbreviation}${cardNumber}"
        /*

            binding.isCardSet = true*/

        referenceId = mList1[position].referenceID!!
        primaryAmount = mList1[position].balance

        // ActivityBase.activity.showToastMessage("statusCode: "+ primaryCardObj.statusCode)
        accountViewModel.getMyProfile(false)
        /*if(primaryCardObj.statusCode == "B")
        {

            accountViewModel.getMyProfile(false)
        }
        else{

            binding.rlActivateCard.visibility = View.GONE
        }*/


        /* binding.cardAmount = mList1[position].balance
         binding.cardNumber = num*/
    }

    private fun setUiObserver() {

        accountViewModel.getProfileResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.data != null) {

                    programID = obj.data!!.cardProgramID.toString()
                    transitionFlag = obj.data!!.transitionFlag!!.toInt()


                    if(transitionFlag == 1 && programID!= "MOC_DIGI" && primaryCardObj.statusCode == "B")
                    {

                        binding.rlActivateCard.visibility = View.VISIBLE
                    }
                    else{

                        binding.rlActivateCard.visibility = View.GONE
                    }

                }
            }
        })
        accountViewModel.logoutFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })

        accountViewModel.logoutSuccess.observe(viewLifecycleOwner, Observer {
            toTheAuth()
        })

        commonViewModel.urlResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.data != null && obj.data.size > 0 && !TextUtils.isEmpty(obj.data[0].url)) {
                    accountViewModel.uploadProfilePic(UploadProfilePicRequestModel(obj.data[0].url, obj.data[0].thumbUrl))
                    MovoApp.db.putString(Constants.USER_IMAGE, obj.data[0].url)
                    MovoApp.db.putString(Constants.USER_THUMB, obj.data[0].thumbUrl)
                }
            }
        })
        /*  accountViewModel.ReviewAccountSuccess.observe(viewLifecycleOwner, Observer {
              it?.let { obj ->
                  ActivityBase.activity.showToastMessage("working")

                  if (obj.data!!.isEligibleForReview!!)
                  {
                      MovoApp.db.putString(Constants.REVIEW_COUNT_DB, "1")
                      accountViewModel.UPDATER_REVIEW_COUNT()

                      ActivityBase.activity.showToastMessage(MovoApp.db.getString(Constants.REVIEW_COUNT_DB).toString())

                      if(MovoApp.db.getString(Constants.REVIEW_COUNT_DB) == "0")
                      {
                          //accountViewModel.UPDATER_REVIEW_COUNT()
                          MovoApp.db.putString(Constants.REVIEW_COUNT_DB, "1")
                          ActivityBase.activity.showToastMessage(MovoApp.db.getString(Constants.REVIEW_COUNT_DB).toString())
                          activateReviewInfo()

                      }
                      else
                      {

                          //accountViewModel.UPDATER_REVIEW_COUNT()
                          ActivityBase.activity.showToastMessage("else")

                          accountViewModel.logoutUser()

                      }
                  }
                  else
                  {
                      ActivityBase.activity.showToastMessage("else")

                      accountViewModel.logoutUser()
                  }
              }
          })

          accountViewModel.ReviewAccountFailure.observe(viewLifecycleOwner, Observer {
              it?.let { obj ->
                  ActivityBase.activity.showToastMessage(obj)
                  ActivityBase.activity.showToastMessage("Failure")

                  //accountViewModel.UPDATER_REVIEW_COUNT()

              }
          })

          accountViewModel.UPDATER_REVIEW_COUNT_Success.observe(viewLifecycleOwner, Observer {
              it?.let { obj ->
                  ActivityBase.activity.showToastMessage(obj)


              }
          })

          accountViewModel.UPDATER_REVIEW_COUNT_Failure.observe(viewLifecycleOwner, Observer {
              it?.let { obj ->
                  ActivityBase.activity.showToastMessage(obj)


              }
          })
  */
        commonViewModel.urlFailure.observe(viewLifecycleOwner, Observer { msg ->
            binding.isFail = true
            binding.isLoading = false
            ActivityBase.activity.showToastMessage(msg)
        })

        accountViewModel.picUploadSuccess.observe(viewLifecycleOwner, Observer { msg ->
            binding.isFail = false
            binding.isLoading = false
            ActivityBase.activity.showToastMessage("Profile picture updated")
        })



        accountViewModel.picUploadFailure.observe(viewLifecycleOwner, Observer { msg ->
            binding.isFail = true
            binding.isLoading = false
            ActivityBase.activity.showToastMessage(msg)
        })
    }

    private fun toTheAuth() {
        val lastUserName = MovoApp.db.getString(Constants.USER_NAME)
        val lastUserPass = MovoApp.db.getString(Constants.USER_PASS)
        val rememberUserPass = MovoApp.db.getString(Constants.REMEMBERED_USER_NAME)
        MovoApp.db.clear()
        MovoApp.db.putString(Constants.USER_NAME, lastUserName!!)
        MovoApp.db.putString(Constants.USER_PASS, lastUserPass!!)
        MovoApp.db.putString(Constants.REMEMBERED_USER_NAME, rememberUserPass!!)
        MovoApp.initThreatMatrix()
        val intent = Intent(ActivityBase.activity, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        ActivityBase.activity.finish()
    }

    private fun setView() {

        //MovoApp.db.getString(Constants.menu)!!
        if(MovoApp.db.getString(Constants.MOVO_CASH)!! == "false")
        {
            binding.rlMovoCash.visibility = View.GONE
           // ActivityBase.activity.showToastMessage("true case")
        }
        /*else{
            ActivityBase.activity.showToastMessage("else case: "+MovoApp.db.getString(Constants.MOVO_CASH)!!)
        }*/
        if(MovoApp.db.getString(Constants.MOVO_PAY)!! == "false")
        {
            binding.rlMovoPay.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.CASH_CARD)!! == "false")
        {
            binding.rlCashCard.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.MOVO_CHAIN)!! == "false")
        {
            binding.rlmovoChain.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.DIGITAL_BANKING)!! == "false")
        {
            binding.rlDigitalBank.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.ECHECK_BOOK)!! == "false")
        {
            binding.rlECheckBook.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.MY_PROFILE)!! == "false")
        {
            binding.rlProfileSett.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.LOCK_UNLOCK_CARD)!! == "false")
        {
            binding.rlLockUnlock.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.CHANGE_PASSWORD)!! == "false")
        {
            binding.rlChangePass.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.DEPOSIT_HUB)!! == "false")
        {
            binding.rlDepositHub.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.ABOUT_US)!! == "false")
        {
            binding.rlAbout.visibility = View.GONE
        }

        ///////////////DIGITAL BANKING ///////////////////
        if(MovoApp.db.getString(Constants.M_MY_BANK_ACCOUNTS)!! == "false")
        {
            binding.rlMyBank.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_CASH_OUT_TO_BANK)!! == "false")
        {
            binding.rlCashOut.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_CASH_IN_TO_MOVO)!! == "false")
        {
            binding.rlCashTnToMOVO.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_DIRECT_DEPOSIT)!! == "false")
        {
            binding.rlDirectDeposit.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_ACH_TRANSFERS)!! == "false")
        {
            binding.rlAch.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_SCHEDULED_TRANSFER)!! == "false")
        {
            binding.rlScheduleTrans.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_Transfer_ACTIVITY)!! == "false")
        {
            binding.rlTransferActivity.visibility = View.GONE
        }
        /////////////////////E-CheckBook/////////////////////

        if(MovoApp.db.getString(Constants.M_MAKEPAYMENT)!! == "false")
        {
            binding.rlMakePay.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_PAYMENT_HISTORY)!! == "false")
        {
            binding.rlPaymentHistory.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_ADD_PAYEES)!! == "false")
        {
            binding.rlAddPayees.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_MY_PAYEES)!! == "false")
        {
            binding.rlMyPayees.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_SCHEDULED_PAYMENT)!! == "false")
        {
            binding.rlSchedulePayment.visibility = View.GONE
        }

        /////////////////////MY PROFILE/SETTINGS////////////////

        if(MovoApp.db.getString(Constants.M_MANAGE_PROFILE)!! == "false")
        {
            binding.rlManage.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_MOVO_CASH_ALERTS)!! == "false")
        {
            binding.rlMovoAlert.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_BIOMETRIC_AUTHENTICATION)!! == "false")
        {
            binding.rlBioAuth.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_MOPRO_SUPPORT)!! == "false")
        {
            binding.rlMoSupport.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_TERMS_CONDITION)!! == "false")
        {
            binding.rlTerms.visibility = View.GONE
        }
        if(MovoApp.db.getString(Constants.M_PRIVACY_POLICY)!! == "false")
        {
            binding.rlPrivacy.visibility = View.GONE
        }
        //MOC_DIGI for production
        //MOVO_DIGITAL for DEV
        binding.isFail = false
        binding.isLoading = false
        binding.tvName.text = "${MovoApp.db.getString(Constants.FIRST_NAME)} ${MovoApp.db.getString(Constants.LAST_NAME)}"
        if (!TextUtils.isEmpty(MovoApp.db.getString(Constants.USER_THUMB))) {
            binding.isImageSet = true
            binding.ivUser.load(MovoApp.db.getString(Constants.USER_THUMB)!!)
        } else
            binding.isImageSet = false

        //accountViewModel.getMyProfile(false)

        /* if(transitionFlag == 1 && programID!= "MOC_DIGI")
         {

             binding.rlActivateCard.visibility = View.VISIBLE
         }
         else{

             binding.rlActivateCard.visibility = View.GONE
         }

         ActivityBase.activity.showToastMessage("TransitionFlag: "+transitionFlag)
         ActivityBase.activity.showToastMessage("programID: "+programID)*/
    }

    private fun activateReviewInfo()
    {
        val manager = ReviewManagerFactory.create(MovoApp.context)


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

                        /*Toast.makeText(MovoApp.context, "Review is completed", Toast.LENGTH_SHORT)
                                .show()*/


                        accountViewModel.logoutUser()
                    }
                    else
                    {
                        accountViewModel.logoutUser()
                    }
                    /*Toast.makeText(MovoApp.context, "Review is completed", Toast.LENGTH_SHORT)
                            .show()*/
                }

            } else {
                // There was some problem, log or handle the error code.
                //@ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
                accountViewModel.logoutUser()
            }
        }



    }

    private fun setListeners() {
        binding.rlActivateCard.setOnClickListener(this)
        binding.rlMain.setOnClickListener(this)
        binding.rlMovoCash.setOnClickListener(this)
        binding.rlMovoPay.setOnClickListener(this)
        binding.rlmovoChain.setOnClickListener(this)
        binding.rlAch.setOnClickListener(this)

        binding.rlDigitalBank.setOnClickListener(this)
        binding.rlECheckBook.setOnClickListener(this)
        binding.rlProfileSett.setOnClickListener(this)
        binding.rlAccounts.setOnClickListener(this)
        binding.rlAccountSummary.setOnClickListener(this)
        binding.rlDepositHub.setOnClickListener(this)
        binding.rlCashCard.setOnClickListener(this)
        binding.rlCashOut.setOnClickListener(this)
        binding.rlDirectDeposit.setOnClickListener(this)
        binding.rlMyBank.setOnClickListener(this)
        binding.rlScheduleTrans.setOnClickListener(this)
        binding.rlTransferActivity.setOnClickListener(this)
        binding.rlMakePay.setOnClickListener(this)
        binding.rlPaymentHistory.setOnClickListener(this)
        binding.rlAddPayees.setOnClickListener(this)
        binding.rlMyPayees.setOnClickListener(this)
        binding.rlSchedulePayment.setOnClickListener(this)
        binding.rlPassCode.setOnClickListener(this)
        binding.rlMovoAlert.setOnClickListener(this)
        binding.rlBioAuth.setOnClickListener(this)
        binding.rlMoSupport.setOnClickListener(this)
        binding.rlTerms.setOnClickListener(this)
        binding.rlPrivacy.setOnClickListener(this)
        binding.rlManage.setOnClickListener(this)
        binding.rlLockUnlock.setOnClickListener(this)
        binding.rlChangePass.setOnClickListener(this)
        binding.rlAbout.setOnClickListener(this)
        binding.llLogout.setOnClickListener(this)
        binding.rlSendMoney.setOnClickListener(this)
        binding.rlSocialMedia.setOnClickListener(this)
        binding.rlHistory.setOnClickListener(this)
        binding.rlUserPic.setOnClickListener(this)
        binding.rlPhysicalCard.setOnClickListener(this)
        binding.rlCashTnToMOVO.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlCashTnToMOVO -> {

                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, cashintoMovo(), null)
            }
            R.id.rlAch -> {

                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()

                addFragment(R.id.mainContainer, AddBankAccountFragment(), null)
            }
            R.id.rlUserPic -> {
                if (binding.isLoading!!) {
                    ActivityBase.activity.showToastMessage("Uploading...")
                } else if (binding.isFail!!) {
                    uploadProfilePic()
                } else
                //Permissions.isStoragePermissionGranted(ActivityBase.activity)
                    showImageDialog()
            }
            R.id.llLogout -> {
                //  accountViewModel.ReviewAccount()
                accountViewModel.logoutUser()
            }
            R.id.rlMain -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            }
            R.id.rlmovoChain -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, movoChain(), null)
                //addFragment(R.id.mainContainer, AutoReloads(), null)
            }

            R.id.rlMovoCash -> movoCashClicked()
            R.id.rlDigitalBank -> digitalBankClicked()
            R.id.rlECheckBook -> eCheckClicked()
            R.id.rlProfileSett -> profileSettingClicked()
            R.id.rlMovoPay -> movoPayClicked()
            R.id.rlAccounts -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, MyCardsFragment(), null)
            }
            R.id.rlAccountSummary -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, AccountSummaryFragment(), null)
            }
            R.id.rlPhysicalCard -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, OrderPhysicalCard(), null)
            }
            R.id.rlActivateCard -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, ActivateCardFragment(), null)
            }
            R.id.rlSendMoney -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, SendMoneyFragment(), null)
            }
            R.id.rlSocialMedia -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, SocialMediaFragment(), null)
            }
            R.id.rlHistory -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, HistoryFragment(), null)
            }
            R.id.rlDepositHub -> {
                /**Track event when user clicks on Deposit HUB*/
                val paramBundle = Bundle()
                paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                    requireContext(),
                    CustomEventNames.EVENT_CLICK_DEPOSIT_HUB,
                    paramBundle
                )


                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, DepositHubFragment(), null)
            }
            R.id.rlCashCard -> {
                /**track event when user clicks on Cash card*/
                val paramBundle = Bundle()
                paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                    requireContext(),
                    CustomEventNames.EVENT_CLICK_CASH_CARD,
                    paramBundle
                )

                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, CashCardFragment(), null)
            }
            R.id.rlCashOut -> {
//                *track event when user clicks on Cash out to bank
                val paramBundle = Bundle()
                paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                    requireContext(),
                    CustomEventNames.EVENT_CLICK_CASH_OUT_TO_BANK,
                    paramBundle
                )

                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, CashOutBankFragment.newInstance(null), null)
            }
            R.id.rlDirectDeposit -> {
                /**trigger event when user clicks on direct deposit option*/
                val paramBundle = Bundle()
                paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                    requireContext(),
                    CustomEventNames.EVENT_ACCESS_DIRECT_DEPOSIT,
                    paramBundle
                )

                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, DirectDepositFragment(), null)
            }
            R.id.rlMyBank -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, MyBankAccountFragment(), null)
            }
            R.id.rlScheduleTrans -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, ScheduleTransferFragment(), null)
            }
            R.id.rlTransferActivity -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, TransferActivityFragment(), null)
            }
            R.id.rlMakePay -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, MakePaymentFragment.newInstance("", true), null)
            }
            R.id.rlPaymentHistory -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, PaymentHistoryFragment(), null)
            }
            R.id.rlAddPayees -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, AddPayeeFragment(), null)
            }
            R.id.rlMyPayees -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, MyPayeesFragment(), null)
            }
            R.id.rlSchedulePayment -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, SchedulePaymentsFragment(), null)
            }
            R.id.rlPassCode -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, PassCodeFragment(), null)
            }
            R.id.rlMovoAlert -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, MovoAlertsFragment(), null)
            }
            R.id.rlBioAuth -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, BioAuthFragment(), null)
            }
            R.id.rlMoSupport -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, WebContentFragment.newInstance(Constants.WEB_SUPPORT), null)
            }
            R.id.rlTerms -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, WebContentFragment.newInstance(Constants.WEB_TERMS), null)
            }
            R.id.rlPrivacy -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, WebContentFragment.newInstance(Constants.WEB_PRIVACY), null)
            }
            R.id.rlManage -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, ProfileFragment(), null)
            }
            R.id.rlLockUnlock -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, LockUnLockCardFragment(), null)
            }
            R.id.rlChangePass -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, ChangePasswordFragment(), null)
            }
            R.id.rlAbout -> {
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                addFragment(R.id.mainContainer, AboutUsFragment(), null)
                /*val intent = Intent(context, PlaidMain::class.java)

                requireContext().startActivity(intent)*/
            }
        }
    }

    private fun uploadProfilePic() {
        binding.isFail = false
        binding.isLoading = true
        commonViewModel.uploadFullImage(cameraImageUri)
    }

    private fun showImageDialog() {
        itemList.clear()
        itemList.add("Camera")
        itemList.add("Photo Album")
        bottomSheetListFragment = MediaBottomSheetFragment(itemList, "Cancel")
        bottomSheetListFragment.setMyListener(this)
        if (!bottomSheetListFragment.isAdded) {
            bottomSheetListFragment.show(ActivityBase.activity.supportFragmentManager, "")
        }
    }

    private fun movoPayClicked() {
        /**track event when user clicked on movo pay side menu*/
        val paramBundle = Bundle()
        paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
        FirebaseAnalyticsEventHelper.trackAnalyticEvent(
            requireContext(),
            CustomEventNames.EVENT_CLICK_MOVO_PAY,
            paramBundle
        )

        isMovoSubOpen = false
        isDigitalSubOpen = false
        isECheckOpen = false
        isProfileClicked = false
        binding.ivDigitalDrop.rotation = 0f
        binding.ivProfileSettDrop.rotation = 0f
        binding.ivECheckBookDrop.rotation = 0f
        binding.ivMovoDrop.rotation = 0f

        binding.elMovo.collapse()
        binding.elDigitalBank.collapse()
        binding.elProfile.collapse()
        binding.elEBook.collapse()

        if (isMovoPayOpen) {
            isMovoPayOpen = false
            binding.elMovoPay.collapse()
            binding.ivMovoPayDrop.rotation = 0f
        } else {
            isMovoPayOpen = true
            binding.elMovoPay.expand()
            binding.ivMovoPayDrop.rotation = 180f
        }
    }

    private fun profileSettingClicked() {
        isMovoSubOpen = false
        isDigitalSubOpen = false
        isECheckOpen = false
        isMovoPayOpen = false
        binding.ivDigitalDrop.rotation = 0f
        binding.ivMovoDrop.rotation = 0f
        binding.ivECheckBookDrop.rotation = 0f
        binding.ivMovoPayDrop.rotation = 0f

        binding.elMovo.collapse()
        binding.elDigitalBank.collapse()
        binding.elMovoPay.collapse()
        binding.elEBook.collapse()

        if (isProfileClicked) {
            isProfileClicked = false
            binding.elProfile.collapse()
            binding.ivProfileSettDrop.rotation = 0f
        } else {
            isProfileClicked = true
            binding.elProfile.expand()
            binding.ivProfileSettDrop.rotation = 180f
        }
    }

    private fun eCheckClicked() {
        isMovoSubOpen = false
        isDigitalSubOpen = false
        isProfileClicked = false
        isMovoPayOpen = false
        binding.ivDigitalDrop.rotation = 0f
        binding.ivMovoDrop.rotation = 0f
        binding.ivProfileSettDrop.rotation = 0f
        binding.ivMovoPayDrop.rotation = 0f

        binding.elMovo.collapse()
        binding.elDigitalBank.collapse()
        binding.elMovoPay.collapse()
        binding.elProfile.collapse()

        if (isECheckOpen) {
            isECheckOpen = false
            binding.elEBook.collapse()
            binding.ivECheckBookDrop.rotation = 0f
        } else {
            isECheckOpen = true
            binding.elEBook.expand()
            binding.ivECheckBookDrop.rotation = 180f
        }
    }

    private fun digitalBankClicked() {
        /**track event when user clicks on digital banking*/
        val paramBundle = Bundle()
        paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
        FirebaseAnalyticsEventHelper.trackAnalyticEvent(
            requireContext(),
            CustomEventNames.EVENT_CLICK_DIGITAL_BANKING,
            paramBundle
        )

        isMovoSubOpen = false
        isECheckOpen = false
        isProfileClicked = false
        isMovoPayOpen = false
        binding.ivECheckBookDrop.rotation = 0f
        binding.ivMovoDrop.rotation = 0f
        binding.ivProfileSettDrop.rotation = 0f
        binding.ivMovoPayDrop.rotation = 0f

        binding.elMovo.collapse()
        binding.elEBook.collapse()
        binding.elMovoPay.collapse()
        binding.elProfile.collapse()

        if (isDigitalSubOpen) {
            isDigitalSubOpen = false
            binding.elDigitalBank.collapse()
            binding.ivDigitalDrop.rotation = 0f
        } else {
            isDigitalSubOpen = true
            binding.elDigitalBank.expand()
            binding.ivDigitalDrop.rotation = 180f
        }
    }

    private fun movoCashClicked() {
        isDigitalSubOpen = false
        isECheckOpen = false
        isProfileClicked = false
        isMovoPayOpen = false
        binding.ivECheckBookDrop.rotation = 0f
        binding.ivDigitalDrop.rotation = 0f
        binding.ivProfileSettDrop.rotation = 0f
        binding.ivMovoPayDrop.rotation = 0f

        binding.elDigitalBank.collapse()
        binding.elEBook.collapse()
        binding.elMovoPay.collapse()
        binding.elProfile.collapse()

        if (isMovoSubOpen) {
            isMovoSubOpen = false
            binding.elMovo.collapse()
            binding.ivMovoDrop.rotation = 0f
        } else {
            isMovoSubOpen = true
            binding.elMovo.expand()
            binding.ivMovoDrop.rotation = 180f
        }
    }

    override fun onMediaSelect(pos: Int) {
        when (itemList[pos]) {
            "Camera" -> {
                if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.CAMERA)) {
                    ActivityBase.activity.startCamera(REQ_CODE_CAMERA)
                }
            }
            "Photo Album" -> {
                // if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //ActivityBase.activity.showGallery(REQ_CODE_GALLERY)
                //}
                //showGallery2()
               // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)


               // Permissions.requestSinglePermission(ActivityBase.activity, Manifest.permission.READ_EXTERNAL_STORAGE)
              //  val MY_WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"
               // Permissions.requestSinglePermission(ActivityBase.activity, MY_WRITE_EXTERNAL_STORAGE)
               //ActivityBase.activity.showGallery(REQ_CODE_GALLERY)
              /*  if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                   // if (Permissions.checkSinglePermission(ActivityBase.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        ActivityBase.activity.showGallery(REQ_CODE_GALLERY)
                    //}
                }
                else
                {

                    ActivityBase.activity.showGallery(REQ_CODE_GALLERY)
                }*/
                ActivityBase.activity.showGallery(REQ_CODE_GALLERY)
            }
           // ActivityBase.activity.showGallery(REQ_CODE_GALLERY)
        }
    }






    override fun onMediaSelect(value: Boolean) {


        if(value){


            binding.rlMovoCash.visibility = View.VISIBLE
        }
        else
        {
            binding.rlMovoCash.visibility = View.GONE
        }

    }


    override fun onMediaCancel() {
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CODE_GALLERY ->
                    if (data != null) {
                        try {
                            ActivityBase.activity.processGalleryPhoto(data, object : ICallBackUri {
                                override fun imageUri(result: Uri?) {
                                    binding.isImageSet = true
                                    cameraImageUri = ActivityBase.activity.compressFile(result!!)
                                    binding.ivUser.setImageBitmap(ActivityBase.activity.handleSamplingAndRotationBitmap(cameraImageUri!!))
                                    uploadProfilePic()
                                }
                            })
                        } catch (e: IOException) {
                            e.printStackTrace()
                            ActivityBase.activity.showToastMessage("Failed")
                        }
                    }
                REQ_CODE_CAMERA ->
                    ActivityBase.activity.processCapturedPhoto(object : ICallBackUri {
                        override fun imageUri(result: Uri?) {
                            if(result!=null) {
                                // val imageBitmap = data?.extras?.get("data") as Bitmap
                                binding.isImageSet = true
                                cameraImageUri = ActivityBase.activity.compressFile( result!!)
                                binding.ivUser.setImageBitmap(ActivityBase.activity.handleSamplingAndRotationBitmap(cameraImageUri!!))
                                uploadProfilePic()
                            }
                            else
                            {
                                ActivityBase.activity.showToastMessage("uri empty")
                            }
                        }
                    })
            }
        }
    }

    override fun onUpdate(status: Boolean) {

        if(status){
            /*btnHabilitarMed.setEnabled(true);
            btnHabilitarImc.setEnabled(true);*/
        }
    }


}