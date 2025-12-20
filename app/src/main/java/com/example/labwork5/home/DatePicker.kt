package com.example.labwork5.home

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.runBlocking
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(parametersList: List<Any>) {
    val stateDate = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = { openDialogRangeSelection.value = false },
        confirmButton = {
            TextButton(
                onClick = {
                    dateRangeStart.value = stateDate.selectedStartDateMillis
                    dateRangeEnd.value = stateDate.selectedEndDateMillis
                    allSteps.value = runBlocking {
                        homeViewModel.getData(
                            dateRangeStart.value,
                            dateRangeEnd.value
                        )
                    }
                    openDialogRangeSelection.value = false
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                openDialogRangeSelection.value = false
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
}*/