package com.example.clappass

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.PatternsCompat
import com.example.clappass.ui.theme.naranjaLogo
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth: FirebaseAuth

@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Header(Modifier.align(Alignment.TopCenter))
        Spacer(modifier = Modifier.height(50.dp))
        //Body(Modifier.align(Alignment.Center))
    }
}

@Composable
fun Header(modifier: Modifier) {
    logo()
}

@Composable
fun logo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.clap_pass_logo),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp) // Tamaño de la imagen
                .align(alignment = Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(30.dp))
        iniciaSesion()
        Spacer(modifier = Modifier.height(20.dp))
        Body()
        Spacer(modifier = Modifier.height(20.dp))
        // Divider con padding
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .height(0.5.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        correoContraseña()
    }
}

@Composable
fun correoContraseña() {
    var email by rememberSaveable { mutableStateOf("b@b.com") }
    var password by rememberSaveable { mutableStateOf("123456") }
    var isLoginEnable by rememberSaveable { mutableStateOf(false) }

    Column {
        Email(email) {
            email = it
            if (password.length > 0 && email.length > 0 && isValidEmail(email))
                isLoginEnable = true
            else
                isLoginEnable = false
        }
        Spacer(modifier = Modifier.size(4.dp))
        Password(password) {
            password = it
            if (password.length > 0 && email.length > 0 && isValidEmail(email))
                isLoginEnable = true
            else
                isLoginEnable = false
        }
        Spacer(modifier = Modifier.size(32.dp))
        LoginButton(email, password)

    }
}

@Composable
fun LoginButton(email: String, password: String) {

    val contexto= LocalContext.current
    Button(
        onClick = {
            try{

                val auth = FirebaseAuth.getInstance()
                // Iniciar sesión con Firebase
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Inicio de sesión exitoso
                            val intent = Intent(contexto, PantallaPrincipal::class.java)
                            Toast.makeText(contexto, "Sesión iniciada.", Toast.LENGTH_SHORT).show()
                            contexto.startActivity(intent)

                        } else {
                            Log.i("Jorge guapo", "Log in with " + email + " with " + password
                                    + " failed with reason " + task.exception)
                            Toast.makeText(contexto, ""+task.exception, Toast.LENGTH_SHORT).show()


                            // Usuario no encontrado, crea el usuario
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Usuario creado exitosamente
                                        val intent = Intent(contexto, PantallaPrincipal::class.java)
                                        contexto.startActivity(intent)
                                        Toast.makeText(contexto, "Usuario creado.", Toast.LENGTH_SHORT).show()


                                    } else {
                                        // Error en la creación del usuario
                                        Toast.makeText(
                                            contexto,
                                            "Error al crear el usuario:",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Log.i("Jorge", "" + task.exception)
                                    }
                                }

                        }

                    }

            } catch (e: Exception) {
                // Capturar cualquier excepción y mostrar un Toast
                Toast.makeText(contexto, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        colors = ButtonDefaults.buttonColors(
            disabledContentColor = Color.White,
            contentColor = Color.White,
            containerColor = naranjaLogo,
            disabledContainerColor = naranjaLogo
        )
    ) {
        Text(text = "Acceder", color = Color.White, fontSize = 25.sp, fontFamily = changoFuente,)
    }
}

@Composable
fun createUser(email: String, password: String) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Usuario creado exitosamente
                val intent = Intent(context, PantallaPrincipal::class.java)
                context.startActivity(intent)
            } else {
                // Error en la creación del usuario
                Toast.makeText(context, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(password: String, onTextChanged: (String) -> Unit) {
    var showPassword by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = password,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black),
        placeholder = { Text(text = "Contraseña") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.textFieldColors(
            // textColor = Color(0xFF000000),
            containerColor = naranjaLogo,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Email(email: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.textFieldColors(
            // textColor = Color(0xFF000000),
            containerColor = naranjaLogo
        )
    )
}

@Composable
fun iniciaSesion() {
    Text(
        text = "INICIAR SESIÓN \n EN CLAP PASS",

        modifier = Modifier
            .background(color = Color.Black)
            .border(1.dp, color = Color.Black)

            .fillMaxWidth(),
        fontSize = 30.sp,
        fontFamily = changoFuente,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White
    )
}

fun isValidEmail(email: String): Boolean {
    return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun Body() {
    Row(Modifier.fillMaxWidth()) {
        continuaCon("Google")
    }
    Row(Modifier.fillMaxWidth()) {
        continuaCon("Facebook")
    }
    Row(Modifier.fillMaxWidth()) {
        continuaCon("Apple")
    }
}

@Composable
fun continuaCon(app: String) {
    Button(
        onClick = {
            val text = "No implementado."
        },
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, color = Color(0xFF878787), shape = CircleShape)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.Black
        )
    ) {
        Text(
            text = "Continuar con $app",
            color = Color.White,
            fontSize = 16.sp,

            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
        )
    }
}