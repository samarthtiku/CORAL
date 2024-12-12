# 🌟 Team 1 CORAL PROJECT (Winston , Nahum , Samarth )

# 🌍 CORAL - Carbon Observation, Reduction, and Automated Lifestyle-Tracker

## 📚 Table of Contents
1. [Project Overview](#-project-overview)
2. [Problem Statement](#-problem-statement)
3. [Our Approach and Enhancements](#-our-approach-and-enhancements)
5. [Team Members](#-team-members)
6. [Functional Requirements](#-functional-requirements)
7. [Non-Functional Requirements](#-non-functional-requirements)
8. [Getting Started](#-getting-started)
9. [Dependencies](#-dependencies)
10. [Build and Run](#-build-and-run)
11. [Testing](#-testing)
12. [CI/CD](#-cicd)
13. [Code Quality](#-code-quality)
14. [Project Structure](#-project-structure)
15. [Concepts and Design Patterns](#-concepts-and-design-patterns)
16. [Requirements](#-requirements)
17. [License](#-license)

## 🌟 Project Overview
CORAL is an Android application designed to help users calculate their carbon emissions based on specific activities. It features a dropdown menu for activity selection and an automated calculation engine for estimating emissions. While personalized recommendations are planned, they are not implemented in this version.

## 🧑‍💻 Problem Statement
Despite growing environmental awareness, there is a lack of accessible tools to measure individual carbon footprints effectively. CORAL bridges this gap by providing an interface to calculate emissions for common activities like transportation, food consumption, and energy usage.

## 🚀 Our Approach and Enhancements
1. Real-time carbon emission calculations using activity-based dropdowns.
2. Modular architecture following MVVM principles for scalability and maintainability.
3. User-friendly dropdown box for selecting activity types.
4. Firebase integration for user authentication and data handling.
5. Planned future features: personalized recommendations and offline support.


## 👥 Team Members
This project was developed by:
1. **Nahum Gessesse** - [GitHub Profile](https://github.com/nahumguess)
2. **Samarth Tiku** - [GitHub Profile](https://github.com/samarthtiku)
3. **Winston Essibu** - [GitHub Profile](https://github.com/kvngwinston)

## 🎯 Original Thought Process
In the begining, decisions to use the either the platorm such as Java , or Kotlin was on debate.
We settled with kotlin because we believed in the project will be easier to execute in kotlin. 

## 🚀 Initial setbacks 
We used android studio, however, setting up android studion to work with Kotlin proved a little complex.We run into many bugs when setting up the project. Problems linked to configuration and use of libraries lead to build errors or failures to launch the app on the emulator.


## 🚀 Our Solution 
We found it easier to work with intelli J for our project,because the code integration was rather seamless and easier to work with. Intelli J allowed for simple integration with Firebase backend through the use of plugins. 


## 🔧 Getting Started

### Prerequisites
- Android Studio (latest version)
- Gradle Build System
- Firebase project setup for authentication and database

### Setup
1. Clone the repository:
   git clone https://github.com/samarthtiku/CORAL.git
2. Open the project in Android Studio
3. Configure Firebase credentials in google-services.json
4. Sync the Gradle files

## 📦 Dependencies
This project utilizes:
- Firebase for backend services (Realtime Database, Authentication)
- Kotlin as the programming language
- JUnit 5 and Mockito for testing

## 🏗️ Build and Run
Build the project using:
./gradlew build

Install the APK on an emulator or device:
./gradlew installDebug

## 🧪 Testing
Run the test suite:
./gradlew test

Unit tests and UI tests ensure the reliability of the app.

## 🔄 CI/CD
CORAL uses GitHub Actions for continuous integration. The CI pipeline:
- Builds the app
- Executes tests
- Performs static code analysis

## 📁 Project Structure
CORAL/
├── app/src/main/
│   ├── java/com/coral/
│   │   ├── activities/         # UI Components
│   │   ├── models/            # Data Models
│   │   ├── utils/             # Utility Classes
│   │   └── viewmodels/        # ViewModel Classes
│   ├── res/                   # UI Resources
│   │   ├── layout/            # Layout XML Files
│   │   ├── drawable/          # Drawables (Icons, Images)
│   │   └── values/            # Strings, Colors, Styles
└── build.gradle               # Gradle Build Script

## 🧠 Concepts and Design Patterns

### Key Patterns
- MVVM Architecture: Separation of UI, data, and business logic
- Singleton Pattern: Single instances for utilities like Firebase operations
- Repository Pattern (Planned): Abstract data operations for better maintainability

### SOLID Principles
- Single Responsibility: Each class has one responsibility
- Open/Closed: The system is open for extension but closed for modification

## 📊 Requirements

### Functional Requirements:
1. **User Authentication**: Secure login and registration using Firebase.
2. **Activity Tracking**: Calculate carbon emissions for selected activities.
3. **Dropdown Menu**: Allow users to select activity types (e.g., transportation, food).
4. **Calculation Engine**: Automatically compute emissions based on user input.
5. **Real-Time Syncing**: Update data across devices via Firebase.

### Non-Functional Requirements:
1. **Performance**: Quick response times for dropdown and calculations.
2. **Security**: Robust authentication and secure data handling.
3. **Usability**: Intuitive UI for activity selection and results display.
4. **Scalability**: Designed to handle additional activity types in the future.
5. **Maintainability**: Clean code structure adhering to SOLID principles.




## 🙏 Acknowledgments

We would like to express our gratitude to:

- The original creators of the CORAL application enjoyed applying are tecnniques to help solve problems such as this. 
- Our instructor, Dr. Konstantin Läufer ([@klaeufer](https://github.com/klaeufer)), for his invaluable guidance, support, and expertise.
- The open-source community for the various tools and libraries that made this project possible

Special thanks to Dr. Läufer for introducing us to advanced software development concepts and practice projects ( introducing us to Mars Rover Problem ).

Thank you for your interest in our CORAL project. We hope you find it useful.   



