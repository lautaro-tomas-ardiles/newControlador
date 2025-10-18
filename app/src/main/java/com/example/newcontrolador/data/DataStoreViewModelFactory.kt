package com.example.newcontrolador.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DataStoreViewModelFactory(private val dataStoreManager: DataStoreManager) :
	ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(DataStoreViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST")
			return DataStoreViewModel(dataStoreManager) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}