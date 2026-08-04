package com.example.data.repository

import com.example.data.model.*

object InitialDataSeed {

    val subjects = listOf(
        Subject(
            id = "PHYS",
            name = "Physics (পদার্থবিদ্যা)",
            code = "PHYS",
            description = "WBCHSE Class 11 & 12 Physics: Mechanics, Optics, Electromagnetism & Modern Physics",
            iconName = "bolt",
            examCategory = ExamCategory.WBCHSE_12,
            totalChapters = 10,
            accentColorHex = 0xFF1E88E5
        ),
        Subject(
            id = "CHEM",
            name = "Chemistry (রসায়ন)",
            code = "CHEM",
            description = "Organic Reactions, Physical Chemistry Numericals & Inorganic Coordination Compounds",
            iconName = "science",
            examCategory = ExamCategory.WBCHSE_12,
            totalChapters = 12,
            accentColorHex = 0xFF00ACC1
        ),
        Subject(
            id = "BIO",
            name = "Biology (জীববিদ্যা)",
            code = "BIO",
            description = "Plant Physiology, Human Genetics, Ecology & Biotechnology for HS Board & NEET",
            iconName = "eco",
            examCategory = ExamCategory.WBCHSE_12,
            totalChapters = 14,
            accentColorHex = 0xFF43A047
        ),
        Subject(
            id = "MATH",
            name = "Mathematics (গণিত)",
            code = "MATH",
            description = "Calculus, Differential Equations, Vector Algebra, 3D Geometry & Probability",
            iconName = "calculate",
            examCategory = ExamCategory.WBCHSE_12,
            totalChapters = 11,
            accentColorHex = 0xFFD81B60
        ),
        Subject(
            id = "BEN",
            name = "বাংলা (Bengali Literature)",
            code = "BEN",
            description = "উচ্চমাধ্যমিক বাংলা - গল্প, কবিতা, নাটক, আন্তর্জাতিক গল্প ও ব্যাকরণ-নির্মিতি",
            iconName = "menu_book",
            examCategory = ExamCategory.WBCHSE_12,
            totalChapters = 8,
            accentColorHex = 0xFFFB8C00
        ),
        Subject(
            id = "ENG",
            name = "English (B Stream)",
            code = "ENG",
            description = "WBCHSE HS English Prose, Poetry, Grammar & Official Writing Skills",
            iconName = "translate",
            examCategory = ExamCategory.WBCHSE_12,
            totalChapters = 8,
            accentColorHex = 0xFF8E24AA
        ),
        Subject(
            id = "NEET_HUB",
            name = "NEET Medical Entrance Hub",
            code = "NEET",
            description = "NCERT Line-by-line Biology drills, Chemistry formula sheets & 2018-2025 PYQs",
            iconName = "health_and_safety",
            examCategory = ExamCategory.NEET,
            totalChapters = 15,
            accentColorHex = 0xFFE53935
        )
    )

    val chapters = listOf(
        // Physics
        Chapter(
            id = "PHYS_CH1",
            subjectId = "PHYS",
            chapterNumber = 1,
            title = "Electrostatics & Coulomb's Law (স্থির তড়িৎ)",
            subtitle = "Electric field, dipole moment, Gauss theorem & capacitance",
            isNeetHighYield = true,
            videoLecturesCount = 4,
            notesCount = 2,
            quizCount = 10,
            pyqCount = 8
        ),
        Chapter(
            id = "PHYS_CH2",
            subjectId = "PHYS",
            chapterNumber = 2,
            title = "Current Electricity & Kirchhoff's Laws (প্রবাহী তড়িৎ)",
            subtitle = "Drift velocity, Wheatstone bridge, potentiometer & meter bridge",
            isNeetHighYield = true,
            videoLecturesCount = 5,
            notesCount = 2,
            quizCount = 12,
            pyqCount = 10
        ),
        Chapter(
            id = "PHYS_CH3",
            subjectId = "PHYS",
            chapterNumber = 3,
            title = "Ray Optics & Optical Instruments (আলোর প্রতিফলন ও প্রতিসরণ)",
            subtitle = "Refraction at spherical surfaces, prisms, lenses & telescopes",
            isNeetHighYield = true,
            videoLecturesCount = 6,
            notesCount = 3,
            quizCount = 15,
            pyqCount = 12
        ),

        // Chemistry
        Chapter(
            id = "CHEM_CH1",
            subjectId = "CHEM",
            chapterNumber = 1,
            title = "Solid State & Electrochemistry (কঠিন অবস্থা ও তড়িৎ রসায়ন)",
            subtitle = "Crystal lattice, Nernst equation, Kohlrausch law & molar conductivity",
            isNeetHighYield = true,
            videoLecturesCount = 5,
            notesCount = 2,
            quizCount = 12,
            pyqCount = 9
        ),
        Chapter(
            id = "CHEM_CH2",
            subjectId = "CHEM",
            chapterNumber = 2,
            title = "Organic Haloalkanes & Haloarenes (হ্যালোঅ্যালকেন ও হ্যালোঅ্যারিন)",
            subtitle = "SN1 vs SN2 reaction mechanisms, Grignard reagents & named reactions",
            isNeetHighYield = true,
            videoLecturesCount = 6,
            notesCount = 3,
            quizCount = 15,
            pyqCount = 11
        ),

        // Biology
        Chapter(
            id = "BIO_CH1",
            subjectId = "BIO",
            chapterNumber = 1,
            title = "Genetics & Molecular Basis of Inheritance (বংশগতি ও আণবিক ভিত্তি)",
            subtitle = "Mendelian inheritance, DNA replication, transcription & translation",
            isNeetHighYield = true,
            videoLecturesCount = 6,
            notesCount = 3,
            quizCount = 15,
            pyqCount = 14
        ),
        Chapter(
            id = "BIO_CH2",
            subjectId = "BIO",
            chapterNumber = 2,
            title = "Human Reproduction & Reproductive Health (জনন স্বাস্থ্য)",
            subtitle = "Gametogenesis, menstrual cycle, IVF, ART & contraceptive methods",
            isNeetHighYield = true,
            videoLecturesCount = 4,
            notesCount = 2,
            quizCount = 12,
            pyqCount = 10
        ),

        // Mathematics
        Chapter(
            id = "MATH_CH1",
            subjectId = "MATH",
            chapterNumber = 1,
            title = "Limits, Continuity & Differentiation (সীমাবদ্ধতা ও অবকলন)",
            subtitle = "Standard limit forms, L'Hopital rule, derivative of implicit functions",
            isNeetHighYield = false,
            videoLecturesCount = 5,
            notesCount = 2,
            quizCount = 12,
            pyqCount = 10
        ),
        Chapter(
            id = "MATH_CH2",
            subjectId = "MATH",
            chapterNumber = 2,
            title = "Indefinite & Definite Integration (সমাকলন)",
            subtitle = "Integration by parts, substitution, partial fractions & properties",
            isNeetHighYield = false,
            videoLecturesCount = 7,
            notesCount = 3,
            quizCount = 15,
            pyqCount = 12
        ),

        // Bengali
        Chapter(
            id = "BEN_CH1",
            subjectId = "BEN",
            chapterNumber = 1,
            title = "গল্প: 'ভাত' - মহাশ্বেতা দেবী",
            subtitle = "উৎসব নাইয়ার জীবন সংগ্রাম, চরিত্র বিশ্লেষণ ও উচ্চমাধ্যমিক সম্ভাব্য প্রশ্নাবলী",
            isNeetHighYield = false,
            videoLecturesCount = 3,
            notesCount = 2,
            quizCount = 8,
            pyqCount = 6
        ),
        Chapter(
            id = "BEN_CH2",
            subjectId = "BEN",
            chapterNumber = 2,
            title = "কবিতা: 'রূপনারানের কূলে' - রবীন্দ্রনাথ ঠাকুর",
            subtitle = "জীবনের শেষ প্রান্তের সত্য উপলব্ধি, কাব্যিক তাৎপর্য ও শব্দার্থ",
            isNeetHighYield = false,
            videoLecturesCount = 2,
            notesCount = 2,
            quizCount = 6,
            pyqCount = 5
        ),

        // English
        Chapter(
            id = "ENG_CH1",
            subjectId = "ENG",
            chapterNumber = 1,
            title = "Prose: 'The Eyes Have It' - Ruskin Bond",
            subtitle = "Narrative technique, blind girl encounter, irony & important LAQs",
            isNeetHighYield = false,
            videoLecturesCount = 3,
            notesCount = 2,
            quizCount = 8,
            pyqCount = 7
        ),
        Chapter(
            id = "ENG_CH2",
            subjectId = "ENG",
            chapterNumber = 2,
            title = "Poetry: 'Asleep in the Valley' - Arthur Rimbaud",
            subtitle = "Anti-war poem, imagery, hummin' insects & red holes motif",
            isNeetHighYield = false,
            videoLecturesCount = 2,
            notesCount = 2,
            quizCount = 6,
            pyqCount = 5
        ),

        // NEET Special
        Chapter(
            id = "NEET_CH1",
            subjectId = "NEET_HUB",
            chapterNumber = 1,
            title = "NEET Biology High-Weightage NCERT Booster",
            subtitle = "Genetics, Cell Biology & Human Physiology Line-by-Line NCERT Questions",
            isNeetHighYield = true,
            videoLecturesCount = 8,
            notesCount = 5,
            quizCount = 25,
            pyqCount = 30
        )
    )

    val videoLectures = listOf(
        VideoLecture(
            id = "VID_PHYS_1",
            chapterId = "PHYS_CH1",
            subjectId = "PHYS",
            title = "Lecture 01: Coulomb's Law & Electric Dipole Derivation",
            durationMinutes = 42,
            tutorName = "Prof. S. N. Roy (Ex-Presidency Faculty)",
            videoUrlMock = "https://wbchse-edu.org/videos/phys_ch1_lec1.mp4",
            summaryText = "Covers Coulomb's Law in vector form, dielectric constant, electric force superposition principle, and dipole axial & equatorial field derivations for WBCHSE 5-marks question.",
            timestampsJson = "00:00 - Introduction & Vector Form | 12:10 - Dielectric Constant | 25:40 - Electric Dipole Field | 38:00 - WBCHSE Previous Year Problem"
        ),
        VideoLecture(
            id = "VID_PHYS_2",
            chapterId = "PHYS_CH1",
            subjectId = "PHYS",
            title = "Lecture 02: Gauss's Theorem & Applications",
            durationMinutes = 38,
            tutorName = "Prof. S. N. Roy",
            videoUrlMock = "https://wbchse-edu.org/videos/phys_ch1_lec2.mp4",
            summaryText = "Detailed proof of Gauss's Law, flux calculation through closed surfaces, electric field due to infinite wire and thin spherical shell.",
            timestampsJson = "00:00 - Concept of Electric Flux | 10:20 - Gauss Theorem Proof | 22:15 - Field due to Thin Sheet | 32:00 - Practice Numerical"
        ),
        VideoLecture(
            id = "VID_CHEM_1",
            chapterId = "CHEM_CH2",
            subjectId = "CHEM",
            title = "Lecture 01: SN1 vs SN2 Reaction Mechanisms in Detail",
            durationMinutes = 45,
            tutorName = "Dr. Ananya Banerjee",
            videoUrlMock = "https://wbchse-edu.org/videos/chem_ch2_lec1.mp4",
            summaryText = "Comparison between Bimolecular Substitution (SN2) and Unimolecular Substitution (SN1), inversion of configuration (Walden Inversion) and racemization.",
            timestampsJson = "00:00 - SN2 Kinetics & Transition State | 18:30 - SN1 Carbocation Stability | 32:00 - Solvent Effects & Steric Hindrance | 40:00 - HS Board Questions"
        ),
        VideoLecture(
            id = "VID_BIO_1",
            chapterId = "BIO_CH1",
            subjectId = "BIO",
            title = "Lecture 01: DNA Replication Mechanism & Transcription in Eukaryotes",
            durationMinutes = 50,
            tutorName = "Dr. Subhasis Pal (NEET Top Coach)",
            videoUrlMock = "https://wbchse-edu.org/videos/bio_ch1_lec1.mp4",
            summaryText = "Semiconservative replication proof (Meselson & Stahl experiment), Okazaki fragments, RNA Polymerase I, II, III functions, and Intron splicing.",
            timestampsJson = "00:00 - Replication Fork Dynamics | 15:40 - Meselson-Stahl Experiment | 30:20 - Transcription Bubble | 44:00 - Capping & Tailing"
        ),
        VideoLecture(
            id = "VID_MATH_1",
            chapterId = "MATH_CH2",
            subjectId = "MATH",
            title = "Lecture 01: Integration by Parts & Special Algebraic Tricks",
            durationMinutes = 48,
            tutorName = "P. K. Das Sir",
            videoUrlMock = "https://wbchse-edu.org/videos/math_ch2_lec1.mp4",
            summaryText = "Master the ILATE rule for integration by parts, shortcut formulas for integral e^x [f(x) + f'(x)] dx, and WBCHSE 4-marks standard questions.",
            timestampsJson = "00:00 - ILATE Rule Derivation | 14:20 - Standard Integrals | 28:00 - e^x [f(x) + f'(x)] Shortcut | 41:00 - Board Solved Examples"
        ),
        VideoLecture(
            id = "VID_BEN_1",
            chapterId = "BEN_CH1",
            subjectId = "BEN",
            title = "লেকাচার ০১: মহাশ্বেতা দেবীর 'ভাত' গল্পের বিস্তারিত বিশ্লেষণ",
            durationMinutes = 35,
            tutorName = "ড. অমিয় ভট্টাচার্য (উচ্চমাধ্যমিক পশ্চিমবঙ্গ পর্ষদ বিশেষজ্ঞ)",
            videoUrlMock = "https://wbchse-edu.org/videos/ben_ch1_lec1.mp4",
            summaryText = "উৎসব নাইয়ার চরিত্র, সামন্তবাদী সমাজের শোষণ চিত্র, বাদা অঞ্চল ও ভাতের গুরুত্ব এবং পরীক্ষায় ৫ নম্বরের উত্তর লেখার কৌশল।",
            timestampsJson = "০০:০০ - সূচনা ও মহাশ্বেতা দেবী | ১২:৩০ - উৎসবের চরিত্র বিশ্লেষণ | ২২:১৫ - সামন্ততান্ত্রিক শোষণ | ৩০:০০ - সম্ভাব্য বড় প্রশ্ন"
        ),
        VideoLecture(
            id = "VID_ENG_1",
            chapterId = "ENG_CH1",
            subjectId = "ENG",
            title = "Lecture 01: Comprehensive Analysis of 'The Eyes Have It'",
            durationMinutes = 36,
            tutorName = "Mrs. R. Mukherjee",
            videoUrlMock = "https://wbchse-edu.org/videos/eng_ch1_lec1.mp4",
            summaryText = "Character study of the blind narrator and the girl passenger, irony of the situational blindness, and important 6-marks Broad Questions for HS 2026.",
            timestampsJson = "00:00 - Textual Reading with Line Meaning | 14:00 - Irony of Circumstance | 24:00 - Broad Question Writing Blueprint | 32:00 - Grammar & MCQ Drill"
        )
    )

    val notes = listOf(
        ChapterNote(
            id = "NOTE_PHYS_1",
            chapterId = "PHYS_CH1",
            subjectId = "PHYS",
            title = "Electrostatics Formula Sheet & WBCHSE Board Quick Revision",
            overviewMarkdown = """
                # Electrostatics - WBCHSE Class 12 Master Notes
                
                ### 1. Coulomb's Law in Vector Form
                F = [1 / (4πε₀ ε_r)] * [(q₁ q₂) / r²] r̂
                - In vacuum: ε₀ = 8.854 × 10⁻¹² C²/N·m²
                - Dielectric constant of water K = 80
                
                ### 2. Electric Field due to Electric Dipole
                - Axial Line (r >> a): E_axis = [1 / (4πε₀)] * [2p / r³]
                - Equatorial Line (r >> a): E_eq = [1 / (4πε₀)] * [p / r³]
                - Ratio: E_axis : E_eq = 2 : 1
                
                ### 3. Gauss's Law Application
                ∮ E · dA = q_enclosed / ε₀
                - Field due to infinite plane sheet: E = σ / (2ε₀)
                - Field due to hollow spherical shell (R): 
                  - Outside (r > R): E = [1 / (4πε₀)] * [Q / r²]
                  - Inside (r < R): E = 0
            """.trimIndent(),
            keyFormulas = listOf(
                "F = (1 / 4πε₀) * (q₁q₂ / r²)",
                "p = q * 2a (Dipole Moment)",
                "Torque τ = p × E = pE sinθ",
                "Work done W = pE (cosθ₁ - cosθ₂)",
                "Capacitance C = Aε₀ / d"
            ),
            boardExamTips = "WBCHSE 5-marks question guaranteed from Electric Dipole axial/equatorial field proof OR Gauss theorem proof. Always draw clear ray/field diagrams!"
        ),
        ChapterNote(
            id = "NOTE_CHEM_1",
            chapterId = "CHEM_CH2",
            subjectId = "CHEM",
            title = "Haloalkanes & Haloarenes Reaction Mindmap",
            overviewMarkdown = """
                # Organic Chemistry: Haloalkanes Reaction Guide
                
                ### SN1 Mechanism vs SN2 Mechanism
                | Feature | SN1 Reaction | SN2 Reaction |
                |---|---|---|
                | Kinetics | 1st Order Rate = k[R-X] | 2nd Order Rate = k[R-X][Nu⁻] |
                | Intermediate | Carbocation Formation | Single Transition State |
                | Reactivity | 3° > 2° > 1° | 1° > 2° > 3° |
                | Stereochemistry | Racemization (50% Inversion + 50% Retention) | Complete Walden Inversion |
                | Solvent | Polar Protic (H₂O, EtOH) | Polar Aprotic (Acetone, DMSO) |
                
                ### Name Reactions for HS Board
                1. Finkelstein Reaction: R-Cl + NaI --(Acetone)--> R-I + NaCl
                2. Swarts Reaction: R-Br + AgF --> R-F + AgBr
                3. Wurtz-Fittig Reaction: Ar-Br + R-Br + 2Na --(Dry Ether)--> Ar-R + 2NaBr
                4. Sandmeyer Reaction: Ar-N₂⁺ Cl⁻ + CuCl/HCl --> Ar-Cl + N₂
            """.trimIndent(),
            keyFormulas = listOf(
                "SN1 Rate = k [Substrate]",
                "SN2 Rate = k [Substrate] [Nucleophile]",
                "Carbocation Stability: 3° > 2° > 1° > Methyl",
                "Grignard Synthesis: R-X + Mg -> R-MgX"
            ),
            boardExamTips = "In WBCHSE Chemistry paper, conversion questions carrying 1x3 = 3 marks frequently ask conversion from Chlorobenzene to Phenol or Aniline to Bromobenzene."
        ),
        ChapterNote(
            id = "NOTE_BIO_1",
            chapterId = "BIO_CH1",
            subjectId = "BIO",
            title = "Genetics & Molecular Biology NCERT Summary",
            overviewMarkdown = """
                # Genetics & DNA Replication - NCERT High Yield
                
                ### Central Dogma of Molecular Biology
                DNA --(Transcription)--> mRNA --(Translation)--> Protein
                *(Reverse transcription occurs in retroviruses via Reverse Transcriptase)*
                
                ### Enzymes of DNA Replication in Prokaryotes
                1. Helicase: Unwinds DNA double helix by breaking hydrogen bonds.
                2. SSBP (Single Strand Binding Protein): Prevents premature re-annealing.
                3. DNA Polymerase III: Main enzyme adding dNTPs in 5' -> 3' direction.
                4. DNA Ligase: Joins Okazaki fragments on the lagging strand.
                
                ### Genetic Code Features
                - Unambiguous & Specific: One codon codes for only one amino acid.
                - Degenerate: Most amino acids are coded by more than one codon (61 functional codons).
                - Universal: AUG codes for Methionine in bacteria as well as humans.
                - Initiation Codon: AUG (Codes for Methionine)
                - Stop Codons (Nonsense): UAA (Ochre), UAG (Amber), UGA (Opal)
            """.trimIndent(),
            keyFormulas = listOf(
                "Chargaff's Rule: [A] = [T] and [G] = [C]; [A]+[G] / [T]+[C] = 1",
                "Distance between base pairs = 0.34 nm",
                "1 Turn of DNA Helix = 3.4 nm (10 bp)",
                "Nucloeosome pitch = 200 bp wrapping histone octamer"
            ),
            boardExamTips = "For NEET & WBCHSE, memorize Lac Operon diagram (Inducer Allolactose presence vs absence) and Meselson-Stahl N-15 / N-14 density gradient centrifugation setup!"
        )
    )

    val quizQuestions = listOf(
        // Physics Quiz
        QuizQuestion(
            id = "Q_PHYS_1",
            quizId = "QUIZ_PHYS_1",
            questionText = "Two point charges +4q and +q are placed at a distance 'L' apart. At what distance from +4q on the line joining them will the net electric field be zero?",
            options = listOf("L / 3", "2L / 3", "L / 2", "3L / 4"),
            correctIndex = 1,
            explanation = "Let net E = 0 at distance x from +4q. Then k(4q)/x² = k(q)/(L - x)². Taking square root: 2/x = 1/(L - x) => 2L - 2x = x => 3x = 2L => x = 2L / 3.",
            formulaHint = "Equate E₁ = E₂ => k·q₁/x² = k·q₂/(L - x)²",
            topicTag = "Electrostatics"
        ),
        QuizQuestion(
            id = "Q_PHYS_2",
            quizId = "QUIZ_PHYS_1",
            questionText = "An electric dipole of moment 'p' is placed in a uniform electric field 'E'. What is the maximum torque experienced by the dipole?",
            options = listOf("p · E", "p / E", "2 p E", "p × E (Magnitude pE when θ = 90°)"),
            correctIndex = 3,
            explanation = "Torque vector τ = p × E. Magnitude τ = pE sinθ. Maximum torque occurs at θ = 90°, where sin(90°) = 1, so τ_max = pE.",
            formulaHint = "τ = pE sinθ",
            topicTag = "Electric Dipole"
        ),
        QuizQuestion(
            id = "Q_PHYS_3",
            quizId = "QUIZ_PHYS_1",
            questionText = "What is the equivalent capacitance between A and B when three identical capacitors each of 6 μF are connected in series?",
            options = listOf("18 μF", "2 μF", "6 μF", "9 μF"),
            correctIndex = 1,
            explanation = "For 3 identical capacitors in series: 1/C_eq = 1/C + 1/C + 1/C = 3/C => C_eq = C / 3 = 6 μF / 3 = 2 μF.",
            formulaHint = "1/C_eq = Σ (1/C_i)",
            topicTag = "Capacitance"
        ),

        // Chemistry Quiz
        QuizQuestion(
            id = "Q_CHEM_1",
            quizId = "QUIZ_CHEM_1",
            questionText = "Which alkyl halide will undergo the fastest SN1 reaction with aqueous KOH?",
            options = listOf("1-Chlorobutane", "2-Chlorobutane", "2-Chloro-2-methylpropane (tert-butyl chloride)", "1-Bromobutane"),
            correctIndex = 2,
            explanation = "SN1 reaction rate depends on the stability of the carbocation intermediate. Tertiary carbocation (3°) formed from tert-butyl chloride is hyperconjugatively most stable.",
            formulaHint = "SN1 rate ∝ Carbocation Stability (3° > 2° > 1°)",
            topicTag = "Organic Mechanisms"
        ),
        QuizQuestion(
            id = "Q_CHEM_2",
            quizId = "QUIZ_CHEM_1",
            questionText = "What is the organic product formed when Chlorobenzene is reacted with Sodium metal in dry ether (Fittig Reaction)?",
            options = listOf("Toluene", "Biphenyl (Diphenyl)", "Chlorotoluene", "Benzene"),
            correctIndex = 1,
            explanation = "2 C₆H₅Cl + 2 Na --(dry ether)--> C₆H₅-C₆H₅ (Biphenyl) + 2 NaCl. This coupling reaction between two aryl halides is called Fittig reaction.",
            formulaHint = "2 Ar-X + 2 Na -> Ar-Ar + 2 NaX",
            topicTag = "Name Reactions"
        ),

        // Biology Quiz
        QuizQuestion(
            id = "Q_BIO_1",
            quizId = "QUIZ_BIO_1",
            questionText = "If a double stranded DNA has 20% Cytosine, what percentage of Adenine is present in the DNA according to Chargaff's Rule?",
            options = listOf("20%", "30%", "40%", "60%"),
            correctIndex = 1,
            explanation = "According to Chargaff's rule, %G = %C = 20%. Therefore, %G + %C = 40%. The remaining 60% belongs to A + T. Since %A = %T, %A = 60% / 2 = 30%.",
            formulaHint = "%A = %T and %G = %C; Total = 100%",
            topicTag = "Molecular Genetics"
        ),
        QuizQuestion(
            id = "Q_BIO_2",
            quizId = "QUIZ_BIO_1",
            questionText = "Which of the following stop codons is also known as the 'Ochre' codon?",
            options = listOf("UAA", "UAG", "UGA", "AUG"),
            correctIndex = 0,
            explanation = "UAA is Ochre, UAG is Amber, and UGA is Opal. AUG is the start codon coding for Methionine.",
            formulaHint = "Stop Codons: UAA (Ochre), UAG (Amber), UGA (Opal)",
            topicTag = "Genetic Code"
        ),

        // NEET Special Quiz
        QuizQuestion(
            id = "Q_NEET_1",
            quizId = "QUIZ_NEET_1",
            questionText = "[NEET 2024 Pattern] In a plant, smooth seed coat (S) is dominant over wrinkled (s), and green pod (G) is dominant over yellow (g). What proportion of offspring from SsGg × ssgg cross will have wrinkled seeds and green pods?",
            options = listOf("1/16", "1/4 (25%)", "3/8", "1/2"),
            correctIndex = 1,
            explanation = "This is a test cross (SsGg × ssgg). The phenotypic ratio in offspring is 1:1:1:1 (25% for each phenotype: Smooth Green, Smooth Yellow, Wrinkled Green, Wrinkled Yellow). Thus, Wrinkled Green = 1/4 (25%).",
            formulaHint = "Dihybrid Test Cross Phenotypic Ratio = 1:1:1:1",
            topicTag = "Mendelian Genetics"
        )
    )

    val pyqItems = listOf(
        PyqItem(
            id = "PYQ_PHYS_2024",
            subjectId = "PHYS",
            examName = "WBCHSE HS 2024",
            year = 2024,
            questionNumber = 12,
            questionText = "State Gauss's theorem in electrostatics. Derive an expression for the electric field intensity at a point near an infinitely long straight charged wire using Gauss's theorem. (2 + 3 = 5 Marks)",
            marks = 5,
            officialAnswerText = "Statement: The total electric flux through any closed surface in free space is equal to 1/ε₀ times the net total charge enclosed inside the surface.\n\nDerivation: Consider an infinitely long wire with linear charge density λ. Choose a cylindrical Gaussian surface of radius 'r' and length 'L' coaxial with the wire.\nTotal flux Φ = ∮ E · dA = E · (2πrL).\nEnclosed charge q = λL.\nBy Gauss's theorem: E · 2πrL = λL / ε₀ => E = λ / (2π ε₀ r).",
            markingSchemeBreakdown = "Statement: 2 Marks | Gaussian surface diagram: 1 Mark | Mathematical derivation: 2 Marks"
        ),
        PyqItem(
            id = "PYQ_CHEM_2023",
            subjectId = "CHEM",
            examName = "WBCHSE HS 2023",
            year = 2023,
            questionNumber = 18,
            questionText = "(a) Write SN1 and SN2 mechanism differences in 2 points. (b) How will you convert Aniline to Bromobenzene? (2 + 3 = 5 Marks)",
            marks = 5,
            officialAnswerText = "(a) SN1 involves 2 steps with carbocation intermediate; SN2 is a single concerted step with inversion. SN1 rate = k[R-X]; SN2 rate = k[R-X][Nu].\n(b) Conversion: Aniline + NaNO₂ + HCl (0-5°C) -> Benzene Diazonium Chloride (C₆H₅N₂⁺Cl⁻). Then treat C₆H₅N₂⁺Cl⁻ with CuBr / HBr (Sandmeyer Reaction) -> Bromobenzene + N₂.",
            markingSchemeBreakdown = "SN1/SN2 differences: 2 Marks | Diazotization step: 1.5 Marks | Sandmeyer reaction step: 1.5 Marks"
        ),
        PyqItem(
            id = "PYQ_NEET_2024_1",
            subjectId = "BIO",
            examName = "NEET UG 2024",
            year = 2024,
            questionNumber = 45,
            questionText = "Which one of the following statements is INCORRECT regarding Lac Operon in E. coli?",
            marks = 4,
            officialAnswerText = "Incorrect statement: 'Lac repressor protein binds to the promoter region.'\nExplanation: The Lac repressor protein encoded by 'i' gene binds specifically to the OPERATOR region (O), not the promoter region, to inhibit RNA Polymerase transcription when lactose is absent.",
            markingSchemeBreakdown = "NEET Marking: +4 for correct option, -1 for incorrect attempt.",
            isNeet = true
        ),
        PyqItem(
            id = "PYQ_NEET_2023_1",
            subjectId = "PHYS",
            examName = "NEET UG 2023",
            year = 2023,
            questionNumber = 14,
            questionText = "A full wave rectifier circuit consists of two p-n junction diodes, a center-tapped transformer, and a load resistor. If the input AC frequency is 50 Hz, what is the ripple frequency in the output?",
            marks = 4,
            officialAnswerText = "Correct Answer: 100 Hz.\nExplanation: In a full wave rectifier, both positive and negative half cycles of the input AC are converted into unidirectional output pulses. Therefore, the output ripple frequency is twice the input frequency = 2 × 50 Hz = 100 Hz.",
            markingSchemeBreakdown = "NEET Marking: +4 for 100 Hz, -1 for 50 Hz error.",
            isNeet = true
        )
    )

    val forumPosts = listOf(
        ForumPostEntity(
            id = "POST_1",
            authorName = "Sourav Das",
            authorRole = "WBCHSE Class 12 Science",
            subjectId = "PHYS",
            category = "Doubt",
            title = "How to score full 5/5 in Electrostatics dipole numericals in HS Board?",
            content = "Friends, in Physics paper, when deriving field due to dipole at axial point, should we draw the vector direction of p and E explicitly in diagram? My school teacher cut 1 mark for missing vector sign on dipole moment p.",
            timestamp = System.currentTimeMillis() - 86400000L * 2,
            upvotes = 24,
            replyCount = 5,
            isLiked = false
        ),
        ForumPostEntity(
            id = "POST_2",
            authorName = "Priyanka Roy",
            authorRole = "NEET 2026 Aspirant (Nadia)",
            subjectId = "BIO",
            category = "NEET Strategy",
            title = "Best strategy for NCERT Biology line-by-line reading for 340+ in NEET Bio",
            content = "I am preparing for WBCHSE HS and NEET together. Biology NCERT has so many small scientist notes before chapters. Are questions asked from those scientist biographies in NEET?",
            timestamp = System.currentTimeMillis() - 86400000L,
            upvotes = 38,
            replyCount = 8,
            isLiked = true
        ),
        ForumPostEntity(
            id = "POST_3",
            authorName = "Anirban Sen",
            authorRole = "Class 11 HS Student",
            subjectId = "MATH",
            category = "Exam Prep",
            title = "Bengali & English scoring tips along with PCM Stream",
            content = "Guys, how much time do you dedicate daily for Bengali and English? Can we score 90+ in Bengali literature by studying 2 hours on weekends?",
            timestamp = System.currentTimeMillis() - 3600000L * 4,
            upvotes = 19,
            replyCount = 4,
            isLiked = false
        )
    )

    val forumReplies = listOf(
        ForumReplyEntity(
            id = 1,
            postId = "POST_1",
            authorName = "Subhajit Sir (Physics Mentor)",
            authorRole = "WBCHSE Senior Examiner",
            content = "Yes Sourav! In WBCHSE marking scheme, vector signs on p⃗ and E⃗ in the diagram carry 0.5 to 1 mark. Always write p⃗ = q × 2â along the negative to positive charge axis.",
            timestamp = System.currentTimeMillis() - 86400000L * 2 + 1800000L,
            isVerifiedSolution = true
        ),
        ForumReplyEntity(
            id = 2,
            postId = "POST_2",
            authorName = "Debolina Chanda",
            authorRole = "NEET Air 1420 (RGMCH)",
            content = "Yes Priyanka! In NEET 2023 & 2024, questions were asked directly from Ernst Mayr and Katherine Esau biographies in NCERT! Do not skip them.",
            timestamp = System.currentTimeMillis() - 86400000L + 3600000L,
            isVerifiedSolution = true
        )
    )
}
