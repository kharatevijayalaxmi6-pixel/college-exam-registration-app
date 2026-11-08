package com.example.myapplication4

data class Subject(
    val name: String,
    val code: String
)

val semesterSubjects = mapOf(

    "1-odd" to listOf(
        Subject("Mathematics 1 for CSE","22MATS11"),
        Subject("Chemistry for CSE","22CHES12"),
        Subject("Principles of Programming using C","22POP13"),
        Subject("Introduction to Electrical Engineering","22ESC142"),
        Subject("Introduction to AI","22ETC100"),
        Subject("Professional Writing Skills","22PWS16"),
        Subject("Indian Constitution","22ICO17"),
        Subject("Scientific Foundation of Health","22SFH18")
    ),

    "1-even" to listOf(
        Subject("Mathematics 2","22MATS21"),
        Subject("Physics for CSE","22PHYS22"),
        Subject("Computer Aided Engineering Drawing","22CED23"),
        Subject("Introduction to Electronics Engineering","22ESC243"),
        Subject("Python Programming","22ETC25B"),
        Subject("Communicative English","22ENG26"),
        Subject("Kannada / Balake Kannada","22KSK27"),
        Subject("Innovation and Design Thinking","22IDT28")
    ),

    "2-odd" to listOf(
        Subject("Engineering Mathematics III","22UMA301"),
        Subject("Data Structures","22UAI301"),
        Subject("Introduction to Artificial Intelligence","22UAI302"),
        Subject("Digital Systems","22UAI303"),
        Subject("Data Structures Lab","22UAIL304"),
        Subject("Digital Systems Lab","22UAIL305"),
        Subject("Operating System","22UAIC305"),
        Subject("Introduction to web technology Lab","22UAI321"),
        Subject("Universal Human Values","22UHVK306"),
    ),

    "2-even" to listOf(
        Subject("Engineering Mathematics IV","22UMA401"),
        Subject("Introduction to ML","22UAIC400"),
        Subject("DBMS","22UAIC401"),
        Subject("OOP with Java","22UAIC402"),
        Subject("DBMS Lab","22UAIL403"),
        Subject("Java Lab","22UAIL404"),
        Subject("Design & Analysis of Algorithms","22UAIC405")
    ),

    "3-odd" to listOf(
        Subject("Software Engineering","22UAIC500"),
        Subject("Theory of Computation","22UAIC501"),
        Subject("Computer Networks","22UAIC502"),
        Subject("Advanced Data Structures","22UAIE521"),
        Subject("AIML Lab","22UAIL503"),
        Subject("Computer Networks Lab","22UAIL504"),
        Subject("Minor Project I","22UAIL505"),
        Subject("Research Methodology","22UPRMK506"),
        Subject("Environmental Studies","22UVESK507")
    ),

    "3-even" to listOf(
        Subject("Deep Learning","22UAIC600"),
        Subject("Data Science & Analytics","22UAIC601"),
        Subject("Cloud Computing","22UAIC602"),
        Subject("Natural Language Processing","22UAIE621"),
        Subject("Blockchain Technology","22UAIE632"),
        Subject("Human Computer Interaction","22UAIO641"),
        Subject("DL Lab","22UAIL603"),
        Subject("DS & Analytics Lab","22UAIL604"),
        Subject("Minor Project II","22UAIL605")
    ),

    "4-odd" to listOf(
        Subject("Generative AI","22UAIC700"),
        Subject("Agentic AI","22UAIE721"),
        Subject("AI for Cybersecurity","22UAIE731"),
        Subject("Applied AI","22UAIO741"),
        Subject("Generative AI Lab","22UAIL701"),
        Subject("Major Project","22UAIL702")
    )

)

// CSE Subjects
val cseSem1 = listOf(
    "Mathematics-1",
    "Physics",
    "Basic Electrical Engineering",
    "Engineering Graphics",
    "Communicative English",
    "Physics Lab",
    "Electrical Lab"
)

val cseSem2 = listOf(
    "Mathematics-2",
    "Chemistry",
    "Basic Electronics",
    "Programming in C",
    "Mechanical Engineering",
    "Chemistry Lab",
    "C Programming Lab"
)

val cseSem3 = listOf(
    "Data Structures",
    "Discrete Mathematics",
    "Digital Logic",
    "Computer Organization",
    "Unix Programming",
    "DS Lab",
    "Digital Lab"
)

val cseSem4 = listOf(
    "Design & Analysis of Algorithms",
    "Operating Systems",
    "Database Management Systems",
    "Microcontrollers",
    "Automata Theory",
    "OS Lab",
    "DBMS Lab"
)

val cseSem5 = listOf(
    "Computer Networks",
    "Software Engineering",
    "Web Technologies",
    "Artificial Intelligence",
    "Computer Graphics",
    "CN Lab",
    "Web Lab"
)

val cseSem6 = listOf(
    "Machine Learning",
    "Compiler Design",
    "Cloud Computing",
    "Big Data Analytics",
    "Professional Elective",
    "ML Lab",
    "Mini Project"
)

val cseSem7 = listOf(
    "Cryptography & Network Security",
    "Distributed Systems",
    "Natural Language Processing",
    "Software Testing",
    "Blockchain Technology",
    "Security Lab",
    "Major Project-1"
)
