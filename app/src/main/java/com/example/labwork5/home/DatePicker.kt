package com.example.labwork5.home

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(parametersList: List<Any>, homeViewModel: HomeViewModel) {
    val stateDate = rememberDateRangePickerState()

    val dateStart:  MutableState<Long?> = parametersList[0] as MutableState<Long?>
    val dateEnd: MutableState<Long?>  = parametersList[1] as MutableState<Long?>
    val modalState: MutableState<Boolean> = parametersList[2] as MutableState<Boolean>
    var allSteps: MutableState<String>? = null

    if (parametersList.size == 4){
        allSteps = parametersList[3] as MutableState<String>
    }

    DatePickerDialog(
        onDismissRequest = { modalState.value = false },
        confirmButton = {
            TextButton(
                onClick = {
                    dateStart.value = stateDate.selectedStartDateMillis
                    dateEnd.value = stateDate.selectedEndDateMillis
                    if (allSteps != null) {
                        allSteps.value = runBlocking {
                            homeViewModel.getData(
                                dateStart.value,
                                dateEnd.value
                            )
                        }
                    }
                    modalState.value = false
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                modalState.value = false
            }) {
                Text("Close")
            }
        }
    ) {
        DateRangePicker(
            state = stateDate,
            modifier = Modifier.weight(1f)
        )
    }
}