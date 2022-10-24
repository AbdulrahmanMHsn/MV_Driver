package com.seamlabs.mvpdriver.common.utility

import artifact.signals_bus.contract.Navigational
import artifact.signals_bus.contract.Signal as SignalContract


sealed class Signal(override var signature: String? = null) : SignalContract

object Load : Signal()

object StopLoading : Signal()

sealed class Navigate : Signal(), Navigational {
    object ToAppContent : Navigate()
    object SetupMainActivity: Navigate()
    object ToInRequestHandle: Navigate()
    object ToAddStudent: Navigate()
    object ToStudentsTab: Navigate()
    object ToDismissQrDialog: Navigate()
    object ToAppSettings: Navigate()
    object ToProfile: Navigate()
    object ToShowUpdateDialog: Navigate()
    object ToStudentProfile: Navigate()
    object ToAddEditStudent: Navigate()
    object ToVerifyStudentDialog: Navigate()
    object ToAppSettingsFromNotifications: Navigate()
    object ToMapFragment: Navigate()
    object ToNotificationsDetails: Navigate()
    object OnSuccessCancelPickUpRequest: Navigate()
    object OnSuccessPickStudent: Navigate()
    object OnSuccessFinishRequest: Navigate()
    object SuccessLoginNavigate: Navigate()
    object SuccessRegisterNavigate: Navigate()
    object SendVerificationCode: Navigate()
    object FireBaseOnCodeSentSuccess: Navigate()
    object PhoneAlreadyVerifiedByFireBaseNavigateToNewPassword: Navigate()
    object SuccessLoginWithClassera: Navigate()
    object ToChooseResetOptions: Navigate()
    object ToLogin: Navigate()
    object SuccessResendCode: Navigate()
    object SuccessCompeleteProfile: Navigate()
    object OnSuccessVerifyCodeNormal: Navigate()
    object OnSuccessSendCodeByEmaiOrCall: Navigate()
    object OnSuccessVerifyCodeReset: Navigate()
    object SuccessAssignHelper: Navigate()
    object OnSuccessEditHelper: Navigate()
    object HandleHelperAssign: Navigate()
    object OnSuccessAddHelper: Navigate()
    object OnSuccessDeleteHelper: Navigate()
    object OnErrorGetNormalCall: Navigate()
    object OnSuccessCreateRequest: Navigate()
    object UserReadMessage: Navigate()
    object ErrorInternetMakeRequest: Navigate()
    object NavigateToClassAttendance: Navigate()
    object InitStaffHome: Navigate()
    object NavigateToMatchingFromGuard: Navigate()
    object NavigateToMatchingFromStaff: Navigate()
    object OnSuccessDeliverRequest: Navigate()
    object OnSuccessMakeNewRequestQr: Navigate()
    object CallNextPage: Navigate()
    object OnSuccessSetStudentAttendance: Navigate()
    object OnSuccessStaffDeliver: Navigate()
    object OnErrorInGetRequestWaitingScreen: Navigate()
    object DelegationRequestCreated: Navigate()
    object RemoveBottomNavBadgeInRequest: Navigate()
    object RemoveBottomNavBadgePendingRequest: Navigate()
    object AcceptDelegationRequest: Navigate()
    object CancelDelegationRequest: Navigate()
    object RejectDelegationRequest: Navigate()
    object ToCallBusSubscription: Navigate()
    object OnSuccessSetAbsent: Navigate()
    object OnSuccessSubmitOffer: Navigate()
}

sealed class SomethingWentWrong (): Signal() {
    object Validation : SomethingWentWrong()
    object ErrorMessage: SomethingWentWrong()
    object ConnectionFailure : SomethingWentWrong()
    object CodeVerificationError : SomethingWentWrong()
    object PhoneNotValid : SomethingWentWrong()
    object DelegationError: SomethingWentWrong()
}


