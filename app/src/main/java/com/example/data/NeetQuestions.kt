package com.example.data

data class NeetQuestion(
    val id: String,
    val subject: String,
    val chapter: String,
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    val year: Int
)

object NeetQuestionBank {
    val questions = listOf(
        NeetQuestion(
            id = "BIO_01",
            subject = "Biology",
            chapter = "Cell: The Unit of Life",
            questionText = "Which of the following cell organelles is non-membrane bound?",
            options = listOf("Lysosomes", "Ribosomes", "Vacuoles", "Mitochondria"),
            correctOptionIndex = 1,
            explanation = "Ribosomes non-membrane bound organelles hote hain. Yeh prokaryotic aur eukaryotic dono cells mein paaye jaate hain aur protein synthesis ke liye crucial hain.",
            year = 2023
        ),
        NeetQuestion(
            id = "BIO_02",
            subject = "Biology",
            chapter = "The Living World",
            questionText = "Mendel selected how many contrasting traits of pea plants for his experiments?",
            options = listOf("7 pairs", "14 pairs", "4 pairs", "3 pairs"),
            correctOptionIndex = 0,
            explanation = "Mendel ne garden pea plant (Pisum sativum) ke 7 pairs of contrasting characters (yani total 14 traits) ko experimental study ke liye choose kiya tha.",
            year = 2022
        ),
        NeetQuestion(
            id = "BIO_03",
            subject = "Biology",
            chapter = "Genetics & Evolution",
            questionText = "Which of the following is an example of an autosomal dominant genetic disorder?",
            options = listOf("Myotonic dystrophy", "Hemophilia", "Sickle cell anemia", "Thalassemia"),
            correctOptionIndex = 0,
            explanation = "Myotonic dystrophy ek autosomal dominant genetic disorder hai. Hemophilia ek sex-linked recessive disease hai, jabki Sickle-cell anemia aur Thalassemia autosomal recessive disorders hain.",
            year = 2021
        ),
        NeetQuestion(
            id = "BIO_04",
            subject = "Biology",
            chapter = "Human Physiology",
            questionText = "Select the correct enzyme-substrate matches correctly interacting in digestion:",
            options = listOf("Amylase - Lactose", "Lipase - Fats", "Rennin - Starch", "Trypsin - Casein"),
            correctOptionIndex = 1,
            explanation = "Lipase enzyme lipids/fats ko di and monoglycerides aur fatty acids mein break down karta hai. Amylase starch ko breakdown karta hai.",
            year = 2023
        ),
        NeetQuestion(
            id = "BIO_05",
            subject = "Biology",
            chapter = "Plant Physiology",
            questionText = "In C4 plants, the primary carbon dioxide (CO2) acceptor in mesophyll cells is:",
            options = listOf("RuBP", "PEP (Phosphoenolpyruvate)", "Oxaloacetate", "PGA (Phosphoglyceric acid)"),
            correctOptionIndex = 1,
            explanation = "C4 plants mein mesophyll cells ke paas PEP Carboxylase enzyme hota hai jo CO2 ko Phosphoenolpyruvate (PEP) se react karwa kar 4-carbon compound Oxaloacetic Acid banata hai.",
            year = 2022
        ),
        NeetQuestion(
            id = "BIO_06",
            subject = "Biology",
            chapter = "Biotechnology",
            questionText = "The genetic material of Tobacco Mosaic Virus (TMV) is represented as:",
            options = listOf("Double stranded RNA", "Single stranded RNA", "Double stranded DNA", "Single stranded DNA"),
            correctOptionIndex = 1,
            explanation = "TMV ek plant virus hai jisme genetic material single-stranded RNA (ssRNA) ke form mein maujood hota hai.",
            year = 2021
        ),
        NeetQuestion(
            id = "BIO_07",
            subject = "Biology",
            chapter = "Ecology",
            questionText = "Which organism acts as the primary consumer in a standard cropland or grassland ecosystem?",
            options = listOf("Grasshopper", "Frog", "Snake", "Hawk"),
            correctOptionIndex = 0,
            explanation = "Grasshopper primary consumer (herbivore) hai kyunki yeh seedhe primary producer (grass/plants) ko feed karta hai.",
            year = 2023
        ),
        NeetQuestion(
            id = "BIO_08",
            subject = "Biology",
            chapter = "Cell Cycle & Division",
            questionText = "In which specific stage of Prophase-I of Meiosis does crossing over take place?",
            options = listOf("Leptotene", "Zygotene", "Pachytene", "Diplotene"),
            correctOptionIndex = 2,
            explanation = "Crossing over homologous chromosomes ke non-sister chromatids ke beech mein Pachytene stage mein hota hai, recombination nodule ki help se.",
            year = 2023
        ),
        NeetQuestion(
            id = "CHEM_01",
            subject = "Chemistry",
            chapter = "Chemical Bonding",
            questionText = "Which of the following organic or inorganic molecules exhibits a zero dipole moment?",
            options = listOf("BF3", "NH3", "NF3", "H2O"),
            correctOptionIndex = 0,
            explanation = "BF3 symmetrical planar molecule (sp2 hybridized) hai jiske teenon B-F bond dipoles aapas mein complete cancel out ho jaate hain, resulting in Net dipole moment = 0.",
            year = 2023
        ),
        NeetQuestion(
            id = "CHEM_02",
            subject = "Chemistry",
            chapter = "Atomic Structure",
            questionText = "What is the maximum number of electrons that can be hosted in a subshell with principal quantum numbers having l = 3?",
            options = listOf("14", "10", "6", "2"),
            correctOptionIndex = 0,
            explanation = "l = 3 represents the 'f' subshell. Total orbital count = 2l + 1 = 2(3) + 1 = 7. Each orbital hosts up to 2 electrons, so 7 * 2 = 14 maximum electrons.",
            year = 2022
        ),
        NeetQuestion(
            id = "CHEM_03",
            subject = "Chemistry",
            chapter = "Periodic Table",
            questionText = "According to IUPAC naming rules, what is the systematic name of the element with atomic number 119?",
            options = listOf("Ununennium", "Ununoctium", "Unnilennium", "Ununseptium"),
            correctOptionIndex = 0,
            explanation = "Atomic number 119 digits ke root paths: 1 = un, 1 = un, 9 = enn. End mein '-ium' lagane se 'Ununennium' banta hai, shorthand symbol: Uue.",
            year = 2022
        ),
        NeetQuestion(
            id = "CHEM_04",
            subject = "Chemistry",
            chapter = "Thermodynamics",
            questionText = "For any spontaneous chemical process, the net change in total entropy (dS_total) is:",
            options = listOf("Greater than zero", "Less than zero", "Equal to zero", "Constant Negative value"),
            correctOptionIndex = 0,
            explanation = "Spontaneous response ya reaction ke liye Second Law of Thermodynamics kehti hai ki Delta S (total) = Delta S (system) + Delta S (surrounding) must be greater than zero (> 0).",
            year = 2023
        ),
        NeetQuestion(
            id = "CHEM_05",
            subject = "Chemistry",
            chapter = "Organic Chemistry",
            questionText = "The primary organic product formed upon the acid-catalyzed hydration of propene is:",
            options = listOf("Propan-1-ol", "Propan-2-ol", "Propane gas", "Propanal aldehyde"),
            correctOptionIndex = 1,
            explanation = "Propene (CH3-CH=CH2) par water H2O ka addition Markovnikov's Rule ke rules ke coding ke anusaar hota hai, jahan stable cardocation pathway ke through Propan-2-ol (CH3-CH(OH)-CH3) banta hai.",
            year = 2022
        ),
        NeetQuestion(
            id = "PHYS_01",
            subject = "Physics",
            chapter = "Kinematics",
            questionText = "A motor vehicle covers the first half of the entire distance at velocity v1 and the balance half distance at velocity v2. The average velocity is:",
            options = listOf("(v1 + v2) / 2", "2 * v1 * v2 / (v1 + v2)", "SquareRoot(v1 * v2)", "(v1 - v2) / 2"),
            correctOptionIndex = 1,
            explanation = "Jab segments equal distances ke hote hain, tab average speed Harmonic Mean formula se calculate hoti hai: Average Speed = Total Distance / Total Time = 2 / (1/v1 + 1/v2) = 2v1v2/(v1+v2).",
            year = 2023
        ),
        NeetQuestion(
            id = "PHYS_02",
            subject = "Physics",
            chapter = "Modern Physics",
            questionText = "The de-Broglie wavelength corresponding to a mechanical particle of mass m moving with kinetic energy E is:",
            options = listOf("h / SQRT(2mE)", "h / (2mE)", "SQRT(2mE) / h", "h / (m * E)"),
            correctOptionIndex = 0,
            explanation = "de-Broglie formula: lambda = h / p. Kinetic energy E particle ke momentum p se connected hai: p = sqrt(2mE). Substitute karne par humein lambda = h / sqrt(2mE) prapt hota hai.",
            year = 2022
        ),
        NeetQuestion(
            id = "PHYS_03",
            subject = "Physics",
            chapter = "Semiconductors",
            questionText = "In a pure p-type semiconductor, the majority charge carriers are classified as:",
            options = listOf("Free valence electrons", "Holes (Vacancy sites)", "Thermal neutrons", "Bound nuclear protons"),
            correctOptionIndex = 1,
            explanation = "P-type semiconductor material mein group-13 (trivalent) impurity insert karne se excess virtual positive charge sites create hote hain jinhe holes (acceptor levels) kaha jata hai aur woh majority carrier hote hain.",
            year = 2021
        ),
        NeetQuestion(
            id = "PHYS_04",
            subject = "Physics",
            chapter = "Electrostatics",
            questionText = "A net electrostatic charge q is placed centrally at the flat open mouth of a cylindrical jar of radius R. The electrical flux of the electric field radiating through the solid walls of the jar is:",
            options = listOf("q / e0", "q / (2 * e0)", "2 * q / e0", "Strictly Zero"),
            correctOptionIndex = 1,
            explanation = "Gauss's law assume karne ke liye hum cylinder ke dusre half ko represent karte hain, total symmetrical Gaussian cylinder ban jayega. Total enclosed flux lambda_net = q/e0. To safe half jar se passing flux is half, i.e., q / (2 * e0).",
            year = 2023
        ),
        NeetQuestion(
            id = "PHYS_05",
            subject = "Physics",
            chapter = "Optics",
            questionText = "The critical angle for a transparent glass block of refractivity n = SQRT(2) aligned with air index (n0 = 1.0) is:",
            options = listOf("30 degrees", "45 degrees", "60 degrees", "90 degrees"),
            correctOptionIndex = 1,
            explanation = "Critical angle equation is: sin C = 1 / refractive_index. Yahan refractive_index = sqrt(2) diya hai, to sin C = 1/sqrt(2), so critical angle C = 45 degrees.",
            year = 2022
        ),
        NeetQuestion(
            id = "BIO_09",
            subject = "Biology",
            chapter = "Plant Kingdom",
            questionText = "Which of the following is responsible for peat formation?",
            options = listOf("Sphagnum", "Marchantia", "Riccia", "Funaria"),
            correctOptionIndex = 0,
            explanation = "Sphagnum (moss) high water-holding capacity ki wajah se peat formation (koila jaisa substance) ke liye aur trans-shipment material ki tarah useful hota hai.",
            year = 2023
        ),
        NeetQuestion(
            id = "BIO_10",
            subject = "Biology",
            chapter = "Animal Kingdom",
            questionText = "Which of the following organisms exhibits true metameric segmentation?",
            options = listOf("Pheretima is Earthworm", "Ascaris roundworm", "Planaria flatworm", "Taenia tapeworm"),
            correctOptionIndex = 0,
            explanation = "Metamerism ya true segmentation Annelida phylum (jaise Earthworm / Pheretima) mein paaya jata hai jahan internal and external segmentations synchronized hoti hain.",
            year = 2022
        ),
        NeetQuestion(
            id = "BIO_11",
            subject = "Biology",
            chapter = "Human Reproduction",
            questionText = "Capacitation of human sperm occurs in which specific site?",
            options = listOf("Epididymis", "Vas deferens", "Female reproductive tract", "Rete testis"),
            correctOptionIndex = 2,
            explanation = "Capacitation sperm ki final physiological activation process hai jo female reproductive tract (uterus / fallopian tubes) mein travel karte waqt hoti hai.",
            year = 2021
        ),
        NeetQuestion(
            id = "BIO_12",
            subject = "Biology",
            chapter = "Reproductive Health",
            questionText = "Which of the following is a classic hormone-releasing Intrauterine Device (IUD)?",
            options = listOf("Multiload 375", "LNG-20", "Lippes Loop", "Cu7"),
            correctOptionIndex = 1,
            explanation = "LNG-20 aur Progestasert modern hormone-releasing IUDs hain jo cervical mucus ko thick banakar sperm entry blocks karte hain.",
            year = 2023
        ),
        NeetQuestion(
            id = "BIO_13",
            subject = "Biology",
            chapter = "Microbes in Human Welfare",
            questionText = "Which of the following microbes is commercially used in the production of citric acid?",
            options = listOf("Aspergillus niger", "Lactobacillus", "Saccharomyces cerevisiae", "Acetobacter aceti"),
            correctOptionIndex = 0,
            explanation = "Citric acid ki industrial and commercial bulk level production ke liye filamentous fungus Aspergillus niger ka use kiya jata hai.",
            year = 2022
        ),
        NeetQuestion(
            id = "BIO_14",
            subject = "Biology",
            chapter = "Biodiversity and Conservation",
            questionText = "Which of the following is an established example of ex-situ conservation method?",
            options = listOf("Sacred groves", "National Park", "Seed Bank (or Gene Bank)", "Biosphere Reserve"),
            correctOptionIndex = 2,
            explanation = "Seed Banks, Botanical Gardens, aur Cryopreservation off-site ya ex-situ conservation ke tarike hain, jahan species ko unke natural habitat se door secure kiya jata hai.",
            year = 2023
        ),
        NeetQuestion(
            id = "BIO_15",
            subject = "Biology",
            chapter = "Biological Classification",
            questionText = "Which of the following primary groups are found in extreme saline conditions?",
            options = listOf("Archaebacteria", "Eubacteria", "Cyanobacteria", "Mycobacteria"),
            correctOptionIndex = 0,
            explanation = "Archaebacteria halophiles extreme saline and high salt content areas mein survive kar sakte hain, unki unique cell wall peptidoglycan-less structure ki wajah se.",
            year = 2021
        ),
        NeetQuestion(
            id = "BIO_16",
            subject = "Biology",
            chapter = "Chemical Coordination",
            questionText = "Which hormone is commonly designated as the emergency 'fight-or-flight' hormone?",
            options = listOf("Thyroxine", "Adrenaline (Epinephrine)", "Insulin", "Estrogen"),
            correctOptionIndex = 1,
            explanation = "Adrenaline (epinephrine) adrenal medulla gland se release hota hai jo emergency situations mein oxygen supply aur heart rate immediately boost karta hai.",
            year = 2023
        ),
        NeetQuestion(
            id = "CHEM_06",
            subject = "Chemistry",
            chapter = "Chemical Kinetics",
            questionText = "A first-order reaction has a rate constant of 0.00693 per second. Calculate the half-life of the reaction:",
            options = listOf("100 seconds", "10 seconds", "69.3 seconds", "200 seconds"),
            correctOptionIndex = 0,
            explanation = "First-order simple reaction ke liye half-life equation: t(1/2) = 0.693 / k. Isliye t(1/2) = 0.693 / 0.00693 = 100 seconds.",
            year = 2023
        ),
        NeetQuestion(
            id = "CHEM_07",
            subject = "Chemistry",
            chapter = "Solutions",
            questionText = "Which of the following represents a classic colligative property of a solution?",
            options = listOf("Boiling point temperature", "Osmotic pressure", "Absolute vapour pressure", "Freezing point temperature"),
            correctOptionIndex = 1,
            explanation = "Osmotic pressure ek colligative property hai kyunki yeh total number of solute particles par depend karti hai, na ki solute ke chemical nature par.",
            year = 2022
        ),
        NeetQuestion(
            id = "CHEM_08",
            subject = "Chemistry",
            chapter = "Electrochemistry",
            questionText = "Which of the following statements is chemically true for a standard functioning Galvanic cell?",
            options = listOf("Reduction takes place at anode", "Oxidation takes place at cathode", "Anode is negative and cathode is positive", "No active potential is generated"),
            correctOptionIndex = 2,
            explanation = "Galvanic cells automatic chemical reactions se electric current create karte hain, jisme oxidation Negative Anode par aur reduction Positive Cathode par hota hai.",
            year = 2023
        ),
        NeetQuestion(
            id = "CHEM_09",
            subject = "Chemistry",
            chapter = "Coordination Compounds",
            questionText = "What is the expected spatial structural geometry of coordination complex [Ni(CN)4]2-?",
            options = listOf("Tetrahedral structure", "Square Planar", "Octahedral geometry", "Trigonal bipyramidal"),
            correctOptionIndex = 1,
            explanation = "CN- scale par ek strong field ligand hai jo dynamic inner d-orbitals pairing forced karta hai, resulting in dsp2 hybridization and a symmetrical Square Planar structure.",
            year = 2022
        ),
        NeetQuestion(
            id = "CHEM_10",
            subject = "Chemistry",
            chapter = "Aldehydes and Ketones",
            questionText = "Which of the following organic structures undergoes automatic Cannizzaro reaction?",
            options = listOf("Acetaldehyde", "Benzaldehyde", "Acetone", "Propanal"),
            correctOptionIndex = 1,
            explanation = "Cannizzaro reaction sirf wahi aldehydes display karte hain jinme alpha-hydrogen absolute absent ho. Benzaldehyde (C6H5-CHO) ke paas zero alpha-hydrogens hote hain.",
            year = 2021
        ),
        NeetQuestion(
            id = "CHEM_11",
            subject = "Chemistry",
            chapter = "Hydrocarbons",
            questionText = "Ozonolysis of symmetrical 2-Butene molecule followed by treatment with active Zn/H2O delivers:",
            options = listOf("Two units of Acetaldehyde", "One unit of Acetone", "Two units of Formaldehyde", "One unit of Propanal"),
            correctOptionIndex = 0,
            explanation = "Ozonolysis dual bond split pattern lagata hai: CH3-CH=CH-CH3 yields dual molecules of Acetaldehyde (CH3-CHO) cleanly under reducing conditions.",
            year = 2023
        ),
        NeetQuestion(
            id = "CHEM_12",
            subject = "Chemistry",
            chapter = "Ionic Equilibrium",
            questionText = "Calculate the direct pH value of an aqueous 10^-3 M strong HCl solution:",
            options = listOf("3", "11", "7", "1.5"),
            correctOptionIndex = 0,
            explanation = "HCl is a strong monobasic acid translating to [H+] = 10^-3 M. Applying pH = -log[H+] yields -log(10^-3) = 3 precisely.",
            year = 2022
        ),
        NeetQuestion(
            id = "CHEM_13",
            subject = "Chemistry",
            chapter = "D & F Block Elements",
            questionText = "Which of the following transition metal ions remains colorless in an open aqueous solution?",
            options = listOf("Cu2+ ion", "Fe3+ ion", "Zn2+ ion", "Cr3+ ion"),
            correctOptionIndex = 2,
            explanation = "Zn2+ metal ion holds completely filled d-orbitals ([Ar] 3d10). Is vajah se isme spin d-d transition path fail hota hai aur solution colorless rehta hai.",
            year = 2023
        ),
        NeetQuestion(
            id = "PHYS_06",
            subject = "Physics",
            chapter = "Units and Dimensions",
            questionText = "The dimensional formula of Planck's constant (h) is completely identical to which physical quantity?",
            options = listOf("Linear momentum", "Angular momentum", "Active Mechanical Energy", "Power rating"),
            correctOptionIndex = 1,
            explanation = "Planck's constant (h) aur Angular momentum (L) dono ka fundamental dimensional equation is [M L^2 T^-1]. Dono complete matching dynamics follow karte hain.",
            year = 2021
        ),
        NeetQuestion(
            id = "PHYS_07",
            subject = "Physics",
            chapter = "Laws of Motion",
            questionText = "A mechanical body of mass m is moving in a line with uniform constant velocity v. What is the net force acting on the body?",
            options = listOf("mv", "0.5 * m * v^2", "Strictly Zero", "m * g"),
            correctOptionIndex = 2,
            explanation = "Newton's First Law criteria says that constant velocity translates to zero acceleration (a = 0). Since Force F = m * a, the real net force is strictly zero.",
            year = 2023
        ),
        NeetQuestion(
            id = "PHYS_08",
            subject = "Physics",
            chapter = "Work, Energy & Power",
            questionText = "If the kinetic energy of a moving solid body is scaled up by 4 times its initial state, its new momentum will scale by:",
            options = listOf("Two times (2x)", "Four times (4x)", "Eight times (8x)", "Unchanged"),
            correctOptionIndex = 0,
            explanation = "Linear momentum 'p' is related to kinetic energy 'K' as p = sqrt(2mK). Scale factor 4 under square root scales momentum by factor of sqrt(4) = 2 times.",
            year = 2022
        ),
        NeetQuestion(
            id = "PHYS_09",
            subject = "Physics",
            chapter = "Rotational Motion",
            questionText = "The moment of inertia of a flat uniform ring of mass M and radius R about its central geometric axis is given by:",
            options = listOf("M * R^2 / 2", "M * R^2", "2 * M * R^2", "M * R^2 / 4"),
            correctOptionIndex = 1,
            explanation = "Ring ka complete mass axis of rotation level se equal distance 'R' par hosted hota hai, producing Moment of inertia I = M * R^2.",
            year = 2023
        ),
        NeetQuestion(
            id = "PHYS_10",
            subject = "Physics",
            chapter = "Gravitation",
            questionText = "What is the specific value of local acceleration due to gravity (g) at the absolute center of Earth?",
            options = listOf("Infinite value", "9.8 m/sec^2", "Strictly Zero", "Double of surface value"),
            correctOptionIndex = 2,
            explanation = "Earth ke center par solid depth radius parameters ke absolute barrier reach karti hai (d = R). Formula: g_depth = g(1 - d/R) yields g_center = 0.",
            year = 2022
        ),
        NeetQuestion(
            id = "PHYS_11",
            subject = "Physics",
            chapter = "Current Electricity",
            questionText = "The electric resistance of an isotropic metallic conductor is universally inversely proportional to its:",
            options = listOf("Total wire length", "Area of cross-section", "Ambient temperature", "Material resistivity"),
            correctOptionIndex = 1,
            explanation = "Resistance relation parameters: R = rho * (L / A). Isliye electric resistance cross-sectional area (A) ke inversely proportional linked hota hai.",
            year = 2023
        ),
        NeetQuestion(
            id = "PHYS_12",
            subject = "Physics",
            chapter = "Magnetic Effects",
            questionText = "The internal magnetic field strength inside a very long straight solenoid carrying stationary current I is:",
            options = listOf("Strictly Zero", "Directly proportional to current I", "Inversely proportional to current I", "Decreasing quadratically towards center"),
            correctOptionIndex = 1,
            explanation = "Ideal Solenoid ke core center region par magnetic field is constant: B = mu_0 * n * I. This scales linearly directly proportional to the current 'I'.",
            year = 2022
        ),
        NeetQuestion(
            id = "PHYS_13",
            subject = "Physics",
            chapter = "Electromagnetic Induction",
            questionText = "What is the standard frequency rating of alternating power supplies (AC) across Indian domestic grids?",
            options = listOf("60 Hz", "50 Hz", "110 Hz", "220 Hz"),
            correctOptionIndex = 1,
            explanation = "Indian standard AC electrical grid line levels run stably with a standard frequency of 50 Hz and nominal RMS potential of 220 V.",
            year = 2021
        ),
        NeetQuestion(
            id = "PHYS_14",
            subject = "Physics",
            chapter = "Dual Nature of Matter",
            questionText = "The photoelectric threshold work function (phi) of a pure metal surface is functionally dependent upon:",
            options = listOf("Intensity of incident light stream", "Frequency of incident wave", "Nature and clean state of metal surface", "Duration of continuous exposure"),
            correctOptionIndex = 2,
            explanation = "Work function is defined as the minimum energy benchmark. This is an intrinsic material constant strictly dependent upon the unique electronic properties of the metal surface itself.",
            year = 2022
        )
    )
}

