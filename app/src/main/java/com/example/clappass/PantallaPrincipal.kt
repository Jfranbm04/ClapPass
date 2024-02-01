package com.example.clappass

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clappass.ui.theme.ClapPassTheme
import kotlinx.coroutines.delay

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.clappass.ui.theme.naranjaLogo
import com.example.clappass.ui.theme.rojoLogo
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

// Firebase Realtime Database
// val database = Firebase.database
// val palmadaRef = database.getReference("Palmadas")

//val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Palmadas")
val databaseReference = Firebase.database("https://clap-pass-default-rtdb.europe-west1.firebasedatabase.app/")
    .getReference("Palmadas")

class PantallaPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClapPassTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),

                    color = MaterialTheme.colorScheme.background,

                ) {
                    BackgroundP1()

                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun BackgroundP1() {

    var isRecording by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.clap_pass_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    .clip(shape = MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(100.dp))

            RecordButton(
                isRecording = isRecording,
                onClick = {
                    isRecording = !isRecording
                }
            )


        }
        // Botón ver ranking
        Button(
            onClick = {


            },
            modifier = Modifier
                .padding(top = 750.dp)
                .fillMaxWidth()

                ,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = rojoLogo
            )
        ) {
            Text(
                text = "VER RANKING",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
    }
}
// ----------------------------------------------------------------------------------------
@Composable
fun RecordButton(isRecording: Boolean, onClick: () -> Unit) {
    var progresoVisible by remember { mutableStateOf(false) }
    var progreso by remember { mutableStateOf(0f) }
    var showDialog by remember { mutableStateOf(false) }
    var puntuacion by remember { mutableStateOf(0) }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            progresoVisible = true
            repeat(50) {
                progreso = (it + 1) / 50f
                delay(100)
            }
            progresoVisible = false
            progreso = 0f

            showDialog = true
            delay(3000)
            showDialog = false

            // Generar puntuación al azar (entre 0 y 100)
            puntuacion = (0..100).random()

            // Palmada palmada01 = new Palmada(1,"",1)                     // Revisar identificador y nombreUsuario

            onClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopCenter)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(if (isRecording) rojoLogo else naranjaLogo)
                .clickable { onClick() }
                .padding(16.dp)
                .border(2.dp, Color.White, CircleShape)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Clear else Icons.Default.PlayArrow,
                contentDescription = "Record Button",
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.White, shape = CircleShape)
            )
        }

        if (progresoVisible) {
            LinearProgressIndicator(
                progress = progreso,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = rojoLogo
            )
            Text(
                text = "Grabando audio...",
                color = Color.White,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally),
            )
        }

        // Evaluar palmada si showDialog es true
        // Mostramos el AlertDialog si showDialog es true
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                text = {
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Evaluando palmada",
                            color = Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(
                            color = rojoLogo,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                },
                confirmButton = {}
            )
        }

        // Mostrar la puntuación después de cerrar el AlertDialog
        if (!showDialog) {
            Text(
                    text = "Puntuación: $puntuacion",
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(top = 16.dp)
                    .background(Color.Black)
                    .wrapContentSize(Alignment.Center)
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }

        // Botón añadir puntuación
        Button(
            onClick = {
                 //val nuevaPuntuacionRef = palmadaRef.push()
                //databaseReference <- Palmadas


                 anadirPuntuacion(puntuacion)


            },
            modifier = Modifier
                .padding(30.dp)

                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = naranjaLogo
            )
        ) {
            Text(
                text = "Añadir puntuación a la base de datos",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }



    }
}


// ----------------------------------------------------------------------------

fun anadirPuntuacion(puntuacion: Int) {
    Log.i("MyApp", "puntiuacion: " + puntuacion);
    val puntuacionReferencia = databaseReference.child("Palmada05").child("puntuacion")
    puntuacionReferencia.setValue(puntuacion)
}

/*
fun añadirPuntuacion(palmada01: Palmada) {      // Revisar
    val key = databaseReference.push().key
    if(key != null){
        databaseReference.child(key).setValue(palmada01)
    }
}
*/






