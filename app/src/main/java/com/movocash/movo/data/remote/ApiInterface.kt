package com.movocash.movo.data.remote

import com.movocash.movo.data.model.requestmodel.*
import com.movocash.movo.data.model.responsemodel.*
import com.movocash.movo.utilities.Constants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    //Accounts
    /*@POST(Constants.SIGN_UP_URL)
    fun signUpUser(@Body obj: SignUpRequestModel): Call<SignUpResponseModel>*/

    @POST(Constants.SIGN_UP_URL2)
    fun signUpUser2(@Body obj: SignUpRequestModel2): Call<SignUpResponseModel>
    @POST(Constants.SIGNUP_PERSONAL_INFO)
    fun signupPersonalinfo(@Body obj: SignUpPersonalInfoRequestModel): Call<SignupPersoanlinfoResponseModel>

    @POST(Constants.LOGIN_URL)
    fun loginUser(@Body obj: LoginRequestModel): Call<LoginResponseModel>

/*    /api/Card/reissuecard

    @POST(Constants.Re)
    fun loginUser(@Body obj: LoginRequestModel): Call<LoginResponseModel>*/

    @POST(Constants.SEND_MEDIA_URL)
    fun sendMediaFiles(@Body obj: MediaRequestModel): Call<SignupPersoanlinfoResponseModel>

    @POST(Constants.REISSUE_CARD)
    fun reIssueCard(@Body obj: ReissueCardModelApi): Call<ReissueCardModelApi>

    @POST(Constants.ACTIVATE_CARD)
    fun ActivateCard(@Body obj: ActivateCardModelApi): Call<ActivateCardModelApi>

    @POST(Constants.UPDATER_REVIEW_COUNT)
    fun UPDATER_REVIEW_COUNT(): Call<GeneralResponseModel>

   /* @POST(Constants.REVIEW_ACCOUNT)
    fun ReviewAccount(): Call<GeneralResponseModel>*///UPDATER_REVIEW_COUNT

    @GET(Constants.REVIEW_ACCOUNT)
    fun ReviewAccount(): Call<ReviewAccountResponseModel>

    @GET("${Constants.SEND_CODE_URL}/{userId}")
    fun sendVerificationCode(@Path("userId") userId: String): Call<SendVerificationResponseModel>

    @POST(Constants.VERIFY_CODE_URL)
    fun verifyVerificationCode(@Body obj: VerifyCodeRequestModel): Call<GeneralResponseModel>

    @POST(Constants.VERIFY_Signup_CODE)
    fun verifySignupVerificationCode(@Body obj: VerifySignupCodeRequestModel): Call<SignupPersoanlinfoResponseModel>



    @POST(Constants.VERIFY_BANK_CODE_URL)
    fun verifyBankVerificationCode(@Body obj: VerifyBankCodeRequestModel): Call<GeneralResponseModel>

    @POST(Constants.VERIFY_SIGNUP_EMAIL_CODE)
    fun verifySignupEmailCode(@Body obj: VerifySignupEmailCodeRequestModel): Call<GeneralResponseModel>

    @GET("${Constants.CHECK_USERNAME_URL}/{username}")
    fun checkUserName(@Path("username") userName: String): Call<UserNameResponseModel>

    @GET("${Constants.CHECK_EMAIL_URL}/{email}")
    fun checkEmail(@Path("email") email: String): Call<UserNameResponseModel>

    @POST(Constants.CHANGE_PASS_URL)
    fun changePassword(@Body obj: ChangePasswordRequestModel): Call<GeneralResponseModel>

    @GET("${Constants.LOGOUT_URL}/{logoutFromAll}")
    fun logoutUser(@Path("logoutFromAll") logoutFromAll: Boolean): Call<GeneralResponseModel>

    @POST(Constants.UPDATE_PROFILE_URL)
    fun updateProfile(@Body obj: UpdateProfileRequestModel): Call<UpdateProfileResponseModel>

    @GET(Constants.GET_USER_PROFILE_URL)
    fun getUserProfile(): Call<GetMyProfileResponseModel>

    @POST(Constants.SEND_VERIFICATION_CODE_URL)
    fun sendForgotVerificationCode(@Body obj: SendVerificationCodeRequestModel): Call<GeneralResponseModel>

    @POST(Constants.FORGOT_PASSWORD_URL)
    fun forgotPassword(@Body obj: ForgotPasswordRequestModel): Call<GeneralResponseModel>

    @GET(Constants.GET_USER_ALERTS_URL)
    fun getUserAlerts(): Call<GetUserAlertsResponseModel>

    @POST(Constants.SET_USER_ALERTS_URL)
    fun setUserAlerts(@Body obj: AddUpdateUserAlerts): Call<GeneralResponseModel>

    @POST(Constants.UPLOAD_PIC_URL)
    fun uploadProfilePic(@Body obj: UploadProfilePicRequestModel): Call<GeneralResponseModel>

    @POST(Constants.SEND_PROFILE_CODE)
    fun sendProfileCode(): Call<GeneralResponseModel>

    @POST(Constants.SEND_BANK_OTP)
    fun sendBankOtp(): Call<GeneralResponseModel>

    @POST(Constants.VERIFY_PROFILE_CODE)
    fun verifyProfileCode(@Body obj: VerifyProfileCodeRequestModel): Call<GeneralResponseModel>



    //Common
    @GET(Constants.GET_COUNTRIES_URL)
    fun getCountries(): Call<IDNameResponseModel>

    @GET(Constants.GET_FREQUENCIES)
    fun getFrequencies(): Call<GetFrequesnciesResponseModel>

    @GET(Constants.GET_CONFIGURATIONS)
    fun getConfigurations(): Call<sideMenuResponseModel>

    @GET("${Constants.GET_STATES_URL}/{countryId}")
    fun getStates(@Path("countryId") countryId: Int): Call<IDNameResponseModel>

    @GET("${Constants.GET_SERVICE_FEE}/{referenceId}/{serviceType}")
    fun getServiceFee(
        @Path("referenceId") referenceId: String,
        @Path("serviceType") serviceType: Int
    ): Call<GetServiceFeeResponseModel>

    @GET(Constants.GET_IDENTIFICATION_URL)
    fun getIdentificationTypes(): Call<IDNameResponseModel>

    @GET(Constants.GET_QUESTIONS_URL)
    fun getQuestions(): Call<IDNameResponseModel>

    //Card  GET_USERSCHEDULE_RELOAD
    @GET("${Constants.GET_CARD_AUTH_DATA_URL}/{referenceID}")
    fun getAuthData(@Path("referenceID") referenceID: String): Call<GetCardAuthResponseModel>

    @GET("${Constants.GET_USERSCHEDULE_RELOAD}/{referenceID}")
    fun getUserScheduleReload(@Path("referenceID") referenceID: String): Call<getUserScheduleReloadResponseModel>

    @GET(Constants.GET_CARDS_ACCOUNT_URL)
    fun getCardAccounts(): Call<GetCardAccountsResponseModel>

    @GET(Constants.GET_MINI_STATEMENT_ACCOUNTS_URL)
    fun getMiniStatementAccounts(): Call<GetMiniStatementResponseModel>

    @GET("${Constants.GET_TRANSACTION_HISTORY_URL}/{referenceID}")
    fun getTransactionHistory(@Path("referenceID") referenceID: String): Call<GetTransactionHistoryResponseModel>

    @POST(Constants.SHARE_FUND_URL)
    fun shareFund(@Body obj: ShareFundRequestModel): Call<ShareFundResponseModel>

    @POST(Constants.ADD_USERSCHEDULE_RELOAD)
    fun AddUserScheduleReload(@Body obj: AddUserScheduleReloadsRequestModel): Call<UpdatedGeneralResponseModel>

    @GET("${Constants.GET_SHARE_HISTORY_URL}/{referenceID}")
    fun getShareHistory(@Path("referenceID") referenceID: String): Call<GetShareHistoryResponseModel>

    @POST(Constants.ADD_CASH_CARD_URL)
    fun addCashCard(@Body obj: AddCashCardRequestModel): Call<ShareFundResponseModel>

    @POST(Constants.RELOAD_CASH_CARD_URL)
    fun reloadCashCard(@Body obj: LoadUnloadRequestModel): Call<ShareFundResponseModel>

    @POST(Constants.UNLOAD_CASH_CARD_URL)
    fun unloadCashCard(@Body obj: LoadUnloadRequestModel): Call<ShareFundResponseModel>

    @GET("${Constants.BLOCK_UNBLOCK_CARD_URL}/{referenceID}/{isBlocked}")
    fun blockUnblockCard(
        @Path("referenceID") referenceID: String,
        @Path("isBlocked") isBlocked: Boolean
    ): Call<CardBlockedResponseModel>

    @POST(Constants.CHANGE_CASH_CARD_NAME)
    fun changeCashCardName(@Body obj: UpdateNameRequestModel): Call<GeneralResponseModel>

    @POST(Constants.GET_MOVO_TOKEN_URL)
    fun getAuthToken(@Body obj: GetAuthTokenRequestModel): Call<GetAuthTokenResponseModel>
    //Banking

    @POST(Constants.CREATE_BANK_ACCOUNT_URL)
    fun createBankAccount(@Body obj: CreateBankAccountRequestModel): Call<CreateBankAccountResponseModel>

    @POST(Constants.PLAID_CREATE_BANK_ACCOUNT_URL)
    fun plaidcreateBankAccount(@Body obj: PlaidCreateBankAccountRequestModel): Call<CreateBankAccountResponseModel>

    @GET(Constants.GET_BANK_ACCOUNTS_URL)
    fun getBankAccounts(): Call<GetBankAccountsResponseModel>

    @POST(Constants.EDIT_BANK_ACCOUNT_URL)
    fun editBankAccount(@Body obj: CreateBankAccountRequestModel): Call<CreateBankAccountResponseModel>

    @GET("${Constants.REMOVE_BANK_ACCOUNT_URL}/{accountSerialNo}")
    fun removeBankAccount(@Path("accountSerialNo") accountSerialNo: String): Call<CreateBankAccountResponseModel>

    @POST(Constants.CASH_TO_BANK_TRANSFER_URL)
    fun cashToBankTransfer(@Body obj: CashOutToBankRequestModel): Call<CashToBankResponseModel>

    @POST(Constants.BANK_TO_CASH_TRANSFER_URL)
    fun bankToCashTransfer(@Body obj: CashintoMovoRequestModel): Call<CashToBankResponseModel>

    @POST(Constants.CASH_TO_BANK_TRANSFER_UPDATE_URL)
    fun cashToBankTransferUpdate(@Body obj: CashOutToBankRequestModel): Call<CashToBankResponseModel>

    @POST(Constants.BANK_TO_CASH_TRANSFER_UPDATE_URL)
    fun bankToCashTransferUpdate(@Body obj: CashOutToBankRequestModel): Call<CashToBankResponseModel>

    /*@GET(Constants.GET_CASH_TO_BANK_TRANSFER_LIST_URL)/
    fun getCashBankTransferList(): Call<GetScheduledTransfersResponseModel>*/
    @GET("${Constants.GET_CASH_TO_BANK_TRANSFER_LIST_URL}/{referenceID}")
    fun getCashBankTransferList(@Path("referenceID") referenceID: String): Call<GetScheduledTransfersResponseModel>

    @GET("${Constants.GET_BANK_TO_CASH_TRANSFER_LIST_URL}/{referenceID}")
    fun getBankCashTransferList(@Path("referenceID") referenceID: String): Call<GetScheduledTransfersResponseModel>

/*
    @GET("${Constants.GET_SHARE_HISTORY_URL}/{referenceID}")
    fun getShareHistory(@Path("referenceID") referenceID: String): Call<GetShareHistoryResponseModel>*/

    @GET("${Constants.CASH_TO_BANK_TRANSFER_CANCEL_URL}/{transferId}")
    fun cancelScheduledTransfer(@Path("transferId") transferId: String): Call<CashToBankResponseModel>

    @GET("${Constants.BANK_TO_CASH_TRANSFER_CANCEL_URL}/{transferId}")
    fun cancelBtcScheduledTransfer(@Path("transferId") transferId: String): Call<CashToBankResponseModel>

    @GET(Constants.GET_PLAID_TOKEN)
    fun getPlaidToken(): Call<UpdatedGeneralResponseModel>

    //ECheckBook

    @POST(Constants.ADD_PAYEE_URL)
    fun addPayee(@Body obj: AddEditPayeeRequestModel): Call<PayeeResponseModel>

    @POST(Constants.EDIT_PAYEE_URL)
    fun editPayee(@Body obj: AddEditPayeeRequestModel): Call<PayeeResponseModel>

    @GET("${Constants.REMOVE_PAYEE_URL}/{accountSerialNo}/{payeeAccountNumber}")
    fun removePayee(@Path("accountSerialNo") accountSerialNo: String, @Path("payeeAccountNumber") payeeAccountNumber: String): Call<PayeeResponseModel>

    @GET(Constants.GET_CARD_HOLDER_LIST_URL)
    fun getMyPayeesList(): Call<GetMyPayeeResponseModel>

    @POST(Constants.SEARCH_PAYEE_URL)
    fun searchPayee(@Body obj: SearchPayeeRequestModel): Call<GetSearchedPayeeResponseModel>

    @POST(Constants.MAKE_PAYMENT_URL)
    fun makePayment(@Body obj: MakeUpdatePaymentRequestModel): Call<PaymentResponseModel>

    @POST(Constants.EDIT_PAYMENT_URL)
    fun updatePayment(@Body obj: MakeUpdatePaymentRequestModel): Call<PaymentResponseModel>

    @GET("${Constants.CANCEL_PAYMENT_URL}/{transId}")
    fun cancelPayment(@Path("transId") transId: String): Call<PaymentResponseModel>

    @GET("${Constants.GET_PAYMENT_HISTORY_URL}/{referenceID}")
    fun getPaymentHistory(@Path("referenceID") referenceID: String): Call<GetPaymentHistoryResponseModel>






    //Upload
    @Multipart
    @POST(Constants.GET_PIC_URL)
    fun uploadMedia(@Part mediaFiles: ArrayList<MultipartBody.Part>, @Part("type") type: Int): Call<UploadMediaResponseModel>
}