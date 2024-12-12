# 📐 Architecture Decision Record (ADR)

## 🏗️ ADR 1: Layered Architecture

🟦 **Context**
We require a scalable, maintainable structure to efficiently manage CORAL's growing functionality and separate concerns across its features.

🟩 **Decision**
Adopt a layered architecture with the following layers:
1. **UI Layer** (`activities` package): Handles user interactions and displays data.
2. **Business Logic Layer** (`utils` package): Manages carbon calculations, recommendations, and validation.
3. **Data Layer** (`models` package): Responsible for managing data and synchronization.
4. **Utils Layer** (`utils` package): Provides reusable functionalities like Firebase operations and validation.

🟨 **Consequences**
- ✅ Promotes clean code and separation of concerns.
- ✅ Simplifies debugging and feature updates.
- ✅ Enhances scalability for future feature additions.
- ❌ May require initial setup effort.

---

## 🔥 ADR 2: Firebase Backend

🟦 **Context**
CORAL needs a backend for real-time data syncing and user authentication.

🟩 **Decision**
Leverage Firebase for real-time database operations, user authentication, and hosting services.

🟨 **Consequences**
- ✅ Provides scalable backend services with minimal setup.
- ✅ Facilitates real-time updates and multi-user support.
- ✅ Ensures robust authentication.
- ❌ Introduces tight coupling to Firebase services.
- ❌ Testing challenges due to direct Firebase dependencies.

---

## 🔄 ADR 3: Singleton Pattern for Utils

🟦 **Context**
Consistent access to utility services like Firebase operations is required across the app.

🟩 **Decision**
Implement utility classes like `FirebaseUtils` and `CarbonCalculator` as Kotlin objects to ensure a single instance across the app.

🟨 **Consequences**
- ✅ Prevents multiple initializations.
- ✅ Simplifies access to shared resources.
- ❌ Potential misuse due to global access.

---

## 📱 ADR 4: MVVM Architecture

🟦 **Context**
Clean separation between the UI and business logic is critical for scalability.

🟩 **Decision**
Adopt the Model-View-ViewModel (MVVM) architecture.

🟨 **Consequences**
- ✅ Improves maintainability and testability.
- ✅ Supports lifecycle-aware components.
- ❌ Currently **partially implemented**, requiring further development.
- ❌ Steeper learning curve for team members new to MVVM.

---

## 📚 ADR 5: Repository Pattern for Data Abstraction

🟦 **Context**
Abstracting database operations to improve testability and maintainability.

🟩 **Decision**
Propose the implementation of a repository layer between the ViewModel and Firebase backend.

🟨 **Consequences**
- ✅ Decouples the database and business logic.
- ✅ Facilitates local caching for offline support.
- ❌ Not currently implemented; planned for future development.
- ❌ Adds complexity to data flow.

---

## 🎨 ADR 6: Observer Pattern for Real-Time Updates

🟦 **Context**
Enable live updates for activity logs and dashboard insights.

🟩 **Decision**
Use Firebase's real-time listeners to implement the Observer Pattern in components like the `DashboardActivity`.

🟨 **Consequences**
- ✅ Provides dynamic user feedback.
- ✅ Enhances user experience with live updates.
- ❌ Increased dependency on Firebase's event system.

---

## ♻️ ADR 7: Carbon Calculation Engine

🟦 **Context**
Efficient and reusable carbon emission calculations are vital for various activities.

🟩 **Decision**
Centralize calculations within the `CarbonCalculator` utility class.

🟨 **Consequences**
- ✅ Simplifies emission calculations.
- ✅ Ensures consistency across the app.
- ❌ Requires updating logic for new activity types.

---

## 🚀 ADR 8: Recommendations Engine

🟦 **Context**
Provide actionable insights to users based on their carbon footprint.

🟩 **Decision**
Develop a `RecommendationEngine` to generate activity-based suggestions dynamically.

🟨 **Consequences**
- ✅ Personalizes user experience.
- ✅ Aligns with CORAL's goal of promoting carbon reduction.
- ❌ Dependency on accurate activity logging for meaningful recommendations.

---

## 💉 ADR 9: Dependency Injection (Planned)

🟦 **Context**
Simplify dependency management and improve modularity.

🟩 **Decision**
Propose the use of Dagger or Koin for Dependency Injection.

🟨 **Consequences**
- ✅ Improves code modularity.
- ✅ Simplifies unit testing.
- ❌ Not currently implemented; planned for future development.
- ❌ Adds initial setup complexity.

---

## 🗂️ ADR 10: Local Caching for Offline Support (Planned)

🟦 **Context**
Ensure functionality in offline scenarios.

🟩 **Decision**
Implement local caching using Room or SQLite for activity data.

🟨 **Consequences**
- ✅ Enhances app usability during network issues.
- ✅ Reduces reliance on Firebase for real-time operations.
- ❌ Not currently implemented; planned for future development.
- ❌ Increases complexity of synchronization logic.

---
