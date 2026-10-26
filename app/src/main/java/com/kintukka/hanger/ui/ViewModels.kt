package com.kintukka.hanger.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kintukka.hanger.AppContainer
import com.kintukka.hanger.HangerApp

@Composable
fun appContainer(): AppContainer = (LocalContext.current.applicationContext as HangerApp).container

/** ViewModel scoped to the current nav destination, built from the app container. */
@Composable
inline fun <reified VM : ViewModel> hangerViewModel(key: String? = null, crossinline create: (AppContainer) -> VM): VM {
    val container = appContainer()
    return viewModel(key = key, factory = viewModelFactory { initializer { create(container) } })
}
