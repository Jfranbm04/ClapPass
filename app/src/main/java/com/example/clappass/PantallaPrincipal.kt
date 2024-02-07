package com.example.clappass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.clappass.ui.theme.azulMolon
import com.example.clappass.ui.theme.naranjaLogo
import com.example.clappass.ui.theme.rojoLogo
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

// Firebase Realtime Database
val databaseReference = Firebase.database("https://clap-pass-default-rtdb.europe-west1.firebasedatabase.app/")
    .getReference("Palmadas")

// Importo la fuente y la almaceno en una variable
val changoFuente = FontFamily(
    Font(R.font.chango)
)

class PantallaPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClapPassTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(azulMolon),

                    color = MaterialTheme.colorScheme.background,

                ) {
                    BackgroundP1()

                }
            }
        }
    }
}
// Método para añadir toast
fun showToast(context: Context, mensaje: String, duracion: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, mensaje, duracion).show()
}

@Preview(showBackground = true)
@Composable
fun BackgroundP1() {

    var isRecording by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("Desconocido") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(azulMolon)
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

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(rojoLogo)
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ){
                Text(
                    text = "NOMBRE DE USUARIO",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontFamily = changoFuente,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // TextField Nombre Usuario
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(rojoLogo)
                    .padding(16.dp)
            ) {

                // Espacio para escribir el nombre de usuario
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(color = Color.Transparent),

                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            RecordButton(
                isRecording = isRecording,
                username = username,
                onClick = {
                    isRecording = !isRecording
                }
            )


        }

        val contexto= LocalContext.current
        // Botón ver ranking

        Button(

            onClick = {
                val intent = Intent(contexto, Ranking::class.java)
                contexto.startActivity(intent)
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
                fontSize = 30.sp,
                fontFamily = changoFuente,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
    }



}
// ----------------------------------------------------------------------------------------
@Composable
fun RecordButton(isRecording: Boolean, onClick: () -> Unit, username : String) {
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
                    .background(azulMolon)
                    .wrapContentSize(Alignment.Center)
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontFamily = changoFuente,
                fontSize = 20.sp
            )
        }

        // Botón añadir puntuación
        val contexto= LocalContext.current

        Button(
            onClick = {
                anadirPuntuacion(username, puntuacion)
                showToast(contexto, "Puntuación añadida.");

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
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }



    }
}


// ----------------------------------------------------------------------------

var contadorPalmadas = 0  // Variable global para mantener el contador de palmadas

fun anadirPuntuacion(username: String, puntuacion: Int) {

    // Obtener el contador actual de palmadas
    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val listaPalmadas: MutableList<Palmada> = mutableListOf()

            for (snapshot in dataSnapshot.children) {
                val nombre = snapshot.child("Usuario").getValue(String::class.java)
                val puntuacion = snapshot.child("Puntuacion").getValue(Long::class.java)

                if (nombre != null && puntuacion != null) {
                    val palmada = Palmada(nombre, puntuacion)
                    listaPalmadas.add(palmada)
                }
            }

            // Incrementar el contador de palmadas
            val contadorPalmadas = listaPalmadas.size + 1
            // Formatear el contador para que tenga dos dígitos
            val contadorFormateado = String.format("%02d", contadorPalmadas)

            Log.i("Número de palmadas", "puntuacion: $puntuacion - Palmadas: $contadorFormateado")

            // Crear una nueva referencia con el contador formateado
            val puntuacionReferencia = databaseReference.child("Palmada$contadorFormateado")
            puntuacionReferencia.child("Usuario").setValue(username)
            puntuacionReferencia.child("Puntuacion").setValue(puntuacion)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Manejar el error según sea necesario
        }
    })
}









