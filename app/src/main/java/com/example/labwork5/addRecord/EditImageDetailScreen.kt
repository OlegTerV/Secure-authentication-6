package com.example.labwork5.addRecord

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.labwork5.R
import com.example.labwork5.imageDetail.ImageDetailViewModel
import com.example.labwork5.navigation.NavigationDestination


object EditImageDetailDestination : NavigationDestination {
    override val route = "editDetailRoute"
    override val title = "editDetail"
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun EditImageDetailScreen(
    uriImage: Uri,
    saveChangesAndGoBack: (String) -> Unit
) {
    val editImageDetailViewModel = EditImageDetailViewModel()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp
    val customWidthPadding = screenWidth * 0.05
    val customHeightPadding = screenHeight * 0.05

    val context = LocalContext.current
    val imageData = remember {ImageDetailViewModel.getExifData(uriImage, context)}

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Edit EXIF tags",
                modifier = Modifier.padding(innerPadding),
                fontSize = 30.sp,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Serif
            )

            OutlinedTextField(
                value = imageData["Date"].toString(),
                onValueChange = {imageData["Date"] = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text(stringResource(R.string.dateField)) },
                modifier = Modifier.padding(customWidthPadding.dp, 0.dp, customWidthPadding.dp, 0.dp).fillMaxWidth(),
                singleLine = true,
                isError = false,
                supportingText = {/*Text("Некорректный формат даты")*/}
            )

            OutlinedTextField(
                value = imageData["Lat"].toString(),
                onValueChange = {imageData["Lat"] = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text(stringResource(R.string.latField)) },
                modifier = Modifier.padding(customWidthPadding.dp, 0.dp, customWidthPadding.dp, 0.dp).fillMaxWidth(),
                singleLine = true,
                isError = false,
                supportingText = {/*Text("Некорректный формат даты")*/}
            )

            OutlinedTextField(
                value = imageData["Lon"].toString(),
                onValueChange = {imageData["Lon"] = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text(stringResource(R.string.lonField)) },
                modifier = Modifier.padding(customWidthPadding.dp, 0.dp, customWidthPadding.dp, 0.dp).fillMaxWidth(),
                singleLine = true,
                isError = false,
                supportingText = {/*Text("Некорректный формат даты")*/}
            )

            OutlinedTextField(
                value = imageData["Device"].toString(),
                onValueChange = {imageData["Device"] = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text(stringResource(R.string.deviceField)) },
                modifier = Modifier.padding(customWidthPadding.dp, 0.dp, customWidthPadding.dp, 0.dp).fillMaxWidth(),
                singleLine = true,
                isError = false,
                supportingText = {/*Text("Некорректный формат даты")*/}
            )

            OutlinedTextField(
                value = imageData["Model"].toString(),
                onValueChange = {imageData["Model"] = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text(stringResource(R.string.modelField)) },
                modifier = Modifier.padding(customWidthPadding.dp, 0.dp, customWidthPadding.dp, 0.dp).fillMaxWidth(),
                singleLine = true,
                isError = false,
                supportingText = {/*Text("Некорректный формат даты")*/}
            )

            Button(
                onClick = {
                    editImageDetailViewModel.saveNewData(imageData, context, uriImage, saveChangesAndGoBack)
                },
                modifier = Modifier.padding(0.dp, customHeightPadding.dp, 0.dp, 0.dp)
            ) {
                Text("Сохранить изменения")
            }
        }
    }
}