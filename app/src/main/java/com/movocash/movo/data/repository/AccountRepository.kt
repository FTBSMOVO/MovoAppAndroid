package com.movocash.movo.data.repository

import android.app.PendingIntent.getActivity
import android.text.TextUtils
import android.widget.Toast
import com.movocash.movo.MovoApp
import com.movocash.movo.data.model.requestmodel.*
import com.movocash.movo.data.model.responsemodel.*
import com.movocash.movo.data.remote.SingleEnqueueCall
import com.movocash.movo.data.remote.callback.IGenericCallBack
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.utilities.helper.SingleLiveData
import com.movocash.movo.view.ui.base.ActivityBase

class AccountRepository : IGenericCallBack {

    var signUpResponseModel: SingleLiveData<SignUpResponseModel> = SingleLiveData()
    var signUpFailure: SingleLiveData<String> = SingleLiveData()

    var UPDATER_REVIEW_COUNT_Success: SingleLiveData<String> = SingleLiveData()
    var UPDATER_REVIEW_COUNT_Failure: SingleLiveData<String> = SingleLiveData()

    var signUpCodeSuccess: SingleLiveData<SignupPersoanlinfoResponseModel> = SingleLiveData()
    var signUpCodeFailure: SingleLiveData<SignupPersoanlinfoResponseModel> = SingleLiveData()

    var ReviewAccountSucess: SingleLiveData<ReviewAccountResponseModel> = SingleLiveData()
    var ReviewAccountFailure: SingleLiveData<String> = SingleLiveData()

    var signupPersonalInfo: SingleLiveData<SignupPersoanlinfoResponseModel> = SingleLiveData()
    var signupPersonalInfoFailure: SingleLiveData<SignupPersoanlinfoResponseModel> = SingleLiveData()

    var loginResponseModel: SingleLiveData<LoginResponseModel> = SingleLiveData()
    var loginFailure: SingleLiveData<String> = SingleLiveData()
    var mediaSent: SingleLiveData<SignupPersoanlinfoResponseModel> = SingleLiveData()
    var mediaFailure: SingleLiveData<SignupPersoanlinfoResponseModel> = SingleLiveData()
    var verificationResponseModel: SingleLiveData<SendVerificationResponseModel> = SingleLiveData()
    var verificationFailure: SingleLiveData<String> = SingleLiveData()
    var verificationSuccess: SingleLiveData<Boolean> = SingleLiveData()
    var verifyFailure: SingleLiveData<String> = SingleLiveData()
    var userNameValid: SingleLiveData<Boolean> = SingleLiveData()
    var userNameInValid: SingleLiveData<String> = SingleLiveData()
    var emailValid: SingleLiveData<Boolean> = SingleLiveData()
    var emailInValid: SingleLiveData<String> = SingleLiveData()
    var logoutSuccess: SingleLiveData<Boolean> = SingleLiveData()
    var logoutFailure: SingleLiveData<String> = SingleLiveData()
    var passwordChangeSuccess: SingleLiveData<Boolean> = SingleLiveData()
    var passwordChangeFailure: SingleLiveData<String> = SingleLiveData()
    var updateProfileResponseModel: SingleLiveData<UpdateProfileResponseModel> = SingleLiveData()
    var updateProfileFailure: SingleLiveData<String> = SingleLiveData()
    var getProfileResponseModel: SingleLiveData<GetMyProfileResponseModel> = SingleLiveData()
    var getProfileFailure: SingleLiveData<String> = SingleLiveData()
    var codeSent: SingleLiveData<Boolean> = SingleLiveData()
    var codeSentFailure: SingleLiveData<String> = SingleLiveData()
    var passwordResetSuccess: SingleLiveData<Boolean> = SingleLiveData()
    var passwordResetFailure: SingleLiveData<String> = SingleLiveData()
    var getUserAlertsResponseModel: SingleLiveData<GetUserAlertsResponseModel> = SingleLiveData()
    var getUserAlertsFailure: SingleLiveData<String> = SingleLiveData()
    var alertSetSuccess: SingleLiveData<Boolean> = SingleLiveData()
    var alertSetFailure: SingleLiveData<String> = SingleLiveData()
    var picUploadSuccess: SingleLiveData<Boolean> = SingleLiveData()
    var picUploadFailure: SingleLiveData<String> = SingleLiveData()
    var profileCodeSent: SingleLiveData<Boolean> = SingleLiveData()
    var bankCodeSent: SingleLiveData<Boolean> = SingleLiveData()
    var profileCodeFailure: SingleLiveData<String> = SingleLiveData()
    var bankCodeFailure: SingleLiveData<String> = SingleLiveData()
    var verifyProfileCodeSuccess: SingleLiveData<Boolean> = SingleLiveData()
    var verifyProfileCodeFailure: SingleLiveData<String> = SingleLiveData()

    var verifybankCodeSuccess: SingleLiveData<String> = SingleLiveData()
    var verifyBankCodeFailure: SingleLiveData<String> = SingleLiveData()

    var VerifySignupEmailCodeSuccess: SingleLiveData<String> = SingleLiveData()
    var VerifySignupEmailCodeFailure: SingleLiveData<String> = SingleLiveData()

    var reissueCardMessage: SingleLiveData<String> = SingleLiveData()

    /*fun signUp(obj: SignUpRequestModel) {
        val call = MovoApp.apiService.signUpUser(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.SIGN_UP_URL, true, this)
    }*/

    fun signUp2(obj: SignUpRequestModel2) {
        val call = MovoApp.apiService.signUpUser2(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.SIGN_UP_URL2, true, this)
    }

    fun loginUser(obj: LoginRequestModel) {
        val call = MovoApp.apiService.loginUser(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.LOGIN_URL, true, this)
    }

    fun sendMediaFiles(obj: MediaRequestModel) {
        val call = MovoApp.apiService2.sendMediaFiles(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.SEND_MEDIA_URL, true, this)
    }

    fun reIssueCard(obj: ReissueCardModelApi) {
        val call = MovoApp.apiService.reIssueCard(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.REISSUE_CARD, true, this)
    }

    fun UPDATER_REVIEW_COUNT() {
        val call = MovoApp.apiService.UPDATER_REVIEW_COUNT()
        SingleEnqueueCall.callRetrofit(call, Constants.UPDATER_REVIEW_COUNT, true, this)
    }

    fun ReviewAccount() {
        val call = MovoApp.apiService.ReviewAccount()
        SingleEnqueueCall.callRetrofit(call, Constants.REVIEW_ACCOUNT, true, this)
    }

    fun sendVerificationCode(isLoader: Boolean) {
        //val call = MovoApp.apiService2.sendVerificationCode(MovoApp.db.getString(Constants.SESSION_ID)!!)
        val call = MovoApp.apiService2.sendVerificationCode(MovoApp.db.getString(Constants.MY_USER_ID)!!)
        SingleEnqueueCall.callRetrofit(call, Constants.SEND_CODE_URL, isLoader, this)
    }

    fun verifyVerificationCode(obj: VerifyCodeRequestModel) {
        val call = MovoApp.apiService2.verifyVerificationCode(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.VERIFY_CODE_URL, true, this)
    }

    fun verifySignupVerificationCode(obj: VerifySignupCodeRequestModel) {
        val call = MovoApp.apiService2.verifySignupVerificationCode(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.VERIFY_Signup_CODE, true, this)
    }

    fun verifyBankVerificationCode(obj: VerifyBankCodeRequestModel) {
        val call = MovoApp.apiService.verifyBankVerificationCode(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.VERIFY_BANK_CODE_URL, true, this)
    }

    fun verifySignupEmailCode(obj: VerifySignupEmailCodeRequestModel) {
        val call = MovoApp.apiService.verifySignupEmailCode(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.VERIFY_SIGNUP_EMAIL_CODE, true, this)
    }

    fun signupPersonalInfo(obj: SignUpPersonalInfoRequestModel) {
        val call = MovoApp.apiService.signupPersonalinfo(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.SIGNUP_PERSONAL_INFO, true, this)
    }

    fun checkUserName(userName: String) {
        val call = MovoApp.apiService.checkUserName(userName)
        SingleEnqueueCall.callRetrofit(call, Constants.CHECK_USERNAME_URL, false, this)
    }

    fun checkEmail(email: String) {
        val call = MovoApp.apiService.checkEmail(email)
        SingleEnqueueCall.callRetrofit(call, Constants.CHECK_EMAIL_URL, false, this)
    }

    fun changePassword(oldPass: String, newPass: String) {
        val call = MovoApp.apiService.changePassword(ChangePasswordRequestModel(oldPass, newPass))
        SingleEnqueueCall.callRetrofit(call, Constants.CHANGE_PASS_URL, false, this)
    }

    fun logoutUser() {
        if (!TextUtils.isEmpty(MovoApp.db.getString(Constants.ACCESS_TOKEN))) {
            val call = MovoApp.apiService.logoutUser(true)
            SingleEnqueueCall.callRetrofit(call, Constants.LOGOUT_URL, true, this)
        }
    }

    fun updateProfile(obj: UpdateProfileRequestModel) {
        val call = MovoApp.apiService.updateProfile(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.UPDATE_PROFILE_URL, true, this)
    }

    fun getMyProfile(isLoader: Boolean) {
        val call = MovoApp.apiService.getUserProfile()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_USER_PROFILE_URL, isLoader, this)
    }

    fun sendForgotVerificationCode(obj: SendVerificationCodeRequestModel) {
        val call = MovoApp.apiService.sendForgotVerificationCode(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.SEND_VERIFICATION_CODE_URL, true, this)
    }

    fun forgotPassword(obj: ForgotPasswordRequestModel) {
        val call = MovoApp.apiService.forgotPassword(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.FORGOT_PASSWORD_URL, true, this)
    }

    fun getUserAlerts() {
        val call = MovoApp.apiService.getUserAlerts()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_USER_ALERTS_URL, true, this)
    }

    fun setAlert(obj: AddUpdateUserAlerts) {
        val call = MovoApp.apiService.setUserAlerts(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.SET_USER_ALERTS_URL, true, this)
    }

    fun uploadProfilePic(obj: UploadProfilePicRequestModel) {
        val call = MovoApp.apiService.uploadProfilePic(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.UPLOAD_PIC_URL, false, this)
    }

    fun sendProfileCode() {
        val call = MovoApp.apiService.sendProfileCode()
        SingleEnqueueCall.callRetrofit(call, Constants.SEND_PROFILE_CODE, true, this)
    }

    fun sendBankOtp() {
        val call = MovoApp.apiService.sendBankOtp()
        SingleEnqueueCall.callRetrofit(call, Constants.SEND_BANK_OTP, true, this)
    }

    fun verifyProfileCode(code: String) {
        val call = MovoApp.apiService.verifyProfileCode(VerifyProfileCodeRequestModel(code))
        SingleEnqueueCall.callRetrofit(call, Constants.VERIFY_PROFILE_CODE, true, this)
    }

    override fun success(apiName: String, response: Any?) {
        when (apiName) {
            Constants.REISSUE_CARD -> {
                val responseModel = response as GeneralResponseModel
                reissueCardMessage.postValue(responseModel.messages)
            }
            Constants.VERIFY_BANK_CODE_URL -> {
                val responseModel = response as GeneralResponseModel
                verifybankCodeSuccess.postValue(responseModel.messages)
            }
            Constants.VERIFY_SIGNUP_EMAIL_CODE -> {
                val responseModel = response as GeneralResponseModel
                VerifySignupEmailCodeSuccess.postValue(responseModel.messages)
            }
            Constants.SIGN_UP_URL2 -> {
                val responseModel = response as SignUpResponseModel
                if (response.isError) {
                    signUpFailure.postValue(responseModel.messages)
                } else
                    signUpResponseModel.postValue(responseModel)
            }
            Constants.REVIEW_ACCOUNT -> {
                val responseModel = response as ReviewAccountResponseModel
                if (response.isError) {
                    ReviewAccountFailure.postValue(responseModel.messages)
                    ActivityBase.activity.showToastMessage("error")
                } else
                    ReviewAccountSucess.postValue(responseModel)
               // ActivityBase.activity.showToastMessage("working")
            }
            Constants.UPDATER_REVIEW_COUNT -> {
                val responseModel = response as GeneralResponseModel
                if (response.isError) {
                    UPDATER_REVIEW_COUNT_Failure.postValue(responseModel.messages)
                    ActivityBase.activity.showToastMessage("error")
                } else
                    UPDATER_REVIEW_COUNT_Success.postValue(responseModel.messages)
                ActivityBase.activity.showToastMessage("working")
            }
            Constants.SIGNUP_PERSONAL_INFO -> {
                val responseModel = response as SignupPersoanlinfoResponseModel
                if (response.isError) {
                    ActivityBase.activity.showToastMessage(responseModel.messages!!)
                    signupPersonalInfoFailure.postValue(responseModel)
                } else
                    signupPersonalInfo.postValue(responseModel)
            }
            Constants.VERIFY_Signup_CODE -> {
                val responseModel = response as SignupPersoanlinfoResponseModel
                if (response.isError) {
                    signUpCodeFailure.postValue(responseModel)
                } else
                    signUpCodeSuccess.postValue(responseModel)
            }
            Constants.LOGIN_URL -> {
                val responseModel = response as LoginResponseModel
                if (response.isError) {
                    loginFailure.postValue(response.messages!!)
                } else {
                    MovoApp.db.putString(Constants.CONST_CARD_RESPONSE, "")
                    MovoApp.db.putString(Constants.CONST_LOGIN_RESPONSE, "")
                    MovoApp.db.putString(Constants.CONST_MY_BANKS_RESPONSE, "")
                    MovoApp.db.putString(Constants.CONST_MY_PAYEE_RESPONSE, "")
                    MovoApp.db.putString(Constants.ACCESS_TOKEN, responseModel.data!!.token)
                    getMyProfile(false)
                    loginResponseModel.postValue(responseModel)
                }
            }
            Constants.SEND_MEDIA_URL -> {
                val responseModel = response as SignupPersoanlinfoResponseModel
                if (response.isError) {
                    mediaFailure.postValue(response)
                } else
                    mediaSent.postValue(responseModel)
            }
            Constants.SEND_CODE_URL -> {
                val responseModel = response as SendVerificationResponseModel
                if (response.isError) {
                    verificationFailure.postValue(response.messages!!)
                } else
                    verificationResponseModel.postValue(responseModel)
            }
            Constants.VERIFY_CODE_URL -> {
                val responseModel = response as GeneralResponseModel
                if (response.isError) {
                    verifyFailure.postValue(response.messages!!)
                } else
                    verificationSuccess.postValue(true)
            }
            Constants.VERIFY_SIGNUP_EMAIL_CODE -> {
                val responseModel = response as GeneralResponseModel
                if (response.isError) {
                    verifyFailure.postValue(response.messages!!)
                } else
                    verificationSuccess.postValue(true)
            }
            Constants.CHECK_USERNAME_URL -> {
                val responseModel = response as UserNameResponseModel
                if (response.data) {
                    userNameValid.postValue(false)
                } else
                    userNameValid.postValue(true)
            }
            Constants.CHECK_EMAIL_URL -> {
                val responseModel = response as UserNameResponseModel
                if (response.data) {
                    emailValid.postValue(false)
                } else
                    emailValid.postValue(true)
            }
            Constants.LOGOUT_URL -> {
                val responseModel = response as GeneralResponseModel
                if (response.isError) {
                    logoutFailure.postValue(response.messages)
                } else
                    logoutSuccess.postValue(true)
            }
            Constants.CHANGE_PASS_URL -> {
                val responseModel = response as GeneralResponseModel
                if (response.isError) {
                    passwordChangeFailure.postValue(response.messages)
                } else
                    passwordChangeSuccess.postValue(true)
            }
            Constants.UPDATE_PROFILE_URL -> {
                val responseModel = response as UpdateProfileResponseModel
                if (response.isError) {
                    updateProfileFailure.postValue(response.messages)
                } else
                    updateProfileResponseModel.postValue(responseModel)
            }
            Constants.GET_USER_PROFILE_URL -> {
                val responseModel = response as GetMyProfileResponseModel
                if (response.isError) {
                    getProfileFailure.postValue(response.messages)
                } else {
                    if (responseModel.data != null) {
                        if (!TextUtils.isEmpty(responseModel.data!!.billingAddress1)) {
                            val shipAddress =
                                    "${responseModel.data!!.billingAddress1}, ${responseModel.data!!.city} , ${responseModel.data!!.billingStateCode},${responseModel.data!!.billingPostalCode} , USA"
                            MovoApp.db.putString(Constants.CONST_MY_SHIPPING_INFO, shipAddress)
                        } else
                            MovoApp.db.putString(Constants.CONST_MY_SHIPPING_INFO, "")
                    }

                    getProfileResponseModel.postValue(responseModel)
                }
            }
            Constants.SEND_VERIFICATION_CODE_URL -> {
                val responseModel = response as GeneralResponseModel
                if (response.isError) {
                    codeSentFailure.postValue(response.messages)
                } else
                    codeSent.postValue(true)
            }
            Constants.FORGOT_PASSWORD_URL -> {
                val responseModel = response as GeneralResponseModel
                if (response.isError) {
                    passwordResetFailure.postValue(response.messages)
                } else
                    passwordResetSuccess.postValue(true)
            }
            Constants.SET_USER_ALERTS_URL -> {
                val responseModel = response as GeneralResponseModel
                if (response.isError) {
                    alertSetFailure.postValue(response.messages)
                } else
                    alertSetSuccess.postValue(true)
            }
            Constants.UPLOAD_PIC_URL -> {
                val responseModel = response as GeneralResponseModel
                if (response.isError) {
                    picUploadFailure.postValue(response.messages)
                } else
                    picUploadSuccess.postValue(true)
            }
            Constants.GET_USER_ALERTS_URL -> {
                val responseModel = response as GetUserAlertsResponseModel
                if (response.isError) {
                    getUserAlertsFailure.postValue(response.messages)
                } else
                    getUserAlertsResponseModel.postValue(responseModel)
            }
            Constants.SEND_PROFILE_CODE -> {
                val responseModel = response as GeneralResponseModel
                if (responseModel.isError) {
                    profileCodeFailure.postValue(response.messages)
                } else
                    profileCodeSent.postValue(true)
            }
            Constants.SEND_BANK_OTP -> {
                val responseModel = response as GeneralResponseModel
                if (responseModel.isError) {
                    bankCodeFailure.postValue(response.messages)
                } else
                    bankCodeSent.postValue(true)
            }
            Constants.VERIFY_PROFILE_CODE -> {
                val responseModel = response as GeneralResponseModel
                if (responseModel.isError) {
                    verifyProfileCodeFailure.postValue(response.messages)
                } else
                    verifyProfileCodeSuccess.postValue(true)
            }


        }
    }


    override fun failure(apiName: String, message: String?, response: Any?) {
        when (apiName) {
            /*Constants.VERIFY_Signup_CODE -> {
                signUpCodeFailure.postValue(message!!)
            }*/
            /*Constants.SIGNUP_PERSONAL_INFO -> {
                 // val responseModel = response as SignupPersoanlinfoResponseModel
                  //signupPersonalInfoFailure.postValue(message!!)
                ActivityBase.activity.showToastMessage(message.toString())

            }*/
            Constants.SIGNUP_PERSONAL_INFO -> {

               val responseModel = response as SignupPersoanlinfoResponseModel
               if (response.isError) {

                   ActivityBase.activity.showToastMessage(responseModel.messages!!)

               }
               signupPersonalInfoFailure.postValue(responseModel)
           }
            Constants.REISSUE_CARD -> {
                reissueCardMessage.postValue(message!!)
            }
            Constants.REVIEW_ACCOUNT -> {
                ReviewAccountFailure.postValue(message!!)
                ActivityBase.activity.showToastMessage("Failure")
            }
            Constants.UPDATER_REVIEW_COUNT -> {
                UPDATER_REVIEW_COUNT_Failure.postValue(message!!)
                ActivityBase.activity.showToastMessage("Failure")
            }
            Constants.VERIFY_BANK_CODE_URL -> {
                //reissueCardMessage.postValue(message!!)
                verifyBankCodeFailure.postValue(message!!)
            }
            Constants.VERIFY_SIGNUP_EMAIL_CODE -> {
                //reissueCardMessage.postValue(message!!)
                VerifySignupEmailCodeFailure.postValue(message!!)
            }
            Constants.SIGN_UP_URL2 -> {
                signUpFailure.postValue(message!!)
            }

            Constants.LOGIN_URL -> {
                loginFailure.postValue(message!!)
            }
            /*Constants.SEND_MEDIA_URL -> {
                val responseModel = response as SignupPersoanlinfoResponseModel
                mediaFailure.postValue(responseModel)
            }*/
            Constants.SEND_CODE_URL -> {
                verificationFailure.postValue(message!!)
            }
            Constants.VERIFY_CODE_URL -> {
                verifyFailure.postValue(message!!)
            }
            Constants.CHECK_USERNAME_URL -> {
                userNameInValid.postValue(message!!)
            }
            Constants.CHECK_EMAIL_URL -> {
                emailInValid.postValue(message!!)
            }
            Constants.LOGOUT_URL -> {
                logoutFailure.postValue(message!!)
            }
            Constants.CHANGE_PASS_URL -> {
                passwordChangeFailure.postValue(message!!)
            }
            Constants.UPDATE_PROFILE_URL -> {
                updateProfileFailure.postValue(message!!)
            }
            Constants.GET_USER_PROFILE_URL -> {
                getProfileFailure.postValue(message!!)
            }
            Constants.SEND_VERIFICATION_CODE_URL -> {
                codeSentFailure.postValue(message!!)
            }
            Constants.FORGOT_PASSWORD_URL -> {
                passwordResetFailure.postValue(message!!)
            }
            Constants.SET_USER_ALERTS_URL -> {
                alertSetFailure.postValue(message!!)
            }
            Constants.GET_USER_ALERTS_URL -> {
                getUserAlertsFailure.postValue(message!!)
            }
            Constants.UPLOAD_PIC_URL -> {
                picUploadFailure.postValue(message!!)
            }
            Constants.SEND_PROFILE_CODE -> {
                profileCodeFailure.postValue(message!!)
            }
            Constants.VERIFY_PROFILE_CODE -> {
                verifyProfileCodeFailure.postValue(message!!)
            }
            /*Constants.SIGNUP_PERSONAL_INFO -> {

                val responseModel = response as SignupPersoanlinfoResponseModel
                if (response.isError) {

                    ActivityBase.activity.showToastMessage(responseModel.messages!!)

                }
                signupPersonalInfoFailure.postValue(responseModel)
            }*/
            Constants.VERIFY_Signup_CODE -> {
                val responseModel = response as SignupPersoanlinfoResponseModel
                signUpCodeFailure.postValue(responseModel)
            }
            Constants.SEND_MEDIA_URL -> {
                val responseModel = response as SignupPersoanlinfoResponseModel
                mediaFailure.postValue(responseModel)
            }

        }
    }

/*    override fun failure(apiName: String, message: String?, response: Any?) {
        when (apiName) {
            Constants.SIGNUP_PERSONAL_INFO -> {

                val responseModel = response as SignupPersoanlinfoResponseModel
                if (response.isError) {

                    ActivityBase.activity.showToastMessage(responseModel.messages!!)

                }
                signupPersonalInfoFailure.postValue(responseModel)
            }
            Constants.VERIFY_Signup_CODE -> {
                val responseModel = response as SignupPersoanlinfoResponseModel
                signUpCodeFailure.postValue(responseModel)
            }
            Constants.SEND_MEDIA_URL -> {
                val responseModel = response as SignupPersoanlinfoResponseModel
                mediaFailure.postValue(responseModel)
            }
        }
    }*/




}