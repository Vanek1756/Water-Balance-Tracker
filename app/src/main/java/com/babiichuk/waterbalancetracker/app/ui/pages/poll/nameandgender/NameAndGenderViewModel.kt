package com.babiichuk.waterbalancetracker.app.ui.pages.poll.nameandgender

import com.babiichuk.waterbalancetracker.app.ui.loaders.UserLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import com.babiichuk.waterbalancetracker.app.ui.utils.GenderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NameAndGenderViewModel @Inject constructor(
    private val userLoader: UserLoader
) : BaseViewModel() {

    val userName = MutableStateFlow<String?>(null)

    fun onNextClicked(genderType: GenderType) {
        userLoader.insertUserNameAndGender(userName.value ?: "User", genderType)
    }

}