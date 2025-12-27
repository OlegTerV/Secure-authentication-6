package com.example.labwork5.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.runBlocking

@Composable
fun OneRecordView (
        screenWidth: Int,
        oneRecord: RecordInterface,
        homeViewModel: HomeViewModel,
        allSteps: MutableState<String>,
        dateRangeStart: MutableState<Long?>,
        dateRangeEnd: MutableState<Long?>,
        removeRecord: () -> Unit
    ) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = oneRecord.count,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(10.dp)  //.width((screenWidth * 0.5).dp)
        )
        Text(
            text = "С ${oneRecord.startDate} по ${oneRecord.endDate}",
            fontSize = 22.sp,
            modifier = Modifier.padding(10.dp).width((screenWidth * 0.5).dp)
        )
        IconButton(onClick = {
            runBlocking {homeViewModel.reduceStepsCount(oneRecord.startDateLongMilliseconds, oneRecord.endDateLongMilliseconds, oneRecord.count.toLong())}
            if (dateRangeEnd.value != null) { allSteps.value = runBlocking {homeViewModel.getData(dateRangeStart.value, dateRangeEnd.value) }}
            removeRecord()
        }) {
            Icon(
                Icons.Outlined.Delete,
                "deleteRecord",
                modifier = Modifier.size(screenWidth.dp)
            )
        }
    }
    //HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(10.dp, 0.dp , 10.dp ,0.dp))
}