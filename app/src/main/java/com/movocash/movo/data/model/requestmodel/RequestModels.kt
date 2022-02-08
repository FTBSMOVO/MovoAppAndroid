package com.movocash.movo.data.model.requestmodel

data class SignUpRequestModel(val firstName: String, val lastName: String, val identificationTypeId: Int, val identificationValue: String, val dateOfBirth: String, val genderId: Int,  val cellCountryCode: String, val cellPhoneNumber: String, val addressLine1: String,val addressLine2: String, val countryId: Int, val stateId: Int, val city: String, val zipCode: String, val lnSessionId: String, val lnSessionIdResponse: String)
data class SignUpRequestModel2(val email: String, val username: String, val password: String, val secretQuestionId: Int, val secretAnswer: String)
data class SignUpPersonalInfoRequestModel(val firstName: String, val middleName: String,val lastName: String,
                                          val identificationTypeId: Int, val identificationValue: String, val dateOfBirth: String,
                                          val genderId: Int,
                                          val cellCountryCode: String, val cellPhoneNumber: String,
                                          val addressLine1: String,val addressLine2: String, val countryId: Int,
                                          val stateId: Int, val city: String, val zipCode: String,
                                          val userId: String,val lnSessionId: String, val lnSessionIdResponse: String)



data class LoginRequestModel(val userName: String, val password: String, val sessionId: String, val lnSessionIdResponse: String, var deviceToken: String, var deviceModel: String, var os: String, var version: String, var deviceType: Int)

data class MediaRequestModel(val userId: String, val documents: ArrayList<Document>)
data class ReissueCardModelApi(val referenceId: String)
data class ActivateCardModelApi(val referenceId: String)

data class Document(val documentType: Int, val base64: String)

data class VerifyCodeRequestModel(val username: String, val password: String, val secretQuestionId: Int, val secretAnswer: String, val otp: String, val sessionId: String, val coversationId: String)
data class VerifySignupCodeRequestModel(val code: String, val userId: String, val conversationId: String)

data class VerifyBankCodeRequestModel(val phoneCode: String, val emailCode: String)
data class VerifySignupEmailCodeRequestModel(val code: String, val userId: String)

data class ContactModel(val countryCode: String, val number: String, val name: String, var isSelected: Boolean)

data class CustomHistoryModel(var amount: Double, var description: String, var date: String, var type: Int)

data class CustomShareHistoryModel(var amount: Double,var label: String, var sendTo: String, var date: String, var status: String, var type: Int,var comments: String)

data class AddUserScheduleReloadsRequestModel(var id:String,var cardReferenceId: String, var isEnabled: Boolean,var serviceProvider: String,  var paymentAmount: Double, var paymentDueDate: String, var autoReloadDate: String, var nextPaymentDueDate: String, var nextAutoReloadDate: String, var frequencyId: Int)

data class ShareFundRequestModel(var fromReferenceId: String, var amount: Double, var toPhoneOrEmail: String, var comments: String)


data class AddCashCardRequestModel(var fromReferenceId: String, var amount: Double, var nameOnCard: String)

data class LoadUnloadRequestModel(var primaryReferenceId: String, var cashCardReferenceId: String, var amount: Double)

data class CreateBankAccountRequestModel(var bankSerialNumberIfEdit: String, var accountType: Int, var legalName: String, var bankName: String, var routingNumber: String, var isCheckingAccount: Boolean, var bankAccountNumber: String, var nickName: String, var comments: String)

data class PlaidCreateBankAccountRequestModel(var publicToken: String, var achType: Int)

data class ChangePasswordRequestModel(var oldPassword: String, var newPassword: String)

data class AddEditPayeeRequestModel(var srNo: String, var payeeName: String, var address: String, var city: String, var stateId: Int, var stateIso2: String, var zip: String, var nickName: String, var depositAccountNumber: String)

data class CustomCashOutBankModel(var srNo: String, var bankNumber: String, var referenceNum: String, var cardNum: String, var frequencyType: Int, var date: String, var cardAmount: Double, var fee: Double, var totalAmount: Double, var comments: String, var notes: String)

data class CashOutToBankRequestModel(var referenceId: String,var transferId: String, var accountSrNo: String, var amount: Double, var transferComments: String, var transferFrequency: Int, var transferDate: String)

data class CashintoMovoRequestModel(var transferId: String, var accountSrNo: String, var amount: Double, var transferComments: String, var transferFrequency: Int, var transferDate: String)


data class CustomScheduleTransferModels(var fromAccount: String, var toAccount: String, var amount: Double, var date: String, var transferId: String, var type: Int, var status: String)

data class MyCustomScheduleTransferModels(var fromAccount: String, var toAccount: String, var amount: Double, var date: String, var transferId: String, var type: Int, var status: String,var achType: Long)


data class CustomEditScheduledTransferModel(var transferId: String, var fromAccount: String, var toAccount: String, var amount: Double, var date: String, var frequencyType: Int, var comments: String)

data class SearchPayeeRequestModel(var searchString: String, var skip: Int, var take: Int)

data class MakeUpdatePaymentRequestModel(var referenceId: String, var tansId: String, var payeeSrNo: String, var payeeAccountNumber: String, var amount: Double, var paymentDate: String, var comments: String)

data class CustomPaymentDetailModel(var fromAccount: String, var toAccount: String, var transferDate: String, var amount: Double, var fee: Double, var totalAmount: Double, var status: String, var notes: String, var address: String, var type: Int, var paymentId: String, var payeeAccountNumber: String)

data class UpdateProfileRequestModel(var email: String, var cellCountryCode: String, var cellPhoneNumber: String, var addressInformation: AddressInfo, var shippingInformation: AddressInfo)

data class AddressInfo(var addressLine1: String, var countryId: Int, var stateId: Int, var stateIso2: String, var city: String, var zipCode: String)

data class SendVerificationCodeRequestModel(var username: String, var countryCode: String, var phoneNumber: String)

data class ForgotPasswordRequestModel(var username: String, var code: String, var phoneNumber: String?, var newPassword: String)

data class AddUpdateUserAlerts(var alertId: Int, var alertTypeId: String, var id: String, var operatorTypeId: Int, var amount: Double, var sms: String, var email: String, var mobilePush: Boolean)

data class UpdateNameRequestModel(var referenceId: String, var nameOnCard: String)

data class UploadProfilePicRequestModel(var profilePicture: String, var profilePictureThumb: String)

data class VerifyProfileCodeRequestModel(var code: String)

data class GetAuthTokenRequestModel(var referenceId: String, var entityId: String)