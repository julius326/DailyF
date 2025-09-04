package com.example.abcd.ui.theme.SCREENS

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.abcd.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.abcd.data.AuthViewModel
import com.example.abcd.navigations.ROUTE_HOME
import com.example.abcd.navigations.ROUTE_LOGIN
import com.example.abcd.navigations.ROUTE_REGISTER


//@Composable
//fun RegisterScreen(navController: NavHostController) {
//    val authViewModel: AuthViewModel = viewModel()  // ✅ ViewModel instance
//
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmpassword by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf<String?>(null) } // ✅ Add this
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color(0xFFADD8E6)),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "Register Here",
//            fontSize = 50.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF800080),
//            fontFamily = FontFamily.Cursive
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        OutlinedTextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text(text = "Fullname") },
//            shape = RoundedCornerShape(50),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp),
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = "person icon"
//                )
//            }
//        )
//
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text(text = "email address") },
//            shape = RoundedCornerShape(50),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp),
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Email,
//                    contentDescription = "email icon"
//                )
//            }
//        )
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text(text = "Password") },
//            shape = RoundedCornerShape(50),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp),
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Lock,
//                    contentDescription = "password icon"
//                )
//            }
//        )
//
//        OutlinedTextField(
//            value = confirmpassword,
//            onValueChange = { confirmpassword = it },
//            label = { Text(text = "Confirm password") },
//            shape = RoundedCornerShape(50),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp),
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Lock,
//                    contentDescription = "password icon"
//                )
//            }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = {
//            if (password != confirmpassword) {
//                errorMessage = "Passwords do not match"
//                return@Button
//            }
//            if (name.isBlank() || email.isBlank() || password.isBlank() || confirmpassword.isBlank()) {
//                errorMessage = "Inputs cannot be blank"
//                return@Button
//            }
//
//            authViewModel.registerUser(
//                name = name,
//                email = email,
//                password = password,
//                onSuccess = {
//                    navController.navigate(ROUTE_HOME) {
//                        popUpTo(ROUTE_REGISTER) { inclusive = true }
//                    }
//                },
//
//                onError = { error ->
//                    errorMessage = error
//                }
//            )
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(20.dp)
//        )
//
//        {
//            Text(
//                "Register",
//
//            )
//
//        }
//
//        errorMessage?.let {
//            Text(
//                text = it,
//                color = Color.Red,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        TextButton(onClick = { navController.navigate(ROUTE_LOGIN) }) {
//            Text(
//                text = "Already have an account? LOGIN",
//                fontSize = 24.sp
//            )
//        }
//    }
//}

@Composable
fun RegisterScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFADD8E6)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register Here",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF800080),
            fontFamily = FontFamily.Cursive
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🌟 Card wrapping all text fields
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "Fullname") },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "person icon"
                        )
                    }
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email address") },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "email icon"
                        )
                    }
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Password") },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "password icon"
                        )
                    }
                )

                OutlinedTextField(
                    value = confirmpassword,
                    onValueChange = { confirmpassword = it },
                    label = { Text(text = "Confirm password") },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "password icon"
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (password != confirmpassword) {
                    errorMessage = "Passwords do not match"
                    return@Button
                }
                if (name.isBlank() || email.isBlank() || password.isBlank() || confirmpassword.isBlank()) {
                    errorMessage = "Inputs cannot be blank"
                    return@Button
                }

                authViewModel.registerUser(
                    name = name,
                    email = email,
                    password = password,
                    onSuccess = {
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(ROUTE_REGISTER) { inclusive = true }
                        }
                    },
                    onError = { error ->
                        errorMessage = error
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text("Register")
        }

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate(ROUTE_LOGIN) }) {
            Text(
                text = "Already have an account? LOGIN",
                fontSize = 20.sp
            )
        }
    }
}


@Preview
@Composable
fun RegisterScreenPreview(){
    RegisterScreen(rememberNavController())
}
