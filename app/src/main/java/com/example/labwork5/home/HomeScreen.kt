package com.example.labwork5.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labwork5.navigation.NavigationDestination
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.example.labwork5.R
import kotlinx.coroutines.runBlocking

object HomeDestination :NavigationDestination {
    override val route = "homeRoute"
    override val title = "Home"
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HomeScreen(
) {
    val homeViewModel = HomeViewModel()
    val context = LocalContext.current
    homeViewModel.initHealthConnect(context)

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp
    val specificPaddings = (screenWidth * 0.03).dp

    val openDialogRangeSelection = remember{ mutableStateOf(false) }
    val openDialogAddRange = remember{ mutableStateOf(false) }
    val openDialogRangeSelection_2= remember{ mutableStateOf(false) }
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()

    val dateRangeStart = remember {mutableStateOf<Long?>(null)}
    val dateRangeStartString = homeViewModel.convertLongToDate(dateRangeStart.value)
    val dateRangeEnd = remember {mutableStateOf<Long?>(null)}
    val dateRangeEndString = homeViewModel.convertLongToDate(dateRangeEnd.value)

    val dateRangeStart_2 = remember {mutableStateOf<Long?>(null)}
    val dateRangeStartString_2 = homeViewModel.convertLongToDate(dateRangeStart_2.value)
    val dateRangeEnd_2 = remember {mutableStateOf<Long?>(null)}
    val dateRangeEndString_2 = homeViewModel.convertLongToDate(dateRangeEnd_2.value)

    val allSteps = remember { mutableStateOf("-") }
    val countStepsAdd = remember { mutableStateOf("") }

    val recordList = remember { mutableStateListOf<RecordInterface>() }

    SnackbarHost(hostState = snackState, Modifier)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize().padding(0.dp, (screenHeight*0.01).dp, 0.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "HealthApp",
                modifier = Modifier.padding(innerPadding),
                fontSize = 50.sp,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(specificPaddings))
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier.padding(specificPaddings, 0.dp , specificPaddings ,0.dp)
            ) {
                Text(
                    text = "Всего шагов: ${allSteps.value}",
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(10.dp)  //.width((screenWidth * 0.5).dp)
                )
                if (dateRangeEnd.value != null) {
                    Text(
                        text = "В период с ${dateRangeStartString} по ${dateRangeEndString}",
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(10.dp)  //.width((screenWidth * 0.5).dp)
                    )
                }
            }

            Button(onClick = { openDialogRangeSelection.value = true }, modifier = Modifier.padding(innerPadding)) {
                Text("Выбрать период", fontSize = 22.sp)
            }

            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier.padding(specificPaddings, 0.dp , specificPaddings ,0.dp)
            ) {
                recordList.map { oneRecord ->
                        OneRecordView(
                            screenWidth,
                            oneRecord,
                            homeViewModel,
                            allSteps,
                            dateRangeStart,
                            dateRangeEnd,
                            { recordList.remove(oneRecord) } //СПИСАЛ У GEMINI
                        )
                    if(recordList.last() != oneRecord) {
                        HorizontalDivider(
                            thickness = 2.dp,
                            modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp)
                        )
                    }
                }
                /*
                OneRecordView(screenWidth, "Тестовый текст")
                HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(10.dp, 0.dp , 10.dp ,0.dp))

                OneRecordView(screenWidth, "Тестовый текст")
                HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(10.dp, 0.dp , 10.dp ,0.dp))

                OneRecordView(screenWidth, "Тестовый текст")*/
            }

            Button(onClick = { openDialogAddRange.value = true }, modifier = Modifier.padding(innerPadding)) {
                Text("Добавить запись", fontSize = 22.sp)
            }

            if (openDialogRangeSelection.value) {
                DatePicker(listOf(dateRangeStart, dateRangeEnd, openDialogRangeSelection, allSteps), homeViewModel)

                /*
                DatePickerDialog(
                    onDismissRequest = {openDialogRangeSelection.value = false},
                    confirmButton = {
                        TextButton(
                            onClick = {
                                dateRangeStart.value = stateDate.selectedStartDateMillis
                                dateRangeEnd.value = stateDate.selectedEndDateMillis
                                allSteps.value = runBlocking {homeViewModel.getData(dateRangeStart.value, dateRangeEnd.value) }
                                openDialogRangeSelection.value = false
                            }
                        ) {
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            openDialogRangeSelection.value = false
                        }){
                            Text("Close")
                        }
                    }
                ) {
                    DateRangePicker(
                        state = stateDate,
                        modifier = Modifier.weight(1f)
                    )
                }*/
            }

            if (openDialogAddRange.value) {
                Dialog(
                    onDismissRequest = {openDialogAddRange.value = false}
                ) {
                    ElevatedCard(
                        modifier = Modifier.padding(0.dp, 0.dp , 0.dp ,0.dp)
                    ) {
                        OutlinedTextField(
                            value = countStepsAdd.value,
                            onValueChange = { countStepsAdd.value = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = { Text(stringResource(R.string.typeCount)) },
                            modifier = Modifier.padding(
                                specificPaddings,
                                specificPaddings,
                                specificPaddings,
                                specificPaddings
                            ).fillMaxWidth(),
                            singleLine = true
                        )
                        Button(
                            onClick = { openDialogRangeSelection_2.value = true },
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            Text("Выбрать диапазон", fontSize = 22.sp)
                        }
                        if (dateRangeEnd_2.value != null) {
                            Text("Период с ${dateRangeStartString_2} по ${dateRangeEndString_2}")

                            Button(
                                onClick = {
                                    runBlocking {
                                        homeViewModel.addRange(
                                            dateRangeStart_2.value,
                                            dateRangeEnd_2.value,
                                            countStepsAdd.value.toLong()
                                        )
                                    }
                                    recordList.add(
                                        object : RecordInterface {
                                            override val startDate = dateRangeStartString_2
                                            override val endDate = dateRangeEndString_2
                                            override val startDateLongMilliseconds =
                                                dateRangeStart_2.value ?: 0
                                            override val endDateLongMilliseconds =
                                                dateRangeEnd_2.value ?: 0
                                            override val count = countStepsAdd.value
                                        }
                                    )
                                    if (dateRangeEnd.value != null) {
                                        allSteps.value = runBlocking {
                                            homeViewModel.getData(
                                                dateRangeStart.value,
                                                dateRangeEnd.value
                                            )
                                        }
                                    }
                                    openDialogAddRange.value = false
                                }) { Text("Сохранить", fontSize = 22.sp) }
                        }
                        if (openDialogRangeSelection_2.value) {
                            DatePicker(listOf(dateRangeStart_2, dateRangeEnd_2, openDialogRangeSelection_2), homeViewModel)
                            /*
                            DatePickerDialog(
                                onDismissRequest = { openDialogRangeSelection_2.value = false },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            dateRangeStart_2.value =
                                                stateDate_2.selectedStartDateMillis
                                            dateRangeEnd_2.value =
                                                stateDate_2.selectedEndDateMillis
                                            openDialogRangeSelection_2.value = false
                                        }
                                    ) {
                                        Text("Ok")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = {
                                        openDialogRangeSelection_2.value = false
                                    }) {
                                        Text("Close")
                                    }
                                }
                            ) {
                                DateRangePicker(
                                    state = stateDate_2,
                                    modifier = Modifier.weight(1f)
                                )
                            }*/
                        }
                    }
                }
            }
            /*
            DatePickerDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDialog.value = false
                            /*
                            snackScope.launch {
                                snackState.showSnackbar(
                                    "Selected date timestamp: ${datePickerState.selectedDateMillis}"
                                )
                            }*/
                        },
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog.value = false }) { Text("Cancel") }
                }
            ) {
                DateRangePicker(
                    state = stateDate,
                    modifier = Modifier.weight(1f)
                )
            }*/
        }
    }
}

/*
TODO(): стили для списка и окна доавбления записей (линии под последней записью быть не должно)
 */