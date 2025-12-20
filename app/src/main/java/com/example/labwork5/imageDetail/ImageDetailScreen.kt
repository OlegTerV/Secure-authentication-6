package com.example.labwork5.imageDetail

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.labwork5.navigation.NavigationDestination
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object ImageDetailDestination: NavigationDestination {
    override val route = "imageDetailRoute"
    override val title = "imageDetail"
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ImageDetailScreen(
    uriImage: Uri,
    navigateToEditImageDetailScreen: (String) -> Unit
) {
    val imageDetailViewModel = ImageDetailViewModel()

    val context = LocalContext.current
    val currentImage = ImageDetailViewModel.getBitmapFormUri(uriImage, context)
    val imageData = remember {ImageDetailViewModel.getExifData(uriImage, context)}

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp
    val specificPaddings = (screenWidth * 0.03).dp

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize().padding(0.dp, 0.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                bitmap = currentImage.asImageBitmap(),
                contentDescription = "Image from gallery",
                modifier = Modifier.padding(innerPadding),
                contentScale = ContentScale.Fit
            )
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier.padding(specificPaddings, 0.dp , specificPaddings ,0.dp)
            ) {
                Row {
                    Text(
                        text = "Дата создания",
                        modifier = Modifier
                            .padding(10.dp).width((screenWidth * 0.5).dp)
                    )
                    Text(
                        text =  imageData["Date"] ?: "",
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(10.dp, 0.dp , 10.dp ,0.dp))
                Row {
                    Text(
                        text = "Широта геолокации точки съемки",
                        modifier = Modifier
                            .padding(10.dp).width((screenWidth * 0.5).dp)
                    )
                    Text(
                        text = imageData["Lat"] ?: "",
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(10.dp, 0.dp , 10.dp ,0.dp))
                Row {
                    Text(
                        text = "Долгота геолокации точки съемки",
                        modifier = Modifier
                            .padding(10.dp).width((screenWidth * 0.5).dp)
                    )
                    Text(
                        text = imageData["Lon"] ?: "",
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(10.dp, 0.dp , 10.dp ,0.dp))
                Row {
                    Text(
                        text = "Устройство создания изображения",
                        modifier = Modifier
                            .padding(10.dp).width((screenWidth * 0.5).dp)
                    )
                    Text(
                        text = imageData["Device"] ?: "",
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(10.dp, 0.dp , 10.dp ,0.dp))
                Row {
                    Text(
                        text = "Модель устройства создания изображения",
                        modifier = Modifier
                            .padding(10.dp).width((screenWidth * 0.5).dp)
                    )
                    Text(
                        text = imageData["Model"] ?: "",
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
            }
            Button(onClick = { navigateToEditImageDetailScreen(URLEncoder.encode(uriImage.toString(), StandardCharsets.UTF_8.toString())) }, modifier = Modifier.padding(innerPadding)) {
                Text("Редактировать метаданные изображения")
            }
        }
    }
}