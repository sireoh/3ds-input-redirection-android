package com.example.client__android_app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.client__android_app.ui.components.Credits
import com.example.client__android_app.ui.components.Instructions
import com.example.client__android_app.ui.components.IpAddressInput
import com.example.client__android_app.ui.components.DSConsoleButtons
import com.example.client__android_app.ui.components.InputRedirectionImage
import com.example.client__android_app.ui.viewmodel.MainViewModel


@Composable
fun MainScreen(
    navController: NavController, viewModel: MainViewModel
) {
    val ipAddress: String by viewModel.ipAddress.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InputRedirectionImage()
        IpAddressInput(ipAddress, viewModel::onIpAddressChanged)
        HorizontalDivider(thickness = 2.dp)
        DSConsoleButtons()
        HorizontalDivider(thickness = 2.dp)
        Instructions()
        HorizontalDivider(thickness = 2.dp)
        Credits()
    }
}