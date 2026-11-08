# 🎓 Exam Registration App: Project Presentation

Welcome to the presentation guide for the **Exam Registration App**. This document is designed to help your team effectively present the project by walking through its architecture, workflow, key features, and live demonstration steps.

## 1. How the Application Works (Architecture & Tech Stack)

The application is a modernized Android mobile app built to streamline the process of registering for backlog and makeup exams. It replaces legacy manual processes and unreliable UPI intents with a robust, cloud-connected architecture.

**Key Technologies:**
- **UI Framework:** Kotlin with Jetpack Compose for a modern, reactive, and smooth user interface.
- **Authentication:** Firebase Authentication for secure student login and password resets.
- **Database:** Firebase Firestore for reliable, highly scalable cloud storage of registration records.
- **Payment Gateway:** Razorpay Android SDK for a seamless, secure, and integrated payment experience.
- **Document Generation:** Android's native `PdfDocument` API to generate student-specific receipt PDFs.
- **System Integration:** Android Intents for interacting with the native file system (`MediaStore`) and Email clients.

## 2. The Student Workflow

The app is mapped out logically to follow the exact journey a student takes when registering for an exam.

1. **Authentication:** The student logs into the app using their registered credentials.
2. **Student Dashboard:** Upon login, the student lands on a personalized dashboard providing quick actions, such as initiating a new "Exam Registration."
3. **Student Details & Validation:** The student inputs their details (Name, USN, Branch, Year, Semester). The app performs validation (e.g., matching the USN to a specific Regular Expression strict format).
4. **Subject Selection:** Depending on the selected branch and semester, the app dynamically loads the correct list of subjects. The student selects the subjects they wish to appear for, and the app calculates the total fee.
5. **Secure Payment:** The app summarizes the selection and launches the Razorpay Checkout flow, bypassing the need to leave the app.
6. **Cloud Confirmation:** Once the payment is marked successful, the registration data (including the Razorpay Transaction ID) is securely written to Firebase Firestore.
7. **Receipt & Sharing:** The student generates a PDF receipt. The app saves this directly to the device's Downloads folder and seamlessly queues an email intent to send the receipt to the student's logged-in email address.

## 3. Key Features to Highlight

When presenting, make sure to emphasize these standout features:

- **Dynamic, Reactive UI:** Show how selecting a different branch or semester dynamically updates the subject checklist using Compose's state management.
- **Robust Payment Processing:** Highlight the transition from a flaky UPI QR intent to a rock-solid Razorpay checkout. Explain how it handles errors and payment success callbacks natively.
- **Cloud-Synced Database:** Mention that records are no longer stored locally. Firestore ensures that the college administration can view all real-time successful registrations on the backend.
- **Automated PDF Generation:** The app doesn't just show a success screen; it procedurally generates a customized invoice PDF drawing on Android's `PdfDocument` canvas API, injecting the user's name, USN, subject codes, and transaction ID.
- **Frictionless Sharing:** The use of implicit intents automatically opens the user's email client with the PDF receipt pre-attached and the text auto-filled.

## 4. Live Demonstration Steps

Follow these steps during your live presentation to deliver a flawless demo:

> [!TIP]
> Ensure you have an active internet connection and are using details associated with your Razorpay Test API keys before starting the demo.

1. **Run the App & Login:** 
   - Open the app to the `AuthScreen`. 
   - Demonstrate logging in with an existing student account.
2. **Dashboard Navigation:** 
   - Show the dynamic welcome message on the dashboard.
   - Click on the "Exam Registration" card.
3. **Fill the Form:**
   - Enter a valid USN (e.g., `2SD21CS001`). Briefly type an invalid one to show the "Invalid USN Format" red text, then correct it.
   - Select Branch, Year, and Semester (e.g., CSE, 3rd Year, Odd). Click "Next".
4. **Select Subjects:**
   - On the `SubjectScreen`, select 2 or 3 subjects. 
   - Point out how the "Total Fee" updates in real-time based on the selection. Click "Next".
5. **Execute Payment:**
   - On the `PaymentSummary` screen, review the details.
   - Click "Pay Now" to launch the Razorpay bottom sheet.
   - Use a test card/UPI method in Razorpay to simulate a successful transaction.
6. **Post-Payment Success:**
   - Point out the "Payment Successful!" screen and the generated Transaction ID.
   - (Optional) Open the Firebase Console in your browser back drop to demonstrate the record instantly appearing in Firestore.
7. **Generate & Share Receipt:**
   - Click "Download Receipt".
   - Show the Android toast confirming it saved to `Downloads`.
   - Show the Intent popping up the Email client, and display the generated PDF attachment with the student's specific transaction details formatted beautifully in the PDF viewer.

> [!IMPORTANT]
> The PDF feature uses `MediaStore` for safe external storage. Mention that this implementation adheres to modern Android scoped storage guidelines!
