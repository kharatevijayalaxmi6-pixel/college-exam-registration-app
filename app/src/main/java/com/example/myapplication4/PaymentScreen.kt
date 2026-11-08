package com.example.myapplication4

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PaymentScreen(
    total: Int,
    subjectCount: Int,
    subjects: List<Subject>,
    paymentSuccess: Boolean,
    transactionId: String,
    name: String,
    usn: String,
    branch: String,
    year: String,
    semester: String
) {

    val context = LocalContext.current

    LaunchedEffect(paymentSuccess) {
        if (paymentSuccess && transactionId.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val registrationData = hashMapOf(
                "name" to name,
                "usn" to usn,
                "branch" to branch,
                "year" to year,
                "semester" to semester,
                "totalFee" to total,
                "subjectCount" to subjectCount,
                "subjects" to subjects.map { "${it.name} (${it.code})" },
                "transactionId" to transactionId,
                "timestamp" to System.currentTimeMillis()
            )
            
            db.collection("registrations")
                .document(transactionId)
                .set(registrationData)
        }
    }

    if (paymentSuccess) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Payment Successful!",
                style = MaterialTheme.typography.headlineMedium,
                color = androidx.compose.ui.graphics.Color.Green
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text("Transaction ID: $transactionId")
            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = {
                generatePdfReceipt(
                    context, transactionId, total, subjects, name, usn, branch, year, semester
                )
            }) {
                Text("Download Receipt")
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔹 TITLE
            Text(
                text = "Payment Summary",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Subjects Selected: $subjectCount")
            Text("Total Fee: ₹$total")

            Spacer(modifier = Modifier.height(20.dp))

            Text("Selected Subjects:")

            subjects.forEach {
                Text("• ${it.name}")
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔹 PAY BUTTON - TRIGGER RAZORPAY
            Button(onClick = {
                val activity = context as? MainActivity
                activity?.startPayment(total)
            }) {
                Text("Pay Now")
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@android.annotation.SuppressLint("NewApi")
fun generatePdfReceipt(
    context: android.content.Context,
    transactionId: String,
    totalFee: Int,
    subjects: List<Subject>,
    name: String,
    usn: String,
    branch: String,
    year: String,
    semester: String
) {
    val pdfDocument = android.graphics.pdf.PdfDocument()
    // Make the page taller to fit all the new data + subject list
    val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(400, 800, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    
    val paint = android.graphics.Paint().apply {
        textSize = 14f
    }
    val boldPaint = android.graphics.Paint().apply {
        textSize = 16f
        isFakeBoldText = true
    }

    var y = 50f

    canvas.drawText("EXAM REGISTRATION RECEIPT", 80f, y, boldPaint)
    y += 40f

    val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault())
    val currentDateTime = dateFormat.format(java.util.Date())

    canvas.drawText("Date & Time: $currentDateTime", 40f, y, paint)
    y += 25f
    canvas.drawText("Transaction ID: $transactionId", 40f, y, paint)
    y += 35f

    // User Details
    canvas.drawText("Student Details:", 40f, y, boldPaint)
    y += 25f
    canvas.drawText("Name: $name", 40f, y, paint)
    y += 20f
    canvas.drawText("USN: $usn", 40f, y, paint)
    y += 20f
    canvas.drawText("Branch: $branch | $year | $semester Sem", 40f, y, paint)
    y += 35f

    // Subjects
    canvas.drawText("Selected Subjects (${subjects.size}):", 40f, y, boldPaint)
    y += 25f
    for (subject in subjects) {
        canvas.drawText("• ${subject.name} (${subject.code})", 50f, y, paint)
        y += 20f
    }
    y += 15f

    canvas.drawText("Total Fee Paid: Rs. $totalFee", 40f, y, boldPaint)
    y += 40f
    
    // Final Success & Informational Messages
    paint.textSize = 14f
    paint.isFakeBoldText = true
    paint.color = android.graphics.Color.parseColor("#006400") // Dark green
    canvas.drawText("Status: SUCCESS", 40f, y, paint)
    y += 20f
    canvas.drawText("Payment Successful ✅", 40f, y, paint)
    y += 30f

    paint.color = android.graphics.Color.DKGRAY
    paint.textSize = 12f
    paint.isFakeBoldText = false
    canvas.drawText("Your exam registration is confirmed.", 40f, y, paint)
    y += 20f
    canvas.drawText("Hall Ticket will be available soon.", 40f, y, paint)
    y += 20f
    canvas.drawText("Please check your college portal.", 40f, y, paint)
    y += 20f
    canvas.drawText("Carry Hall Ticket & ID card for exam.", 40f, y, paint)
    y += 30f
    
    paint.textSize = 14f
    paint.isFakeBoldText = true
    paint.color = android.graphics.Color.BLACK
    canvas.drawText("Thank you!", 40f, y, paint)
    pdfDocument.finishPage(page)

    val contentValues = android.content.ContentValues().apply {
        put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, "ExamReceipt_$transactionId.pdf")
        put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOWNLOADS)
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    if (uri != null) {
        try {
            resolver.openOutputStream(uri)?.use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
            android.widget.Toast.makeText(context, "Receipt saved to Downloads!", android.widget.Toast.LENGTH_SHORT).show()

            // Trigger Email Intent
            val emailStr = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.email ?: ""
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(emailStr))
                putExtra(Intent.EXTRA_SUBJECT, "Exam Registration Receipt - $usn")
                putExtra(Intent.EXTRA_TEXT, "Hello $name,\n\nPlease find your exam registration receipt attached. Your transaction ID is $transactionId.\n\nur hall ticket will be generated as zoon as possinble a.")
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(emailIntent, "Send Email Receipt..."))
            
        } catch (e: Exception) {
            android.widget.Toast.makeText(context, "Failed to save or share PDF", android.widget.Toast.LENGTH_LONG).show()
        }
    } else {
        android.widget.Toast.makeText(context, "Failed to create file via MediaStore", android.widget.Toast.LENGTH_LONG).show()
    }
    
    pdfDocument.close()
}
