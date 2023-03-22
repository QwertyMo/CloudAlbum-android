package ru.kettuproj.cloudalbum.ui.component.alert

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.kettuproj.cloudalbum.R
import ru.kettuproj.cloudalbum.ui.theme.CloudAlbumTheme

@Composable
fun DialogWindow(
    onClose: () -> Unit = {},
    properties: DialogProperties = DialogProperties(),
    title: String? = null,
    icon: Painter? = null,
    text: String? = null,
    buttonText: String = "Close"
){
    Dialog(
        onDismissRequest = {onClose()},
        properties = properties
    ){
        Card(modifier = Modifier
            .defaultMinSize(384.dp, 192.dp)){
            Column(modifier = Modifier
                .padding(16.dp)
            ) {
                if (!title.isNullOrEmpty()) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(64.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                if (!text.isNullOrEmpty()) {
                    Text(
                        text = text,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
                Box(modifier = Modifier
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center,

                ){
                    Button(
                        modifier = Modifier.defaultMinSize(96.dp, Dp.Unspecified),
                        onClick = { onClose() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = buttonText,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DialogWindowPreview(){
    CloudAlbumTheme{
        DialogWindow(
            icon = painterResource(id = R.drawable.baseline_error_outline_24),
            text = "Can't connect to server. Please, try again later",
            buttonText = "Retry"
        )
    }
}