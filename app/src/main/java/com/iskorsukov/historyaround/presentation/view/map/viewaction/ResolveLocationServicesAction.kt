package com.iskorsukov.historyaround.presentation.view.map.viewaction

import com.google.android.gms.common.api.ResolvableApiException
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction

data class ResolveLocationServicesAction(private val resolvableApiException: ResolvableApiException):
    ViewAction<ResolvableApiException>(resolvableApiException)