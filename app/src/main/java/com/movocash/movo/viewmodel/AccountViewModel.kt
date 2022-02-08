package com.movocash.movo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.movocash.movo.data.model.requestmodel.*
import com.movocash.movo.data.model.responsemodel.*
import com.movocash.movo.data.repository.AccountRepository
import com.movocash.movo.utilities.helper.SingleLiveData

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    var repository = AccountRepository()
    var signUpResponseModel: SingleLiveData<SignUpResponseModel> = SingleLiveData()
    var signUpFailure: SingleLiveData<String> = SingleLiveData()

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
    var profileCodeFailure: SingleLiveData<String> = SingleLiveData()
    var bankCodeSent: SingleLiveData<Boolean> = SingleLiveData()
    var bankCodeFailure: SingleLiveData<String> = SingleLiveData()

    var verifyProfileCodeSuccess: SingleLiveData<Boolean> = SingleLiveData()
    var verifyProfileCodeFailure: SingleLiveData<String> = SingleLiveData()

    var verifyBankCodeSuccess: SingleLiveData<String> = SingleLiveData()
    var verifyBankCodeFailure: SingleLiveData<String> = SingleLiveData()

    var verifySignupEmailCodeSuccess: SingleLiveData<String> = SingleLiveData()
    var verifySignupEmailCodeFailure: SingleLiveData<String> = SingleLiveData()


    var signUpCodeSuccess: SingleLiveData<SignupPersoanlinfoResponseModel> = SingleLiveData()
    var signUpCodeFailure: SingleLiveData<SignupPersoanlinfoResponseModel> = SingleLiveData()

    var reissueCardMessage: SingleLiveData<String> = SingleLiveData()

    var ReviewAccountSuccess: SingleLiveData<ReviewAccountResponseModel> = SingleLiveData()
    var ReviewAccountFailure: SingleLiveData<String> = SingleLiveData()

    var UPDATER_REVIEW_COUNT_Success: SingleLiveData<String> = SingleLiveData()
    var UPDATER_REVIEW_COUNT_Failure: SingleLiveData<String> = SingleLiveData()


    init {
        reissueCardMessage = repository.reissueCardMessage

        ReviewAccountSuccess = repository.ReviewAccountSucess
        ReviewAccountFailure = repository.ReviewAccountFailure
        signUpResponseModel = repository.signUpResponseModel
        signUpFailure = repository.signUpFailure

        signupPersonalInfo = repository.signupPersonalInfo
        signupPersonalInfoFailure = repository.signupPersonalInfoFailure

        loginResponseModel = repository.loginResponseModel
        loginFailure = repository.loginFailure
        mediaSent = repository.mediaSent
        mediaFailure = repository.mediaFailure
        verificationResponseModel = repository.verificationResponseModel
        verificationFailure = repository.verificationFailure
        verificationSuccess = repository.verificationSuccess
        verifyFailure = repository.verifyFailure
        userNameValid = repository.userNameValid
        userNameInValid = repository.userNameInValid
        emailInValid = repository.emailInValid
        emailValid = repository.emailValid
        logoutSuccess = repository.logoutSuccess
        logoutFailure = repository.logoutFailure
        passwordChangeSuccess = repository.passwordChangeSuccess
        passwordChangeFailure = repository.passwordChangeFailure
        updateProfileResponseModel = repository.updateProfileResponseModel
        updateProfileFailure = repository.updateProfileFailure
        getProfileResponseModel = repository.getProfileResponseModel
        getProfileFailure = repository.getProfileFailure
        codeSent = repository.codeSent
        codeSentFailure = repository.codeSentFailure
        passwordResetSuccess = repository.passwordResetSuccess
        passwordResetFailure = repository.passwordResetFailure
        getUserAlertsResponseModel = repository.getUserAlertsResponseModel
        getUserAlertsFailure = repository.getUserAlertsFailure
        alertSetSuccess = repository.alertSetSuccess
        alertSetFailure = repository.alertSetFailure
        picUploadSuccess = repository.picUploadSuccess
        picUploadFailure = repository.picUploadFailure
        profileCodeSent = repository.profileCodeSent
        profileCodeFailure = repository.profileCodeFailure
        bankCodeSent = repository.bankCodeSent
        bankCodeFailure = repository.bankCodeFailure
        verifyProfileCodeSuccess = repository.verifyProfileCodeSuccess
        verifyProfileCodeFailure = repository.verifyProfileCodeFailure

        verifyBankCodeSuccess = repository.verifybankCodeSuccess
        verifyBankCodeFailure = repository.verifyBankCodeFailure

        verifySignupEmailCodeSuccess = repository.VerifySignupEmailCodeSuccess
        verifySignupEmailCodeFailure = repository.VerifySignupEmailCodeFailure


        signUpCodeSuccess = repository.signUpCodeSuccess
        signUpCodeFailure= repository.signUpCodeFailure

        ReviewAccountSuccess = repository.ReviewAccountSucess
        ReviewAccountFailure = repository.ReviewAccountFailure

        UPDATER_REVIEW_COUNT_Success = repository.UPDATER_REVIEW_COUNT_Success
        UPDATER_REVIEW_COUNT_Failure = repository.UPDATER_REVIEW_COUNT_Failure
    }

    /*fun signUp(obj: SignUpRequestModel) {
        repository.signUp(obj)
    }*/
    fun signUp2(obj: SignUpRequestModel2) {
        repository.signUp2(obj)
    }

    fun loginUser(obj: LoginRequestModel) {
        repository.loginUser(obj)
    }

    fun sendMediaFiles(obj: MediaRequestModel) {
        repository.sendMediaFiles(obj)
    }

    fun reIssueCard(obj: ReissueCardModelApi) {
        repository.reIssueCard(obj)
    }

    fun UPDATER_REVIEW_COUNT() {
        repository.UPDATER_REVIEW_COUNT()
    }

    fun ReviewAccount() {
        repository.ReviewAccount()
    }

    fun sendVerificationCode(isLoader: Boolean) {
        repository.sendVerificationCode(isLoader)
    }

    fun verifyVerificationCode(obj: VerifyCodeRequestModel) {
        repository.verifyVerificationCode(obj)
    }

    fun verifySignupVerificationCode(obj: VerifySignupCodeRequestModel) {
        repository.verifySignupVerificationCode(obj)
    }

    fun verifySignupEmailCode(obj: VerifySignupEmailCodeRequestModel) {
        repository.verifySignupEmailCode(obj)
    }

    fun signupPersonalInfo(obj: SignUpPersonalInfoRequestModel) {
        repository.signupPersonalInfo(obj)
    }



    fun checkUserName(userName: String) {
        repository.checkUserName(userName)
    }

    fun checkEmail(email: String) {
        repository.checkEmail(email)
    }

    fun changePassword(oldPass: String, newPass: String) {
        repository.changePassword(oldPass, newPass)
    }

    fun logoutUser() {
        repository.logoutUser()
    }

    fun updateProfile(obj: UpdateProfileRequestModel) {
        repository.updateProfile(obj)
    }

    fun getMyProfile(isLoader: Boolean) {
        repository.getMyProfile(isLoader)
    }

    fun sendForgotVerificationCode(obj: SendVerificationCodeRequestModel) {
        repository.sendForgotVerificationCode(obj)
    }

    fun forgotPassword(obj: ForgotPasswordRequestModel) {
        repository.forgotPassword(obj)
    }

    fun getUserAlerts() {
        repository.getUserAlerts()
    }

    fun setAlert(obj: AddUpdateUserAlerts) {
        repository.setAlert(obj)
    }

    fun uploadProfilePic(obj: UploadProfilePicRequestModel) {
        repository.uploadProfilePic(obj)
    }

    fun sendProfileCode() {
        repository.sendProfileCode()
    }
    fun sendBankOtp() {
        repository.sendBankOtp()
    }

    fun verifyProfileCode(code: String) {
        repository.verifyProfileCode(code)
    }

    fun verifyBankVerificationCode(obj: VerifyBankCodeRequestModel) {
        repository.verifyBankVerificationCode(obj)
    }



}