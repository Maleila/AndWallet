package hu.ait.andwallet.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import hu.ait.andwallet.data.MoneyItem
import hu.ait.andwallet.data.MoneyType

class WalletViewModel: ViewModel() {
    private var _moneyList = mutableStateListOf<MoneyItem>()

    fun getAllItems(): List<MoneyItem> {
        return _moneyList
    }

    fun getExpenses(): Int {
        var expense = 0
        _moneyList.forEach {
            if (it.type == MoneyType.EXPENSE) {
                expense += it.amount
            }
        }
        return expense
    }

    fun getIncome(): Int {
        var income = 0
        _moneyList.forEach {
            if (it.type == MoneyType.INCOME) {
                income += it.amount
            }
        }
        return income
    }

    fun addToMoneyList(moneyItem: MoneyItem) {
        _moneyList.add(moneyItem)
    }

    fun removeItem(moneyItem: MoneyItem) {
        _moneyList.remove(moneyItem)
    }

    fun editMoneyItem(originalMoneyItem: MoneyItem, editedMoneyItem: MoneyItem) {
        val index = _moneyList.indexOf(originalMoneyItem)
        _moneyList[index] = editedMoneyItem
    }

    fun clearAllItems() {
        _moneyList.clear()
    }
}