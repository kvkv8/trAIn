package com.krist.train.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
fun <T : ViewModel> viewModelFactory(create: () -> T): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = create() as VM
    }
