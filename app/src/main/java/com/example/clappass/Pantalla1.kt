package com.example.clappass

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.clappass.R
import com.example.clappass.*

// import com.example.loginspotifyinterfaces.Login.Body
// import com.example.loginspotifyinterfaces.Login.correoContraseña
// import com.example.loginspotifyinterfaces.Login.iniciaSesion



class Pantalla1 {
    @Composable
    fun BackgroundP1() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.clap_pass_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(500.dp) // Tamaño de la imagen
                        .align(alignment = Alignment.CenterHorizontally)
                )

            }


        }
    }
}
