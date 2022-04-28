package com.iskorsukov.historyaround.presentation.view.map.viewaction

import com.google.android.gms.common.api.ResolvableApiException
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction

class ResolveLocationServicesAction(resolvableApiException: ResolvableApiException):
    ViewAction<ResolvableApiException>(resolvableApiException)