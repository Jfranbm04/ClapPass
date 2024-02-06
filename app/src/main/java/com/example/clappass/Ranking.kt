package com.example.clappass

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clappass.ui.theme.ClapPassTheme
import com.example.clappass.ui.theme.azulMolon
import com.example.clappass.ui.theme.naranjaFondo
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class Ranking : ComponentActivity() {

    val databaseReference = Firebase.database("https://clap-pass-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("Palmadas")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClapPassTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var listaPalmadas by remember { mutableStateOf(emptyList<Palmada>()) }

                    LaunchedEffect(Unit) {
                        obtenerDatosDeFirebase { palmadas ->
                            listaPalmadas = palmadas
                        }
                    }
                    val listaOrdenada = ordenarPorPuntuacionDescendente(ArrayList(listaPalmadas))
                    BackgroundP2(listaOrdenada)
                }
            }
        }
    }

}

// Funcion de kotlin que te ordena el arraylist por puntuación
fun ordenarPorPuntuacionDescendente(listaPalmadas: ArrayList<Palmada>): List<Palmada> {
    return listaPalmadas.sortedByDescending { it.puntuación }
}

fun obtenerDatosDeFirebase(onDataLoaded: (List<Palmada>) -> Unit) {
    val listaPalmadas: MutableList<Palmada> = mutableListOf()

    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (snapshot in dataSnapshot.children) {
                val nombre = snapshot.child("Usuario").getValue(String::class.java)
                val puntuacion = snapshot.child("Puntuacion").getValue(Long::class.java)

                if (nombre != null && puntuacion != null) {
                    val palmada = Palmada(nombre, puntuacion)
                    listaPalmadas.add(palmada)
                }
            }

            onDataLoaded(listaPalmadas)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Manejar el error según sea necesario
        }
    })
}

// @Preview(showBackground = true)
@Composable
fun BackgroundP2(listaPalmadas: List<Palmada>) {
    val contexto = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(naranjaFondo)
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
                    .size(30.dp)
                    .align(alignment = Alignment.Start)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .clickable {
                        (contexto as? Activity)?.finish()
                    }
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(azulMolon)
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "MEJORES PALMADAS",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(
                        color = azulMolon,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                LazyColumn {
                    items(listaPalmadas) { palmada ->
                        UserScoreCard(palmada)
                    }
                }
            }
        }
    }
}

