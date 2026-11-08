package com.example.myapplication4

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SubjectScreen(subjects: List<Subject>, onProceed: (Int, Int,List<Subject>) -> Unit) {

    var total by remember { mutableStateOf(0) }
    var examType by remember{mutableStateOf("MAKEUP")}

    val selectedSubjects = remember { mutableStateMapOf<String, Boolean>() }
    val selectedList=remember{mutableStateListOf<Subject>()}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "BACKLOG/MAKEUP EXAM REGISTRATION",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text("Select Exam Type")

        Row {

            Button(
                onClick = { examType = "BACKLOG" },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (examType == "BACKLOG")
                            Color(0xFF0D47A1)
                        else
                            Color.LightGray
                )
            ) {
                Text(
                    "Backlog",
                    color = if (examType == "BACKLOG") Color.White else Color.Black
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { examType = "MAKEUP" },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (examType == "MAKEUP")
                            Color(0xFF0D47A1)
                        else
                            Color.LightGray
                )
            ) {
                Text(
                    "Make-up",
                    color = if (examType == "MAKEUP") Color.White else Color.Black
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { examType = "REVALUATION" },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (examType == "REVALUATION")
                            Color(0xFF0D47A1)
                        else
                            Color.LightGray
                )
            ) {
                Text(
                    "Revaluation",
                    color = if (examType == "REVALUATION") Color.White else Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text("Exam Type: $examType")
        LaunchedEffect(examType) {
            total = 0
            selectedList.forEach { subject ->

                val fee = when (examType) {
                    "BACKLOG" ->
                        if (subject.name.contains("Lab", true)) 1200 else 1000

                    "MAKEUP" ->
                        if (subject.name.contains("Lab", true)) 1200 else 600

                    "REVALUATION" ->
                        if (subject.name.contains("Lab", true)) 0 else 500

                    else -> 0
                }

                total += fee
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        subjects.forEach { subject ->

            val checked = selectedSubjects[subject.code] ?: false

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {

                Checkbox(
                    checked = checked,
                    enabled = !(examType == "REVALUATION" && subject.name.contains("Lab", true)),
                    onCheckedChange = {
                        selectedSubjects[subject.code] = it
                        if(it){
                            selectedList.add(subject)
                        }else{
                            selectedList.remove(subject)
                        }

                        val fee =
                            when (examType) {

                                "BACKLOG" ->
                                    if (subject.name.contains("Lab", true)) 1200 else 1000

                                "MAKEUP" ->
                                    if (subject.name.contains("Lab", true)) 1200 else 600

                                "REVALUATION" ->
                                    if (subject.name.contains("Lab", true)) 0 else 500

                                else -> 0
                            }

                        total=if(it) total+fee else total-fee

                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("${subject.name} (${subject.code})",
                    color = if (examType == "REVALUATION" && subject.name.contains("Lab", true))
                        Color.Red
                    else
                        Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Total Fee: ₹$total")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onProceed(
                    total,
                    selectedList.size,
                    selectedList.toList()
                )
            },
            enabled = selectedList.isNotEmpty()
        ) {
            Text("Proceed to Payment")
        }
    }
}
