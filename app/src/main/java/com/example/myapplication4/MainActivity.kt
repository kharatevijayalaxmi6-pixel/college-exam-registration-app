package com.example.myapplication4

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication4.ui.theme.MyApplication4Theme
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class MainActivity : ComponentActivity(), PaymentResultListener {
    var totalFee = 0
    var subjectCount = 0
    
    var paymentSuccessState = mutableStateOf(false)
    var transactionIdState = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Checkout.preload(applicationContext)
        enableEdgeToEdge()
        setContent {
            MyApplication4Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MyApp()
                    }
                }
            }
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String) {
        paymentSuccessState.value = true
        transactionIdState.value = razorpayPaymentID
    }

    override fun onPaymentError(code: Int, response: String) {
        paymentSuccessState.value = false
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_LONG).show()
    }

    fun startPayment(totalAmount: Int) {
        val checkout = Checkout()
        
        // This is your Test Key. Replace this with your Live Key (rzp_live_...) 
        // after you complete the Video KYC on Razorpay's dashboard.
        checkout.setKeyID("rzp_live_Sfp5YFH6CWdmdU")
        
        try {
            val options = JSONObject()
            options.put("name", "Exam Registration")
            options.put("description", "Subject Registration Fee")
            options.put("currency", "INR")
            options.put("amount", 100) // Razorpay accepts value in paise (100 paise = 1 Rupee for testing)

            // Prefilling user details is highly recommended for live payments
            val prefill = JSONObject()
            val userEmail = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.email
            if (!userEmail.isNullOrBlank()) {
                prefill.put("email", userEmail)
            }
            // If you gather phone numbers in the future, add it here:
            // prefill.put("contact", "9876543210") 
            options.put("prefill", prefill)

            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in starting Razorpay Checkout", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplication4Theme {
        Greeting("Android")
    }
}

@Composable
fun MyApp() {
    val context = LocalContext.current
    var isLoggedIn by remember { mutableStateOf(false) }
    var inDashboard by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var usn by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }
    var selectedSemester by remember { mutableStateOf("") }
    var showNextScreen by remember { mutableStateOf(false) }
    var subjectsToShow by remember { mutableStateOf(listOf<Subject>()) }
    var showPayment by remember { mutableStateOf(false) }

    val activity = context as? MainActivity
    val paymentSuccess = activity?.paymentSuccessState?.value ?: false
    val transactionId = activity?.transactionIdState?.value ?: ""

    BackHandler {

        when {
            showPayment -> showPayment = false
            showNextScreen -> showNextScreen = false
            !inDashboard && isLoggedIn -> inDashboard = true
            else -> activity?.moveTaskToBack(true)
        }
    }

    fun isValidUSN(usn: String): Boolean {

        val upperUSN = usn.uppercase()

        val regex = Regex("^2SD\\d{2}(AI|CS|IS|CV|EC|EE|ME|CH)\\d{3}$")

        return regex.matches(upperUSN)
    }

    if (!isLoggedIn) {

        AuthScreen {
            isLoggedIn = true
            inDashboard = true
        }

    } else if (inDashboard) {

        DashboardScreen(
            onNewRegistration = { inDashboard = false },
            onLogout = { 
                isLoggedIn = false
                name = ""
                usn = ""
                branch = ""
                selectedYear = ""
                selectedSemester = ""
                showNextScreen = false
                subjectsToShow = emptyList()
                showPayment = false
            }
        )

    } else if (!showNextScreen) {

        Column(Modifier.padding(16.dp)){

            Text(
                text = "Exam Registration",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Student Name") },
                textStyle = LocalTextStyle.current.copy(color = Color.White)
            )

            OutlinedTextField(
                value = usn,
                onValueChange = { usn = it },
                label = { Text("USN") },
                textStyle = LocalTextStyle.current.copy(color = Color.White)
            )
            if (usn.isNotBlank() && !isValidUSN(usn)) {
                Text(
                    text = "Invalid USN Format",
                    color = Color.Red
                )
            }

            Text(text = "Select Branch")

            Row {
                Button(
                    onClick = { branch = "AIML" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (branch == "AIML") MaterialTheme.colorScheme.primary
                            else Color.LightGray
                    )
                ) {
                    Text("AIML")
                }

                Button(
                    onClick = { branch = "CSE" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (branch == "CSE") MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.secondary
                    )
                ){
                    Text("CSE")
                }
            }

            Text("Selected Branch: $branch")

            Text(text = "Select Year")
            Button(
                onClick = { selectedYear = "1st Year" },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (selectedYear == "1st Year")
                            MaterialTheme.colorScheme.primary
                        else
                            Color.LightGray
                )
            ) {
                Text("1st Year")
            }

            Button(
                onClick = { selectedYear = "2nd Year" },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (selectedYear == "2nd Year")
                            MaterialTheme.colorScheme.primary
                        else
                            Color.LightGray
                )
            ) {
                Text("2nd Year")
            }
            Button(
                onClick = { selectedYear = "3rd Year" },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (selectedYear == "3rd Year")
                            MaterialTheme.colorScheme.primary
                        else
                            Color.LightGray
                )
            ) {
                Text("3rd Year")
            }

            Button(
                onClick = { selectedYear = "4th Year" },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (selectedYear == "4th Year")
                            MaterialTheme.colorScheme.primary
                        else
                            Color.LightGray
                )
            ) {
                Text("4th Year")
            }


            Text(text = "Semester")
            Button(
                onClick = { selectedSemester = "Odd" },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (selectedSemester == "Odd")
                            MaterialTheme.colorScheme.primary
                        else
                            Color.LightGray
                )
            ) {
                Text("Odd Semester")
            }

            Button(
                onClick = { selectedSemester = "Even" },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (selectedSemester == "Even")
                            MaterialTheme.colorScheme.primary
                        else
                            Color.LightGray
                )
            ) {
                Text("Even Semester")
            }
            Text("Selected Year: $selectedYear")
            Text("Selected Semester: $selectedSemester")
            Button(
                onClick = {

                    val key = when (selectedYear) {
                        "1st Year" -> if (selectedSemester == "Odd") "1-odd" else "1-even"
                        "2nd Year" -> if (selectedSemester == "Odd") "2-odd" else "2-even"
                        "3rd Year" -> if (selectedSemester == "Odd") "3-odd" else "3-even"
                        "4th Year" -> if (selectedSemester == "Odd") "4-odd" else "4-even"
                        else -> ""
                    }

                    subjectsToShow =
                        if (branch.uppercase() == "AIML") {
                            semesterSubjects[key] ?: emptyList()
                        } else if (branch.uppercase() == "CSE") {

                            when (key) {
                                "1-odd" -> cseSem1.mapIndexed { i, s -> Subject(s, "CSE1O$i") }
                                "1-even" -> cseSem2.mapIndexed { i, s -> Subject(s, "CSE1E$i") }
                                "2-odd" -> cseSem3.mapIndexed { i, s -> Subject(s, "CSE2O$i") }
                                "2-even" -> cseSem4.mapIndexed { i, s -> Subject(s, "CSE2E$i") }
                                "3-odd" -> cseSem5.mapIndexed { i, s -> Subject(s, "CSE3O$i") }
                                "3-even" -> cseSem6.mapIndexed { i, s -> Subject(s, "CSE3E$i") }
                                "4-odd" -> cseSem7.mapIndexed { i, s -> Subject(s, "CSE4O$i") }
                                else -> emptyList()
                            }

                        } else emptyList()

                    showNextScreen = true

                },
                enabled = name.isNotBlank()
                        && isValidUSN(usn)
                        && branch.isNotBlank()
                        && selectedYear.isNotBlank()
                        && selectedSemester.isNotBlank()
            )
            {
                Text("Next")
            }

        }

    } else {

        var totalFee by remember { mutableStateOf(0) }
        var subjectCount by remember { mutableStateOf(0) }
        var selectedSubjectsList by remember{mutableStateOf(listOf<Subject>())}

        if (!showPayment) {

            SubjectScreen(subjectsToShow) { total, count ,list->
                totalFee = total
                subjectCount = count
                selectedSubjectsList=list
                showPayment = true
            }

        } else {
            val activity = context as? MainActivity

            activity?.let {
                PaymentScreen(
                    totalFee,
                    subjectCount,
                    selectedSubjectsList,
                    paymentSuccess,
                    transactionId,
                    name,
                    usn,
                    branch,
                    selectedYear,
                    selectedSemester
                )
            }

        }
    }

}