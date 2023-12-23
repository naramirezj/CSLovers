package hu.ait.cslovers.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.cslovers.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginScreenViewModel = viewModel(),
    onLoginSucces: () -> Unit
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("natalia@ait.hu") }
    var password by rememberSaveable { mutableStateOf("123456") }
    val coroutineScope = rememberCoroutineScope()
    MaterialTheme(
       colorScheme = MaterialTheme.colorScheme.copy(
           primary = Color(0xFFE91E63), // Pink color for buttons
           onPrimary = Color.White, // White text color
           surface = Color(0xFFFFCCCB), // Pastel pink for text fields
           onSurface = Color.Black // Black text color for text fields
       )
    ) {
        Box() {
            Image(
                painter = painterResource(id = R.drawable.csloverslogo), // Replace R.drawable.csloverslogo with your actual image resource
                contentDescription = stringResource(R.string.logo_description),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .padding(top = 100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    label = {
                        Text(text = stringResource(R.string.e_mail))
                    },
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Email, null)
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    label = {
                        Text(text = stringResource(R.string.password))
                    },
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(Icons.Default.Password, null)
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            if (showPassword) {
                                Icon(Icons.Default.Visibility, null)
                            } else {
                                Icon(Icons.Default.VisibilityOff, null)
                            }
                        }
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = {
                        coroutineScope.launch {
                            val result = loginViewModel.loginUser(email, password)
                            if (result != null) {
                                onLoginSucces()
                            }
                        }
                    }) {
                        Text(text = stringResource(R.string.login))
                    }
                    OutlinedButton(onClick = {
                        loginViewModel.registerUser(email, password)
                    }) {
                        Text(text = stringResource(R.string.register))
                    }
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (loginViewModel.loginUiState) {
                    is LoginUiState.Init -> {}
                    is LoginUiState.Loading -> CircularProgressIndicator()
                    is LoginUiState.RegisterSuccess -> Text(text = stringResource(R.string.registration_ok),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.40f))
                    is LoginUiState.LoginSuccess -> Text(text = stringResource(R.string.login_ok),style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.40f))
                    is LoginUiState.Error -> Text(
                        text = stringResource(R.string.error) + "${
                            (loginViewModel.loginUiState as LoginUiState.Error).error
                        }",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.40f)
                    )
                }
            }
        }
    }
}
