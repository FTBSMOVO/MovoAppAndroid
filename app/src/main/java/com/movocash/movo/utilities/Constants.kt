package com.movocash.movo.utilities

import com.movocash.movo.data.model.requestmodel.ContactModel
import java.util.*

object Constants {
    //BASE URL

    //DEV URL
//    const val BASE_URL = "http://ec2-54-221-37-200.compute-1.amazonaws.com/api/"

   // PRODUCTION URL LINUX
    const val BASE_URL = "https://appapi.movo.cash/api/v5/"

    //DEV URL LINUX
 // const val BASE_URL = "https://devappapi.movo.cash/api/v5/"


    const val CONST_NO_INTERNET_CONNECTION = "Please check your internet connection"
    const val CONST_SERVER_NOT_RESPONDING = "Server not responding Please try again later"
    const val ACCESS_TOKEN = "access_token"
    const val SESSION_ID = "session_id"
    const val MY_USER_ID = "user_id"
    const val SESSION_ID_STATUS = "session_id_status"
    const val REVIEW_COUNT_DB = "0"

    //MOVO SDK IDs
    const val MOVO_APP_ID = "movoSDK"
    const val MOVO_API_KEY = "LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUNRd0RRWUpLb1pJaHZjTkFRRUJCUUFERXdBd0VBSUpBTHcxem1XcEVxaEpBZ01CQUFFPQotLS0tLUVORCBQVUJMSUMgS0VZLS0tLS0K"

    //SDK TASK
    const val MOVO_GET_CARD_INFO = "revealCardInfo"


    // Bank Transfer

    const val BANK_TO_CARD_TRANSFER = 1
    const val CARD_TO_BANK_TRANSFER = 2


    ///// SIGNUP KYC FLOW ENUM

    const val INSTANT_PASS =  1
    const val FAIL = 2
    const val OTP_SCREEN = 3
    const val ID_VERFICATION = 4

    //Web URLS
    const val DEPOSIT_URL = "https://movo.cash/ccb-deposit-faq/"
    const val MO_PRO_SUPPORT_URL = "https://help.movo.cash/hc/en-us"
    const val TERMS_CONDITION_URL = "https://movo.cash/ccb-digital-wallet-tcs/"
    const val PRIVACY_POLICY_URL = "https://movo.cash/ccb-privacy/"
    const val AGREEMENT_URL = "https://movo.cash/mobileapp/ccb-cardholder-agreement/"
    const val COASTAL_URL =  "https://movo.cash/ccb-privacy-document/"
    const val MOVO_PRIVACY_URL = "https://movo.cash/ccb-movo-privacy-pdf/"

    const val CONVERSATION_ID = "conversationID"

    const val ERROR_CODE = "Error Code"

    const val CONST_TEXT = 0
    const val CONST_DATE = 1
    const val WEB_SUPPORT = 1
    const val WEB_TERMS = 2
    const val WEB_PRIVACY = 3
    const val WEB_AGREEMENT = 4
    const val WEB_COASTAL = 5
    const val WEB_MOVO_PRIVACY = 6

    const val MEDIA_DOC_FRONT = 1
    const val MEDIA_DOC_BACK = 2
    const val CONST_IMAGE = "2"
    const val MEDIA_SELFIE = 3
    const val CONST_DEFAULT_IMAGE_WIDTH = 1024
    const val CONST_OPERATOR_GREATER = 1
    const val CONST_OPERATOR_LESS = 2
    var CONST_NUM = "CONST_NUM"
    var REMEMBERED_USER_NAME = "remembered_user_name"
    var CONST_EMAIL = "CONST_EMAIL"
    var CUSTOMER_ID = "customer_id"
    var CONST_CC = "CONST_CC"
    var FIRST_NAME = "FIRST_NAME"
    var LAST_NAME = "LAST_NAME"
    var USER_IMAGE = "USER_IMAGE"
    var USER_THUMB = "USER_THUMB"
    var EMAIL = "email"
    var USER_ID = "user_id"
    var USER_LAST_LOGIN = "user_last_login"
    var USER_NAME = "user_name"
    var USER_PASS = "user_pass"
    var USER_NUMBER = "user_number"
    var CONST_IS_THUMB_LOGIN = "const_is_thumb_login"
    var CONTACT_LIST = ArrayList<ContactModel>()
    var CONST_CARD_RESPONSE = "const_card_response"
    var CONST_LOGIN_RESPONSE = "const_login_response"
    var CONST_MY_BANKS_RESPONSE = "const_my_banks_response"
    var CONST_MY_PAYEE_RESPONSE = "const_my_payee_response"
    var CONST_MY_SHIPPING_INFO = "const_my_shipping_info"
    var PRIMARY_REF_ID = "primary_ref_id"
    const val CONST_SCHEDULE = "Schedule"
    const val CONST_DOB = "const_dob"
    const val INSUFFICIENT_FUND = "You don't have sufficient balance."

    const val CONST_PRIMARY = 0 //"MAINCARD" – For Main Card
    const val CONST_PURSE = 1 //• "PURSE" – For Card Purse
    const val CONST_SECONDARY = 2 //• "SUPPLEMENTARY" – For Supplementary Card
    const val CONST_BALANCE = 3 // • "SHAREDBALANCE" – For Shared Balance Card
    /*const val CARD_TO_BANK = 1
    const val BANK_TO_CARD = 2*/
    const val CARD_TO_BANK = 2
    const val BANK_TO_CARD = 1
    const val CONST_GREEN_RELOAD = 1
    const val CONST_GREEN_MONEY = 2
    const val CONST_VISA_READY = 3
    const val CONST_STEP_DEPOSIT = 4
    const val CONST_MOVO_CHAIN = 5
    const val CONST_FREQUENCY_ONCE = 1
    const val CONST_FREQUENCY_DATE = 2

    //////////////////////////////////Dynamic Menu
    var MOVO_CASH = "true"
    var MOVO_PAY = "true"
    var CASH_CARD = "true"
    var MOVO_CHAIN = "true"
    var DIGITAL_BANKING = "true"
    var ECHECK_BOOK = "true"
    var MY_PROFILE = "true"
    var LOCK_UNLOCK_CARD = "true"
    var CHANGE_PASSWORD = "true"
    var DEPOSIT_HUB = "true"
    var ABOUT_US = "true"

    var m_Accounts = "true"
    var M_ACCOUNTS_SUMMARY = "true"
    var M_ORDER_PHYSICAL_CARD = "true"
    var M_SEND_MONEY = "true"
    var M_HISTORY = "true"
    var M_MY_BANK_ACCOUNTS = "true"
    var M_ACH_TRANSFERS = "true"
    var M_CASH_OUT_TO_BANK = "true"
    var M_CASH_IN_TO_MOVO = "true"
    var M_DIRECT_DEPOSIT = "true"
    var M_SCHEDULED_TRANSFER = "true"
    var M_Transfer_ACTIVITY = "true"
    var M_MAKEPAYMENT = "true"
    var M_PAYMENT_HISTORY = "true"
    var M_ADD_PAYEES = "true"
    var M_MY_PAYEES = "true"
    var M_SCHEDULED_PAYMENT = "true"
    var M_MANAGE_PROFILE = "true"
    var M_MOVO_CASH_ALERTS = "true"
    var M_BIOMETRIC_AUTHENTICATION = "true"
    var M_MOPRO_SUPPORT = "true"
    var M_TERMS_CONDITION = "true"
    var M_PRIVACY_POLICY = "true"



    //Payment Status
    const val PAY_SCHEDULED = "S"
    const val PAY_FAILED = "F"
    const val PAY_PROCESSED = "P"
    const val PAY_CANCELLED = "C"
    const val PAY_IN_PROGRESS = "I"
    const val PAY_DEFFERRED = "D"
    const val PAY_DEBIT_SUCCESSFUL = "B"
    const val PAY_LOGGED = "L"

    //Make Payment Status
    const val PAYMENT_LOGGED = "Logged"
    const val PAYMENT_POSTED = "Posted"
    const val PAYMENT_FAILED = "Failed"
    const val PAYMENT_RETURNED = "Returned/Sent"
    const val PAYMENT_IN_PROGRESS = "InProgress"
    const val PAYMENT_CANCELLED = "Canceled"
    const val PAYMENT_SCHEDULED = "Scheduled"
    const val PAYMENT_ALL = "All"

  /*  //Make Payment Status
    const val PAYMENT_LOGGED = "Logged"
    const val PAYMENT_POSTED = "Posted"
    const val PAYMENT_FAILED = "F"
    const val PAYMENT_RETURNED = "Returned/Sent"
    const val PAYMENT_IN_PROGRESS = "I"
    const val PAYMENT_CANCELLED = "C"
    const val PAYMENT_SCHEDULED = "S"
    const val PAYMENT_ALL = "All"*/


    //Card Status
    const val CARD_CLOSED = "F"
    const val CARD_INACTIVE = "I"
    const val CARD_ISSUED_INACTIVE = "A"

    //FEE ENUMS

    const val CONST_UNLOAD_CASHCARD = 0
    const val CONST_RELOAD_CASHCARD = 1
    const val CONST_SHARE_FUNDS = 2
    const val CONST_GENERATE_CASHCARD = 3
    const val CONST_PAY_TO_NON_MOV = 4
    const val CONST_MAKE_PAYMENT = 5
    const val CONST_CASH_TO_BANK = 6
    const val CONST_CARD_REISSUE = 7
    const val ITC_FEES = "ACH_USE"



    /////////////Review Account///////////////////

    const val REVIEW_ACCOUNT = "Account/getusersetup"
    const val UPDATER_REVIEW_COUNT = "Account/updatereviewcount"
 //////// Account Status  ////////////////////

 const val ACCOUNT_FAILED = "F"
 const val ACCOUNT_LOGGED = "L"
 const val ACCOUNT_VERIFIED = "F"
 const val ACCOUNT_INPROGRESS = "I"
 const val NOT_APPLICABLE = "N"

    //------------Api Constants ------------//

    //accounts
   //const val SIGN_UP_URL = "Account/signup"
    const val SIGN_UP_URL2 = "Account/signup"
    const val LOGIN_URL = "Account/login"

    const val SEND_CODE_URL = "Account/createotp"
    const val SEND_MEDIA_URL = "Account/verifydocuments"
    const val VERIFY_CODE_URL = "Account/verifyotp"

    const val VERIFY_Signup_CODE = "Account/verifyotp"

    const val VERIFY_BANK_CODE_URL = "Account/iscodeverifieddifferentforsmsemail"
    const val VERIFY_SIGNUP_EMAIL_CODE = "Account/signupverifyemailcode"
    const val MY_EMAIL_CODE = "emailcode"
    const val SIGNUP_PERSONAL_INFO = "Account/signuppersonalinformation"
    const val CHECK_USERNAME_URL = "Account/isusernameexists"
    const val CHECK_EMAIL_URL = "Account/isemailexists"
    const val CHANGE_PASS_URL = "Account/updatepassword"
    const val LOGOUT_URL = "Account/logout"
    const val UPDATE_PROFILE_URL = "Account/updateprofile"
    const val GET_USER_PROFILE_URL = "Account/getcardholderprofile"
    const val SEND_VERIFICATION_CODE_URL = "Account/forgotsentverificationcode"
    const val FORGOT_PASSWORD_URL = "Account/changeforgotpasswordwithcode"
    const val GET_USER_ALERTS_URL = "Account/getuseralerts"
    const val SET_USER_ALERTS_URL = "Account/addupdateuseralerts"
    const val UPLOAD_PIC_URL = "Account/updateprofilepicture"
    const val SEND_PROFILE_CODE = "Account/sendverificationcode"
    const val SEND_BANK_OTP = "Account/sendverificationcodedifferentforsmsemail"

    const val VERIFY_PROFILE_CODE = "Account/iscodeverified"


    //common
    const val GET_COUNTRIES_URL = "Common/getcountries"
    const val GET_STATES_URL = "Common/getstates"
    const val GET_IDENTIFICATION_URL = "Common/getidentificationtypes"
    const val GET_QUESTIONS_URL = "Common/getsecretquestions"
    const val GET_SERVICE_FEE = "Common/getservicefee"
    const val GET_FREQUENCIES = "Common/getfrequencies"
    const val GET_CONFIGURATIONS = "Common/getconfiguration"


    //Card
    const val GET_CARD_AUTH_DATA_URL = "Card/getcardauthdata"
    const val GET_CARDS_ACCOUNT_URL = "Card/getcardaccounts"
    const val GET_MINI_STATEMENT_ACCOUNTS_URL = "Card/ministatementmultiaccounts"
    const val GET_TRANSACTION_HISTORY_URL = "Card/transactionhistory"
    const val SHARE_FUND_URL = "Card/sharefunds"
    const val GET_SHARE_HISTORY_URL = "Card/payhistory"
    const val ADD_CASH_CARD_URL = "Card/addcashcard"
    const val RELOAD_CASH_CARD_URL = "Card/reloadcashcard"
    const val UNLOAD_CASH_CARD_URL = "Card/unloadcashcard"
    const val BLOCK_UNBLOCK_CARD_URL = "Card/setcardstatus"
    const val UNBLOCK_CARD_URL = "unblock_card_url"
    const val CHANGE_CASH_CARD_NAME = "Card/updatename"
    const val GET_MOVO_TOKEN_URL = "Card/getsignontoken"
    const val GET_USERSCHEDULE_RELOAD = "Card/getuserscheduledreload"
    const val ADD_USERSCHEDULE_RELOAD = "Card/addupdatescheduledreload"
    const val REISSUE_CARD = "Card/reissuecard"
    const val ACTIVATE_CARD = "Card/activatecard"

    //Banks
    const val CREATE_BANK_ACCOUNT_URL = "Banking/createbankaccount"
    const val PLAID_CREATE_BANK_ACCOUNT_URL = "Banking/plaidcreatebankaccount"

    const val GET_BANK_ACCOUNTS_URL = "Banking/bankaccountslist"
    const val REMOVE_BANK_ACCOUNT_URL = "Banking/removebankaccount"
    const val EDIT_BANK_ACCOUNT_URL = "Banking/editbankaccount"

    const val CASH_TO_BANK_TRANSFER_URL = "Banking/c2btransfer"
    const val BANK_TO_CASH_TRANSFER_URL = "Banking/b2ctransfer"

    const val CASH_TO_BANK_TRANSFER_UPDATE_URL = "Banking/c2btransferupdate"
    const val BANK_TO_CASH_TRANSFER_UPDATE_URL = "Banking/b2ctransferupdate"

    const val GET_CASH_TO_BANK_TRANSFER_LIST_URL = "Banking/c2btransferlist"
    const val GET_BANK_TO_CASH_TRANSFER_LIST_URL = "Banking/b2ctransferlist"

    const val CASH_TO_BANK_TRANSFER_CANCEL_URL = "Banking/c2btransfercancel"
    const val BANK_TO_CASH_TRANSFER_CANCEL_URL = "Banking/b2ctransfercancel"

    const val GET_PLAID_TOKEN = "Banking/getlinktoken"



    //ECheckBook
    const val ADD_PAYEE_URL = "ECheckbook/addpayee"
    const val EDIT_PAYEE_URL = "ECheckbook/editcardholderpayee"
    const val REMOVE_PAYEE_URL = "ECheckbook/deletecardholderpayee"
    const val GET_CARD_HOLDER_LIST_URL = "ECheckbook/getcardholderpayeelist"
    const val SEARCH_PAYEE_URL = "ECheckbook/searchpayee"
    const val MAKE_PAYMENT_URL = "ECheckbook/makepayment"
    const val EDIT_PAYMENT_URL = "ECheckbook/editpayment"
    const val CANCEL_PAYMENT_URL = "ECheckbook/cancelpayment"
    const val GET_PAYMENT_HISTORY_URL = "ECheckbook/getbillpaymenthistory"

    //Upload
    const val GET_PIC_URL = "Upload/multipleuploadmedias3"
}