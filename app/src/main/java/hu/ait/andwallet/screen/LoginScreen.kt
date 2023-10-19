package hu.ait.andwallet.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hu.ait.andwallet.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onNavigateToWallet: () -> Unit) {
    val code by rememberSaveable {
        mutableStateOf("1234")
    }
    var enteredCode by rememberSaveable {
        mutableStateOf("")
    }

    Column(Modifier.padding(5.dp).fillMaxWidth().fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Enter pin")
        OutlinedTextField(
            value = enteredCode,
            onValueChange = {enteredCode = it},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(text = stringResource(R.string.enter_pin_text)) })
        Button(modifier = Modifier.padding(10.dp), onClick = {
            if(enteredCode == code) {
                onNavigateToWallet()
            }
        }) {
            Text(text = stringResource(R.string.submit_btn_text))
        }
    }
}