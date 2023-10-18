package hu.ait.andwallet.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import hu.ait.andwallet.data.MoneyItem
import hu.ait.andwallet.data.MoneyType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    walletViewModel: WalletViewModel = viewModel(),
    onNavigateToSummary: (Int, Int) -> Unit
) {
    var moneyItemTitle by rememberSaveable {
        mutableStateOf("")
    }
    var moneyItemAmount by rememberSaveable {
        mutableStateOf("")
    }
    var isIncome by rememberSaveable {
        mutableStateOf(false)
    }
    var amountErrorState by rememberSaveable {
        mutableStateOf(false)
    }
    var titleErrorState by rememberSaveable {
        mutableStateOf(false)
    }
    var amountErrorText by rememberSaveable {
        mutableStateOf("")
    }
    var titleErrorText by rememberSaveable {
        mutableStateOf("")
    }

    fun validate(text: String) {
        val allDigits = text.all { char -> char.isDigit() }
        amountErrorText = "This field can be number only"
        amountErrorState = !allDigits
    }

    fun validateTitle(text: String) {
        if (text.trim() == "") {
            titleErrorState = true
            titleErrorText = "Please enter a title"
        } else {
            titleErrorState = false
        }
    }

    Column(modifier = Modifier.padding(5.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                modifier = Modifier.weight(1F, false),
                isError = titleErrorState,
                value = moneyItemTitle,
                trailingIcon = {
                    if (titleErrorState) {
                        Icon(
                            Icons.Filled.Warning, "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = {
                    moneyItemTitle = it
                    validateTitle(moneyItemTitle)},
                singleLine = true,
                label = { Text(text = "Enter type: ") })

            Spacer(modifier = Modifier.fillMaxSize(0.02f))
            OutlinedTextField(
                modifier = Modifier.weight(1F, false),
                isError = amountErrorState,
                value = moneyItemAmount,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                trailingIcon = {
                    if (amountErrorState) {
                        Icon(
                            Icons.Filled.Warning, "error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onValueChange = {
                    moneyItemAmount = it
                    validate(moneyItemAmount)},
                singleLine = true,
                label = { Text(text = "Enter amount: ") })
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (titleErrorState) {
                Text(
                    text = titleErrorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            if (amountErrorState) {
                Text(
                    text = amountErrorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isIncome, onCheckedChange = {
                isIncome = it
            })
            Text(text = "Income")
        }
        Row(modifier = Modifier.fillMaxWidth(0.8f), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = {
                if(!titleErrorState && !amountErrorState){
                    try {
                        walletViewModel.addToMoneyList(
                            MoneyItem(
                                moneyItemTitle,
                                moneyItemAmount.toInt(),
                                if (isIncome) MoneyType.INCOME else MoneyType.EXPENSE
                            )
                        )
                        //reset textfields
                        moneyItemTitle = ""
                        moneyItemAmount = ""
                    } catch (e: Exception) {
                        amountErrorState = true
                        //errorText = e.message!!
                    }
                }
            }) {
                Text(text = "Save")
            }
            Button(onClick = {
                onNavigateToSummary(
                    walletViewModel.getExpenses(),
                    walletViewModel.getIncome()
                )
            }) {
                Text(text = "Summary")
            }
            Button(onClick = {
                walletViewModel.clearAllItems()
            }) {
                Text(text = "Delete All")
            }
        }
        if (walletViewModel.getAllItems().isEmpty())
            Text(text = "No items", modifier = Modifier.padding(5.dp))
        else {
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(walletViewModel.getAllItems()) {
                    MoneyItemCard(moneyItem = it,
                        onRemoveItem = { walletViewModel.removeItem(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun MoneyItemCard(
    moneyItem: MoneyItem,
    onRemoveItem: () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = moneyItem.type.getIcon()),
                contentDescription = "Type",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 10.dp)
            )
            Column{
                Text(moneyItem.title)
                Text("$${moneyItem.amount}")
            }
            Spacer(modifier = Modifier.fillMaxSize(0.8f))
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                modifier = Modifier.clickable {
                    onRemoveItem()
                },
                tint = Color.Red
            )
        }
    }
}